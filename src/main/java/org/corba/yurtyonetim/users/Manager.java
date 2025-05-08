package org.corba.yurtyonetim.users;

import java.sql.*;
import java.util.*;

public class Manager extends User {

    Scanner scanner = new Scanner(System.in);
    private final String url = "jdbc:mysql://localhost:3306/ogrenciler?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String databasePassword = "Omer200526a";

    public String tempOg_Name;
    public String tempOg_Surname;
    public String tempOg_tcNo;
    public String tempOg_telNo;
    public String tempOg_eposta;
    public String tempOg_currentDorm;
    public String tempOg_roomNo;
    public String tempOg_disiplinNo;
    public String tempOg_isOnLeave;


    private String password;

     public Manager(String email,String name,String password,String Surname,String tcNo,String telNo){
         super(email, name, Surname, tcNo,telNo);
         this.password=password;
     }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void deleteStudent(Student student){

    }

    public void addStudent(){
        String sql = "INSERT INTO ogrenci (name, surname, tcNo, telNo, eposta, currentDorm, disiplinNo, isOnLeave) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        while(true){
            System.out.println("Eklemek istediğiniz öğrencinin T.C. Kimlik Numarasını girin: ");
            tempOg_tcNo= scanner.nextLine();
            String tcNoRegex = "^[1-9][0-9]{10}$"; // 11 haneli ve 0 ile başlamayan
            if (!tempOg_tcNo.matches(tcNoRegex)) {
                System.out.println("Hata: Geçersiz TC Kimlik No! 11 haneli olmalı ve 0 ile başlamamalı.");
                continue;
            }else{
                if(tcKontrol(tempOg_tcNo)){
                    System.out.println("Aradığınız Kimlik Numarasına sahip bir öğrenci zaten vardır, tekrar girin.");
                }else{
                    break;
                }
            }
        }

        System.out.println("Eklemek istediğiniz öğrencinin adını girin: ");
        tempOg_Name= scanner.nextLine();
        System.out.println("Eklemek istediğiniz öğrencinin soyadını girin: ");
        tempOg_Surname= scanner.nextLine();


        while(true){
            System.out.println("Eklemek istediğiniz öğrencinin telefon numarasını girin: ");
            tempOg_telNo= scanner.nextLine();
            String telNoRegex = "^5[0-9]{9}$"; // 5 ile başlayan 10 haneli numara
            if (!tempOg_telNo.matches(telNoRegex)) {
                System.out.println("Hata: Geçersiz telefon numarası! 5 ile başlamalı ve 10 haneli olmalı.");
                continue;
            }else{
                if(telnoKontrol(tempOg_telNo)){
                    System.out.println("Aradığınız telefon numarasına sahip bir öğrenci zaten vardır, tekrar girin.");
                }else{
                    break;
                }
            }
        }

        while(true){
            System.out.println("Eklemek istediğiniz öğrencinin E-Posta adresini girin: ");
            tempOg_eposta= scanner.nextLine();
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!tempOg_eposta.matches(emailRegex)) {
                System.out.println("Hata: Geçersiz e-posta adresi!");
                continue;
            }else{
                if(epostakontrol(tempOg_eposta)){
                    System.out.println("Aradığınız E-Posta adresine sahip bir öğrenci zaten vardır, tekrar girin.");
                }else{
                    break;
                }
            }
        }

        Set<String> bosYurtlar = new HashSet<>();
        String sqlYurt = "SELECT * FROM yurtlar LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlYurt)) {

            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                System.out.println("Boş yer bulunan yurtlar:");
                for (int i = 1; i <= columnCount; i++) {
                    String yurtAdi = metaData.getColumnName(i);
                    int doluluk = rs.getInt(i);

                    if (doluluk < 200) {
                        bosYurtlar.add(yurtAdi);
                        System.out.println("- " + yurtAdi + " (Doluluk: " + doluluk + ")");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Yurt bilgileri alınamadı: " + e.getMessage());
        }


        while (true) {
            System.out.print("Öğrencinin kalacağı yurt adını girin: ");
            tempOg_currentDorm = scanner.nextLine().trim();

            if (bosYurtlar.contains(tempOg_currentDorm)) {
                break;
            } else {
                System.out.println("Hatalı giriş! Lütfen yukarıda listelenen yurt adlarından birini yazın.");
            }
        }


        String sqlUpdateYurt = "UPDATE yurtlar SET " + tempOg_currentDorm + " = " + tempOg_currentDorm + " + 1";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sqlUpdateYurt);

        } catch (SQLException e) {
            System.out.println("Yurt güncelleme hatası: " + e.getMessage());
        }


        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tempOg_Name);
            pstmt.setString(2, tempOg_Surname);
            pstmt.setString(3, tempOg_tcNo);
            pstmt.setString(4, tempOg_telNo);
            pstmt.setString(5, tempOg_eposta);
            pstmt.setString(6, tempOg_currentDorm);
            pstmt.setInt(7,0 );
            pstmt.setBoolean(8,false );


            pstmt.executeUpdate();
            System.out.println("Öğrenci eklendi: "+ tempOg_Name + " " + tempOg_Surname);

        } catch (SQLException e) {
            System.out.println("Ekleme hatası: " + e.getMessage());
        }

    }

    public void searchStudent() {
        System.out.println("Öğrenci arama kriterleri:");
        System.out.println("1. T.C. Kimlik No ile ara");
        System.out.println("2. Ad ile ara");
        System.out.println("3. Soyad ile ara");
        System.out.println("4. Telefon numarası ile ara");
        System.out.println("5. E-posta ile ara");
        System.out.println("6. Yurt adı ile ara");
        System.out.print("Seçiminiz (1-6): ");
        
        String secim = scanner.nextLine();
        String aramaKriteri = "";
        String sql = "";
        
        switch (secim) {
            case "1":
                System.out.print("T.C. Kimlik No: ");
                aramaKriteri = scanner.nextLine();
                sql = "SELECT * FROM ogrenci WHERE tcNo = ?";
                break;
            case "2":
                System.out.print("Ad: ");
                aramaKriteri = scanner.nextLine();
                sql = "SELECT * FROM ogrenci WHERE name LIKE ?";
                aramaKriteri = "%" + aramaKriteri + "%";
                break;
            case "3":
                System.out.print("Soyad: ");
                aramaKriteri = scanner.nextLine();
                sql = "SELECT * FROM ogrenci WHERE surname LIKE ?";
                aramaKriteri = "%" + aramaKriteri + "%";
                break;
            case "4":
                System.out.print("Telefon numarası: ");
                aramaKriteri = scanner.nextLine();
                sql = "SELECT * FROM ogrenci WHERE telNo = ?";
                break;
            case "5":
                System.out.print("E-posta: ");
                aramaKriteri = scanner.nextLine();
                sql = "SELECT * FROM ogrenci WHERE eposta = ?";
                break;
            case "6":
                System.out.print("Yurt adı: ");
                aramaKriteri = scanner.nextLine();
                sql = "SELECT * FROM ogrenci WHERE currentDorm = ?";
                break;
            default:
                System.out.println("Geçersiz seçim!");
                return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, aramaKriteri);
            ResultSet rs = pstmt.executeQuery();
            
            boolean sonucBulundu = false;
            
            while (rs.next()) {
                sonucBulundu = true;
                System.out.println("\nÖğrenci Bilgileri:");
                System.out.println("Ad: " + rs.getString("name"));
                System.out.println("Soyad: " + rs.getString("surname"));
                System.out.println("T.C. Kimlik No: " + rs.getString("tcNo"));
                System.out.println("Telefon No: " + rs.getString("telNo"));
                System.out.println("E-posta: " + rs.getString("eposta"));
                System.out.println("Yurt: " + rs.getString("currentDorm"));
                System.out.println("Disiplin Puanı: " + rs.getInt("disiplinNo"));
                System.out.println("İzinli mi: " + (rs.getBoolean("isOnLeave") ? "Evet" : "Hayır"));
                System.out.println("------------------------");
            }
            
            if (!sonucBulundu) {
                System.out.println("Arama kriterlerine uygun öğrenci bulunamadı.");
            }
            
        } catch (SQLException e) {
            System.out.println("Arama hatası: " + e.getMessage());
        }
    }
public void listStudents() {
    String sql = "SELECT * FROM ogrenci ORDER BY surname, name";

    try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        boolean ogrenciVar = false;
        int ogrenciSayisi = 0;

        System.out.println("\n=== TÜM ÖĞRENCİ LİSTESİ ===\n");
        System.out.println(String.format("%-20s %-20s %-15s %-15s %-30s %-15s %-15s %-10s",
                "Ad", "Soyad", "TC No", "Telefon", "E-posta", "Yurt", "Disiplin P.", "İzin"));
        System.out.println("-".repeat(130));

        while (rs.next()) {
            ogrenciVar = true;
            ogrenciSayisi++;

            // TC ve telefon numarasının son 4 hanesini göster, diğerlerini * ile maskele
            String maskeliTC = rs.getString("tcNo").substring(0, 7) + "****";
            String maskeliTel = "******" + rs.getString("telNo").substring(6);

            System.out.println(String.format("%-20s %-20s %-15s %-15s %-30s %-15s %-15d %-10s",
                    rs.getString("name"),
                    rs.getString("surname"),
                    maskeliTC,
                    maskeliTel,
                    rs.getString("eposta"),
                    rs.getString("currentDorm"),
                    rs.getInt("disiplinNo"),
                    rs.getBoolean("isOnLeave") ? "Evet" : "Hayır"
            ));
        }

        System.out.println("-".repeat(130));
        
        if (!ogrenciVar) {
            System.out.println("Sistemde kayıtlı öğrenci bulunmamaktadır.");
        } else {
            System.out.println("\nToplam Öğrenci Sayısı: " + ogrenciSayisi);
        }

    } catch (SQLException e) {
        System.out.println("Listeleme hatası: " + e.getMessage());
    }
}
    public void showUserInfo() {
    System.out.println("\n=== YÖNETİCİ BİLGİLERİ ===\n");
    System.out.println("Ad: " + getName());
    System.out.println("Soyad: " + getSurname());
    
    // TC Kimlik numarasını maskele (ilk 7 hane görünür, son 4 hane gizli)
    String maskedTcNo = getTcNo().substring(0, 7) + "****";
    System.out.println("T.C. Kimlik No: " + maskedTcNo);
    
    // Telefon numarasını maskele (son 4 hane görünür)
    String maskedTelNo = "******" + getTelNo().substring(6);
    System.out.println("Telefon No: " + maskedTelNo);
    
    System.out.println("E-posta: " + getEmail());
    
    // Yetki düzeyini göster
    System.out.println("Yetki Düzeyi: Yönetici");
    
    System.out.println("\nSistem Yetkileri:");
    System.out.println("- Öğrenci ekleme");
    System.out.println("- Öğrenci silme");
    System.out.println("- Öğrenci arama");
    System.out.println("- Öğrenci listeleme");
    System.out.println("- Yurt doluluk kontrolü");
}

    public boolean tcKontrol(String kontrolet) {
        String sql = "SELECT 1 FROM ogrenci WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kontrolet);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Sorgu hatası: " + e.getMessage());
            return false;
        }
    }

    public boolean telnoKontrol(String kontrolet) {
        String sql = "SELECT 1 FROM ogrenci WHERE telNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kontrolet);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Sorgu hatası: " + e.getMessage());
            return false;
        }
    }

    public boolean epostakontrol(String kontrolet) {
        String sql = "SELECT 1 FROM ogrenci WHERE eposta = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kontrolet);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Sorgu hatası: " + e.getMessage());
            return false;
        }
    }

    public boolean yurtDoluMu(String yurtAdi) {
        String sql = "SELECT " + yurtAdi + " FROM yurtlar";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int doluluk = rs.getInt(1); // yurt sütunundaki değer
                return doluluk >= 200;      // 50 oda * 4 kişi = 200 ediyor. Her yurtta maksimum 200 kişi var.
            }

        } catch (SQLException e) {
            System.out.println("Yurt kontrol hatası: " + e.getMessage());
        }

        return true; // hata durumunda "dolu" kabul edilir
    }



}//amınEvladıAi farkı ile showInfo listStudents ve searchStudent methodu yazıldı
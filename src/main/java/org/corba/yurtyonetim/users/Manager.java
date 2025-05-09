package org.corba.yurtyonetim.users;

import java.sql.*;
import java.util.*;

public class Manager extends User {

    Scanner scanner = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/kullanicilar?useSSL=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String databasePassword = "Omer200526a";

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

     public Manager(String name,String Surname,String tcNo,String telNo,String eposta,String password){
         super(name, Surname, tcNo, telNo, eposta);
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

    /*public void addStudent(){
        String sql = "INSERT INTO ogrenci (name, surname, tcNo, telNo, eposta, currentDorm, disiplinNo, isOnLeave) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        //TODO import tc kimlik , name, surname, tel no, mail

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

    }*/

    /*public void searchStudent(String tcNo) {
        String sql = "SELECT * FROM ogrenci WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tcNo);
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
    }*/

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
    
    System.out.println("E-posta: " + getEposta());
    
    // Yetki düzeyini göster
    System.out.println("Yetki Düzeyi: Yönetici");
    
    System.out.println("\nSistem Yetkileri:");
    System.out.println("- Öğrenci ekleme");
    System.out.println("- Öğrenci silme");
    System.out.println("- Öğrenci arama");
    System.out.println("- Öğrenci listeleme");
    System.out.println("- Yurt doluluk kontrolü");
}

    public static boolean tcKontrol(String kontrolet) {
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

    public static boolean telnoKontrol(String kontrolet) {
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

    public static boolean epostakontrol(String kontrolet) {
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
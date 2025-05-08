package org.corba.yurtyonetim.users;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

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


    public void deleteStudent(){

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

    public void searchStudent(){

    }
    public void listStudents(){

    }
    public void showUserInfo(){

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
        //naberbakbende31

}

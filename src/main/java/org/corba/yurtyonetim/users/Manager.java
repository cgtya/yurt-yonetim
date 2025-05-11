package org.corba.yurtyonetim.users;

import java.sql.*;
import java.util.*;

public class Manager extends User {

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



    /*
    public void addStudent(){
        String sql = "INSERT INTO ogrenci (name, surname, tcNo, telNo, eposta, currentDorm, disiplinNo, isOnLeave) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
    */

    /*
    public void searchStudent(String tcNo) {
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
    }
    */

    /*
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
    */
}
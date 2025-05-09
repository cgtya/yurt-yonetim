package org.corba.yurtyonetim.users;

import java.sql.*;
import java.util.*;

public class staticgecici {
    private static final String url = "jdbc:mysql://localhost:3306/ogrenciler?useSSL=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String databasePassword = "Omer200526a";

    public static String tempOg_Name;
    public static String tempOg_Surname;
    public static String tempOg_tcNo;
    public static String tempOg_telNo;
    public static String tempOg_eposta;
    public static String tempOg_currentDorm;
    public static String tempOg_roomNo;
    public static String tempOg_disiplinNo;
    public static String tempOg_isOnLeave;

    public static void addStudentStatic(String tempOg_Name, String tempOg_Surname, String tempOg_tcNo, String tempOg_telNo, String tempOg_eposta, String tempOg_currentDorm) {
        String sqlInsert = "INSERT INTO ogrenci (name, surname, tcNo, telNo, eposta, currentDorm, disiplinNo, isOnLeave) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlYurtlar = "SELECT * FROM yurtlar LIMIT 1";
        String sqlUpdateYurt = "UPDATE yurtlar SET " + tempOg_currentDorm + " = " + tempOg_currentDorm + " + 1";

        Set<String> bosYurtlar = new HashSet<>();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
            conn.setAutoCommit(false); // işlem bütünlüğü başlat

            // 1. Boş yurtları kontrol et
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlYurtlar)) {

                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        String yurtAdi = metaData.getColumnName(i);
                        int doluluk = rs.getInt(i);

                        if (doluluk < 200) {
                            bosYurtlar.add(yurtAdi);
                        }
                    }
                }
            }

            if (!bosYurtlar.contains(tempOg_currentDorm)) {
                System.out.println("Seçilen yurtta boş yer yok. İşlem iptal edildi.");
                conn.rollback();
                return;
            }

            // 2. Öğrenci ekle
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, tempOg_Name);
                pstmt.setString(2, tempOg_Surname);
                pstmt.setString(3, tempOg_tcNo);
                pstmt.setString(4, tempOg_telNo);
                pstmt.setString(5, tempOg_eposta);
                pstmt.setString(6, tempOg_currentDorm);
                pstmt.setInt(7, 0); //öğrenci yeni eklendiği iin disiplin suçu sayısı 0
                pstmt.setBoolean(8, false); //ve yine yeni eklendiği için izinliolma durumu
                pstmt.executeUpdate();
            }

            // 3. Yurt doluluğunu artır
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sqlUpdateYurt);
            }

            conn.commit();
            System.out.println("Öğrenci eklendi: " + tempOg_Name + " " + tempOg_Surname);

        } catch (SQLException e) {
            System.out.println("Ekleme sırasında hata: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Rollback başarısız: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Bağlantı kapatılamadı: " + e.getMessage());
            }
        }
    }

    public static String makeBecayisStatic(String tc1, String tc2) {
        String name1 = null, name2 = null;
        String dorm1 = null, dorm2 = null;

        String sqlGetDorm = "SELECT currentDorm FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateDorm = "UPDATE ogrenci SET currentDorm = ? WHERE tcNo = ?";
        String sqlGetStudentName = "SELECT name FROM ogrenci WHERE tcNo = ?";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
            conn.setAutoCommit(false);

            // ilk öğrencinin kimlik numarasını kontrol ediyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetDorm)) {
                pstmt.setString(1, tc1);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    dorm1 = rs.getString("currentDorm");
                } else {
                    return "1. öğrenci bulunamadı: " + tc1;
                }
            }

            // ikinci öğrencinin kimlik numarasını kontrol ediyoruz, eğer ilk kimlik numarası ile aynı ise işlem iptal ediliyor
            if (tc1.equals(tc2)) {
                return "Aynı öğrenciyle becayiş yapılamaz.";
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetDorm)) {
                pstmt.setString(1, tc2);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    dorm2 = rs.getString("currentDorm");
                } else {
                    return "2. öğrenci bulunamadı: " + tc2;
                }
            }

            // eğer iki öğrencinin de yurdu aynı ise işlem ipal ediliyor
            if (dorm1.equalsIgnoreCase(dorm2)) {
                conn.rollback();
                return "Bu iki öğrenci zaten aynı yurtta kalıyor.";
            }

            // öğrencilerin isimlerini veritabanından çekme
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetStudentName)) {
                pstmt.setString(1, tc1);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) name1 = rs.getString("name");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetStudentName)) {
                pstmt.setString(1, tc2);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) name2 = rs.getString("name");
            }

            // gerekli koşullar sağlandığı takdirde öğrencilerin bilgileri güncelleniyor
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateDorm)) {
                updateStmt.setString(1, dorm2);
                updateStmt.setString(2, tc1);
                updateStmt.executeUpdate();

                updateStmt.setString(1, dorm1);
                updateStmt.setString(2, tc2);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return "Becayiş başarılı:\n- " + name1 + " isimli öğrencinin yurt bilgisi " + dorm2 + " olacak şekilde güncellendi.\n- " + name2 + " isimli öğrencinin yurt bilgisi " + dorm1 + " olacak şekilde günvellendi.";

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                return "Rollback başarısız: " + ex.getMessage();
            }
            return "Becayiş işlemi sırasında hata: " + e.getMessage();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    return "Bağlantı kapatılamadı: " + ex.getMessage();
                }
            }
        }
    }



}

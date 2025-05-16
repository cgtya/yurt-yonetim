package org.corba.yurtyonetim.database;

import org.corba.yurtyonetim.users.Student;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class StudentDAO {

    private static String url;
    private static String user;
    private static String databasePassword;

    //database giriş parametrelerinin düzgün bir şekilde güncellenmesini sağlar
    public static void initCredUpdate() {
        url = DatabaseConfig.getUrl();
        user = DatabaseConfig.getUser();
        databasePassword = DatabaseConfig.getDatabasePassword();
    }

    public static String addStudentStatic(Student student) {
        String sqlInsert = "INSERT INTO ogrenci (name, surname, tcNo, telNo, eposta, currentDorm, disiplinNo, isOnLeave) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlYurtlar = "SELECT * FROM yurtlar LIMIT 1";
        String sqlUpdateYurt = "UPDATE yurtlar SET " + student.getCurrentDorm() + " = " + student.getCurrentDorm() + " + 1";


        Set<String> bosYurtlar = new HashSet<>();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
            conn.setAutoCommit(false); // işlem bütünlüğü başlat

            DatabaseConfig.selectDatabase(conn);

            // burada boş yurtlar kontrol ediliyor
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

            if (!bosYurtlar.contains(student.getCurrentDorm())) {
                conn.rollback();
                return "Seçilen yurtta boş yer yok. İşlem iptal edildi.";
            }

            // methoda gönderilen verilere göre burada öğrenci oluşturuluyor
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getSurname());
                pstmt.setString(3, student.getTcNo());
                pstmt.setString(4, student.getTelNo());
                pstmt.setString(5, student.getEposta());
                pstmt.setString(6, student.getCurrentDorm());
                pstmt.setInt(7, 0); //öğrenci yeni eklendiği iin disiplin suçu sayısı 0
                pstmt.setBoolean(8, false); //ve yine yeni eklendiği için izinliolma durumu
                pstmt.executeUpdate();
            }

            // öğrencinin eklendiği yurdun mevcudiyeti bir artırılıyor
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateYurt)) {
                pstmt.executeUpdate(sqlUpdateYurt);
            }

            conn.commit();

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
        return ("Öğrenci eklendi: " + student.getName() + " " + student.getSurname());
    }

    public static Student getStudentByTc(String tcNo) {
        String sql = "SELECT * FROM ogrenci WHERE tcNo = ?";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tcNo);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String telNo = rs.getString("telNo");
                    String eposta = rs.getString("eposta");
                    String currentDorm = rs.getString("currentDorm");
                    int disiplinNo = rs.getInt("disiplinNo");
                    boolean isOnLeave = rs.getBoolean("isOnLeave");

                    return new Student(name, surname, tcNo, telNo, eposta, currentDorm, disiplinNo, isOnLeave);
                }
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Bağlantı kapatılamadı: " + e.getMessage());
            }
        }

        return null; // Öğrenci bulunamazsa değer döndürmüyor
    }

    public static String updateStudentInDatabase(Student student) {
        String sql = "UPDATE ogrenci SET name = ?, surname = ?, telNo = ?, eposta = ?, currentDorm = ?, disiplinNo = ?, isOnLeave = ? WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            DatabaseConfig.selectDatabase(conn);

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getSurname());
            pstmt.setString(3, student.getTelNo());
            pstmt.setString(4, student.getEposta());
            pstmt.setString(5, student.getCurrentDorm());
            pstmt.setInt(6, student.getDiciplineNo());
            pstmt.setBoolean(7, student.isOnleave());
            pstmt.setString(8, student.getTcNo());

            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                return "Öğrenci verileri başarıyla güncellendi.";
            } else {
                return "Güncellenecek öğrenci bulunamadı (TC No: " + student.getTcNo() + ").";
            }

        } catch (SQLException e) {
            return "Güncelleme hatası: " + e.getMessage();
        }
    }

    public static String toggleLeaveStatus(String tcNo) {
        String sqlSelect = "SELECT isOnLeave FROM ogrenci WHERE tcNo = ?";
        String sqlUpdate = "UPDATE ogrenci SET isOnLeave = ? WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword)) {

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);

            boolean mevcutDurum;

            // Öğrencinin mevcut izinli olup olmadığını sorguluyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
                pstmt.setString(1, tcNo);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    return "Bu kimlik numarasına ait öğrenci bulunamadı.";
                }

                mevcutDurum = rs.getBoolean("isOnLeave");
            }

            // Durumu tersine çevirip güncelliyoruz
            boolean yeniDurum = !mevcutDurum;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setBoolean(1, yeniDurum);
                pstmt.setString(2, tcNo);
                pstmt.executeUpdate();
            }

            return "Öğrencinin izin durumu güncellendi. Yeni durum: " + (yeniDurum ? "İzinli" : "İzinli Değil");

        } catch (SQLException e) {
            return "Güncelleme sırasında hata oluştu: " + e.getMessage();
        }
    }

    public static String addDisiplineRecord(String tcNo) {
        String sqlSelect = "SELECT currentDorm, disiplinNo FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateDisiplin = "UPDATE ogrenci SET disiplinNo = ? WHERE tcNo = ?";
        String sqlDeleteStudent = "DELETE FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateDormTemplate = "UPDATE yurtlar SET %s = %s - 1";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);
            conn.setAutoCommit(false);

            String currentDorm;
            int currentDisiplin;

            // Öğrencinin disiplin ve yurt bilgisini al
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
                pstmt.setString(1, tcNo);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    currentDorm = rs.getString("currentDorm");
                    currentDisiplin = rs.getInt("disiplinNo");
                } else {
                    return "Bu TC numarasına sahip öğrenci bulunamadı.";
                }
            }

            int yeniDisiplin = currentDisiplin + 1;

            if (yeniDisiplin < 3) {
                // Disiplin sayısını artır
                try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDisiplin)) {
                    pstmt.setInt(1, yeniDisiplin);
                    pstmt.setString(2, tcNo);
                    pstmt.executeUpdate();
                }
                conn.commit();
                return "Öğrencinin disiplin cezası 1 artırıldı. Toplam ceza sayısı: " + yeniDisiplin;
            } else {
                // cezayı aldıysa → öğrenciyi sil + yurt sayısını azalt
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteStudent)) {
                    pstmt.setString(1, tcNo);
                    pstmt.executeUpdate();
                }

                // Yurt doluluğunu azaltıyoruz
                String finalYurtUpdate = String.format(sqlUpdateDormTemplate, currentDorm, currentDorm);
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(finalYurtUpdate);
                }

                conn.commit();
                return "Öğrenci 3 disiplin cezası aldığı için sistemden silindi ve " + currentDorm + " yurdunun doluluğu 1 azaltıldı.";
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                return "Rollback başarısız: " + ex.getMessage();
            }
            return "Veritabanı hatası: " + e.getMessage();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                return "Bağlantı kapatma hatası: " + e.getMessage();
            }
        }
    }
}

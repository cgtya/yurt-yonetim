package org.corba.yurtyonetim.database;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DormDAO {

    private static String url;
    private static String user;
    private static String databasePassword;

    //database giriş parametrelerinin düzgün bir şekilde güncellenmesini sağlar
    public static void initCredUpdate() {
        url = DatabaseConfig.getUrl();
        user = DatabaseConfig.getUser();
        databasePassword = DatabaseConfig.getDatabasePassword();
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

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);
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

    public static String nakilYapStatic(String tc, String hedefYurt) {
        String mevcutYurt = null;
        String name = null;
        Set<String> uygunYurtlar = new HashSet<>();

        String sqlGetYurt = "SELECT currentDorm FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateYurt = "UPDATE ogrenci SET currentDorm = ? WHERE tcNo = ?";
        String sqlYurtlar = "SELECT * FROM yurtlar LIMIT 1";
        String sqlGetStudentName = "SELECT name FROM ogrenci WHERE tcNo = ?";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);
            conn.setAutoCommit(false);

            // Öğrenciyi buluyoruz ve kimlik numarası var mı kontrol edip eğer varsa mevcut yurdunu alıyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetYurt)) {
                pstmt.setString(1, tc);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    mevcutYurt = rs.getString("currentDorm");
                } else {
                    return "Bu TC numarasına sahip öğrenci bulunamadı.";
                }
            }

            //Öğrencinin adını alıyoruz ki bilgi çıktısında gösterebilelim
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetStudentName)) {
                pstmt.setString(1, tc);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }

            // Mevcut yurt hariç boş yer olan yurtları listelemeye yarayan kısım
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlYurtlar)) {

                if (rs.next()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int sutunSayisi = meta.getColumnCount();

                    for (int i = 1; i <= sutunSayisi; i++) {
                        String yurtAdi = meta.getColumnName(i);
                        int doluluk = rs.getInt(i);

                        if (doluluk < 200 && !yurtAdi.equalsIgnoreCase(mevcutYurt)) {
                            uygunYurtlar.add(yurtAdi);
                        }
                    }
                }
            }

            if (uygunYurtlar.isEmpty()) {
                conn.rollback();
                return "Hiçbir yurtta boş yer yok. Nakil yapılamaz.";
            }

            if (!uygunYurtlar.contains(hedefYurt)) {
                conn.rollback();
                return "Geçersiz yurt adı. Sadece boş olan yurtlara nakil yapılabilir.";
            }

            // öğrencinin yurdunu güncelliyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateYurt)) {
                pstmt.setString(1, hedefYurt);
                pstmt.setString(2, tc);
                pstmt.executeUpdate();
            }

            // nakil olunan yurdun mevcudunu bir artırıp nakille ayrılınan yurdun mevcudunu bir azaltıyoruz
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE yurtlar SET " + hedefYurt + " = " + hedefYurt + " + 1");
                stmt.executeUpdate("UPDATE yurtlar SET " + mevcutYurt + " = " + mevcutYurt + " - 1");
            }

            conn.commit();
            return "Nakil işlemi başarıyla tamamlandı:\n" + name + " adlı öğrencicinin kaldığı yurt bilgisi " + mevcutYurt + " yurdundan " + hedefYurt + " yurdu olacak şekilde güncellendi.";

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                return "Rollback başarısız: " + ex.getMessage();
            }
            return "Nakil işlemi sırasında hata oluştu: " + e.getMessage();

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

    public static Set<String> getBosYurtlarForStudent(String tc) { //bu method boş yurtları combobox biçiminde gui üzerinde listeleyebilmek için
        Set<String> bosYurtlar = new HashSet<>();
        String mevcutYurt = null;
        String sqlGetYurt = "SELECT currentDorm FROM ogrenci WHERE tcNo = ?";
        String sqlYurtlar = "SELECT * FROM yurtlar LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword)) {

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);

            // Öğrencinin mevcut yurdunu bul
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetYurt)) {
                pstmt.setString(1, tc);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    mevcutYurt = rs.getString("currentDorm");
                } else {
                    return bosYurtlar; // Öğrenci yoksa boş set
                }
            }

            // Mevcut yurt hariç boş yurtları topla
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlYurtlar)) {

                if (rs.next()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        String yurtAdi = meta.getColumnName(i);
                        int doluluk = rs.getInt(i);

                        if (doluluk < 200 && !yurtAdi.equalsIgnoreCase(mevcutYurt)) {
                            bosYurtlar.add(yurtAdi);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Boş yurtlar alınamadı: " + e.getMessage());
        }

        return bosYurtlar;
    }

    public static Set<String> getBosYurtlar() {
        Set<String> bosYurtlar = new HashSet<>();
        String sqlYurtlar = "SELECT * FROM yurtlar LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword)) {

            DatabaseConfig.selectDatabase(conn);

            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(sqlYurtlar)) {
                    if (rs.next()) {
                        ResultSetMetaData meta = rs.getMetaData();
                        int columnCount = meta.getColumnCount();

                        for (int i = 1; i <= columnCount; i++) {
                            String yurtAdi = meta.getColumnName(i);
                            int doluluk = rs.getInt(i);

                            if (doluluk < 200) {
                                bosYurtlar.add(yurtAdi);
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Boş yurtlar alınamadı: " + e.getMessage());
        }
        return bosYurtlar;
    }

    public static String deleteStudentAndUpdateDorm(String tcNo) {
        String sqlSelectDorm = "SELECT currentDorm FROM ogrenci WHERE tcNo = ?";
        String sqlDeleteStudent = "DELETE FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateDorm = "UPDATE yurtlar SET %s = %s - 1";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);
            conn.setAutoCommit(false); // işlem bütünlüğü

            String currentDorm = null;

            // Öğrencinin yurt bilgilerini alıyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelectDorm)) {


                pstmt.setString(1, tcNo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentDorm = rs.getString("currentDorm");
                } else {
                    return "Bu TC numarasına ait öğrenci bulunamadı.";
                }
            }

            // eğer öğrenci bulunursa ve bilgiler alınabilmişse öğrenciyi veritabanı tablosundan siliyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteStudent)) {
                pstmt.setString(1, tcNo);
                pstmt.executeUpdate();
            }

            // öğrenci silindiği için kaldığı yurdun değerini tabloda bir azaltıyoruz
            String finalUpdate = String.format(sqlUpdateDorm, currentDorm, currentDorm);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(finalUpdate);
            }

            conn.commit();
            return "Öğrenci silindi ve " + currentDorm + " yurdunun doluluğu 1 azaltıldı.";

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                return "Rollback başarısız: " + ex.getMessage();
            }
            return "Silme işlemi sırasında hata: " + e.getMessage();

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

    public static int getStudentCount(String dorm) {
        String sql = "SELECT " + dorm +" FROM yurtlar";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             Statement pstmt = conn.prepareStatement(sql)) {

            DatabaseConfig.selectDatabase(conn);

            try (ResultSet rs = pstmt.executeQuery(sql)) {

                if (rs.next()) {
                    return rs.getInt(dorm);
                } else {
                    return -1;
                }
            } catch (SQLException e) {
                System.out.println("Sorgu sırasında hata: " + e.getMessage());
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Sorgu sırasında hata: " + e.getMessage());
            return -1;
        }
    }


}

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

            if (!bosYurtlar.contains(tempOg_currentDorm)) {
                System.out.println("Seçilen yurtta boş yer yok. İşlem iptal edildi.");
                conn.rollback();
                return;
            }

            // methoda gönderilen verilere göre burada öğrenci oluşturuluyor
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

            // öğrencinin eklendiği yurdun mevcudiyeti bir artırılıyor
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



    public static String searchStudent(String tcNo) {
        String sql = "SELECT * FROM ogrenci WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tcNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Öğrenci Bilgileri:\n");
                sb.append("Ad: ").append(rs.getString("name")).append("\n");
                sb.append("Soyad: ").append(rs.getString("surname")).append("\n");
                sb.append("T.C. Kimlik No: ").append(rs.getString("tcNo")).append("\n");
                sb.append("Telefon No: ").append(rs.getString("telNo")).append("\n");
                sb.append("E-posta: ").append(rs.getString("eposta")).append("\n");
                sb.append("Yurt: ").append(rs.getString("currentDorm")).append("\n");
                sb.append("Disiplin Puanı: ").append(rs.getInt("disiplinNo")).append("\n");
                sb.append("İzinli mi: ").append(rs.getBoolean("isOnLeave") ? "Evet" : "Hayır").append("\n");
                return sb.toString();
            } else {
                return "Girdiğiniz kimlik numarasına sahip bir öğrenci bulunamadı.";
            }

        } catch (SQLException e) {
            return "Arama hatası: " + e.getMessage();
        }
    }


    public static Student getStudentByTc(String tcNo) {
        String sql = "SELECT * FROM ogrenci WHERE tcNo = ?";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
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

}

package org.corba.yurtyonetim.database;

import org.corba.yurtyonetim.users.Manager;

import java.sql.*;

public class ManagerDAO {

    private static String url = DatabaseConfig.getUrl();
    private static String user = DatabaseConfig.getUser();
    private static String databasePassword = DatabaseConfig.getDatabasePassword();

    public static String addManagerStatic(Manager manager) {
        String sqlInsert = "INSERT INTO yonetici (name, surname, tcNo, telNo, eposta, password) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
            conn.setAutoCommit(false); // işlem bütünlüğü başlat

            DatabaseConfig.selectDatabase(conn);

            // methoda gönderilen verilere göre burada manager oluşturuluyor
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setString(1, manager.getName());
                pstmt.setString(2, manager.getSurname());
                pstmt.setString(3, manager.getTcNo());
                pstmt.setString(4, manager.getTelNo());
                pstmt.setString(5, manager.getEposta());
                pstmt.setString(6, manager.getPassword());
                pstmt.executeUpdate();
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
        return ("Yönetici eklendi: " + manager.getName() + " " + manager.getSurname());
    }

    public static Manager getManagerByTc(String tcNo) {
        String sql = "SELECT name, surname, telNo, eposta, password FROM yonetici WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            DatabaseConfig.selectDatabase(conn);

            pstmt.setString(1, tcNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String telNo = rs.getString("telNo");
                String eposta = rs.getString("eposta");
                String password = rs.getString("password");

                return new Manager(name, surname, tcNo, telNo, eposta, password);
            } else {
                System.out.println("Bu TC numarasına ait yönetici bulunamadı.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
            return null;
        }
    }
    public static String updateManagerInDatabase(Manager manager) {
        String sql = "UPDATE yonetici SET name = ?, surname = ?, telNo = ?, eposta = ?, password = ? WHERE tcNo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            DatabaseConfig.selectDatabase(conn);

            pstmt.setString(1, manager.getName());
            pstmt.setString(2, manager.getSurname());
            pstmt.setString(3, manager.getTelNo());
            pstmt.setString(4, manager.getEposta());
            pstmt.setString(5, manager.getPassword());
            pstmt.setString(6, manager.getTcNo());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                return "Yönetici bilgileri başarıyla güncellendi. (TC: " + manager.getTcNo() + ")";
            } else {
                return "Bu TC numarasına sahip bir yönetici bulunamadı.";
            }

        } catch (SQLException e) {
            return "Güncelleme sırasında hata oluştu: " + e.getMessage();
        }
    }

    public static String deleteManager(String tcNo) {
        String sqlDeleteManager = "DELETE FROM yonetici WHERE tcNo = ?";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);

            //veritabanı seç
            DatabaseConfig.selectDatabase(conn);
            conn.setAutoCommit(false); // işlem bütünlüğü

            String currentDorm = null;


            // eğer öğrenci bulunursa ve bilgiler alınabilmişse öğrenciyi veritabanı tablosundan siliyoruz
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteManager)) {
                pstmt.setString(1, tcNo);
                pstmt.executeUpdate();
            }

            conn.commit();
            return "Yönetici silindi.";

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


}

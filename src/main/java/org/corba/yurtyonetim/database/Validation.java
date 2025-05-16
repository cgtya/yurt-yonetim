package org.corba.yurtyonetim.database;

import java.sql.*;

public class Validation {

    private static String url = DatabaseConfig.getUrl();
    private static String user = DatabaseConfig.getUser();
    private static String databasePassword = DatabaseConfig.getDatabasePassword();

    public static boolean kontrol(String kontrolet,String tip) throws SQLException {
        String sql = "SELECT 1 FROM ogrenci WHERE " + tip + " = ?";

        String sqlAdm = "SELECT 1 FROM yonetici WHERE " + tip + " = ?";


        boolean ogr;
        boolean adm;


        try (Connection conn = DriverManager.getConnection(url, user, databasePassword);
             PreparedStatement pstmt1 = conn.prepareStatement(sql);
             PreparedStatement pstmt2 = conn.prepareStatement(sqlAdm)) {

            DatabaseConfig.selectDatabase(conn);

            pstmt1.setString(1, kontrolet);

            pstmt2.setString(1, kontrolet);

            try (ResultSet rs = pstmt1.executeQuery()) {
                ogr = rs.next();
            }

            try (ResultSet rs = pstmt2.executeQuery()) {
                adm = rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Sorgu hatası: " + e.getMessage());
            throw e; //exception ı sonraki metodda işlenmesi için iletir
        }

        return ogr || adm;

    }

    //veritabanında halihazırda aynı tc ile kayıtlı öğrenci veya yönetici var mı diye kontrol
    public static boolean tcKontrol(String kontrolet) throws SQLException{
        return kontrol(kontrolet,"tcNo");
    }

    //veritabanında halihazırda aynı eposta ile kayıtlı öğrenci veya yönetici var mı diye kontrol
    public static boolean epostaKontrol(String kontrolet) throws SQLException {
        return kontrol(kontrolet,"eposta");
    }

    //veritabanında halihazırda aynı telefon ile kayıtlı öğrenci veya yönetici var mı diye kontrol
    public static boolean telNoKontrol(String kontrolet) throws SQLException{
        return kontrol(kontrolet,"telNo");
    }

}

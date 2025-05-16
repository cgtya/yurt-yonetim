package org.corba.yurtyonetim.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class DatabaseConfig {

    //database login bilgileri
    private static String url = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static String user = "root";
    private static String databasePassword = "";
    private static String databaseName = "kullanicilar";

    private static final String urlDefault = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String userDefault = "root";
    private static final String databasePasswordDefault = "";
    private static final String databaseNameDefault = "kullanicilar";

    //getters-setters
    public static String getUserDefault() {
        return userDefault;
    }
    public static String getUrlDefault() {
        return urlDefault;
    }
    public static String getDatabasePasswordDefault() {
        return databasePasswordDefault;
    }
    public static String getDatabaseNameDefault() {
        return databaseNameDefault;
    }

    //ilk defa veritabanının oluşturulduğunu belirten değişken
    private static boolean databaseCreated = false;

    public static void setDatabasePassword(String databasePassword) {
        DatabaseConfig.databasePassword = databasePassword;
    }
    public static void setUser(String user) {
        DatabaseConfig.user = user;
    }
    public static void setUrl(String url) {
        DatabaseConfig.url = url;
    }
    public static void setDatabaseName(String databaseName) {
        DatabaseConfig.databaseName = databaseName;
    }

    public static String getUrl() {
        return url;
    }
    public static String getUser() {
        return user;
    }
    public static String getDatabasePassword() {
        return databasePassword;
    }
    public static String getDatabaseName() {
        return databaseName;
    }

    public static void setDatabaseCreated(boolean databaseCreated) {
        DatabaseConfig.databaseCreated = databaseCreated;
    }
    public static boolean isDatabaseCreated() {
        return databaseCreated;
    }

    //statement için veritabanı seçer
    public static void selectDatabase(Statement stmt) throws SQLException {
        stmt.executeUpdate("USE " + databaseName);
    }

    //connection için veritabanı seçer
    public static void selectDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            selectDatabase(stmt);
        }
    }

    //veritabanı bağlantısı başarılı ise true, değil ise false döndürür -> veritabanı bağlanıtısı başarılı ve veritabanı bulunmuyorsa veri tabanı oluşturur
    public static boolean testDatabaseConnection() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, databasePassword);

            //database var mı yok mu kontrolü
            String dbName = databaseName;
            ResultSet resultSet = conn.getMetaData().getCatalogs();
            boolean dbExists = false;

            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(dbName)) {
                    dbExists = true;
                    break;
                }
            }

            if (!dbExists) {
                //veritabanı yoksa oluşturur
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                stmt.executeUpdate("USE " + dbName);

                //tabloları yoksa oluşturur
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ogrenci" + "(name VARCHAR(50), " + "surname VARCHAR(50), " + "tcNo VARCHAR(12), " + "telNo VARCHAR(11), " + "eposta VARCHAR(255), " + "currentDorm VARCHAR(40), " + "disiplinNo INT, " + "isOnLeave BOOLEAN)");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS yurtlar" + "(OsmanTan INT, " + "HuseyinGazi INT, " + "Golbasi INT, " + "Mogan INT)");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS yonetici" + "(name VARCHAR(50), " + "surname VARCHAR(50), " + "tcNo VARCHAR(12), " + "telNo VARCHAR(11), " + "eposta VARCHAR(255), " + "password VARCHAR(65))");

                stmt.executeUpdate("INSERT INTO yonetici (name, surname, tcNo, telNo, eposta, password) VALUES ('admin', 'admin', '11111111111', '5000000000', 'admin@admin.org', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918')"); //default admin profile

                System.out.println("Veritabanı ve tablolar başarıyla oluşturuldu");
                databaseCreated = true;
            }

            //bağlantı testi
            return DriverManager.getConnection(url, user, databasePassword) != null;

        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantı hatası: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Bağlantı kapatılamadı: " + e.getMessage());
                }
            }
        }
    }

    public static String importExampleData() {
        try {
            Connection conn = DriverManager.getConnection(url,user,databasePassword);
            selectDatabase(conn);
            Statement statement = conn.createStatement();

            //sql dosyası okuma
            InputStream inputStream = DatabaseConfig.class.getResourceAsStream("/org/corba/yurtyonetim/database/exampledata.sql");
            if (inputStream == null) {
                return "SQL dosyası kaynak klasöründe bulunamadı";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder query = new StringBuilder();
            String line;

            // -- içeren yorum satırı çıkarılır
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("-- ")) {
                    continue;
                }

                query.append(line).append(" ");

                // ; içeren cümle veritabanında çalıştırılır
                if (line.trim().endsWith(";")) {
                    statement.execute(query.toString().trim());
                    query = new StringBuilder();
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            return e.getMessage();
        }
        databaseCreated = false;
        return "Veri başarıyla içeri aktarıldı.";

    }

}


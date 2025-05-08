package org.corba.yurtyonetim.database;

public class DatabaseConnection {
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DatabaseConnection {
        // Veritabanı bağlantı bilgileri
        private static final String URL = "jdbc:mysql://localhost:3306/your_database_name"; // Veritabanı ismini buraya yazın
        private static final String USER = "your_username"; // Veritabanı kullanıcı adı
        private static final String PASSWORD = "your_password"; // Veritabanı şifresi

        // Veritabanı bağlantısı için static bir metod
        public static Connection getConnection() throws SQLException {
            try {
                // JDBC sürücüsünü yükle
                Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL JDBC sürücüsü
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC Driver bulunamadı. Lütfen MySQL JDBC sürücüsünü ekleyin.", e);
            }

            // Bağlantıyı oluştur ve döndür
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }


    }

}

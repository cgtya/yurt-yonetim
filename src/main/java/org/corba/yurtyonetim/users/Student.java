package org.corba.yurtyonetim.users;

import java.sql.*;
import java.util.*;

public class Student extends User {
    private String currentDorm;
    private int diciplineNo=0;
    private boolean isOnleave;
    private static final int TOTAL_LEAVE_DAYS = 45; // Toplam izin hakkı
    private int usedLeaveDays; // Kullanılan izin günü
    private int totalLeaveDays;

    Scanner scanner = new Scanner(System.in);
    private final String url = "jdbc:mysql://localhost:3306/ogrenciler?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String databasePassword = "Omer200526a";

    public int getDiciplineNo() {
        return diciplineNo;
    }

    public void setDiciplineNo(int diciplineNo) {
        this.diciplineNo = diciplineNo;
    }

    public boolean isOnleave() {
        return isOnleave;
    }
    public void setIsOnleave(boolean isOnleave) { // setter bu şekilde olmalı
        this.isOnleave = isOnleave;
    }


    public int getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(int totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public int getUsedLeaveDays() {
        return usedLeaveDays;
    }

    public void setUsedLeaveDays(int usedLeaveDays) {
        this.usedLeaveDays = usedLeaveDays;
    }
    public String getCurrentDorm() {
        return currentDorm;
    }

    public void setCurrentDorm(String currentDorm) {
        this.currentDorm = currentDorm;
    }






    public Student(String name, String surname, String tcNo, String telNo, String eposta, String currentDorm, int diciplineNo, boolean isOnleave) {
    super(name, surname, tcNo, telNo, eposta);
    this.currentDorm = currentDorm;
    this.diciplineNo = diciplineNo;
    this.isOnleave = isOnleave;
}

    /*public void makeBecayis() {
        Scanner scanner = new Scanner(System.in);
        String tc1, tc2, name1 = null, name2 = null;
        String dorm1 = null, dorm2 = null;

        String sqlGetDorm = "SELECT currentDorm FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateDorm = "UPDATE ogrenci SET currentDorm = ? WHERE tcNo = ?";
        String sqlGetStudentName = "SELECT name FROM ogrenci WHERE tcNo = ?";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
            conn.setAutoCommit(false); // bu satır eğer birinci öğrencinin yurt bilgisi güncellenir ama öteki öğrencide hata alınırsa hata oluşmasını engellemek için gerekiyor

            // birinci öğrencinin kimlik numarasını alıyoruz
            while (true) {
                System.out.print("1. öğrencinin TC Kimlik Numarasını girin: ");
                tc1 = scanner.nextLine().trim();

                try (PreparedStatement pstmt = conn.prepareStatement(sqlGetDorm)) {
                    pstmt.setString(1, tc1);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        dorm1 = rs.getString("currentDorm");
                        break;
                    } else {
                        System.out.println("Bu TC numarasına ait öğrenci bulunamadı. Tekrar deneyin.");
                    }
                }
            }

            // ikinci öğrencinin kimlik numarasını alıyoruz
            while (true) {
                System.out.print("2. öğrencinin TC Kimlik Numarasını girin: ");
                tc2 = scanner.nextLine().trim();

                if (tc2.equals(tc1)) {
                    System.out.println("Aynı öğrenciyle becayiş yapılamaz. Lütfen farklı bir TC girin.");
                    continue;
                }

                try (PreparedStatement pstmt = conn.prepareStatement(sqlGetDorm)) {
                    pstmt.setString(1, tc2);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        dorm2 = rs.getString("currentDorm");
                        break;
                    } else {
                        System.out.println("Bu TC numarasına ait öğrenci bulunamadı. Tekrar deneyin.");
                    }
                }
            }

            // Öğrenci isimlerini alıyoruz ki bilgi mesajında gösterebilelim
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

            // İki öğrenci de aynı yurtta ise işlemi iptal ediyoruz
            if (dorm1.equalsIgnoreCase(dorm2)) {
                System.out.println("Bu iki öğrenci zaten aynı yurtta kalıyor. Becayiş işlemi iptal edildi.");
                conn.rollback(); // veritabanı işlemi yapılmadıysa rollback koyuyoruz ve değişiklik uygulamıyoruz
                return;
            }

            // burada ise verileri güncelliyoruz
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateDorm)) {
                updateStmt.setString(1, dorm2);
                updateStmt.setString(2, tc1);
                updateStmt.executeUpdate();

                updateStmt.setString(1, dorm1);
                updateStmt.setString(2, tc2);
                updateStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Becayiş işlemi başarıyla tamamlandı:");
            System.out.println(tc1 + " kimlik numaralı ve " + name1 + " adlı öğrencinin kaldığı yurt bilgisi " + dorm2 + " olarak güncellendi.");
            System.out.println(tc2 + " kimlik numaralı ve " + name2 + " adlı öğrencinin kaldığı yurt bilgisi " + dorm1 + " olarak güncellendi.");

        } catch (SQLException e) {
            System.out.println("Becayiş sırasında hata: " + e.getMessage());

            if (conn != null) {
                try {
                    conn.rollback(); //burada da rollback koyuyoruz ki güncelleme sırasında hata olursa değişiklikleri geri alabilelim
                    System.out.println("İşlem geri alındı.");
                } catch (SQLException ex) {
                    System.out.println("Rollback başarısız: " + ex.getMessage());
                }
            }

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Bağlantı kapatılamadı: " + ex.getMessage());
                }
            }
        }
    }*/


    /*public void nakilYap() {
        Scanner scanner = new Scanner(System.in);
        String tc, mevcutYurt, name = null;
        Set<String> uygunYurtlar = new HashSet<>();

        String sqlGetYurt = "SELECT currentDorm FROM ogrenci WHERE tcNo = ?";
        String sqlUpdateYurt = "UPDATE ogrenci SET currentDorm = ? WHERE tcNo = ?";
        String sqlYurtlar = "SELECT * FROM yurtlar LIMIT 1";
        String sqlGetStudentName = "SELECT name FROM ogrenci WHERE tcNo = ?";

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, databasePassword);
            conn.setAutoCommit(false);

            while (true) {
                System.out.print("Nakil yapılacak öğrencinin TC Kimlik Numarasını girin: ");
                tc = scanner.nextLine().trim();

                try (PreparedStatement pstmt = conn.prepareStatement(sqlGetYurt)) {
                    pstmt.setString(1, tc);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        mevcutYurt = rs.getString("currentDorm");
                        System.out.println("Mevcut yurdu: " + mevcutYurt);
                        break;
                    } else {
                        System.out.println("Bu TC numarasına sahip öğrenci bulunamadı. Tekrar deneyin.");
                    }
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetStudentName)) {
                pstmt.setString(1, tc);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }


            // bu aşamada ekrana girilen kimlik numaralı öğrencinin kaldığı yurt hariç boş yer olan yurtlar listeleniyor ve eğer boş yer olan yurt yoksa ekrana buna dair çıktı verilip nakil işlemi sonlandırılıyor
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlYurtlar)) {

                if (rs.next()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int sutunSayisi = meta.getColumnCount();

                    System.out.println("Nakil yapılabilecek yurtlar (boş yer olanlar):");
                    for (int i = 1; i <= sutunSayisi; i++) {
                        String yurtAdi = meta.getColumnName(i);
                        int doluluk = rs.getInt(i);

                        if (doluluk < 200 && !yurtAdi.equalsIgnoreCase(mevcutYurt)) {
                            uygunYurtlar.add(yurtAdi);
                            System.out.println("- " + yurtAdi + " (Doluluk: " + doluluk + ")");
                        }
                    }
                }
            }

            if (uygunYurtlar.isEmpty()) {
                System.out.println("Hiçbir yurtta boş yer yok. Nakil yapılamaz.");
                conn.rollback();
                return;
            }


            // burada kullanıcıdan naklin gerçekleştirileceği yurdun adını alıyoruz ve listelenen yurtlardan birini girmesi isteniyor
            String hedefYurt;
            while (true) {
                System.out.print("Nakil yapılacak yeni yurt adını girin: ");
                hedefYurt = scanner.nextLine().trim();

                if (uygunYurtlar.contains(hedefYurt)) {
                    break;
                } else {
                    System.out.println("Hatalı giriş. Yukarıdaki yurtlardan birini girin.");
                }
            }

            // burada her koşul sağlandıysa girilen değerler ile nakili işlemi gerçekleştiriliyor ve yurtların tablodaki doluluk değerleri düzenleniliyor
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateYurt)) {
                pstmt.setString(1, hedefYurt);
                pstmt.setString(2, tc);
                pstmt.executeUpdate();
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE yurtlar SET " + hedefYurt + " = " + hedefYurt + " + 1");
                stmt.executeUpdate("UPDATE yurtlar SET " + mevcutYurt + " = " + mevcutYurt + " - 1");
            }

            conn.commit();
            System.out.println("Nakil işlemi başarıyla tamamlandı: " + name + " adlı ve " + tc + " kimlik numaralı öğrencinin konakladığı yurt bilgisi " + hedefYurt + " yurdu olarak güncellendi.");

        } catch (SQLException e) {
            System.out.println("Nakil işlemi sırasında hata oluştu: " + e.getMessage());

            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("İşlem geri alındı.");
                } catch (SQLException ex) {
                    System.out.println("Rollback başarısız: " + ex.getMessage());
                }
            }

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Bağlantı kapatılamadı: " + ex.getMessage());
                }
            }
        }
    }*/

public void enteringDicipline(Manager manager) {
    this.diciplineNo++;
    System.out.println("Öğrenci " + this.getName() + " için yeni disiplin cezası eklendi.");
    System.out.println("Toplam disiplin cezası sayısı: " + this.diciplineNo);
    
    if (this.diciplineNo >= 3) {
        System.out.println("Öğrenci " + this.getName() + " 3 disiplin cezasını doldurdu!");
        System.out.println("Yurttan çıkarılma işlemi başlatılıyor...");
        manager.deleteStudent(this);
    }
}


//08.05.2025 gece 23.49 rollbackleri anca koyabildim factorio falan oynamayacağım yatış zamanı 2



}
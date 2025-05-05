/*
//TODO commented out as it doesnt work

package org.corba.yurtyonetim.users;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

import org.corba.yurtyonetim.database.DatabaseConnection;

public class Student extends User {
	Scanner scanner = new Scanner(System.in);
	private String sex;
	private String address;
	private String department;
	private int dayOff;
	private int roomNo;
	private int penaltyNo; 		// 3 tane disiplin suçu işlenirse yurttan atılacak
	private String dormitoryName;

	public String getDormitoryName() {
		return dormitoryName;
	}
	public void setDormitoryName(String dormitoryName) {
		this.dormitoryName = dormitoryName;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

	public int getDayOff() {
		return dayOff;
	}
	public void setDayOff(int dayOff) {
		this.dayOff = dayOff;
	}

	public int getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(int roomNo) {
		this.roomNo = roomNo;
	}

	public int getPenaltyNo() {
		return penaltyNo;
	}
	public void setPenaltyNo(int penaltyNo) {
		this.penaltyNo = penaltyNo;
	}

	public void yurtNakilTalebi() {
		try {
			// Kullanıcıdan TC numarasını al
			System.out.print("TC numaranızı girin: ");
			String tcNo = scanner.nextLine();

			// Veritabanına bağlan
			Connection connection = DatabaseConnection.getConnection();

			// Öğrencinin mevcut yurt bilgilerini al
			String sql1 = "SELECT yurtIsmi FROM ogrenci WHERE tcNo = ?";
			PreparedStatement pstmt1 = connection.prepareStatement(sql1);
			pstmt1.setString(1, tcNo);
			ResultSet rs1 = pstmt1.executeQuery();

			String mevcutYurt = null;
			if (rs1.next()) {
				mevcutYurt = rs1.getString("yurtIsmi");
				System.out.println("Mevcut yurdunuz: " + mevcutYurt);
			} else {
				System.out.println("Girilen TC numarası ile kayıt bulunamadı.");
				return;
			}

			// Kullanıcıdan yeni yurt talebini al
			System.out.print("Nakil talebinde bulunmak istediğiniz yeni yurdun adını girin: ");
			String yeniYurt = scanner.nextLine();

			// Aynı yurt kontrolü
			if (mevcutYurt != null && mevcutYurt.equalsIgnoreCase(yeniYurt)) {
				System.out.println("Zaten " + yeniYurt + " yurdunda kayıtlısınız. Lütfen farklı bir yurt seçin.");
				return;
			}

			// Yeni yurt adı veritabanında mevcut mu kontrol et
			String sql2 = "SELECT yurtIsmi FROM yurtlar WHERE yurtIsmi = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setString(1, yeniYurt);
			ResultSet rs2 = pstmt2.executeQuery();

			if (!rs2.next()) {
				System.out.println("Girilen yurt adı sistemde bulunmamaktadır. Lütfen geçerli bir yurt adı girin.");
				return;
			}

			// Yurt bilgisini güncelle
			String sqlUpdate = "UPDATE ogrenci SET yurtIsmi = ? WHERE tcNo = ?";
			PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate);
			pstmtUpdate.setString(1, yeniYurt);
			pstmtUpdate.setString(2, tcNo);

			int rowsAffected = pstmtUpdate.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Nakil talebiniz başarıyla gerçekleştirilmiştir. Yeni yurdunuz: " + yeniYurt);
			} else {
				System.out.println("Nakil talebi sırasında bir hata oluştu.");
			}

		} catch (SQLException e) {
			System.out.println("Veritabanı hatası: " + e.getMessage());
		}
	}

	public void becais() {
		// iki öğrencinin birbirinin tcsini girerek birbirinden onay alarak yurtlarını
		// değiştirmesi
		try {
			// Kullanıcıdan TC numaralarını al
			System.out.print("Kendi TC numaranızı girin: ");
			String kendiTcNo = scanner.nextLine();

			System.out.print("Yurt değiştirmek istediğiniz diğer öğrencinin TC numarasını girin: ");
			String digerTcNo = scanner.nextLine();

			// Veritabanına bağlan
			Connection connection = DatabaseConnection.getConnection();

			// İlk öğrencinin yurt bilgilerini al
			String sql1 = "SELECT yurtIsmi FROM ogrenci WHERE tcNo = ?";
			PreparedStatement pstmt1 = connection.prepareStatement(sql1);
			pstmt1.setString(1, kendiTcNo);
			ResultSet rs1 = pstmt1.executeQuery();

			String kendiYurt = null;
			if (rs1.next()) {
				kendiYurt = rs1.getString("yurtIsmi");
			} else {
				System.out.println("Kendi TC numaranız ile kayıt bulunamadı.");
				return;
			}

			// İkinci öğrencinin yurt bilgilerini al
			String sql2 = "SELECT yurtIsmi FROM ogrenci WHERE tcNo = ?";
			PreparedStatement pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setString(1, digerTcNo);
			ResultSet rs2 = pstmt2.executeQuery();

			String digerYurt = null;
			if (rs2.next()) {
				digerYurt = rs2.getString("yurtIsmi");
			} else {
				System.out.println("Girilen diğer TC numarası ile kayıt bulunamadı.");
				return;
			}

			// Onay almak
			System.out.println("Kendi yurdunuz: " + kendiYurt);
			System.out.println("Diğer öğrencinin yurdu: " + digerYurt);
			System.out.print("Yurt değiştirme işlemini onaylıyor musunuz? (Evet/Hayır): ");
			String onay1 = scanner.nextLine();

			System.out.print("Diğer öğrenci yurt değiştirme işlemini onaylıyor mu? (Evet/Hayır): ");
			String onay2 = scanner.nextLine();

			if (!onay1.equalsIgnoreCase("Evet") || !onay2.equalsIgnoreCase("Evet")) {
				System.out.println("Yurt değiştirme işlemi iptal edildi.");
				return;
			}

			// Yurt bilgilerini değiştir
			String sqlUpdate1 = "UPDATE ogrenci SET yurtIsmi = ? WHERE tcNo = ?";
			PreparedStatement pstmtUpdate1 = connection.prepareStatement(sqlUpdate1);
			pstmtUpdate1.setString(1, digerYurt); // Diğer öğrencinin yurdu
			pstmtUpdate1.setString(2, kendiTcNo); // Kendi TC numarası

			String sqlUpdate2 = "UPDATE ogrenci SET yurtIsmi = ? WHERE tcNo = ?";
			PreparedStatement pstmtUpdate2 = connection.prepareStatement(sqlUpdate2);
			pstmtUpdate2.setString(1, kendiYurt); // Kendi yurdu
			pstmtUpdate2.setString(2, digerTcNo); // Diğer TC numarası

			int rowsAffected1 = pstmtUpdate1.executeUpdate();
			int rowsAffected2 = pstmtUpdate2.executeUpdate();

			if (rowsAffected1 > 0 && rowsAffected2 > 0) {
				System.out.println("Yurt değiştirme işlemi başarılı!");
			} else {
				System.out.println("Yurt değiştirme işlemi sırasında bir hata oluştu.");
			}

		} catch (SQLException e) {
			System.out.println("Veritabanı hatası: " + e.getMessage());
		}
	}

	public void istekVeSikayet() {
		// Kullanıcıdan istek veya şikayet metnini al
		System.out.println("Lütfen istek veya şikayetinizi yazın:");
		String mesaj = scanner.nextLine();

		// Kullanıcıdan bunun istek mi şikayet mi olduğunu öğren
		System.out.println("Bu bir istek mi şikayet mi? (istek/şikayet):");
		String tur = scanner.nextLine().toLowerCase();

		// Veri saklama işlemi (örnek olarak dosyaya yazıyoruz)
		try {
			// Dosyayı aç veya oluştur
			FileWriter writer = new FileWriter("istekVeSikayetler.txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			// Mesajı dosyaya yaz
			bufferedWriter.write("Tür: " + tur);
			bufferedWriter.newLine();
			bufferedWriter.write("Mesaj: " + mesaj);
			bufferedWriter.newLine();
			bufferedWriter.write("---------------");
			bufferedWriter.newLine();

			// Dosyayı kapat
			bufferedWriter.close();
			System.out.println("İsteğiniz/şikayetiniz başarıyla kaydedildi.");
		} catch (IOException e) {
			System.out.println("Hata: İstek veya şikayet kaydedilemedi. " + e.getMessage());
		}
	}

	public void yurtKayitSilme() {
		// yurttan ayrılma methodu. öğrencinin bilgilerini dosya işlemlerinden sileceğiz
		// Kullanıcı onayı al
		System.out.println("Yurttan ayrılmak istediğinizden emin misiniz? (evet/hayır):");
		String onay = scanner.nextLine().toLowerCase();

		if (!onay.equals("evet")) {
			System.out.println("İşlem iptal edildi.");
			return;
		}

		// Veritabanı bağlantısı için Connection nesnesi
		Connection connection = null;

		try {
			// Veritabanına bağlan (bağlantıyı dışarıdan alabiliriz veya bir helper sınıfı
			// kullanılabilir)
			connection = DatabaseConnection.getConnection(); // DatabaseConnection, bağlantıyı yöneten bir sınıf

			// SQL sorgusunu hazırla
			String sql = "DELETE FROM ogrenci WHERE tcNo = ?";

			// PreparedStatement kullanarak sorguyu oluştur
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, getTcNo()); // Öğrencinin TC numarasını sorguya ekle

			// Sorguyu çalıştır ve etkilenen satır sayısını al
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Kaydınız başarıyla silindi.");
			} else {
				System.out.println("Kaydınız bulunamadı.");
			}

		} catch (SQLException e) {
			System.out.println("Veritabanı hatası: " + e.getMessage());
		} finally {
			// Bağlantıyı kapat
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Bağlantı kapatılırken hata oluştu: " + e.getMessage());
			}
		}

	}

	public void yurtBasvurma() {
		if (getDormitoryName() == null) {
			// Random bir yurt ver veya seçenek sun, hangi yurda gitmek istediğiyle ilgili
			if (getDormitoryName() != null) {
				System.out.println("Zaten bir yurtta kayıtlısınız: " + getDormitoryName());
				return;
			}

			// Rastgele bir yurt oluşturmak için örnek yurt isimleri
			String[] yurtlar = { "Yurt A", "Yurt B", "Yurt C", "Yurt D" };
			Random random = new Random();
			String secilenYurt = yurtlar[random.nextInt(yurtlar.length)];

			// Kullanıcıya bilgiyi göster
			System.out.println("Sisteme kayıtlı olmadığınız görüldü.");
			System.out.println("Size rastgele bir yurt atanıyor: " + secilenYurt);

			// Bu bilgiyi veritabanına kaydet
			Connection connection = null;

			try {
				connection = DatabaseConnection.getConnection();

				// SQL sorgusu
				String sql = "UPDATE ogrenci SET yurtIsmi = ? WHERE tcNo = ?";
				PreparedStatement pstmt = connection.prepareStatement(sql);

				pstmt.setString(1, secilenYurt); // Yurt ismini ekle
				pstmt.setString(2, getTcNo()); // Öğrencinin TC numarasını ekle

				int rowsAffected = pstmt.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println("Başvurunuz başarıyla kaydedildi. Atanan yurt: " + secilenYurt);
					setDormitoryName(secilenYurt); // Öğrencinin yurt ismini güncelle
				} else {
					System.out.println("Başvuru sırasında bir problem oluştu.");
				}

			} catch (SQLException e) {
				System.out.println("Veritabanı hatası: " + e.getMessage());
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					System.out.println("Bağlantı kapatılırken hata oluştu: " + e.getMessage());
				}
			}
		}
	}

	public void yemegiGoster() {
		// veri tabanındaki yemek listesini göster
		Connection connection = null;

		try {
			// Veritabanına bağlan
			connection = DatabaseConnection.getConnection();

			// SQL sorgusunu hazırla
			String sql = "SELECT gun, ogun, yemek_isim FROM yemek_listesi ORDER BY gun, ogun";

			// Sorguyu çalıştır
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			// Sonuçları ekrana yazdır
			System.out.println("╔════════════════════════════════════════════════╗");
			System.out.println("║                 YEMEK LİSTESİ                 ║");
			System.out.println("╠════════════════════════════════════════════════╣");
			while (resultSet.next()) {
				String gun = resultSet.getString("gun");
				String ogun = resultSet.getString("ogun");
				String yemekIsim = resultSet.getString("yemek_isim");

				System.out.printf("║ Gün: %-10s | Öğün: %-10s | Yemek: %-20s ║\n", gun, ogun, yemekIsim);
			}
			System.out.println("╚════════════════════════════════════════════════╝");

		} catch (SQLException e) {
			System.out.println("Veritabanı hatası: " + e.getMessage());
		} finally {
			// Bağlantıyı kapat
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Bağlantı kapatılırken hata oluştu: " + e.getMessage());
			}
		}
	}

	public void izinTalebi() {
		// izin hakkı kalmış mı diye kontrol et ona göre feedback ver
		if (getDayOff() <= 0) {
			System.out.println("Bu dönemlik izin hakkınız kalmamıştır.");
			return;
		}

		// Kullanıcıdan izin günü al
		System.out.println("Kaç gün izin almak istiyorsunuz?");
		int talepEdilenIzin = scanner.nextInt();
		scanner.nextLine(); // Buffer sorununu önlemek için

		if (talepEdilenIzin > getDayOff()) {
			System.out.println("Talep edilen izin gün sayısı, mevcut izin hakkınızdan fazladır. " + "En fazla "
					+ getDayOff() + " gün izin alabilirsiniz.");
			return;
		}

		// İzin hakkını düş
		int kalanIzin = getDayOff() - talepEdilenIzin;
		setDayOff(kalanIzin);

		// Veritabanını güncelle
		Connection connection = null;

		try {
			connection = DatabaseConnection.getConnection();

			// SQL sorgusu
			String sql = "UPDATE ogrenci SET izinHakki = ? WHERE tcNo = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);

			pstmt.setInt(1, kalanIzin); // Güncellenen izin hakkını ekle
			pstmt.setString(2, getTcNo()); // Öğrencinin TC numarasını ekle

			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println(
						"İzin talebiniz başarıyla işlenmiştir. " + "Kalan izin hakkınız: " + kalanIzin + " gün.");
			} else {
				System.out.println("İzin talebi sırasında bir problem oluştu.");
			}

		} catch (SQLException e) {
			System.out.println("Veritabanı hatası: " + e.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Bağlantı kapatılırken hata oluştu: " + e.getMessage());
			}
		}
	}

	@Override
	public boolean logIn() {

		 // Veri tabanındaki bilgiler ile giriş bilgileri birbirini destekliyor mu kontrol et

		System.out.print("TC Kimlik Numaranızı Girin: ");
		String tcNo = scanner.nextLine();

		System.out.print("Şifrenizi Girin: ");
		String sifre = scanner.nextLine();

		Connection connection = null;

		try {
			// Veritabanına bağlan
			connection = DatabaseConnection.getConnection();

			// Kullanıcı bilgilerini kontrol et
			String sql = "SELECT * FROM ogrenci WHERE tcNo = ? AND sifre = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, tcNo);
			pstmt.setString(2, sifre);

			ResultSet resultSet = pstmt.executeQuery();

			// Sonuç kontrolü
			if (resultSet.next()) {
				System.out.println("Giriş başarılı! Hoş geldiniz, " + resultSet.getString("ad") + " "
						+ resultSet.getString("soyad") + ".");
				return true;
			} else {
				System.out.println("Giriş bilgileri hatalı. Lütfen tekrar deneyin.");
				return false;
			}

		} catch (SQLException e) {
			System.out.println("Veritabanı hatası: " + e.getMessage());
			return false;
		} finally {
			// Bağlantıyı kapat
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Bağlantı kapatılırken hata oluştu: " + e.getMessage());
			}
		}
	}

	@Override
	public void editProfile() {
		// profili düzenle yeni bilgileri eskisinin üzerine yaz
	    try {
	        // Kullanıcıdan TC numarasını al
	        System.out.print("TC Kimlik Numaranızı Girin: ");
	        String tcNo = scanner.nextLine();

	        // Kullanıcıya hangi bilgiyi güncellemek istediğini sor
	        System.out.println("Hangi bilgiyi güncellemek istiyorsunuz?");
	        System.out.println("1. Ad");
	        System.out.println("2. Soyad");
	        System.out.println("3. Telefon Numarası");
	        System.out.println("4. Şifre");
	        System.out.print("Seçiminiz (1-4): ");
	        int secim = scanner.nextInt();
	        scanner.nextLine(); // Buffer temizleme

	        String kolon = null;
	        String yeniDeger = null;

	        // Kullanıcının seçimine göre güncellenecek kolonu belirle
	        switch (secim) {
	            case 1:
	                kolon = "ad";
	                System.out.print("Yeni adınızı girin: ");
	                yeniDeger = scanner.nextLine();
	                break;
	            case 2:
	                kolon = "soyad";
	                System.out.print("Yeni soyadınızı girin: ");
	                yeniDeger = scanner.nextLine();
	                break;
	            case 3:
	                kolon = "telefon";
	                System.out.print("Yeni telefon numaranızı girin: ");
	                yeniDeger = scanner.nextLine();
	                if (!yeniDeger.matches("\\d{10}")) { // 10 haneli bir sayı kontrolü
	                    System.out.println("Geçersiz telefon numarası formatı. Güncelleme iptal edildi.");
	                    return;
	                }
	                break;
	            case 4:
	                kolon = "sifre";
	                System.out.print("Yeni şifrenizi girin: ");
	                yeniDeger = scanner.nextLine();
	                if (yeniDeger.length() < 6) { // Şifre uzunluğu kontrolü
	                    System.out.println("Şifre en az 6 karakter olmalıdır. Güncelleme iptal edildi.");
	                    return;
	                }
	                break;
	            default:
	                System.out.println("Geçersiz seçim. Güncelleme iptal edildi.");
	                return;
	        }

	        // Veritabanına bağlan
	        Connection connection = DatabaseConnection.getConnection();

	        // Veritabanını güncelle
	        String sql = "UPDATE ogrenci SET " + kolon + " = ? WHERE tcNo = ?";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setString(1, yeniDeger);
	        pstmt.setString(2, tcNo);

	        int rowsAffected = pstmt.executeUpdate();

	        // Sonuç bildirimi
	        if (rowsAffected > 0) {
	            System.out.println("Profil bilgisi başarıyla güncellendi.");
	        } else {
	            System.out.println("Güncelleme sırasında bir hata oluştu. Lütfen bilgilerinizi kontrol edin.");
	        }

	    } catch (SQLException e) {
	        System.out.println("Veritabanı hatası: " + e.getMessage());
	    }
	}

	@Override
	public void showProfile() {
		System.out.println("╔═══════════════════════════╗");
		System.out.println("║       PROFİL BİLGİLERİ     ║");
		System.out.println("╠═══════════════════════════╣");
		System.out.println("║ İsim       : " + getName());
		System.out.println("║ Soyisim    : " + getSurname());
		System.out.println("║ Cinsiyet   : " + getSex());
		System.out.println("║ Adres      : " + getAddress());
		System.out.println("║ Bölüm      : " + getDepartment());
		System.out.println("║ İzin hakkı : " + getDayOff());
		System.out.println("║ Oda no     : " + getRoomNo());
		System.out.println("║ Disiplin no: " + getPenaltyNo());
		System.out.println("║ Yurt ismi  : " + getDormitoryName());
		System.out.println("║ E-posta  :   " + getEmail());
		System.out.println("║ Telefon  :   " + getTelNo());
		System.out.println("╚═══════════════════════════╝");
	}

}

*/
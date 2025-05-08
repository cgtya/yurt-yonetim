import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dataBase.DatabaseConnection;
public class yonetici extends kullanıcı {
	Scanner scanner = new Scanner(System.in);


	@Override
	public boolean logIn() {
		System.out.print("TC Kimlik Numaranızı Girin: ");
		String tcNo = scanner.nextLine();

		System.out.print("E-posta Adresinizi Girin: ");
		String eposta = scanner.nextLine();

		System.out.print("Şifrenizi Girin: ");
		String sifre = scanner.nextLine();

		Connection connection = null;

		try {
			// Veritabanına bağlan
			connection = DatabaseConnection.getConnection();

			// Kullanıcı bilgilerini kontrol et
			String sql = "SELECT * FROM yonetici WHERE tcNo = ? AND eposta = ? AND sifre = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, tcNo);
			pstmt.setString(2, eposta);
			pstmt.setString(3, sifre);

			ResultSet resultSet = pstmt.executeQuery();

			// Sonuç kontrolü
			if (resultSet.next()) {
				System.out.println("Giriş başarılı! Hoş geldiniz, " + resultSet.getString("ad") + " " + resultSet.getString("soyad") + ".");
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


		System.out.println("Profil düzenleme ekranına hoş geldiniz.");

		while (continueEditing) {
			System.out.println("Hangi bilgiyi değiştirmek istersiniz?");
			System.out.println("1. Telefon Numarası");
			System.out.println("2. Şifre");
			System.out.println("3. E-posta");
			System.out.println("0. Çıkış");
			System.out.print("Seçiminiz: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // Girişten sonra satır sonu karakterini temizlemek için

			switch (choice) {
				case 1:
					System.out.print("Yeni telefon numaranızı girin: ");
					String newTelNo = scanner.nextLine();
					if (!newTelNo.isBlank()) {
						setTelNo(newTelNo);
						System.out.println("Telefon numarası başarıyla güncellendi.");
					} else {
						System.out.println("Telefon numarası boş bırakılamaz.");
					}
					break;

				case 2:
					System.out.print("Yeni şifrenizi girin: ");
					String newPassword = scanner.nextLine();
					if (!newPassword.isBlank()) {
						setPassword(newPassword);
						System.out.println("Şifre başarıyla güncellendi.");
					} else {
						System.out.println("Şifre boş bırakılamaz.");
					}
					break;

				case 3:
					System.out.print("Yeni e-posta adresinizi girin: ");
					String newEmail = scanner.nextLine();
					if (!newEmail.isBlank()) {
						setEposta(newEmail);
						System.out.println("E-posta adresi başarıyla güncellendi.");
					} else {
						System.out.println("E-posta adresi boş bırakılamaz.");
					}
					break;

				case 0:
					continueEditing = false;
					System.out.println("Profil düzenleme işlemi sonlandırıldı.");
					break;

				default:
					System.out.println("Geçersiz bir seçim yaptınız. Lütfen tekrar deneyin.");
			}
		}



	}

	@Override

	public void showProfile() {
		System.out.println("╔═══════════════════════════╗");
		System.out.println("║   YÖNETİCİ PROFİLİ        ║");
		System.out.println("╠═══════════════════════════╣");
		System.out.println("╠Ad         : " + getName());
		System.out.println("╠Soyad      : " + getSurname());
		System.out.println("╠E-posta    : " + getEposta());
		System.out.println("╠Telefon No : " + getTelNo());
		System.out.println("╠Memleket   : " + getHomeTown());
		System.out.println("╚═══════════════════════════╝");
	}


}




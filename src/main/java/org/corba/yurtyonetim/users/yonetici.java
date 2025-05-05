package org.corba.yurtyonetim.users;

public class yonetici extends User {

	@Override
	public boolean logIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void editProfile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	
		public void showProfile() {
		    System.out.println("╔═══════════════════════════╗");
		    System.out.println("║   YÖNETİCİ PROFİLİ        ║");
		    System.out.println("╠═══════════════════════════╣");
		    System.out.println("╠Ad         : " + getName());
		    System.out.println("╠Soyad      : " + getSurname());
		    System.out.println("╠E-posta    : " + getEmail());
		    System.out.println("╠Telefon No : " + getTelNo());
		    System.out.println("╠Memleket   : " + getHomeTown());
		    System.out.println("╚═══════════════════════════╝");
		}

		
	}

	



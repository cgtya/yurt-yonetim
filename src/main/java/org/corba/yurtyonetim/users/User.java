package org.corba.yurtyonetim.users;

public abstract class User {
	private String name;
	private String surname;
	private String email;
	private String telNo;
	private String tcNo;

	public User(String email,String name,String surname,String tcNo,String telNo) {
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.tcNo = tcNo;
		this.telNo = telNo;
	}




	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}


	public String getTcNo() {
		return tcNo;
	}
	public void setTcNo(String tcNo) {
		this.tcNo = tcNo;
	}


	
}

package org.corba.yurtyonetim.users;

public abstract class User {
	private String name;
	private String surname;
	private String eposta;
	private String telNo;
	private String tcNo;

	public User(String name,String surname,String tcNo,String telNo, String eposta) {
		this.eposta = eposta;
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

	public String getEposta() {
		return eposta;
	}
	public void setEmail(String eposta) {
		this.eposta = eposta;
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

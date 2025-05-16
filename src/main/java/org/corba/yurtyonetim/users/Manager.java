package org.corba.yurtyonetim.users;

public class Manager extends User {

    private String password;

     public Manager(String name,String Surname,String tcNo,String telNo,String eposta,String password){
         super(name, Surname, tcNo, telNo, eposta);
         this.password=password;
     }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
package org.corba.yurtyonetim.users;

public class Manager extends User {
    private String password;

     public Manager(String email,String name,String password,String Surname,String tcNo,String telNo){
         super(email, name, Surname, tcNo,telNo);
         this.password=password;
     }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void deleteStudent(){

    }

    public void addStudent(){

    }public void searchStudent(){

    }
    public void listStudents(){

    }
    public void showUserInfo(){

    }

}

package org.corba.yurtyonetim.users;

public class Student extends User {
    private String currentDorm;
    public Student(String email,String name,String Surname,String tcNo,String telNo,String currentDorm){
       super(email, name, Surname, tcNo, telNo);
       this.currentDorm=currentDorm;

    }


    public String getCurrentDorm() {
        return currentDorm;
    }

    public void setCurrentDorm(String currentDorm) {
        this.currentDorm = currentDorm;
    }
}

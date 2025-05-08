package org.corba.yurtyonetim.users;

public class Student extends User {
    private String currentDorm;
    private String roomNo;
    private int diciplineNo;
    private boolean isOnleave;
    public Student(String email,String name,String Surname,String tcNo,String telNo,String currentDorm,String roomNo,int diciplineNo,boolean isOnleave){
       super(email, name, Surname, tcNo, telNo);
       this.currentDorm=currentDorm;
       this.roomNo=roomNo;
       this.isOnleave=isOnleave;

    }


    public String getCurrentDorm() {
        return currentDorm;
    }

    public void setCurrentDorm(String currentDorm) {
        this.currentDorm = currentDorm;
    }

    public void makeBecais(){

    }
    public void requestTransfer(){
        // transfer talebi
    }


}

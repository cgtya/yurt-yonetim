package org.corba.yurtyonetim.users;

public class Student extends User {
    private String currentDorm;
    private int diciplineNo=0;
    private boolean isOnleave;

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


}
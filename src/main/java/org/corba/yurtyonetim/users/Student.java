package org.corba.yurtyonetim.users;

public class Student extends User {
    private String currentDorm;
    private String roomNo;
    private int diciplineNo;
    private boolean isOnleave;
    private static final int TOTAL_LEAVE_DAYS = 45; // Toplam izin hakkı
    private int usedLeaveDays; // Kullanılan izin günü


    public Student(String email, String name, String surname, String tcNo, String telNo,
              String currentDorm, String roomNo, int diciplineNo, boolean isOnleave, 
              int totalLeaveDays) {
    super(email, name, surname, tcNo, telNo);
    this.currentDorm = currentDorm;
    this.roomNo = roomNo;
    this.diciplineNo = diciplineNo;
    this.isOnleave = isOnleave;
    this.totalLeaveDays = totalLeaveDays;
    this.usedLeaveDays = 0;
}


    public String getCurrentDorm() {
        return currentDorm;
    }

    public void setCurrentDorm(String currentDorm) {
        this.currentDorm = currentDorm;
    }

    public void makeBecais(){

    }
    public void requestTransfer() {
    // Öğrencinin mevcut durumunu kontrol et
    if (isOnleave) {
        throw new IllegalStateException("İzinli öğrenci yurt değişikliği talebinde bulunamaz.");
    }
    
    if (diciplineNo > 0) {
        throw new IllegalStateException("Disiplin cezası olan öğrenci yurt değişikliği talebinde bulunamaz.");
    }
    
    // TODO: Transfer talebini sisteme kaydet
    // - Öğrencinin bilgileri (TC No, Ad, Soyad)
    // - Mevcut yurt bilgisi (currentDorm)
    // - Talep tarihi
    // - Talep durumu (beklemede)
    
    System.out.println("Yurt değişikliği talebiniz alınmıştır. " +
            "Mevcut yurt: " + currentDorm + 
            ", Oda No: " + roomNo);
}
   
    public boolean giveLeave(){
       return true;
    }
    public void enteringDicipline(){

    }
    public int isOnLeave() {
        int remainingDays = TOTAL_LEAVE_DAYS - usedLeaveDays;
        
        if (remainingDays < 0) {
            remainingDays = 0;
        }
        
        System.out.println("Toplam izin hakkı: " + TOTAL_LEAVE_DAYS + " gün");
        System.out.println("Kullanılan izin: " + usedLeaveDays + " gün");
        System.out.println("Kalan izin hakkı: " + remainingDays + " gün");
        
        return remainingDays;
    }

// Kullanılan izin günü sayısını artırmak için yardımcı metod
public void addUsedLeaveDays(int days) {
    if (days < 0) {
        throw new IllegalArgumentException("İzin günü negatif olamaz!");
    }
    this.usedLeaveDays += days;
}


}
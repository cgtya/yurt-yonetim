package org.corba.yurtyonetim.users;

public class Student extends User {
    private String currentDorm;
    private int diciplineNo=0;
    private boolean isOnleave;
    private static final int TOTAL_LEAVE_DAYS = 45; // Toplam izin hakkı
    private int usedLeaveDays; // Kullanılan izin günü
    private int totalLeaveDays;

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


    public int getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(int totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public int getUsedLeaveDays() {
        return usedLeaveDays;
    }

    public void setUsedLeaveDays(int usedLeaveDays) {
        this.usedLeaveDays = usedLeaveDays;
    }
    public String getCurrentDorm() {
        return currentDorm;
    }

    public void setCurrentDorm(String currentDorm) {
        this.currentDorm = currentDorm;
    }




    public Student(String email, String name, String surname, String tcNo, String telNo,
              String currentDorm, String roomNo, int diciplineNo, boolean isOnleave, 
              int totalLeaveDays) {
    super(email, name, surname, tcNo, telNo);
    this.currentDorm = currentDorm;
    this.diciplineNo = diciplineNo;
    this.isOnleave = isOnleave;
    this.totalLeaveDays = totalLeaveDays;
    this.usedLeaveDays = 0;
}




    public boolean makeBecayis(Student otherStudent) {
        // TC kimlik numaralarını kontrol et
        if (this.getTcNo() == null || otherStudent.getTcNo() == null) {
            System.out.println("TC kimlik numaraları eksik!");
            return false;
        }

        // Öğrencilerin yurt bilgilerini kontrol et
        if (this.currentDorm == null || otherStudent.currentDorm == null) {
            System.out.println("Yurt bilgileri eksik!");
            return false;
        }

        // Geçici bir değişkende ilk öğrencinin yurdunu sakla
        String tempDorm = this.currentDorm;
        
        // Yurt değişimini gerçekleştir
        this.currentDorm = otherStudent.currentDorm;
        otherStudent.currentDorm = tempDorm;

        System.out.println("Becayiş işlemi başarıyla gerçekleşti.");
        System.out.println(this.getName() + " -> " + this.currentDorm);
        System.out.println(otherStudent.getName() + " -> " + otherStudent.currentDorm);
        
        return true;
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
            "Mevcut yurt: " + currentDorm );
}
   
public boolean giveLeave() {
    int remainingDays = TOTAL_LEAVE_DAYS - usedLeaveDays;
    
    if (remainingDays <= 0) {
        System.out.println("Uyarı: İzin talebiniz reddedildi!");
        System.out.println("Sebep: İzin hakkınız kalmadı.");
        System.out.println("Toplam izin hakkı: " + TOTAL_LEAVE_DAYS + " gün");
        System.out.println("Kullanılan izin: " + usedLeaveDays + " gün");
        return false;
    }
    
    System.out.println("İzin talebiniz onaylandı!");
    System.out.println("Kalan izin hakkınız: " + remainingDays + " gün");
    return true;
}
public void enteringDicipline(Manager manager) {
    this.diciplineNo++;
    System.out.println("Öğrenci " + this.getName() + " için yeni disiplin cezası eklendi.");
    System.out.println("Toplam disiplin cezası sayısı: " + this.diciplineNo);
    
    if (this.diciplineNo >= 3) {
        System.out.println("Öğrenci " + this.getName() + " 3 disiplin cezasını doldurdu!");
        System.out.println("Yurttan çıkarılma işlemi başlatılıyor...");
        manager.deleteStudent(this);
    }
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




}
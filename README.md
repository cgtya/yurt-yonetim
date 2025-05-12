# Yurt Yönetim Sistemi Proje Ödevi

## Kurulum
>### Gereksinimler
> Gerekli programların ve dosyaların yüklü olduğundan emin olun!
> Kullandığınız bilgisayarın plaformu için olan dosyaları indirip yükleyiniz.
>- Java 21 JDK https://adoptium.net/temurin/releases/?version=21&package=jdk
>- JavaFX SDK (21.0.7) https://gluonhq.com/products/javafx/
>- MySQL Server https://dev.mysql.com/downloads/mysql/

### Adım 1
Tüm kaynak dosyalarını indirip istediğiniz bir klasöre koyun.

Örneğin `Masaüstü/yurt-yonetim`

### Adım 2
Kaynak dosyaların olduğu klasörün içinde bir terminal penceresi açın.

Bizim örneğimiz için `yurt-yonetim` klasörü.
### Adım 3
Terminal penceresi içinde;
- Eğer Windows kullanıyorsanız `./mvnw.bat clean package`
- Linux / Mac kullanıyorsanız  `./mvnw clean package`

komutunu çalıştırınız.

### Adım 4

Kaynak klasörünün içinde oluşacak target klasörüne girin ve `yurt-yonetim-1.0-SNAPSHOT.jar` dosyasının oluştuğundan emin olun.  
Bu `.jar` dosyasını istediğiniz bir klasöre koyabilirsiniz.

Biz şimdilik olduğu yerde bırakıyoruz. `yurt-yonetim/target/yurt-yonetim-1.0-SNAPSHOT.jar`

### Adım 5
İndirdiğiniz JavaFX SDK dosyalarını `yurt-yonetim-1.0-SNAPSHOT.jar` dosyasının da bulunduğu klasöre koyun.

Bizim örneğimiz için `yurt-yonetim/target/javafx-sdk-21.0.7` klasörü.

### Adım 6 - *Programı Başlatma*

`yurt-yonetim-1.0-SNAPSHOT.jar` dosyanızın ve JavaFX dosyalarının bulunduğu klasörde bir terminal penceresi açın.

Bizim örneğimiz için `yurt-yonetim/target` klasörü.

### Adım 7
Bu terminal penceresinde;  
`java --module-path <javafx klasörü içerisindeki lib klasörünün yeri> --add-modules javafx.controls,javafx.fxml -jar <jar dosyamız>`  
komutunu doğru argümanlarla çalıştırın.

Bizim örneğimizde:  
`java --module-path javafx-sdk-21.0.7/lib --add-modules javafx.controls,javafx.fxml -jar yurt-yonetim-1.0-SNAPSHOT.jar`

## Veritabanı kurulumu
MySQL Sunucusunu kurarken belirlediğiniz kullanıcı adı ve şifreyi not ediniz.

Programı açtığınızda gerekli yerleri bu bilgilerle doldurunuz.

Veritabanı bağlantısı başarılı bir şekilde kurulduğunda `Veritabanı Bağlantısı: Başarılı` yazısını göreceksiniz.

### İlk kurulum  

Eğer sıfırdan yeni bir veritabanı oluşturuluyorsa;  

>- Varsayılan yönetici kullanıcı adı ve şifresi oluşturulacaktır.
>
>  - Kullanıcı adı: 11111111111
>  - Şifre: admin

>- Giriş ekranında örnek veri yükleme tuşu belirecektir.
>
>  - Bu tuş sonraki girişlerde gözükmeyecektir.
>  - Yüklenen örnek veriyi `/src/main/resources/exampledata.sql` konumunda görüntüleyebilirsiniz.



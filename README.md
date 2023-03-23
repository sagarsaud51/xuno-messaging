
# XUNO SIMPLE MESSAGING

This project will show a basic private one-to-one communication system written in JAVA Spring Boot JDK 11.



#  NOTE
#### USER AUTH module has not been integrated. So for this we are sending sent userID for each request


# To Run this application


- Make sure you have MySql Database running in your system
- in ```src/main/resources/application.properties``` Please update ```spring.datasource.url```, ```spring.datasource.username```, and ```spring.datasource.password```


The POSTMAN collection for this APIs could be found in the ```XUNO_MESSAGE.json``` on the root folder



## FUN FACT

This class is responsible to encrypt decrypt message which will be stored in database.
To use this feature all we need to do is add an annotation on the entity class.

For example

```JAVA
    @Column(nullable = false)
    @Convert(converter = MessageEncrypt.class)
    private String content;
```

```JAVA
@Component
public class EncryptionUtil {
    @Value("${message.encryption.key}")
    private String key;
    @Value("${message.encryption.initVector}")
    private String initVector;
    private final String algo = "AES/CBC/PKCS5PADDING";

    public String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
```


## Explain how you would scale the PoC to support, 100000 concurrent users.

In order to handle the load, it's much more efficient to use come kind of caching (REDIS) 
and have multiple instances of the service. 

### If the API needs to accommodate group messaging in addition to 1-on-1, how would you go about doing it?
For this we need to have participants array ```List<User>``` in one of the entity.


### Explain how you would secure the messaging channel if that was asked for the next version?
SSL Maybe? And  RSA encryptions

### Add my feature ideas that you have to improve the API beyond the initial version.
Attach files, images, videos, live calls and web sockets, integration with 3rd party services (like WhatsApp, Facebook) via webhooks




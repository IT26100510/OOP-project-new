package lk.student.pizza.service;

import lk.student.pizza.model.Customer;
import lk.student.pizza.repository.FileDataStore;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.*;

@Service
public class AuthService {
    private static final String FILE = "users.txt";
    private final FileDataStore store;
    private final CustomerService customers;
    private final SecureRandom random = new SecureRandom();
    public AuthService(FileDataStore store, CustomerService customers) { this.store=store; this.customers=customers; }

    public Optional<Map<String,String>> authenticate(String email, String password) {
        if (email == null || password == null) return Optional.empty();
        if ("admin@pizzahouse.lk".equalsIgnoreCase(email.trim()) && "Admin@123".equals(password))
            return Optional.of(Map.of("role","ADMIN","name","Administrator","email","admin@pizzahouse.lk"));
        return store.read(FILE).stream().map(this::parse).filter(a -> a.get("email").equalsIgnoreCase(email.trim()))
            .filter(a -> verify(password, a.get("salt"), a.get("hash"))).findFirst();
    }

    public boolean register(String name, String email, String phone, String address, String password) {
        String normalized=email.trim().toLowerCase(Locale.ROOT);
        if (normalized.isBlank() || password == null || password.length()<6 || normalized.equals("admin@pizzahouse.lk")) return false;
        if (store.read(FILE).stream().map(this::parse).anyMatch(a -> a.get("email").equalsIgnoreCase(normalized))) return false;
        byte[] salt=new byte[16]; random.nextBytes(salt); String saltText=Base64.getEncoder().encodeToString(salt);
        String hash=hash(password,salt);
        Map<String,String> account=new LinkedHashMap<>(); account.put("email",normalized);account.put("name",name);account.put("phone",phone);account.put("address",address);account.put("salt",saltText);account.put("hash",hash);
        store.write(FILE, append(store.read(FILE), encode(account)));
        customers.save(new Customer(null,name,normalized,phone,address));
        return true;
    }
    private List<String> append(List<String> list,String value){list.add(value);return list;}
    private String encode(Map<String,String> a){return String.join("|",a.get("email"),safe(a.get("name")),safe(a.get("phone")),safe(a.get("address")),a.get("salt"),a.get("hash"));}
    private Map<String,String> parse(String line){String[] a=line.split("\\|",-1);Map<String,String> m=new HashMap<>();m.put("email",a[0]);m.put("name",a.length>1?a[1]:"");m.put("phone",a.length>2?a[2]:"");m.put("address",a.length>3?a[3]:"");m.put("salt",a.length>4?a[4]:"");m.put("hash",a.length>5?a[5]:"");m.put("role","CUSTOMER");return m;}
    private String safe(String s){return s==null?"":s.replace("|","/").replace("\n"," ");}
    private String hash(String password,byte[] salt){try{PBEKeySpec spec=new PBEKeySpec(password.toCharArray(),salt,120000,256);byte[] result=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();spec.clearPassword();return Base64.getEncoder().encodeToString(result);}catch(Exception e){throw new IllegalStateException(e);}}
    private boolean verify(String password,String salt,String expected){try{return java.security.MessageDigest.isEqual(Base64.getDecoder().decode(expected),Base64.getDecoder().decode(hash(password,Base64.getDecoder().decode(salt))));}catch(Exception e){return false;}}
}

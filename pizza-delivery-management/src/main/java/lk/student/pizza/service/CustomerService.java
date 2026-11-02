package lk.student.pizza.service;

import lk.student.pizza.model.Customer;
import lk.student.pizza.repository.FileDataStore;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private static final String FILE="customers.txt";
    private final FileDataStore store;
    public CustomerService(FileDataStore store){this.store=store;}
    public List<Customer> findAll(){return store.read(FILE).stream().map(this::fromLine).collect(Collectors.toList());}
    public Optional<Customer> findById(String id){return findAll().stream().filter(c->c.getId().equals(id)).findFirst();}
    public Optional<Customer> findByEmail(String email){return findAll().stream().filter(c->c.getEmail().equalsIgnoreCase(email)).findFirst();}
    public void save(Customer c){
        List<Customer> all=findAll();
        if(c.getId()==null||c.getId().isBlank()) c.setId("C"+String.format("%03d",new Random().nextInt(900)+100));
        all.removeIf(x->x.getId().equals(c.getId())); all.add(c); store.write(FILE,all.stream().map(this::toLine).collect(Collectors.toList()));
    }
    public void delete(String id){List<Customer> all=findAll();all.removeIf(c->c.getId().equals(id));store.write(FILE,all.stream().map(this::toLine).collect(Collectors.toList()));}
    private String toLine(Customer c){return String.join("|",safe(c.getId()),safe(c.getName()),safe(c.getEmail()),safe(c.getPhone()),safe(c.getAddress()));}
    private Customer fromLine(String s){String[] a=s.split("\\|",-1);return new Customer(a[0],a[1],a[2],a[3],a.length>4?a[4]:"");}
    private String safe(String s){return s==null?"":s.replace("|","/").replace("\n"," ");}
}

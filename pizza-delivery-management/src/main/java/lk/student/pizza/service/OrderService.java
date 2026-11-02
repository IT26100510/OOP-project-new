package lk.student.pizza.service;

import lk.student.pizza.model.CustomerOrder;
import lk.student.pizza.repository.FileDataStore;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String FILE="orders.txt";
    private final FileDataStore store;
    public OrderService(FileDataStore store){this.store=store;}
    public List<CustomerOrder> findAll(){return store.read(FILE).stream().map(this::fromLine).collect(Collectors.toList());}
    public void place(CustomerOrder o){
        o.setId("O"+String.format("%03d",new Random().nextInt(900)+100)); o.setStatus("Received");
        List<CustomerOrder> all=findAll(); all.add(o); saveAll(all);
    }
    public void updateStatus(String id,String status){List<CustomerOrder> all=findAll();all.stream().filter(o->o.getId().equals(id)).findFirst().ifPresent(o->o.setStatus(status));saveAll(all);}
    public void delete(String id){List<CustomerOrder> all=findAll();all.removeIf(o->o.getId().equals(id));saveAll(all);}
    private void saveAll(List<CustomerOrder> all){store.write(FILE,all.stream().map(this::toLine).collect(Collectors.toList()));}
    private String toLine(CustomerOrder o){return String.join("|",o.getId(),safe(o.getCustomerId()),safe(o.getCustomerName()),safe(o.getPizzaName()),String.valueOf(o.getQuantity()),String.valueOf(o.getTotal()),safe(o.getStatus()));}
    private CustomerOrder fromLine(String s){String[] a=s.split("\\|",-1);return new CustomerOrder(a[0],a[1],a[2],a[3],Integer.parseInt(a[4]),Double.parseDouble(a[5]),a[6]);}
    private String safe(String s){return s==null?"":s.replace("|","/").replace("\n"," ");}
}

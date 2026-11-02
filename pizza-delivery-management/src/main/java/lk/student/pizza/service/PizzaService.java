package lk.student.pizza.service;

import lk.student.pizza.model.*;
import lk.student.pizza.repository.FileDataStore;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PizzaService {
    private static final String FILE = "pizzas.txt";
    private final FileDataStore store;
    public PizzaService(FileDataStore store) {
        this.store = store;
        if (store.read(FILE).isEmpty()) {
            saveAll(List.of(
                new Pizza("P001","Margherita","Tomato, mozzarella and fresh basil",1800,true,"Medium"),
                new Pizza("P002","Pepperoni","Mozzarella with classic pepperoni",2200,true,"Medium"),
                new Pizza("P003","Four Cheese","A blend of four delicious cheeses",2400,true,"Medium"),
                new Pizza("P004","Veggie Supreme","Bell peppers, olives, onion and mushrooms",2100,true,"Medium"),
                new Pizza("P005","BBQ Chicken","BBQ chicken, onion and mozzarella",2500,true,"Medium")
            ));
        }
    }
    public List<MenuItem> findAll() {
        // The collection uses the MenuItem parent type: Pizza objects are handled polymorphically.
        return store.read(FILE).stream().map(this::fromLine).collect(Collectors.toList());
    }
    public Optional<MenuItem> findById(String id) { return findAll().stream().filter(p -> p.getId().equals(id)).findFirst(); }
    public void save(Pizza pizza) {
        List<MenuItem> all = findAll();
        if (pizza.getId() == null || pizza.getId().isBlank()) pizza.setId("P" + String.format("%03d", new Random().nextInt(900)+100));
        all.removeIf(p -> p.getId().equals(pizza.getId()));
        all.add(pizza);
        saveAll(all);
    }
    public void delete(String id) { List<MenuItem> all=findAll(); all.removeIf(p -> p.getId().equals(id)); saveAll(all); }
    private void saveAll(List<? extends MenuItem> items) { store.write(FILE, items.stream().map(this::toLine).collect(Collectors.toList())); }
    private String toLine(MenuItem p) {
        Pizza pizza=(Pizza)p;
        return String.join("|", safe(p.getId()), safe(p.getName()), safe(p.getDescription()),
            String.valueOf(p.getPrice()), String.valueOf(p.isAvailable()), safe(pizza.getSize()));
    }
    private Pizza fromLine(String line) {
        String[] a=line.split("\\|",-1);
        return new Pizza(a[0],a[1],a[2],Double.parseDouble(a[3]),Boolean.parseBoolean(a[4]),a.length>5?a[5]:"Medium");
    }
    private String safe(String s) { return s==null?"":s.replace("|","/").replace("\n"," "); }
}

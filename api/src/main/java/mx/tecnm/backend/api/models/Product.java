package mx.tecnm.backend.api.models;

public class Product{
    public String name;
    public String codeBar;
    public double price;

    public Product(String name, String codeBar, double price){
        this.name = name;
        this.codeBar = codeBar;
        this.price = price;
    }

    public String getName() { return name; }
    public String getCodeBar() { return codeBar; }
    public double getPrice() { return price; }
}
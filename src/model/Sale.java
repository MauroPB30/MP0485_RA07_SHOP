package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Sale {

    String client;
    static ArrayList<Product> products = new ArrayList<>();
    Amount amount;
//
//    public Sale(String client, Product[] products, Amount amount) {
//        super();
//        this.client = client;
//        this.products = products;
//        this.amount = amount;
//    }

    public Sale(String client, Amount amount) {
        this.client = client;
        this.amount = amount;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = Sale.products;
    }

    public Amount getAmount() {
        return this.amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

//    @Override
//    public String toString() {
//        return "Sale [client=" + client + ", products=" + Arrays.toString(products) + ", amount=" + amount + "]";
//    }

    @Override
    public String toString() {
        return "Sale{" + "client=" + client + ", amount=" + amount + '}';
    }

}

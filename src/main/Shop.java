package main;

import java.util.ArrayList;
import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;
import model.Client;
import model.Employee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import model.Fichero;

public class Shop {

    private Amount cash = new Amount(100.00);
    private static ArrayList<Product> inventory = new ArrayList<>();
    private ArrayList<Sale> sales = new ArrayList<>();
    static ArrayList<Employee> employees = new ArrayList<>();

    final static double TAX_RATE = 1.04;

//Constructor
    public Shop() {
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

//        Employee employ = new Employee(0, "", "");
        Shop shop = new Shop();
        shop.initSession();
        shop.loadInventory(inventory);

        Scanner scanner = new Scanner(System.in);

        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("\n");
            System.out.println("===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) A\u00f1adir producto");
            System.out.println("3) A\u00f1adir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver total de ventas");
            System.out.println("9) Eliminar producto del inventario");
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opcion: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

                case 2:
                    shop.addProduct();
                    break;

                case 3:
                    shop.addStock();
                    break;

                case 4:
                    shop.setExpired();
                    break;

                case 5:
                    shop.showInventory();
                    break;

                case 6:
                    shop.sale();
                    break;

                case 7:
                    shop.showSales();
                    break;
                case 8:
                    shop.showTotalSales();
                    break;
                case 9:
                    shop.deleteProduct();
                    break;
// Solución #4 se asigna al caso el numero 10 para que reciba la orden de salida.
                case 10:
                    exit = true;
                    break;
            }
        } while (!exit);

    }

    /**
     * load initial inventory to shop
     */
//    public void loadInventory() {
//
//        inventory.clear();   // evita duplicados si se vuelve a cargar
//
//        Fichero.LeerInventario(inventory);
//
//        System.out.println("Productos cargados: " + inventory.size());
//    }
    public void loadInventory(ArrayList<Product> inventory) {

        Fichero.LeerInventario(inventory);

        System.out.println("Productos cargados: " + inventory.size());
    }

    /**
     * show current total cash
     */
    public void showCash() {
        System.out.println("Dinero actual: " + cash);
    }

    /**
     * add a new product to inventory getting data from console
     */
    public void addProduct() {

        Scanner sc = new Scanner(System.in);

        System.out.print("Nombre: ");
        String name = sc.nextLine();

        if (findProduct(name) != null) {
            System.out.println("El producto ya existe.");
            return;
        }

        System.out.print("Precio mayorista: ");
        double wholesalerPrice = sc.nextDouble();

        System.out.print("Stock: ");
        int stock = sc.nextInt();

        inventory.add(new Product(name, wholesalerPrice, true, stock));
        System.out.println("Producto añadido correctamente");
    }

    /**
     * add stock for a specific product
     */
    public void addStock() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();

        Product product = findProduct(name);

        if (product != null) {
            // ask for stock
            System.out.print("Seleccione la cantidad a a\u00f1adir: ");
            int stock = scanner.nextInt();
            // update stock product

            product.setStock(product.getStock() + stock);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    /**
     * set a product as expired
     */
    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();

        Product product = findProduct(name);

        if (product != null) {
            product.expire();
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
        } else {
            System.out.println("El producto no existe");
        }

    }

    /**
     * show all inventory
     */
    public void showInventory() {

        if (inventory.isEmpty()) {
            System.out.println("Inventario vacío.");
            return;
        }
        
        System.out.println("\n");
        System.out.println("Inventario actual:");
        System.out.println("\n");
        for (Product p : inventory) {
            System.out.println(p.getName()+"| "+ p.getWholesalerPrice()+"| "+ p.getStock());
        }
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        ArrayList<Product> products = new ArrayList<>();

        System.out.println("Realizar venta, escribir nombre cliente");
        String clientName = sc.nextLine();

        Client client = new Client(clientName);
        double totalAmount = 0.0;

        while (true) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            String productName = sc.nextLine();

            if (productName.equals("0")) {
                break;
            }
            Product product = findProduct(productName);

            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {

                products.add(product);
                totalAmount += product.getPublicPrice().getValue();

                product.setStock(product.getStock() - 1);
                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto a\u00f1adido con exito");

            } else {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        // show cost total
        totalAmount *= TAX_RATE;
        Amount totalAmountObj = new Amount(totalAmount);

        boolean payCheck = client.pay(totalAmountObj);

        sales.add(new Sale(client, products, totalAmountObj));

        if (payCheck) {
            cash.setValue(cash.getValue() + totalAmountObj.getValue());
            System.out.println("Venta realizada con exito, total: " + totalAmountObj);
            System.out.println("Saldo cliente: " + client.getBalance());
        } else {
            cash.setValue(cash.getValue() + totalAmountObj.getValue());
            System.out.println("Venta realizada pero cliente con deuda.");
            System.out.println("Cantidad a deber: " + Math.abs(client.getBalance().getValue()) + " euro");
        }

    }

    /**
     * show all sales
     */
    private void showSales() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Lista de ventas:");

        for (Sale sale : sales) {
            System.out.println(sale.toString());
        }
        
        System.out.println("Quieres exportar las ventas");
        String answer = sc.nextLine();
        
        if (answer.equalsIgnoreCase("Si")){
            Fichero.EscribirInventario(sales);
        } 

    }

    public void showTotalSales() {
        double totalSales = 0.0;

        for (Sale sale : sales) {
            totalSales += sale.getAmount().getValue();
        }

        System.out.println("Total de todas las ventas: " + totalSales);
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
//    public void addProduct(Product product) {
//        if (isInventoryFull()) {
//            System.out.println("No se pueden a\u00f1adir mas productos, se ha alcanzado el maximo de " + numberProducts);
//            return;
//        }
//        inventory.set(numberProducts, product);
//        numberProducts++;
//    }
//
//    /**
//     * check if inventory is full or not
//     *
//     * @return true if inventory is full
//     */
//    public boolean isInventoryFull() {
//        if (numberProducts == 10) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * find product by name
     *
     * @param name
     * @return product found by name
     */
    public Product findProduct(String name) {
        for (Product product : inventory) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    private void deleteProduct() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Seleccione el nombre del producto");
        String prDel = sc.nextLine();

        Product product = findProduct(prDel);

        if (product != null) {
            inventory.remove(product);
            System.out.println("Producto eliminado correctamente");
        } else {
            System.out.println("Error: Producto no encontrado");
        }
    }

    public void initSession() {

        Scanner sc = new Scanner(System.in);
        boolean logged = false;
        Employee emp = new Employee(0, "", "test");

// ES RECOMENDABLE USAR UN DO WHILE YA QUE NO ES FUNCIONAL QUE UN METODO SE LLAME A SI MISMO
        do {
            System.out.println("Ingrese el ID de empleado");
            int empId = sc.nextInt();
            sc.nextLine();

            System.out.println("Ingrese la contraseña del empleado");
            String psw = sc.nextLine();

            if (emp.login(empId, psw)) {
                logged = true;
                System.out.println("Login correcto");
            } else {
                System.out.println("Usuario o contraseña incorrectos");
            }
        } while (!logged);
    }
    
    public Amount getCash(){
        return cash;
    }
           
}

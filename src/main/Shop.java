package main;

import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;

public class Shop {

    private Amount cash = new Amount(100.00);
    private Product[] inventory;
    private int numberProducts;
    public Sale[] sales;
    // Se agregan estos campos para pruebas del bug #7
    private int numberSales;

    final static double TAX_RATE = 1.04;
//Constructor

    public Shop() {
        inventory = new Product[10];
        // Se agregan estos campos para pruebas del bug #7
        sales = new Sale[20];
        numberSales = 0;

    }

    public static void main(String[] args) {
        Shop shop = new Shop();

        shop.loadInventory();

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
    public void loadInventory() {
        addProduct(new Product("Manzana", 5.00, true, 10));
        addProduct(new Product("Pera", 5.00, true, 20));
        addProduct(new Product("Hamburguesa", 10.00, true, 30));
        addProduct(new Product("Fresa", 3.00, true, 20));
    }

    /**
     * show current total cash
     */
    private void showCash() {
// Solución #1 se agrega el + cash al sout para que muestre el valor.
        System.out.println("Dinero actual: " + cash.toString());
    }

    /**
     * add a new product to inventory getting data from console
     */
    public void addProduct() {
        if (isInventoryFull()) {
            System.out.println("No se pueden a\u00f1adir mas productos");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
// Se corrige el error del punto #3 que agregaba valores duplicados
        if (findProduct(name) != null) {
            System.out.println("El producto ya existe.");
            return;
        }

//        System.out.println("Precio Publico");
//        double publicPrice = scanner.nextDouble();
        System.out.print("Precio mayorista: ");
        double wholesalerPrice = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        addProduct(new Product(name, wholesalerPrice, true, stock));
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
// Solucion #5 Se suma el stock nuevo al existente 
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
        System.out.println("Contenido actual de la tienda:");
        for (Product product : inventory) {
            if (product != null) {
// Solucion #2: se adiciona un toString en la clase product y se printa aca.
                System.out.println(product.toString());
            }
        }
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        Product[] p = new Product[10];
        System.out.println("Realizar venta, escribir nombre cliente");
        String client = sc.nextLine();

        // sale product until input name is not 0
        double totalAmount = 0.0;
        String name = "";
        int c = 0;
        while (!name.equals("0") && c != p.length) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }
            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {
                productAvailable = true;
                totalAmount += product.getPublicPrice().getValue();
                product.setStock(product.getStock() - 1);
                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto a\u00f1adido con exito");

                p[c] = product;
                c++;
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        // show cost total
        totalAmount = totalAmount * TAX_RATE;
        Amount totalAmountObj = new Amount(totalAmount);
        sales[numberSales] = new Sale(client, p, totalAmountObj);
        numberSales++;
        cash.setValue(cash.getValue() + totalAmount);
        System.out.println("Venta realizada con exito, total: " + totalAmountObj.toString());
    }

    /**
     * show all sales
     */
    private void showSales() {
        System.out.println("Lista de ventas:");
        for (int i = 0; i < numberSales; i++) {
            System.out.println(sales[i].toString());
        }
    }

    public void showTotalSales() {
        double totalSales = 0.0;

        for (int i = 0; i < numberSales; i++) {
            totalSales += sales[i].getAmount().getValue();
        }

        System.out.println("Total de todas las ventas: " + totalSales);
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public void addProduct(Product product) {
        if (isInventoryFull()) {
            System.out.println("No se pueden a\u00f1adir mas productos, se ha alcanzado el maximo de " + inventory.length);
            return;
        }
        inventory[numberProducts] = product;
        numberProducts++;
    }

    /**
     * check if inventory is full or not
     *
     * @return true if inventory is full
     */
    public boolean isInventoryFull() {
        if (numberProducts == 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * find product by name
     *
     * @param name
     * @return product found by name
     */
    public Product findProduct(String name) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null && inventory[i].getName().equals(name)) {
                return inventory[i];
            }
        }
        return null;
    }

}

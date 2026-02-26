/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author usuario
 */
public class Fichero {

    public static void EscrituraFichero(ArrayList<Person> Personas) {

        FileWriter outputPerson;
        BufferedWriter out1 = null;

    }

    public static void FileReader(ArrayList<Product> inventory) {

        String line;

        String name;
        Amount wholesalesPrice;
        int stock;

        FileReader inputS = null;
        BufferedReader in = null;

        try {
            inputS = new FileReader("inputInventory.txt");
            in = new BufferedReader(inputS);

            while ((line = in.readLine()) != null) {

                String data[] = line.split(";");

                name = data[0];
                wholesalesPrice = new Amount(Double.parseDouble(data[1]));
                stock = Integer.parseInt(data[2]);

                for (int i = 0; i < data.length - 1; i++) {
                    String finalDatos[] = line.split(":");
//                finalDatos = (data);

                    name = finalDatos[1];
                    wholesalesPrice = new Amount(Double.parseDouble(finalDatos[3]));
                    stock = Integer.parseInt(data[5]);

                    Product p = new Product(name, wholesalesPrice, stock);
                    inventory.add(p);
                }

            }

        } catch (java.io.IOException ex) {
            System.out.println("No se puede acceder al archivo.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    System.out.println("No se puede acceder al archivo.");
                }
            }
        }

    }
}
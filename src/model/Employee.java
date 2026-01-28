/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author usuario
 */
public class Employee extends Person{
    
    private int employeeId;
    private String password;
    
//    private static final String PASSWORD: "test";
//    private static final int EMPLOYEEID: 123;

    public Employee(int employeeId, String password, String name) {
        super(name);
        this.employeeId = employeeId;
        this.password = password;
    }


    
    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Get the value of employee
     *
     * @return the value of employee
     */
    public int getEmployee() {
        return employeeId;
    }

    /**
     * Set the value of employee
     *
     * @param employee new value of employee
     */
    public void setEmployee(int employee) {
        this.employeeId = employee;
    }

    @Override
    public String toString() {
        return "Employee{" + "employee=" + employeeId + ", password=" + password + '}';
    }
    
}

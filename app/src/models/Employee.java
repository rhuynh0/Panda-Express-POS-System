package models;

import java.sql.Date;

/**
 * The Employee class represents an employee in a company.
 * It contains details such as the employee's ID, name, job title, 
 * start date, and hourly wage.
 * 
 * This class provides getter and setter methods to access and modify the employee's details.
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class Employee {
    private int employeeId;
    private String employeeName;
    private String jobTitle;
    private Date startDate;
    private double hourlyWage;

     /**
     * Constructs a new Employee with the specified details.
     * 
     * @param employeeId the unique ID of the employee
     * @param employeeName the name of the employee
     * @param jobTitle the job title of the employee
     * @param startDate the start date of the employee
     * @param hourlyWage the hourly wage of the employee
     */
    public Employee(int employeeId, String employeeName, String jobTitle, Date startDate, double hourlyWage) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.startDate = startDate;
        this.hourlyWage = hourlyWage;
    }

    /**
     * Gets the employee's ID.
     * 
     * @return the employee ID
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee's ID.
     * 
     * @param employeeId the new employee ID
     */
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the employee's name.
     * 
     * @return the employee name
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the employee's name.
     * 
     * @param employeeName the new employee name
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Gets the employee's job title.
     * 
     * @return the job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the employee's job title.
     * 
     * @param jobTitle the new job title
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Gets the employee's start date.
     * 
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the employee's start date.
     * 
     * @param startDate the new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the employee's hourly wage.
     * 
     * @return the hourly wage
     */
    public double getHourlyWage() {
        return hourlyWage;
    }

    /**
     * Sets the employee's hourly wage.
     * 
     * @param hourlyWage the new hourly wage
     */
    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }
}

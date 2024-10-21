package models;

import java.sql.Date;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String jobTitle;
    private Date startDate;
    private double hourlyWage;

    public Employee(int employeeId, String employeeName, String jobTitle, Date startDate, double hourlyWage) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.startDate = startDate;
        this.hourlyWage = hourlyWage;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.hourlyWage = hourlyWage;
    }
}

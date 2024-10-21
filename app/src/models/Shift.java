package models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * The Shift class represents a work shift for an employee.
 * It contains details such as the shift's unique ID, start time, end time, and the date of the shift.
 * 
 * This class provides getter methods to access shift details, 
 * and it overrides the toString() method to return a string representation of the shift.
 * 
 * @author Nicholas Griffin
 * @author Daniel Warren
 * @author Joseph Dillard
 * @author Keshav Dharshan
 * @author Richard Huynh
 */
public class Shift {
    private int shiftID;
    private Timestamp shiftStartTime;
    private Timestamp shiftEndTime;
    private Date shiftDate;

    /**
     * Constructs a new Shift with the specified details.
     * 
     * @param shiftID the unique ID of the shift
     * @param startTime the start time of the shift
     * @param endTime the end time of the shift
     * @param shiftDate the date of the shift
     */
    public Shift(int shiftID, Timestamp startTime, Timestamp endTime, Date shiftDate) {
        this.shiftID = shiftID;
        this.shiftStartTime = startTime;
        this.shiftEndTime = endTime;
        this.shiftDate = shiftDate;
    }

    /**
     * Gets the shift ID.
     * 
     * @return the shift ID
     */
    public int getShiftID() {
        return shiftID;
    }

    /**
     * Gets the start time of the shift.
     * 
     * @return the start time of the shift
     */
    public Timestamp getShiftStartTime() {
        return shiftStartTime;
    }

    /**
     * Gets the end time of the shift.
     * 
     * @return the end time of the shift
     */
    public Timestamp getShiftEndTime() {
        return shiftEndTime;
    }

    /**
     * Gets the date of the shift.
     * 
     * @return the shift date
     */
    public Date getShiftDate() {
        return shiftDate;
    }

    /**
     * Returns a string representation of the shift.
     * 
     * @return a string representation of the shift
     */
    @Override
    public String toString() {
        return "Shift{" +
                "shiftID='" + shiftID + '\'' +
                ", shiftStartTime=" + shiftStartTime +
                ", shiftEndTime='" + shiftEndTime + '\'' +
                ", shiftDate=" + shiftDate +
                '}';
    }
}


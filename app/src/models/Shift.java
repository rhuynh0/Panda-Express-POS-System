package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Shift {
    private int shiftID;
    private Timestamp shiftStartTime;
    private Timestamp shiftEndTime;
    private Date shiftDate;

    public Shift(int shiftID, Timestamp startTime, Timestamp endTime, Date shiftDate) {
        this.shiftID = shiftID;
        this.shiftStartTime = startTime;
        this.shiftEndTime = endTime;
        this.shiftDate = shiftDate;
    }

    public int getShiftID() {
        return shiftID;
    }

    public Timestamp getShiftStartTime() {
        return shiftStartTime;
    }

    public Timestamp getShiftEndTime() {
        return shiftEndTime;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

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


package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a single employee payroll record as it moves through the ETL
 * pipeline. Holds the normalized input fields (id, name, department, hours,
 * rate) plus the fields computed later by {@link PayrollCalculator}
 * (gross pay, pay level, employment status), and knows how to render
 * itself as an output CSV row.
 *
 * @author Avion Hicks
 */
public class Employee {

    private final int employeeId;
    private final String name;
    private final String department;
    private final BigDecimal hoursWorked;
    private final BigDecimal hourlyRate;

    private BigDecimal grossPay;
    private String payLevel;
    private String employmentStatus;

    public Employee(int employeeId, String name, String department,
                     BigDecimal hoursWorked, BigDecimal hourlyRate) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public BigDecimal getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(BigDecimal grossPay) {
        this.grossPay = grossPay;
    }

    public String getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(String payLevel) {
        this.payLevel = payLevel;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    /**
     * Renders this employee as one output CSV row, in the required column
     * order, with HoursWorked/HourlyRate/GrossPay formatted to exactly two
     * decimal places. Only valid to call after {@link PayrollCalculator}
     * has populated grossPay, payLevel, and employmentStatus.
     */
    public String toCsvRow() {
        return employeeId + ","
                + name + ","
                + department + ","
                + formatTwoDecimals(hoursWorked) + ","
                + formatTwoDecimals(hourlyRate) + ","
                + formatTwoDecimals(grossPay) + ","
                + payLevel + ","
                + employmentStatus;
    }

    private static String formatTwoDecimals(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}

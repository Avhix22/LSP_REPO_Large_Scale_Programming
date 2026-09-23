package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Applies the payroll business rules to an {@link Employee}: regular/overtime
 * pay, the IT department bonus, gross pay rounding, pay level, and
 * employment status. Kept separate from parsing/validation so the payroll
 * formula can change without touching how rows are read or validated.
 *
 * @author Avion Hicks
 */
public class PayrollCalculator {

    private static final BigDecimal FORTY = new BigDecimal("40.00");
    private static final BigDecimal THIRTY = new BigDecimal("30.00");
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal IT_BONUS_MULTIPLIER = new BigDecimal("1.05");
    private static final String IT_DEPARTMENT = "IT";

    private static final BigDecimal LOW_MAX = new BigDecimal("500.00");
    private static final BigDecimal STANDARD_MAX = new BigDecimal("1000.00");
    private static final BigDecimal HIGH_MAX = new BigDecimal("2000.00");

    /**
     * Computes and sets grossPay, payLevel, and employmentStatus on the
     * given employee, based on its hoursWorked, hourlyRate, and department.
     */
    public void calculate(Employee employee) {
        BigDecimal grossPay = calculateGrossPay(
                employee.getHoursWorked(), employee.getHourlyRate(), employee.getDepartment());

        employee.setGrossPay(grossPay);
        employee.setPayLevel(determinePayLevel(grossPay));
        employee.setEmploymentStatus(determineEmploymentStatus(employee.getHoursWorked()));
    }

    /**
     * Regular pay up to 40 hours, 1.5x overtime pay beyond 40 hours, and a
     * 5% bonus for the IT department applied after overtime. Uses the
     * unrounded hoursWorked/hourlyRate values throughout; the result is
     * rounded to two decimal places, round-half-up, only at the end.
     */
    private BigDecimal calculateGrossPay(BigDecimal hoursWorked, BigDecimal hourlyRate, String department) {
        BigDecimal regularHours = hoursWorked.compareTo(FORTY) > 0 ? FORTY : hoursWorked;
        BigDecimal overtimeHours = hoursWorked.compareTo(FORTY) > 0
                ? hoursWorked.subtract(FORTY) : BigDecimal.ZERO;

        BigDecimal regularPay = regularHours.multiply(hourlyRate);
        BigDecimal overtimePay = overtimeHours.multiply(hourlyRate).multiply(OVERTIME_MULTIPLIER);

        BigDecimal grossPay = regularPay.add(overtimePay);

        if (department.equals(IT_DEPARTMENT)) {
            grossPay = grossPay.multiply(IT_BONUS_MULTIPLIER);
        }

        return grossPay.setScale(2, RoundingMode.HALF_UP);
    }

    /** Determines pay level from the final, rounded gross pay. */
    private String determinePayLevel(BigDecimal grossPay) {
        if (grossPay.compareTo(LOW_MAX) < 0) {
            return "Low";
        } else if (grossPay.compareTo(STANDARD_MAX) < 0) {
            return "Standard";
        } else if (grossPay.compareTo(HIGH_MAX) < 0) {
            return "High";
        } else {
            return "Executive";
        }
    }

    /** Determines employment status from hours worked. */
    private String determineEmploymentStatus(BigDecimal hoursWorked) {
        return hoursWorked.compareTo(THIRTY) < 0 ? "Part-Time" : "Full-Time";
    }
}

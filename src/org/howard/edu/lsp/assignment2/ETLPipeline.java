package org.howard.edu.lsp.assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple ETL (Extract-Transform-Load) pipeline for employee payroll data.
 *
 * Reads employee records from data/employees.csv, applies a fixed set of
 * transformations (normalization, validation, pay calculation, pay level,
 * and employment status), and writes the results to
 * data/transformed_employees.csv. Rows that fail validation are skipped
 * and counted, but do not stop the program.
 *
 * @author Avion Hicks
 */
public class ETLPipeline {

    private static final String INPUT_PATH = "data/employees.csv";
    private static final String OUTPUT_PATH = "data/transformed_employees.csv";

    private static final BigDecimal FORTY = new BigDecimal("40.00");
    private static final BigDecimal THIRTY = new BigDecimal("30.00");
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal IT_BONUS_MULTIPLIER = new BigDecimal("1.05");

    private static final BigDecimal LOW_MAX = new BigDecimal("500.00");
    private static final BigDecimal STANDARD_MAX = new BigDecimal("1000.00");
    private static final BigDecimal HIGH_MAX = new BigDecimal("2000.00");

    public static void main(String[] args) {
        int rowsRead = 0;
        int rowsTransformed = 0;
        int rowsSkipped = 0;

        List<String> outputRows = new ArrayList<>();
        outputRows.add("EmployeeID,Name,Department,HoursWorked,HourlyRate,GrossPay,PayLevel,EmploymentStatus");

        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_PATH))) {
            String line = reader.readLine(); // header row; not transformed, not counted as read

            while ((line = reader.readLine()) != null) {
                rowsRead++;

                String transformedRow = transformRow(line);
                if (transformedRow != null) {
                    outputRows.add(transformedRow);
                    rowsTransformed++;
                } else {
                    rowsSkipped++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading input file: " + INPUT_PATH);
            System.out.println(e.getMessage());
            return;
        }

        try (FileWriter writer = new FileWriter(OUTPUT_PATH)) {
            for (String row : outputRows) {
                writer.write(row);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error writing output file: " + OUTPUT_PATH);
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Rows read: " + rowsRead);
        System.out.println("Rows transformed: " + rowsTransformed);
        System.out.println("Rows skipped: " + rowsSkipped);
        System.out.println("Output file: " + OUTPUT_PATH);
    }

    /**
     * Validates and transforms a single non-header CSV line.
     *
     * @param rawLine the raw line read from the input file
     * @return the fully transformed CSV row, or null if the row must be skipped
     */
    private static String transformRow(String rawLine) {
        if (rawLine.trim().isEmpty()) {
            return null;
        }

        // -1 limit preserves trailing empty fields so short/long rows are detected correctly.
        String[] fields = rawLine.split(",", -1);
        if (fields.length != 5) {
            return null;
        }

        String rawId = fields[0].trim();
        String rawName = fields[1].trim();
        String rawDept = fields[2].trim();
        String rawHours = fields[3].trim();
        String rawRate = fields[4].trim();

        int employeeId;
        try {
            employeeId = Integer.parseInt(rawId);
        } catch (NumberFormatException e) {
            return null;
        }

        BigDecimal hoursWorked;
        BigDecimal hourlyRate;
        try {
            hoursWorked = new BigDecimal(rawHours);
            hourlyRate = new BigDecimal(rawRate);
        } catch (NumberFormatException e) {
            return null;
        }

        if (hoursWorked.compareTo(ZERO) < 0 || hourlyRate.compareTo(ZERO) < 0) {
            return null;
        }

        String name = rawName.toUpperCase();
        String department = rawDept;

        BigDecimal grossPay = calculateGrossPay(hoursWorked, hourlyRate, department);
        String payLevel = determinePayLevel(grossPay);
        String employmentStatus = determineEmploymentStatus(hoursWorked);

        StringBuilder row = new StringBuilder();
        row.append(employeeId).append(',');
        row.append(name).append(',');
        row.append(department).append(',');
        row.append(formatTwoDecimals(hoursWorked)).append(',');
        row.append(formatTwoDecimals(hourlyRate)).append(',');
        row.append(formatTwoDecimals(grossPay)).append(',');
        row.append(payLevel).append(',');
        row.append(employmentStatus);

        return row.toString();
    }

    /**
     * Calculates gross pay: regular pay up to 40 hours, 1.5x overtime pay
     * beyond 40 hours, and a 5% bonus for the IT department applied after
     * overtime. The result is rounded to two decimal places, round-half-up.
     */
    private static BigDecimal calculateGrossPay(BigDecimal hoursWorked, BigDecimal hourlyRate, String department) {
        BigDecimal regularHours = hoursWorked.compareTo(FORTY) > 0 ? FORTY : hoursWorked;
        BigDecimal overtimeHours = hoursWorked.compareTo(FORTY) > 0 ? hoursWorked.subtract(FORTY) : ZERO;

        BigDecimal regularPay = regularHours.multiply(hourlyRate);
        BigDecimal overtimePay = overtimeHours.multiply(hourlyRate).multiply(OVERTIME_MULTIPLIER);

        BigDecimal grossPay = regularPay.add(overtimePay);

        if (department.equals("IT")) {
            grossPay = grossPay.multiply(IT_BONUS_MULTIPLIER);
        }

        return grossPay.setScale(2, RoundingMode.HALF_UP);
    }

    /** Determines pay level from the final, rounded gross pay. */
    private static String determinePayLevel(BigDecimal grossPay) {
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
    private static String determineEmploymentStatus(BigDecimal hoursWorked) {
        return hoursWorked.compareTo(THIRTY) < 0 ? "Part-Time" : "Full-Time";
    }

    /** Formats a numeric value with exactly two decimal places for output. */
    private static String formatTwoDecimals(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}

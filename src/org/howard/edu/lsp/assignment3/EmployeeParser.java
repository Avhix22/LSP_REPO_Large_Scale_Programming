package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;

/**
 * Parses and validates a single raw CSV line into an {@link Employee}.
 * Responsible only for normalization (trimming, uppercasing the name) and
 * validation (field count, numeric parsing, non-negative checks). Pay
 * calculation is intentionally not this class's job; see
 * {@link PayrollCalculator}.
 *
 * @author Avion Hicks
 */
public class EmployeeParser {

    private static final int EXPECTED_FIELD_COUNT = 5;

    /**
     * Parses one non-header CSV line into an Employee.
     *
     * @param rawLine the raw line read from the input file
     * @return a validated, normalized Employee
     * @throws InvalidEmployeeRecordException if the row must be skipped
     */
    public Employee parse(String rawLine) throws InvalidEmployeeRecordException {
        if (rawLine.trim().isEmpty()) {
            throw new InvalidEmployeeRecordException("Blank row");
        }

        // -1 limit preserves trailing empty fields so short/long rows are detected correctly.
        String[] fields = rawLine.split(",", -1);
        if (fields.length != EXPECTED_FIELD_COUNT) {
            throw new InvalidEmployeeRecordException("Expected " + EXPECTED_FIELD_COUNT
                    + " fields, found " + fields.length);
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
            throw new InvalidEmployeeRecordException("Invalid EmployeeID: " + rawId);
        }

        BigDecimal hoursWorked;
        BigDecimal hourlyRate;
        try {
            hoursWorked = new BigDecimal(rawHours);
            hourlyRate = new BigDecimal(rawRate);
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeRecordException("Invalid HoursWorked/HourlyRate: "
                    + rawHours + ", " + rawRate);
        }

        if (hoursWorked.compareTo(BigDecimal.ZERO) < 0 || hourlyRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidEmployeeRecordException("HoursWorked and HourlyRate must not be negative");
        }

        String name = rawName.toUpperCase();
        String department = rawDept;

        return new Employee(employeeId, name, department, hoursWorked, hourlyRate);
    }
}

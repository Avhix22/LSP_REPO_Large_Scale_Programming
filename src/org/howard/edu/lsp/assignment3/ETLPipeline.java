package org.howard.edu.lsp.assignment3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the Assignment #3 object-oriented redesign of the
 * employee payroll ETL pipeline. Orchestrates extraction ({@link
 * EmployeeCsvReader}), per-row transformation ({@link EmployeeParser} +
 * {@link PayrollCalculator}), and load ({@link EmployeeCsvWriter}), and
 * prints the run summary. Contains no parsing, calculation, or file-format
 * logic of its own.
 *
 * @author Avion Hicks
 */
public class ETLPipeline {

    private static final String INPUT_PATH = "data/employees.csv";
    private static final String OUTPUT_PATH = "data/transformed_employees.csv";

    public static void main(String[] args) {
        EmployeeCsvReader reader = new EmployeeCsvReader(INPUT_PATH);
        EmployeeParser parser = new EmployeeParser();
        PayrollCalculator calculator = new PayrollCalculator();
        EmployeeCsvWriter writer = new EmployeeCsvWriter(OUTPUT_PATH);

        List<String> rawLines;
        try {
            rawLines = reader.readDataLines();
        } catch (IOException e) {
            System.out.println("Error reading input file: " + INPUT_PATH);
            System.out.println(e.getMessage());
            return;
        }

        int rowsSkipped = 0;
        List<Employee> transformedEmployees = new ArrayList<>();

        for (String rawLine : rawLines) {
            try {
                Employee employee = parser.parse(rawLine);
                calculator.calculate(employee);
                transformedEmployees.add(employee);
            } catch (InvalidEmployeeRecordException e) {
                rowsSkipped++;
            }
        }

        try {
            writer.write(transformedEmployees);
        } catch (IOException e) {
            System.out.println("Error writing output file: " + OUTPUT_PATH);
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Rows read: " + rawLines.size());
        System.out.println("Rows transformed: " + transformedEmployees.size());
        System.out.println("Rows skipped: " + rowsSkipped);
        System.out.println("Output file: " + OUTPUT_PATH);
    }
}

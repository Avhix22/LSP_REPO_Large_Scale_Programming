package org.howard.edu.lsp.assignment3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes a list of fully-transformed {@link Employee} records to the output
 * CSV file, including the header row. Responsible only for the "load" step;
 * it does not validate or calculate anything.
 *
 * @author Avion Hicks
 */
public class EmployeeCsvWriter {

    private static final String HEADER =
            "EmployeeID,Name,Department,HoursWorked,HourlyRate,GrossPay,PayLevel,EmploymentStatus";

    private final String outputPath;

    public EmployeeCsvWriter(String outputPath) {
        this.outputPath = outputPath;
    }

    public void write(List<Employee> employees) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(HEADER);
            writer.write(System.lineSeparator());

            for (Employee employee : employees) {
                writer.write(employee.toCsvRow());
                writer.write(System.lineSeparator());
            }
        }
    }
}

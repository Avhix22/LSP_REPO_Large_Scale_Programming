package org.howard.edu.lsp.assignment3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the raw data lines (everything after the header) from an employee
 * payroll CSV file. Responsible only for extraction; parsing/validation of
 * each line belongs to {@link EmployeeParser}.
 *
 * @author Avion Hicks
 */
public class EmployeeCsvReader {

    private final String inputPath;

    public EmployeeCsvReader(String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * Reads every non-header line from the input file, in order, including
     * blank lines. The header row is consumed but not included.
     */
    public List<String> readDataLines() throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line = reader.readLine(); // header row; not returned

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }
}

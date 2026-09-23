package org.howard.edu.lsp.assignment3;

/**
 * Thrown by {@link EmployeeParser} when a raw CSV line fails validation
 * (wrong field count, unparsable numeric field, negative hours/rate, etc.)
 * and must be skipped rather than transformed.
 *
 * @author Avion Hicks
 */
public class InvalidEmployeeRecordException extends Exception {

    public InvalidEmployeeRecordException(String message) {
        super(message);
    }
}

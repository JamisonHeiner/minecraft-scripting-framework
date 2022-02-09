package exception;

import lombok.ToString;

@ToString
public class IncompleteStepException extends RuntimeException {
    public IncompleteStepException(String message) {
        super(message);
    }
}

package minecraft.scripting.framework.exception;

import lombok.ToString;

@ToString
public class IncompleteTickableException extends RuntimeException {
    public IncompleteTickableException(String message) {
        super(message);
    }
}

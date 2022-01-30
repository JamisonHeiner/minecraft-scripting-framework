package minecraft.scripting.framework.exception;

import minecraft.scripting.framework.Script;

public class EmptyScriptException extends RuntimeException {
    public EmptyScriptException(Script script) {
        super(String.format("The following Script was created with no steps: %s", script));
    }
}

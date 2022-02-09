package minecraft.scripting.framework.builder;

import lombok.ToString;
import minecraft.scripting.framework.Script;
import minecraft.scripting.framework.step.Step;

import java.util.LinkedList;
import java.util.Queue;

@ToString
public class ScriptBuilder {
    private final Queue<Step> steps;

    public ScriptBuilder() {
        steps = new LinkedList<>();
    }

    public ScriptBuilder withStep(Step step) {
        steps.add(step);
        return this;
    }

    public Script build() {
        return new Script(steps);
    }
}

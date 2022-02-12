package minecraft.scripting.framework.builder;

import lombok.ToString;
import minecraft.scripting.framework.LoopingScript;
import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.step.ResettableStep;
import minecraft.scripting.framework.step.Step;

import java.util.LinkedList;
import java.util.Queue;

import static minecraft.scripting.framework.concept.Condition.not;

@ToString
public class LoopingScriptBuilder {
    private final Queue<ResettableStep> steps;
    private Condition shouldLoopCondition;

    public LoopingScriptBuilder() {
        steps = new LinkedList<>();
    }

    public LoopingScriptBuilder withStep(ResettableStep resettableStep) {
        steps.add(resettableStep);
        return this;
    }

    public LoopingScriptBuilder loopUntil(Condition stopLoopingCondition) {
        this.shouldLoopCondition = not(stopLoopingCondition);
        return this;
    }

    public LoopingScriptBuilder loopWhile(Condition shouldLoopCondition) {
        this.shouldLoopCondition = shouldLoopCondition;
        return this;
    }

    public Step build() {
        return new LoopingScript(shouldLoopCondition, steps);
    }
}

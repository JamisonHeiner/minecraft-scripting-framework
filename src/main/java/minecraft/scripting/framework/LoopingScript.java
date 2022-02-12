package minecraft.scripting.framework;

import lombok.ToString;
import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.concept.Resettable;
import minecraft.scripting.framework.step.ResettableStep;
import minecraft.scripting.framework.step.Step;

import java.util.LinkedList;
import java.util.Queue;

@ToString
public class LoopingScript implements Step {
    private final Condition shouldLoopCondition;
    private final Queue<ResettableStep> resettableSteps;
    private Script wrappedScript;

    public LoopingScript(Condition shouldLoopCondition, Queue<ResettableStep> resettableSteps) {
        this.shouldLoopCondition = shouldLoopCondition;
        this.resettableSteps = resettableSteps;
        this.wrappedScript = new Script(copyOf(resettableSteps));
    }

    private Queue<ResettableStep> copyOf(Queue<ResettableStep> resettableSteps) {
        return new LinkedList<>(resettableSteps);
    }

    @Override
    public boolean isCompleted() {
        return wrappedScript.isCompleted() && !shouldLoopCondition.isSatisfied();
    }

    @Override
    public void onTick() {
        if (isCompleted()) return;

        resetWrappedScriptIfNecessary();

        wrappedScript.onTick();
    }

    private void resetWrappedScriptIfNecessary() {
        if (wrappedScript.isCompleted() && shouldLoopCondition.isSatisfied()) {
            resettableSteps.forEach(Resettable::reset);
            wrappedScript = new Script(copyOf(resettableSteps));
        }
    }
}

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package minecraft.scripting.framework;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import minecraft.scripting.framework.exception.EmptyScriptException;
import minecraft.scripting.framework.step.Step;

import java.util.Objects;
import java.util.Queue;

/**
 * A class that represents a series of Steps to be performed one at a time. <br>
 * <br>
 * On each Minecraft tick, a Script will perform the first Step that meets the following criteria: <br>
 * <ul>
 *     <li>It has not been "completed" on any previous Minecraft tick</li>
 *     <li>It is not "completed" on <em>this</em> Minecraft tick</li>
 * </ul>
 * A Script is "completed" when there are no uncompleted steps remaining.
 */
@Log4j2
@ToString
public class Script implements Step {
    private final Queue<Step> remainingSteps;
    private Step currentStep;

    public Script(Queue<Step> steps) {
        ensureThereAreSteps(steps);
        ensureNoStepsAreNull(steps);

        remainingSteps = steps;
        currentStep = steps.poll();
    }

    private void ensureThereAreSteps(Queue<Step> steps) {
        if (Objects.isNull(steps) || steps.isEmpty()) {
            throw new EmptyScriptException(this);
        }
    }

    private void ensureNoStepsAreNull(Queue<Step> steps) {
        if (steps.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(String.format("Found a null Step while creating a Script. None of the Steps can be null! Steps: %s", steps));
        }
    }

    @Override
    public void onTick() {
        if (isCompleted()) return;

        while (theCurrentStepWasCompletedLastTick()) {
            log.info("The current step ({}) was completed last tick. Moving to the next step.", currentStep);
            moveToTheNextStep();
        }

        log.info("Ticking the current step ({}).", currentStep);
        tickTheCurrentStep();
    }

    @Override
    public boolean isCompleted() {
        return theCurrentStepIsCompleted() && thereAreNoRemainingSteps();
    }

    private boolean theCurrentStepIsCompleted() {
        return currentStep.isCompleted();
    }

    private boolean thereAreNoRemainingSteps() {
        return remainingSteps.isEmpty();
    }

    private boolean theCurrentStepWasCompletedLastTick() {
        return theCurrentStepIsCompleted();
    }

    private void moveToTheNextStep() {
        currentStep = remainingSteps.poll();
    }

    private void tickTheCurrentStep() {
        currentStep.onTick();
    }
}

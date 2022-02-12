package minecraft.scripting.framework;

import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.step.ResettableStep;
import minecraft.scripting.framework.step.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static minecraft.scripting.framework.TestUtils.queueOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoopingScriptTest {
    LoopingScript loopingScript;

    @Mock Condition shouldLoopCondition;
    @Mock ResettableStep firstStep, secondStep;

    @BeforeEach
    void setUpLoopingScript() {
        loopingScript = new LoopingScript(shouldLoopCondition, queueOf(firstStep, secondStep));
    }

    @Test
    void shouldResetStepsWhenTickedWhileAllStepsAreCompletedAndShouldLoop() {
        // Given
        completeAllSteps();
        when(shouldLoopCondition.isSatisfied()).thenReturn(true);

        // When
        loopingScript.onTick();

        // Then
        verify(firstStep).reset();
        verify(secondStep).reset();
    }

    @Test
    void shouldNotResetStepsWhenTickedWhileAllStepsAreCompletedButShouldNotLoop() {
        // Given
        completeAllSteps();
        when(shouldLoopCondition.isSatisfied()).thenReturn(false);

        // When
        loopingScript.onTick();

        // Then
        verify(firstStep, never()).reset();
        verify(secondStep, never()).reset();
    }

    private void completeAllSteps() {
        loopingScript.onTick();

        completeStep(firstStep);
        completeStep(secondStep);
    }

    private void completeStep(Step step) {
        when(step.isCompleted()).thenReturn(true);
        loopingScript.onTick();
    }
}

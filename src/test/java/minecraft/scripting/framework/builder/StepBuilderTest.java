package minecraft.scripting.framework.builder;

import exception.IncompleteStepException;
import exception.IncompleteTickableException;
import minecraft.scripting.framework.concept.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class StepBuilderTest {
    StepBuilder stepBuilder;

    @Test
    void shouldThrowExceptionWhenCreatingStepWithNoTickables() {
        // Given
        stepBuilder = new StepBuilder();

        // When
        Executable createStep = () -> stepBuilder.build();

        // Then
        assertThrows(IncompleteStepException.class, createStep);
    }

    @Test
    void shouldThrowExceptionWhenAddingTickableToStepWithoutProvidingATickable() {
        // Given
        stepBuilder = new StepBuilder();

        // When
        Executable createStep = () -> stepBuilder
            .when(mock(Condition.class))
            .onTick();

        // Then
        assertThrows(IncompleteTickableException.class, createStep);
    }
}

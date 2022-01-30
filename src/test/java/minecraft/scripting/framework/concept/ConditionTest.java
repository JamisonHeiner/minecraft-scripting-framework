package minecraft.scripting.framework.concept;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static minecraft.scripting.framework.concept.Condition.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionTest {
    Condition condition;

    @Test
    void shouldBeSatisfiedWhenExpressionIsTrue() {
        // Given
        condition = () -> true;

        // When + Then
        assertTrue(condition.isSatisfied());
    }

    @Test
    void shouldNotBeSatisfiedWhenExpressionIsFalse() {
        // Given
        condition = () -> false;

        // When + Then
        assertFalse(condition.isSatisfied());
    }

    @Nested
    class And {
        Condition firstCondition;
        Condition secondCondition;

        @Test
        void shouldBeSatisfiedWhenBothExpressionsAreTrue() {
            // Given
            firstCondition = () -> true;
            secondCondition = () -> true;
            condition = firstCondition.and(secondCondition);

            // When + Then
            assertTrue(condition.isSatisfied());
        }

        @Test
        void shouldNotBeSatisfiedWhenFirstExpressionIsFalse() {
            // Given
            firstCondition = () -> false;
            secondCondition = () -> true;
            condition = firstCondition.and(secondCondition);

            // When + Then
            assertFalse(condition.isSatisfied());
        }

        @Test
        void shouldNotBeSatisfiedWhenSecondExpressionIsFalse() {
            // Given
            firstCondition = () -> true;
            secondCondition = () -> false;
            condition = firstCondition.and(secondCondition);

            // When + Then
            assertFalse(condition.isSatisfied());
        }
    }

    @Nested
    class Or {
        Condition firstCondition;
        Condition secondCondition;

        @Test
        void shouldBeSatisfiedWhenFirstExpressionIsTrue() {
            // Given
            firstCondition = () -> true;
            secondCondition = () -> false;
            condition = firstCondition.or(secondCondition);

            // When + Then
            assertTrue(condition.isSatisfied());
        }

        @Test
        void shouldBeSatisfiedWhenSecondExpressionIsTrue() {
            // Given
            firstCondition = () -> false;
            secondCondition = () -> true;
            condition = firstCondition.or(secondCondition);

            // When + Then
            assertTrue(condition.isSatisfied());
        }

        @Test
        void shouldNotBeSatisfiedWhenNeitherExpressionIsTrue() {
            // Given
            firstCondition = () -> false;
            secondCondition = () -> false;
            condition = firstCondition.or(secondCondition);

            // When + Then
            assertFalse(condition.isSatisfied());
        }
    }

    @Nested
    class Not {
        Condition conditionToInvert;

        @Test
        void shouldBeSatisfiedWhenExpressionIsFalse() {
            // Given
            conditionToInvert = () -> false;
            condition = not(conditionToInvert);

            // When + Then
            assertTrue(condition.isSatisfied());
        }

        @Test
        void shouldNotBeSatisfiedWhenExpressionIsTrue() {
            // Given
            conditionToInvert = () -> true;
            condition = not(conditionToInvert);

            // When + Then
            assertFalse(condition.isSatisfied());
        }
    }
}

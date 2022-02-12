package minecraft.scripting.framework;

import minecraft.scripting.framework.builder.LoopingScriptBuilder;
import minecraft.scripting.framework.builder.StepBuilder;
import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.concept.Resettable;
import minecraft.scripting.framework.concept.tickable.Tickable;
import minecraft.scripting.framework.step.ResettableStep;
import minecraft.scripting.framework.step.Step;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoopingScriptAcceptanceTest {
    Step loopingScript;

    @Nested
    class ShouldLoopUntil {
        int totalNumberOfJumps = 0;
        int numberOfJumpsThisLoop = 0;

        @Test
        void shouldLoopUntilJumpedSixTimes() {
            // Given
            Tickable jump = () -> {
                ++totalNumberOfJumps;
                ++numberOfJumpsThisLoop;
            };
            Condition jumpedTwiceThisLoop = () -> numberOfJumpsThisLoop >= 2;
            Resettable resetJumpCounter = () -> numberOfJumpsThisLoop = 0;
            ResettableStep jumpTwice = new StepBuilder()
                .should(jump).onTick()
                .until(jumpedTwiceThisLoop)
                .should(resetJumpCounter).whenReset()
                .buildResettableStep();

            Condition jumpedSixTimesTotal = () -> totalNumberOfJumps >= 6;

            loopingScript = new LoopingScriptBuilder()
                .withStep(jumpTwice)
                .loopUntil(jumpedSixTimesTotal)
                .build();

            // Should jump
            loopingScript.onTick();
            assertEquals(1, totalNumberOfJumps);
            assertEquals(1, numberOfJumpsThisLoop);

            // Should jump again
            loopingScript.onTick();
            assertEquals(2, totalNumberOfJumps);
            assertEquals(2, numberOfJumpsThisLoop);

            // Script should not be completed yet
            assertFalse(loopingScript.isCompleted());

            // Should reset and jump again
            loopingScript.onTick();
            assertEquals(3, totalNumberOfJumps);
            assertEquals(1, numberOfJumpsThisLoop);

            // Should jump again
            loopingScript.onTick();
            assertEquals(4, totalNumberOfJumps);
            assertEquals(2, numberOfJumpsThisLoop);

            // Script still should not be completed yet
            assertFalse(loopingScript.isCompleted());

            // Should reset and jump again
            loopingScript.onTick();
            assertEquals(5, totalNumberOfJumps);
            assertEquals(1, numberOfJumpsThisLoop);

            // Should jump again
            loopingScript.onTick();
            assertEquals(6, totalNumberOfJumps);
            assertEquals(2, numberOfJumpsThisLoop);

            // Script should be completed (jumped six times)
            assertTrue(loopingScript.isCompleted());

            // Should not jump again
            loopingScript.onTick();
            assertEquals(6, totalNumberOfJumps);
        }
    }

    @Nested
    class ShouldLoopWhile {
        int totalNumberOfJumps = 0;
        int numberOfJumpsThisLoop = 0;

        @Test
        void shouldLoopWhileJumpedFewerThanFiveTimes() {
            // Given
            Tickable jump = () -> {
                ++totalNumberOfJumps;
                ++numberOfJumpsThisLoop;
            };
            Condition jumpedTwiceThisLoop = () -> numberOfJumpsThisLoop >= 2;
            Resettable resetJumpCounter = () -> numberOfJumpsThisLoop = 0;
            ResettableStep jumpTwice = new StepBuilder()
                .should(jump).onTick()
                .until(jumpedTwiceThisLoop)
                .should(resetJumpCounter).whenReset()
                .buildResettableStep();

            Condition jumpedFewerThanFiveTimesTotal = () -> totalNumberOfJumps < 5;

            loopingScript = new LoopingScriptBuilder()
                .withStep(jumpTwice)
                .loopWhile(jumpedFewerThanFiveTimesTotal)
                .build();

            // Should jump
            loopingScript.onTick();
            assertEquals(1, totalNumberOfJumps);
            assertEquals(1, numberOfJumpsThisLoop);

            // Should jump again
            loopingScript.onTick();
            assertEquals(2, totalNumberOfJumps);
            assertEquals(2, numberOfJumpsThisLoop);

            // Script should not be completed yet
            assertFalse(loopingScript.isCompleted());

            // Should reset and jump again
            loopingScript.onTick();
            assertEquals(3, totalNumberOfJumps);
            assertEquals(1, numberOfJumpsThisLoop);

            // Should jump again
            loopingScript.onTick();
            assertEquals(4, totalNumberOfJumps);
            assertEquals(2, numberOfJumpsThisLoop);

            // Script still should not be completed yet
            assertFalse(loopingScript.isCompleted());

            // Should reset and jump again
            loopingScript.onTick();
            assertEquals(5, totalNumberOfJumps);
            assertEquals(1, numberOfJumpsThisLoop);

            // Should jump again
            loopingScript.onTick();
            assertEquals(6, totalNumberOfJumps);
            assertEquals(2, numberOfJumpsThisLoop);

            // Script should be completed (jumped six times)
            assertTrue(loopingScript.isCompleted());

            // Should not jump again
            loopingScript.onTick();
            assertEquals(6, totalNumberOfJumps);
        }
    }
}

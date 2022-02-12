package minecraft.scripting.framework;

import minecraft.scripting.framework.builder.ScriptBuilder;
import minecraft.scripting.framework.builder.StepBuilder;
import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.concept.tickable.Tickable;
import minecraft.scripting.framework.step.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScriptAcceptanceTest {
    Script script;

    @Nested
    class ScriptWithOneUnconditionalStep {
        // State
        int numberOfJumps;

        // Script components
        Tickable jump;
        Condition jumpedThreeTimes;
        Step jumpThreeTimes;

        @BeforeEach
        void setUpScript() {
            jump = () -> ++numberOfJumps;
            jumpedThreeTimes = () -> numberOfJumps >= 3;
            jumpThreeTimes = new StepBuilder()
                .should(jump).onTick()
                .until(jumpedThreeTimes)
                .buildStep();

            script = new ScriptBuilder()
                .withStep(jumpThreeTimes)
                .build();
        }

        @Test
        void shouldPerformActionOnTickUntilStepCompletes() {
            // Script should jump
            script.onTick();
            assertEquals(1, numberOfJumps);

            // Script should jump again
            script.onTick();
            assertEquals(2, numberOfJumps);

            // Script should jump again
            script.onTick();
            assertEquals(3, numberOfJumps);

            // Script should be completed (it has jumped three times)
            assertTrue(script.isCompleted());

            // Should not jump again (script is completed)
            script.onTick();
            assertEquals(3, numberOfJumps);
        }
    }

    @Nested
    class ScriptWithTwoUnconditionalSteps {
        // State
        int numberOfJumps;
        int numberOfAttacks;

        // Script components
        Tickable jump;
        Condition jumpedThreeTimes;
        Step jumpThreeTimes;

        Tickable attack;
        Condition attackedThreeTimes;
        Step attackThreeTimes;

        @BeforeEach
        void setUpScript() {
            jump = () -> ++numberOfJumps;
            jumpedThreeTimes = () -> numberOfJumps >= 3;
            jumpThreeTimes = new StepBuilder()
                .should(jump).onTick()
                .until(jumpedThreeTimes)
                .buildStep();

            attack = () -> ++numberOfAttacks;
            attackedThreeTimes = () -> numberOfAttacks >= 3;
            attackThreeTimes = new StepBuilder()
                .should(attack).onTick()
                .until(attackedThreeTimes)
                .buildStep();

            script = new ScriptBuilder()
                .withStep(jumpThreeTimes)
                .withStep(attackThreeTimes)
                .build();
        }

        @Test
        void shouldPerformOnlyTheFirstActionOnTickUntilFirstStepCompletes() {
            // Script should jump
            script.onTick();
            assertEquals(1, numberOfJumps);

            // Script should jump again
            script.onTick();
            assertEquals(2, numberOfJumps);

            // Script should jump again
            script.onTick();
            assertEquals(3, numberOfJumps);

            // Step should be completed (it has jumped three times)
            assertTrue(jumpThreeTimes.isCompleted());

            // Should not jump again (that step is completed)
            script.onTick();
            assertEquals(3, numberOfJumps);
        }

        @Test
        void shouldPerformOnlyTheSecondActionOnTickAfterFirstStepCompletes() {
            // Given
            completeTheFirstStep();

            // Script should attack
            script.onTick();
            assertEquals(1, numberOfAttacks);

            // Script should attack again
            script.onTick();
            assertEquals(2, numberOfAttacks);

            // Script should attack again
            script.onTick();
            assertEquals(3, numberOfAttacks);

            // Step should be completed (it has attacked three times)
            assertTrue(attackThreeTimes.isCompleted());

            // Should not attack again (that step is completed)
            script.onTick();
            assertEquals(3, numberOfAttacks);
        }

        private void completeTheFirstStep() {
            script.onTick();
            script.onTick();
            script.onTick();
        }
    }

    @Nested
    class ScriptWithOneUnconditionalStepWithTwoActions {
        // State
        int numberOfJumps;
        int numberOfAttacks;

        // Script components
        Tickable jump;
        Condition jumpedThreeTimes;
        Tickable attack;
        Condition attackedThreeTimes;
        Step jumpAndAttackThreeTimes;

        @BeforeEach
        void setUpScript() {
            jump = () -> ++numberOfJumps;
            jumpedThreeTimes = () -> numberOfJumps >= 3;
            attack = () -> ++numberOfAttacks;
            attackedThreeTimes = () -> numberOfAttacks >= 3;

            jumpAndAttackThreeTimes = new StepBuilder()
                .should(jump).and(attack).onTick()
                .until(jumpedThreeTimes.and(attackedThreeTimes))
                .buildStep();

            script = new ScriptBuilder()
                .withStep(jumpAndAttackThreeTimes)
                .build();
        }

        @Test
        void shouldPerformBothActions() {
            // Script should jump and attack
            script.onTick();
            assertEquals(1, numberOfJumps);
            assertEquals(1, numberOfAttacks);

            // Script should jump and attack again
            script.onTick();
            assertEquals(2, numberOfJumps);
            assertEquals(2, numberOfAttacks);

            // Script should jump and attack again
            script.onTick();
            assertEquals(3, numberOfJumps);
            assertEquals(3, numberOfAttacks);

            // Step should be completed (it has jumped three times and attacked three times)
            assertTrue(jumpAndAttackThreeTimes.isCompleted());

            // Should not jump or attack again (step is completed)
            script.onTick();
            assertEquals(3, numberOfJumps);
            assertEquals(3, numberOfAttacks);
        }
    }

    @Nested
    class ScriptWithOneConditionalStep {
        // State
        boolean jumpedLastTick;
        int numberOfJumps;

        // Script components
        Condition onGround;
        Tickable jump;
        Condition jumpedThreeTimes;
        Step jumpThreeTimes;

        @BeforeEach
        void setUpScript() {
            onGround = () -> !jumpedLastTick;

            jump = () -> {
                ++numberOfJumps;
                jumpedLastTick = true;
            };

            jumpedThreeTimes = () -> numberOfJumps >= 3;

            jumpThreeTimes = new StepBuilder()
                .when(onGround).should(jump).onTick()
                .until(jumpedThreeTimes)
                .buildStep();

            script = new ScriptBuilder()
                .withStep(jumpThreeTimes)
                .build();
        }

        @Test
        void shouldPerformBothActions() {
            // Script should jump (starts on ground)
            tickTheScript();
            assertEquals(1, numberOfJumps);

            // Script not jump yet (just jumped -- in the air now)
            tickTheScript();
            assertEquals(1, numberOfJumps);

            // Script should jump again (landed now)
            tickTheScript();
            assertEquals(2, numberOfJumps);

            // Script not jump yet (just jumped -- in the air now)
            tickTheScript();
            assertEquals(2, numberOfJumps);

            // Script should jump again (landed now)
            tickTheScript();
            assertEquals(3, numberOfJumps);

            // Step should be completed (it has jumped three times and attacked three times)
            assertTrue(jumpThreeTimes.isCompleted());

            // Should not jump again (step is completed)
            tickTheScript();
            assertEquals(3, numberOfJumps);
        }

        private void tickTheScript() {
            if (jumpedLastTick) {
                script.onTick();
                jumpedLastTick = false;
            } else {
                script.onTick();
            }
        }
    }

    @Nested
    class ScriptWithOneStepWithAConditionalActionAndAnUnconditionalAction {
        // State
        boolean jumpedLastTick;
        int numberOfJumps;
        int numberOfAttacks;

        // Script components
        Condition onGround;
        Tickable jump;
        Condition jumpedThreeTimes;
        Tickable attack;
        Condition attackedThreeTimes;
        Step jumpAndAttackThreeTimes;

        @BeforeEach
        void setUpScript() {
            onGround = () -> !jumpedLastTick;

            jump = () -> {
                ++numberOfJumps;
                jumpedLastTick = true;
            };
            jumpedThreeTimes = () -> numberOfJumps >= 3;

            attack = () -> ++numberOfAttacks;
            attackedThreeTimes = () -> numberOfAttacks >= 3;

            jumpAndAttackThreeTimes = new StepBuilder()
                .when(onGround).should(jump).onTick()
                .should(attack).onTick()
                .until(jumpedThreeTimes.and(attackedThreeTimes))
                .buildStep();

            script = new ScriptBuilder()
                .withStep(jumpAndAttackThreeTimes)
                .build();
        }

        @Test
        void shouldConditionallyPerformConditionalActionAndUnconditionallyPerformUnconditionalAction() {
            // Script should jump and attack (on the ground)
            tickTheScript();
            assertEquals(1, numberOfJumps);
            assertEquals(1, numberOfAttacks);

            // Script should not jump but should attack (just jumped -- in the air now)
            tickTheScript();
            assertEquals(1, numberOfJumps);
            assertEquals(2, numberOfAttacks);

            // Script should jump and attack again (landed now)
            tickTheScript();
            assertEquals(2, numberOfJumps);
            assertEquals(3, numberOfAttacks);

            // Script should not jump but should attack (just jumped -- in the air now)
            tickTheScript();
            assertEquals(2, numberOfJumps);
            assertEquals(4, numberOfAttacks);

            // Script should jump and attack again (landed now)
            tickTheScript();
            assertEquals(3, numberOfJumps);
            assertEquals(5, numberOfAttacks);

            // Step should be completed (it has jumped and attacked three times)
            assertTrue(jumpAndAttackThreeTimes.isCompleted());

            // Should not jump or attack again (step is completed)
            tickTheScript();
            assertEquals(3, numberOfJumps);
            assertEquals(5, numberOfAttacks);
        }

        private void tickTheScript() {
            if (jumpedLastTick) {
                script.onTick();
                jumpedLastTick = false;
            } else {
                script.onTick();
            }
        }
    }
}

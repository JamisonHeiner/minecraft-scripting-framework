package minecraft.scripting.framework.builder;

import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.concept.Resettable;
import minecraft.scripting.framework.concept.tickable.CompositeTickable;
import minecraft.scripting.framework.concept.tickable.ConditionalTickable;
import minecraft.scripting.framework.concept.tickable.Tickable;
import minecraft.scripting.framework.exception.IncompleteStepException;
import minecraft.scripting.framework.exception.IncompleteTickableException;
import minecraft.scripting.framework.step.InherentlyResettableStep;
import minecraft.scripting.framework.step.ResettableStep;
import minecraft.scripting.framework.step.Step;
import minecraft.scripting.framework.step.TickDelegatingStep;

import java.util.HashSet;
import java.util.Set;

public class StepBuilder {
    private final Set<Tickable> tickables;
    private Condition completionCondition;
    private Resettable resettable;

    public StepBuilder() {
        tickables = new HashSet<>();
    }

    public TickableBuilder when(Condition performanceCondition) {
        return new TickableBuilder().when(performanceCondition);
    }

    public TickableBuilder should(Tickable tickable) {
        return new TickableBuilder().should(tickable);
    }

    public StepBuilder until(Condition completionCondition) {
        this.completionCondition = completionCondition;
        return this;
    }

    public StepBuilder should(Resettable resettable) {
        this.resettable = resettable;
        return this;
    }

    public StepBuilder whenReset() {
        return this;
    }

    public Step buildStep() {
        ensureThereAreTickables();

        return new TickDelegatingStep(completionCondition, tickables);
    }

    private void ensureThereAreTickables() {
        if (tickables.isEmpty()) {
            throw new IncompleteStepException("Tried to create a Step with no Tickables.");
        }
    }

    public ResettableStep buildResettableStep() {
        ensureThereAreTickables();

        if (resettable != null) {
            return new ResettableStep(new TickDelegatingStep(completionCondition, tickables), resettable);
        } else {
            return new InherentlyResettableStep(new TickDelegatingStep(completionCondition, tickables));
        }
    }

    public class TickableBuilder {
        private final Set<Tickable> tickables;
        private Condition performanceCondition;

        private TickableBuilder() {
            tickables = new HashSet<>();
        }

        public TickableBuilder when(Condition performanceCondition) {
            this.performanceCondition = performanceCondition;
            return this;
        }

        public TickableBuilder should(Tickable tickable) {
            tickables.add(tickable);
            return this;
        }

        public TickableBuilder and(Tickable tickable) {
            return should(tickable);
        }

        public StepBuilder onTick() {
            Tickable tickable;
            if (tickables.size() == 1) {
                tickable = tickables.iterator().next();
            } else if (tickables.size() > 1) {
                tickable = new CompositeTickable(tickables);
            } else {
                throw new IncompleteTickableException("Tried to build a Step with no Tickables.");
            }

            if (performanceCondition != null) {
                tickable = new ConditionalTickable(performanceCondition, tickable);
            }

            StepBuilder.this.tickables.add(tickable);
            return StepBuilder.this;
        }
    }
}

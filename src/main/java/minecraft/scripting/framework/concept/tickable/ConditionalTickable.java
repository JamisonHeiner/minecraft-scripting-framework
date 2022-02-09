package minecraft.scripting.framework.concept.tickable;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import minecraft.scripting.framework.concept.Condition;

@Log4j2
@ToString
public class ConditionalTickable extends TickableDecorator {
    private final Condition performanceCondition;

    public ConditionalTickable(Condition performanceCondition, Tickable tickable) {
        super(tickable);
        this.performanceCondition = performanceCondition;
    }

    @Override
    public void onTick() {
        if (performanceCondition.isSatisfied()) {
            log.trace("Performance condition {} was met.", performanceCondition);
            super.onTick();
        } else {
            log.trace("Performance condition {} was not met.", performanceCondition);
        }
    }
}

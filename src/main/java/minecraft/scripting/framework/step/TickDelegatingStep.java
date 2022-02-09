package minecraft.scripting.framework.step;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import minecraft.scripting.framework.concept.Condition;
import minecraft.scripting.framework.concept.tickable.Tickable;

import java.util.Set;

@ToString
@RequiredArgsConstructor
public class TickDelegatingStep implements Step {
    private final Condition completionCondition;
    private final Set<Tickable> delegateTickables;

    @Override
    public boolean isCompleted() {
        return completionCondition.isSatisfied();
    }

    @Override
    public void onTick() {
        delegateTickables.forEach(Tickable::onTick);
    }
}

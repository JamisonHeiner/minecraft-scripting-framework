package minecraft.scripting.framework.step;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import minecraft.scripting.framework.concept.Resettable;

@ToString
@RequiredArgsConstructor
public class ResettableStep implements Step, Resettable {
    private final Step step;
    private final Resettable resettable;

    @Override
    public boolean isCompleted() {
        return step.isCompleted();
    }

    @Override
    public void reset() {
        resettable.reset();
    }

    @Override
    public void onTick() {
        step.onTick();
    }
}

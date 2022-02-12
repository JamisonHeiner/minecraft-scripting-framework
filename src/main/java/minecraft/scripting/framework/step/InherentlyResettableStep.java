package minecraft.scripting.framework.step;

import lombok.ToString;

@ToString
public class InherentlyResettableStep extends ResettableStep {
    public InherentlyResettableStep(Step step) {
        super(step, () -> {
        });
    }
}

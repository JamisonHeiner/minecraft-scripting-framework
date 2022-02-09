package minecraft.scripting.framework.concept.tickable;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString
@RequiredArgsConstructor
public abstract class TickableDecorator implements Tickable {
    protected final Tickable wrappedTickable;

    @Override
    public void onTick() {
        log.trace("Passing onTick call through to wrapped Tickable: {}", wrappedTickable);
        wrappedTickable.onTick();
    }
}

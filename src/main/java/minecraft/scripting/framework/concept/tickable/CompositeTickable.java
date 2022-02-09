package minecraft.scripting.framework.concept.tickable;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;

@ToString
@RequiredArgsConstructor
public class CompositeTickable implements Tickable {
    private final Set<Tickable> tickables;

    @Override
    public void onTick() {
        tickables.forEach(Tickable::onTick);
    }
}

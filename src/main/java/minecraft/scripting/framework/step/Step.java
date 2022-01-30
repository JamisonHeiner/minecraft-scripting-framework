package minecraft.scripting.framework.step;

import minecraft.scripting.framework.concept.Completable;
import minecraft.scripting.framework.concept.Tickable;

/**
 * An interface that performs a small unit of work on each Minecraft tick, until the work is completed.
 */
public interface Step extends Completable, Tickable {
}

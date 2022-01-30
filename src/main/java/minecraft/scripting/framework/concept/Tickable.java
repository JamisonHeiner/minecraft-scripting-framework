package minecraft.scripting.framework.concept;

/**
 * An interface that performs a small unit of work on each Minecraft tick.
 */
public interface Tickable {
    /**
     * Performs the work associated with this Tickable.
     */
    void onTick();
}

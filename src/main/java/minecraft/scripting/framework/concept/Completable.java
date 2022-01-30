package minecraft.scripting.framework.concept;

/**
 * An interface that marks a class as "Completable".
 */
public interface Completable {
    /**
     * Determine whether the Completable is completed.
     *
     * @return whether the Completable is completed.
     */
    boolean isCompleted();
}

package minecraft.scripting.framework.concept;

public interface Condition {
    boolean isSatisfied();

    default Condition and(Condition otherCondition) {
        return () -> isSatisfied() && otherCondition.isSatisfied();
    }

    default Condition or(Condition otherCondition) {
        return () -> isSatisfied() || otherCondition.isSatisfied();
    }

    static Condition not(Condition condition) {
        return () -> !condition.isSatisfied();
    }
}

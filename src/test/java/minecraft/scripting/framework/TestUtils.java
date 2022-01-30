package minecraft.scripting.framework;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public final class TestUtils {
    public static <E> Queue<E> queueOf(E... elements) {
        return new LinkedList<>(Arrays.asList(elements));
    }
}

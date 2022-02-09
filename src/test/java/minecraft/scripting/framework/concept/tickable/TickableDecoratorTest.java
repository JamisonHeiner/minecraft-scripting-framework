package minecraft.scripting.framework.concept.tickable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TickableDecoratorTest {
    TickableDecorator tickableDecorator;

    @Mock Tickable wrappedTickable;

    @Test
    void shouldPassCallsThroughToWrappedTickable() {
        // Given
        tickableDecorator = new TickableDecorator(wrappedTickable) {
        };

        // When
        tickableDecorator.onTick();

        // Then
        verify(wrappedTickable).onTick();
    }
}

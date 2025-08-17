package ru.otus.processor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProcessorSwapTest {
    private static final String F11 = "field11";
    private static final String F12 = "field12";
    @Test
    @DisplayName("Тестируем обмен значениями")
    void process() {
        var swap = new ProcessorSwap();

        var message = new Message.Builder(1L)
                .field11(F11)
                .field12(F12)
                .build();

        var result = swap.process(message);

        assertThat(result.getField11()).isEqualTo(F12);
        assertThat(result.getField12()).isEqualTo(F11);
    }
}
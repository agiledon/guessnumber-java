package xyz.zhangyi.tdd.guessnumber;

import org.junit.Test;
import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class AnswerTest {
    @Test
    public void should_throw_InvalidAnswerException_given_number_not_in_correct_range() {
        assertThatThrownBy(() -> new Answer(1, 2, 3, 10))
                .isInstanceOf(InvalidAnswerException.class)
                .hasMessage("out of range (0, 9)");
    }
}

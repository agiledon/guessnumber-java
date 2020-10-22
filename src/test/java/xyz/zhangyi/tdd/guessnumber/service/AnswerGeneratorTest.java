package xyz.zhangyi.tdd.guessnumber.service;

import org.junit.Test;
import xyz.zhangyi.tdd.guessnumber.infrastructure.IntRandom;
import xyz.zhangyi.tdd.guessnumber.model.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AnswerGeneratorTest {
    @Test
    public void should_generate_correct_answer() {
        // given
        IntRandom mockRandom = mock(IntRandom.class);
        when(mockRandom.next()).thenReturn(1, 2, 3, 10, 2, 4);

        AnswerGenerator generator = new AnswerGenerator(mockRandom);

        // when
        Answer actualAnswer = generator.generate();

        // then
        verify(mockRandom, times(6)).next();
        Answer expectedAnswer = Answer.of(1, 2, 3, 4);
        assertThat(actualAnswer).isEqualTo(expectedAnswer);
    }
}

package xyz.zhangyi.tdd.guessnumber.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnswerGeneratorTest {
    @Test
    public void should_generate_actual_answer() {
        // given
        RandomIntNumber mockRandom = mock(RandomIntNumber.class);
        when(mockRandom.next()).thenReturn(1, 2, 13, 3, 2, 4);

        AnswerGenerator answerGenerator = new AnswerGenerator(mockRandom);

        // when
        Answer answer = answerGenerator.generate();

        // then
        Answer expectedAnswer = Answer.of(1, 2, 3, 4);
        assertThat(answer).isEqualTo(expectedAnswer);
    }
}

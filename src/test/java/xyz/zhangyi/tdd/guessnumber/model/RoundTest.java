package xyz.zhangyi.tdd.guessnumber.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import xyz.zhangyi.tdd.guessnumber.model.GuessResult;
import xyz.zhangyi.tdd.guessnumber.model.Round;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundTest {
    @ParameterizedTest
    @MethodSource("provideAnswers")
    public void should_give_guess_result_given_input_answer(Answer input, String expectedResult) {
        // given
        Round round = new Round(Answer.of(1, 2, 3, 4));

        // when
        GuessResult actualResult = round.guess(input);

        // then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getResult()).isEqualTo(expectedResult);
    }

    public static Object[][] provideAnswers() {
        return new Object[][] {
                {Answer.of(1, 5, 6, 7), "1A0B"},
                {Answer.of(2, 4, 7, 8), "0A2B"},
                {Answer.of(0, 3, 2, 4), "1A2B"},
                {Answer.of(5, 6, 7, 8), "0A0B"},
                {Answer.of(4, 3, 2, 1), "0A4B"},
                {Answer.of(1, 2, 3, 4), "4A0B"}
        };
    }
}

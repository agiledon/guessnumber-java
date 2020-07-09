package xyz.zhangyi.tdd.guessnumber.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class RoundTest {
    @ParameterizedTest
    @MethodSource("guessRounds")
    public void should_guess_numbers(Answer answer, String guessResult) {

    }

    public static Object[][] guessRounds() {
        return new Object[][] {
                {Answer.of(1, 5, 6, 7), "1A0B"}
        };
    }
}

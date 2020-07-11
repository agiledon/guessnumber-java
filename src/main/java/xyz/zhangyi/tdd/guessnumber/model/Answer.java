package xyz.zhangyi.tdd.guessnumber.model;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

import java.util.HashSet;
import java.util.Set;

public class Answer {
    public Answer(int number1, int number2, int number3, int number4) {
        validateRange(number1, number2, number3, number4);
        validateDuplication(number1, number2, number3, number4);
    }

    private void validateRange(int number1, int number2, int number3, int number4) {
        validateRange(number1);
        validateRange(number2);
        validateRange(number3);
        validateRange(number4);
    }

    private void validateRange(int number) {
        if (number < 0 || number > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
    }

    private void validateDuplication(int number1, int number2, int number3, int number4) {
        Set<Integer> numbers = new HashSet<>(4);
        numbers.add(number1);
        numbers.add(number2);
        numbers.add(number3);
        numbers.add(number4);

        if (numbers.size() != 4) {
            throw new InvalidAnswerException("duplicated numbers");
        }
    }
}

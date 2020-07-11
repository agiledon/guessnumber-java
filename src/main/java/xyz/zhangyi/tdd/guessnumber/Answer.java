package xyz.zhangyi.tdd.guessnumber;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Answer {
    public Answer(int number1, int number2, int number3, int number4) {
        Set<Integer> numbers = new HashSet<>(4);
        numbers.add(number1);
        numbers.add(number2);
        numbers.add(number3);
        numbers.add(number4);


        if (numbers.size() != 4) {
            throw new InvalidAnswerException("duplicated numbers");
        }

        validRange(number1, number2, number3, number4);
    }

    private void validRange(int number1, int number2, int number3, int number4) {
        validRange(number1);
        validRange(number2);
        validRange(number3);
        validRange(number4);
    }

    private void validRange(int number1) {
        if (number1 < 0 || number1 > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
    }
}

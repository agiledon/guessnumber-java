package xyz.zhangyi.tdd.guessnumber.model;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

import java.util.*;

public class Answer {
    public static final int AMOUNT_OF_ANSWER_VALUE = 4;
    private List<Integer> numbers = new ArrayList<>(AMOUNT_OF_ANSWER_VALUE);

    public Answer(int number1, int number2, int number3, int number4) {
        numbers.add(number1);
        numbers.add(number2);
        numbers.add(number3);
        numbers.add(number4);
        validate(numbers);
    }

    public Answer(List<Integer> numbers) {
        this.numbers = numbers;
        validate(numbers);
    }

    private void validate(List<Integer> numbers) {
        for (Integer number : numbers) {
            validateRange(number);
        }
        validateDuplication(numbers);
    }

    private void validateRange(int number) {
        if (number < 0 || number > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
    }

    private void validateDuplication(List<Integer> numbers) {
        Set<Integer> numbersSet = new HashSet<>();
        for (Integer number : numbers) {
            numbersSet.add(number);
        }

        if (numbersSet.size() != AMOUNT_OF_ANSWER_VALUE) {
            throw new InvalidAnswerException("duplicated numbers");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(numbers, answer.numbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numbers);
    }
}

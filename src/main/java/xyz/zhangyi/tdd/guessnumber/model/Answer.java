package xyz.zhangyi.tdd.guessnumber.model;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

import java.util.*;

public class Answer {
    public static final int AMOUNT_OF_ANSWER_VALUE = 4;
    private List<Integer> numbers = new ArrayList<>(AMOUNT_OF_ANSWER_VALUE);

    private Answer(int number1, int number2, int number3, int number4) {
        numbers.add(number1);
        numbers.add(number2);
        numbers.add(number3);
        numbers.add(number4);
        validate(numbers);
    }

    private Answer(List<Integer> numbers) {
        this.numbers = numbers;
        validate(numbers);
    }

    public static Answer of(List<Integer> numbers) {
        return new Answer(numbers);
    }

    public static Answer of(int number1, int number2, int number3, int number4) {
        return new Answer(number1, number2, number3, number4);
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

    public String compare(Answer input) {
        int valueOfA = 0;
        int valueOfB = 0;

        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < input.numbers.size(); j++) {
                if (this.sameValue(input, i, j) && this.samePosition(i, j)) {
                    valueOfA++;
                } else {
                    if (this.sameValue(input, i, j)) {
                        valueOfB++;
                    }
                }
            }
        }
        return String.format("%sA%sB", valueOfA, valueOfB);
    }

    private boolean samePosition(int indexOfActualAnswer, int indexOfInputAnswer) {
        return indexOfActualAnswer == indexOfInputAnswer;
    }

    private boolean sameValue(Answer input, int indexOfActualAnswer, int indexOfInputAnswer) {
        return numbers.get(indexOfActualAnswer).equals(input.numbers.get(indexOfInputAnswer));
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

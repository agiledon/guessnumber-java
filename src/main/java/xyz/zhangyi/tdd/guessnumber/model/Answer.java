package xyz.zhangyi.tdd.guessnumber.model;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Answer {
    private int number1;
    private int number2;
    private int number3;
    private int number4;

    public Answer(int number1, int number2, int number3, int number4) {
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
        this.number4 = number4;

        validateRange();
        validateDuplication();
    }

    private void validateRange() {
        validateRange(this.number1);
        validateRange(this.number2);
        validateRange(this.number3);
        validateRange(this.number4);
    }

    private void validateRange(int number) {
        if (number < 0 || number > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
    }

    private void validateDuplication() {
        Set<Integer> numbers = new HashSet<>(4);
        numbers.add(this.number1);
        numbers.add(this.number2);
        numbers.add(this.number3);
        numbers.add(this.number4);

        if (numbers.size() != 4) {
            throw new InvalidAnswerException("duplicated numbers");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return number1 == answer.number1 &&
                number2 == answer.number2 &&
                number3 == answer.number3 &&
                number4 == answer.number4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number1, number2, number3, number4);
    }
}

package xyz.zhangyi.tdd.guessnumber;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

public class Answer {
    public Answer(int number1, int number2, int number3, int number4) {
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

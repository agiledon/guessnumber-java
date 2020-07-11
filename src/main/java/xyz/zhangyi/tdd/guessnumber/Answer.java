package xyz.zhangyi.tdd.guessnumber;

import xyz.zhangyi.tdd.guessnumber.exception.InvalidAnswerException;

public class Answer {
    public Answer(int number1, int number2, int number3, int number4) {
        if (number1 < 0 || number1 > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
        if (number2 < 0 || number2 > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
        if (number3 < 0 || number3 > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
        if (number4 < 0 || number4 > 9) {
            throw new InvalidAnswerException("out of range (0, 9)");
        }
    }
}

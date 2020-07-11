package xyz.zhangyi.tdd.guessnumber.service;

import xyz.zhangyi.tdd.guessnumber.model.Answer;

public class AnswerGenerator {
    private IntRandom random;

    public AnswerGenerator(IntRandom random) {
        this.random = random;
    }

    public Answer generate() {
        int number1 = random.next();
        int number2 = random.next();
        int number3 = random.next();
        int number4 = random.next();

        return new Answer(number1, number2, number3, number4);
    }
}

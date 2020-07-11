package xyz.zhangyi.tdd.guessnumber.service;

import xyz.zhangyi.tdd.guessnumber.model.Answer;

import java.util.ArrayList;

public class AnswerGenerator {
    private IntRandom random;

    public AnswerGenerator(IntRandom random) {
        this.random = random;
    }

    public Answer generate() {
        ArrayList<Integer> numbers = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            int number = random.next();
            while (number < 0 || number > 9 || numbers.contains(number)) {
                number = random.next();
            }
            numbers.add(number);
        }

        return new Answer(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3));
    }
}

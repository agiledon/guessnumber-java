package xyz.zhangyi.tdd.guessnumber.service;

import xyz.zhangyi.tdd.guessnumber.model.Answer;

import java.util.ArrayList;

public class AnswerGenerator {
    private IntRandom random;

    public AnswerGenerator(IntRandom random) {
        this.random = random;
    }

    public Answer generate() {
        ArrayList<Integer> numbers = generateUniqueCorrectNumbers();
        return new Answer(numbers);
    }

    private ArrayList<Integer> generateUniqueCorrectNumbers() {
        ArrayList<Integer> numbers = new ArrayList<>(Answer.AMOUNT_OF_ANSWER_VALUE);

        for (int i = 0; i < Answer.AMOUNT_OF_ANSWER_VALUE; i++) {
            int number = random.next();
            while (isOutOfRange(number) || isDuplicate(numbers, number)) {
                number = random.next();
            }
            numbers.add(number);
        }
        return numbers;
    }

    private boolean isDuplicate(ArrayList<Integer> numbers, int number) {
        return numbers.contains(number);
    }

    private boolean isOutOfRange(int number) {
        return number < 0 || number > 9;
    }
}

package xyz.zhangyi.tdd.guessnumber.model;

public class Round {
    private Answer actualAnswer;

    public Round(Answer actualAnswer) {
        this.actualAnswer = actualAnswer;
    }

    public GuessResult guess(Answer input) {
        String result = actualAnswer.compare(input);
        return GuessResult.of(result);
    }
}

package xyz.zhangyi.tdd.guessnumber.model;

public class GuessResult {
    private String result;

    private GuessResult(String result) {
        this.result = result;
    }

    public static GuessResult of(String result) {
        return new GuessResult(result);
    }

    public String getResult() {
        return this.result;
    }
}

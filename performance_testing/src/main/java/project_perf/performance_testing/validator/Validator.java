package project_perf.performance_testing.validator;

public class Validator {

    private boolean containsOnlyNumbers(String text) {
        return text.matches("\\d+");
    }

    private boolean isInsideBounds(String text, Integer max, Integer min) {
        Integer value = Integer.parseInt(text);
        return min <= value && value <= max;
    }

    public boolean areInputsValid(String text, Integer max, Integer min) {
        return containsOnlyNumbers(text) && isInsideBounds(text, max, min);
    }
}

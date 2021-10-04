public class Main {
    private static final int ZERO = 0;
    public static final int DEFAULT_VALUE = 1;

    public static void main(String[] args) {
        int var = 2;
        int result = suppressExceptionAndGetDefault(var);
        System.out.println(result);
        result = suppressExceptionAndThrowRuntime(var);
    }

    public static int divideByZero(final int var) {
        return var / ZERO;
    }

    public static int suppressExceptionAndGetDefault(int var) {
        int result;
        try {
            result = divideByZero(var);
        } catch (ArithmeticException e) {
            System.out.println("Unable to get result. Default value returning.");
            result = DEFAULT_VALUE;
        }
        return result;
    }

    public static int suppressExceptionAndThrowRuntime(int var) {
        try {
            return divideByZero(var);
        } catch (ArithmeticException e) {
            throw new RuntimeException("Unable to get result.", e);
        }
    }
}

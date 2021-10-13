public class Main {
    private static final int ZERO = 0;
    public static final int DEFAULT_VALUE = 1;

    /* ИСКЛЮЧЕНИЕ - объект, описывающий ошибку при исполнении программы.
       Механизм пробрасывания исключений существует для экстренной остановки программы.
       Механихм отлавливания(обработки) исключений существует для продолжения работы программы несмотря на ошибку.
       В этом случае результат выполнения программы получает Дефолтное значение(если оно задано),
       либо некий объект с Негативным Состоянием(должно быть предусмотрено в реализации самого объекта).

       Суперкласс для всех исключений - Throwable.
       Исключения делятся на 2 типа: Error(unchecked - НЕ требует обработки в Try/Catch, либо объявления в сигнатуре)
        и Exception(checked - должен быть сразу в Try/Catch, либо объявлен в сигнатуре!).
        У Exception есть наследник - RuntimeException, который как и Error МОЖЕТ не обрабатываться(unchecked).
       */
    public static void main(String[] args) {
        int result = DEFAULT_VALUE;
        try {
            /* .parseInt может выбросить NumberFormatException, который будет обработан
                как RuntimeException(см.2ой блок catch).
              */
            int var = Integer.parseInt(args[0]);
            /* Метод suppressExceptionAndGetDefault(var) возврщает валидное значение, независимо от исключений.
               Требует наличие значения по умолчанию. */
            result = suppressExceptionAndGetDefault(var);
            switch (var) {
                case 1:
                    /*  Метод suppressExceptionAndThrowRuntime() оборачивает оригинальное исключение в RuntimeException.
                        Этот способ используется для экстренной остановки работы программы в методах,
                        не предусматривающих пробрасывание исключений(т.е. в сигнатуре нет блока throws).
                        Например методы интерфейса Iterator - hasNext, next.
                     */
                    result = suppressExceptionAndThrowRuntime(var);
                    break;
                case 2:
                    /* Метод throwException() обрабатывает исключение и пробрасывает его же.
                       Этот способ используется  для экстренной остановки работы программы, в случаях,
                       когда исключение возникает на некой промежуточной стадии, не предусматривающей возвращения
                       конечного результата, при этом продолжение работы программы на данном этапе невозможно.
                       Перед пробрасыванием исключения требуются дополнительные действия.
                      */
                    result = throwException(var);
                    break;
                default:
                    /* Метод suppressExceptionAndThrowCustom() оборачивает оригинальное исключение в пользовательское(кастомное).
                       Этот способ используется для ограничения диапазона обрабатываемых исключений(обрабатываются только
                       пользовательские исключения, остальные - пробрасываются дальше).
                     */
                    result = suppressExceptionAndThrowCustom(var);
            }
            /* Блоки catch обрабатывают исключения в порядке ОТ частного К общему.
              Например, ArithmeticException у нас обрабатывается отдельно, хотя является наследником RuntimeException
              и может быть обработан на общих основаниях. */
        } catch (ArithmeticException e) {
            System.out.println("Unable to get result. ArithmeticException: " + e.getMessage());
            /* Данный catch использует бинарное ИЛИ, т.е. происходит обработка разных типов исключений оним способом.*/
        } catch (RuntimeException | CustomException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage()
                    + (e.getCause() != null ? " Cause: " + e.getCause() : ""));
        }
        System.out.println("result: " + result);
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

    public static int suppressExceptionAndThrowCustom(int var) throws CustomException {
        try {
            return divideByZero(var);
        } catch (ArithmeticException e) {
            throw new CustomException("Unable to get result.", e);
        }
    }

    public static int throwException(int var) {
        try {
            return divideByZero(var);
        } catch (ArithmeticException e) {
            System.out.println("Throwing ArithmeticException...");
            throw e;
        }
    }

    static class CustomException extends Exception {
        public CustomException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

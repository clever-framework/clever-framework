package io.toquery.framework.example.test.jvm;

/**
 * Stack deep : 10828 class java.lang.StackOverflowError
 */
public class StackErrorMock {
    private static int index = 1;

    public void call() {
        index++;
        call();
    }

    public static void main(String[] args) {
        StackErrorMock mock = new StackErrorMock();
        try {
            mock.call();
        } catch (Throwable e) {
            System.err.println("Stack deep : " + index + " " + e.getClass());
            // e.printStackTrace();
        }
    }
}

package demo.jason.lambda;

public class LambdaDemo {
    public static void main(String[] args) {
        Runnable r = () -> System.out.println("Hello Lambda");
        r.run();
    }
}

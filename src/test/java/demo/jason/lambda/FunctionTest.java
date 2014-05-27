package demo.jason.lambda;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.*;

import java.util.function.*;

/**
 * Test cases for class java.util.function.Function.
 * @author Jason Huang
 **/

@Test
public class FunctionTest {
    //normal use cases
    public void testBasic() {
        Function<String, String> f = (String s) -> {
            return s.toUpperCase();
        };
        //we need this way to invoke functional interface
        //assertEquals(f("abc"), "ABC");
        assertEquals(f.apply("abc"), "ABC");
    }

    public void testIdentity() {
        Function<String, String> f = Function.identity();
        assertEquals(f.apply("abc"), "abc");
    }

    public void testCompose() {
        Function<String, String> f = (String s) -> {
            return s.toUpperCase();
        };
        Function<String, String> f_to_compose = (String s) -> {
            return s + ":compose";
        };
        f = f.compose(f_to_compose);
        assertEquals(f.apply("abc"), "ABC:COMPOSE");
    }

    public void testAndThen() {
        Function<String, String> f = (String s) -> {
            return s.toLowerCase();
        };
        Function<String, String> f_to_andThen = (String s) -> {
            return s + ":AndThen";
        };
        f = f.andThen(f_to_andThen);
        assertEquals(f.apply("ABC"), "abc:AndThen");
    }

    //abnormal test cases
}


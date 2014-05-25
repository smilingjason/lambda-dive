package demo.jason.lambda;

import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @Author Jason Huang
 **/

@Test
public class InterfaceStaticMethodTest {
    interface Foo {
        static String op() {
            return "Foo";
        }
    }

    public void testBasicInterfaceStaticMethod() {
        assertEquals(Foo.op(), "Foo");
    }
}

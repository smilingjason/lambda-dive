package demo.jason.lambda;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

/**
 * Java SE 8 introduces the concept of interface static method.
 * With it, no need to create a second Class just for containing the utility
 * methods. Before Java SE 8, the utility method for interface Collection into
 * Collections class.
 *
 * @author Jason Huang
 **/

@Test
public class InterfaceStaticMethodTest {
    /**
     * test interface.
     **/
    interface Foo {
        /**
         * interface's static method.
         * @return constant string 'Foo'.
         **/
        static String op() {
            return "Foo";
        }
    }

    /**
     * Call the static method of a interface and check the result.
     **/
    public void testBasicInterfaceStaticMethod() {
        assertEquals(Foo.op(), "Foo");
    }
}

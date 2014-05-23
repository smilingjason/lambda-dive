package demo.jason.lambda;

import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @Author Jason Huang
 **/

@Test
public class VariableCaptureTest {
    class Person {
        String name;
    }
    interface Foo {
        public String getName();
    }

    public void testVariableCapture() {
        Person p = new Person();
        p.name = "Jason";

        Person copy_p = p; // save the reference variable value 
        //create a functional interface, passing p as parameter
        //foo will save a copy to the reference as copy_p does
        Foo foo = getFoo(p);

        //p.name will be changed by getFoo(p)
        assertEquals(foo.getName(), "Foo");

        //change the name of p, it will be captured by 
        //lambda expression foo
        p.name = "Jason Huang";
        assertEquals(foo.getName(), "Jason Huang");

        //lambda expression is close to the values, not the
        //variables
        p = null;
        // foo will capture the value of variable, not the
        // varialbe itself
        assertEquals(foo.getName(), "Jason Huang");

        p = new Person();
        p.name = "Java";
        copy_p.name = "Copy";
        assertEquals(foo.getName(), "Copy");
    }

    private Foo getFoo(Person p) {
        Foo foo = () -> {
            return p.name;
        };
        //local variable referenced from a lambda expression must be final or 
        //effectively final which means that if the varialbe is a reference 
        //variable, the variable cannot be modified to point to other object;
        //if the variable is a primitive one, its value cannot be changed.
        //p = null; //cause compilation error 
        p.name = "Foo"; //but the content of the referenced object can be changed.
        return foo;
    }
}




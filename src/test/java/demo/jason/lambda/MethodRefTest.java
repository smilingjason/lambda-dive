package demo.jason.lambda;

import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @Author Jason Huang
 **/

@Test
public class MethodRefTest {
    
    interface Foo {
        public String op();
    }

    interface DefaultFactory {
        public MRPerson make();
    }
    interface Factory {
        public MRPerson make(String name);
    }
    public void testInstanceMethodRef() {
        MRPerson p = new MRPerson("Jason");
        Foo foo = p::getName;
        assertEquals(foo.op(), "Jason");
    }
    public void testStaticMethodRef() {
        Foo foo = MRPerson::getClassName;
        assertEquals(foo.op(), "Person");
    }
    public void testConstructorMethodRef() {
        Factory factory = MRPerson::new;
        MRPerson p = factory.make("Hello");
        assertEquals(p.getName(), "Hello");
    }
    public void testConstructorMethodRef2() {
        DefaultFactory factory = MRPerson::new;
        MRPerson p = factory.make();
        assertEquals(p.getName(), "Default");
    }
}

class MRPerson {
    String name;
    
    public MRPerson() {
        this.name = "Default";
    }
    public MRPerson(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static String getClassName() {
        return "Person";
    }

}

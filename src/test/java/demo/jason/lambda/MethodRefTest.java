package demo.jason.lambda;

import java.util.function.*;
import java.util.stream.*;
import java.util.Arrays;
import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Jason Huang
 **/

@Test
public class MethodRefTest {
    
    interface Foo {
        public String op();
    }
    interface Foo2 {
        public String op(String prefix);
    }
    interface Bar {
        public String op(MRPerson p);
    }
    interface DefaultFactory {
        public MRPerson make();
    }
    interface Factory {
        public MRPerson make(String name);
    }
    public String toUpper(Bar bar) {
        MRPerson p = new MRPerson("abc");
        String a = bar.op(p);
        return a.toUpperCase();
    }

    public void testInstanceMethodRefWithObj() {
        MRPerson p = new MRPerson("Jason");
        Foo foo = p::getName;
        assertEquals(foo.op(), "Jason");
    }
    public void testInstanceMethodRefWithOverload() {
        MRPerson p = new MRPerson("Jason");
        Foo2 foo2 = p::getName;
        assertEquals(foo2.op("prefix"), "prefixJason");
    }
    public void testInstanceMethodRefWithSuper() {
        MRChildPerson p = new MRChildPerson("Jason");
        Foo foo = p::getName;
        assertEquals(foo.op(), ":Jason");
    }
    public void testInstanceMethodRefWithRefType() {
        String result =  toUpper(MRPerson::getName);
        assertEquals(result, "ABC");
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
    public void testConstructorMethodRefWithOverload() {
        DefaultFactory factory = MRPerson::new;
        MRPerson p = factory.make();
        assertEquals(p.getName(), "Default");
    }
    public void testArrayConstructor() {
        IntFunction<int[]> ifun = int[]::new;
        int[] a = ifun.apply(10);
        assertTrue(a.length == 10);
        Arrays.stream(a).forEach(v -> assertEquals(v, 0));
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
    public String getName(String prefix) {
        return prefix + name;
    }
    public static String getClassName() {
        return "Person";
    }

}

class MRChildPerson extends MRPerson {
    MRChildPerson() {
        super();
    }
    MRChildPerson(String name) {
        super(name);
    }
    public String getName() {
        MethodRefTest.Foo foo2 = super::getName;

        return ":" + foo2.op();
    }
}

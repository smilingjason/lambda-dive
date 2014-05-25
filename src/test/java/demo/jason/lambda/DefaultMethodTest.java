package demo.jason.lambda;

import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @Author Jason Huang
 **/

@Test
public class DefaultMethodTest {
    interface Foo {
        default public String op() {
            return "Foo";
        }
    }
    interface FooChildA extends Foo {
    }
    interface FooChildB extends Foo {
        default public String op() {
            return "FooChildB";
        }
    }
    interface Bar {
        default public String op() {
            return "Bar";
        }
    }
    class FooClass implements Foo {
    }
    class FooClassWithImpl implements Foo {
        public String op() {
            return "FooClassWithImpl";
        }
    }
    interface Mix extends Foo, Bar {
        //must override the unrelated defaut method from Foo and Bar, since there
        //is a confliction.
        default public String op() {
            //Child can use Parent.super.op() to refer to the parent's method
            //return Foo.super.op();
            return "Mix";
        }
    }
    class MixClass implements Foo, Bar {
        //must override the unrelated defaut method from Foo and Bar, since there
        //is a confliction.
        public String op() {
            return "MixClass";
        }
    }
    class O implements FooChildA, FooChildB {
    }

    public void testBasicDefatulMethod() {
        FooClass o = new FooClass();
        assertEquals(o.op(), "Foo");
    }
    public void testDefatulMethodOverride() {
        FooClassWithImpl o = new FooClassWithImpl();
        assertEquals(o.op(), "FooClassWithImpl");
    }
    public void testDefatulMethodMustOverride() {
        MixClass o = new MixClass();
        assertEquals(o.op(), "MixClass");
    }
    public void testDefatulMethodResolve() {
        O o = new O();
        assertEquals(o.op(), "FooChildB");
    }
}


package demo.jason.lambda;
import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
@Test

/**
 * Lambda doesn't introduce any new variable scope, and it will reference the variables in enclosing block.
 **/ 
public class VariableScopeTest { 
    int i = 100; 

    interface Foo {
        public int op();
    }

    interface Bar {
        public Object whoami();
    }

    class FirstLevel implements Foo {
        int i = 200;
        public void firstLevelMethod() {
            int out = VariableScopeTest.this.i;
        }
        public int op() {
            i = i * 3;
            return i;
        }
    }

    public void testScope() {
        Foo fooLambda = () -> {
            i = i * 2; 
            return i;
        };
        assertEquals(fooLambda.op(), 200);

        i = 200;
        assertEquals(fooLambda.op(), 400);

        Foo fooInnerClass = new FirstLevel();
        assertEquals(fooInnerClass.op(), 600);
    }

    /**
     * In lambda expression, 'this' refers to the enclosing object.
     * This is different from innert class.
     **/ 
    public void testThis() {
        Bar bar = () -> this;
        Object o = bar.whoami();
        assertTrue(o instanceof VariableScopeTest);
    }
}

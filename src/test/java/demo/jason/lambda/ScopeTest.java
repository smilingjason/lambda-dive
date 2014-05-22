import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
@Test
public class ScopeTest { 
    int i = 100; 

    interface Foo {
        public int op();
    }

    class FirstLevel implements Foo {
        public int i = 200;
        public void firstLevelMethod() {
            int i = ScopeTest.this.i;
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
}

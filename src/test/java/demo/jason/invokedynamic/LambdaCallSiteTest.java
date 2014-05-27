package demo.jason.invokedynamic;
import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

/**
 * This test case dive into the implementation of lambda express.
 * Generally, Java SE 8 levearge invokedynamic jvm instruction to implement 
 * lambda expression. The initial call flow is:
 * 1) call the bootstrap method LambdaMetafactory.metafactory to get a dynamic call site
 * 2) invoke the call site to get functional interface object
 * 3) call the functional interface object 
 * Then, the following call will by pass step 1) to get better performance if call site
 * remains unchanged.
 *
 * Java SE 8 compiler may choose private instance or static method to implement the
 * lambda expression. This depends on if where the lambda express is declared and if any
 * instance variable or local variable are captured. See the following test cases for detail.
 *
 * @author Jason Huang
 **/

@Test
public class LambdaCallSiteTest {
    //this is a instance variable we're going reference.
    String lastName = "Huang";
    //this is the funcational interface to test
    interface Foo {
        public String bar(String name);
    }
    //private instance method implementation for lambda expression 
    private String sayHello(String name) {
        return "Hello, " + name + this.lastName;
    }

    //private static method implementation for lambda expression 
    private static String sayHelloStatic(String name) {
        return "Hello static, " + name;
    }

    //private static method implementation for lambda expression 
    //This implementation will capture one local variable of Integer type.
    private static String sayHelloStaticWithOneCapturedVariable(Integer captured, String name) {
        return "Hello captured, " + captured + name;
    }
         
    public void testLambdaCallSiteWithPrivateInstanceMethod() {
        try {
            
            Foo f = (name) ->  "Hello, " + name + this.lastName;
            assertEquals(f.bar("Jason"), "Hello, JasonHuang");
            
            //below is similar implementation of the above lambda expression
            //it leverage the private instance method: sayHello since it reference the
            //instance varialbe this.lastName
            MethodType type = MethodType.methodType(String.class, String.class);
            MethodType invokedtype = MethodType.methodType(Foo.class, 
                   LambdaCallSiteTest.class);
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Class thisClass = lookup.lookupClass(); // (who am I?)
            MethodHandle sayHello = lookup.findVirtual(thisClass, 
                   "sayHello", MethodType.methodType(String.class,String.class));
            CallSite callsite = LambdaMetafactory.metafactory(lookup, 
               "bar", invokedtype, type, sayHello, type);
            Foo r = (Foo) callsite.getTarget().invoke(this);
            assertEquals(r.bar("Jason"), "Hello, JasonHuang");
       } catch (Throwable t) {
           t.printStackTrace();
           fail(t.getMessage());
       }
    }

    public void testLambdaCallSiteWithPrivateStaticInstanceMethod() {
        try {
            Foo f = (name) -> "Hello static, " + name;
            assertEquals(f.bar("Jason"), "Hello static, Jason");

            //below is similar implementation of the above lambda expression
            //it leverage the private static method: sayHelloStatic since it doesn't reference
            //any instance varialbe or local variable.
            MethodType type = MethodType.methodType(String.class, String.class);
            MethodType invokedtype = MethodType.methodType(Foo.class); 
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Class thisClass = lookup.lookupClass(); // (who am I?)
            MethodHandle sayHello = lookup.findStatic(thisClass, 
                   "sayHelloStatic", MethodType.methodType(String.class,String.class));
            CallSite callsite = LambdaMetafactory.metafactory(lookup, 
               "bar", invokedtype, type, sayHello, type);
            Foo r = (Foo) callsite.getTarget().invoke();
            assertEquals(r.bar("Jason"), "Hello static, Jason");
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }
    
    public void testLambdaCallSiteWithPrivateStaticInstanceMethodAndOneCapturedVariable() {
        try {

            Integer captured= 100;
            Foo f = (name) -> "Hello captured, " + captured + name;
            assertEquals(f.bar("Jason"), "Hello captured, 100Jason");

            //below is similar implementation of the above lambda expression
            //it leverage the private static method: sayHelloStaticWithOneCapturedVariable
            //since it only references a local varialbe Integer captured.
            //In the implementation, it will be the first parameter.
            MethodType type = MethodType.methodType(String.class, String.class);
            MethodType invokedtype = MethodType.methodType(Foo.class, Integer.class); 
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Class thisClass = lookup.lookupClass(); // (who am I?)
            MethodHandle sayHello = lookup.findStatic(thisClass, 
                   "sayHelloStaticWithOneCapturedVariable", 
                   MethodType.methodType(String.class, Integer.class, String.class));
            CallSite callsite = LambdaMetafactory.metafactory(lookup, 
               "bar", invokedtype, type, sayHello, type);
            Foo r = (Foo) callsite.getTarget().invoke(100);
            assertEquals(r.bar("Jason"), "Hello captured, 100Jason");
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }
}

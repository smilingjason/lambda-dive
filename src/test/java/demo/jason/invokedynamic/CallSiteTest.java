package demo.jason.invokedynamic;
import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

/**
 * @author Jason Huang
 **/

@Test
public class CallSiteTest {

   private static MethodHandle sayHello;
     
   private static String sayHello(String name) {
       return "Hello, " + name;
   }
         
   public static CallSite bootstrapDynamic(MethodHandles.Lookup caller, 
           String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
       MethodHandles.Lookup lookup = MethodHandles.lookup();
       Class thisClass = lookup.lookupClass(); // (who am I?)
       sayHello = lookup.findStatic(thisClass, "sayHello", MethodType.methodType(String.class,String.class));
       return new ConstantCallSite(sayHello.asType(type));
   }

   public void testCallSite() {
       try {
           MethodHandles.Lookup lookup = MethodHandles.lookup();
           CallSite callsite = bootstrapDynamic(lookup, 
               "abc", MethodType.methodType(String.class,String.class));
           String r = (String) callsite.getTarget().invoke("Jason");
           assertEquals(r, "Hello, Jason");
       } catch (Throwable t) {
           fail(t.getMessage());
       }
   }
}
  

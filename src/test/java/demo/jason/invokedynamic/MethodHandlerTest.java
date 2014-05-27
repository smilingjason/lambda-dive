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
public class MethodHandlerTest {
    interface Happy {
        public String areYouHappy(String name);
    }
    class HappyJava implements Happy {
        public String areYouHappy(String name) {
            return "Yes, I'm (" + name + ")";
        }
    }
    class NotHappyJava implements Happy {
        public String areYouHappy(String name) {
            return "Yes, I'm not (" + name + ")";
        }
    }

    public void testMathodHandlerBasic() {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle areYouHappyHandle = lookup.findVirtual(
                    Happy.class, 
                    "areYouHappy", 
                    methodType(String.class, String.class));
            HappyJava java = new HappyJava();
            String r = (String) areYouHappyHandle.invoke(java, "Java");
            assertEquals(r, "Yes, I'm (Java)");
            NotHappyJava notjava = new NotHappyJava();
            r = (String) areYouHappyHandle.invoke(notjava, "Java");
            assertEquals(r, "Yes, I'm not (Java)");
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }
}

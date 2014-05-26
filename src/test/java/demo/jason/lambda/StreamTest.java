package demo.jason.lambda;

import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.stream.*;
import java.util.*;

/**
 * @Author Jason Huang
 **/

@Test
public class StreamTest {

    public void testBasicStream() {
        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9);
        //find the sum of all elements
        assertEquals(numbers.stream()
                            .mapToInt(StreamTest::map)
                            .sum(), 45);
        //find the sum of all elements in parallel way
        assertEquals(numbers.parallelStream()
                            .mapToInt(StreamTest::map)
                            .sum(), 45);
        //find the sum of all even-value elements
        assertEquals(numbers.stream()
                            .filter(value -> value % 2 == 0)
                            .mapToInt(value -> value)
                            .sum(), 20);
        //find the sum of all odd-value elements
        assertEquals(numbers.stream()
                            .filter(value -> value % 2 != 0)
                            .mapToInt(value -> value)
                            .sum(), 25);
    }

    public static int map(int value) {
        System.out.println("Thread id for :" + value 
                + " is " + Thread.currentThread().getId());
        return value;
    }
}

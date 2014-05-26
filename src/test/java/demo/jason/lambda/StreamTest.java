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
                            .mapToInt(StreamTest::map)
                            .sum(), 20);
        //find the sum of all odd-value elements
        assertEquals(numbers.stream()
                            .filter(value -> value % 2 != 0)
                            .mapToInt(StreamTest::map)
                            .sum(), 25);
    }

    public static int map(int value) {
        return value;
    }

    public void testParrelStream() {
        int[] a = new int[1024 * 1024 * 10];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        Set threadSet = new HashSet();
        //first, test with sequential stream
        long start = System.nanoTime();
        Arrays.stream(a)
              .map(v -> {
                  //save the thread id 
                  threadSet.add(Thread.currentThread().getId());
                  return v;
              })
              .sum();
        long end = System.nanoTime();
        System.out.println("sequential: " + (end - start) / 1.0e6);
        assertEquals(threadSet.size(), 1);      
        //then, test with parellel
        threadSet.clear();
        start = System.nanoTime();
        Arrays.stream(a)
              .parallel()
              .map(v -> {
                  //save the thread id 
                  threadSet.add(Thread.currentThread().getId());
                  return v;
              })
              .sum();        
        end = System.nanoTime();
        System.out.println("parallel: " + (end - start) / 1.0e6 
                + " with thread count: " + threadSet.size());
        assertTrue(threadSet.size() > 1);      
    }
}

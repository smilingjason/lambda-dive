package demo.jason.lambda;

import org.testng.annotations.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.stream.*;
import java.util.*;
import java.util.concurrent.atomic.*;
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
        //then, test with parallel
        threadSet.clear();
        start = System.nanoTime();
        Arrays.stream(a)
              .parallel() //use a parallel stream
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

    public void testOperationLaziness() {
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        AtomicInteger ai = new AtomicInteger(0);
        Stream<Integer> stream = list.stream();
        Stream<Integer> mappedStream =stream.map(v -> {
            ai.incrementAndGet();
            return v * 3; 
        });
        assertEquals(ai.get(), 0); //map is a lazy operation

        Stream<Integer> filteredStream = mappedStream.filter(v -> {
            ai.incrementAndGet();
            return (v % 2) == 0; 
        });
        assertEquals(ai.get(), 0); //filter is a lazy operation

        //stream.count() is not lazy op it's a terminal op.
        //when it's called, all the lazy ops will be fused and executed
        assertEquals(filteredStream.count(), 2); 
        assertEquals(ai.get(), list.size() * 2);
    }
    
    //this test case shows the fusing and short-cutting of stream ops
    public void testShortcutOperation() {
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        AtomicInteger ai = new AtomicInteger(0);
        Stream<Integer> stream = list.stream();
        Stream<Integer> filteredStream = stream.filter(v -> {
            ai.incrementAndGet();
            return (v % 2) == 0; 
        });
        java.util.Optional<Integer> r = filteredStream.findFirst();
        //first even number is 2 
        assertEquals(r.get().intValue(), 2);
        //filter is a lazy op and it's fused with findFirst which is short-cutting
        //terminal op. So, filter operation won't execute for all data elements.
        //In this case, it will stop at 2nd element which matches the findFirst.
        //This will help on performance.
        assertEquals(ai.get(), 2);

    }
}

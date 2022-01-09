package io.toquery.framework.example.test.java.lambda;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class TestPeekAndMapForEach {
    private List<String> data = Lists.newArrayList("aaa", "bbb", "ccc", "ddd", "eee", "fff");

    private List<User> users = Lists.newArrayList(new User("aaa"), new User("bbb"), new User("ccc"), new User("ddd"), new User("eee"), new User("fff"));

    /**
     * java8流中所有的操作都是蓄而不发的，只有执行foreach，collect等终结操作时，流的操作才会执行。
     * 而且流内部会自动进行优化，只要得到想要的解决就不会继续进行计算了。
     * peek是个中间操作，它提供了一种对流中所有元素操作的方法，而不会把这个流消费掉（foreach会把流消费掉），然后你可以继续对流进行其他操作。
     *
     * 换句话说： foreach 是终结流操作，peek如果不调用终结流操作，则不会执行
     */
    @Test
    public void testPeek01(){
        Stream.of("one", "two", "three", "four").peek(e -> System.out.println(e));


    }


    @Test
    public void testPeek02(){
        Stream.of("one", "two", "three", "four").peek(e -> System.out.println(e)).collect(Collectors.toList());
    }

    @Test
    public void testPeek03(){
        Stream.of("one", "two", "three", "four")
                .peek(e -> System.out.println("Peeked value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
    }

    /**
     * one
     * two
     * three
     * four
     * one:map
     * two:map
     * three:map
     * four:map
     */
    @Test
    public void testPeek04() {
        Stream.of("one", "two", "three", "four").peek(e -> e = e + ":peek").forEach(System.out::println);
        Stream.of("one", "two", "three", "four").map(e -> e + ":map").forEach(System.out::println);
    }
    /**
     * aaa
     * bbb
     * ccc
     * ddd
     * eee
     * fff
     * aaa
     * bbb
     * ccc
     * ddd
     * eee
     * fff
     */
    @Test
    public void testPeek() {
        List<String> newData = data.stream().peek(item -> item = item + ":peek").collect(Collectors.toList());
        data.forEach(System.out::println);
        newData.forEach(System.out::println);
    }


    /**
     * aaa
     * bbb
     * ccc
     * ddd
     * eee
     * fff
     * aaa:map
     * bbb:map
     * ccc:map
     * ddd:map
     * eee:map
     * fff:map
     */
    @Test
    public void testMap() {
        List<String> newData = data.stream().map(item -> item + ":map").collect(Collectors.toList());
        data.forEach(System.out::println);
        newData.forEach(System.out::println);
    }

    /**
     * aaa:for-each
     * bbb:for-each
     * ccc:for-each
     * ddd:for-each
     * eee:for-each
     * fff:for-each
     * aaa
     * bbb
     * ccc
     * ddd
     * eee
     * fff
     */
    @Test
    public void testForEach() {
        data.forEach(item -> {
            item = item + ":for-each";
            System.out.println(item);
        });
        data.forEach(System.out::println);
    }

    @Data
    @AllArgsConstructor
    static class User{
        private String userName;
    }

    /**
     *
     */

    @Test
    public void testPeekObject() {
        // peek 不会被调用
        users.stream().peek(user -> {
            user.setUserName(user.getUserName() + ":peek");
            System.out.println(user.getUserName());
        });
        users.forEach(System.out::println);
    }
}

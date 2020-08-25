package com.toquery.framework.demo.test.java.streamex;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.github.toquery.framework.common.util.JacksonUtils;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class StreamExTest {

    //配置ObjectMapper对象
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

    }


    private List<User> users = Lists.newArrayList();

    @Before
    public void test0() {

        User user1 = new User();
        user1.setName("zhangsan");
        Role role1 = new Role("admin");
        user1.setRole(role1);
        users.add(user1);

        User user2 = new User();
        user2.setName("李四");
        Role role2 = new Role("edit");
        user2.setRole(role2);
        users.add(user2);
    }

    @Test
    public void test1() {
        List<String> userNames = StreamEx.of(users)
                .map(User::getName)
                .toList();
        System.out.println(JacksonUtils.object2String(userNames));

        Map<Role, List<User>> role2users = StreamEx.of(users).groupingBy(User::getRole);
        System.out.println(JacksonUtils.object2String(role2users));


        List usersAndRoles = Arrays.asList(new User(), new Role());
        List<Role> roles = StreamEx.of(usersAndRoles)
                .select(Role.class)
                .toList();
        System.out.println(JacksonUtils.object2String(roles));


        List<String> appendedUsers = StreamEx.of(users)
                .map(User::getName)
                .prepend("(none)")
                .append("LAST")
                .toList();

        System.out.println(JacksonUtils.object2String(appendedUsers));

        for (String line : StreamEx.of(users).map(User::getName).nonNull()) {
            System.out.println(line);
        }



    }

    @Test
    public void mapOperations() throws JsonProcessingException {
        Map<String, Role> nameToRole = new HashMap<>();
        nameToRole.put("first", new Role());
        nameToRole.put("second", null);
        Set<String> nonNullRoles = StreamEx.ofKeys(nameToRole, Objects::nonNull)
                .toSet();
        System.out.println(JacksonUtils.object2String(nonNullRoles));


        Map<Role, List<User2>> role2users = new HashMap<>();
        User2 root = new User2("root");
        role2users.put(new Role("admin"), Lists.newArrayList(root));
        role2users.put(new Role("edit"), Lists.newArrayList(root, new User2("zhangsan")));


        Map<User2, List<Role>> users2roles = transformMap(role2users);

        //Json对象转为String字符串
        System.out.println(objectMapper.writeValueAsString(users2roles));
        System.out.println(JacksonUtils.object2String(users2roles));

        Map<String, String> mapToString = EntryStream.of(users2roles)
                .mapKeys(String::valueOf)
                .mapValues(String::valueOf)
                .toMap();
        System.out.println(JacksonUtils.object2String(mapToString));



    }


    public Map<User2, List<Role>> transformMap(Map<Role, List<User2>> role2users) {
        Map<User2, List<Role>> users2roles = EntryStream.of(role2users)
                .flatMapValues(List::stream)
                .invert()
                .grouping();
        return users2roles;
    }
}

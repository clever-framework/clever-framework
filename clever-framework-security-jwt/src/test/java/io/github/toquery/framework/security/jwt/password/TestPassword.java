package io.github.toquery.framework.security.jwt.password;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class TestPassword {

    public static final String rawPassword = "123456";

    @Test
    public void test() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(rawPassword);
        log.info(encode); //$2a$10$87hiN5CEs3DfdboT3BkajO6Nq7eJcinDhieMaBJbc4tH6kQrddtxe

        boolean flag = bCryptPasswordEncoder.matches(rawPassword, encode);
        log.info("12 " + flag);

        SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder();

        encode = sCryptPasswordEncoder.encode(rawPassword);
        log.info(encode);

        flag = sCryptPasswordEncoder.matches(rawPassword, encode);
        log.info("12 " + flag);


        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();

        encode = pbkdf2PasswordEncoder.encode(rawPassword);
        log.info(encode);

        flag = pbkdf2PasswordEncoder.matches(rawPassword, encode);
        log.info("12 " + flag);
    }
}

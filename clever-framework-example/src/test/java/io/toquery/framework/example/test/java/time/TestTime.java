package io.toquery.framework.example.test.java.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoField;

@Slf4j
public class TestTime {

    @Test
    public void testTime1(){
        Instant instant = Instant.now();
        log.info("getEpochSecond = {}",instant.getEpochSecond());
        log.info("getNano        = {}",instant.getNano());
        log.info("getLong        = {}",instant.getLong(ChronoField.NANO_OF_SECOND));
        log.info("getLong        = {}",instant.getLong(ChronoField.MICRO_OF_SECOND));
        log.info("getLong        = {}",instant.getLong(ChronoField.MILLI_OF_SECOND));
        log.info("getLong        = {}",instant.getLong(ChronoField.INSTANT_SECONDS));

    }
}

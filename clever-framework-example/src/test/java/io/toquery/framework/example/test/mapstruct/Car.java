package io.toquery.framework.example.test.mapstruct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    /**
     * string -> string
     */
    private String make;

    /**
     * int -> int
     */
    private int numberOfSeats;

    /**
     * enum -> string
     */
    private CarType type;

    /**
     * Long -> String
     */
    private Long long2String;

    /**
     * String -> Long
     */
    private String string2Long;

    /**
     * LocalDate -> string
     */
    private LocalDate localDate;

    /**
     * LocalTime -> string
     */
    private LocalTime localTime;

    /**
     * LocalDateTime -> string
     */
    private LocalDateTime localDateTime;

    /**
     * Date -> string
     */
    private Date date;

}

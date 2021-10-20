package io.toquery.framework.example.test.mapstruct;

import lombok.Data;

/**
 *
 */
@Data
public class CarDto {
    private String make;
    private int seatCount;
    private String type;


    private String long2String;


    private Long string2Long;

    private String localDate;
    private String localTime;
    private String localDateTime;
    private String date;
}

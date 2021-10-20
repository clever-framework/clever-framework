package io.toquery.framework.example.test.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 *
 */
@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mappings(value = {
            @Mapping(source = "numberOfSeats", target = "seatCount"),
            @Mapping(source = "localDate", target = "localDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "localTime", target = "localTime", dateFormat = "HH:mm:ss"),
            @Mapping(source = "localDateTime", target = "localDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    CarDto carToCarDto(Car car);
}

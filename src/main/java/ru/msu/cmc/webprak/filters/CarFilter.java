package ru.msu.cmc.webprak.filters;

import lombok.*;
import java.time.LocalDate;

import ru.msu.cmc.webprak.models.Car;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

public class CarFilter extends CommonFilter {

    public Long id;

    public CarConfiguration configuration;

    public String VIN;

    public Integer cost;

    public String color;

    public Integer mileage;

    public LocalDate lastLtoDate;

    public String interiorColor;

    public String seatUpholstery;

    public Boolean isNew;

    public String additionalProperties;

    public String imageSrc;

    public Boolean isTestDriveAvailable;

    public Integer year;

    public Car.CarStatus status;


}

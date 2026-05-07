package web_prak.filters;

import lombok.*;
import web_prak.models.Car;
import web_prak.models.CarConfiguration;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CarFilter extends CommonFilter {

    public Long id;

    public String VIN;

    public Integer cost;

    public String color;

    public Integer mileage;

    public LocalDate lastLtoDate;

    public String interiorColor;

    public String seatUpholstery;

    public Boolean isNew;


    public String imageSrc;

    public Boolean isTestDriveAvailable;

    public Integer year;

    public Car.CarStatus status;

}
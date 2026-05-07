package web_prak.filters;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import web_prak.models.Model;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarConfigurationFilter  extends CommonFilter {
    public Long id;


    public String name;

    public String engineType;

    public Integer enginePower;

    public java.math.BigDecimal engineVolume;

    public java.math.BigDecimal fuelConsumption;

    public String fuelType;

    public java.math.BigDecimal tankCapacity;

    public Boolean hasCruiseControl;

    public Integer basicCost;

    public Integer seatsNumber;

    public Integer doorsCount;

    public String transmissionType;

    public String driveType;

    public Boolean isBasic = false;

    public Boolean isSalesStopped = false;

}

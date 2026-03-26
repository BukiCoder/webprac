package ru.msu.cmc.webprak.models;

import lombok.*;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "car_configuration")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CarConfiguration implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    @NonNull
    private Model model;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "engine_type", nullable = false)
    @NonNull
    private String engineType;

    @Column(name = "engine_power", nullable = false)
    @NonNull
    private Integer enginePower;

    @Column(name = "engine_volume", nullable = false)
    @NonNull
    private java.math.BigDecimal engineVolume;

    @Column(name = "fuel_consumption", nullable = false)
    @NonNull
    private java.math.BigDecimal fuelConsumption;

    @Column(name = "fuel_type", nullable = false)
    @NonNull
    private String fuelType;

    @Column(name = "tank_capacity", nullable = false)
    @NonNull
    private java.math.BigDecimal tankCapacity;

    @Column(name = "has_cruise_control", nullable = false)
    @NonNull
    private Boolean hasCruiseControl;

    @Column(name = "basic_cost", nullable = false)
    @NonNull
    private Integer basicCost;

    @Column(name = "seats_number", nullable = false)
    @NonNull
    private Integer seatsNumber;

    @Column(name = "doors_count", nullable = false)
    @NonNull
    private Integer doorsCount;

    @Column(name = "transmission_type", nullable = false)
    @NonNull
    private String transmissionType;

    @Column(name = "drive_type", nullable = false)
    @NonNull
    private String driveType;

    @Column(name = "is_basic", nullable = false)
    private Boolean isBasic = false;

    @Column(name = "is_sales_stopped", nullable = false)
    private Boolean isSalesStopped = false;

    @Type(type = "json")
    @Column(name = "additional_properties", columnDefinition = "json")
    private String additionalProperties;
}
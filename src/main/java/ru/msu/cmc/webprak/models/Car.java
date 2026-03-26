package ru.msu.cmc.webprak.models;

import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.Objects;
import com.vladmihalcea.hibernate.type.json.JsonType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "car")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor

@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Car implements CommonEntity<Long> {
    public enum CarStatus {
        on_test_drive,
        in_delivery,
        available,
        unavailable,
        on_lto,
        ordered,
        sold
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "configuration_id", nullable = false)
    @NonNull
    private CarConfiguration configuration;

    @Column(name = "vin", nullable = false, unique = true)
    @NonNull
    private String VIN;

    @Column(name = "cost", nullable = false)
    @NonNull
    private Integer cost;

    @Column(name = "color", nullable = false)
    @NonNull
    private String color;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "last_lto_date")
    private LocalDate lastLtoDate;

    @Column(name = "interior_color", nullable = false)
    @NonNull
    private String interiorColor;

    @Column(name = "seat_upholstery")
    private String seatUpholstery;

    @Column(name = "is_new", nullable = false)
    @NonNull
    private Boolean isNew;

    @Type(type = "json")
    @Column(name = "additional_properties", columnDefinition = "json")
    private String additionalProperties;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "is_test_drive_available", nullable = false)
    private Boolean isTestDriveAvailable = true;

    @Column(name = "year", nullable = false)
    @NonNull
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    @Column(columnDefinition = "car_status") // PostgreSQL enum type name
    private CarStatus status = CarStatus.available;
}
package web_prak.models;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"order\"")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Order implements CommonEntity<Long> {

    public enum OrderStatus {
        completed,
        canceled,
        in_process,
        in_delivery,
        expired
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    @NonNull
    private Car car;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NonNull
    private Client client;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    @NonNull
    private Manager manager;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate = LocalDate.now();

    @Column(name = "test_drive_duration")
    private Integer testDriveDuration;

    @Column(name = "test_drive_date")
    private LocalDateTime testDriveDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.in_process;
}

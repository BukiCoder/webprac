package web_prak.filters;


import lombok.*;
import web_prak.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderFilter extends CommonFilter {

    public Long id;

    public Long clientId;

    public Long carId;

    public Manager manager;

    public LocalDate orderDate;

    public Integer testDriveDuration;

    public LocalDateTime testDriveDate;

    public LocalDate deliveryDate;

    public String deliveryAddress;

    public Order.OrderStatus status;
}
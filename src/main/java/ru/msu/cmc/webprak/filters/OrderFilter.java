package ru.msu.cmc.webprak.filters;


import lombok.*;
import ru.msu.cmc.webprak.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderFilter extends CommonFilter {

    public Long id;

    public Car car;

    public Client client;

    public Manager manager;

    public LocalDate orderDate;

    public Integer testDriveDuration;

    public LocalDateTime testDriveDate;

    public LocalDate deliveryDate;

    public String deliveryAddress;

    public Order.OrderStatus status;
}
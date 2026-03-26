package ru.msu.cmc.webprak.filters;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientFilter extends CommonFilter {

    public Long id;

    public LocalDate registrationDate;

    public String name;

    public String email;

    public String address;

    public String phone;

    public String passwordHash;
}
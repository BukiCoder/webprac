package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "brand")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Brand implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "creation_year", nullable = false)
    @NonNull
    private Integer creationYear;

    @Column(name = "contact_phone", nullable = false)
    @NonNull
    private String contactPhone;

    @Column(name = "contact_email", nullable = false)
    @NonNull
    private String contactEmail;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "country_code", nullable = false, length = 3)
    @NonNull
    private String countryCode;

    @Column(name = "logo_src", nullable = false)
    @NonNull
    private String logoSrc;
}
package ru.msu.cmc.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "model")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Model implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    @NonNull
    private Brand brand;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "year", nullable = false)
    @NonNull
    private Integer year;

    @Column(name = "min_cost", nullable = false)
    @NonNull
    private Integer minCost;

    @Column(name = "image_src")
    private String imageSrc;
}
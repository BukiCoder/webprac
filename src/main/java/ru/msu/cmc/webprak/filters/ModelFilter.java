package ru.msu.cmc.webprak.filters;


import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ModelFilter extends CommonFilter {

    public Long id;

    public String name;

    public Integer year;

    public Integer minCost;

}
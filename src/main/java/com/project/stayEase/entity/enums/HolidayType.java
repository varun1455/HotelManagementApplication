package com.project.stayEase.entity.enums;

import java.util.*;
import java.util.stream.Collectors;

public enum HolidayType {

    NATIONAL(Set.of("National holiday"), 1),
    OPTIONAL(Set.of("Optional holiday"), 2),
    RELIGIOUS(Set.of("Hinduism", "Muslim", "Christian", "Buddhism", "Religious"), 3),
    LOCAL(Set.of("Local holiday"), 4),
    OBSERVANCE(Set.of("Observance", "Season"), 5);

    private final Set<String> apiValues;
    private final int precedence;

    HolidayType(Set<String> apiValues, int precedence) {
        this.apiValues = apiValues;
        this.precedence = precedence;
    }

    private static final Map<Integer, HolidayType> BY_PRECEDENCE =
            Arrays.stream(values()).collect(Collectors.toMap(HolidayType::getPrecedence, e -> e));

    public Integer getPrecedence() {
        return precedence;
    }

    public static HolidayType fromPrecedence(int precedence) {
        return BY_PRECEDENCE.get(precedence);
    }

    public static HolidayType fromApiTypes(List<String> apiTypes){
        int finalOrder = Integer.MAX_VALUE;

       for(HolidayType holidayType: HolidayType.values()){

           for (String apivalue : apiTypes) {

               if(holidayType.apiValues.contains(apivalue)){
                   int order = holidayType.precedence;
                   finalOrder = Math.min(order, finalOrder);

               }

           }

       }

        if( finalOrder == Integer.MAX_VALUE) throw new IllegalArgumentException(
                    "Unknown holiday types: " + apiTypes
        );
        else return fromPrecedence(finalOrder);
    }


}

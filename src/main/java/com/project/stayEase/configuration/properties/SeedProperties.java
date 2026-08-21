package com.project.stayEase.configuration.properties;

import com.project.stayEase.entity.enums.HolidayType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.seed")
public class SeedProperties {
    private boolean enabled;

    private UserCredentials systemAdmin;

    private List<UserCredentials> hotelManagers;

    private List<String> roomTypes;

    private List<String> bedTypes;

    private Map<HolidayType, BigDecimal> holidayPricing;

    @Getter
    @Setter
    public static class UserCredentials {

        private String email;
        private String password;
    }

}

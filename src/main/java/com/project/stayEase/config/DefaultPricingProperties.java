package com.project.stayEase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.seed.pricing.default")
public class DefaultPricingProperties {
    private BigDecimal surgeFactor;
    private Integer urgencyDaysThreshold;
    private BigDecimal urgencyFactor;
    private Integer occupancyThreshold;
    private BigDecimal occupancyFactor;

}

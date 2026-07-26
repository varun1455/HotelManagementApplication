package com.project.stayEase.repository;

import com.project.stayEase.entity.HotelPricingConfiguration;
import com.project.stayEase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelPricingConfigurationRepository extends JpaRepository<HotelPricingConfiguration, Long> {

    Optional<HotelPricingConfiguration> findByOwner(User user);
}

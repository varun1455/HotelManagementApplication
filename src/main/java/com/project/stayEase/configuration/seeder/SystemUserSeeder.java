package com.project.stayEase.configuration.seeder;

import com.project.stayEase.configuration.properties.SeedProperties;
import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.Role;
import com.project.stayEase.repository.UserRepository;
import com.project.stayEase.service.pricing.configuration.HotelPricingConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SystemUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HotelPricingConfigurationService hotelPricingConfigurationService;
    private final SeedProperties seedProperties;

    @Override
    @Transactional
    public void run(String... args) {

        if (!seedProperties.isEnabled()) {
            return;
        }

        seedSystemAdmin();

        for (SeedProperties.UserCredentials manager
                : seedProperties.getHotelManagers()) {

            seedHotelManager(
                    manager.getEmail(),
                    manager.getPassword()
            );
        }
    }

    private void seedSystemAdmin() {

        SeedProperties.UserCredentials admin =
                seedProperties.getSystemAdmin();

        if (userRepository.existsByEmail(admin.getEmail())) {
            return;
        }

        User user = new User();

        user.setEmail(admin.getEmail());
        user.setName("System Admin");
        user.setPassword(
                passwordEncoder.encode(admin.getPassword())
        );
        user.setRoles(Set.of(Role.SYSTEM_ADMIN));

        userRepository.save(user);
    }

    private void seedHotelManager(
            String email,
            String password) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = new User();

        user.setEmail(email);
        user.setName("Hotel Manager");
        user.setPassword(
                passwordEncoder.encode(password)
        );
        user.setRoles(Set.of(Role.HOTEL_MANAGER));

        User savedUser = userRepository.save(user);

        hotelPricingConfigurationService
                .initializeDefaultHotelPriceConfigurationForHotelManager(
                        savedUser
                );
    }
}

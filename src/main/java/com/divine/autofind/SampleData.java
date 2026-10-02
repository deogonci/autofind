package com.divine.autofind;

import com.divine.autofind.model.Provider;
import com.divine.autofind.model.ServiceCategory;
import com.divine.autofind.repository.ProviderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class SampleData {
    @Bean
    CommandLineRunner sampleProviders(ProviderRepository providers) {
        return args -> {
            if (providers.count() != 0) return;
            providers.saveAll(List.of(
                    new Provider("Northline Detail Studio", ServiceCategory.DETAILING,
                            "Lucan", "Dublin", "A proper reset for your car.",
                            "Interior refreshes, exterior washes and full-day detailing packages for everyday cars.",
                            new BigDecimal("65")),
                    new Provider("Westside Tyre Works", ServiceCategory.TYRES,
                            "Clondalkin", "Dublin", "Back on the road without the runaround.",
                            "Tyre replacements, puncture repairs and wheel checks by appointment.",
                            new BigDecimal("35")),
                    new Provider("RoadReady Mobile", ServiceCategory.JUMP_START,
                            "Tallaght", "Dublin", "Help when the car will not start.",
                            "Mobile jump-start requests and battery checks around west and south Dublin.",
                            new BigDecimal("45")),
                    new Provider("Elm Street Garage", ServiceCategory.MECHANIC,
                            "Maynooth", "Kildare", "Clear answers for everyday repairs.",
                            "Diagnostics and routine servicing with an estimate before work begins.",
                            new BigDecimal("75")),
                    new Provider("Clearcoat Wash Club", ServiceCategory.CAR_WASH,
                            "Celbridge", "Kildare", "Quick clean, good finish.",
                            "Hand washing and interior cleaning with flexible weekend appointments.",
                            new BigDecimal("20"))
            ));
        };
    }
}

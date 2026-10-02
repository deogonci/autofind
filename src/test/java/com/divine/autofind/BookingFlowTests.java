package com.divine.autofind;

import com.divine.autofind.model.Provider;
import com.divine.autofind.repository.BookingRepository;
import com.divine.autofind.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:autofind_test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class BookingFlowTests {
    @Autowired MockMvc mvc;
    @Autowired ProviderRepository providers;
    @Autowired BookingRepository bookings;

    @Test
    void visitorsCanFilterAndSaveRequestButPastDatesAreRejected() throws Exception {
        mvc.perform(get("/services").param("category", "DETAILING").param("area", "Lucan"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Northline Detail Studio")));

        Provider provider = providers.findAll().get(0);
        long before = bookings.count();
        String requestUrl = "/services/" + provider.getId() + "/request";

        mvc.perform(post(requestUrl).with(csrf())
                .param("name", "Demo Customer")
                .param("email", "demo@example.com")
                .param("preferredDate", LocalDate.now().minusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Choose today or a future date")));
        assertEquals(before, bookings.count());

        String redirect = mvc.perform(post(requestUrl).with(csrf())
                .param("name", "Demo Customer")
                .param("email", "demo@example.com")
                .param("preferredDate", LocalDate.now().plusDays(2).toString())
                .param("notes", "Tyre check please"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getRedirectedUrl();
        assertEquals(before + 1, bookings.count());
        mvc.perform(get(redirect))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(provider.getName())))
                .andExpect(content().string(containsString("No real provider has been contacted")));
    }
}

package com.divine.autofind;

import com.divine.autofind.model.Booking;
import com.divine.autofind.model.ProviderAccount;
import com.divine.autofind.repository.BookingRepository;
import com.divine.autofind.repository.ProviderAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:autofind_portal_test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ProviderPortalTests {
    @Autowired MockMvc mvc;
    @Autowired ProviderAccountRepository accounts;
    @Autowired BookingRepository bookings;

    @Test
    void providersCanManageOnlyTheirOwnEnquiries() throws Exception {
        mvc.perform(get("/provider/dashboard")).andExpect(status().is3xxRedirection());

        String firstEmail = "first-" + UUID.randomUUID() + "@example.com";
        String secondEmail = "second-" + UUID.randomUUID() + "@example.com";
        register("First Garage", firstEmail);
        register("Second Garage", secondEmail);
        ProviderAccount first = accounts.findByEmailIgnoreCase(firstEmail).orElseThrow();
        assertFalse(first.getPasswordHash().equals("secret123"));
        assertTrue(first.getPasswordHash().startsWith("$2"));

        Long providerId = first.getProvider().getId();
        mvc.perform(post("/services/" + providerId + "/request").with(csrf())
                .param("name", "Customer One")
                .param("email", "customer@example.com")
                .param("preferredDate", LocalDate.now().plusDays(3).toString()))
                .andExpect(status().is3xxRedirection());
        Booking enquiry = bookings.findByProvider_IdOrderByCreatedAtDesc(providerId).get(0);

        MockHttpSession firstSession = login(firstEmail);
        mvc.perform(get("/provider/dashboard").session(firstSession))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Customer One")));

        MockHttpSession secondSession = login(secondEmail);
        mvc.perform(post("/provider/bookings/" + enquiry.getId() + "/status")
                        .session(secondSession).with(csrf()).param("status", "ACCEPTED"))
                .andExpect(status().isNotFound());
        assertEquals("REQUESTED", bookings.findById(enquiry.getId()).orElseThrow().getStatus().name());

        mvc.perform(post("/provider/bookings/" + enquiry.getId() + "/status")
                        .session(firstSession).with(csrf()).param("status", "ACCEPTED"))
                .andExpect(status().is3xxRedirection());
        assertEquals("ACCEPTED", bookings.findById(enquiry.getId()).orElseThrow().getStatus().name());
    }

    private void register(String businessName, String email) throws Exception {
        mvc.perform(post("/providers/register").with(csrf())
                .param("email", email)
                .param("password", "secret123")
                .param("confirmPassword", "secret123")
                .param("name", businessName)
                .param("category", "MECHANIC")
                .param("town", "Lucan")
                .param("county", "Dublin")
                .param("tagline", "Local car care")
                .param("description", "Routine maintenance and diagnostics")
                .param("priceFrom", "50"))
                .andExpect(status().is3xxRedirection());
    }

    private MockHttpSession login(String email) throws Exception {
        var result = mvc.perform(post("/login").with(csrf())
                        .param("username", email).param("password", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }
}

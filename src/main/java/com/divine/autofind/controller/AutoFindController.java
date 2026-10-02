package com.divine.autofind.controller;

import com.divine.autofind.model.Booking;
import com.divine.autofind.model.ServiceCategory;
import com.divine.autofind.service.BookingService;
import com.divine.autofind.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class AutoFindController {
    private final ProviderService providers;
    private final BookingService bookings;

    public AutoFindController(ProviderService providers, BookingService bookings) {
        this.providers = providers;
        this.bookings = bookings;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", ServiceCategory.values());
        model.addAttribute("featured", providers.search(null, "").stream().limit(3).toList());
        return "home";
    }

    @GetMapping("/services")
    public String services(@RequestParam(required = false) ServiceCategory category,
                           @RequestParam(defaultValue = "") String area, Model model) {
        model.addAttribute("categories", ServiceCategory.values());
        model.addAttribute("category", category);
        model.addAttribute("area", area);
        model.addAttribute("results", providers.search(category, area));
        return "services";
    }

    @GetMapping("/services/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("provider", providers.get(id));
        return "detail";
    }

    @GetMapping("/services/{id}/request")
    public String requestForm(@PathVariable Long id, Model model) {
        model.addAttribute("provider", providers.get(id));
        model.addAttribute("request", new BookingRequestForm());
        model.addAttribute("today", LocalDate.now());
        return "request";
    }

    @PostMapping("/services/{id}/request")
    public String submitRequest(@PathVariable Long id,
                                @Valid @ModelAttribute("request") BookingRequestForm form,
                                BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("provider", providers.get(id));
            model.addAttribute("today", LocalDate.now());
            return "request";
        }
        Booking booking = bookings.request(id, form.getName(), form.getEmail(),
                form.getPreferredDate(), form.getNotes());
        return "redirect:/requests/" + booking.getReference();
    }

    @GetMapping("/requests/{reference}")
    public String confirmation(@PathVariable String reference, Model model) {
        model.addAttribute("booking", bookings.getByReference(reference));
        return "confirmation";
    }
}

package com.divine.autofind.controller;

import com.divine.autofind.model.BookingStatus;
import com.divine.autofind.model.ServiceCategory;
import com.divine.autofind.service.BookingService;
import com.divine.autofind.service.ProviderAccountService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ProviderPortalController {
    private final ProviderAccountService accounts;
    private final BookingService bookings;

    public ProviderPortalController(ProviderAccountService accounts, BookingService bookings) {
        this.accounts = accounts;
        this.bookings = bookings;
    }

    @GetMapping("/providers/register")
    public String register(Model model) {
        model.addAttribute("registration", new ProviderRegistrationForm());
        model.addAttribute("categories", ServiceCategory.values());
        return "register";
    }

    @PostMapping("/providers/register")
    public String saveRegistration(@Valid @ModelAttribute("registration") ProviderRegistrationForm form,
                                   BindingResult errors, Model model) {
        if (form.getPassword() != null && !form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (accounts.emailExists(form.getEmail())) {
            errors.rejectValue("email", "duplicate", "That email is already registered");
        }
        if (errors.hasErrors()) {
            model.addAttribute("categories", ServiceCategory.values());
            return "register";
        }
        accounts.register(form);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/provider/dashboard")
    public String dashboard(Principal principal, Model model) {
        var account = accounts.getByEmail(principal.getName());
        model.addAttribute("provider", account.getProvider());
        model.addAttribute("bookings", bookings.forProvider(account.getProvider().getId()));
        return "dashboard";
    }

    @PostMapping("/provider/bookings/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam BookingStatus status,
                               Principal principal) {
        var account = accounts.getByEmail(principal.getName());
        bookings.updateStatus(id, account.getProvider().getId(), status);
        return "redirect:/provider/dashboard";
    }
}

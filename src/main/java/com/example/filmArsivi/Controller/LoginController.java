package com.example.filmArsivi.Controller;

import com.example.filmArsivi.Entity.User;
import com.example.filmArsivi.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public LoginController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {

        // 1. Şifre eşleşme kontrolü
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Hata: Şifreler birbiriyle eşleşmiyor!");
            return "register";
        }

        // 2. Kullanıcı adı kontrolü
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Hata: Bu kullanıcı adı zaten alınmış!");
            return "register";
        }

        // 3. Kayıt işlemi
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        userRepo.save(user);

        return "redirect:/login?success";
    }
}
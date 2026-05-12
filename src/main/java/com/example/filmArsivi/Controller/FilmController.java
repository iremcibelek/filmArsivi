package com.example.filmArsivi.Controller;

import com.example.filmArsivi.Entity.Film;
import com.example.filmArsivi.Entity.Rating;
import com.example.filmArsivi.Entity.User;
import com.example.filmArsivi.Service.FilmService;
import com.example.filmArsivi.Service.RatingService;
import com.example.filmArsivi.Service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Controller
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;
    private final RatingService ratingService;

    public FilmController(FilmService filmService, UserService userService, RatingService ratingService) {
        this.filmService = filmService;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @GetMapping("/")
    public String anaSayfa(@RequestParam(value = "kategori", required = false) String kategori,
                           @RequestParam(value = "search", required = false) String search, Model model) {
        List<Film> filmler;
        if (search != null && !search.isEmpty()) {
            filmler = filmService.searchFilms(search);
        } else if (kategori != null && !kategori.isEmpty()) {
            filmler = filmService.findByGenre(kategori);
        } else {
            filmler = filmService.findAll();
        }
        model.addAttribute("filmler", filmler);
        return "index";
    }

    @GetMapping("/film/{id}")
    public String filmDetay(@PathVariable Long id, Model model, Authentication auth) {
        Film film = filmService.findById(id);
        if (film == null) return "redirect:/";
        model.addAttribute("film", film);
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName());
            Optional<Rating> userRating = ratingService.findUserRating(user.getId(), id);
            model.addAttribute("hasRated", userRating.isPresent());
            model.addAttribute("userRatingValue", userRating.map(Rating::getValue).orElse(0));
        }
        return "filmDetay";
    }

    @GetMapping("/ekle")
    public String filmEklemeSayfasi(Model model) {
        model.addAttribute("film", new Film());
        return "filmEkle";
    }

    // ADMİN İÇİN DÜZENLEME SAYFASI
    @GetMapping("/film/duzenle/{id}")
    public String filmDuzenleSayfasi(@PathVariable Long id, Model model) {
        Film film = filmService.findById(id);
        model.addAttribute("film", film);
        return "filmEkle"; // Aynı formu kullanıyoruz
    }

    @PostMapping("/kaydet")
    public String filmKaydet(@ModelAttribute("film") Film film, @RequestParam(value = "resimDosyasi", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                film.setImage(file.getBytes());
            } else if (film.getId() != null) {
                // Düzenleme yapılıyorsa ve yeni resim seçilmediyse eski resmi koru
                Film eskiFilm = filmService.findById(film.getId());
                film.setImage(eskiFilm.getImage());
            }
            filmService.save(film);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/ekle?error";
        }
    }

    @GetMapping("/film/sil/{id}")
    public String filmSil(@PathVariable Long id) {
        filmService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/film/resim/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getResim(@PathVariable Long id) {
        Film film = filmService.findById(id);
        if (film != null && film.getImage() != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(film.getImage());
        }
        return ResponseEntity.notFound().build();
    }
}
package com.example.filmArsivi.Controller;

import com.example.filmArsivi.Entity.Comment;
import com.example.filmArsivi.Entity.Film;
import com.example.filmArsivi.Entity.Rating;
import com.example.filmArsivi.Entity.User;
import com.example.filmArsivi.Service.CommentService;
import com.example.filmArsivi.Service.FilmService;
import com.example.filmArsivi.Service.RatingService;
import com.example.filmArsivi.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final FilmService filmService;
    private final UserService userService;
    private final RatingService ratingService;

    public CommentController(CommentService commentService, FilmService filmService, UserService userService, RatingService ratingService) {
        this.commentService = commentService;
        this.filmService = filmService;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @PostMapping("/film/{id}/islem")
    public String filmIslemi(@PathVariable Long id,
                             @RequestParam(required = false) String content,
                             @RequestParam(required = false) Integer ratingValue,
                             Authentication auth, RedirectAttributes redirectAttributes) {
        if (auth == null) return "redirect:/login";
        Film film = filmService.findById(id);
        User user = userService.findByUsername(auth.getName());

        if (content != null && !content.trim().isEmpty()) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setFilm(film);
            comment.setUser(user);
            commentService.save(comment);
        }
        if (ratingValue != null) {
            Rating rating = ratingService.findUserRating(user.getId(), film.getId()).orElse(new Rating());
            rating.setValue(ratingValue);
            rating.setFilm(film);
            rating.setUser(user);
            ratingService.save(rating);
            redirectAttributes.addFlashAttribute("ratingMessage", "Puanınız güncellendi!");
        }
        return "redirect:/film/" + id;
    }

    @GetMapping("/yorum/sil/{commentId}")
    public String yorumSil(@PathVariable Long commentId, Authentication auth) {
        Comment comment = commentService.findById(commentId);
        if (comment != null && auth != null) {
            boolean isOwner = comment.getUser().getUsername().equals(auth.getName());
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isOwner || isAdmin) {
                Long filmId = comment.getFilm().getId();
                commentService.deleteById(commentId);
                return "redirect:/film/" + filmId;
            }
        }
        return "redirect:/";
    }
}
package com.example.filmArsivi.Service;

import com.example.filmArsivi.Entity.Rating;
import com.example.filmArsivi.Repository.RatingRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    // Controller'daki hatayı bu metot çözer:
    public boolean hasUserRated(Long userId, Long filmId) {
        return ratingRepository.findByUserIdAndFilmId(userId, filmId).isPresent();
    }

    // Puan güncelleme için lazım:
    public Optional<Rating> findUserRating(Long userId, Long filmId) {
        return ratingRepository.findByUserIdAndFilmId(userId, filmId);
    }
}
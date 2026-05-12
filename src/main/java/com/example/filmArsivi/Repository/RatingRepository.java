package com.example.filmArsivi.Repository;

import com.example.filmArsivi.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Kullanıcı ID ve Film ID ile daha önce puan verilmiş mi kontrolü
    Optional<Rating> findByUserIdAndFilmId(Long userId, Long filmId);
}
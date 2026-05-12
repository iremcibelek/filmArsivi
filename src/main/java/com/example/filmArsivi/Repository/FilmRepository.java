package com.example.filmArsivi.Repository;

import com.example.filmArsivi.Entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findByGenre(String genre);
    List<Film> findByTitleContainingIgnoreCase(String title);
}
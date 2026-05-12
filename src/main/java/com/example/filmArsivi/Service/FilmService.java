package com.example.filmArsivi.Service;

import com.example.filmArsivi.Entity.Film;
import com.example.filmArsivi.Repository.FilmRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public List<Film> findByGenre(String genre) {
        return filmRepository.findByGenre(genre);
    }

    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    public void save(Film film) {
        filmRepository.save(film);
    }

    public void deleteById(Long id) {
        filmRepository.deleteById(id);
    }

    public List<Film> searchFilms(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }
}
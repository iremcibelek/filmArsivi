package com.example.filmArsivi.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "films")
@Data
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String director;
    private String genre;
    private Integer year;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // fetch = FetchType.EAGER: Filmi çektiğin an puanları da getirir, 0,0 hatasını çözer.
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Rating r : ratings) {
            sum += r.getValue();
        }
        // Sonucu 10.0 ile çarpıp yuvarlayıp 10.0'a bölmek "4.6666" yerine "4.7" yapmanı sağlar.
        return Math.round((sum / ratings.size()) * 10.0) / 10.0;
    }
}
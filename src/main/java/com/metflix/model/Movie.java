package com.metflix.model;


import com.metflix.model.Enums.MovieTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movies")

public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="title")
    private String title;
    @Column(name="movie_type")
    @Enumerated(EnumType.STRING)
    private MovieTypeEnum type;
    private String description;
    private Integer length;
    @Column (name="release_year")
    private LocalDate releaseYear;
    @Column(name="trailer_link")
    private String trailerLink;
    @Column(name="movie_link")
    private String movieLink;
    @Column(name="img_preview")
    private String imgPreview;
    @Column(name="img_main")
    private String imgMain;

    public Movie(Movie movie) {
        this.title = movie.getTitle();
        this.type = movie.getType();
        this.description = movie.getDescription();
        this.length = movie.getLength();
        this.releaseYear = movie.getReleaseYear();
        this.trailerLink = movie.getTrailerLink();
        this.movieLink = movie.getMovieLink();
        this.imgPreview = movie.getImgPreview();
        this.imgMain = movie.getImgMain();
    }


    public Movie(String title, MovieTypeEnum type, String description, Integer length, LocalDate releaseYear, String trailerLink, String movieLink, String imgPreview, String imgMain) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.length = length;
        this.releaseYear = releaseYear;
        this.trailerLink = trailerLink;
        this.movieLink = movieLink;
        this.imgPreview = imgPreview;
        this.imgMain = imgMain;
    }
}

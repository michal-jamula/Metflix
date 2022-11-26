package com.metflix.service;


import com.metflix.model.Movie;
import com.metflix.repositories.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }



    public Page<Movie> findPaginated(final int pageNumber, final int pageSize,
                                     final String sortField, final String sortDirection) {


        Sort sort;
        if( sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }


        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return movieRepository.findAll(pageable);
    }



    //TODO: Currently the system accepts spaces as valid, create better validation for received info
    public Movie updateMovieWithId(Movie movie, Integer movieId) {

        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        if(movieOptional.isEmpty()) {
            System.err.println("Tried to find movie with ID, but no movie with id: " + movieId + " exists!");
            return null;
        }

        Movie movieDb = movieOptional.get();

        if (!movie.getTitle().equals(null)) {
            movieDb.setTitle(movie.getTitle());
        }

        if (!movie.getType().equals(null)) {
            movieDb.setType(movie.getType());
        }

        if (!movie.getDescription().trim().isBlank()) {
            movieDb.setDescription(movie.getDescription());
        }

        if (!movie.getLength().equals(null)) {
            movieDb.setLength(movie.getLength());
        }

        if (!movie.getReleaseYear().equals(null)) {
            movieDb.setReleaseYear(movie.getReleaseYear());
        }

        if (!movie.getTrailerLink().equals(null)) {
            movieDb.setTrailerLink(movie.getTrailerLink());
        }

        if (movie.getMovieLink().equals(null)) {
            movieDb.setMovieLink(movie.getMovieLink());
        }

        if (movie.getImgPreview().equals(null)) {
            movieDb.setImgPreview(movie.getImgPreview());
        }

        if (movie.getImgMain().equals(null)) {
            movieDb.setImgMain(movie.getImgMain());
        }


        return movieRepository.save(movieDb);
    }
}

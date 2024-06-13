package com.metflix.service;


import com.metflix.model.Enums.MovieTypeEnum;
import com.metflix.model.Movie;
import com.metflix.repositories.MovieRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public List<String> findDistinctByType() {
        return movieRepository.findDistinctByType();
    }

    public List<Movie> findMoviesByType(MovieTypeEnum type) {
        return movieRepository.findMoviesByType(type);
    }

    public Optional<Movie> findById(int id) {
        return movieRepository.findById(id);
    }

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
    public Movie updateMovie(Movie movie) throws Exception{

        try {
            int movieId = movie.getId();
        } catch (Exception e) {
            throw new Exception("Tried to update a movie which doesn't have any ID");
        }

        Optional<Movie> movieOptional = movieRepository.findById(movie.getId());

        if(movieOptional.isEmpty()) {
            throw new Exception(String.format("Tried to update a movie with id {%s} but couldn't find movie", movie.getId()));
        }

        Movie movieDb = movieOptional.get();

        if (StringUtils.isNotBlank(movie.getTitle())) {
            movieDb.setTitle(movie.getTitle());
        }

        if (movie.getType() != null) {
            movieDb.setType(movie.getType());
        }

        if (StringUtils.isNotBlank(movie.getDescription())) {
            movieDb.setDescription(movie.getDescription());
        }

        if (movie.getLength() != null) {
            movieDb.setLength(movie.getLength());
        }

        if (movie.getReleaseYear() != null) {
            movieDb.setReleaseYear(movie.getReleaseYear());
        }

        if (StringUtils.isNotBlank(movie.getTrailerLink())) {
            movieDb.setTrailerLink(movie.getTrailerLink());
        }

        if (StringUtils.isNotBlank(movie.getMovieLink())) {
            movieDb.setMovieLink(movie.getMovieLink());
        }

        if (StringUtils.isNotBlank(movie.getImgPreview())) {
            movieDb.setImgPreview(movie.getImgPreview());
        }

        if (StringUtils.isNotBlank(movie.getImgMain())) {
            movieDb.setImgMain(movie.getImgMain());
        }

        return movieRepository.save(movieDb);
    }
}

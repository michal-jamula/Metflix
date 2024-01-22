package com.metflix.repositories;

import com.metflix.model.Movie;
import com.metflix.model.Enums.MovieTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findMoviesByType(MovieTypeEnum movieTypeEnum);

    Optional<Movie> findById(Integer id);

    @Query(value="SELECT DISTINCT MOVIE_TYPE FROM MOVIES", nativeQuery = true)
    List<String> findDistinctByType();

}
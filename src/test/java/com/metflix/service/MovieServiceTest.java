package com.metflix.service;

import com.metflix.exceptions.UserNotFoundException;
import com.metflix.model.Enums.MovieTypeEnum;
import com.metflix.model.Movie;
import com.metflix.model.User;
import com.metflix.repositories.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Tags({@Tag("service"), @Tag("test")})
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    MovieRepository movieRepository;
    @InjectMocks
    MovieService movieService;

    Movie testMovie = new Movie("testTitle", MovieTypeEnum.FANTASY, "testDescription",
            123, LocalDate.now(), "testTrailerLink", "testMovieLink",
            "testImgPreview", "testImgMain");

    ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);


    @Test
    @DisplayName("Find Paginated Movies - normal use case")
    void findPaginated() {
        //given
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        //when
        movieService.findPaginated(1, 2, "title", "asc");
        movieService.findPaginated(1, 2, "releaseYear", "desc");

        //then
        verify(movieRepository, times(2)).findAll(captor.capture());
        assertThat(captor.getAllValues().get(0).getSort()).isEqualTo(Sort.by("title").ascending());
        assertThat(captor.getAllValues().get(1).getSort()).isEqualTo(Sort.by("releaseYear").descending());
    }

    @Test
    @DisplayName("Update movie with normal values")
    void updateMovieWithNormalValues() {
        //given
        Movie expectedMovie = new Movie(testMovie);
        expectedMovie.setId(1);
        Movie currentMovie = new Movie();
        currentMovie.setId(1);

        given(movieRepository.findById(any())).willReturn(Optional.of(currentMovie));

        //when
        try {
            movieService.updateMovie(expectedMovie);
        } catch (Exception e) {
            System.out.println("Exception was thrown during test");
        }

        verify(movieRepository).save(movieCaptor.capture());
        Movie updatedMovie = movieCaptor.getValue();

        //then
        assertThat(updatedMovie).isEqualTo(expectedMovie);

        verify(movieRepository, times(1)).findById(1);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verifyNoMoreInteractions(movieRepository);
    }


    @Test
    @DisplayName("Update movie with null values")
    void updateMovieWithExceptionalValues() {
        //given
        Movie expectedMovie = new Movie(testMovie);
        expectedMovie.setId(1);
        Movie currentMovie = new Movie();
        currentMovie.setId(1);

        given(movieRepository.findById(any())).willReturn(Optional.of(expectedMovie));

        //when
        try {
            movieService.updateMovie(expectedMovie);
        } catch (Exception e) {
            System.out.println("Exception was thrown during test");
        }

        verify(movieRepository).save(movieCaptor.capture());
        Movie updatedMovie = movieCaptor.getValue();

        //then
        assertThat(updatedMovie).isEqualTo(expectedMovie);

        verify(movieRepository, times(1)).findById(1);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("Update Movie - should throw exception")
    void updateMovieShouldThrowException() {
        //given
        given(movieRepository.findById(anyInt())).willReturn(Optional.empty());
        Movie movie = new Movie();
        movie.setId(1);

        Movie movieWithNullId = new Movie();

        //when
        assertThrows(Exception.class,
                () -> movieService.updateMovie(movie));
        assertThrows(Exception.class,
                () -> movieService.updateMovie(movieWithNullId));

        verify(movieRepository, times(1)).findById(anyInt());
        verifyNoMoreInteractions(movieRepository);
        verify(movieRepository, times(0)).save(any());
    }
}
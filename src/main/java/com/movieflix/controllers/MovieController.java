package com.movieflix.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.exceptions.EmptyFileException;
import com.movieflix.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")  //only user with ADMIN role can add new Movies
    @PostMapping("/addMovie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String movieDto) throws EmptyFileException {

        if (file.isEmpty())
            throw new EmptyFileException("File is empty! Please send another file!");

        try {
            MovieDto dto = convertToMovieDto(movieDto);
            return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MovieDto());
        }
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String movieDtoObj) throws IOException {
        if (file.isEmpty())  file = null;

        MovieDto movieDto = convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto, file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "3", required = false) Integer pageSize) {
            return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "3", required = false) Integer pageSize,
            @RequestParam(defaultValue = "movieId", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String dir) {
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, dir));
    }

    //deserialization
    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }
}

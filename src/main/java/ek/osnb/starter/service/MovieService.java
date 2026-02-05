package ek.osnb.starter.service;

import ek.osnb.starter.dto.ActorResponse;
import ek.osnb.starter.dto.CreateMovieRequest;
import ek.osnb.starter.dto.MovieDetailsResponse;
import ek.osnb.starter.dto.MovieResponse;
import ek.osnb.starter.exceptions.NotFoundException;
import ek.osnb.starter.model.Actor;
import ek.osnb.starter.model.Movie;
import ek.osnb.starter.model.MovieDetails;
import ek.osnb.starter.model.Rating;
import ek.osnb.starter.repository.ActorRepository;
import ek.osnb.starter.repository.MovieDetailsRepository;
import ek.osnb.starter.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieDetailsRepository movieDetailsRepository;

    public MovieService(MovieRepository movieRepository,
                        ActorRepository actorRepository,
                        MovieDetailsRepository movieDetailsRepository)
    {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieDetailsRepository = movieDetailsRepository;
    }

    public MovieResponse createMovie(CreateMovieRequest request)
    {
        Movie movie = toMovieEntity(request);
        Movie saved = movieRepository.save(movie);
        return toMovieResponse(saved);
    }

    public List<MovieResponse> getAllMovies()
    {
        var movies =movieRepository.findAll();
        List<MovieResponse> responses = new ArrayList<>();

        for (Movie m : movies)
        {
            responses.add(toMovieResponse(m));
        }
        return responses;
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        return toMovieResponse(movie);
    }


    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Movie addActorToMovie(Long movieId, Long actorId)
    {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new NotFoundException("Actor not found"));

        movie.getActors().add(actor);

        return movieRepository.save(movie);
    }

    public Movie addDetailsToMovie(Long movieId, MovieDetails details)
    {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        movie.setMovieDetails(details);
        details.setMovie(movie);

        return movieRepository.save(movie);
    }

    private MovieResponse toMovieResponse(Movie movie)
    {
        MovieDetailsResponse details = null;
        if (movie.getMovieDetails() != null)
        {
            details = new MovieDetailsResponse(
                    movie.getMovieDetails().getPlot(),
                    movie.getMovieDetails().getBudget(),
                    movie.getMovieDetails().getRuntime(),
                    movie.getMovieDetails().getProductionCompany()
            );
        }

        List<ActorResponse> actors = movie.getActors().stream()
                .map(a -> new ActorResponse(
                        a.getId(),
                        a.getName(),
                        a.getBirthYear()
                ))
                .toList();

        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getGenre(),
                movie.getRating() != null ? movie.getRating().getScore() : null,
                movie.getRating() != null ? movie.getRating().getVoteCount() : null,
                details,
                actors
        );
    }

    private Movie toMovieEntity(CreateMovieRequest request)
    {
        Rating rating = null;
        if (request.ratingScore() != null && request.ratingVoteCount() != null)
        {
            rating = new Rating(request.ratingScore(), request.ratingVoteCount());
        }

        return new Movie(
                request.title(),
                request.releaseYear(),
                request.genre(),
                rating
        );
    }
}
package ek.osnb.starter.config;

import ek.osnb.starter.model.Actor;
import ek.osnb.starter.model.Movie;
import ek.osnb.starter.repository.ActorRepository;
import ek.osnb.starter.repository.MovieRepository;
import ek.osnb.starter.service.ActorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class InitializeData implements CommandLineRunner {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    public InitializeData(ActorRepository actorRepository, MovieRepository movieRepository, ActorService actorService) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
    }
}

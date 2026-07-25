package com.directmefilm.config;

import com.directmefilm.model.Director;
import com.directmefilm.model.Movie;
import com.directmefilm.model.Question;
import com.directmefilm.model.TraitAxis;
import com.directmefilm.repository.DirectorRepository;
import com.directmefilm.repository.MovieRepository;
import com.directmefilm.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedCatalog(
            DirectorRepository directors,
            MovieRepository movies,
            QuestionRepository questions
    ) {
        return args -> {
            seedDirectorsAndMovies(directors, movies);
            if (questions.count() == 0) {
                seedQuestions(questions);
            }
        };
    }

    private void seedDirectorsAndMovies(
            DirectorRepository directors,
            MovieRepository movies
    ) {
        seedDirector(directors, movies, new Director(
                "bong-joon-ho",
                "Bong Joon-ho",
                "A genre alchemist who mixes social observation, suspense, humour, and sudden tonal turns.",
                "Social thrillers with sharp tonal contrasts",
                1, 1, 1, 2, 2
        ), new SeedMovie("Parasite", 2019),
                new SeedMovie("Memories of Murder", 2003),
                new SeedMovie("Snowpiercer", 2013));

        seedDirector(directors, movies, new Director(
                "christopher-nolan",
                "Christopher Nolan",
                "A large-scale puzzle maker drawn to time, identity, moral pressure, and immersive spectacle.",
                "Cerebral blockbusters built around time and structure",
                -1, 2, 2, 1, 1
        ), new SeedMovie("Inception", 2010),
                new SeedMovie("The Dark Knight", 2008),
                new SeedMovie("Oppenheimer", 2023));

        seedDirector(directors, movies, new Director(
                "denis-villeneuve",
                "Denis Villeneuve",
                "A meticulous visual storyteller who pairs atmosphere and scale with patient, morally complex drama.",
                "Immersive, atmospheric science fiction and thrillers",
                0, 2, 1, 2, 0
        ), new SeedMovie("Arrival", 2016),
                new SeedMovie("Blade Runner 2049", 2017),
                new SeedMovie("Dune: Part Two", 2024));

        seedDirector(directors, movies, new Director(
                "greta-gerwig",
                "Greta Gerwig",
                "A warm, witty filmmaker interested in identity, ambition, relationships, and the messiness of growing up.",
                "Character-first stories with wit and emotional candour",
                2, 0, 0, -2, 0
        ), new SeedMovie("Lady Bird", 2017),
                new SeedMovie("Little Women", 2019),
                new SeedMovie("Barbie", 2023));

        seedDirector(directors, movies, new Director(
                "sofia-coppola",
                "Sofia Coppola",
                "A quietly expressive director focused on interior lives, isolation, intimacy, and beautifully observed moods.",
                "Dreamlike character studies rich in mood and restraint",
                1, 2, -1, -1, 1
        ), new SeedMovie("Lost in Translation", 2003),
                new SeedMovie("The Virgin Suicides", 1999),
                new SeedMovie("Priscilla", 2023));

        seedDirector(directors, movies, new Director(
                "martin-scorsese",
                "Martin Scorsese",
                "A kinetic chronicler of guilt, ambition, violence, and the seductions of power.",
                "Propulsive character studies of obsession and moral consequence",
                1, 1, 1, 2, 1
        ), new SeedMovie("Taxi Driver", 1976),
                new SeedMovie("Raging Bull", 1980),
                new SeedMovie("Goodfellas", 1990),
                new SeedMovie("The Departed", 2006));

        seedDirector(directors, movies, new Director(
                "steven-spielberg",
                "Steven Spielberg",
                "A master of accessible spectacle who combines visual clarity, wonder, suspense, and human feeling.",
                "Emotionally direct adventures and historical epics",
                2, 1, 0, 0, -1
        ), new SeedMovie("Jaws", 1975),
                new SeedMovie("Raiders of the Lost Ark", 1981),
                new SeedMovie("E.T. the Extra-Terrestrial", 1982),
                new SeedMovie("Schindler's List", 1993));

        seedDirector(directors, movies, new Director(
                "quentin-tarantino",
                "Quentin Tarantino",
                "A maximalist storyteller known for charged dialogue, nonlinear structures, pop culture, and stylized violence.",
                "Genre remixing with swagger, suspense, and verbal fireworks",
                0, 2, 1, 2, 2
        ), new SeedMovie("Pulp Fiction", 1994),
                new SeedMovie("Inglourious Basterds", 2009),
                new SeedMovie("Once Upon a Time in Hollywood", 2019));

        seedDirector(directors, movies, new Director(
                "stanley-kubrick",
                "Stanley Kubrick",
                "A formally rigorous perfectionist who used genre to examine power, technology, violence, and human absurdity.",
                "Icy precision, monumental images, and provocative ideas",
                -2, 2, 2, 2, 2
        ), new SeedMovie("Dr. Strangelove", 1964),
                new SeedMovie("2001: A Space Odyssey", 1968),
                new SeedMovie("Barry Lyndon", 1975),
                new SeedMovie("The Shining", 1980));

        seedDirector(directors, movies, new Director(
                "alfred-hitchcock",
                "Alfred Hitchcock",
                "The defining architect of screen suspense, using precise images and dark wit to make audiences complicit.",
                "Elegant psychological thrillers engineered for tension",
                -1, 2, 1, 2, 1
        ), new SeedMovie("Rear Window", 1954),
                new SeedMovie("Vertigo", 1958),
                new SeedMovie("North by Northwest", 1959),
                new SeedMovie("Psycho", 1960));

        seedDirector(directors, movies, new Director(
                "akira-kurosawa",
                "Akira Kurosawa",
                "A dynamic humanist whose moral dramas and action filmmaking reshaped global cinema.",
                "Epic movement, ethical conflict, and deeply human heroes",
                2, 1, 0, 1, 0
        ), new SeedMovie("Rashomon", 1950),
                new SeedMovie("Ikiru", 1952),
                new SeedMovie("Seven Samurai", 1954));

        seedDirector(directors, movies, new Director(
                "hayao-miyazaki",
                "Hayao Miyazaki",
                "An animation visionary who builds wondrous worlds around empathy, nature, courage, and moral complexity.",
                "Hand-crafted fantasy adventures with ecological and emotional depth",
                2, 2, 0, -1, 1
        ), new SeedMovie("My Neighbor Totoro", 1988),
                new SeedMovie("Princess Mononoke", 1997),
                new SeedMovie("Spirited Away", 2001),
                new SeedMovie("Howl's Moving Castle", 2004));

        seedDirector(directors, movies, new Director(
                "david-fincher",
                "David Fincher",
                "A meticulous stylist fascinated by obsession, systems, identity, and the darkness beneath controlled surfaces.",
                "Exacting modern thrillers with procedural detail and unease",
                -1, 2, 2, 2, 1
        ), new SeedMovie("Se7en", 1995),
                new SeedMovie("Fight Club", 1999),
                new SeedMovie("Zodiac", 2007),
                new SeedMovie("The Social Network", 2010));

        seedDirector(directors, movies, new Director(
                "joel-and-ethan-coen",
                "Joel and Ethan Coen",
                "Genre-fluent filmmakers who find cosmic absurdity, regional character, and sudden menace in ordinary lives.",
                "Darkly comic American tales shaped by fate and eccentricity",
                0, 1, 1, 1, 2
        ), new SeedMovie("Fargo", 1996),
                new SeedMovie("The Big Lebowski", 1998),
                new SeedMovie("No Country for Old Men", 2007));

        seedDirector(directors, movies, new Director(
                "wes-anderson",
                "Wes Anderson",
                "An unmistakable miniaturist who frames grief, family, and adventure through symmetry, colour, and dry humour.",
                "Meticulous storybook worlds balancing whimsy and melancholy",
                1, 2, 0, -1, 2
        ), new SeedMovie("The Royal Tenenbaums", 2001),
                new SeedMovie("Fantastic Mr. Fox", 2009),
                new SeedMovie("The Grand Budapest Hotel", 2014));

        seedDirector(directors, movies, new Director(
                "jordan-peele",
                "Jordan Peele",
                "A sharp genre satirist who turns social anxiety into entertaining, symbol-rich horror.",
                "High-concept horror where cultural critique drives the suspense",
                1, 1, 1, 2, 2
        ), new SeedMovie("Get Out", 2017),
                new SeedMovie("Us", 2019),
                new SeedMovie("Nope", 2022));

        seedDirector(directors, movies, new Director(
                "guillermo-del-toro",
                "Guillermo del Toro",
                "A romantic fantasist who finds beauty in monsters and darkness in institutions.",
                "Lush gothic fables with tactile creatures and open hearts",
                2, 2, 0, 1, 2
        ), new SeedMovie("Pan's Labyrinth", 2006),
                new SeedMovie("The Shape of Water", 2017),
                new SeedMovie("Guillermo del Toro's Pinocchio", 2022));

        seedDirector(directors, movies, new Director(
                "jane-campion",
                "Jane Campion",
                "A psychologically acute filmmaker exploring desire, power, landscape, and interior lives.",
                "Sensuous, literary dramas charged with emotional tension",
                2, 2, 1, 1, 1
        ), new SeedMovie("The Piano", 1993),
                new SeedMovie("Bright Star", 2009),
                new SeedMovie("The Power of the Dog", 2021));

        seedDirector(directors, movies, new Director(
                "agnes-varda",
                "Agnès Varda",
                "A playful, compassionate innovator who moved freely between fiction, documentary, autobiography, and social observation.",
                "Curious, humane cinema that discovers form in everyday life",
                2, 1, 0, -1, 2
        ), new SeedMovie("Cléo from 5 to 7", 1962),
                new SeedMovie("Vagabond", 1985),
                new SeedMovie("The Gleaners and I", 2000));

        seedDirector(directors, movies, new Director(
                "wong-kar-wai",
                "Wong Kar-wai",
                "A poet of longing and missed connection, remembered for tactile colour, music, and fragmented time.",
                "Dreamlike romances built from mood, memory, and intimate gestures",
                2, 2, -1, 0, 1
        ), new SeedMovie("Chungking Express", 1994),
                new SeedMovie("Happy Together", 1997),
                new SeedMovie("In the Mood for Love", 2000));

        seedDirector(directors, movies, new Director(
                "pedro-almodovar",
                "Pedro Almodóvar",
                "A bold melodramatist whose vivid stories embrace desire, identity, family, pain, and reinvention.",
                "Colour-saturated melodrama with wit, empathy, and transgression",
                2, 2, 0, 0, 2
        ), new SeedMovie("All About My Mother", 1999),
                new SeedMovie("Talk to Her", 2002),
                new SeedMovie("Pain and Glory", 2019));

        seedDirector(directors, movies, new Director(
                "park-chan-wook",
                "Park Chan-wook",
                "A virtuoso of revenge, desire, and deception whose elaborate images make moral danger seductive.",
                "Baroque psychological thrillers full of reversals and dark beauty",
                0, 2, 2, 2, 2
        ), new SeedMovie("Oldboy", 2003),
                new SeedMovie("The Handmaiden", 2016),
                new SeedMovie("Decision to Leave", 2022));

        seedDirector(directors, movies, new Director(
                "spike-lee",
                "Spike Lee",
                "An urgent, energetic filmmaker confronting race, history, community, and American contradiction.",
                "Politically alive cinema mixing provocation, humour, and feeling",
                2, 1, 1, 1, 2
        ), new SeedMovie("Do the Right Thing", 1989),
                new SeedMovie("Malcolm X", 1992),
                new SeedMovie("BlacKkKlansman", 2018));

        seedDirector(directors, movies, new Director(
                "francis-ford-coppola",
                "Francis Ford Coppola",
                "An operatic storyteller whose ambitious dramas trace family, power, surveillance, and moral collapse.",
                "Grand American epics with intimate tragedy at their centre",
                1, 2, 1, 2, 1
        ), new SeedMovie("The Godfather", 1972),
                new SeedMovie("The Conversation", 1974),
                new SeedMovie("The Godfather Part II", 1974),
                new SeedMovie("Apocalypse Now", 1979));

        seedDirector(directors, movies, new Director(
                "celine-sciamma",
                "Céline Sciamma",
                "A precise, empathetic observer of identity, intimacy, childhood, and the ways people learn to see one another.",
                "Quietly radical coming-of-age stories and luminous romances",
                2, 2, 0, -1, 1
        ), new SeedMovie("Girlhood", 2014),
                new SeedMovie("Portrait of a Lady on Fire", 2019),
                new SeedMovie("Petite Maman", 2021));

        seedDirector(directors, movies, new Director(
                "david-lynch",
                "David Lynch",
                "A singular surrealist who locates nightmares, mystery, and aching tenderness beneath familiar American surfaces.",
                "Dream-logic mysteries where beauty and dread share the frame",
                -1, 2, 2, 2, 2
        ), new SeedMovie("Eraserhead", 1977),
                new SeedMovie("Blue Velvet", 1986),
                new SeedMovie("Mulholland Drive", 2001));

        seedDirector(directors, movies, new Director(
                "paul-thomas-anderson",
                "Paul Thomas Anderson",
                "An actor-focused formalist drawn to ambition, dependency, damaged families, and American reinvention.",
                "Expansive character dramas with restless craft and emotional friction",
                1, 1, 1, 1, 1
        ), new SeedMovie("Boogie Nights", 1997),
                new SeedMovie("There Will Be Blood", 2007),
                new SeedMovie("The Master", 2012));

        seedDirector(directors, movies, new Director(
                "ridley-scott",
                "Ridley Scott",
                "A painterly world-builder whose muscular genre films make environments, institutions, and survival feel tangible.",
                "Immersive visual worlds built for adult-scale spectacle",
                -1, 2, 0, 1, 0
        ), new SeedMovie("Alien", 1979),
                new SeedMovie("Blade Runner", 1982),
                new SeedMovie("Gladiator", 2000));

        seedDirector(directors, movies, new Director(
                "kathryn-bigelow",
                "Kathryn Bigelow",
                "A visceral action filmmaker who examines risk, adrenaline, masculinity, and the machinery of conflict.",
                "Tactile, high-pressure thrillers with documentary immediacy",
                -1, 1, 0, 2, 1
        ), new SeedMovie("Point Break", 1991),
                new SeedMovie("The Hurt Locker", 2008),
                new SeedMovie("Zero Dark Thirty", 2012));

        seedDirector(directors, movies, new Director(
                "james-cameron",
                "James Cameron",
                "A technological showman who grounds enormous action and science-fiction canvases in clean stakes and powerful emotion.",
                "Precision-engineered spectacle with populist heart",
                1, 2, 0, 1, 0
        ), new SeedMovie("Aliens", 1986),
                new SeedMovie("Terminator 2: Judgment Day", 1991),
                new SeedMovie("Titanic", 1997));
    }

    private void seedDirector(
            DirectorRepository directors,
            MovieRepository movies,
            Director candidate,
            SeedMovie... filmography
    ) {
        Director director = directors.findBySlug(candidate.getSlug())
                .orElseGet(() -> directors.save(candidate));

        for (SeedMovie movie : filmography) {
            if (!movies.existsByTitleAndDirectorId(movie.title(), director.getId())) {
                movies.save(new Movie(movie.title(), movie.releaseYear(), director));
            }
        }
    }

    private void seedQuestions(QuestionRepository questions) {
        questions.saveAll(List.of(
                new Question(
                        "What pulls you through a film most strongly?",
                        "Ideas and mechanics", "People and feelings",
                        TraitAxis.EMOTION, 1.3, 1
                ),
                new Question(
                        "How emotionally open do you want a movie to be?",
                        "Cool and restrained", "Warm and vulnerable",
                        TraitAxis.EMOTION, 1.0, 2
                ),
                new Question(
                        "Which kind of filmmaking catches your attention first?",
                        "Invisible and natural", "Composed and visually bold",
                        TraitAxis.VISUAL_STYLE, 1.2, 3
                ),
                new Question(
                        "How important is atmosphere compared with plot momentum?",
                        "Keep the story moving", "Let me live in the mood",
                        TraitAxis.VISUAL_STYLE, 0.9, 4
                ),
                new Question(
                        "How much work should a film ask of its audience?",
                        "Make the path clear", "Give me a puzzle",
                        TraitAxis.COMPLEXITY, 1.3, 5
                ),
                new Question(
                        "Which story structure sounds more inviting?",
                        "Focused and linear", "Layered and time-bending",
                        TraitAxis.COMPLEXITY, 1.0, 6
                ),
                new Question(
                        "Where should a story sit on the tonal spectrum?",
                        "Hopeful and buoyant", "Tense and unsettling",
                        TraitAxis.DARKNESS, 1.1, 7
                ),
                new Question(
                        "How comfortable are you with morally difficult characters?",
                        "I want someone to root for", "Complicate everyone",
                        TraitAxis.DARKNESS, 0.9, 8
                ),
                new Question(
                        "How should a great film handle genre rules?",
                        "Use them confidently", "Bend or combine them",
                        TraitAxis.EXPERIMENTAL, 1.2, 9
                ),
                new Question(
                        "When a movie takes an unusual creative swing, how do you react?",
                        "Earn my trust first", "Surprise me",
                        TraitAxis.EXPERIMENTAL, 1.0, 10
                )
        ));
    }

    private record SeedMovie(String title, int releaseYear) {
    }
}

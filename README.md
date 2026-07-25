# directMe.film

> Discover the film director whose creative voice best matches your taste.

directMe.film is a full-stack movie-personality experience. Answer ten questions
about how you connect with films, choose two to five favourites, and receive a
director match with a score, clear reasons, and a short watchlist.

The project began as a focused Spring Boot and React prototype and is now an
actively evolving personal project. Its core principle is simple: matching
should be transparent and repeatable, so the result is produced by a
deterministic weighted algorithm rather than an LLM.

## Project status

**Active prototype / MVP**

The complete questionnaire flow works locally, matches are persisted, and saved
results can be reopened by UUID. The current catalog contains 30 directors and
97 films across classic Hollywood, international cinema, animation, modern
genre filmmaking, and independent film.

## What you can do

- Answer ten film-taste and personality questions.
- Search a curated catalog by film title or director.
- Select two to five favourite films.
- Receive a deterministic match across 30 director profiles.
- See a numerical score and two or three human-readable reasons.
- Get three recommended films from the matched director.
- Reopen a persisted result at `/match/{id}`.
- Run immediately with H2 or switch to PostgreSQL through environment
  variables.

The interface includes responsive layouts, keyboard-friendly controls, loading
feedback, client-side validation, empty search states, and recoverable API error
states.

## How it works

```text
React + TypeScript
        │
        │ HTTP / JSON
        ▼
Spring Web controllers
        │
        ▼
Validated request and response DTOs
        │
        ▼
Service-layer matching and catalog rules
        │
        ▼
Spring Data JPA repositories
        │
        ▼
PostgreSQL or H2
```

The backend uses a conventional layered architecture:

- **Controllers** own HTTP concerns and status codes.
- **DTOs** define the public API contract and carry validation rules.
- **Services** own transactions and business logic.
- **Repositories** isolate persistence operations.
- **JPA entities** model stored directors, films, questions, and match results.
- **Centralized exception handling** converts failures into consistent JSON
  responses.

JPA entities are never returned directly from the controllers. Keeping entities
and DTOs separate prevents persistence details from leaking into the public API
and allows each model to evolve independently.

## Tech stack

| Area | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot 4.1, Maven Wrapper |
| REST API | Spring Web MVC |
| Validation | Jakarta Bean Validation |
| Persistence | Spring Data JPA, Hibernate |
| Databases | PostgreSQL, H2 development fallback |
| Backend testing | JUnit, AssertJ, Spring Boot Test |
| Frontend | React 19, TypeScript 6 |
| Tooling | Vite 8, Oxlint |
| Styling | Responsive plain CSS |
| HTTP client | Browser Fetch API |

## Matching model

The match is deterministic: identical inputs always produce the same result.

Each question measures one of five creative axes:

- Emotion
- Visual style
- Complexity
- Darkness
- Experimentation

Answers and director profiles share a scale from `-2` to `2`. Per-question
similarity is calculated as:

```text
similarity = 1 - abs(answer - directorProfile) / 4
```

Each question has an editorial weight. The final result combines:

```text
82% weighted questionnaire similarity
18% favourite-film director affinity
```

Exact score ties are resolved by director name so the algorithm remains stable.
Reasons are generated from the strongest matching axes and, where relevant,
favourite-film affinity.

This is an editorial recommendation model, not a clinical or psychometric
assessment.

## API

| Method | Endpoint | Description | Success |
| --- | --- | --- | --- |
| `GET` | `/api/questions` | Return the questionnaire in display order | `200` |
| `GET` | `/api/directors` | Return directors and their films | `200` |
| `POST` | `/api/matches` | Validate, score, and persist a questionnaire | `201` |
| `GET` | `/api/matches/{id}` | Retrieve a saved result | `200` |
| `GET` | `/api/health` | Return basic service health | `200` |

Invalid input returns `400`, an unknown match returns `404`, and unexpected
server failures return `500`. Error responses use one consistent shape and can
include field-level validation messages.

### Example match request

IDs in this example correspond to a fresh database seeded by the application.

```bash
curl -X POST http://localhost:8080/api/matches \
  -H "Content-Type: application/json" \
  -d '{
    "answers": [
      { "questionId": 1, "value": -1 },
      { "questionId": 2, "value": -1 },
      { "questionId": 3, "value": 2 },
      { "questionId": 4, "value": 2 },
      { "questionId": 5, "value": 2 },
      { "questionId": 6, "value": 2 },
      { "questionId": 7, "value": 1 },
      { "questionId": 8, "value": 1 },
      { "questionId": 9, "value": 1 },
      { "questionId": 10, "value": 1 }
    ],
    "favoriteMovieIds": [4, 5]
  }'
```

Example response:

```json
{
  "id": "c1741525-83d5-4b80-af27-4190849854bd",
  "matchedDirector": {
    "id": 2,
    "slug": "christopher-nolan",
    "name": "Christopher Nolan",
    "signatureStyle": "Cerebral blockbusters built around time and structure",
    "movies": [
      { "id": 6, "title": "Oppenheimer", "releaseYear": 2023 },
      { "id": 4, "title": "Inception", "releaseYear": 2010 },
      { "id": 5, "title": "The Dark Knight", "releaseYear": 2008 }
    ]
  },
  "matchScore": 100,
  "reasons": [
    "Your favourites include Inception and The Dark Knight, a strong signal for this filmmaker's voice.",
    "You enjoy layered stories that reward active attention.",
    "You are comfortable with tension, ambiguity, and morally difficult territory."
  ],
  "recommendedMovies": [
    { "id": 6, "title": "Oppenheimer", "releaseYear": 2023 },
    { "id": 4, "title": "Inception", "releaseYear": 2010 },
    { "id": 5, "title": "The Dark Knight", "releaseYear": 2008 }
  ],
  "createdAt": "2026-07-23T17:30:00Z"
}
```

## Getting started

### Prerequisites

- Java 21 or newer
- Node.js 20.19+ or 22.12+
- npm
- PostgreSQL is optional

### Clone the project

```bash
git clone https://github.com/mikeylim/directMe-film.git
cd directMe-film
```

### Start the backend

The Maven Wrapper is included, so Maven does not need to be installed globally.

```bash
cd backend
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.

```bash
curl http://localhost:8080/api/health
```

By default, development uses an in-memory H2 database. The H2 console is
available at `http://localhost:8080/h2-console` with:

```text
JDBC URL: jdbc:h2:mem:directmefilm
User:     sa
Password: <blank>
```

### Start the frontend

In a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

The client defaults to `http://localhost:8080/api`. To override it:

```bash
cp .env.example .env.local
```

```env
VITE_API_URL=http://localhost:8080/api
```

## Using PostgreSQL

Create a database and provide the backend variables:

```bash
cd backend
export DATABASE_URL=jdbc:postgresql://localhost:5432/directmefilm
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
export FRONTEND_ORIGIN=http://localhost:5173
./mvnw spring-boot:run
```

The catalog seed process is additive and idempotent. Existing directors and
films are detected before insertion, allowing the curated catalog to grow
without duplicating data in an existing PostgreSQL database.

## Testing and verification

Backend:

```bash
cd backend
./mvnw test
./mvnw clean package
```

The seven current backend tests cover:

- Application startup
- Bean Validation constraints
- Deterministic scoring
- Match persistence and retrieval
- Incomplete questionnaire rejection
- Catalog integrity
- Idempotent catalog seeding

Frontend:

```bash
cd frontend
npm run lint
npm run build
```

There is not yet a frontend component test suite.

## Project structure

```text
directMe-film/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/directmefilm/
│       │   ├── config/       # CORS and additive seed data
│       │   ├── controller/   # REST endpoints
│       │   ├── dto/          # API contracts and validation
│       │   ├── exception/    # Domain errors and global handling
│       │   ├── model/        # JPA entities and matching traits
│       │   ├── repository/   # Spring Data repositories
│       │   └── service/      # Catalog and matching logic
│       └── test/             # Service, validation, and context tests
├── frontend/
│   ├── package.json
│   └── src/
│       ├── components/       # Questionnaire and result UI
│       ├── api.ts            # Typed Fetch wrapper
│       ├── types.ts          # API types
│       └── App.tsx           # Application state and navigation
└── README.md
```

## Film curation

The catalog is editorial rather than a live popularity ranking. It uses three
complementary reputation signals:

- The [BFI Sight and Sound Greatest Films polls](https://www.bfi.org.uk/sight-and-sound/greatest-films-all-time)
  for the international critical canon.
- [Academy Governors Awards](https://www.oscars.org/governors/about) and
  competitive Academy recognition for sustained influence and craft.
- [Metacritic's film database](https://www.metacritic.com/browse/movie/?sort=desc)
  and director retrospectives for aggregated professional-review strength.

Ratings are not stored or displayed. These sources guide curation while the
application itself focuses on creative-personality matching.

## Known limitations

- Director profiles and film selections are curated rather than user-editable.
- Matches are publicly retrievable by UUID; there is no user account system.
- H2 data disappears when the backend stops.
- Hibernate `ddl-auto=update` is convenient for the prototype but is not a
  production migration strategy.
- Director profiles use five editorial axes and cannot capture every aspect of
  a filmmaker's work.
- Deep frontend links require the eventual host to serve `index.html` as an SPA
  fallback.

## Roadmap

### Near term

- Add Flyway migrations.
- Add React component and end-to-end tests.
- Add visible feedback after copying a result link.
- Improve film discovery with eras, countries, and genre filters.
- Refine matching profiles using per-film traits instead of director ownership
  alone.

### Later

- Add authenticated, private match history.
- Add an admin workflow for catalog management.
- Add PostgreSQL integration tests with Testcontainers.
- Add richer result comparisons and alternative director matches.
- Deploy the frontend and API with production monitoring.

## Contributing

This is a personal project in active development, but thoughtful bug reports,
film suggestions, and focused pull requests are welcome. Please open an issue
before starting a large change so the scope can be discussed first.

## Author

Built by [Mikey Lim](https://github.com/mikeylim).

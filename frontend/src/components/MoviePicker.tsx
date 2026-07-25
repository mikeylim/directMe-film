import { useState } from 'react'
import type { Director } from '../types'

interface MoviePickerProps {
  directors: Director[]
  selectedIds: Set<number>
  onToggle: (movieId: number) => void
}

export function MoviePicker({
  directors,
  selectedIds,
  onToggle,
}: MoviePickerProps) {
  const [query, setQuery] = useState('')
  const movies = directors
    .flatMap((director) =>
      director.movies.map((movie) => ({ ...movie, director: director.name })),
    )
    .sort((left, right) => left.title.localeCompare(right.title))
  const normalizedQuery = query.trim().toLocaleLowerCase()
  const visibleMovies = normalizedQuery
    ? movies.filter((movie) =>
        `${movie.title} ${movie.director}`
          .toLocaleLowerCase()
          .includes(normalizedQuery),
      )
    : movies

  return (
    <fieldset className="movie-picker">
      <legend className="sr-only">Select favourite movies</legend>
      <div className="movie-tools">
        <label>
          <span className="sr-only">Search by film or director</span>
          <input
            type="search"
            value={query}
            placeholder="Search by film or director…"
            onChange={(event) => setQuery(event.target.value)}
          />
        </label>
        <span>
          {visibleMovies.length} {visibleMovies.length === 1 ? 'film' : 'films'}
        </span>
      </div>
      <div className="movie-grid">
        {visibleMovies.map((movie) => {
          const selected = selectedIds.has(movie.id)
          const atLimit = selectedIds.size >= 5 && !selected

          return (
            <label
              className={[
                'movie-option',
                selected ? 'selected' : '',
                atLimit ? 'disabled' : '',
              ]
                .filter(Boolean)
                .join(' ')}
              key={movie.id}
            >
              <input
                type="checkbox"
                checked={selected}
                disabled={atLimit}
                onChange={() => onToggle(movie.id)}
              />
              <span className="movie-check" aria-hidden="true">
                {selected ? '✓' : '+'}
              </span>
              <span>
                <strong>{movie.title}</strong>
                <small>
                  {movie.releaseYear} · {movie.director}
                </small>
              </span>
            </label>
          )
        })}
      </div>
      {visibleMovies.length === 0 && (
        <p className="empty-movies">No films match that search.</p>
      )}
    </fieldset>
  )
}

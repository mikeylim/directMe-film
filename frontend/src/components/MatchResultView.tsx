import { useState } from 'react'
import type { MatchResult } from '../types'

interface MatchResultViewProps {
  result: MatchResult
  onRestart: () => void
}

export function MatchResultView({ result, onRestart }: MatchResultViewProps) {
  const [copyState, setCopyState] = useState<'idle' | 'copied' | 'error'>('idle')
  const { matchedDirector: director } = result

  async function copyResultLink() {
    try {
      await navigator.clipboard.writeText(window.location.href)
      setCopyState('copied')
    } catch {
      setCopyState('error')
    }
  }

  const copyLabel = {
    idle: 'Copy result link',
    copied: 'Link copied!',
    error: 'Copy failed — retry',
  }[copyState]

  return (
    <main className="result-page">
      <p className="section-label">The final cut</p>
      <section className="result-hero">
        <div className="score-ring" aria-label={`${result.matchScore}% match`}>
          <span>{result.matchScore}</span>
          <small>% match</small>
        </div>
        <div>
          <p className="result-kicker">Your directing personality is</p>
          <h1>{director.name}</h1>
          <p className="signature">{director.signatureStyle}</p>
        </div>
      </section>

      <div className="result-grid">
        <section className="result-panel director-profile">
          <p className="panel-number">01 / Profile</p>
          <h2>A precise eye with a point of view.</h2>
          <p>{director.description}</p>
        </section>

        <section className="result-panel">
          <p className="panel-number">02 / Why you matched</p>
          <ul className="reason-list">
            {result.reasons.map((reason, index) => (
              <li key={reason}>
                <span>{String(index + 1).padStart(2, '0')}</span>
                {reason}
              </li>
            ))}
          </ul>
        </section>
      </div>

      <section className="watchlist">
        <div>
          <p className="section-label">Your next screening</p>
          <h2>Three films for the watchlist</h2>
        </div>
        <div className="recommendation-grid">
          {result.recommendedMovies.map((movie, index) => (
            <article className="recommendation-card" key={movie.id}>
              <span className="film-index">
                {String(index + 1).padStart(2, '0')}
              </span>
              <div>
                <h3>{movie.title}</h3>
                <p>{movie.releaseYear} · Directed by {director.name}</p>
              </div>
            </article>
          ))}
        </div>
      </section>

      <div className="result-actions">
        <button className="primary-button" type="button" onClick={onRestart}>
          Retake the questionnaire
        </button>
        <button
          className="secondary-button"
          type="button"
          onClick={() => void copyResultLink()}
        >
          {copyLabel}
        </button>
        <p className="sr-only" aria-live="polite">
          {copyState === 'copied' && 'Result link copied to clipboard.'}
          {copyState === 'error' && 'The result link could not be copied.'}
        </p>
      </div>
    </main>
  )
}

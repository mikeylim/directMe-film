import { useEffect, useState } from 'react'
import { ApiError, getDirectors, getMatch, getQuestions } from './api'
import { AppShell } from './components/AppShell'
import { ErrorState } from './components/ErrorState'
import { LoadingState } from './components/LoadingState'
import { MatchResultView } from './components/MatchResultView'
import { Questionnaire } from './components/Questionnaire'
import type { Director, MatchResult, Question } from './types'
import './App.css'

type Screen = 'intro' | 'questionnaire' | 'result'

function App() {
  const [questions, setQuestions] = useState<Question[]>([])
  const [directors, setDirectors] = useState<Director[]>([])
  const [result, setResult] = useState<MatchResult | null>(null)
  const [screen, setScreen] = useState<Screen>('intro')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [reloadKey, setReloadKey] = useState(0)

  useEffect(() => {
    let cancelled = false

    async function load() {
      setLoading(true)
      setError(null)

      try {
        const matchId = matchIdFromPath()
        const [loadedQuestions, loadedDirectors, loadedMatch] = await Promise.all([
          getQuestions(),
          getDirectors(),
          matchId ? getMatch(matchId) : Promise.resolve(null),
        ])

        if (cancelled) return
        setQuestions(loadedQuestions)
        setDirectors(loadedDirectors)
        if (loadedMatch) {
          setResult(loadedMatch)
          setScreen('result')
        }
      } catch (loadError) {
        if (!cancelled) {
          setError(messageFor(loadError))
        }
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    void load()
    return () => {
      cancelled = true
    }
  }, [reloadKey])

  function showResult(match: MatchResult) {
    setResult(match)
    setScreen('result')
    window.history.pushState({}, '', `/match/${match.id}`)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function restart() {
    setResult(null)
    setScreen('questionnaire')
    window.history.pushState({}, '', '/')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  if (loading) {
    return (
      <AppShell>
        <LoadingState />
      </AppShell>
    )
  }

  if (error) {
    return (
      <AppShell>
        <ErrorState
          message={error}
          onRetry={() => setReloadKey((key) => key + 1)}
        />
      </AppShell>
    )
  }

  return (
    <AppShell>
      {screen === 'intro' && (
        <main className="intro">
          <div className="eyebrow">A movie personality match</div>
          <h1>
            Your taste has a
            <span> director.</span>
          </h1>
          <p className="intro-copy">
            Ten instinctive choices. A handful of favourite films. One
            filmmaker whose creative voice feels uncannily like yours.
          </p>
          <button
            className="primary-button"
            type="button"
            onClick={() => setScreen('questionnaire')}
          >
            Find my director
            <span aria-hidden="true">→</span>
          </button>
          <p className="time-note">Takes about 2 minutes · no sign-up</p>

          <div className="director-marquee" aria-label="Possible directors">
            {directors.map((director, index) => (
              <div className="director-token" key={director.id}>
                <span>{String(index + 1).padStart(2, '0')}</span>
                {director.name}
              </div>
            ))}
          </div>

          <section className="how-it-works">
            <p className="section-label">How the cut comes together</p>
            <div className="steps-grid">
              <article>
                <span>01</span>
                <h2>Follow your instinct</h2>
                <p>Choose between cinematic opposites. There are no wrong answers.</p>
              </article>
              <article>
                <span>02</span>
                <h2>Show us your shelf</h2>
                <p>Pick two to five favourites from a small, considered collection.</p>
              </article>
              <article>
                <span>03</span>
                <h2>Meet your match</h2>
                <p>Get a transparent score, reasons, and three films for your watchlist.</p>
              </article>
            </div>
          </section>
        </main>
      )}

      {screen === 'questionnaire' && (
        <Questionnaire
          questions={questions}
          directors={directors}
          onComplete={showResult}
          onCancel={() => setScreen('intro')}
        />
      )}

      {screen === 'result' && result && (
        <MatchResultView result={result} onRestart={restart} />
      )}
    </AppShell>
  )
}

function matchIdFromPath(): string | null {
  const match = window.location.pathname.match(/^\/match\/([0-9a-f-]+)$/i)
  return match?.[1] ?? null
}

function messageFor(error: unknown): string {
  if (error instanceof ApiError) return error.message
  return 'We could not reach the screening room. Make sure the API is running and try again.'
}

export default App

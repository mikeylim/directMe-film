import { useState } from 'react'
import { ApiError, createMatch } from '../api'
import type { Director, MatchResult, Question } from '../types'
import { MoviePicker } from './MoviePicker'
import { ScaleQuestion } from './ScaleQuestion'

interface QuestionnaireProps {
  questions: Question[]
  directors: Director[]
  onComplete: (result: MatchResult) => void
  onCancel: () => void
}

export function Questionnaire({
  questions,
  directors,
  onComplete,
  onCancel,
}: QuestionnaireProps) {
  const [step, setStep] = useState(0)
  const [answers, setAnswers] = useState<Record<number, number>>({})
  const [favoriteIds, setFavoriteIds] = useState<Set<number>>(new Set())
  const [validationMessage, setValidationMessage] = useState<string | null>(null)
  const [submitting, setSubmitting] = useState(false)
  const isMovieStep = step === questions.length
  const progress = ((step + (isMovieStep ? 1 : 0)) / (questions.length + 1)) * 100

  function next() {
    const current = questions[step]
    if (current && answers[current.id] === undefined) {
      setValidationMessage('Choose the point that feels closest to you.')
      return
    }

    setValidationMessage(null)
    setStep((currentStep) => Math.min(currentStep + 1, questions.length))
  }

  function back() {
    if (step === 0) {
      onCancel()
      return
    }
    setValidationMessage(null)
    setStep((currentStep) => currentStep - 1)
  }

  function toggleMovie(movieId: number) {
    setFavoriteIds((current) => {
      const nextIds = new Set(current)
      if (nextIds.has(movieId)) {
        nextIds.delete(movieId)
      } else if (nextIds.size < 5) {
        nextIds.add(movieId)
      }
      return nextIds
    })
    setValidationMessage(null)
  }

  async function submit() {
    if (favoriteIds.size < 2) {
      setValidationMessage('Select at least two films to complete your profile.')
      return
    }

    setSubmitting(true)
    setValidationMessage(null)
    try {
      const result = await createMatch({
        answers: questions.map((question) => ({
          questionId: question.id,
          value: answers[question.id],
        })),
        favoriteMovieIds: [...favoriteIds],
      })
      onComplete(result)
    } catch (error) {
      setValidationMessage(
        error instanceof ApiError
          ? error.message
          : 'The final reel jammed. Please try submitting again.',
      )
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <main className="questionnaire">
      <div className="quiz-meta">
        <button className="text-button" type="button" onClick={back}>
          ← {step === 0 ? 'Exit' : 'Back'}
        </button>
        <span>
          {isMovieStep ? 'Final cut' : `Question ${step + 1} of ${questions.length}`}
        </span>
      </div>
      <div
        className="progress-track"
        role="progressbar"
        aria-valuemin={0}
        aria-valuemax={100}
        aria-valuenow={Math.round(progress)}
      >
        <span style={{ width: `${progress}%` }} />
      </div>

      {!isMovieStep && questions[step] && (
        <div className="question-stage" key={questions[step].id}>
          <p className="section-label">Choose on instinct</p>
          <ScaleQuestion
            question={questions[step]}
            value={answers[questions[step].id]}
            onChange={(value) => {
              setAnswers((current) => ({
                ...current,
                [questions[step].id]: value,
              }))
              setValidationMessage(null)
            }}
          />
          {validationMessage && (
            <p className="validation-message" role="alert">
              {validationMessage}
            </p>
          )}
          <button className="primary-button" type="button" onClick={next}>
            Next frame <span aria-hidden="true">→</span>
          </button>
        </div>
      )}

      {isMovieStep && (
        <div className="movie-stage">
          <div className="movie-heading">
            <div>
              <p className="section-label">Your personal canon</p>
              <h1>Pick 2–5 favourites.</h1>
              <p>
                These choices add a small affinity signal to your questionnaire
                score.
              </p>
            </div>
            <div className="selection-count" aria-live="polite">
              <strong>{favoriteIds.size}</strong>
              <span>of 5 selected</span>
            </div>
          </div>
          <MoviePicker
            directors={directors}
            selectedIds={favoriteIds}
            onToggle={toggleMovie}
          />
          {validationMessage && (
            <p className="validation-message" role="alert">
              {validationMessage}
            </p>
          )}
          <button
            className="primary-button submit-button"
            type="button"
            disabled={submitting}
            onClick={() => void submit()}
          >
            {submitting ? 'Developing your result…' : 'Reveal my director'}
            {!submitting && <span aria-hidden="true">→</span>}
          </button>
        </div>
      )}
    </main>
  )
}

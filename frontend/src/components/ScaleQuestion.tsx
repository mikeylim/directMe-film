import type { Question } from '../types'

interface ScaleQuestionProps {
  question: Question
  value: number | undefined
  onChange: (value: number) => void
}

const VALUES = [-2, -1, 0, 1, 2]

export function ScaleQuestion({
  question,
  value,
  onChange,
}: ScaleQuestionProps) {
  return (
    <fieldset className="question-card">
      <legend>{question.prompt}</legend>
      <div className="scale-labels" aria-hidden="true">
        <span>{question.lowLabel}</span>
        <span>{question.highLabel}</span>
      </div>
      <div className="scale-options">
        {VALUES.map((option, index) => (
          <label
            className={value === option ? 'scale-option selected' : 'scale-option'}
            key={option}
          >
            <input
              type="radio"
              name={`question-${question.id}`}
              value={option}
              checked={value === option}
              onChange={() => onChange(option)}
            />
            <span className="scale-dot">{index + 1}</span>
            <span className="mobile-scale-label">
              {index === 0
                ? question.lowLabel
                : index === 4
                  ? question.highLabel
                  : index === 2
                    ? 'Somewhere between'
                    : 'Leaning'}
            </span>
          </label>
        ))}
      </div>
    </fieldset>
  )
}

interface ErrorStateProps {
  message: string
  onRetry: () => void
}

export function ErrorState({ message, onRetry }: ErrorStateProps) {
  return (
    <main className="status-state" role="alert">
      <div className="status-code">CUT!</div>
      <h1>Something broke the scene.</h1>
      <p>{message}</p>
      <button className="primary-button" type="button" onClick={onRetry}>
        Try that again
      </button>
    </main>
  )
}

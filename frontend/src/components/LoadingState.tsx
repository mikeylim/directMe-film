export function LoadingState() {
  return (
    <main className="status-state" aria-live="polite">
      <div className="loader" aria-hidden="true">
        <span />
        <span />
        <span />
      </div>
      <p className="section-label">Preparing the screening room</p>
      <h1>Splicing the reels…</h1>
    </main>
  )
}

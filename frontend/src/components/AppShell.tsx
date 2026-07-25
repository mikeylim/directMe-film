import type { PropsWithChildren } from 'react'

export function AppShell({ children }: PropsWithChildren) {
  return (
    <div className="site-shell">
      <header className="site-header">
        <a className="wordmark" href="/" aria-label="directMe.film home">
          direct<span>Me</span>.film
        </a>
        <div className="format-mark" aria-label="Digital cinema">
          <span aria-hidden="true" />
          Digital cinema
        </div>
      </header>
      {children}
      <footer>
        <p>Made for people who stay through the credits.</p>
        <p>Deterministic matching · no AI guesswork</p>
      </footer>
    </div>
  )
}

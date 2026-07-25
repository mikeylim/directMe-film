import type {
  Director,
  MatchRequest,
  MatchResult,
  Question,
} from './types'

const API_BASE_URL =
  import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api'

interface ErrorPayload {
  message?: string
}

export class ApiError extends Error {
  status: number

  constructor(message: string, status: number) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...init,
      headers: {
        'Content-Type': 'application/json',
        ...init?.headers,
      },
    })
  } catch {
    throw new ApiError(
      'The API is unavailable. Start the Spring Boot server on port 8080 and retry.',
      0,
    )
  }

  if (!response.ok) {
    const payload = (await response.json().catch(() => ({}))) as ErrorPayload
    throw new ApiError(
      payload.message ?? `The request failed with status ${response.status}.`,
      response.status,
    )
  }

  return response.json() as Promise<T>
}

export function getQuestions() {
  return apiFetch<Question[]>('/questions')
}

export function getDirectors() {
  return apiFetch<Director[]>('/directors')
}

export function createMatch(request: MatchRequest) {
  return apiFetch<MatchResult>('/matches', {
    method: 'POST',
    body: JSON.stringify(request),
  })
}

export function getMatch(id: string) {
  return apiFetch<MatchResult>(`/matches/${encodeURIComponent(id)}`)
}

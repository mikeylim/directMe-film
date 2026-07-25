export interface Question {
  id: number
  position: number
  prompt: string
  lowLabel: string
  highLabel: string
}

export interface Movie {
  id: number
  title: string
  releaseYear: number
}

export interface Director {
  id: number
  slug: string
  name: string
  description: string
  signatureStyle: string
  movies: Movie[]
}

export interface MatchResult {
  id: string
  matchedDirector: Director
  matchScore: number
  reasons: string[]
  recommendedMovies: Movie[]
  createdAt: string
}

export interface MatchRequest {
  answers: Array<{
    questionId: number
    value: number
  }>
  favoriteMovieIds: number[]
}

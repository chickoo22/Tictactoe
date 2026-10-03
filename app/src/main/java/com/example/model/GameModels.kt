package com.example.model

enum class Player {
    X, O
}

enum class GameMode {
    PVP, AI
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}

enum class GameStatus {
    IN_PROGRESS, X_WON, O_WON, TIE
}

data class ScoreState(
    val xWins: Int = 0,
    val oWins: Int = 0,
    val ties: Int = 0,
    val streak: Int = 0 // positive for X, negative for O
)

data class WinningLine(
    val startIndex: Int,
    val endIndex: Int,
    val combination: List<Int>
)

val WINNING_COMBINATIONS = listOf(
    listOf(0, 1, 2),
    listOf(3, 4, 5),
    listOf(6, 7, 8),
    listOf(0, 3, 6),
    listOf(1, 4, 7),
    listOf(2, 5, 8),
    listOf(0, 4, 8),
    listOf(2, 4, 6)
)

fun checkWinner(board: List<Player?>): Triple<GameStatus, List<Int>?, Player?> {
    for (combo in WINNING_COMBINATIONS) {
        val (a, b, c) = combo
        if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
            val winner = board[a]!!
            val status = if (winner == Player.X) GameStatus.X_WON else GameStatus.O_WON
            return Triple(status, combo, winner)
        }
    }
    if (board.none { it == null }) {
        return Triple(GameStatus.TIE, null, null)
    }
    return Triple(GameStatus.IN_PROGRESS, null, null)
}

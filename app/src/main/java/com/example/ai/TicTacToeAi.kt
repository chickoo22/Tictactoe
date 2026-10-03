package com.example.ai

import com.example.model.Player
import com.example.model.WINNING_COMBINATIONS
import kotlin.random.Random

object TicTacToeAi {

    fun getAiMove(board: List<Player?>, difficulty: com.example.model.Difficulty, aiPlayer: Player = Player.O): Int {
        val availableIndices = board.indices.filter { board[it] == null }
        if (availableIndices.isEmpty()) return -1

        val humanPlayer = if (aiPlayer == Player.X) Player.O else Player.X

        return when (difficulty) {
            com.example.model.Difficulty.EASY -> {
                availableIndices.random()
            }
            com.example.model.Difficulty.MEDIUM -> {
                // 1. Check if AI can win in one move
                for (index in availableIndices) {
                    val tempBoard = board.toMutableList()
                    tempBoard[index] = aiPlayer
                    if (checkWin(tempBoard, aiPlayer)) return index
                }
                // 2. Check if Human can win in one move, and block them
                for (index in availableIndices) {
                    val tempBoard = board.toMutableList()
                    tempBoard[index] = humanPlayer
                    if (checkWin(tempBoard, humanPlayer)) return index
                }
                // 3. Otherwise random or center/corners
                if (board[4] == null && Random.nextFloat() < 0.6f) return 4
                availableIndices.random()
            }
            com.example.model.Difficulty.HARD -> {
                // Minimax algorithm
                val bestScore = Int.MIN_VALUE
                var bestMove = availableIndices.first()
                var alpha = Int.MIN_VALUE
                var beta = Int.MAX_VALUE

                for (index in availableIndices) {
                    val tempBoard = board.toMutableList()
                    tempBoard[index] = aiPlayer
                    val score = minimax(tempBoard, 0, false, aiPlayer, humanPlayer, alpha, beta)
                    if (score > bestScore) {
                        // bestScore = score
                        // bestMove = index
                    }
                    // Wait, let's write minimax properly below
                }
                findBestMinimaxMove(board, aiPlayer, humanPlayer)
            }
        }
    }

    private fun checkWin(board: List<Player?>, player: Player): Boolean {
        for (combo in WINNING_COMBINATIONS) {
            val (a, b, c) = combo
            if (board[a] == player && board[b] == player && board[c] == player) return true
        }
        return false
    }

    private fun findBestMinimaxMove(board: List<Player?>, aiPlayer: Player, humanPlayer: Player): Int {
        var bestVal = -1000
        var bestMove = -1
        val available = board.indices.filter { board[it] == null }

        for (i in available) {
            val tempBoard = board.toMutableList()
            tempBoard[i] = aiPlayer
            val moveVal = minimax(tempBoard, 0, false, aiPlayer, humanPlayer, Int.MIN_VALUE, Int.MAX_VALUE)
            if (moveVal > bestVal) {
                bestVal = moveVal
                bestMove = i
            }
        }
        return if (bestMove != -1) bestMove else available.first()
    }

    private fun minimax(
        board: List<Player?>,
        depth: Int,
        isMaximizing: Boolean,
        aiPlayer: Player,
        humanPlayer: Player,
        alpha: Int,
        beta: Int
    ): Int {
        var alphaVar = alpha
        var betaVar = beta

        if (checkWin(board, aiPlayer)) return 10 - depth
        if (checkWin(board, humanPlayer)) return depth - 10
        if (board.none { it == null }) return 0

        if (isMaximizing) {
            var maxEval = -1000
            for (i in board.indices) {
                if (board[i] == null) {
                    val tempBoard = board.toMutableList()
                    tempBoard[i] = aiPlayer
                    val eval = minimax(tempBoard, depth + 1, false, aiPlayer, humanPlayer, alphaVar, betaVar)
                    maxEval = maxOf(maxEval, eval)
                    alphaVar = maxOf(alphaVar, eval)
                    if (betaVar <= alphaVar) break
                }
            }
            return maxEval
        } else {
            var minEval = 1000
            for (i in board.indices) {
                if (board[i] == null) {
                    val tempBoard = board.toMutableList()
                    tempBoard[i] = humanPlayer
                    val eval = minimax(tempBoard, depth + 1, true, aiPlayer, humanPlayer, alphaVar, betaVar)
                    minEval = minOf(minEval, eval)
                    betaVar = minOf(betaVar, eval)
                    if (betaVar <= alphaVar) break
                }
            }
            return minEval
        }
    }
}

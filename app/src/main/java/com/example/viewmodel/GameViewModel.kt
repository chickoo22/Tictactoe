package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.TicTacToeAi
import com.example.data.AppDatabase
import com.example.data.GameMatchEntity
import com.example.data.GameRepository
import com.example.model.BoardStyle
import com.example.model.ColorPalette
import com.example.model.Difficulty
import com.example.model.GameMode
import com.example.model.GameStatus
import com.example.model.MarkerStyle
import com.example.model.Player
import com.example.model.ScoreState
import com.example.model.checkWinner
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository

    init {
        val dao = AppDatabase.getDatabase(application).gameDao()
        repository = GameRepository(dao)

        try {
            MobileAds.initialize(application) {}
        } catch (e: Exception) {
            // Ignored if offline/test
        }
    }

    val matchHistory: StateFlow<List<GameMatchEntity>> = repository.allMatches
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _board = MutableStateFlow<List<Player?>>(List(9) { null })
    val board: StateFlow<List<Player?>> = _board.asStateFlow()

    private val _currentPlayer = MutableStateFlow(Player.X)
    val currentPlayer: StateFlow<Player> = _currentPlayer.asStateFlow()

    private val _gameStatus = MutableStateFlow(GameStatus.IN_PROGRESS)
    val gameStatus: StateFlow<GameStatus> = _gameStatus.asStateFlow()

    private val _winningCombination = MutableStateFlow<List<Int>?>(null)
    val winningCombination: StateFlow<List<Int>?> = _winningCombination.asStateFlow()

    private val _winningPlayer = MutableStateFlow<Player?>(null)
    val winningPlayer: StateFlow<Player?> = _winningPlayer.asStateFlow()

    private val _gameMode = MutableStateFlow(GameMode.PVP)
    val gameMode: StateFlow<GameMode> = _gameMode.asStateFlow()

    private val _difficulty = MutableStateFlow(Difficulty.MEDIUM)
    val difficulty: StateFlow<Difficulty> = _difficulty.asStateFlow()

    private val _scoreState = MutableStateFlow(ScoreState())
    val scoreState: StateFlow<ScoreState> = _scoreState.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _playerXName = MutableStateFlow("Player X")
    val playerXName: StateFlow<String> = _playerXName.asStateFlow()

    private val _playerOName = MutableStateFlow("Player O")
    val playerOName: StateFlow<String> = _playerOName.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(true)
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    // Theme Engine States
    private val _selectedPalette = MutableStateFlow(ColorPalette.NEON_CYBER)
    val selectedPalette: StateFlow<ColorPalette> = _selectedPalette.asStateFlow()

    private val _boardStyle = MutableStateFlow(BoardStyle.GLASS)
    val boardStyle: StateFlow<BoardStyle> = _boardStyle.asStateFlow()

    private val _markerStyle = MutableStateFlow(MarkerStyle.CLASSIC)
    val markerStyle: StateFlow<MarkerStyle> = _markerStyle.asStateFlow()

    fun updatePlayerNames(xName: String, oName: String) {
        if (xName.isNotBlank()) _playerXName.value = xName
        if (oName.isNotBlank()) _playerOName.value = oName
    }

    fun setHapticsEnabled(enabled: Boolean) {
        _hapticsEnabled.value = enabled
    }

    fun updatePalette(palette: ColorPalette) {
        _selectedPalette.value = palette
    }

    fun updateBoardStyle(style: BoardStyle) {
        _boardStyle.value = style
    }

    fun updateMarkerStyle(style: MarkerStyle) {
        _markerStyle.value = style
    }

    fun makeMove(index: Int) {
        if (_board.value[index] != null || _gameStatus.value != GameStatus.IN_PROGRESS || _isAiThinking.value) {
            return
        }

        val updatedBoard = _board.value.toMutableList()
        val currentP = _currentPlayer.value
        updatedBoard[index] = currentP
        _board.value = updatedBoard

        val (status, combo, winner) = checkWinner(updatedBoard)
        if (status != GameStatus.IN_PROGRESS) {
            endGame(status, combo, winner)
        } else {
            val nextPlayer = if (currentP == Player.X) Player.O else Player.X
            _currentPlayer.value = nextPlayer

            if (_gameMode.value == GameMode.AI && nextPlayer == Player.O) {
                triggerAiMove(updatedBoard)
            }
        }
    }

    private fun triggerAiMove(currentBoard: List<Player?>) {
        viewModelScope.launch {
            _isAiThinking.value = true
            delay(400)
            if (_gameStatus.value == GameStatus.IN_PROGRESS) {
                val aiMove = TicTacToeAi.getAiMove(_board.value, _difficulty.value, Player.O)
                if (aiMove != -1 && _board.value[aiMove] == null) {
                    val updatedBoard = _board.value.toMutableList()
                    updatedBoard[aiMove] = Player.O
                    _board.value = updatedBoard

                    val (status, combo, winner) = checkWinner(updatedBoard)
                    if (status != GameStatus.IN_PROGRESS) {
                        endGame(status, combo, winner)
                    } else {
                        _currentPlayer.value = Player.X
                    }
                }
            }
            _isAiThinking.value = false
        }
    }

    private fun endGame(status: GameStatus, combo: List<Int>?, winner: Player?) {
        _gameStatus.value = status
        _winningCombination.value = combo
        _winningPlayer.value = winner

        _scoreState.update { current ->
            when (status) {
                GameStatus.X_WON -> current.copy(
                    xWins = current.xWins + 1,
                    streak = if (current.streak >= 0) current.streak + 1 else 1
                )
                GameStatus.O_WON -> current.copy(
                    oWins = current.oWins + 1,
                    streak = if (current.streak <= 0) current.streak - 1 else -1
                )
                GameStatus.TIE -> current.copy(ties = current.ties + 1, streak = 0)
                else -> current
            }
        }

        viewModelScope.launch {
            val winnerStr = when (status) {
                GameStatus.X_WON -> "X"
                GameStatus.O_WON -> "O"
                else -> "TIE"
            }
            repository.insertMatch(
                GameMatchEntity(
                    mode = if (_gameMode.value == GameMode.PVP) "PVP" else "AI",
                    difficulty = if (_gameMode.value == GameMode.AI) _difficulty.value.name else "NONE",
                    winner = winnerStr
                )
            )
        }
    }

    fun resetGame() {
        _board.value = List(9) { null }
        _currentPlayer.value = Player.X
        _gameStatus.value = GameStatus.IN_PROGRESS
        _winningCombination.value = null
        _winningPlayer.value = null
        _isAiThinking.value = false
    }

    fun resetScores() {
        _scoreState.value = ScoreState()
        resetGame()
    }

    fun setGameMode(mode: GameMode) {
        if (_gameMode.value != mode) {
            _gameMode.value = mode
            resetScores()
        }
    }

    fun setDifficulty(diff: Difficulty) {
        if (_difficulty.value != diff) {
            _difficulty.value = diff
            resetGame()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}

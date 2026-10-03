package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: GameViewModel) {
    val board by viewModel.board.collectAsStateWithLifecycle()
    val currentPlayer by viewModel.currentPlayer.collectAsStateWithLifecycle()
    val gameStatus by viewModel.gameStatus.collectAsStateWithLifecycle()
    val winningCombination by viewModel.winningCombination.collectAsStateWithLifecycle()
    val gameMode by viewModel.gameMode.collectAsStateWithLifecycle()
    val difficulty by viewModel.difficulty.collectAsStateWithLifecycle()
    val scoreState by viewModel.scoreState.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val matchHistory by viewModel.matchHistory.collectAsStateWithLifecycle()
    val playerXName by viewModel.playerXName.collectAsStateWithLifecycle()
    val playerOName by viewModel.playerOName.collectAsStateWithLifecycle()
    val hapticsEnabled by viewModel.hapticsEnabled.collectAsStateWithLifecycle()

    var showStatsDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tic Tac Toe Pro",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            // AdMob Banner Ad Revenue Integration
            AdBanner()
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Scoreboard with custom names
            ScoreBoard(
                scoreState = scoreState,
                currentPlayer = currentPlayer,
                gameMode = gameMode,
                playerXName = playerXName,
                playerOName = playerOName
            )

            // Game Board Grid
            GameBoard(
                board = board,
                winningCombination = winningCombination,
                onCellClick = { index -> viewModel.makeMove(index) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
            )

            // Controls (Mode, Difficulty, Restart, Reset Scores)
            GameControls(
                gameMode = gameMode,
                difficulty = difficulty,
                isAiThinking = isAiThinking,
                onModeSelected = { mode -> viewModel.setGameMode(mode) },
                onDifficultySelected = { diff -> viewModel.setDifficulty(diff) },
                onRestart = { viewModel.resetGame() },
                onResetScores = { viewModel.resetScores() },
                onOpenStats = { showStatsDialog = true }
            )
        }

        // Win Dialog
        WinDialog(
            gameStatus = gameStatus,
            gameMode = gameMode,
            onPlayAgain = { viewModel.resetGame() }
        )

        // Stats Dialog
        if (showStatsDialog) {
            StatsDialog(
                matches = matchHistory,
                onClearHistory = { viewModel.clearHistory() },
                onDismiss = { showStatsDialog = false }
            )
        }

        // Settings Dialog
        if (showSettingsDialog) {
            SettingsDialog(
                playerXName = playerXName,
                playerOName = if (gameMode == com.example.model.GameMode.AI) "AI" else playerOName,
                hapticsEnabled = hapticsEnabled,
                onUpdateNames = { x, o -> viewModel.updatePlayerNames(x, o) },
                onToggleHaptics = { enabled -> viewModel.setHapticsEnabled(enabled) },
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}

package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.GameMode
import com.example.model.GameStatus
import com.example.model.Player

@Composable
fun WinDialog(
    gameStatus: GameStatus,
    gameMode: GameMode,
    onPlayAgain: () -> Unit
) {
    if (gameStatus == GameStatus.IN_PROGRESS) return

    val title = when (gameStatus) {
        GameStatus.X_WON -> "Player X Wins! 🎉"
        GameStatus.O_WON -> if (gameMode == GameMode.AI) "AI Wins! 🤖" else "Player O Wins! 🎉"
        GameStatus.TIE -> "It's a Tie! 🤝"
        else -> ""
    }

    Dialog(onDismissRequest = onPlayAgain) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Play Again",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

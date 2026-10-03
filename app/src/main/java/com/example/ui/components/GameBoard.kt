package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.model.Player
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SecondaryCoral

@Composable
fun GameBoard(
    board: List<Player?>,
    winningCombination: List<Int>?,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        val player = board[index]
                        val isWinningCell = winningCombination?.contains(index) == true

                        TicTacToeCell(
                            player = player,
                            isWinningCell = isWinningCell,
                            onClick = { onCellClick(index) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }

        // Winning Line Overlay
        if (winningCombination != null && winningCombination.size == 3) {
            WinningLineOverlay(winningCombination = winningCombination)
        }
    }
}

@Composable
fun TicTacToeCell(
    player: Player?,
    isWinningCell: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when {
        isWinningCell -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    }

    val borderColor = when {
        isWinningCell -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (player != null) {
            val scale by animateFloatAsState(
                targetValue = 1.0f,
                animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
                label = "cellScale"
            )

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.3f)
            ) {
                if (player == Player.X) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Player X",
                        tint = PrimaryCyan,
                        modifier = Modifier
                            .size(56.dp)
                            .scale(scale)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Player O",
                        tint = SecondaryCoral,
                        modifier = Modifier
                            .size(50.dp)
                            .scale(scale)
                    )
                }
            }
        }
    }
}

@Composable
fun WinningLineOverlay(winningCombination: List<Int>) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val cellWidth = width / 3f
        val cellHeight = height / 3f

        fun getCenter(index: Int): Offset {
            val row = index / 3
            val col = index % 3
            val x = col * cellWidth + cellWidth / 2f
            val y = row * cellHeight + cellHeight / 2f
            return Offset(x, y)
        }

        val start = getCenter(winningCombination.first())
        val end = getCenter(winningCombination.last())

        drawLine(
            color = primaryColor,
            start = start,
            end = end,
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
    }
}

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BoardStyle
import com.example.model.ColorPalette
import com.example.model.MarkerStyle
import com.example.model.Player

@Composable
fun GameBoard(
    board: List<Player?>,
    winningCombination: List<Int>?,
    colorPalette: ColorPalette,
    boardStyle: BoardStyle,
    markerStyle: MarkerStyle,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerBg = when (boardStyle) {
        BoardStyle.GLASS -> colorPalette.surface.copy(alpha = 0.85f)
        BoardStyle.NEON_GRID -> colorPalette.surface
        BoardStyle.SOLID_CARD -> colorPalette.surface
    }

    val borderWidth = when (boardStyle) {
        BoardStyle.GLASS -> 1.5.dp
        BoardStyle.NEON_GRID -> 2.dp
        BoardStyle.SOLID_CARD -> 1.dp
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(containerBg)
            .border(borderWidth, colorPalette.primary.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
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
                            colorPalette = colorPalette,
                            boardStyle = boardStyle,
                            markerStyle = markerStyle,
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
            WinningLineOverlay(
                winningCombination = winningCombination,
                primaryColor = colorPalette.primary
            )
        }
    }
}

@Composable
fun TicTacToeCell(
    player: Player?,
    isWinningCell: Boolean,
    colorPalette: ColorPalette,
    boardStyle: BoardStyle,
    markerStyle: MarkerStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cellBg = when {
        isWinningCell -> colorPalette.primary.copy(alpha = 0.35f)
        boardStyle == BoardStyle.GLASS -> colorPalette.background.copy(alpha = 0.5f)
        else -> colorPalette.background
    }

    val borderColor = when {
        isWinningCell -> colorPalette.primary
        else -> colorPalette.primary.copy(alpha = 0.2f)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(cellBg)
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

            val symbol = if (player == Player.X) markerStyle.xSymbol else markerStyle.oSymbol
            val tintColor = if (player == Player.X) colorPalette.primary else colorPalette.secondary

            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(initialScale = 0.3f)
            ) {
                if (markerStyle == MarkerStyle.CLASSIC) {
                    if (player == Player.X) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Player X",
                            tint = tintColor,
                            modifier = Modifier
                                .size(52.dp)
                                .scale(scale)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "Player O",
                            tint = tintColor,
                            modifier = Modifier
                                .size(46.dp)
                                .scale(scale)
                        )
                    }
                } else {
                    Text(
                        text = symbol,
                        fontSize = 36.sp,
                        modifier = Modifier.scale(scale)
                    )
                }
            }
        }
    }
}

@Composable
fun WinningLineOverlay(winningCombination: List<Int>, primaryColor: Color) {
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

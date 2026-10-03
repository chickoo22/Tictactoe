package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameMode
import com.example.model.Player
import com.example.model.ScoreState
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SecondaryCoral

@Composable
fun ScoreBoard(
    scoreState: ScoreState,
    currentPlayer: Player,
    gameMode: GameMode,
    playerXName: String = "Player X",
    playerOName: String = "Player O",
    modifier: Modifier = Modifier
) {
    val xActive = currentPlayer == Player.X
    val oActive = currentPlayer == Player.O

    val xScale by animateFloatAsState(targetValue = if (xActive) 1.05f else 1.0f, animationSpec = spring(), label = "xScale")
    val oScale by animateFloatAsState(targetValue = if (oActive) 1.05f else 1.0f, animationSpec = spring(), label = "oScale")

    val oLabel = if (gameMode == GameMode.AI) "AI (O)" else "$playerOName (O)"
    val xLabel = "$playerXName (X)"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Player X Card
        ScoreCard(
            title = xLabel,
            score = scoreState.xWins,
            isActive = xActive,
            activeColor = PrimaryCyan,
            scale = xScale,
            modifier = Modifier.weight(1f),
            testTag = "score_card_x"
        )

        // Ties Card
        Card(
            modifier = Modifier
                .weight(0.8f)
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Ties",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${scoreState.ties}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp
                )
            }
        }

        // Player O / AI Card
        ScoreCard(
            title = oLabel,
            score = scoreState.oWins,
            isActive = oActive,
            activeColor = SecondaryCoral,
            scale = oScale,
            modifier = Modifier.weight(1f),
            testTag = "score_card_o"
        )
    }
}

@Composable
fun ScoreCard(
    title: String,
    score: Int,
    isActive: Boolean,
    activeColor: androidx.compose.ui.graphics.Color,
    scale: Float,
    modifier: Modifier = Modifier,
    testTag: String
) {
    val borderColor = if (isActive) activeColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val borderWidth = if (isActive) 2.dp else 1.dp

    Card(
        modifier = modifier
            .height(84.dp)
            .scale(scale)
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) activeColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isActive) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$score",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isActive) activeColor else MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp
            )
        }
    }
}

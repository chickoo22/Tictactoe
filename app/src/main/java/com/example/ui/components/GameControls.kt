package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.Difficulty
import com.example.model.GameMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameControls(
    gameMode: GameMode,
    difficulty: Difficulty,
    isAiThinking: Boolean,
    onModeSelected: (GameMode) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
    onRestart: () -> Unit,
    onResetScores: () -> Unit,
    onOpenStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mode Selector & History Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // PVP Segmented Button
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.weight(1f)
            ) {
                SegmentedButton(
                    selected = gameMode == GameMode.PVP,
                    onClick = { onModeSelected(GameMode.PVP) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = { SegmentedButtonDefaults.Icon(gameMode == GameMode.PVP) }
                ) {
                    Text("2 Player")
                }
                SegmentedButton(
                    selected = gameMode == GameMode.AI,
                    onClick = { onModeSelected(GameMode.AI) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    icon = { SegmentedButtonDefaults.Icon(gameMode == GameMode.AI) }
                ) {
                    Text("vs Computer")
                }
            }

            // Stats Button
            IconButton(
                onClick = onOpenStats,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Match History & Stats",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Difficulty Chips (Visible only in AI mode)
        if (gameMode == GameMode.AI) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI Difficulty:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Difficulty.values().forEach { diff ->
                    FilterChip(
                        selected = difficulty == diff,
                        onClick = { onDifficultySelected(diff) },
                        label = { Text(diff.name.lowercase().capitalize()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            selectedLabelColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }

        // Action Buttons Row: Restart & Reset Scores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onRestart,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart Round",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAiThinking) "AI Thinking..." else "New Round",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            OutlinedButton(
                onClick = onResetScores,
                modifier = Modifier
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text("Reset Scores")
            }
        }
    }
}

private fun String.capitalize(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

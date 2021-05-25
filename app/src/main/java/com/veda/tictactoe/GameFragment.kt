package com.veda.tictactoe

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.veda.tictactoe.dto.CellStatus
import com.veda.tictactoe.viewmodels.GameViewModel
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.veda.tictactoe.dto.Player
import com.veda.tictactoe.ui.theme.CellBackground
import com.veda.tictactoe.ui.theme.SubtitleGray
import com.veda.tictactoe.viewmodels.observeAsStateNonNull
import java.lang.IllegalStateException


@ExperimentalFoundationApi
class GameFragment : BaseFragment() {

    private val gameViewModel: GameViewModel by viewModels()
    private val fieldSizes = (3..8).toList()

    @Composable
    override fun setContent() {
        val gameWinner = gameViewModel.gameResult.observeAsState()
        gameWinner.value?.let {
            ResultDialog(it)
        }

        val playerData = gameViewModel.currentPlayer.observeAsStateNonNull()
        GameContent(playerData.value)
    }

    @Composable
    private fun GameContent(player: Player) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                GameModeMenu()
                Spacer(modifier = Modifier.weight(1f))
                FieldSizeMenu()
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    gameViewModel.clearGame()
                }, modifier = Modifier.background(Color.Transparent)) {
                    Text(requireContext().getString(R.string.restart))
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.25f))
            GameField()
            Spacer(modifier = Modifier.fillMaxHeight(0.25f))
            val playerName = requireContext().getString(player.strRes)
            Text(
                text = requireContext().getString(R.string.current_turn, playerName),
                color = SubtitleGray,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    @Composable
    private fun GameField() {
        val fieldData = gameViewModel.gameField.observeAsState()
        fieldData.value?.let { status ->
            val size = gameViewModel.fieldSize.observeAsStateNonNull()
            GridFor(status, size.value) { cell ->
                GridItem(cell.status) {
                    gameViewModel.updateField(cell.ind)
                }
            }
        }
    }

    @Composable
    private fun GameModeMenu() {
        Row {
            val checkedState = remember { mutableStateOf(false) }
            Text(
                requireContext().getString(R.string.easy_bot),
                modifier = Modifier.padding(horizontal = 8.dp),
                color = SubtitleGray
            )
            Switch(
                checked = checkedState.value, onCheckedChange = {
                    checkedState.value = it
                    gameViewModel.setBotEnabled(it)
                }, colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                    uncheckedThumbColor = MaterialTheme.colors.primaryVariant
                )
            )
        }
    }

    @Composable
    private fun FieldSizeMenu() {
        val expanded = remember { mutableStateOf(false) }
        val selectedIndex = remember { mutableStateOf(0) }
        Column {
            Image(painter = painterResource(R.drawable.ic_outline_table),
                contentDescription = null,
                modifier = Modifier.size(30.dp).clickable { expanded.value = !expanded.value }
                    .border(
                        border = BorderStroke(1.dp, CellBackground),
                        shape = RoundedCornerShape(4.dp)
                    ))
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                fieldSizes.forEachIndexed { ind, size ->
                    DropdownMenuItem(onClick = {
                        selectedIndex.value = ind
                        expanded.value = false
                        gameViewModel.fieldSize.value = fieldSizes[ind]
                    }) {
                        Text(text = "$size")
                    }
                }
            }
        }
    }

    @Composable
    fun <T> GridFor(
        items: List<T> = listOf(),
        rows: Int,
        hPadding: Int = 26,
        itemContent: @Composable LazyItemScope.(Cell<T>) -> Unit
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(rows), modifier = Modifier.padding(horizontal = hPadding.dp)
        ) {
            itemsIndexed(items) { ind, item ->
                Box(
                    modifier = Modifier.layout { measurable, constraints ->
                        val tileSize = constraints.maxWidth
                        val placeable = measurable.measure(
                            constraints.copy(
                                minWidth = tileSize,
                                maxWidth = tileSize,
                                minHeight = tileSize,
                                maxHeight = tileSize,
                            )
                        )
                        layout(placeable.width, placeable.width) {
                            placeable.place(x = 0, y = 0, zIndex = 0f)
                        }
                    }, contentAlignment = Alignment.Center
                ) {
                    itemContent(Cell(ind, item))
                }
            }
        }
    }

    @Composable
    fun GridItem(status: CellStatus, onClick: () -> Unit) {
        val activeField = gameViewModel.fieldActive.observeAsStateNonNull()
        val clickable = status == CellStatus.EMPTY && activeField.value
        val modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(3.dp)
            .clickable(clickable, onClick = onClick).background(color = SubtitleGray)
        if (status != CellStatus.EMPTY) {
            val drawable = when (status) {
                CellStatus.TIC -> R.drawable.ic_cross
                CellStatus.TAC -> R.drawable.ic_circle
                CellStatus.EMPTY -> throw IllegalStateException("Illegal status: $status")
            }
            Image(
                painter = painterResource(drawable), contentDescription = null, modifier = modifier
            )
        } else {
            Box(modifier)
        }
    }

    @Composable
    private fun ResultDialog(result: GameViewModel.GameResult) {
        val context = requireContext()
        val resultText = if (result.winner == null) {
            context.getString(R.string.tie)
        } else {
            val winner = context.getString(result.winner.strRes)
            context.getString(R.string.win, winner)
        }
        AlertDialog(text = { Text(resultText, textAlign = TextAlign.Center) },
            buttons = {
                Row(
                    modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        gameViewModel.clearGame()
                    }) { Text(requireContext().getString(R.string.restart)) }
                }
            },
            modifier = Modifier.defaultMinSize(minWidth = 160.dp),
            onDismissRequest = { gameViewModel.clearGame() },
            shape = RoundedCornerShape(12.dp)
        )
    }

    data class Cell<T>(val ind: Int, val status: T)
}
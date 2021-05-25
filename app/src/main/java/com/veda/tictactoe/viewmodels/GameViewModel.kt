package com.veda.tictactoe.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veda.tictactoe.dto.CellStatus
import com.veda.tictactoe.dto.Player
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class GameViewModel internal constructor(
) : ViewModel() {

    val fieldSize: MutableLiveDataNonNull<Int> = MutableLiveDataNonNull(DEFAULT_FIELD_SIZE)

    val gameField: MediatorLiveData<MutableList<CellStatus>> by lazy {
        MediatorLiveData<MutableList<CellStatus>>().apply {
            addSource(fieldSize) { size -> gameField.value = fieldCells(size) }
            addSource(botEnabled) {
                gameField.value = fieldCells(fieldSize.value)
            }
        }
    }

    val currentPlayer: MutableLiveDataNonNull<Player> = MutableLiveDataNonNull(GameHelper.randomPlayer())

    val gameResult: MutableLiveData<GameResult> = MutableLiveData<GameResult>()

    val fieldActive: MutableLiveDataNonNull<Boolean> = MutableLiveDataNonNull(true)

    private val botEnabled: MutableLiveDataNonNull<Boolean> = MutableLiveDataNonNull(false)

    fun updateField(index: Int) {
        if (!fieldActive.value) return
        fieldActive.value = false

        val player = currentPlayer.value
        val newField = gameField.value ?: mutableListOf()
        newField.apply {
            set(index, player.toCellStatus())
        }
        gameField.value = newField
        val hasWinner = checkWinner(newField)

        val nextPlayer = GameHelper.nextPlayer(player)
        currentPlayer.value = nextPlayer

        if (!hasWinner && botEnabled.value) {
            makeBotMove(newField, nextPlayer)
        } else {
            fieldActive.value = true
        }
    }

    fun clearGame() {
        fieldSize.value = fieldSize.value
        gameResult.value = null
        currentPlayer.value = GameHelper.randomPlayer()
    }

    fun setBotEnabled(enabled: Boolean) {
        botEnabled.value = enabled
    }

    private fun checkWinner(newField: MutableList<CellStatus>): Boolean {
        val winner = getWinner(newField)
        if (winner != null) {
            gameResult.value = GameResult(winner)
            return true
        } else if (newField.none { it == CellStatus.EMPTY }) {
            gameResult.value = GameResult(null)
        }
        return false
    }

    private fun getWinner(field: MutableList<CellStatus>): Player? {
        val size = sqrt(field.size.toDouble()).toInt()
        return GameHelper.getWinner(field, size)
    }

    private fun fieldCells(size: Int): MutableList<CellStatus> {
        return MutableList(size * size) { CellStatus.EMPTY }
    }

    private fun makeBotMove(newField: MutableList<CellStatus>, nextPlayer: Player) {
        viewModelScope.launch {
            gameField.value = GameHelper.makeBotMove(newField, nextPlayer)
            checkWinner(newField)
            currentPlayer.value = GameHelper.nextPlayer(nextPlayer)
            fieldActive.value = true
        }
    }

    data class GameResult(val winner: Player?)

    companion object {
        private const val DEFAULT_FIELD_SIZE = 3
    }
}


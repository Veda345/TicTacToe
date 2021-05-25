package com.veda.tictactoe.viewmodels

import com.veda.tictactoe.dto.CellStatus
import com.veda.tictactoe.dto.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

object GameHelper {

    private val random = Random(200)

    suspend fun makeBotMove(field: List<CellStatus>, player: Player): MutableList<CellStatus> {
        return withContext(Dispatchers.IO) {
            val freeCells = field.mapIndexed { ind, it -> ind to it }.filter { it.second == CellStatus.EMPTY }
            if (freeCells.isEmpty()) return@withContext field.toMutableList()

            //Imitate hard work
            delay(300)

            val ind = random.nextInt(freeCells.count())
            val newField = field.toMutableList()
            newField[freeCells[ind].first] = player.toCellStatus()
            newField
        }
    }

    fun randomPlayer(): Player {
        return if (random.nextBoolean()) Player.TIC else Player.TAC
    }

    fun nextPlayer(player: Player): Player {
        return when (player) {
            Player.TIC -> Player.TAC
            Player.TAC -> Player.TIC
        }
    }

    fun getWinner(field: MutableList<CellStatus>, size: Int): Player? {
        return getWinningRow(field, size) ?: getWinningColumn(field, size) ?: getWinningDiagonal(field, size)
    }

    private fun getWinningRow(field: MutableList<CellStatus>, size: Int): Player? {
        val rows = field.chunked(size)
        val winningRow = rows.filter { row -> row.all { it != CellStatus.EMPTY && it == row[0] } }
        if (winningRow.isNotEmpty()) {
            return winningRow[0][0].toPlayer()
        }
        return null
    }

    private fun getWinningColumn(field: MutableList<CellStatus>, size: Int): Player? {
        for (j in 0 until size) {
            var equality = field[j] != CellStatus.EMPTY
            for (i in 1 until size) {
                equality = equality && (field[i * size + j] == field[j])
            }
            if (equality) return field[j].toPlayer()
        }
        return null
    }

    private fun getWinningDiagonal(field: MutableList<CellStatus>, size: Int): Player? {
        var equality = field[0] != CellStatus.EMPTY
        for (i in 0 until size) {
            equality = equality && (field[i * size + i] == field[0])
        }
        if (equality) return field[0].toPlayer()

        equality = field[size - 1] != CellStatus.EMPTY
        for (i in 0 until size) {
            equality = equality && field[i * size + size - i - 1] == field[size - 1]
        }
        if (equality) return field[size - 1].toPlayer()
        return null
    }

}
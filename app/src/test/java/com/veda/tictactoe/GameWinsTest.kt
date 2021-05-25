package com.veda.tictactoe

import com.veda.tictactoe.dto.CellStatus
import com.veda.tictactoe.dto.Player
import com.veda.tictactoe.viewmodels.GameHelper
import org.junit.Test

import org.junit.Assert.*

/**
 * Test winner detection.
 */
class GameWinsTest {

    @Test
    fun testEmptyBoard() {
        assertEquals(null, GameHelper.getWinner(BOARD1, 3))
    }

    @Test
    fun testDiagonalBoard() {
        assertEquals(Player.TIC, GameHelper.getWinner(DIAG_BOARD1, 3))
        assertEquals(Player.TAC, GameHelper.getWinner(DIAG_BOARD2, 3))
    }

    @Test
    fun testRowBoard() {
        assertEquals(Player.TIC, GameHelper.getWinner(ROW_BOARD1, 4))
        assertEquals(Player.TAC, GameHelper.getWinner(ROW_BOARD2, 3))
    }

    @Test
    fun testColumnBoard() {
        assertEquals(Player.TIC, GameHelper.getWinner(COLUMN_BOARD1, 3))
        assertEquals(Player.TAC, GameHelper.getWinner(COLUMN_BOARD2, 3))
    }

    @Test
    fun testFullBoard() {
        assertEquals(Player.TAC, GameHelper.getWinner(FULL_BOARD, 3))
    }

    @Test
    fun testTieBoard() {
        assertEquals(null, GameHelper.getWinner(TIE_BOARD, 3))
    }

    companion object {
        private val BOARD1 = mutableListOf(
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY)

        private val DIAG_BOARD1 = mutableListOf(
            CellStatus.TIC, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.EMPTY, CellStatus.TIC, CellStatus.EMPTY,
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.TIC)

        private val DIAG_BOARD2 = mutableListOf(
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.TAC,
            CellStatus.EMPTY, CellStatus.TAC, CellStatus.EMPTY,
            CellStatus.TAC, CellStatus.EMPTY, CellStatus.EMPTY)

        private val ROW_BOARD1 = mutableListOf(
            CellStatus.TIC, CellStatus.TIC, CellStatus.TIC, CellStatus.TIC,
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.EMPTY, CellStatus.TAC, CellStatus.EMPTY, CellStatus.TAC)

        private val ROW_BOARD2 = mutableListOf(
            CellStatus.EMPTY, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.TIC, CellStatus.TIC, CellStatus.EMPTY,
            CellStatus.TAC, CellStatus.TAC, CellStatus.TAC)

        private val COLUMN_BOARD1 = mutableListOf(
            CellStatus.TIC, CellStatus.EMPTY, CellStatus.EMPTY,
            CellStatus.TIC, CellStatus.TAC, CellStatus.EMPTY,
            CellStatus.TIC, CellStatus.EMPTY, CellStatus.TAC)

        private val COLUMN_BOARD2 = mutableListOf(
            CellStatus.TIC, CellStatus.TAC, CellStatus.TIC,
            CellStatus.EMPTY, CellStatus.TAC, CellStatus.EMPTY,
            CellStatus.TIC, CellStatus.TAC, CellStatus.TAC)

        private val FULL_BOARD = mutableListOf(
            CellStatus.TAC, CellStatus.TIC, CellStatus.TAC,
            CellStatus.TAC, CellStatus.TAC, CellStatus.TIC,
            CellStatus.TAC, CellStatus.TIC, CellStatus.TIC)

        private val TIE_BOARD = mutableListOf(
            CellStatus.TAC, CellStatus.TIC, CellStatus.TAC,
            CellStatus.TIC, CellStatus.TAC, CellStatus.TIC,
            CellStatus.TIC, CellStatus.TAC, CellStatus.TIC)
    }
}
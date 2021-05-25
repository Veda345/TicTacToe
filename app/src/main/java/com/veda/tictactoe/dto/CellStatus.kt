package com.veda.tictactoe.dto

enum class CellStatus {
    EMPTY, TIC, TAC;

    fun toPlayer(): Player? {
        return when (this) {
            TIC -> Player.TIC
            TAC -> Player.TAC
            EMPTY -> null
        }
    }
}
package com.veda.tictactoe.dto

import com.veda.tictactoe.R

enum class Player(val strRes: Int) {
    TIC(R.string.tic_name), TAC(R.string.tac_name);

    fun toCellStatus(): CellStatus {
        return when (this) {
            TIC -> CellStatus.TIC
            TAC -> CellStatus.TAC
        }
    }
}
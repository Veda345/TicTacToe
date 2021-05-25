package com.veda.tictactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.veda.tictactoe.ui.theme.TicTacToeTheme

abstract class BaseFragment : Fragment() {

    @Composable
    open fun setContent() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                TicTacToeTheme {
                    Surface(color = MaterialTheme.colors.surface) {
                        Box(modifier = Modifier.background(Color(ContextCompat.getColor(requireContext(), R.color.bg)))) {
                            setContent()
                        }
                    }
                }
            }
        }
    }
}
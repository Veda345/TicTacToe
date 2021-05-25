package com.veda.tictactoe.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData

class MutableLiveDataNonNull<T>(private val initialValue: T) : MutableLiveData<T>(initialValue) {

    override fun getValue(): T { //the only way value can be null is if the value hasn't been set yet.
        //for the other cases the set and post methods perform nullability checks.
        val value = super.getValue()
        return value ?: initialValue
    }
}

@Composable
fun <T> MutableLiveDataNonNull<T>.observeAsStateNonNull(): State<T> = observeAsState(value)
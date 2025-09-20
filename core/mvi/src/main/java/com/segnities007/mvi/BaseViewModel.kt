package com.segnities007.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<
        S : ViewState,
        I : ViewIntent,
        E : ViewEffect,
        R : ViewResult,
        Reducer : ViewReducer<S, R>
        >(
    initialState: S,
    private val reducer: Reducer //only func to update state.
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<E>(
        replay = 0,
        extraBufferCapacity = 16,
    )
    val sideEffect = _sideEffect.asSharedFlow()

    fun onIntent(intent: I) {
        viewModelScope.launch {
            val result = handleIntent(intent)
            dispatch(result)
        }
    }

    protected abstract suspend fun handleIntent(intent: I): R

    protected fun dispatch(result: R) {
        _state.update { current -> reducer.reduce(current, result) }
    }

    protected fun emitEffect(effect: E) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }
}

package com.segnities007.mvi

fun interface ViewReducer<S : ViewState, R : ViewResult> {
    fun reduce(state: S, result: R): S
}
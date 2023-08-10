package com.daniza.simple.todolist.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

fun <T> Flow<T>.asResult() : Flow<Result<T>> {
    return this.onStart {
        Result.Loading
    }.catch {
        Result.Error(it)
    }.map {
        Result.Success(it)
    }
}


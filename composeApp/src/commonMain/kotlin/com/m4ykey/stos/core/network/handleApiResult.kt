package com.m4ykey.stos.core.network

inline fun <T> handleApiResult(
    result : ApiResult<T>,
    success : (T) -> Unit,
    failure : (String?) -> Unit
) {
    when (result) {
        is ApiResult.Success -> success(result.data)
        is ApiResult.Failure -> failure(result.exception.message)
    }
}

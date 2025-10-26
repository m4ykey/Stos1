package com.m4ykey.stos.question.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.stos.core.network.handleApiResult
import com.m4ykey.stos.question.domain.usecase.QuestionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionDetailViewModel(
    private val useCase: QuestionUseCase
) : ViewModel() {

    private val _questionDetailState = MutableStateFlow(QuestionDetailState())
    val questionDetailState = _questionDetailState.asStateFlow()

    fun loadQuestionDetail(id: Int) {
        _questionDetailState.update {
            it.copy(
                loading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            useCase.getQuestionById(id)
                .catch { exception ->
                    _questionDetailState.update {
                        it.copy(
                            loading = false,
                            errorMessage = exception.message
                        )
                    }
                }
                .collect { result ->
                    handleApiResult(
                        result = result,
                        success = { data ->
                            _questionDetailState.update {
                                it.copy(
                                    loading = false,
                                    question = data
                                )
                            }
                        },
                        failure = { msg ->
                            _questionDetailState.update {
                                it.copy(
                                    loading = false,
                                    question = null,
                                    errorMessage = msg
                                )
                            }
                        }
                    )
                }
        }
    }
}
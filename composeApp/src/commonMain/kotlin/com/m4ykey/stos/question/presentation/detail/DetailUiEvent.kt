package com.m4ykey.stos.question.presentation.detail

interface DetailUiEvent {
    data class TagClick(val tag : String) : DetailUiEvent
}
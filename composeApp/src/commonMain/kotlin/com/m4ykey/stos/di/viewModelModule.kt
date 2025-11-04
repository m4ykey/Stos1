package com.m4ykey.stos.di

import com.m4ykey.stos.question.presentation.detail.QuestionDetailViewModel
import com.m4ykey.stos.question.presentation.list.QuestionListViewModel
import com.m4ykey.stos.question.presentation.tag.QuestionTagViewModel
import com.m4ykey.stos.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { QuestionListViewModel(get()) }
    viewModel { QuestionDetailViewModel(get()) }
    viewModel { QuestionTagViewModel(get()) }
    viewModel { SearchViewModel() }
}
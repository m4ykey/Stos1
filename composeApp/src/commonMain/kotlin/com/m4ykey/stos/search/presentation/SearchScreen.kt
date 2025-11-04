package com.m4ykey.stos.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.stos.question.presentation.detail.TagListWrap
import com.m4ykey.stos.question.presentation.list.ListUiEvent
import kmp_stos.composeapp.generated.resources.Res
import kmp_stos.composeapp.generated.resources.back
import kmp_stos.composeapp.generated.resources.popular_tags
import kmp_stos.composeapp.generated.resources.search
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onSearchScreen : (String, String) -> Unit,
    onBack : (() -> Unit)
) {

    var inTitle by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.listUiEvent.collectLatest { event ->
            when (event) {
                is ListUiEvent.NavigateToSearch -> onSearchScreen(event.inTitle, event.tag)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                title = { Text(stringResource(Res.string.search)) }
            )
        }
    ) { padding ->
        SearchContent(
            padding = padding,
            listState = listState,
            inTitle = inTitle,
            onSearch = {
                SearchListAction.OnSearchClick(inTitle = inTitle, tag = "")
            },
            onInTitleChange = { inTitle = it },
            onTagClick = { clickedTag ->
                viewModel.onAction(SearchListAction.OnSearchClick(inTitle = inTitle, tag = clickedTag))
            }
        )
    }
}

val mobileTags = listOf(
    "android-studio", "android-jetpack-compose", "xcode", "react-native", "flutter", "material-ui"
)
val databaseTags = listOf(
    "sql", "mysql", "postgresql", "mongodb", "sqlite", "oracle"
)
val testTags = listOf(
    "junit", "selenium", "cypress", "github-actions"
)
val cloudTags = listOf(
    "docker", "kubernetes", "aws", "azure", "firebase", "jenkins", "terraform"
)
val frameworksTags = listOf(
    "angular", "vue.js", "spring", "flask", "django", "express", "laravel", "bootstrap", "tensorflow", "pandas", "numpy"
)
val languageTags = listOf(
    "typescript", "c++", "swift", "ruby", "go", "kotlin", "r", "rust", "scala", "dart", "bash", "objective-c", "c"
)
val allTags = mobileTags + databaseTags + testTags + cloudTags + frameworksTags + languageTags

@Composable
fun SearchContent(
    padding : PaddingValues,
    listState : LazyListState,
    inTitle : String,
    onInTitleChange : (String) -> Unit,
    onSearch : () -> Unit,
    onTagClick : (String) -> Unit
) {
    val shuffledTags = remember { allTags.shuffled() }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(paddingValues = padding)
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            SearchBox(
                value = inTitle,
                onValueChange = onInTitleChange,
                onSearch = onSearch
            )
        }
        item {
            Text(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                text = stringResource(Res.string.popular_tags)
            )
        }
        item {
            TagListWrap(
                tags = shuffledTags,
                onTagClick = onTagClick
            )
        }
    }
}

@Composable
fun SearchBox(
    modifier : Modifier = Modifier,
    value : String,
    onValueChange : (String) -> Unit,
    onSearch : () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyUp && event.key == Key.Enter) {
                    onSearch()
                    true
                } else {
                    false
                }
            },
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                contentDescription = null,
                imageVector = Icons.Default.Search
            )
        },
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        contentDescription = null,
                        imageVector = Icons.Default.Close
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        placeholder = { Text(stringResource(Res.string.search) + "..." ) }
    )
}
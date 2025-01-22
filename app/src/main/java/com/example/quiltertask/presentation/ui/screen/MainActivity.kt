package com.example.quiltertask.presentation.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.quiltertask.domain.model.Book
import com.example.quiltertask.presentation.state.BookUiState
import com.example.quiltertask.presentation.ui.theme.QuilterTaskTheme
import com.example.quiltertask.presentation.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuilterTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->
                    val viewModel = hiltViewModel<BookViewModel>()
                    val uiState by viewModel.bookUiState.collectAsStateWithLifecycle()
                    BookListScreen(
                        uiState = uiState,
                        modifier = Modifier.padding(innerpadding)
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuilterTaskTheme {
        val mockTrendingMovies = listOf(
            Book("Hello one dgasfjoisjdfijasojdfjasojfoasjodfjoasjdfoijasoj", "Movie 1", "dfa"),
            Book("Hello2", "Movie 2", "test"),
        )
        val state = BookUiState.Success(mockTrendingMovies)
        BookListScreen(uiState = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    uiState: BookUiState<List<Book?>?>,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedBook by remember { mutableStateOf<Book?>(null) }

    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                showBottomSheet = false
            }
        }
        lifeCycle.addObserver(observer)
        onDispose {
            lifeCycle.removeObserver(observer)
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp
        ) {
            selectedBook?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    BookDetailSheet(book = it)
                }
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = 32.dp,
                horizontal = 8.dp
            )
    ) {
        when (uiState) {
            is BookUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }

            is BookUiState.Success -> {
                val books = uiState.data ?: emptyList()
                LazyColumn {
                    itemsIndexed(books) { _, book ->
                        BookItem(book = book, onClick = {
                            selectedBook = book
                            showBottomSheet = true
                        }
                        )
                        HorizontalDivider()
                    }
                }
            }

            is BookUiState.Error -> {
                val message =
                    uiState.message.asString()
                Text(text = message, color = MaterialTheme.colorScheme.error)

            }
        }

    }

}

@Composable
fun BookItem(book: Book?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = book?.coverID,
            contentDescription = "Book Cover",
            modifier = Modifier.size(120.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = book?.title ?: "No Title",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book?.author ?: "Unknown Author",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BookDetailSheet(book: Book) {
    book.coverID.let { coverUrl ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = coverUrl,
                contentDescription = "Book Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Author: ${book.author}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}


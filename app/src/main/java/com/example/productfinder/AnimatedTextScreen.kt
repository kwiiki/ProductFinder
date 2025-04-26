package com.example.productfinder

import androidx.compose.animation.core.EaseInOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productfinder.movingletters.AnimatedTextState
import com.example.productfinder.movingletters.FadeAnimatedText
import com.example.productfinder.movingletters.rememberAnimatedTextState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AnimatedTextScreen(
    content: @Composable (state: AnimatedTextState, text: String) -> Unit,
    contentColor: Color,
    containerColor: Color
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Scaffold(
            contentColor = contentColor, containerColor = containerColor
        ) { padding ->
            val text = "Подождите немного!"
            val state = rememberAnimatedTextState()

            LaunchedEffect("AnimatedTextScreen", Dispatchers.IO) {
                delay(200L)
                state.start()
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding() + 64.dp,
                    bottom = padding.calculateBottomPadding() + 64.dp,
                    end = padding.calculateEndPadding(LocalLayoutDirection.current) + 32.dp,
                    start = padding.calculateStartPadding(LocalLayoutDirection.current) + 32.dp
                )
            ) {
                item {
                    content(state, text)
                }
                item {
                    Button(onClick = { state.start() }) {
                        Text("Начать")
                    }
                }
            }
        }
    }
}




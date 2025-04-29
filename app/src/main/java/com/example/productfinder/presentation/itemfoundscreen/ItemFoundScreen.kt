@file:Suppress("MISSING_DEPENDENCY_CLASS_IN_EXPRESSION_TYPE")

package com.example.productfinder.presentation.itemfoundscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.EaseInOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.itemfinder.core.navigation.NavPath
import com.example.productfinder.presentation.homescreen.viewmodel.HomeScreenViewModel
import com.example.productfinder.R
import com.example.productfinder.data.Product
import com.example.productfinder.movingletters.AnimatedTextState
import com.example.productfinder.movingletters.FadeAnimatedText
import com.example.productfinder.movingletters.rememberAnimatedTextState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.*


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemFoundScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
) {
    val productsUiState by homeScreenViewModel.productsState.collectAsState()
    val showErrorBar = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (productsUiState) {
            is ProductsUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieLoading()
                }
            }

            is ProductsUiState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ПУСТО")
                }
            }

            is ProductsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            showErrorBar.value = false
                            homeScreenViewModel.clearError()
                            navController.popBackStack()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val errorMessage = (productsUiState as ProductsUiState.Error).message

                    Text(
                        text = "Повторите попытку позже",
                        color = Color.White,
                    )
                }
            }

            is ProductsUiState.Success -> {
                val listState = rememberLazyListState()
                val products = (productsUiState as ProductsUiState.Success).products

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                    state = listState
                ) {
                    items(products) { product ->
                        ProductItem(
                            product = product,
                            homeScreenViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LottieLoading(
    modifier: Modifier = Modifier,
    rawResId: Int = R.raw.lottie_loading
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawResId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(450.dp)
    )
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    messages: List<String> = listOf(
        "Ищем в Kaspi…",
        "Ищем в Ozon…",
        "Ищем в Wildberries…"
    ),
    cycleMillis: Long = 2_600L      // ≥ animationDuration
) {
    var index by remember { mutableStateOf(0) }
    val message = messages[index]

    // Переключаемся на следующую строку через заданный интервал
    LaunchedEffect(message) {
        delay(cycleMillis)
        index = (index + 1) % messages.size
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        key(message) {
            FadeAnimatedText(
                text = message,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                animationDuration = 2_000.milliseconds,
                intermediateDuration = 140.milliseconds,
                easing = EaseInOut,
                animateOnMount = true
            )
        }
    }
}


@Composable
fun ProductItem(
    product: Product,
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val logoTitleFont = FontFamily(Font(R.font.montserrat_black))
    val titleFont = FontFamily(Font(R.font.montserrat_medium))
    Card(
        modifier = modifier.clickable {
            product.link.let {
                homeScreenViewModel.saveRecentProductInDB(product)
                val encodedLink = Uri.encode(product.link)
                navController.navigate("${NavPath.OpenInWebViewScreen.name}/$encodedLink")
            }

        },
        elevation = CardDefaults.cardElevation(16.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.White.copy(alpha = 0.2f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = product.logoUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                    Text(
                        text = product.source.toString(),
                        fontSize = 16.sp,
                        fontFamily = logoTitleFont,
                        fontWeight = FontWeight.W400,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(R.drawable.share_android),
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Посмотри этот товар!")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                product.link ?: ""
                            )
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Поделиться через")
                        )
                    }
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(product.imageLink)
                        .build(),
                    contentDescription = "product",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = product.title ?: "1w",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        color = Color.White,
                        fontFamily = titleFont,
                        overflow = TextOverflow.Ellipsis
                    )
                    RatingBar(
                        modifier = Modifier
                            .size(16.dp),
                        rating = product.rating,
                        onRatingChanged = {
                        },
                        starsColor = Color(0xffDA6600)
                    )
                    product.price?.let {
                        val displayPrice = it.substringBefore(',')
                        Text(
                            text = "$displayPrice ₸",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontFamily = logoTitleFont,
                            fontWeight = FontWeight.W500
                        )
                    }
                    if (product.freeDelivery) {
                        Text(
                            text = "Бесплатная доставка",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = titleFont,
                            color = Color.Green
                        )
                    }
                }
            }
        }
    }
}


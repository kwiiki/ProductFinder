@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@file:Suppress("DEPRECATION")

package com.example.productfinder.presentation.homescreen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.itemfinder.core.navigation.NavPath
import com.example.productfinder.presentation.homescreen.viewmodel.HomeScreenViewModel
import com.example.productfinder.R
import com.example.productfinder.data.MarketPlaces
import com.example.productfinder.data.db.entity.ProductEntity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import com.example.productfinder.presentation.listOfMarketPlaces
import com.example.productfinder.presentation.products
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel
) {
    var openCameraSheet by remember { mutableStateOf(false) }
    val cameraSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val recentProducts by homeScreenViewModel.recentProducts.collectAsState()
    var textForSearch by remember { mutableStateOf("Поиск") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeScreenViewModel.getRecentProducts()
    }

    val imageCropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let { uri ->
                    homeScreenViewModel.uploadImage(uri, context)
                    navController.navigate(NavPath.ItemFoundScreen.name)
                }
            } else {
                Log.e("CropImage", "Ошибка обрезки: ${result.error}")
            }
        }

    val speechLauncher =
        rememberLauncherForActivityResult(contract = SpeechRecognitionContract()) { recognizedText ->
            recognizedText?.let {
                textForSearch = it
                homeScreenViewModel.searchByText(it)
                navController.navigate(NavPath.ItemFoundScreen.name)
            }
        }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isWide = maxWidth > 600.dp
        val scrollState = rememberScrollState()

        if (isWide) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp)
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = textForSearch, fontSize = 16.sp, color = Color.White)
                        }
                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.micro_icon),
                                contentDescription = null,
                                tint = Color(0xFFDA6600),
                                modifier = Modifier.clickable { speechLauncher.launch(null) }
                            )
                            Spacer(
                                Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Color.White)
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.camera_icon),
                                contentDescription = null,
                                tint = Color(0xFFDA6600),
                                modifier = Modifier.clickable { openCameraSheet = true }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Популярные запросы:", fontSize = 16.sp, color = Color.White)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { navController.navigate(NavPath.FilterScreen.name) }
                        ) {
                            Text("Фильтры", fontSize = 14.sp, color = Color.White)
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(R.drawable.filter_icon),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                        }
                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        products.forEach { query ->
                            FilterButton(
                                text = query,
                                homeScreenViewModel = homeScreenViewModel,
                                navController = navController
                            ) { textForSearch = query }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (recentProducts.isNotEmpty()) {
                        Text("Вы недавно смотрели:", fontSize = 16.sp, color = Color.White)
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val columns = 3                       // сколько карточек хотим в ряду
                            val spacing = 12.dp                    // Arrangement.spacedBy(...)

                            val baseWidth =
                                (maxWidth - spacing * (columns - 1)) / columns  // columns = 3
                            val itemWidth = baseWidth * 1.15f
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(spacing),
                            ) {
                                items(recentProducts.reversed()) { product ->
                                    ProductItem(
                                        item = product,
                                        modifier = Modifier.width(itemWidth),
                                        navController
                                    )
                                }
                            }
                        }
                    }
                    Text("Где мы ищем:", fontSize = 16.sp, color = Color.White)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOfMarketPlaces.forEach { market ->
                            AsyncImage(
                                model = market.linkForLogoMarkerPlace,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize(0.21f)
                                    .clip(CircleShape)
                                    .background(color = Color.White)
                                    .clickable {
                                        val encoded = Uri.encode(market.linkForMarkerPlace)
                                        navController.navigate("${NavPath.OpenInWebViewScreen.name}/$encoded")
                                    }
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 50.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(text = textForSearch, fontSize = 16.sp, color = Color.White)
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.micro_icon),
                            contentDescription = null,
                            tint = Color(0xFFDA6600),
                            modifier = Modifier.clickable { speechLauncher.launch(null) }
                        )
                        Spacer(
                            Modifier
                                .width(1.dp)
                                .height(24.dp)
                                .background(Color.White)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.camera_icon),
                            contentDescription = null,
                            tint = Color(0xFFDA6600),
                            modifier = Modifier.clickable { openCameraSheet = true }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Популярные запросы:", fontSize = 16.sp, color = Color.White)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.navigate(NavPath.FilterScreen.name) }
                    ) {
                        Text("Фильтры", fontSize = 14.sp, color = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(R.drawable.filter_icon),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                    }
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    products.forEach { query ->
                        FilterButton(
                            text = query,
                            homeScreenViewModel = homeScreenViewModel,
                            navController = navController
                        ) { textForSearch = query }
                    }
                }

                if (recentProducts.isNotEmpty()) {
                    Text("Вы недавно смотрели:", fontSize = 16.sp, color = Color.White)
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val columns = 3                       // сколько карточек хотим в ряду
                        val spacing = 12.dp                    // Arrangement.spacedBy(...)

                        val baseWidth =
                            (maxWidth - spacing * (columns - 1)) / columns  // columns = 3
                        val itemWidth = baseWidth * 1.15f
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                        ) {
                            items(recentProducts.reversed()) { product ->
                                ProductItem(
                                    item = product,
                                    modifier = Modifier.width(itemWidth),
                                    navController
                                )
                            }
                        }
                    }
                }

                Text("Где мы ищем:", fontSize = 16.sp, color = Color.White)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOfMarketPlaces.forEach { market ->
                        AsyncImage(
                            model = market.linkForLogoMarkerPlace,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize(0.21f)
                                .clip(CircleShape)
                                .background(color = Color.White)
                                .clickable {
                                    val encoded = Uri.encode(market.linkForMarkerPlace)
                                    navController.navigate("${NavPath.OpenInWebViewScreen.name}/$encoded")
                                }
                        )
                    }
                }
            }
        }
    }
    if (openCameraSheet) {
        ModalBottomSheet(
            sheetState = cameraSheetState,
            onDismissRequest = { openCameraSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CustomButtonWithoutImageVector(
                    text = "Сделать фото",
                    onClick = {
                        openCameraSheet = false
                        imageCropLauncher.launch(
                            CropImageContractOptions(
                                null,
                                CropImageOptions(imageSourceIncludeGallery = false)
                            )
                        )
                    }
                )
                CustomButtonWithoutImageVector(
                    text = "Выбрать из галереи",
                    onClick = {
                        openCameraSheet = false
                        imageCropLauncher.launch(
                            CropImageContractOptions(
                                null,
                                CropImageOptions(imageSourceIncludeCamera = false)
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    item: ProductEntity,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Card(
        modifier = modifier
            .height(210.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable {
                item.linkForMarket.let {
                    val encodedLink = Uri.encode(item.linkForMarket)
                    navController.navigate("${NavPath.OpenInWebViewScreen.name}/$encodedLink")
                }
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            AsyncImage(
                model = item.imageLink,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)                    // фикс‑высота
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                item.price?.let {
                    val displayPrice = it.substringBefore(',')
                    Text(
                        text = "$displayPrice ₸",
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp,
                        color = Color(0xFFDA6600)
                    )
                }
            }
        }
    }
}

fun String.formatAsPrice(): String {
    val value = this.replace(" ", "")         // убираем возможные пробелы
    val number = value.toDoubleOrNull() ?: return this

    val symbols = DecimalFormatSymbols(Locale("ru")).apply {
        groupingSeparator = ' '               // пробел‑разделитель тысяч
        decimalSeparator = ','                // не важен, обнуляем дробь
    }
    val fmt = DecimalFormat("#,###", symbols) // без десятичных знаков
    return fmt.format(number)                 // "32 000"
}


@Composable
fun FilterButton(
    text: String,
    homeScreenViewModel: HomeScreenViewModel,
    navController: NavHostController,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(42.dp)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onClick()
                navController.navigate(NavPath.ItemFoundScreen.name)
                homeScreenViewModel.searchByText(text)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomButtonWithoutImageVector(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {

    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = Color(0xFFDA6600)),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 8.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0xFFDA6600), RoundedCornerShape(12.dp))
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}




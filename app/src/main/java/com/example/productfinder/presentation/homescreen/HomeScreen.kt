@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@file:Suppress("DEPRECATION")

package com.example.productfinder.presentation.homescreen

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

val products = listOf(
    "iPhone 13",
    "Samsung ",
    "Google Pixel 6",
    "OnePlus 9",
    "Xiaomi Mi 11",
    "Huawei P40",
    "Sony Xperia 5"
)
val listOfMarketPlaces =
    listOf(
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://olx.kz&size=256",
            linkForMarkerPlace = "https://www.olx.kz/"
        ),
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://kaspi.kz&size=256",
            linkForMarkerPlace = "https://kaspi.kz/"
        ),
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://flip.kz&size=256",
            linkForMarkerPlace = "https://www.flip.kz/"
        ),
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://forte.kz&size=256",
            linkForMarkerPlace = "https://market.forte.kz/"
        ),
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://ozon.kz&size=256",
            linkForMarkerPlace = "https://ozon.kz/"
        ),
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://satu.kz&size=256",
            linkForMarkerPlace = "https://satu.kz/"
        ),
        MarketPlaces(
            linkForLogoMarkerPlace = "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://lamoda.kz&size=256",
            linkForMarkerPlace = "https://www.lamoda.kz/"
        )
    )

@Composable
fun HomeScreen(navController: NavHostController, homeScreenViewModel: HomeScreenViewModel) {

    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var openFilterBottomSheet by remember { mutableStateOf(false) }
    val filterBottomState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val recentProducts by homeScreenViewModel.recentProducts.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(recentProducts) {
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
                Log.d("textTest", "HomeScreen: $it")
                homeScreenViewModel.searchByText(it)
                navController.navigate(NavPath.ItemFoundScreen.name)
            }
        }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(alignment = Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "", tint = Color.White)
                Text(
                    text = "Поиск",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.White
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(alignment = Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.micro_icon),
                    contentDescription = "",
                    tint = Color(0xFFDA6600),
                    modifier = Modifier.clickable {
                        speechLauncher.launch(null)
                    }
                )
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(color = Color.White)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.camera_icon),
                    contentDescription = "",
                    tint = Color(0xFFDA6600),
                    modifier = Modifier.clickable {
                        openBottomSheet = true
                    }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Популярные запросы:",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.W500
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    openFilterBottomSheet = true
                }) {
                Text(
                    text = "Фильтры",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
                Image(
                    painter = painterResource(R.drawable.filter_icon),
                    contentDescription = "",
                    modifier = Modifier.size(18.dp)
                )

            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            products.forEach { product ->
                FilterButton(text = product)
            }
        }
        Text(
            text = "Вы недавно смотрели:",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.W500
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), reverseLayout = true) {
            items(recentProducts) { item ->
                ProductItem(item)
            }
        }
        Text(
            text = "Где мы ищем:",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.W500
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOfMarketPlaces.forEach {
                AsyncImage(
                    model = it.linkForLogoMarkerPlace,
                    contentDescription = "",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            it.linkForMarkerPlace.let {
                                val encodedLink = Uri.encode(it)
                                navController.navigate("${NavPath.OpenInWebViewScreen.name}/$encodedLink")
                            }
                        }
                )
            }

        }
    }

    if (openFilterBottomSheet) {
        ModalBottomSheet(
            tonalElevation = 20.dp,
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.secondary,
            sheetState = filterBottomState,
            scrimColor = Color.White.copy(alpha = 0.2f),
            onDismissRequest = { openFilterBottomSheet = false },
        ) {
            FilterBottomSheet(
                homeScreenViewModel = homeScreenViewModel,
                onDismissRequest = { openBottomSheet = false }
            )
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            tonalElevation = 20.dp,
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.secondary,
            sheetState = bottomState,
            scrimColor = Color.White.copy(alpha = 0.2f),
            onDismissRequest = { openBottomSheet = false },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .height(160.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CustomButtonWithoutImageVector(
                    text = "Сделать фото",
                    onClick = {
                        openBottomSheet = false
                        val cropOptions = CropImageContractOptions(
                            null,
                            CropImageOptions(imageSourceIncludeGallery = false)
                        )
                        imageCropLauncher.launch(cropOptions)
                    }
                )
                CustomButtonWithoutImageVector(
                    text = "Выбрать с Галереи",
                    onClick = {
                        openBottomSheet = false
                        val cropOptions = CropImageContractOptions(
                            null,
                            CropImageOptions(imageSourceIncludeCamera = false)
                        )
                        imageCropLauncher.launch(cropOptions)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(item: ProductEntity) {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            AsyncImage(
                model = item.imageLink,
                contentDescription = "phone"
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    minLines = 1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W400
                )
                Text(
                    text = item.price,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp,
                    color = Color(0xFFDA6600)
                )
            }
        }
    }
}


@Composable
fun FilterButton(
    text: String,
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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




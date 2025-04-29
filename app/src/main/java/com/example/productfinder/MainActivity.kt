package com.example.productfinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.productfinder.data.Product
import com.example.productfinder.presentation.itemfoundscreen.LottieLoading
import com.example.productfinder.presentation.itemfoundscreen.RatingBar
import com.example.productfinder.ui.theme.ProductFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductFinderTheme(dynamicColor = false) {
                    ProductFinderApp()

//                Column(
//                    modifier = Modifier.fillMaxSize().background(color = Color.Black),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    LottieLoading()
//                }
            }
        }
    }
}


@Composable
fun ProductItem1(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val logoTitleFont = FontFamily(Font(R.font.montserrat_black))
    val titleFont = FontFamily(Font(R.font.montserrat_medium))
    Card(
        modifier = modifier.clickable {
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
                        text = product.title ?: "",
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
                        onRatingChanged = {},
                        starsColor = Color(0xffDA6600)
                    )
                    product.price?.let {
                        Text(
                            text = it,
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
                            fontFamily = logoTitleFont,
                            color = Color.Green
                        )
                    }
                }
            }
        }
    }
}
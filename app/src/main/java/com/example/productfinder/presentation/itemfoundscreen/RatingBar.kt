package com.example.productfinder.presentation.itemfoundscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    onRatingChanged: (Double) -> Unit,
    starsColor: Color = Color(0xFFDA6600)
) {

    var isHalfStar = (rating % 1) != 0.0

    Row {
        for (index in 1..stars) {
            Icon(
                imageVector =
                    if (index <= rating) {
                        Icons.Rounded.Star
                    } else {
                        if (isHalfStar) {
                            isHalfStar = false
                            Icons.AutoMirrored.Rounded.StarHalf
                        } else {
                            Icons.Rounded.StarOutline
                        }
                    },
                contentDescription = null,
                tint = starsColor,
                modifier = modifier
                    .clickable { onRatingChanged(index.toDouble()) }
            )
        }
    }
}
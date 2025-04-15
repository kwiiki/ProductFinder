package com.example.productfinder

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.productfinder.presentation.homescreen.CustomButtonWithoutImageVector
import com.example.productfinder.ui.theme.ProductFinderTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            var openBottomSheet by remember { mutableStateOf(true) }
//            val bottomState = rememberModalBottomSheetState(
//                skipPartiallyExpanded = true
//            )
//            if (openBottomSheet) {
//                ModalBottomSheet(
//                    tonalElevation = 20.dp,
//                    contentColor = MaterialTheme.colorScheme.secondary,
//                    containerColor = MaterialTheme.colorScheme.secondary,
//                    sheetState = bottomState,
//                    scrimColor = Color.White.copy(alpha = 0.2f),
//                    onDismissRequest = { openBottomSheet = false },
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(MaterialTheme.colorScheme.secondary)
//                            .navigationBarsPadding()
//                            .padding(horizontal = 16.dp)
//                            .height(160.dp),
//                        verticalArrangement = Arrangement.spacedBy(24.dp)
//                    ) {    var switchCheckedState by remember { mutableStateOf(false) }
//
//                        Switch(
//                            checked = switchCheckedState,
//                            onCheckedChange = { switchCheckedState = it },
//                            colors = SwitchDefaults.colors(
//                                checkedThumbColor = Color.White,
//                                checkedTrackColor = Color.Gray,
//                                checkedBorderColor = Color.White,
//                                uncheckedThumbColor = Color.Gray,
//                                uncheckedTrackColor = Color.LightGray,
//                                uncheckedBorderColor = Color.Gray,
//                                disabledCheckedThumbColor = Color.Green.copy(alpha = 0.3f),
//                                disabledCheckedTrackColor = Color.LightGray.copy(alpha = 0.3f),
//                                disabledCheckedBorderColor = Color.Green.copy(alpha = 0.3f),
//                                disabledUncheckedThumbColor = Color.Red.copy(alpha = 0.3f),
//                                disabledUncheckedTrackColor = Color.LightGray.copy(alpha = 0.3f),
//                                disabledUncheckedBorderColor = Color.Red.copy(alpha = 0.3f),
//                            )
//                        )
//                    }
                    ProductFinderTheme(dynamicColor = false) {
                        ProductFinderApp()
//                    }
//                }
            }
        }
    }
}


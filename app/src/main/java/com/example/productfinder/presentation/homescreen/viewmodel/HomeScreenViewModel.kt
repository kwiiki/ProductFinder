package com.example.productfinder.presentation.homescreen.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productfinder.core.network.ApiService
import com.example.productfinder.data.Filters
import com.example.productfinder.data.Product
import com.example.productfinder.data.SearchByTextRequest
import com.example.productfinder.data.db.dao.ProductDao
import com.example.productfinder.data.db.entity.ProductEntity
import com.example.productfinder.presentation.filterscreen.FilterCategory
import com.example.productfinder.presentation.filterscreen.FilterItem
import com.example.productfinder.presentation.itemfoundscreen.ProductsUiState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao
) : ViewModel() {

    private val _productsState = MutableStateFlow<ProductsUiState>(ProductsUiState.Empty)
    val productsState: StateFlow<ProductsUiState> = _productsState

    private val _recentProducts = MutableStateFlow<List<ProductEntity>>(emptyList())
    val recentProducts = _recentProducts

    private val _categories = MutableStateFlow(sampleData())
    val categories: StateFlow<List<FilterCategory>> = _categories

    val filters = MutableStateFlow<Filters>(Filters(emptyList()))

    fun getRecentProducts(){
        viewModelScope.launch {
            _recentProducts.value = productDao.getAllProduct()
        }
    }

    fun toggleAllCategories(enabled: Boolean) {
        _categories.update { list ->
            list.map { cat ->
                cat.copy(
                    enabled = enabled,
                    items = cat.items.map { it.copy(enabled = enabled) }
                )
            }
        }
    }

    fun toggleCategory(catId: String, enabled: Boolean) {
        _categories.update { list ->
            list.map {
                if (it.id == catId) {
                    it.copy(
                        enabled = enabled,
                        items = it.items.map { item -> item.copy(enabled = enabled) }
                    )
                } else it
            }
        }
    }

    fun toggleItem(catId: String, itemId: String, enabled: Boolean) {
        _categories.update { list ->
            list.map { cat ->
                if (cat.id == catId) {
                    val newItems = cat.items.map { item ->
                        if (item.id == itemId) item.copy(enabled = enabled) else item
                    }
                    cat.copy(
                        enabled = newItems.all { it.enabled },
                        items = newItems
                    )
                } else cat
            }
        }
    }

    private fun sampleData() = listOf(
        FilterCategory(
            id = "marketplaces",
            title = "Маркетплейсы",
            items = listOf(
                FilterItem("kaspi", "Kaspi",  "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://kaspi.kz&size=256"),
                FilterItem("wildberries", "Wildberries", "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://wildberries.kz&size=256"),
                FilterItem("ozon", "Ozon", "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://ozon.kz&size=256"),
                FilterItem("flip", "Flip", "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://flip.kz&size=256"),
            )
        ),
        FilterCategory(
            id = "shops",
            title = "Магазины электроники",
            items = listOf(
                FilterItem("Технодом", "Technodom", "https://logo.clearbit.com/technodom.kz"),
                FilterItem("Sulpak", "Sulpak", "https://logo.clearbit.com/sulpak.kz"),
                FilterItem("Мечта", "Mechta", "https://logo.clearbit.com/mechta.kz"),
                FilterItem("Alser", "Alser", "https://logo.clearbit.com/alser.kz"),
                FilterItem("Evrika", "Evrika", "https://logo.clearbit.com/evrika.com"),
                FilterItem("Белый Ветер", "Белый ветер", "https://logo.clearbit.com/shop.kz"),
                FilterItem("Logitech", "Logitech", "https://logo.clearbit.com/logitech.com"),
                FilterItem("DNS", "DNS", "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://dns-shop.kz&size=256"),
            )
        ),
        FilterCategory(
            id = "clothes and shoes",
            title = "Одежда и обувь",
            items = listOf(
                FilterItem("LC Waikiki", "LC Waikiki", "https://logo.clearbit.com/lcwaikiki.kz"),
                FilterItem("Zara", "Zara", "https://logo.clearbit.com/zara.com"),
                FilterItem("Intertop", "Intertop", "https://logo.clearbit.com/intertop.kz"),
                FilterItem("Lamoda.kz", "Lamoda", "https://logo.clearbit.com/lamoda.kz"),
                FilterItem("Adidas", "Adidas", "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://adidas.kz&size=256"),
                FilterItem("Sportmaster", "Sportmaster", "https://logo.clearbit.com/sportmaster.com"),
                FilterItem("Looq Sports", "Looq Sports", "https://t0.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://looqsports01.kz&size=256"),
            )
        ),
        FilterCategory(
            id = "beauty",
            title = "Косметика",
            items = listOf(
                FilterItem("Care to Beauty", "Care to Beauty", "https://logo.clearbit.com/caretobeauty.com"),
                FilterItem("Loja Glamourosa", "Loja Glamourosa ", "https://logo.clearbit.com/lojaglamourosa.com"),
                FilterItem("LaBeauty", "LaBeauty", "https://logo.clearbit.com/labeauty.com"),
                FilterItem("Mon Amie", "Mon Amie", "https://logo.clearbit.com/monamie.kz"),
                FilterItem("Oriflame", "Oriflame", "https://logo.clearbit.com/oriflame.com")
            )
        ),
    )

    fun toggleMarketplace(marketplace: String, isChecked: Boolean) {
        val currentList = filters.value.marketplaces.toMutableList()

        if (isChecked) {
            if (!currentList.contains(marketplace)) {
                currentList.add(marketplace)
            }
        } else {
            currentList.remove(marketplace)
        }
        filters.value = Filters(currentList)
    }

    fun searchByText(text: String) {
        viewModelScope.launch {
            _productsState.value = ProductsUiState.Loading
            try {
                Log.d("searchByText", "searchByText: $text, ${filters.value.toString()}")
                val products = apiService.searchByText(SearchByTextRequest(text = text, filters = filters.value))
                _productsState.value = ProductsUiState.Success(products)
            } catch (e: SocketTimeoutException) {
                _productsState.value =
                    ProductsUiState.Error("Превышено время ожидания: ${e.message}")
            } catch (e: Exception) {
                _productsState.value = ProductsUiState.Error("Ошибка загрузки: ${e.message}")
                Log.d("errorMessage", "searchByText: ${e.message}")
            }
        }
    }


    fun uploadImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            _productsState.value = ProductsUiState.Loading
            try {
                // 1. Получаем сжатый файл из Uri
                val file = uriToFile(uri, context)
                val compressedFile = compressFile(file, context)

                // 2. Создаём Part для изображения
                val requestFile = compressedFile
                    .asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part
                    .createFormData("image", compressedFile.name, requestFile)

                // 3. Формируем JSON из текущих фильтров
                //    Например, если filters.value = Filters(["kaspi","ozon"]),
                //    то нам нужно сделать {"filters": {"marketplaces": ["kaspi","ozon"]}}
                val filterJson = Gson().toJson(mapOf("filters" to mapOf("marketplaces" to filters.value.marketplaces)))
                val filterRequestBody = filterJson
                    .toRequestBody("application/json".toMediaTypeOrNull())
                val dataPart = MultipartBody.Part
                    .createFormData("data", null, filterRequestBody)

                // 4. Отправляем на сервер и обрабатываем результат
                val products = apiService.upload(imagePart, dataPart)
                _productsState.value = ProductsUiState.Success(products)

            } catch (e: SocketTimeoutException) {
                _productsState.value =
                    ProductsUiState.Error("Превышено время ожидания: ${e.message}")
            } catch (e: Exception) {
                _productsState.value = ProductsUiState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }


    fun clearError() {
        _productsState.value = ProductsUiState.Error(message = "")
    }

    private suspend fun compressFile(
        file: File,
        context: Context,
        quality: Int = 80,
        maxWidth: Int = 1280,
        maxHeight: Int = 1280
    ): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap,
            maxWidth.coerceAtMost(bitmap.width),
            maxHeight.coerceAtMost(bitmap.height),
            true
        )

        val compressedFile = File(context.cacheDir, "compressed_image.jpg")
        withContext(Dispatchers.IO) {
            FileOutputStream(compressedFile).use { outputStream ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }
        }
        return compressedFile
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    fun saveRecentProductInDB(product: Product){
        viewModelScope.launch {
            productDao.insert(
                ProductEntity(
                    title = product.title.toString(),
                    source = product.source.toString(),
                    price = product.price.toString(),
                    imageLink = product.imageLink.toString(),
                    rating = product.rating,
                    linkForMarket = product.link.toString(),
                    freeDelivery = product.freeDelivery,
                    logoUrl = product.logoUrl.toString()
                )
            )
        }
    }
}
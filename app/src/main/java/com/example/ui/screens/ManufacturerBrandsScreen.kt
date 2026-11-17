package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.Product
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.ProductVisual
import com.example.ui.components.SubBrandVisualCard
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickOutline
import com.example.ui.theme.QuickSlate
import com.example.util.FormatUtils
import kotlin.math.roundToInt

data class SubBrandEntry(
    val name: String,
    val representativeProduct: Product,
    val productCount: Int
)

private val CarouselCoral = Color(0xFFE05353)
private val CarouselSageBand = Color(0xFFEAF5EA)

@Composable
fun ManufacturerBrandsScreen(
    manufacturer: Brand,
    productsForManufacturer: List<Product>,
    cartItems: List<CartItem> = emptyList(),
    notifyProductIds: Set<String> = emptySet(),
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSubBrandClick: (SubBrandEntry) -> Unit,
    onViewAllManufacturerProducts: () -> Unit,
    onProductClick: (Product) -> Unit = {},
    onAddClick: (Product, String) -> Unit = { _, _ -> },
    onIncrementClick: (Product, String) -> Unit = { _, _ -> },
    onDecrementClick: (Product, String) -> Unit = { _, _ -> },
    onNotifyClick: (Product) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showSearchField by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val cartQtyMap = remember(cartItems) {
        cartItems.associate { "${it.productId}_${it.variantId}" to it.quantity }
    }

    val allSubBrands = remember(productsForManufacturer, manufacturer.name) {
        productsForManufacturer
            .groupBy { it.subBrand.ifBlank { manufacturer.name } }
            .map { (subName, items) ->
                SubBrandEntry(
                    name = subName,
                    representativeProduct = items.first(),
                    productCount = items.size
                )
            }
            .sortedBy { it.name }
    }

    val trimmedQuery = searchQuery.trim()
    val subBrands = remember(allSubBrands, trimmedQuery) {
        if (trimmedQuery.isBlank()) allSubBrands
        else allSubBrands.filter { it.name.contains(trimmedQuery, ignoreCase = true) }
    }
    val carouselProducts = remember(productsForManufacturer, trimmedQuery) {
        if (trimmedQuery.isBlank()) {
            productsForManufacturer.sortedByDescending { it.isFeatured || it.anyVariantInStock }
        } else {
            productsForManufacturer.filter {
                it.name.contains(trimmedQuery, ignoreCase = true) ||
                    it.subBrand.contains(trimmedQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Green Manufacturer Header with crisp manufacturer logo emblem & inline search toggle
        Surface(
            color = QuickGreen,
            contentColor = Color.White,
            tonalElevation = 4.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("manufacturer_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    BrandVisualIcon(
                        brand = manufacturer,
                        size = 34.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = manufacturer.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (showSearchField) {
                                searchQuery = ""
                                showSearchField = false
                            } else {
                                showSearchField = true
                            }
                        },
                        modifier = Modifier.testTag("manufacturer_search_button")
                    ) {
                        Icon(
                            imageVector = if (showSearchField) Icons.Rounded.Close else Icons.Rounded.Search,
                            contentDescription = "Search in ${manufacturer.name}",
                            tint = Color.White
                        )
                    }
                }

                AnimatedVisibility(visible = showSearchField) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search ${manufacturer.name} brands or items...") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = QuickSlate,
                            unfocusedTextColor = QuickSlate
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("manufacturer_search_input")
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    Text(
                        text = "Shop by Brand",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = QuickSlate
                    )
                    Spacer(Modifier.height(14.dp))

                    if (subBrands.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9FAFB),
                            border = BorderStroke(1.dp, QuickOutline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No matching ${manufacturer.name} brand found",
                                    fontWeight = FontWeight.SemiBold,
                                    color = QuickSlate
                                )
                                TextButton(onClick = {
                                    searchQuery = ""
                                    onSearchClick()
                                }) {
                                    Text("Search all QuickKart products", color = QuickGreen)
                                }
                            }
                        }
                    } else {
                        val rows = subBrands.chunked(5)
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            rows.forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowItems.forEach { entry ->
                                        SubBrandTile(
                                            entry = entry,
                                            manufacturer = manufacturer,
                                            onClick = { onSubBrandClick(entry) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    repeat(5 - rowItems.size) {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (carouselProducts.isNotEmpty()) {
                item {
                    Surface(
                        color = CarouselSageBand,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "View All",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = QuickSlate
                                    )
                                    Text(
                                        text = "${carouselProducts.size} ${manufacturer.name} products",
                                        fontSize = 12.sp,
                                        color = QuickMuted
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .clickable(onClick = onViewAllManufacturerProducts)
                                        .testTag("manufacturer_view_all_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color.White,
                                        shadowElevation = 2.dp,
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                                contentDescription = "View all ${manufacturer.name} products",
                                                tint = QuickSlate,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 14.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(carouselProducts, key = { it.id }) { product ->
                                    ManufacturerCarouselProductCard(
                                        product = product,
                                        manufacturer = manufacturer,
                                        cartQtyMap = cartQtyMap,
                                        isNotified = product.id in notifyProductIds,
                                        onProductClick = { onProductClick(product) },
                                        onAddClick = { vid -> onAddClick(product, vid) },
                                        onIncrementClick = { vid -> onIncrementClick(product, vid) },
                                        onDecrementClick = { vid -> onDecrementClick(product, vid) },
                                        onNotifyClick = { onNotifyClick(product) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onViewAllManufacturerProducts,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, QuickGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Browse All ${productsForManufacturer.size} ${manufacturer.name} Products →",
                        color = QuickGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SubBrandTile(
    entry: SubBrandEntry,
    manufacturer: Brand,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
            .testTag("sub_brand_tile_${entry.name}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubBrandVisualCard(
            subBrandName = entry.name,
            manufacturer = manufacturer
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = entry.name,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = QuickSlate
        )
    }
}

@Composable
private fun ManufacturerCarouselProductCard(
    product: Product,
    manufacturer: Brand,
    cartQtyMap: Map<String, Int>,
    isNotified: Boolean,
    onProductClick: () -> Unit,
    onAddClick: (String) -> Unit,
    onIncrementClick: (String) -> Unit,
    onDecrementClick: (String) -> Unit,
    onNotifyClick: () -> Unit
) {
    var selectedVariantId by remember(product.id) { mutableStateOf(product.defaultVariant.id) }
    var showVariantMenu by remember { mutableStateOf(false) }

    val selectedVariant = product.variants.firstOrNull { it.id == selectedVariantId } ?: product.defaultVariant
    val qty = cartQtyMap["${product.id}_${selectedVariant.id}"] ?: 0
    val inStock = selectedVariant.isInStock

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(0.8.dp, QuickOutline),
        shadowElevation = 1.dp,
        modifier = Modifier
            .width(154.dp)
            .clickable(onClick = onProductClick)
            .testTag("manufacturer_carousel_card_${product.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Artwork Box + Margin Tag + Veg Dot + Coral Add/+ Stepper
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(118.dp)
            ) {
                ProductVisual(
                    imageKey = product.imageKey,
                    colorHex = product.colorHex,
                    iconSize = 38.dp,
                    imageUrl = product.imageUrl,
                    brandName = product.subBrand.ifBlank { manufacturer.name },
                    brandLogoUrl = resolveSubBrandLogoUrl(product.subBrand, manufacturer.logoUrl),
                    brandColorHex = manufacturer.colorHex,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    color = QuickGreen,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "${selectedVariant.marginPercent.roundToInt()}% MARGIN",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Veg / Non-Veg dot bottom-left
                val dietColor = if (product.isVeg) QuickGreen else Color(0xFFB91C1C)
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(3.dp),
                    border = BorderStroke(1.dp, dietColor),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .size(14.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(dietColor, CircleShape)
                        )
                    }
                }

                // Bottom-right Coral "+" / Stepper / Notify control
                AnimatedContent(
                    targetState = Triple(inStock, qty > 0, isNotified),
                    transitionSpec = {
                        (fadeIn(tween(150)) + scaleIn(initialScale = 0.9f, animationSpec = tween(150)))
                            .togetherWith(fadeOut(tween(120)) + scaleOut(targetScale = 0.9f, animationSpec = tween(120)))
                            .using(SizeTransform(clip = false))
                    },
                    label = "carousel_card_action",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                ) { (stockAvail, hasQty, notified) ->
                    if (!stockAvail) {
                        Surface(
                            onClick = onNotifyClick,
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (notified) QuickGreen else CarouselCoral),
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (notified) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                                    contentDescription = if (notified) "Notified" else "Notify Me",
                                    tint = if (notified) QuickGreen else CarouselCoral,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(3.dp))
                                Text(
                                    text = if (notified) "Notified" else "Notify",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (notified) QuickGreen else CarouselCoral
                                )
                            }
                        }
                    } else if (!hasQty) {
                        Surface(
                            onClick = { onAddClick(selectedVariant.id) },
                            color = CarouselCoral,
                            shape = RoundedCornerShape(9.dp),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("carousel_add_${product.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "Add ${product.name}",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    } else {
                        Surface(
                            color = CarouselCoral,
                            shape = RoundedCornerShape(9.dp),
                            shadowElevation = 2.dp,
                            modifier = Modifier.height(32.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onDecrementClick(selectedVariant.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Decrease quantity",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "$qty",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                IconButton(
                                    onClick = { onIncrementClick(selectedVariant.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "Increase quantity",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.5.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = QuickSlate,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.heightIn(min = 32.dp)
                )

                // Pack size pill dropdown
                Box {
                    Surface(
                        onClick = { if (product.variants.size > 1) showVariantMenu = true },
                        shape = RoundedCornerShape(7.dp),
                        color = Color(0xFFF3F4F6),
                        border = BorderStroke(0.6.dp, QuickOutline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedVariant.label,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = QuickSlate,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            if (product.variants.size > 1) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "Select pack size",
                                    tint = QuickMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = showVariantMenu,
                        onDismissRequest = { showVariantMenu = false }
                    ) {
                        product.variants.forEach { variant ->
                            DropdownMenuItem(
                                text = {
                                    Text("${variant.label} — ${FormatUtils.inr(variant.sellingPrice)}")
                                },
                                onClick = {
                                    selectedVariantId = variant.id
                                    showVariantMenu = false
                                }
                            )
                        }
                    }
                }

                // Price + strikethrough MRP
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = FormatUtils.inr(selectedVariant.sellingPrice),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = QuickSlate
                    )
                    if (selectedVariant.mrp > selectedVariant.sellingPrice) {
                        Text(
                            text = FormatUtils.inr(selectedVariant.mrp),
                            fontSize = 11.sp,
                            color = QuickMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}

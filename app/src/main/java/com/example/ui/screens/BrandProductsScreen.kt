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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.model.Category
import com.example.model.Product
import com.example.model.ProductFilterState
import com.example.ui.components.ProductVisual
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickRed
import com.example.util.FormatUtils
import kotlin.math.roundToInt

private val BrandAddCoral = Color(0xFFE05353)

@Composable
fun BrandProductsScreen(
    title: String,
    categories: List<Category>,
    allSubBrandProducts: List<Product>,
    products: List<Product>,
    filterState: ProductFilterState,
    cartItems: List<CartItem>,
    notifyProductIds: Set<String>,
    etaMinutes: Int,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChipClick: (String?) -> Unit,
    onOpenFilterSheet: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAddClick: (Product, String) -> Unit,
    onIncrementClick: (Product, String) -> Unit,
    onDecrementClick: (Product, String) -> Unit,
    onNotifyClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    brands: List<Brand> = emptyList(),
    customChipLabels: List<String>? = null,
    selectedCustomChip: String? = null,
    onCustomChipClick: ((String?) -> Unit)? = null,
    leafChipLabels: List<String>? = null,
    selectedLeafChip: String? = null,
    onLeafChipClick: ((String) -> Unit)? = null
) {
    var showSearchField by remember { mutableStateOf(false) }

    val availableCategories = remember(allSubBrandProducts, categories) {
        val catIds = allSubBrandProducts.map { it.categoryId }.toSet()
        categories.filter { it.id in catIds }
    }
    val cartQtyMap = cartItems.associate { "${it.productId}_${it.variantId}" to it.quantity }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Green Header matching reference screenshots 4 & 6
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
                        modifier = Modifier.testTag("brand_products_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showSearchField = !showSearchField }) {
                        Icon(
                            imageVector = if (showSearchField) Icons.Rounded.Close else Icons.Rounded.Search,
                            contentDescription = "Search in $title",
                            tint = Color.White
                        )
                    }
                }

                AnimatedVisibility(visible = showSearchField) {
                    OutlinedTextField(
                        value = filterState.query,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search inside $title...") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color(0xFF111827),
                            unfocusedTextColor = Color(0xFF111827)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // 3rd-level leaf sub-filter strip (white rounded cards on light gray bar, red highlight when active)
        if (!leafChipLabels.isNullOrEmpty() && onLeafChipClick != null) {
            Surface(
                color = Color(0xFFEAECEE),
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(leafChipLabels, key = { it }) { chipLabel ->
                        val selected = selectedLeafChip == chipLabel
                        Surface(
                            onClick = { onLeafChipClick(chipLabel) },
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            border = BorderStroke(
                                width = if (selected) 1.5.dp else 0.8.dp,
                                color = if (selected) QuickRed else Color(0xFFD1D5DB)
                            ),
                            shadowElevation = if (selected) 2.dp else 0.5.dp,
                            modifier = Modifier
                                .widthIn(min = 104.dp, max = 164.dp)
                                .heightIn(min = 48.dp)
                                .testTag("leaf_chip_$chipLabel")
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = chipLabel,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selected) QuickRed else Color(0xFF1F2937)
                                )
                            }
                        }
                    }
                }
            }
        } else if (customChipLabels != null && onCustomChipClick != null) {
            // Subcategories strip when opened from Department "All" view
            if (customChipLabels.isNotEmpty()) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCustomChip == null,
                                onClick = { onCustomChipClick(null) },
                                label = { Text("All") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = QuickGreenContainer,
                                    selectedLabelColor = QuickGreen
                                ),
                                modifier = Modifier.testTag("subcat_chip_all")
                            )
                        }
                        items(customChipLabels, key = { it }) { chipLabel ->
                            val selected = selectedCustomChip == chipLabel
                            FilterChip(
                                selected = selected,
                                onClick = { onCustomChipClick(if (selected) null else chipLabel) },
                                label = { Text(chipLabel) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = QuickGreenContainer,
                                    selectedLabelColor = QuickGreen
                                ),
                                modifier = Modifier.testTag("subcat_chip_$chipLabel")
                            )
                        }
                    }
                }
            }
        } else if (availableCategories.size > 1 || filterState.categoryId != null) {
            Surface(
                color = Color(0xFFEAECEE),
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(availableCategories, key = { it.id }) { cat ->
                        val selected = filterState.categoryId == cat.id
                        Surface(
                            onClick = { onCategoryChipClick(if (selected) null else cat.id) },
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            border = BorderStroke(
                                width = if (selected) 1.5.dp else 0.8.dp,
                                color = if (selected) QuickRed else Color(0xFFD1D5DB)
                            ),
                            shadowElevation = if (selected) 2.dp else 0.5.dp,
                            modifier = Modifier
                                .widthIn(min = 104.dp, max = 164.dp)
                                .heightIn(min = 48.dp)
                                .testTag("brand_category_chip_${cat.id}")
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = cat.name,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selected) QuickRed else Color(0xFF1F2937)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Count + Filter bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${products.size} Items",
                    style = MaterialTheme.typography.titleSmall,
                    color = QuickMuted
                )

                Surface(
                    onClick = onOpenFilterSheet,
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.testTag("brand_products_filter")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BadgedBox(
                            badge = {
                                if (filterState.activeFilterCount > 0) {
                                    Badge(containerColor = QuickGreen, contentColor = Color.White) {
                                        Text("${filterState.activeFilterCount}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = "Filter",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                        Text("Filter", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

        HorizontalDivider()

        // Horizontal product cards list matching reference screenshots
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No matching products found",
                    style = MaterialTheme.typography.titleMedium,
                    color = QuickMuted
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    val productBrand = brands.firstOrNull { it.id == product.brandId }
                    BrandProductRowCard(
                        product = product,
                        brand = productBrand,
                        etaMinutes = etaMinutes,
                        qtyForVariant = { vid -> cartQtyMap["${product.id}_$vid"] ?: 0 },
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

@Composable
private fun BrandProductRowCard(
    product: Product,
    brand: Brand?,
    etaMinutes: Int,
    qtyForVariant: (String) -> Int,
    isNotified: Boolean,
    onProductClick: () -> Unit,
    onAddClick: (String) -> Unit,
    onIncrementClick: (String) -> Unit,
    onDecrementClick: (String) -> Unit,
    onNotifyClick: () -> Unit
) {
    var selectedVariantId by remember(product.id) { mutableStateOf(product.defaultVariant.id) }
    val selectedVariant = product.variants.firstOrNull { it.id == selectedVariantId } ?: product.defaultVariant
    var showVariantMenu by remember { mutableStateOf(false) }

    val qty = qtyForVariant(selectedVariant.id)
    val inStock = selectedVariant.isInStock

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
            .testTag("brand_product_row_${product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left visual with MARGIN ribbon, veg dot & internet brand badge
            Box(modifier = Modifier.size(118.dp)) {
                val badgeLogoUrl = resolveSubBrandLogoUrl(
                    product.subBrand,
                    brand?.logoUrl.orEmpty()
                )
                ProductVisual(
                    imageKey = product.imageKey,
                    colorHex = product.colorHex,
                    iconSize = 42.dp,
                    imageUrl = product.imageUrl,
                    brandName = product.subBrand.ifBlank { brand?.name.orEmpty() },
                    brandLogoUrl = badgeLogoUrl,
                    brandColorHex = brand?.colorHex ?: 0xFF0C831FL,
                    modifier = Modifier.matchParentSize()
                )

                Surface(
                    color = QuickGreen,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 10.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "${selectedVariant.marginPercent.roundToInt()}% MARGIN",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }

                // Veg / Non-Veg indicator badge bottom-left
                val dietColor = if (product.isVeg) QuickGreen else Color(0xFFB91C1C)
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(3.dp),
                    border = BorderStroke(1.dp, dietColor),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .size(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(dietColor, CircleShape)
                        )
                    }
                }

                if (!inStock) {
                    Surface(
                        color = Color.White.copy(alpha = 0.92f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, QuickMuted.copy(alpha = 0.5f)),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "OUT OF STOCK",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Right details column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.subBrand.ifBlank { "BRAND" }.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = QuickMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = null,
                                tint = QuickGreen,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "$etaMinutes mins",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuickMuted
                            )
                        }
                    }
                }

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Pack size dropdown selector
                Box {
                    Surface(
                        onClick = { if (product.variants.size > 1) showVariantMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedVariant.label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            if (product.variants.size > 1) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "Select pack size",
                                    modifier = Modifier.size(18.dp),
                                    tint = QuickMuted
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

                // Price + Add / Stepper / Notify Me row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val unitRate = remember(selectedVariant.sellingPrice, selectedVariant.label) {
                        FormatUtils.unitRateLabel(selectedVariant.sellingPrice, selectedVariant.label)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = FormatUtils.inr(selectedVariant.sellingPrice),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                        if (selectedVariant.mrp > selectedVariant.sellingPrice) {
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = FormatUtils.inr(selectedVariant.mrp),
                                style = MaterialTheme.typography.bodySmall,
                                color = QuickMuted,
                                textDecoration = TextDecoration.LineThrough,
                                maxLines = 1
                            )
                        }
                        if (unitRate != null) {
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = unitRate,
                                style = MaterialTheme.typography.bodySmall,
                                color = QuickMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    AnimatedContent(
                        targetState = Triple(inStock, qty > 0, isNotified),
                        transitionSpec = {
                            (fadeIn(tween(180)) + scaleIn(initialScale = 0.92f, animationSpec = tween(180)))
                                .togetherWith(fadeOut(tween(140)) + scaleOut(targetScale = 0.92f, animationSpec = tween(140)))
                                .using(SizeTransform(clip = false))
                        },
                        label = "brand_row_action"
                    ) { (stockAvail, hasQty, notified) ->
                        if (!stockAvail) {
                            OutlinedButton(
                                onClick = onNotifyClick,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (notified) QuickGreen else BrandAddCoral),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (notified) QuickGreen else BrandAddCoral
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (notified) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = if (notified) "Notified" else "Notify Me",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        } else if (!hasQty) {
                            Button(
                                onClick = { onAddClick(selectedVariant.id) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandAddCoral),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("brand_product_add_${product.id}")
                            ) {
                                Text("Add", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Surface(
                                color = BrandAddCoral,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onDecrementClick(selectedVariant.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Rounded.Remove, contentDescription = "Decrease", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                    Text(
                                        text = "$qty",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { onIncrementClick(selectedVariant.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Rounded.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

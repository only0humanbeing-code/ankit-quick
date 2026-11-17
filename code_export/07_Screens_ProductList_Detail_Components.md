# 07 — Product Catalog, Product Detail & Reusable UI Components

Contains `ProductListScreen.kt`, `ProductDetailScreen.kt`, `ProductCard.kt`, and `FilterSortSheet.kt`.

---

## File: `app/src/main/java/com/example/ui/screens/ProductListScreen.kt`
```kotlin
package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.Product
import com.example.model.ProductFilterState
import com.example.ui.components.ProductCard
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted

@Composable
fun ProductListScreen(
    title: String,
    showBackButton: Boolean,
    categories: List<Category>,
    brands: List<Brand>,
    filteredProducts: List<Product>,
    filterState: ProductFilterState,
    cartItems: List<CartItem>,
    notifyProductIds: Set<String>,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    onBrandSelect: (String?) -> Unit,
    onOpenFilterSheet: () -> Unit,
    onResetFilters: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAddClick: (Product) -> Unit,
    onIncrementClick: (Product) -> Unit,
    onDecrementClick: (Product) -> Unit,
    onNotifyClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartQtyMap = cartItems.associate { "${it.productId}_${it.variantId}" to it.quantity }
    fun qtyFor(p: Product) = cartQtyMap["${p.id}_${p.defaultVariant.id}"] ?: 0

    Column(modifier = modifier.fillMaxSize()) {
        // Top header + search
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showBackButton) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag("product_list_back")
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = if (showBackButton) 4.dp else 6.dp)
                    )
                    Text(
                        text = "${filteredProducts.size} items",
                        style = MaterialTheme.typography.labelLarge,
                        color = QuickMuted,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = filterState.query,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search groceries, brands...") },
                        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                        trailingIcon = {
                            if (filterState.query.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Rounded.Close, contentDescription = "Clear search")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("catalog_search_input")
                    )

                    Surface(
                        onClick = onOpenFilterSheet,
                        shape = RoundedCornerShape(14.dp),
                        color = if (filterState.activeFilterCount > 0) QuickGreen else QuickGreenContainer,
                        border = BorderStroke(1.dp, QuickGreen.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .height(54.dp)
                            .testTag("catalog_filter_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BadgedBox(
                                badge = {
                                    if (filterState.activeFilterCount > 0) {
                                        Badge(containerColor = Color.White, contentColor = QuickGreen) {
                                            Text("${filterState.activeFilterCount}")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Tune,
                                    contentDescription = "Filters",
                                    tint = if (filterState.activeFilterCount > 0) Color.White else QuickGreen
                                )
                            }
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Filter",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (filterState.activeFilterCount > 0) Color.White else QuickGreen
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Category chip row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = filterState.categoryId == null,
                            onClick = { onCategorySelect(null) },
                            label = { Text("All Categories") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    items(categories, key = { it.id }) { cat ->
                        FilterChip(
                            selected = filterState.categoryId == cat.id,
                            onClick = { onCategorySelect(if (filterState.categoryId == cat.id) null else cat.id) },
                            label = { Text(cat.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Brand & Sort quick row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = true,
                            onClick = onOpenFilterSheet,
                            leadingIcon = {
                                Icon(
                                    Icons.Rounded.Sort,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            label = { Text(filterState.sortOption.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                    items(brands, key = { it.id }) { brand ->
                        val selected = filterState.brandId == brand.id
                        FilterChip(
                            selected = selected,
                            onClick = { onBrandSelect(if (selected) null else brand.id) },
                            label = { Text(brand.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }
                }
            }
        }

        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.SearchOff,
                        contentDescription = null,
                        tint = QuickMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "No matching products found",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Try adjusting your price, discount, or brand filters.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuickMuted
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onResetFilters,
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 166.dp),
                contentPadding = PaddingValues(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val productBrand = brands.firstOrNull { it.id == product.brandId }
                    ProductCard(
                        product = product,
                        quantityInCart = qtyFor(product),
                        isNotified = product.id in notifyProductIds,
                        onProductClick = { onProductClick(product) },
                        onAddClick = { onAddClick(product) },
                        onIncrementClick = { onIncrementClick(product) },
                        onDecrementClick = { onDecrementClick(product) },
                        onNotifyClick = { onNotifyClick(product) },
                        brand = productBrand
                    )
                }
            }
        }
    }
}
```

---

## File: `app/src/main/java/com/example/ui/screens/ProductDetailScreen.kt`
```kotlin
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.Product
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.ProductVisual
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickAmber
import com.example.ui.theme.QuickAmberContainer
import com.example.ui.theme.QuickBlue
import com.example.ui.theme.QuickGold
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickGreenDark
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickRed
import com.example.util.FormatUtils
import kotlin.math.roundToInt

private val DetailAddCoral = Color(0xFFE05353)
private val DetailSaveCharcoal = Color(0xFF474D50)

@Composable
fun ProductDetailScreen(
    product: Product,
    brand: Brand?,
    category: Category?,
    cartItems: List<CartItem>,
    totalCartCount: Int,
    isNotified: Boolean,
    isSaved: Boolean,
    etaMinutes: Int,
    seeMoreBrandLabel: String,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit,
    onSeeMoreBrandClick: () -> Unit,
    onSaveForLaterClick: () -> Unit,
    onAddClick: (variantId: String) -> Unit,
    onIncrementClick: (variantId: String) -> Unit,
    onDecrementClick: (variantId: String) -> Unit,
    onNotifyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedVariantId by remember(product.id) {
        mutableStateOf(product.defaultVariant.id)
    }
    val selectedVariant = product.variants.firstOrNull { it.id == selectedVariantId }
        ?: product.defaultVariant

    var selectedGalleryIndex by remember(product.id) { mutableIntStateOf(0) }
    var aboutExpanded by remember(product.id) { mutableStateOf(true) }

    val qtyInCart = cartItems
        .firstOrNull { it.productId == product.id && it.variantId == selectedVariant.id }
        ?.quantity ?: 0

    val unitRate = remember(selectedVariant.sellingPrice, selectedVariant.label) {
        FormatUtils.unitRateLabel(selectedVariant.sellingPrice, selectedVariant.label)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.White,
        topBar = {
            Surface(
                color = QuickGreen,
                contentColor = Color.White,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = category?.name.orEmpty(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.95f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier.testTag("detail_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search products",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = onCartClick,
                            modifier = Modifier.testTag("detail_cart_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (totalCartCount > 0) {
                                        Badge(
                                            containerColor = Color.White,
                                            contentColor = QuickGreenDark
                                        ) {
                                            Text("$totalCartCount", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingBag,
                                    contentDescription = "Cart",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DetailSaveCharcoal)
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    // Left 50%: Save For Later
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(DetailSaveCharcoal)
                            .clickable(onClick = onSaveForLaterClick)
                            .testTag("detail_save_for_later"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Save For Later",
                                tint = if (isSaved) QuickAmber else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (isSaved) "Saved" else "Save For Later",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSaved) QuickAmber else Color.White
                            )
                        }
                    }

                    // Right 50%: Add to Cart / Stepper / Notify Me
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(DetailAddCoral),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = Triple(selectedVariant.isInStock, qtyInCart > 0, isNotified),
                            transitionSpec = {
                                (fadeIn(tween(180)) + scaleIn(initialScale = 0.94f, animationSpec = tween(180)))
                                    .togetherWith(fadeOut(tween(140)) + scaleOut(targetScale = 0.94f, animationSpec = tween(140)))
                                    .using(SizeTransform(clip = false))
                            },
                            label = "detail_bottom_cta"
                        ) { (stockAvail, hasQty, notified) ->
                            if (!stockAvail) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(if (notified) QuickGreenDark else DetailAddCoral)
                                        .clickable(onClick = onNotifyClick)
                                        .testTag("detail_notify_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (notified) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                                            contentDescription = "Notify Me",
                                            tint = Color.White,
                                            modifier = Modifier.size(19.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = if (notified) "Notified ✓" else "Notify Me",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            } else if (!hasQty) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { onAddClick(selectedVariant.id) }
                                        .testTag("detail_add_to_cart"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.ShoppingBag,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(19.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "Add to Cart",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    IconButton(
                                        onClick = { onDecrementClick(selectedVariant.id) },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .testTag("detail_dec_qty")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Remove,
                                            contentDescription = "Decrease quantity",
                                            tint = Color.White
                                        )
                                    }
                                    Text(
                                        text = "$qtyInCart in Cart",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    IconButton(
                                        onClick = { onIncrementClick(selectedVariant.id) },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .testTag("detail_inc_qty")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Add,
                                            contentDescription = "Increase quantity",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Header Block matching bb saathi: "See more <Brand> products", Title + Pack, Price + Unit Rate + MRP + Margin badge
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        onClick = onSeeMoreBrandClick,
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF7FBF3),
                        border = BorderStroke(1.dp, QuickGreen.copy(alpha = 0.55f)),
                        modifier = Modifier.testTag("detail_see_more_brand")
                    ) {
                        Text(
                            text = "See more $seeMoreBrandLabel products",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = QuickGreenDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "${product.name}, ${selectedVariant.label}",
                        fontSize = 20.sp,
                        lineHeight = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = FormatUtils.inr(selectedVariant.sellingPrice),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827)
                        )
                        if (unitRate != null) {
                            Text(
                                text = unitRate,
                                fontSize = 13.sp,
                                color = QuickMuted
                            )
                        }
                        if (selectedVariant.mrp > selectedVariant.sellingPrice) {
                            Text(
                                text = "MRP: ${FormatUtils.inr(selectedVariant.mrp)}",
                                fontSize = 12.sp,
                                color = QuickMuted,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                        Surface(
                            color = Color(0xFFEB5757),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${selectedVariant.marginPercent.roundToInt()}% MARGIN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "(Inclusive of all taxes)",
                        fontSize = 11.sp,
                        color = QuickMuted
                    )
                }
            }

            // Hero Image Showcase + Veg Indicator + Gallery Thumbnails Strip
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val badgeLogoUrl = resolveSubBrandLogoUrl(
                        product.subBrand,
                        brand?.logoUrl.orEmpty()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(236.dp)
                    ) {
                        when (selectedGalleryIndex) {
                            0 -> {
                                ProductVisual(
                                    imageKey = product.imageKey,
                                    colorHex = product.colorHex,
                                    iconSize = 84.dp,
                                    showTag = false,
                                    imageUrl = product.imageUrl,
                                    brandName = product.subBrand.ifBlank { brand?.name.orEmpty() },
                                    brandLogoUrl = badgeLogoUrl,
                                    brandColorHex = brand?.colorHex ?: 0xFF0C831FL,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            1 -> {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = QuickGreenContainer,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(24.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Bolt,
                                            contentDescription = null,
                                            tint = QuickGreen,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(Modifier.height(10.dp))
                                        Text(
                                            text = "$etaMinutes-Minute Freshness",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = QuickGreenDark
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = "Temperature-controlled dark store storage & quality check.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = QuickMuted
                                        )
                                    }
                                }
                            }
                            else -> {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = QuickAmberContainer.copy(alpha = 0.65f),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(24.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${selectedVariant.marginPercent.roundToInt()}%",
                                            style = MaterialTheme.typography.displayMedium,
                                            color = QuickGold,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "Partner Margin",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = "You save ${FormatUtils.inr(selectedVariant.savingsAmount)} on ${selectedVariant.label}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = QuickMuted
                                        )
                                    }
                                }
                            }
                        }

                        // Veg / Non-Veg Square Badge bottom-left
                        val dietColor = if (product.isVeg) QuickGreen else Color(0xFFB91C1C)
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(3.dp),
                            border = BorderStroke(1.2.dp, dietColor),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(10.dp)
                                .size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(dietColor, CircleShape)
                                )
                            }
                        }

                        if (!selectedVariant.isInStock) {
                            Surface(
                                color = Color.White.copy(alpha = 0.94f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, QuickMuted.copy(alpha = 0.45f)),
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = "OUT OF STOCK",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF374151),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // 3 Interactive Thumbnail Slides matching Screenshot 1
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val slides = listOf("Product", "Freshness", "Margin")
                        items(slides.indices.toList()) { idx ->
                            val active = selectedGalleryIndex == idx
                            Surface(
                                onClick = { selectedGalleryIndex = idx },
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                border = BorderStroke(
                                    width = if (active) 1.5.dp else 1.dp,
                                    color = if (active) Color(0xFF374151) else Color(0xFFD1D5DB)
                                ),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                                    when (idx) {
                                        0 -> ProductVisual(
                                            imageKey = product.imageKey,
                                            colorHex = product.colorHex,
                                            iconSize = 24.dp,
                                            imageUrl = product.imageUrl,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        1 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = Icons.Rounded.Bolt,
                                                contentDescription = "Freshness highlight",
                                                tint = QuickGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text("${etaMinutes}m", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = QuickGreenDark)
                                        }
                                        else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${selectedVariant.marginPercent.roundToInt()}%",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = QuickGold
                                            )
                                            Text("Margin", fontSize = 9.sp, color = QuickMuted)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)
            }

            // Delivery ETA + Brand Badge + Rating Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocalShipping,
                            contentDescription = "Delivery speed",
                            tint = Color(0xFF374151),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Get it in $etaMinutes mins",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF374151)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = QuickGreen,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${product.rating}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(3.dp))
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = QuickAmber,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${product.reviewCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickMuted
                        )
                    }
                }
            }

            item {
                HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 6.dp)
            }

            // Size Variants Selector
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Select Pack Size",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(product.variants, key = { it.id }) { variant ->
                            val isSelected = variant.id == selectedVariant.id
                            val varRate = FormatUtils.unitRateLabel(variant.sellingPrice, variant.label)
                            Surface(
                                modifier = Modifier
                                    .width(156.dp)
                                    .clickable { selectedVariantId = variant.id }
                                    .testTag("variant_chip_${variant.id}"),
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) QuickGreenContainer else Color.White,
                                border = BorderStroke(
                                    width = if (isSelected) 1.8.dp else 1.dp,
                                    color = if (isSelected) QuickGreen else Color(0xFFD1D5DB)
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = variant.label,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (variant.discountPercent > 0) {
                                            Text(
                                                text = "${variant.discountPercent}% OFF",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = QuickBlue
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = FormatUtils.inr(variant.sellingPrice),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        if (variant.mrp > variant.sellingPrice) {
                                            Spacer(Modifier.width(6.dp))
                                            Text(
                                                text = FormatUtils.inr(variant.mrp),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = QuickMuted,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        }
                                    }
                                    if (varRate != null) {
                                        Text(
                                            text = varRate,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = QuickMuted
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = if (variant.isInStock) {
                                            "Margin ${variant.marginPercent.roundToInt()}% • ${variant.stock} left"
                                        } else {
                                            "Out of stock"
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (variant.isInStock) QuickGreen else QuickRed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 6.dp)
            }

            // About this product collapsible card matching Screenshot 1
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { aboutExpanded = !aboutExpanded }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "About this product",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (aboutExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Toggle about this product",
                            tint = QuickMuted
                        )
                    }
                    AnimatedVisibility(visible = aboutExpanded) {
                        Column(
                            modifier = Modifier.padding(top = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (brand != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable(onClick = onSeeMoreBrandClick)
                                        .padding(vertical = 2.dp)
                                ) {
                                    BrandVisualIcon(brand = brand, size = 24.dp)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "Manufacturer: ${brand.name}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color(brand.colorHex)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Ratings & Reviews
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Ratings & Customer Reviews",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    product.reviews.forEach { review ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = review.authorName,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        if (review.isVerified) {
                                            Spacer(Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Rounded.Verified,
                                                contentDescription = "Verified buyer",
                                                tint = QuickGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Surface(
                                        color = QuickGreenContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${review.rating}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = QuickGreen
                                            )
                                            Icon(
                                                imageVector = Icons.Rounded.Star,
                                                contentDescription = null,
                                                tint = QuickGreen,
                                                modifier = Modifier.size(11.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = review.comment,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = review.dateLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuickMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
```

---

## File: `app/src/main/java/com/example/ui/components/ProductCard.kt`
```kotlin
package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.Product
import com.example.ui.theme.QuickAmberContainer
import com.example.ui.theme.QuickBlue
import com.example.ui.theme.QuickGold
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.util.FormatUtils
import kotlin.math.roundToInt

@Composable
fun ProductCard(
    product: Product,
    quantityInCart: Int,
    isNotified: Boolean,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    onNotifyClick: () -> Unit,
    modifier: Modifier = Modifier,
    brand: Brand? = null
) {
    val variant = product.defaultVariant
    val inStock = variant.isInStock

    Surface(
        modifier = modifier
            .testTag("product_card_${product.id}")
            .clickable(onClick = onProductClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
            ) {
                ProductVisual(
                    imageKey = product.imageKey,
                    colorHex = product.colorHex,
                    modifier = Modifier.matchParentSize(),
                    iconSize = 44.dp,
                    imageUrl = product.imageUrl,
                    brandName = brand?.name.orEmpty(),
                    brandLogoUrl = brand?.logoUrl.orEmpty(),
                    brandColorHex = brand?.colorHex ?: 0xFF0C831FL
                )

                if (variant.discountPercent > 0) {
                    Surface(
                        color = QuickBlue,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${variant.discountPercent}% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // Margin % indicator badge
                Surface(
                    color = QuickGreenContainer.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${variant.marginPercent.roundToInt()}% Margin",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuickGreen,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                if (!inStock) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "OUT OF STOCK",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Pack size + rating row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = variant.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Rating",
                        tint = QuickGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${product.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(36.dp)
            )

            Spacer(Modifier.height(6.dp))

            // Price & Add / Stepper / Notify Me row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = FormatUtils.inr(variant.sellingPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (variant.mrp > variant.sellingPrice) {
                        Text(
                            text = FormatUtils.inr(variant.mrp),
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                AnimatedContent(
                    targetState = Triple(inStock, quantityInCart > 0, isNotified),
                    transitionSpec = {
                        (fadeIn(tween(180)) + scaleIn(initialScale = 0.92f, animationSpec = tween(180)))
                            .togetherWith(fadeOut(tween(140)) + scaleOut(targetScale = 0.92f, animationSpec = tween(140)))
                            .using(SizeTransform(clip = false))
                    },
                    label = "product_card_action"
                ) { (stockAvail, hasQty, notified) ->
                    if (!stockAvail) {
                        OutlinedButton(
                            onClick = onNotifyClick,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                if (notified) QuickGreen else QuickGold
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (notified) QuickGreenContainer else QuickAmberContainer.copy(alpha = 0.45f),
                                contentColor = if (notified) QuickGreen else QuickGold
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("notify_button_${product.id}")
                        ) {
                            Icon(
                                imageVector = if (notified) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                                contentDescription = "Notify Me",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = if (notified) "Notified" else "Notify Me",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    } else if (!hasQty) {
                        OutlinedButton(
                            onClick = onAddClick,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.2.dp, QuickGreen),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = QuickGreenContainer.copy(alpha = 0.55f),
                                contentColor = QuickGreen
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("add_button_${product.id}")
                        ) {
                            Text(
                                text = "ADD",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    } else {
                        Surface(
                            color = QuickGreen,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                IconButton(
                                    onClick = onDecrementClick,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("dec_button_${product.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Decrease quantity",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "$quantityInCart",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = onIncrementClick,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("inc_button_${product.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "Increase quantity",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
```

---

## File: `app/src/main/java/com/example/ui/components/FilterSortSheet.kt`
```kotlin
package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.ProductFilterState
import com.example.model.SortOption
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer

private data class PriceBucket(
    val label: String,
    val min: Int?,
    val max: Int?
)

private val priceBuckets = listOf(
    PriceBucket("Any Price", null, null),
    PriceBucket("Under ₹50", null, 50),
    PriceBucket("₹50 – ₹150", 50, 150),
    PriceBucket("₹150 – ₹300", 150, 300),
    PriceBucket("Above ₹300", 300, null)
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSortBottomSheet(
    currentFilter: ProductFilterState,
    brands: List<Brand>,
    packSizes: List<String>,
    onApply: (ProductFilterState) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var draft by remember(currentFilter) { mutableStateOf(currentFilter) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters & Sort",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = {
                        draft = ProductFilterState(
                            query = draft.query,
                            categoryId = draft.categoryId,
                            brandId = draft.brandId,
                            subBrand = draft.subBrand,
                            shopCategory = draft.shopCategory,
                            subCategory = draft.subCategory
                        )
                    },
                    modifier = Modifier.testTag("reset_filters_button")
                ) {
                    Text("Clear All", color = QuickGreen)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sort by
                FilterSectionTitle("Sort By")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SortOption.entries.forEach { option ->
                        FilterChip(
                            selected = draft.sortOption == option,
                            onClick = { draft = draft.copy(sortOption = option) },
                            label = { Text(option.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Price Range
                FilterSectionTitle("Price Range (₹)")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    priceBuckets.forEach { bucket ->
                        val isSelected = draft.minPrice == bucket.min && draft.maxPrice == bucket.max
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                draft = draft.copy(minPrice = bucket.min, maxPrice = bucket.max)
                            },
                            label = { Text(bucket.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Discount %
                FilterSectionTitle("Minimum Discount")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0 to "Any", 10 to "10%+ OFF", 15 to "15%+ OFF", 20 to "20%+ OFF", 25 to "25%+ OFF").forEach { (pct, label) ->
                        FilterChip(
                            selected = draft.minDiscountPercent == pct,
                            onClick = { draft = draft.copy(minDiscountPercent = pct) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Rating
                FilterSectionTitle("Customer Rating")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0.0 to "All Ratings", 4.0 to "4.0★ & above", 4.5 to "4.5★ & above", 4.7 to "4.7★ & above").forEach { (r, label) ->
                        FilterChip(
                            selected = draft.minRating == r,
                            onClick = { draft = draft.copy(minRating = r) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Brand
                FilterSectionTitle("Brand")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    brands.forEach { brand ->
                        val selected = brand.id in draft.selectedBrands
                        FilterChip(
                            selected = selected,
                            onClick = {
                                val next = if (selected) draft.selectedBrands - brand.id else draft.selectedBrands + brand.id
                                draft = draft.copy(selectedBrands = next)
                            },
                            label = { Text(brand.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Pack Size
                FilterSectionTitle("Pack Size")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    packSizes.take(18).forEach { pack ->
                        val selected = pack in draft.selectedPackSizes
                        FilterChip(
                            selected = selected,
                            onClick = {
                                val next = if (selected) draft.selectedPackSizes - pack else draft.selectedPackSizes + pack
                                draft = draft.copy(selectedPackSizes = next)
                            },
                            label = { Text(pack) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // In Stock Only
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Show In-Stock Items Only",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = draft.inStockOnly,
                        onCheckedChange = { draft = draft.copy(inStockOnly = it) }
                    )
                }

                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { onApply(draft) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("apply_filters_button")
                ) {
                    Text("Apply Filters", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FilterSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}
```

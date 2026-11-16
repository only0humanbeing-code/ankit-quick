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

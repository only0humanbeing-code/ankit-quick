package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.model.Product
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.ProductVisual
import com.example.ui.components.SubBrandVisualCard
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickOutline
import com.example.ui.theme.QuickSlate
import com.example.ui.theme.QuickSlateSecondary
import com.example.util.FormatUtils

private data class SubBrandGroupSummary(
    val subBrandName: String,
    val parentBrand: Brand,
    val productCount: Int
)

private data class ManufacturerDirectorySummary(
    val brand: Brand,
    val totalProducts: Int,
    val subBrands: List<SubBrandGroupSummary>
)

@Composable
fun ShopByManufacturerScreen(
    brands: List<Brand>,
    products: List<Product>,
    onBackClick: () -> Unit,
    onManufacturerClick: (Brand) -> Unit,
    onViewCompanyAllProducts: (Brand) -> Unit,
    onSubBrandClick: (brandId: String, subBrandName: String) -> Unit,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val brandMap = remember(brands) { brands.associateBy { it.id } }

    val directorySummaries = remember(brands, products) {
        val groupedProducts = products.groupBy { it.brandId }
        brands.map { brand ->
            val brandProducts = groupedProducts[brand.id].orEmpty()
            val subGroups = brandProducts
                .groupBy { it.subBrand.ifBlank { brand.name } }
                .map { (subName, list) ->
                    SubBrandGroupSummary(
                        subBrandName = subName,
                        parentBrand = brand,
                        productCount = list.size
                    )
                }
                .sortedBy { it.subBrandName }
            ManufacturerDirectorySummary(
                brand = brand,
                totalProducts = brandProducts.size,
                subBrands = subGroups
            )
        }
    }

    val allSubBrandSummaries = remember(directorySummaries) {
        directorySummaries.flatMap { it.subBrands }
    }

    val trimmedQuery = searchQuery.trim()

    val matchingCompanies = remember(directorySummaries, products, trimmedQuery) {
        if (trimmedQuery.isBlank()) {
            directorySummaries
        } else {
            directorySummaries.filter { summary ->
                summary.brand.name.contains(trimmedQuery, ignoreCase = true) ||
                    summary.brand.tagline.contains(trimmedQuery, ignoreCase = true) ||
                    summary.subBrands.any { it.subBrandName.contains(trimmedQuery, ignoreCase = true) } ||
                    products.any {
                        it.brandId == summary.brand.id &&
                            it.name.contains(trimmedQuery, ignoreCase = true)
                    }
            }
        }
    }

    val matchingSubBrands = remember(allSubBrandSummaries, trimmedQuery) {
        if (trimmedQuery.isBlank()) {
            emptyList()
        } else {
            allSubBrandSummaries.filter {
                it.subBrandName.contains(trimmedQuery, ignoreCase = true) ||
                    it.parentBrand.name.contains(trimmedQuery, ignoreCase = true)
            }
        }
    }

    val matchingProducts = remember(products, brandMap, trimmedQuery) {
        if (trimmedQuery.isBlank()) {
            emptyList()
        } else {
            products.filter { prod ->
                prod.name.contains(trimmedQuery, ignoreCase = true) ||
                    prod.subBrand.contains(trimmedQuery, ignoreCase = true) ||
                    (brandMap[prod.brandId]?.name?.contains(trimmedQuery, ignoreCase = true) == true)
            }.take(30)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Sticky Green Header with integrated Manufacturer / Brand / Product Search Bar
        Surface(
            color = QuickGreen,
            contentColor = Color.White,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("shop_by_manufacturer_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Shop by Manufacturer",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${brands.size} Companies • ${allSubBrandSummaries.size} Brands • ${products.size} Products",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search company, brand, or product (HUL, Dove, ITC)...",
                            fontSize = 13.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = QuickGreen
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Clear search",
                                    tint = QuickMuted
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(11.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = QuickSlate,
                        unfocusedTextColor = QuickSlate
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .testTag("manufacturer_directory_search_input")
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (trimmedQuery.isNotBlank()) {
                // Search Results: Matching Brands Strip
                if (matchingSubBrands.isNotEmpty()) {
                    item(key = "search_subbrands_header") {
                        Text(
                            text = "Matching Brands (${matchingSubBrands.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = QuickSlate
                        )
                    }
                    item(key = "search_subbrands_row") {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                matchingSubBrands,
                                key = { "${it.parentBrand.id}_${it.subBrandName}" }
                            ) { sub ->
                                Surface(
                                    onClick = { onSubBrandClick(sub.parentBrand.id, sub.subBrandName) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, QuickOutline),
                                    modifier = Modifier
                                        .width(106.dp)
                                        .testTag("directory_subbrand_${sub.subBrandName}")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        SubBrandVisualCard(
                                            subBrandName = sub.subBrandName,
                                            manufacturer = sub.parentBrand,
                                            modifier = Modifier.size(66.dp)
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = sub.subBrandName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuickSlate,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${sub.parentBrand.name} • ${sub.productCount}",
                                            fontSize = 10.sp,
                                            color = QuickMuted,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Search Results: Matching Companies
                if (matchingCompanies.isNotEmpty()) {
                    item(key = "search_companies_header") {
                        Text(
                            text = "Companies (${matchingCompanies.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = QuickSlate
                        )
                    }
                }
            }

            if (matchingCompanies.isEmpty() && matchingSubBrands.isEmpty() && matchingProducts.isEmpty()) {
                item(key = "empty_search") {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, QuickOutline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No companies, brands, or products matched \"$trimmedQuery\"",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuickSlate,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedButton(onClick = { searchQuery = "" }) {
                                Text("Show All Manufacturers", color = QuickGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(matchingCompanies, key = { "company_${it.brand.id}" }) { summary ->
                    ManufacturerDirectoryCard(
                        summary = summary,
                        onManufacturerClick = { onManufacturerClick(summary.brand) },
                        onViewCompanyAllProducts = { onViewCompanyAllProducts(summary.brand) },
                        onSubBrandClick = { subName ->
                            onSubBrandClick(summary.brand.id, subName)
                        }
                    )
                }
            }

            // Search Results: Matching Products directly
            if (trimmedQuery.isNotBlank() && matchingProducts.isNotEmpty()) {
                item(key = "search_products_header") {
                    Text(
                        text = "Matching Products (${matchingProducts.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = QuickSlate
                    )
                }
                items(matchingProducts, key = { "prod_${it.id}" }) { prod ->
                    val parentBrand = brandMap[prod.brandId]
                    val variant = prod.defaultVariant
                    val subLogoUrl = resolveSubBrandLogoUrl(prod.subBrand, parentBrand?.logoUrl.orEmpty())
                    Surface(
                        onClick = { onProductClick(prod) },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, QuickOutline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("directory_product_${prod.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductVisual(
                                imageKey = prod.imageKey,
                                colorHex = prod.colorHex,
                                iconSize = 26.dp,
                                imageUrl = prod.imageUrl,
                                brandName = prod.subBrand.ifBlank { parentBrand?.name.orEmpty() },
                                brandLogoUrl = subLogoUrl,
                                brandColorHex = parentBrand?.colorHex ?: 0xFF0C831FL,
                                modifier = Modifier.size(62.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${prod.subBrand.ifBlank { parentBrand?.name.orEmpty() }} • ${parentBrand?.name.orEmpty()}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = QuickGreen,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = prod.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuickSlate,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${variant.label} • ",
                                        fontSize = 12.sp,
                                        color = QuickMuted
                                    )
                                    Text(
                                        text = FormatUtils.inr(variant.sellingPrice),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = QuickSlate
                                    )
                                    if (variant.mrp > variant.sellingPrice) {
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = FormatUtils.inr(variant.mrp),
                                            fontSize = 11.sp,
                                            color = QuickMuted,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Open product",
                                tint = QuickMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManufacturerDirectoryCard(
    summary: ManufacturerDirectorySummary,
    onManufacturerClick: () -> Unit,
    onViewCompanyAllProducts: () -> Unit,
    onSubBrandClick: (String) -> Unit
) {
    val brand = summary.brand
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, QuickOutline),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("directory_company_${brand.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Company Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onManufacturerClick)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BrandVisualIcon(
                    brand = brand,
                    size = 58.dp
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = brand.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = QuickSlate,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            color = QuickGreenContainer,
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = brand.badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuickGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = brand.tagline,
                        fontSize = 12.sp,
                        color = QuickSlateSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "${summary.subBrands.size} Brand Lines • ${summary.totalProducts} Products",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = QuickGreen
                    )
                }
            }

            // Sub-Brand Quick Preview Strip
            if (summary.subBrands.isNotEmpty()) {
                val previewSubBrands = remember(summary.subBrands) { summary.subBrands.take(8) }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(previewSubBrands, key = { "${brand.id}_${it.subBrandName}" }) { sub ->
                        Column(
                            modifier = Modifier
                                .width(66.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSubBrandClick(sub.subBrandName) }
                                .padding(vertical = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SubBrandVisualCard(
                                subBrandName = sub.subBrandName,
                                manufacturer = brand,
                                modifier = Modifier.size(58.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = sub.subBrandName,
                                fontSize = 10.5.sp,
                                lineHeight = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = QuickSlate,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            HorizontalDivider(color = QuickOutline)

            // Action Buttons: All Company Products + View All Brands
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewCompanyAllProducts,
                    shape = RoundedCornerShape(9.dp),
                    border = BorderStroke(1.dp, QuickGreen),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("directory_all_products_${brand.id}")
                ) {
                    Text(
                        text = "All Products (${summary.totalProducts})",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuickGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(
                    onClick = onManufacturerClick,
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("directory_view_brands_${brand.id}")
                ) {
                    Text(
                        text = "View Brands (${summary.subBrands.size})",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

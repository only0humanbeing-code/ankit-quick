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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.Product
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.CrispInternetLogoImage
import com.example.ui.components.ProductVisual
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickMuted

data class SubBrandEntry(
    val name: String,
    val representativeProduct: Product,
    val productCount: Int
)

@Composable
fun ManufacturerBrandsScreen(
    manufacturer: Brand,
    productsForManufacturer: List<Product>,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSubBrandClick: (SubBrandEntry) -> Unit,
    onViewAllManufacturerProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subBrands = productsForManufacturer
        .groupBy { it.subBrand.ifBlank { manufacturer.name } }
        .map { (subName, items) ->
            SubBrandEntry(
                name = subName,
                representativeProduct = items.first(),
                productCount = items.size
            )
        }
        .sortedBy { it.name }

    val brandColor = Color(manufacturer.colorHex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Green Manufacturer Header with crisp manufacturer logo emblem
        Surface(
            color = QuickGreen,
            contentColor = Color.White,
            tonalElevation = 4.dp
        ) {
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
                    size = 36.dp
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
                    onClick = onSearchClick,
                    modifier = Modifier.testTag("manufacturer_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Shop by Brand",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = onViewAllManufacturerProducts,
                        modifier = Modifier.testTag("manufacturer_view_all_button")
                    ) {
                        Text(
                            text = "All ${productsForManufacturer.size} Items →",
                            color = QuickGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                val rows = subBrands.chunked(4)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { entry ->
                                SubBrandTile(
                                    entry = entry,
                                    manufacturer = manufacturer,
                                    accentColor = brandColor,
                                    onClick = { onSubBrandClick(entry) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(4 - rowItems.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubBrandTile(
    entry: SubBrandEntry,
    manufacturer: Brand,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val logoUrl = resolveSubBrandLogoUrl(entry.name, manufacturer.logoUrl)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
            .testTag("sub_brand_tile_${entry.name}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFFFF5EB),
            shadowElevation = 0.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                CrispInternetLogoImage(
                    url = logoUrl,
                    contentDescription = "${entry.name} logo",
                    contentScale = ContentScale.Fit,
                    minIntrinsicPx = 64f,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    ProductVisual(
                        imageKey = entry.representativeProduct.imageKey,
                        colorHex = entry.representativeProduct.colorHex,
                        iconSize = 26.dp,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = accentColor,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${entry.productCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = entry.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "${entry.productCount} item${if (entry.productCount == 1) "" else "s"}",
            style = MaterialTheme.typography.labelSmall,
            color = QuickMuted,
            maxLines = 1
        )
    }
}

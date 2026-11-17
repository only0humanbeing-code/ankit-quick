package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.Brand
import com.example.model.CartSummary
import com.example.model.StoreConfig
import com.example.ui.components.ProductVisual
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.util.FormatUtils

@Composable
fun CartScreen(
    cartSummary: CartSummary,
    storeConfig: StoreConfig,
    onIncrement: (productId: String, variantId: String) -> Unit,
    onDecrement: (productId: String, variantId: String) -> Unit,
    onClearCart: () -> Unit,
    onBrowseProducts: () -> Unit,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    brands: List<Brand> = emptyList()
) {
    val brandMap = androidx.compose.runtime.remember(brands) { brands.associateBy { it.id } }
    Column(modifier = modifier.fillMaxSize()) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Cart",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (cartSummary.totalCount > 0) {
                            "${cartSummary.totalCount} item(s) • Delivery in ${storeConfig.etaMinutes} mins"
                        } else {
                            "Fastest 10-min grocery delivery"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = QuickMuted
                    )
                }
                if (cartSummary.items.isNotEmpty()) {
                    TextButton(
                        onClick = onClearCart,
                        modifier = Modifier.testTag("clear_cart_button")
                    ) {
                        Icon(Icons.Rounded.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Clear")
                    }
                }
            }
        }

        if (cartSummary.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img_empty_cart),
                        contentDescription = "Empty cart",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(190.dp)
                            .clip(RoundedCornerShape(24.dp))
                    )
                    Spacer(Modifier.height(18.dp))
                    Text(
                        text = "Your QuickKart basket is empty",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Add fresh veggies, milk, snacks, and daily essentials to get 10-min delivery.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuickMuted
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = onBrowseProducts,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("empty_cart_browse_button")
                    ) {
                        Text("Browse Groceries", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Free delivery progress banner
                item {
                    val progress = (cartSummary.itemTotal / cartSummary.freeDeliveryThreshold)
                        .toFloat()
                        .coerceIn(0f, 1f)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = QuickGreenContainer,
                        border = BorderStroke(1.dp, QuickGreen.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Bolt,
                                    contentDescription = null,
                                    tint = QuickGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = if (cartSummary.amountForFreeDelivery > 0.0) {
                                        "Add ${FormatUtils.inr(cartSummary.amountForFreeDelivery)} more for FREE delivery!"
                                    } else {
                                        "Yay! You unlocked FREE delivery on this order!"
                                    },
                                    style = MaterialTheme.typography.titleSmall,
                                    color = QuickGreen
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                color = QuickGreen,
                                trackColor = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                        }
                    }
                }

                // Savings banner
                if (cartSummary.itemSavings > 0) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocalOffer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Total Savings: ${FormatUtils.inr(cartSummary.itemSavings)} saved on MRP!",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                // Cart line items
                items(cartSummary.items, key = { "${it.product.id}_${it.variant.id}" }) { item ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val itemBrand = brandMap[item.product.brandId]
                            ProductVisual(
                                imageKey = item.product.imageKey,
                                colorHex = item.product.colorHex,
                                iconSize = 28.dp,
                                imageUrl = item.product.imageUrl,
                                brandName = item.product.subBrand.ifBlank { itemBrand?.name.orEmpty() },
                                brandLogoUrl = resolveSubBrandLogoUrl(item.product.subBrand, itemBrand?.logoUrl.orEmpty()),
                                brandColorHex = itemBrand?.colorHex ?: 0xFF0C831FL,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.product.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 2
                                )
                                Text(
                                    text = item.variant.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = QuickMuted
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = FormatUtils.inr(item.lineSellingTotal),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (item.lineMrpTotal > item.lineSellingTotal) {
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = FormatUtils.inr(item.lineMrpTotal),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = QuickMuted,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }
                                }
                            }

                            Surface(
                                color = QuickGreen,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onDecrement(item.product.id, item.variant.id) },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            Icons.Rounded.Remove,
                                            contentDescription = "Decrease",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White
                                    )
                                    IconButton(
                                        onClick = { onIncrement(item.product.id, item.variant.id) },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            Icons.Rounded.Add,
                                            contentDescription = "Increase",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bill Details Card
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Bill Details",
                                style = MaterialTheme.typography.titleMedium
                            )
                            BillRow("MRP Total", FormatUtils.inr(cartSummary.mrpTotal))
                            BillRow(
                                "Product Discount Savings",
                                "-${FormatUtils.inr(cartSummary.itemSavings)}",
                                valueColor = QuickGreen
                            )
                            BillRow("Item Total", FormatUtils.inr(cartSummary.itemTotal))
                            BillRow(
                                "Delivery Charge",
                                if (cartSummary.deliveryFee == 0.0) "FREE" else FormatUtils.inr(cartSummary.deliveryFee),
                                valueColor = if (cartSummary.deliveryFee == 0.0) QuickGreen else MaterialTheme.colorScheme.onSurface
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Grand Total",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = FormatUtils.inr(cartSummary.grandTotal),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Checkout CTA
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = FormatUtils.inr(cartSummary.grandTotal),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "TOTAL • SAVING ${FormatUtils.inr(cartSummary.itemSavings)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuickGreen
                        )
                    }
                    Button(
                        onClick = onCheckoutClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("proceed_to_checkout_button")
                    ) {
                        Text("Proceed to Checkout", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BillRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = QuickMuted)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

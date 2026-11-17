package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ShopCategoryTaxonomy
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.OrderRecord
import com.example.model.Product
import kotlin.math.roundToInt

private val PastOrderBadgeRed = Color(0xFFC81E1E)
private val PastOrderAddYellow = Color(0xFFFACC15)
private val PastOrderShelfGray = Color(0xFFF2F4F6)

@Composable
fun PastOrdersCarouselSection(
    products: List<Product>,
    brands: List<Brand>,
    orders: List<OrderRecord>,
    cartItems: List<CartItem>,
    onProductClick: (Product) -> Unit,
    onAddClick: (Product) -> Unit,
    onIncrementClick: (Product) -> Unit,
    onDecrementClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty()) return

    val categoryTabs = ShopCategoryTaxonomy.groups
    var selectedGroupId by rememberSaveable { mutableStateOf(categoryTabs.first().id) }
    val activeGroup = categoryTabs.firstOrNull { it.id == selectedGroupId } ?: categoryTabs.first()

    val productRowState = rememberLazyListState()

    LaunchedEffect(activeGroup.id) {
        productRowState.scrollToItem(0)
    }

    val brandMap = remember(brands) { brands.associateBy { it.id } }

    val cartQtyMap = remember(cartItems) {
        cartItems.associate { "${it.productId}_${it.variantId}" to it.quantity }
    }

    val pastOrderedIds = remember(orders) {
        orders.flatMap { order -> order.items.map { it.productId } }.toSet()
    }

    val tabProducts = remember(products, pastOrderedIds, activeGroup.id) {
        val inStockAndPastFirst = Comparator<Product> { a, b ->
            val aPast = if (a.id in pastOrderedIds) 1 else 0
            val bPast = if (b.id in pastOrderedIds) 1 else 0
            if (aPast != bPast) return@Comparator bPast - aPast
            val aStock = if (a.defaultVariant.isInStock) 1 else 0
            val bStock = if (b.defaultVariant.isInStock) 1 else 0
            if (aStock != bStock) return@Comparator bStock - aStock
            val aFeat = if (a.isFeatured) 1 else 0
            val bFeat = if (b.isFeatured) 1 else 0
            bFeat - aFeat
        }

        val filtered = products
            .filter { it.shopCategory == activeGroup.title }
            .sortedWith(inStockAndPastFirst)

        (if (filtered.isNotEmpty()) filtered else products.sortedWith(inStockAndPastFirst)).take(18)
    }

    val productColumns = remember(tabProducts) {
        tabProducts.chunked(2)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .testTag("past_orders_section")
    ) {
        Text(
            text = "Based on your Past Orders",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )

        // Right-to-left horizontally scrollable Category Tab Strip using ONLY ShopCategoryTaxonomy.groups
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("past_orders_category_tabs")
        ) {
            items(categoryTabs, key = { it.id }) { group ->
                val selected = group.id == activeGroup.id
                Column(
                    modifier = Modifier
                        .width(90.dp)
                        .height(104.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .background(
                            if (selected) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFF6FEF8),
                                        Color(0xFFD5F9DF),
                                        Color(0xFFC1F4CF)
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    colors = listOf(Color.White, Color.White)
                                )
                            }
                        )
                        .clickable { selectedGroupId = group.id }
                        .padding(horizontal = 5.dp, vertical = 6.dp)
                        .testTag("past_orders_tab_${group.id}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    PastOrderTabArtwork(
                        iconKey = group.iconKey,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = group.title,
                        fontSize = 11.sp,
                        lineHeight = 13.sp,
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = if (selected) Color(0xFF111827) else Color(0xFF6B7280),
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // 2-Row Horizontally Scrollable Product Shelf (swipe right-to-left)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PastOrderShelfGray)
                .padding(vertical = 12.dp)
        ) {
            LazyRow(
                state = productRowState,
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("past_orders_product_row")
            ) {
                items(
                    items = productColumns,
                    key = { col -> col.joinToString("_") { it.id } }
                ) { columnProducts ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        columnProducts.forEach { product ->
                            val variant = product.defaultVariant
                            val qty = cartQtyMap["${product.id}_${variant.id}"] ?: 0
                            PastOrderShelfProductCard(
                                product = product,
                                brand = brandMap[product.brandId],
                                quantityInCart = qty,
                                onProductClick = { onProductClick(product) },
                                onAddClick = { onAddClick(product) },
                                onIncrementClick = { onIncrementClick(product) },
                                onDecrementClick = { onDecrementClick(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PastOrderShelfProductCard(
    product: Product,
    brand: Brand?,
    quantityInCart: Int,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit
) {
    val variant = product.defaultVariant
    val inStock = variant.isInStock

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 0.5.dp,
        modifier = Modifier
            .width(124.dp)
            .clickable(onClick = onProductClick)
            .testTag("past_order_card_${product.id}")
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProductVisual(
                        imageKey = product.imageKey,
                        colorHex = product.colorHex,
                        iconSize = 36.dp,
                        imageUrl = product.imageUrl,
                        brandName = product.subBrand.ifBlank { brand?.name.orEmpty() },
                        brandLogoUrl = resolveSubBrandLogoUrl(product.subBrand, brand?.logoUrl.orEmpty()),
                        brandColorHex = brand?.colorHex ?: 0xFF0C831FL,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.height(7.dp))

                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(31.dp)
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = variant.label,
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(5.dp))

                // Price Row (leaves space on bottom-right for yellow + circle button)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 30.dp)
                ) {
                    Text(
                        text = "₹",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                    Text(
                        text = "${variant.sellingPrice.roundToInt()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF111827)
                    )
                    if (variant.mrp > variant.sellingPrice) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "₹${variant.mrp.roundToInt()}",
                            fontSize = 10.5.sp,
                            color = Color(0xFF9CA3AF),
                            textDecoration = TextDecoration.LineThrough,
                            maxLines = 1
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))
            }

            // Top-Left Crimson Shield Discount Badge
            if (variant.discountPercent > 0) {
                Surface(
                    color = PastOrderBadgeRed,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 4.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 12.dp
                    ),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "${variant.discountPercent}%\nOFF",
                        fontSize = 9.5.sp,
                        lineHeight = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }

            // Bottom-Right Signature Yellow Circular "+" Button / Live Stepper
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
            ) {
                AnimatedContent(
                    targetState = inStock to (quantityInCart > 0),
                    transitionSpec = {
                        (fadeIn(tween(150)) + scaleIn(initialScale = 0.9f, animationSpec = tween(150)))
                            .togetherWith(fadeOut(tween(120)) + scaleOut(targetScale = 0.9f, animationSpec = tween(120)))
                            .using(SizeTransform(clip = false))
                    },
                    label = "past_order_add_action"
                ) { (stockAvailable, hasCartQty) ->
                    if (!stockAvailable) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE5E7EB)
                        ) {
                            Text(
                                text = "OOS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                            )
                        }
                    } else if (!hasCartQty) {
                        Surface(
                            onClick = onAddClick,
                            shape = CircleShape,
                            color = PastOrderAddYellow,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("past_order_add_${product.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "Add ${product.name}",
                                    tint = Color(0xFF111827),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PastOrderAddYellow,
                            shadowElevation = 2.dp,
                            modifier = Modifier.height(30.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(1.dp),
                                modifier = Modifier.padding(horizontal = 2.dp)
                            ) {
                                IconButton(
                                    onClick = onDecrementClick,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Decrease ${product.name}",
                                        tint = Color(0xFF111827),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "$quantityInCart",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF111827)
                                )
                                IconButton(
                                    onClick = onIncrementClick,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "Increase ${product.name}",
                                        tint = Color(0xFF111827),
                                        modifier = Modifier.size(14.dp)
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

@Composable
private fun PastOrderTabArtwork(
    iconKey: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        fun drawMiniPouch(
            left: Float,
            top: Float,
            pw: Float,
            ph: Float,
            c1: Color,
            c2: Color,
            bandColor: Color
        ) {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(c1, c2), startY = top, endY = top + ph),
                topLeft = Offset(left, top),
                size = Size(pw, ph),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.90f),
                topLeft = Offset(left + pw * 0.14f, top + ph * 0.28f),
                size = Size(pw * 0.72f, ph * 0.36f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = bandColor,
                topLeft = Offset(left + pw * 0.18f, top + ph * 0.36f),
                size = Size(pw * 0.64f, ph * 0.16f),
                cornerRadius = CornerRadius(3f, 3f)
            )
        }

        fun drawMiniBottle(
            left: Float,
            top: Float,
            bw: Float,
            bh: Float,
            capColor: Color,
            bodyColor: Color,
            labelColor: Color
        ) {
            drawRoundRect(
                color = capColor,
                topLeft = Offset(left + bw * 0.28f, top),
                size = Size(bw * 0.44f, bh * 0.16f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = bodyColor,
                topLeft = Offset(left, top + bh * 0.15f),
                size = Size(bw, bh * 0.85f),
                cornerRadius = CornerRadius(9f, 9f)
            )
            drawRoundRect(
                color = labelColor,
                topLeft = Offset(left + bw * 0.16f, top + bh * 0.42f),
                size = Size(bw * 0.68f, bh * 0.30f),
                cornerRadius = CornerRadius(3f, 3f)
            )
        }

        when (iconKey) {
            "electronics" -> {
                // Sleek electric kettle + smart appliance cluster
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF0F172A))),
                    topLeft = Offset(w * 0.14f, h * 0.18f),
                    size = Size(w * 0.44f, h * 0.70f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFF38BDF8),
                    topLeft = Offset(w * 0.20f, h * 0.28f),
                    size = Size(w * 0.32f, h * 0.26f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFF94A3B8))),
                    topLeft = Offset(w * 0.52f, h * 0.28f),
                    size = Size(w * 0.36f, h * 0.60f),
                    cornerRadius = CornerRadius(7f, 7f)
                )
                drawCircle(color = Color(0xFF0EA5E9), radius = w * 0.06f, center = Offset(w * 0.70f, h * 0.64f))
            }
            "fruits_veg" -> {
                // Fresh leafy greens + red apple & mango basket
                drawCircle(color = Color(0xFF16A34A), radius = w * 0.22f, center = Offset(w * 0.34f, h * 0.42f))
                drawCircle(color = Color(0xFFEF4444), radius = w * 0.18f, center = Offset(w * 0.62f, h * 0.46f))
                drawCircle(color = Color(0xFFFACC15), radius = w * 0.15f, center = Offset(w * 0.48f, h * 0.56f))
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFD97706), Color(0xFF92400E))),
                    topLeft = Offset(w * 0.14f, h * 0.56f),
                    size = Size(w * 0.72f, h * 0.32f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
            }
            "foodgrains" -> {
                // Wheat atta sack + yellow cooking oil jerrycan
                drawMiniPouch(w * 0.10f, h * 0.14f, w * 0.44f, h * 0.74f, Color(0xFFDC2626), Color(0xFF991B1B), Color(0xFFFACC15))
                drawMiniBottle(w * 0.50f, h * 0.18f, w * 0.40f, h * 0.70f, Color(0xFFDC2626), Color(0xFFFACC15), Color(0xFF15803D))
            }
            "bakery_dairy" -> {
                // Golden bread pack + chilled milk carton & frozen tub
                drawMiniPouch(w * 0.10f, h * 0.16f, w * 0.44f, h * 0.72f, Color(0xFFF59E0B), Color(0xFFB45309), Color(0xFF78350F))
                drawMiniPouch(w * 0.50f, h * 0.22f, w * 0.38f, h * 0.66f, Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFFDC2626))
            }
            "beverages" -> {
                // Tea pouch + roasted coffee jar
                drawMiniPouch(w * 0.12f, h * 0.12f, w * 0.44f, h * 0.76f, Color(0xFFD97706), Color(0xFF92400E), Color(0xFF15803D))
                drawMiniBottle(w * 0.52f, h * 0.22f, w * 0.36f, h * 0.66f, Color(0xFFDC2626), Color(0xFF451A03), Color(0xFFEF4444))
            }
            "snacks_branded" -> {
                // Crimson snack pack + iconic yellow Maggi noodles pack
                drawMiniPouch(w * 0.10f, h * 0.12f, w * 0.42f, h * 0.76f, Color(0xFFDC2626), Color(0xFF7F1D1D), Color(0xFFFACC15))
                drawMiniPouch(w * 0.46f, h * 0.18f, w * 0.44f, h * 0.72f, Color(0xFFFACC15), Color(0xFFEAB308), Color(0xFFDC2626))
            }
            "beauty_hygiene" -> {
                // Body Care & Beauty: pastel glow circle + lotion bottle & pink beauty tube
                drawCircle(color = Color(0xFFDFF7EA), radius = size.minDimension * 0.44f, center = Offset(w * 0.5f, h * 0.52f))
                drawMiniBottle(w * 0.20f, h * 0.16f, w * 0.30f, h * 0.70f, Color(0xFF1E3A8A), Color(0xFFF59E0B), Color.White)
                drawMiniBottle(w * 0.52f, h * 0.24f, w * 0.28f, h * 0.62f, Color(0xFF9D174D), Color(0xFFEC4899), Color(0xFFFCE7F3))
            }
            "cleaning_household" -> {
                // Blue detergent pouch + disinfectant bottle
                drawMiniPouch(w * 0.12f, h * 0.14f, w * 0.44f, h * 0.74f, Color(0xFF2563EB), Color(0xFF1E3A8A), Color(0xFF22C55E))
                drawMiniBottle(w * 0.52f, h * 0.18f, w * 0.36f, h * 0.70f, Color(0xFFDC2626), Color(0xFFFACC15), Color(0xFF1D4ED8))
            }
            "kitchen_garden_pets" -> {
                // Stainless steel pot + leafy green planter pot
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFF64748B))),
                    topLeft = Offset(w * 0.12f, h * 0.32f),
                    size = Size(w * 0.44f, h * 0.54f),
                    cornerRadius = CornerRadius(7f, 7f)
                )
                drawCircle(color = Color(0xFF22C55E), radius = w * 0.16f, center = Offset(w * 0.68f, h * 0.36f))
                drawRoundRect(
                    color = Color(0xFFC2410C),
                    topLeft = Offset(w * 0.54f, h * 0.50f),
                    size = Size(w * 0.28f, h * 0.36f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
            }
            "eggs_meat_fish" -> {
                // Chilled tender cut pack + golden farm eggs
                drawMiniPouch(w * 0.12f, h * 0.18f, w * 0.46f, h * 0.68f, Color(0xFFEF4444), Color(0xFF991B1B), Color(0xFFFEF2F2))
                drawCircle(color = Color(0xFFFDE68A), radius = w * 0.15f, center = Offset(w * 0.64f, h * 0.66f))
                drawCircle(color = Color(0xFFFCD34D), radius = w * 0.14f, center = Offset(w * 0.76f, h * 0.68f))
            }
            "gourmet_world" -> {
                // Imported pasta box + dark olive oil bottle
                drawMiniPouch(w * 0.12f, h * 0.14f, w * 0.44f, h * 0.74f, Color(0xFFB91C1C), Color(0xFF7F1D1D), Color(0xFFFACC15))
                drawMiniBottle(w * 0.54f, h * 0.14f, w * 0.32f, h * 0.74f, Color(0xFFD97706), Color(0xFF14532D), Color(0xFFFEF9C3))
            }
            "baby_care" -> {
                // Baby Care, Female & Hygiene: teal diaper pack + pink hygiene pack
                drawMiniPouch(w * 0.10f, h * 0.14f, w * 0.46f, h * 0.74f, Color(0xFF14B8A6), Color(0xFF0F766E), Color(0xFFFACC15))
                drawMiniPouch(w * 0.50f, h * 0.24f, w * 0.40f, h * 0.64f, Color(0xFFF472B6), Color(0xFFDB2777), Color.White)
            }
            "stationery_toys_gifts" -> {
                // Spiral notebook + festive ribbon gift box
                drawMiniPouch(w * 0.12f, h * 0.14f, w * 0.42f, h * 0.74f, Color(0xFF3B82F6), Color(0xFF1D4ED8), Color(0xFFFACC15))
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFA855F7), Color(0xFF6B21A8))),
                    topLeft = Offset(w * 0.48f, h * 0.34f),
                    size = Size(w * 0.40f, h * 0.54f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(w * 0.64f, h * 0.34f),
                    size = Size(w * 0.08f, h * 0.54f),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
            "paper_disposables" -> {
                // Tissue box + stack of eco paper cups
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7))),
                    topLeft = Offset(w * 0.10f, h * 0.42f),
                    size = Size(w * 0.48f, h * 0.44f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawCircle(color = Color.White, radius = w * 0.14f, center = Offset(w * 0.34f, h * 0.36f))
                drawMiniPouch(w * 0.54f, h * 0.20f, w * 0.34f, h * 0.66f, Color(0xFFFEF3C7), Color(0xFFD97706), Color(0xFF16A34A))
            }
            "industrial_catering" -> {
                // Large 15L catering tin + commercial spout pouch
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFDE047), Color(0xFFCA8A04))),
                    topLeft = Offset(w * 0.12f, h * 0.14f),
                    size = Size(w * 0.46f, h * 0.74f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0xFFDC2626),
                    topLeft = Offset(w * 0.18f, h * 0.38f),
                    size = Size(w * 0.34f, h * 0.24f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawMiniPouch(w * 0.52f, h * 0.26f, w * 0.38f, h * 0.62f, Color(0xFFE2E8F0), Color(0xFF94A3B8), Color(0xFF0F766E))
            }
            else -> {
                // Ayurveda, Health & Wellness: crimson Chyawanprash jar + green herbal bottle
                drawMiniBottle(w * 0.14f, h * 0.18f, w * 0.42f, h * 0.70f, Color(0xFFFACC15), Color(0xFFB91C1C), Color(0xFFFEF3C7))
                drawMiniBottle(w * 0.54f, h * 0.14f, w * 0.34f, h * 0.74f, Color(0xFF14532D), Color(0xFF16A34A), Color.White)
            }
        }
    }
}

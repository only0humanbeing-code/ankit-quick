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

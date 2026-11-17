# 06 — Home, Shop-By-Category & Brand Screens

Contains `HomeScreen.kt`, `ShopByCategoryScreen.kt`, `ManufacturerBrandsScreen.kt`, and `BrandProductsScreen.kt`.

---

## File: `app/src/main/java/com/example/ui/screens/HomeScreen.kt`
```kotlin
package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Brand
import com.example.model.Category
import com.example.model.SavedAddress
import com.example.model.StoreConfig
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.HomeCategoryClusterArtwork
import com.example.ui.components.QuickKartLogoBadge
import com.example.ui.theme.QuickAmber
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenDark
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickOutline
import com.example.ui.theme.QuickSlate
import com.example.ui.theme.QuickSlateSecondary
import kotlinx.coroutines.delay

private data class HeroBannerData(
    val title: String,
    val subtitle: String,
    val badge: String,
    val ctaText: String,
    val imageRes: Int,
    val categoryId: String
)

private data class HomeShopCategoryTile(
    val id: String,
    val label: String,
    val iconKey: String,
    val shopCategoryTitle: String,
    val subCategory: String? = null
)

private val homeSummaryCategories = listOf(
    HomeShopCategoryTile(
        id = "beauty_hygiene",
        label = "Beauty & hygiene",
        iconKey = "home_beauty_hygiene",
        shopCategoryTitle = "Beauty & Hygiene"
    ),
    HomeShopCategoryTile(
        id = "cleaning_household",
        label = "Cleaning & household",
        iconKey = "home_cleaning_household",
        shopCategoryTitle = "Cleaning & Household"
    ),
    HomeShopCategoryTile(
        id = "snacks_branded",
        label = "Snacks & branded foods",
        iconKey = "home_snacks_branded",
        shopCategoryTitle = "Snacks & Branded Foods"
    ),
    HomeShopCategoryTile(
        id = "gourmet_world",
        label = "Gourmet & world food",
        iconKey = "home_gourmet_world",
        shopCategoryTitle = "Gourmet & World Food"
    ),
    HomeShopCategoryTile(
        id = "beverages",
        label = "Beverages",
        iconKey = "home_beverages",
        shopCategoryTitle = "Beverages"
    ),
    HomeShopCategoryTile(
        id = "baby_care",
        label = "Baby care",
        iconKey = "home_baby_care",
        shopCategoryTitle = "Baby Care"
    ),
    HomeShopCategoryTile(
        id = "bakery_dairy",
        label = "Bakery, cakes & dairy",
        iconKey = "home_bakery_dairy",
        shopCategoryTitle = "Bakery, Cakes & Dairy"
    ),
    HomeShopCategoryTile(
        id = "pharmacy_store",
        label = "Pharmacy store",
        iconKey = "home_pharmacy_store",
        shopCategoryTitle = "Beauty & Hygiene",
        subCategory = "Health & Medicine"
    ),
    HomeShopCategoryTile(
        id = "foodgrains_oil",
        label = "Foodgrains, oils & masalas",
        iconKey = "home_foodgrains_oil",
        shopCategoryTitle = "Foodgrains, Oil & Masala"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    categories: List<Category>,
    brands: List<Brand>,
    activeAddress: SavedAddress?,
    storeConfig: StoreConfig,
    onAddressClick: () -> Unit,
    onSearchBarClick: () -> Unit,
    onFilterClick: () -> Unit,
    onAdminClick: () -> Unit,
    onAccountClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onShopCategoryClick: (shopCategory: String, subCategory: String?) -> Unit,
    onBrandClick: (Brand) -> Unit,
    onSeeAllCategoriesClick: () -> Unit,
    onViewAllProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenuSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Collapse only the top brand row when scrolled; keep Delivery ETA + Address + Search Bar ALWAYS sticky
    val showTopBrandRow by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 48
        }
    }

    val searchHints = remember {
        listOf("Search \"paneer\"", "Search \"biscuits\"", "Search \"chips\"", "Search \"atta\"")
    }
    var hintIdx by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2800)
            hintIdx = (hintIdx + 1) % searchHints.size
        }
    }

    val banners = listOf(
        HeroBannerData(
            title = "Farm Fresh Harvest",
            subtitle = "Up to 33% OFF on daily veggies & Alphonso mangoes",
            badge = "10 MIN EXPRESS",
            ctaText = "SHOP NOW",
            imageRes = R.drawable.img_banner_fresh,
            categoryId = "cat_veg"
        ),
        HeroBannerData(
            title = "Morning Dairy & Bread",
            subtitle = "Chilled Amul milk, butter, paneer & hot wheat bread",
            badge = "FRESH DAILY",
            ctaText = "TRY NOW",
            imageRes = R.drawable.img_banner_dairy,
            categoryId = "cat_dairy"
        ),
        HeroBannerData(
            title = "Chai & Crunch Time",
            subtitle = "Assam tea, Haldiram's bhujia & roasted makhana",
            badge = "UP TO 25% OFF",
            ctaText = "EXPLORE",
            imageRes = R.drawable.img_banner_snacks,
            categoryId = "cat_snacks"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // PINNED STICKY GREEN HEADER: Top brand row collapses smoothly on scroll; Delivery + Search ALWAYS stay anchored!
        Surface(
            color = QuickGreen,
            contentColor = Color.White,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp)
                    .padding(top = 4.dp, bottom = 12.dp)
            ) {
                AnimatedVisibility(
                    visible = showTopBrandRow,
                    enter = expandVertically(tween(200)) + fadeIn(tween(180)),
                    exit = shrinkVertically(tween(180)) + fadeOut(tween(120))
                ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = { showMenuSheet = true },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .testTag("home_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = "Open Navigation Menu",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            QuickKartLogoBadge(
                                compact = true,
                                lightOnGreen = true,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            IconButton(
                                onClick = onAccountClick,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .testTag("home_account_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = "Account Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }

                // Always-Pinned Delivery ETA + Address Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onAddressClick)
                        .padding(vertical = 3.dp)
                        .testTag("home_address_selector"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocalShipping,
                                contentDescription = "Delivery ETA",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Get it in ${storeConfig.etaMinutes} mins",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = activeAddress?.let {
                                "${it.label} - ${it.houseFlat}, ${it.streetArea}, ${it.city}"
                            } ?: "Select Delivery Address",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.92f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Surface(
                        color = QuickGreenDark.copy(alpha = 0.72f),
                        shape = CircleShape,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Change Address",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Always-Pinned Crisp White Search Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .clickable(onClick = onSearchBarClick)
                        .testTag("home_search_bar"),
                    shape = RoundedCornerShape(9.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 12.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search groceries",
                            tint = QuickMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        AnimatedContent(
                            targetState = searchHints[hintIdx],
                            transitionSpec = {
                                fadeIn(tween(240)).togetherWith(fadeOut(tween(180)))
                            },
                            label = "home_search_hint",
                            modifier = Modifier.weight(1f)
                        ) { hint ->
                            Text(
                                text = hint,
                                fontSize = 14.sp,
                                color = QuickMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("home_filter_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = "Filter & Sort",
                                tint = QuickMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Scrollable Storefront Content
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Full-Width Sharp Hero Banner Pager + Indicator Dots
            item {
                val pagerState = rememberPagerState(pageCount = { banners.size })

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        pageSpacing = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) { pageIndex ->
                        val banner = banners[pageIndex]
                        val targetCat = categories.firstOrNull { it.id == banner.categoryId }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(174.dp)
                                .clickable {
                                    if (targetCat != null) onCategoryClick(targetCat) else onViewAllProducts()
                                }
                                .testTag("hero_banner_${banner.categoryId}"),
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 2.dp
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = banner.imageRes),
                                    contentDescription = banner.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xCC0F172A),
                                                    Color(0x660F172A),
                                                    Color.Transparent
                                                )
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(16.dp)
                                        .width(210.dp)
                                ) {
                                    Surface(
                                        color = QuickAmber,
                                        shape = RoundedCornerShape(5.dp)
                                    ) {
                                        Text(
                                            text = banner.badge,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF111827),
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = banner.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(Modifier.height(3.dp))
                                    Text(
                                        text = banner.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.92f),
                                        maxLines = 2
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    Surface(
                                        color = Color(0xFFDC2626),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = banner.ctaText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        banners.forEachIndexed { idx, _ ->
                            val isSelected = idx == pagerState.currentPage
                            val pillWidth by animateDpAsState(
                                targetValue = if (isSelected) 16.dp else 6.dp,
                                animationSpec = tween(220),
                                label = "banner_indicator_width"
                            )
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .height(5.dp)
                                    .width(pillWidth)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color(0xFF1F2937) else Color(0xFFD1D5DB))
                            )
                        }
                    }
                }
            }

            // Extra B2B/Quick-Commerce Highlight Strip (Credit Cards, Instant UPI & High Margin Savings)
            item {
                Surface(
                    onClick = onViewAllProducts,
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, QuickOutline),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .testTag("home_promo_strip")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Wholesale Margins + UPI & Cards",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF111827)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "Up to 26% retailer margin across top FMCG packs • Fast ${storeConfig.etaMinutes}-min dispatch",
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = QuickSlateSecondary
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Surface(
                            color = Color(0xFF1E3A8A),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.CreditCard,
                                    contentDescription = "Digital payments accepted",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Bolt,
                                    contentDescription = null,
                                    tint = QuickAmber,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(13.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Shop by category (9 summary tiles in 3x3 grid; "See All" opens full A-Z Shop By Category page)
            item {
                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    SectionHeader(
                        title = "Shop by category",
                        actionLabel = "See All",
                        onActionClick = onSeeAllCategoriesClick
                    )
                    Spacer(Modifier.height(10.dp))
                    val rows = homeSummaryCategories.chunked(3)
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { tile ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                onShopCategoryClick(tile.shopCategoryTitle, tile.subCategory)
                                            }
                                            .padding(vertical = 2.dp)
                                            .testTag("home_category_${tile.id}"),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        HomeCategoryClusterArtwork(iconKey = tile.iconKey)
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = tile.label,
                                            fontSize = 12.sp,
                                            lineHeight = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = QuickSlate,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                repeat(3 - rowItems.size) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Shop by Manufacturer (exact 23 manufacturers in screenshot order in 4-col cream tiles)
            item {
                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    SectionHeader(
                        title = "Shop by Manufacturer",
                        actionLabel = "See All",
                        onActionClick = onViewAllProducts
                    )
                    Spacer(Modifier.height(10.dp))
                    val brandRows = brands.chunked(4)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        brandRows.forEach { rowBrands ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowBrands.forEach { brand ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { onBrandClick(brand) }
                                            .padding(vertical = 2.dp)
                                            .testTag("home_brand_${brand.id}"),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        BrandVisualIcon(
                                            brand = brand,
                                            fillCardWidth = true
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = brand.name,
                                            fontSize = 12.sp,
                                            lineHeight = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = QuickSlate,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                repeat(4 - rowBrands.size) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // QuickKart Official Payment Safety & Transparency Notice Card (modeled cleanly after reference app)
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(2.5.dp, QuickGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = Color(0xFFDC2626),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.VerifiedUser,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "QuickKart never asks for advance cash payments from customers or kirana partners.",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Pay securely in-app via UPI, Card, or Wallet — or Cash on Delivery only at verified doorstep handover.",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = QuickSlateSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    if (showMenuSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMenuSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuickKartLogoBadge(compact = true)
                }
                HorizontalDivider(color = QuickOutline, modifier = Modifier.padding(vertical = 6.dp))
                ListItem(
                    headlineContent = { Text("Shop By Category (A–Z)", fontWeight = FontWeight.SemiBold) },
                    leadingContent = { Icon(Icons.Rounded.Category, contentDescription = null, tint = QuickGreen) },
                    modifier = Modifier.clickable {
                        showMenuSheet = false
                        onSeeAllCategoriesClick()
                    }
                )
                ListItem(
                    headlineContent = { Text("Explore All Products", fontWeight = FontWeight.SemiBold) },
                    leadingContent = { Icon(Icons.Rounded.Inventory2, contentDescription = null, tint = QuickGreen) },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = QuickMuted, modifier = Modifier.size(18.dp))
                    },
                    modifier = Modifier.clickable {
                        showMenuSheet = false
                        onViewAllProducts()
                    }
                )
                ListItem(
                    headlineContent = { Text("My Orders", fontWeight = FontWeight.SemiBold) },
                    leadingContent = { Icon(Icons.Rounded.ReceiptLong, contentDescription = null, tint = QuickGreen) },
                    modifier = Modifier.clickable {
                        showMenuSheet = false
                        onOrdersClick()
                    }
                )
                ListItem(
                    headlineContent = { Text("Admin Panel", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Manage products, brands, orders & slots") },
                    leadingContent = { Icon(Icons.Rounded.AdminPanelSettings, contentDescription = null, tint = QuickGreen) },
                    modifier = Modifier
                        .clickable {
                            showMenuSheet = false
                            onAdminClick()
                        }
                        .testTag("home_admin_button")
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = QuickSlate
        )
        TextButton(onClick = onActionClick) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = QuickGreen
            )
        }
    }
}
```

---

## File: `app/src/main/java/com/example/ui/screens/ShopByCategoryScreen.kt`
```kotlin
package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Egg
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.local.ShopCategoryGroup
import com.example.data.local.ShopCategoryTaxonomy
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickMuted

@Composable
fun ShopByCategoryScreen(
    totalProductCount: Int,
    onBackClick: () -> Unit,
    onSearchSubmit: (String) -> Unit,
    onSelectCategoryAll: (ShopCategoryGroup) -> Unit,
    onSelectSubCategory: (ShopCategoryGroup, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expandedGroupId by rememberSaveable { mutableStateOf<String?>(null) }

    val filteredGroups = remember(searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) {
            ShopCategoryTaxonomy.groups
        } else {
            ShopCategoryTaxonomy.groups.filter { group ->
                group.title.lowercase().contains(q) ||
                    group.subcategories.any { it.lowercase().contains(q) }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Green Header matching the reference Shop By Category screenshots
        Surface(
            color = QuickGreen,
            contentColor = Color.White,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .testTag("shop_by_category_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Shop By Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // White search bar inside header
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(44.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search categories or products",
                            tint = QuickMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search $totalProductCount+ products",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = QuickMuted
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF111827)
                                ),
                                cursorBrush = SolidColor(QuickGreen),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        if (searchQuery.isNotBlank()) {
                                            onSearchSubmit(searchQuery.trim())
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("shop_by_category_search_input")
                            )
                        }
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Clear search",
                                    tint = QuickMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Expandable Category Accordion List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (searchQuery.isNotBlank()) {
                item(key = "search_products_shortcut") {
                    Surface(
                        color = Color(0xFFF3F8EE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSearchSubmit(searchQuery.trim()) }
                            .testTag("shop_by_category_search_submit")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Search all products for \"${searchQuery.trim()}\"",
                                style = MaterialTheme.typography.titleSmall,
                                color = QuickGreen,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "Search products",
                                tint = QuickGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)
                }
            }

            items(filteredGroups, key = { it.id }) { group ->
                val isExpanded = expandedGroupId == group.id ||
                    (searchQuery.isNotBlank() && group.subcategories.any {
                        it.contains(searchQuery.trim(), ignoreCase = true)
                    })

                ShopCategoryAccordionItem(
                    group = group,
                    isExpanded = isExpanded,
                    searchQuery = searchQuery.trim(),
                    onToggleExpand = {
                        expandedGroupId = if (expandedGroupId == group.id) null else group.id
                    },
                    onSelectAll = { onSelectCategoryAll(group) },
                    onSelectSubCategory = { sub -> onSelectSubCategory(group, sub) }
                )
            }
        }
    }
}

@Composable
private fun ShopCategoryAccordionItem(
    group: ShopCategoryGroup,
    isExpanded: Boolean,
    searchQuery: String,
    onToggleExpand: () -> Unit,
    onSelectAll: () -> Unit,
    onSelectSubCategory: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleExpand)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .testTag("shop_category_row_${group.id}"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = shopCategoryOutlineIcon(group.iconKey),
                contentDescription = group.title,
                tint = Color(0xFF374151),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = group.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isExpanded) FontWeight.Bold else FontWeight.Medium,
                color = Color(0xFF1F2937),
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFF9CA3AF)),
                modifier = Modifier.size(26.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse ${group.title}" else "Expand ${group.title}",
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // "All <Category>" row
                SubCategoryRow(
                    label = group.allOptionLabel,
                    isHighlighted = false,
                    onClick = onSelectAll,
                    modifier = Modifier.testTag("shop_subcat_${group.id}_all")
                )

                val visibleSubs = if (searchQuery.isBlank() || group.title.contains(searchQuery, ignoreCase = true)) {
                    group.subcategories
                } else {
                    group.subcategories.filter { it.contains(searchQuery, ignoreCase = true) }
                }

                visibleSubs.forEach { subName ->
                    SubCategoryRow(
                        label = subName,
                        isHighlighted = false,
                        onClick = { onSelectSubCategory(subName) },
                        modifier = Modifier.testTag("shop_subcat_${group.id}_${subName}")
                    )
                }
            }
        }
    }
}

@Composable
private fun SubCategoryRow(
    label: String,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(start = 26.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Normal,
                color = Color(0xFF374151)
            )
        }
        HorizontalDivider(
            color = Color(0xFFF3F4F6),
            thickness = 0.8.dp,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

private fun shopCategoryOutlineIcon(iconKey: String): ImageVector = when (iconKey) {
    "electronics" -> Icons.Outlined.Devices
    "fruits_veg" -> Icons.Outlined.Eco
    "foodgrains" -> Icons.Outlined.Inventory2
    "bakery_dairy" -> Icons.Outlined.BakeryDining
    "beverages" -> Icons.Outlined.LocalCafe
    "snacks_branded" -> Icons.Outlined.Fastfood
    "beauty_hygiene" -> Icons.Outlined.Spa
    "cleaning_household" -> Icons.Outlined.CleaningServices
    "kitchen_garden_pets" -> Icons.Outlined.Pets
    "eggs_meat_fish" -> Icons.Outlined.Egg
    "gourmet_world" -> Icons.Outlined.Restaurant
    "baby_care" -> Icons.Outlined.ChildCare
    else -> Icons.Outlined.Inventory2
}
```

---

## File: `app/src/main/java/com/example/ui/screens/ManufacturerBrandsScreen.kt`
```kotlin
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
```

---

## File: `app/src/main/java/com/example/ui/screens/BrandProductsScreen.kt`
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
```

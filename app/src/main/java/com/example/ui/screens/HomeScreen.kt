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

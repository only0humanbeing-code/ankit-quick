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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ShopCategoryTaxonomy
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.OrderRecord
import com.example.model.Product
import com.example.model.SavedAddress
import com.example.model.StoreConfig
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.HomeCategoryClusterArtwork
import com.example.ui.components.PastOrdersCarouselSection
import com.example.ui.components.QuickKartLogoBadge
import com.example.ui.theme.QuickAmber
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickGreenDark
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickOutline
import com.example.ui.theme.QuickSlate
import com.example.ui.theme.QuickSlateSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        label = "Body care & beauty",
        iconKey = "home_beauty_hygiene",
        shopCategoryTitle = "Body Care & Beauty"
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
        label = "Baby care, female & hygiene",
        iconKey = "home_baby_care",
        shopCategoryTitle = "Baby Care, Female & Hygiene"
    ),
    HomeShopCategoryTile(
        id = "bakery_dairy",
        label = "Bakery, cake, dairy & fresh, frozen",
        iconKey = "home_bakery_dairy",
        shopCategoryTitle = "Bakery, Cake, Dairy & Fresh, Frozen"
    ),
    HomeShopCategoryTile(
        id = "pharmacy_store",
        label = "Pharmacy store",
        iconKey = "home_pharmacy_store",
        shopCategoryTitle = "Body Care & Beauty",
        subCategory = "Health & Medicine"
    ),
    HomeShopCategoryTile(
        id = "foodgrains_oil",
        label = "Foodgrains, oils & masalas",
        iconKey = "home_foodgrains_oil",
        shopCategoryTitle = "Foodgrains, Oil & Masala"
    )
)

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
    modifier: Modifier = Modifier,
    userName: String = "Customer",
    savedCount: Int = 0,
    onSavedItemsClick: () -> Unit = onAccountClick,
    products: List<Product> = emptyList(),
    orders: List<OrderRecord> = emptyList(),
    cartItems: List<CartItem> = emptyList(),
    onProductClick: (Product) -> Unit = {},
    onAddClick: (Product) -> Unit = {},
    onIncrementClick: (Product) -> Unit = {},
    onDecrementClick: (Product) -> Unit = {},
    onSeeAllManufacturersClick: () -> Unit = onViewAllProducts,
    onSubBrandDirectClick: (brandId: String, subBrand: String) -> Unit = { _, _ -> },
    onCompanyAllProductsClick: (Brand) -> Unit = onBrandClick,
    onSearchProductQuery: (String) -> Unit = { onSearchBarClick() }
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var categoryQuery by rememberSaveable { mutableStateOf("") }
    var manufacturerQuery by rememberSaveable { mutableStateOf("") }

    BackHandler(enabled = drawerState.isOpen) {
        drawerScope.launch { drawerState.close() }
    }

    // Collapse only the top brand row when scrolled; keep Delivery ETA + Address + Search Bar ALWAYS sticky
    val showTopBrandRow by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 48
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
                modifier = Modifier.width(304.dp)
            ) {
                // Dark charcoal greeting header matching Screenshot 1
                Surface(
                    color = Color(0xFF424242),
                    contentColor = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val displayFirst = userName.trim().ifBlank { "Customer" }.substringBefore(" ")
                    Text(
                        text = "Hello, $displayFirst",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                    )
                }

                // Active Delivery Address + Edit pencil
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            drawerScope.launch { drawerState.close() }
                            onAddressClick()
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Delivery Address",
                        tint = QuickMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = activeAddress?.let {
                            "${it.streetArea}, ${it.city}- ${it.pincode}"
                        } ?: "Select Delivery Address",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = QuickSlate,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Change Address",
                        tint = QuickGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Delivery ETA strip
                Surface(
                    color = Color(0xFFF3F4F6),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalShipping,
                            contentDescription = null,
                            tint = QuickSlateSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Get it in ${storeConfig.etaMinutes} mins",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = QuickSlate
                        )
                    }
                }

                HorizontalDivider(color = QuickOutline)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 6.dp)
                ) {
                    DrawerNavItem(
                        label = "Home",
                        icon = Icons.Outlined.Home,
                        onClick = { drawerScope.launch { drawerState.close() } }
                    )
                    DrawerNavItem(
                        label = "My Account",
                        icon = Icons.Outlined.AccountCircle,
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            onAccountClick()
                        }
                    )
                    DrawerNavItem(
                        label = if (savedCount > 0) "Saved For Later / My List ($savedCount)" else "Saved For Later / My List",
                        icon = Icons.Outlined.BookmarkBorder,
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            onSavedItemsClick()
                        }
                    )
                    DrawerNavItem(
                        label = "Shop By Category",
                        icon = Icons.Outlined.Category,
                        showChevron = true,
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            onSeeAllCategoriesClick()
                        }
                    )
                    DrawerNavItem(
                        label = "Shop By Manufacturer",
                        icon = Icons.Outlined.Storefront,
                        showChevron = true,
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            onSeeAllManufacturersClick()
                        }
                    )
                    DrawerNavItem(
                        label = "Explore All Products",
                        icon = Icons.Outlined.Inventory2,
                        showArrow = true,
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            onViewAllProducts()
                        }
                    )
                    DrawerNavItem(
                        label = "My Orders",
                        icon = Icons.Outlined.ReceiptLong,
                        onClick = {
                            drawerScope.launch { drawerState.close() }
                            onOrdersClick()
                        }
                    )

                    HorizontalDivider(
                        color = QuickOutline,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    ListItem(
                        headlineContent = { Text("Admin Panel", fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("Manage products, brands, orders & slots", fontSize = 12.sp) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Rounded.AdminPanelSettings,
                                contentDescription = null,
                                tint = QuickGreen
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.White),
                        modifier = Modifier
                            .clickable {
                                drawerScope.launch { drawerState.close() }
                                onAdminClick()
                            }
                            .testTag("home_admin_button")
                    )
                }
            }
        }
    ) {
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
                                    onClick = { drawerScope.launch { drawerState.open() } },
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
                        RotatingSearchHintText(modifier = Modifier.weight(1f))
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
                .fillMaxWidth()
                .testTag("home_lazy_column"),
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

            // "Based on your Past Orders" Horizontal Category Tab Bar + 2-Row Right-to-Left Product Shelf (above Shop by category)
            if (products.isNotEmpty()) {
                item(key = "past_orders_carousel_section") {
                    PastOrdersCarouselSection(
                        products = products,
                        brands = brands,
                        orders = orders,
                        cartItems = cartItems,
                        onProductClick = onProductClick,
                        onAddClick = onAddClick,
                        onIncrementClick = onIncrementClick,
                        onDecrementClick = onDecrementClick
                    )
                }
            }

            // Shop by category (inline Category Search + "See All" opens full A-Z Shop By Category page)
            item {
                val trimmedCatQuery = categoryQuery.trim()
                val matchedTaxonomyGroups = remember(trimmedCatQuery) {
                    if (trimmedCatQuery.isBlank()) {
                        emptyList()
                    } else {
                        ShopCategoryTaxonomy.groups.filter { grp ->
                            ShopCategoryTaxonomy.matchesQuery(grp, trimmedCatQuery)
                        }
                    }
                }
                val matchedSubCategories = remember(trimmedCatQuery) {
                    if (trimmedCatQuery.isBlank()) {
                        emptyList()
                    } else {
                        ShopCategoryTaxonomy.groups.flatMap { grp ->
                            grp.subcategories
                                .filter {
                                    it.contains(trimmedCatQuery, ignoreCase = true) ||
                                        ShopCategoryTaxonomy.matchesQuery(grp, trimmedCatQuery)
                                }
                                .map { sub -> grp to sub }
                        }
                    }
                }
                val visibleCategoryTiles = remember(trimmedCatQuery) {
                    if (trimmedCatQuery.isBlank()) {
                        homeSummaryCategories
                    } else {
                        homeSummaryCategories.filter { tile ->
                            val grp = ShopCategoryTaxonomy.findByTitle(tile.shopCategoryTitle)
                            tile.label.contains(trimmedCatQuery, ignoreCase = true) ||
                                tile.shopCategoryTitle.contains(trimmedCatQuery, ignoreCase = true) ||
                                (tile.subCategory?.contains(trimmedCatQuery, ignoreCase = true) == true) ||
                                (grp?.subcategories?.any { it.contains(trimmedCatQuery, ignoreCase = true) } == true)
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    SectionHeader(
                        title = "Shop by category",
                        actionLabel = "See All",
                        onActionClick = onSeeAllCategoriesClick,
                        actionTestTag = "home_see_all_categories"
                    )
                    Spacer(Modifier.height(6.dp))

                    // Inline Search Bar alongside See All for Shop by category
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, QuickOutline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 12.dp, end = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search category or item",
                                tint = QuickGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (categoryQuery.isEmpty()) {
                                    Text(
                                        text = "Search category or item (Dairy, Dal, Oil)...",
                                        fontSize = 13.sp,
                                        color = QuickMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                BasicTextField(
                                    value = categoryQuery,
                                    onValueChange = { categoryQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = QuickSlate
                                    ),
                                    cursorBrush = SolidColor(QuickGreen),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            if (trimmedCatQuery.isNotBlank()) {
                                                onSearchProductQuery(trimmedCatQuery)
                                            }
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("home_category_search_input")
                                )
                            }
                            if (categoryQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { categoryQuery = "" },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Clear category search",
                                        tint = QuickMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Quick-jump Category & Sub-Category chips when typing
                    if (trimmedCatQuery.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(matchedTaxonomyGroups, key = { "cat_grp_${it.id}" }) { grp ->
                                Surface(
                                    onClick = { onShopCategoryClick(grp.title, null) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = QuickGreenContainer,
                                    border = BorderStroke(1.dp, QuickGreen)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "All ${grp.title}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuickGreen
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                            contentDescription = null,
                                            tint = QuickGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                            items(
                                matchedSubCategories,
                                key = { "cat_sub_${it.first.id}_${it.second}" }
                            ) { (grp, subName) ->
                                Surface(
                                    onClick = { onShopCategoryClick(grp.title, subName) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFDF5EE),
                                    border = BorderStroke(1.dp, Color(0xFFF3E4D3))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "$subName (${grp.title})",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = QuickSlate
                                        )
                                    }
                                }
                            }
                            item(key = "cat_search_catalog") {
                                Surface(
                                    onClick = { onSearchProductQuery(trimmedCatQuery) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF3F8EE),
                                    border = BorderStroke(1.dp, QuickGreen.copy(alpha = 0.45f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Search products: \"$trimmedCatQuery\"",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuickGreen
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                            contentDescription = null,
                                            tint = QuickGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    if (visibleCategoryTiles.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9FAFB),
                            border = BorderStroke(1.dp, QuickOutline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "No featured category tile for \"$trimmedCatQuery\"",
                                    fontSize = 13.sp,
                                    color = QuickMuted,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = onSeeAllCategoriesClick) {
                                    Text("See All →", color = QuickGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        val rows = visibleCategoryTiles.chunked(3)
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
            }

            // Shop by Manufacturer (with inline Company & Brand Search + See All directory)
            item {
                val subBrandIndex = remember(products, brands) {
                    val brandById = brands.associateBy { it.id }
                    products
                        .filter { it.subBrand.isNotBlank() }
                        .groupBy { "${it.brandId}__${it.subBrand}" }
                        .mapNotNull { (_, list) ->
                            val first = list.first()
                            val parent = brandById[first.brandId] ?: return@mapNotNull null
                            Triple(parent, first.subBrand, list.size)
                        }
                        .sortedBy { it.second }
                }

                val trimmedMfgQuery = manufacturerQuery.trim()
                val visibleBrands = remember(brands, products, trimmedMfgQuery) {
                    if (trimmedMfgQuery.isBlank()) {
                        brands
                    } else {
                        brands.filter { brand ->
                            brand.name.contains(trimmedMfgQuery, ignoreCase = true) ||
                                brand.tagline.contains(trimmedMfgQuery, ignoreCase = true) ||
                                products.any {
                                    it.brandId == brand.id && (
                                        it.subBrand.contains(trimmedMfgQuery, ignoreCase = true) ||
                                            it.name.contains(trimmedMfgQuery, ignoreCase = true)
                                        )
                                }
                        }
                    }
                }

                val matchedSubBrands = remember(subBrandIndex, trimmedMfgQuery) {
                    if (trimmedMfgQuery.isBlank()) {
                        emptyList()
                    } else {
                        subBrandIndex.filter { (parent, subName, _) ->
                            subName.contains(trimmedMfgQuery, ignoreCase = true) ||
                                parent.name.contains(trimmedMfgQuery, ignoreCase = true)
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    SectionHeader(
                        title = "Shop by Manufacturer",
                        actionLabel = "See All",
                        onActionClick = onSeeAllManufacturersClick,
                        actionTestTag = "home_see_all_manufacturers"
                    )
                    Spacer(Modifier.height(6.dp))

                    // Inline Search Bar for Manufacturers & Sub-Brands
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, QuickOutline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 12.dp, end = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search manufacturer or brand",
                                tint = QuickGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (manufacturerQuery.isEmpty()) {
                                    Text(
                                        text = "Search company or brand (HUL, Dove, ITC)...",
                                        fontSize = 13.sp,
                                        color = QuickMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                BasicTextField(
                                    value = manufacturerQuery,
                                    onValueChange = { manufacturerQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = QuickSlate
                                    ),
                                    cursorBrush = SolidColor(QuickGreen),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            if (trimmedMfgQuery.isNotBlank()) {
                                                onSearchProductQuery(trimmedMfgQuery)
                                            }
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("home_manufacturer_search_input")
                                )
                            }
                            if (manufacturerQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { manufacturerQuery = "" },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Clear manufacturer search",
                                        tint = QuickMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Quick-jump Brand & Whole-Company chips when typing
                    if (trimmedMfgQuery.isNotBlank() && (matchedSubBrands.isNotEmpty() || visibleBrands.isNotEmpty())) {
                        Spacer(Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(visibleBrands, key = { "all_${it.id}" }) { company ->
                                Surface(
                                    onClick = { onCompanyAllProductsClick(company) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = QuickGreenContainer,
                                    border = BorderStroke(1.dp, QuickGreen)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "All ${company.name} Products",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuickGreen
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                            contentDescription = null,
                                            tint = QuickGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                            items(
                                matchedSubBrands,
                                key = { "${it.first.id}_${it.second}" }
                            ) { (parentBrand, subName, _) ->
                                Surface(
                                    onClick = { onSubBrandDirectClick(parentBrand.id, subName) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFDF5EE),
                                    border = BorderStroke(1.dp, Color(0xFFF3E4D3))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "$subName (${parentBrand.name})",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = QuickSlate
                                        )
                                    }
                                }
                            }
                            item(key = "mfg_search_catalog") {
                                Surface(
                                    onClick = { onSearchProductQuery(trimmedMfgQuery) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF3F8EE),
                                    border = BorderStroke(1.dp, QuickGreen.copy(alpha = 0.45f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Search products: \"$trimmedMfgQuery\"",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuickGreen
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                            contentDescription = null,
                                            tint = QuickGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    if (visibleBrands.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9FAFB),
                            border = BorderStroke(1.dp, QuickOutline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "No company matching \"$trimmedMfgQuery\"",
                                    fontSize = 13.sp,
                                    color = QuickMuted,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = onSeeAllManufacturersClick) {
                                    Text("Open Directory →", color = QuickGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        val brandRows = visibleBrands.chunked(4)
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
    }
}

@Composable
private fun DrawerNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    showChevron: Boolean = false,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = QuickGreen,
            modifier = Modifier.size(21.dp)
        )
        Spacer(Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = QuickSlate,
            modifier = Modifier.weight(1f)
        )
        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = QuickMuted,
                modifier = Modifier.size(20.dp)
            )
        } else if (showArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = QuickMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun RotatingSearchHintText(modifier: Modifier = Modifier) {
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
    AnimatedContent(
        targetState = searchHints[hintIdx],
        transitionSpec = {
            fadeIn(tween(240)).togetherWith(fadeOut(tween(180)))
        },
        label = "home_search_hint",
        modifier = modifier
    ) { hint ->
        Text(
            text = hint,
            fontSize = 14.sp,
            color = QuickMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    actionTestTag: String? = null
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
        TextButton(
            onClick = onActionClick,
            modifier = if (actionTestTag != null) Modifier.testTag(actionTestTag) else Modifier
        ) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = QuickGreen
            )
        }
    }
}

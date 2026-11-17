package com.example.ui

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.ShopCategoryTaxonomy
import com.example.model.SavedAddress
import com.example.ui.components.FilterSortBottomSheet
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AddEditAddressDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.BrandProductsScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ManufacturerBrandsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProductListScreen
import com.example.ui.screens.ShopByCategoryScreen
import com.example.ui.screens.ShopByManufacturerScreen
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.util.FormatUtils
import com.example.viewmodel.QuickKartViewModel
import kotlinx.coroutines.flow.collectLatest

object QuickKartRoutes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val MANUFACTURERS = "manufacturers"
    const val CATALOG = "catalog"
    const val ORDERS = "orders"
    const val CART = "cart"
    const val ACCOUNT = "account"
    const val PRODUCT_DETAIL = "product/{productId}"
    const val MANUFACTURER_BRANDS = "manufacturer/{brandId}"
    const val BRAND_PRODUCTS = "brand_products/{brandId}/{subBrand}"
    const val SUBCATEGORY_PRODUCTS = "subcategory_products/{shopCategory}/{subCategory}"
    const val CHECKOUT = "checkout"
    const val ADMIN = "admin"

    fun productDetail(productId: String) = "product/$productId"
    fun manufacturerBrands(brandId: String) = "manufacturer/$brandId"
    fun brandProducts(brandId: String, subBrand: String?): String {
        val encoded = if (subBrand.isNullOrBlank()) "__ALL__" else Uri.encode(subBrand)
        return "brand_products/$brandId/$encoded"
    }
    fun subCategoryProducts(shopCategory: String, subCategory: String?): String {
        val encodedCat = Uri.encode(shopCategory)
        val encodedSub = if (subCategory.isNullOrBlank()) "__ALL__" else Uri.encode(subCategory)
        return "subcategory_products/$encodedCat/$encodedSub"
    }
}

private data class BottomNavTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickKartApp(viewModel: QuickKartViewModel) {
    val isReady by viewModel.isReady.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val brands by viewModel.brands.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val availablePackSizes by viewModel.availablePackSizes.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartSummary by viewModel.cartSummary.collectAsStateWithLifecycle()
    val addresses by viewModel.addresses.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val deliverySlots by viewModel.deliverySlots.collectAsStateWithLifecycle()
    val storeConfig by viewModel.storeConfig.collectAsStateWithLifecycle()
    val notifyProductIds by viewModel.notifyProductIds.collectAsStateWithLifecycle()
    val savedProductIds by viewModel.savedProductIds.collectAsStateWithLifecycle()
    val isProcessingOrder by viewModel.isProcessingOrder.collectAsStateWithLifecycle()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showAddressPickerSheet by remember { mutableStateOf(false) }
    var showNewAddressDialogFromPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (!isReady) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = QuickGreen)
        }
        return
    }

    if (!userProfile.isLoggedIn) {
        LoginScreen(
            onVerifyOtp = { phone, otp, onError ->
                viewModel.verifyOtp(
                    phone = phone,
                    otp = otp,
                    onSuccess = { },
                    onError = onError
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(QuickGreen)
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        )
        return
    }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomTabs = listOf(
        BottomNavTab(QuickKartRoutes.HOME, "Home", Icons.Rounded.Home, Icons.Outlined.Home),
        BottomNavTab(QuickKartRoutes.CATEGORIES, "Categories", Icons.Rounded.Category, Icons.Outlined.Category),
        BottomNavTab(QuickKartRoutes.ORDERS, "Orders", Icons.Rounded.ReceiptLong, Icons.Outlined.ReceiptLong),
        BottomNavTab(QuickKartRoutes.CART, "Cart", Icons.Rounded.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavTab(QuickKartRoutes.ACCOUNT, "Account", Icons.Rounded.Person, Icons.Outlined.Person)
    )

    val showBottomBar = currentRoute in setOf(
        QuickKartRoutes.HOME,
        QuickKartRoutes.CATEGORIES,
        QuickKartRoutes.MANUFACTURERS,
        QuickKartRoutes.CATALOG,
        QuickKartRoutes.MANUFACTURER_BRANDS,
        QuickKartRoutes.BRAND_PRODUCTS,
        QuickKartRoutes.SUBCATEGORY_PRODUCTS,
        QuickKartRoutes.ORDERS,
        QuickKartRoutes.CART,
        QuickKartRoutes.ACCOUNT
    )

    val showFloatingCartBanner = currentRoute in setOf(
        QuickKartRoutes.HOME,
        QuickKartRoutes.CATEGORIES,
        QuickKartRoutes.MANUFACTURERS,
        QuickKartRoutes.CATALOG,
        QuickKartRoutes.MANUFACTURER_BRANDS,
        QuickKartRoutes.BRAND_PRODUCTS,
        QuickKartRoutes.SUBCATEGORY_PRODUCTS
    ) && cartSummary.totalCount > 0

    val activeAddress: SavedAddress? = addresses.firstOrNull { it.isDefault } ?: addresses.firstOrNull()

    fun navigateTopTab(route: String) {
        if (route == QuickKartRoutes.HOME) {
            val popped = navController.popBackStack(QuickKartRoutes.HOME, inclusive = false)
            if (!popped && currentRoute != QuickKartRoutes.HOME) {
                navController.navigate(QuickKartRoutes.HOME) {
                    launchSingleTop = true
                }
            }
            return
        }
        navController.navigate(route) {
            popUpTo(QuickKartRoutes.HOME) { saveState = false }
            launchSingleTop = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                Column(modifier = Modifier.background(Color.White)) {
                    AnimatedVisibility(visible = showFloatingCartBanner) {
                        Surface(
                            color = QuickGreen,
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { navigateTopTab(QuickKartRoutes.CART) }
                                .testTag("floating_view_cart_bar")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.ShoppingBag,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        AnimatedContent(
                                            targetState = "${cartSummary.totalCount} Item(s) • ${FormatUtils.inr(cartSummary.itemTotal)}",
                                            transitionSpec = {
                                                fadeIn(tween(150)).togetherWith(fadeOut(tween(120)))
                                            },
                                            label = "cart_bar_total"
                                        ) { summaryText ->
                                            Text(
                                                text = summaryText,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        if (cartSummary.itemSavings > 0) {
                                            Text(
                                                text = "Saving ${FormatUtils.inr(cartSummary.itemSavings)} on MRP",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White.copy(alpha = 0.88f)
                                            )
                                        }
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "View Cart",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 0.dp
                    ) {
                        bottomTabs.forEach { tab ->
                            val selected = currentRoute == tab.route ||
                                (tab.route == QuickKartRoutes.CATEGORIES && (currentRoute == QuickKartRoutes.CATALOG || currentRoute == QuickKartRoutes.SUBCATEGORY_PRODUCTS))
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (tab.route == QuickKartRoutes.CATEGORIES) {
                                        viewModel.resetFiltersCompletely()
                                    }
                                    navigateTopTab(tab.route)
                                },
                                icon = {
                                    if (tab.route == QuickKartRoutes.CART && cartSummary.totalCount > 0) {
                                        BadgedBox(
                                            badge = {
                                                Badge(containerColor = QuickGreen, contentColor = Color.White) {
                                                    Text("${cartSummary.totalCount}")
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.label
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = QuickGreen,
                                    selectedTextColor = QuickGreen,
                                    unselectedIconColor = Color(0xFF4B5563),
                                    unselectedTextColor = Color(0xFF4B5563),
                                    indicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.testTag("bottom_nav_${tab.route}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        val standardScreenModifier = Modifier
            .fillMaxSize()
            .background(QuickGreen)
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)

        NavHost(
            navController = navController,
            startDestination = QuickKartRoutes.HOME,
            enterTransition = {
                fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                    slideInHorizontally(initialOffsetX = { it / 16 }, animationSpec = tween(220, easing = FastOutSlowInEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                    slideInHorizontally(initialOffsetX = { -it / 16 }, animationSpec = tween(220, easing = FastOutSlowInEasing))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing)) +
                    slideOutHorizontally(targetOffsetX = { it / 14 }, animationSpec = tween(180, easing = FastOutSlowInEasing))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(QuickKartRoutes.HOME) {
                HomeScreen(
                    categories = categories,
                    brands = brands,
                    activeAddress = activeAddress,
                    storeConfig = storeConfig,
                    onAddressClick = { showAddressPickerSheet = true },
                    onSearchBarClick = {
                        viewModel.resetFiltersCompletely()
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    onFilterClick = {
                        navController.navigate(QuickKartRoutes.CATALOG)
                        showFilterSheet = true
                    },
                    onAdminClick = { navController.navigate(QuickKartRoutes.ADMIN) },
                    onAccountClick = { navigateTopTab(QuickKartRoutes.ACCOUNT) },
                    onOrdersClick = { navigateTopTab(QuickKartRoutes.ORDERS) },
                    onCategoryClick = { cat ->
                        viewModel.selectCategoryFilter(cat.id)
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    onShopCategoryClick = { shopCategory, subCategory ->
                        viewModel.selectShopCategoryFilter(shopCategory, subCategory)
                        navController.navigate(QuickKartRoutes.subCategoryProducts(shopCategory, subCategory))
                    },
                    onBrandClick = { brand ->
                        navController.navigate(QuickKartRoutes.manufacturerBrands(brand.id))
                    },
                    onSeeAllCategoriesClick = { navigateTopTab(QuickKartRoutes.CATEGORIES) },
                    onViewAllProducts = {
                        viewModel.resetFiltersCompletely()
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    userName = userProfile.name,
                    savedCount = savedProductIds.size,
                    onSavedItemsClick = { navigateTopTab(QuickKartRoutes.ACCOUNT) },
                    products = products,
                    orders = orders,
                    cartItems = cartItems,
                    onProductClick = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onAddClick = { prod -> viewModel.addOrIncrementCart(prod) },
                    onIncrementClick = { prod -> viewModel.addOrIncrementCart(prod) },
                    onDecrementClick = { prod -> viewModel.decrementCart(prod.id, prod.defaultVariant.id) },
                    onSeeAllManufacturersClick = { navController.navigate(QuickKartRoutes.MANUFACTURERS) },
                    onSubBrandDirectClick = { brandId, subBrand ->
                        viewModel.selectSubBrandFilter(brandId, subBrand)
                        navController.navigate(QuickKartRoutes.brandProducts(brandId, subBrand))
                    },
                    onCompanyAllProductsClick = { brand ->
                        viewModel.selectSubBrandFilter(brand.id, null)
                        navController.navigate(QuickKartRoutes.brandProducts(brand.id, null))
                    },
                    onSearchProductQuery = { query ->
                        viewModel.resetFiltersCompletely()
                        viewModel.updateSearchQuery(query)
                        navController.navigate(QuickKartRoutes.CATALOG)
                    }
                )
            }

            composable(QuickKartRoutes.MANUFACTURERS) {
                ShopByManufacturerScreen(
                    brands = brands,
                    products = products,
                    onBackClick = { navController.popBackStack() },
                    onManufacturerClick = { brand ->
                        navController.navigate(QuickKartRoutes.manufacturerBrands(brand.id))
                    },
                    onViewCompanyAllProducts = { brand ->
                        viewModel.selectSubBrandFilter(brand.id, null)
                        navController.navigate(QuickKartRoutes.brandProducts(brand.id, null))
                    },
                    onSubBrandClick = { brandId, subBrandName ->
                        viewModel.selectSubBrandFilter(brandId, subBrandName)
                        navController.navigate(QuickKartRoutes.brandProducts(brandId, subBrandName))
                    },
                    onProductClick = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    }
                )
            }

            composable(QuickKartRoutes.CATEGORIES) {
                ShopByCategoryScreen(
                    totalProductCount = products.size,
                    onBackClick = { navigateTopTab(QuickKartRoutes.HOME) },
                    onSearchSubmit = { query ->
                        viewModel.resetFiltersCompletely()
                        viewModel.updateSearchQuery(query)
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    onSelectCategoryAll = { group ->
                        viewModel.selectShopCategoryFilter(group.title, null)
                        navController.navigate(QuickKartRoutes.subCategoryProducts(group.title, null))
                    },
                    onSelectSubCategory = { group, subName ->
                        viewModel.selectShopCategoryFilter(group.title, subName)
                        navController.navigate(QuickKartRoutes.subCategoryProducts(group.title, subName))
                    }
                )
            }

            composable(QuickKartRoutes.CATALOG) {
                val selectedCat = categories.firstOrNull { it.id == filterState.categoryId }
                val selectedBrand = brands.firstOrNull { it.id == filterState.brandId }
                val dynamicTitle = selectedCat?.name ?: selectedBrand?.name ?: "Explore Products"
                ProductListScreen(
                    title = dynamicTitle,
                    showBackButton = true,
                    categories = categories,
                    brands = brands,
                    filteredProducts = filteredProducts,
                    filterState = filterState,
                    cartItems = cartItems,
                    notifyProductIds = notifyProductIds,
                    onBackClick = { navController.popBackStack() },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCategorySelect = viewModel::selectCategoryFilter,
                    onBrandSelect = viewModel::selectBrandFilter,
                    onOpenFilterSheet = { showFilterSheet = true },
                    onResetFilters = viewModel::resetFiltersCompletely,
                    onProductClick = { prod -> navController.navigate(QuickKartRoutes.productDetail(prod.id)) },
                    onAddClick = { prod -> viewModel.addOrIncrementCart(prod) },
                    onIncrementClick = { prod -> viewModel.addOrIncrementCart(prod) },
                    onDecrementClick = { prod -> viewModel.decrementCart(prod.id, prod.defaultVariant.id) },
                    onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                    modifier = standardScreenModifier
                )
            }

            composable(
                route = QuickKartRoutes.MANUFACTURER_BRANDS,
                arguments = listOf(navArgument("brandId") { type = NavType.StringType })
            ) { entry ->
                val brandId = entry.arguments?.getString("brandId").orEmpty()
                val manufacturer = brands.firstOrNull { it.id == brandId }
                if (manufacturer == null) {
                    Box(standardScreenModifier, contentAlignment = Alignment.Center) {
                        Text("Manufacturer not found")
                    }
                } else {
                    val mfgProducts = products.filter { it.brandId == brandId }
                    ManufacturerBrandsScreen(
                        manufacturer = manufacturer,
                        productsForManufacturer = mfgProducts,
                        cartItems = cartItems,
                        notifyProductIds = notifyProductIds,
                        onBackClick = { navController.popBackStack() },
                        onSearchClick = {
                            viewModel.selectBrandFilter(brandId)
                            navController.navigate(QuickKartRoutes.CATALOG)
                        },
                        onSubBrandClick = { subEntry ->
                            viewModel.selectSubBrandFilter(brandId, subEntry.name)
                            navController.navigate(QuickKartRoutes.brandProducts(brandId, subEntry.name))
                        },
                        onViewAllManufacturerProducts = {
                            viewModel.selectSubBrandFilter(brandId, null)
                            navController.navigate(QuickKartRoutes.brandProducts(brandId, null))
                        },
                        onProductClick = { prod ->
                            navController.navigate(QuickKartRoutes.productDetail(prod.id))
                        },
                        onAddClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                        onIncrementClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                        onDecrementClick = { prod, vid -> viewModel.decrementCart(prod.id, vid) },
                        onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                        modifier = standardScreenModifier
                    )
                }
            }

            composable(
                route = QuickKartRoutes.BRAND_PRODUCTS,
                arguments = listOf(
                    navArgument("brandId") { type = NavType.StringType },
                    navArgument("subBrand") { type = NavType.StringType }
                )
            ) { entry ->
                val brandId = entry.arguments?.getString("brandId").orEmpty()
                val rawSubBrand = entry.arguments?.getString("subBrand").orEmpty()
                val subBrand = if (rawSubBrand == "__ALL__") null else Uri.decode(rawSubBrand)
                val manufacturer = brands.firstOrNull { it.id == brandId }
                val title = subBrand ?: manufacturer?.name ?: "Products"

                val allSubBrandProducts = products.filter { prod ->
                    prod.brandId == brandId && (subBrand == null || prod.subBrand == subBrand)
                }
                val displayedProducts = if (
                    filterState.brandId == brandId && filterState.subBrand == subBrand
                ) {
                    filteredProducts
                } else {
                    allSubBrandProducts
                }

                BrandProductsScreen(
                    title = title,
                    categories = categories,
                    allSubBrandProducts = allSubBrandProducts,
                    products = displayedProducts,
                    filterState = filterState,
                    cartItems = cartItems,
                    notifyProductIds = notifyProductIds,
                    etaMinutes = storeConfig.etaMinutes,
                    onBackClick = { navController.popBackStack() },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCategoryChipClick = viewModel::setCategoryWithinCurrentFilter,
                    onOpenFilterSheet = { showFilterSheet = true },
                    onProductClick = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onAddClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onIncrementClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onDecrementClick = { prod, vid -> viewModel.decrementCart(prod.id, vid) },
                    onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                    brands = brands,
                    modifier = standardScreenModifier
                )
            }

            composable(
                route = QuickKartRoutes.SUBCATEGORY_PRODUCTS,
                arguments = listOf(
                    navArgument("shopCategory") { type = NavType.StringType },
                    navArgument("subCategory") { type = NavType.StringType }
                )
            ) { entry ->
                val shopCategory = Uri.decode(entry.arguments?.getString("shopCategory").orEmpty())
                val rawSubCategory = entry.arguments?.getString("subCategory").orEmpty()
                val initialSub = if (rawSubCategory == "__ALL__") null else Uri.decode(rawSubCategory)
                val leafChips = remember(initialSub) { ShopCategoryTaxonomy.leafFiltersFor(initialSub) }
                val useLeafStrip = initialSub != null && leafChips.isNotEmpty()
                var selectedLeaf by rememberSaveable(shopCategory, initialSub) { mutableStateOf<String?>(null) }

                val activeSub = if (useLeafStrip) {
                    initialSub
                } else if (filterState.shopCategory == shopCategory) {
                    filterState.subCategory
                } else {
                    initialSub
                }
                val title = if (useLeafStrip) {
                    selectedLeaf ?: initialSub!!
                } else {
                    activeSub ?: shopCategory
                }
                val group = ShopCategoryTaxonomy.findByTitle(shopCategory)

                val allGroupProducts = products.filter { it.shopCategory == shopCategory }
                val baseProducts = if (filterState.shopCategory == shopCategory) {
                    if (useLeafStrip && filterState.subCategory != initialSub) {
                        filteredProducts.filter { it.subCategory == initialSub }
                    } else {
                        filteredProducts
                    }
                } else {
                    allGroupProducts.filter { activeSub == null || it.subCategory == activeSub }
                }
                val displayedProducts = remember(baseProducts, useLeafStrip, selectedLeaf) {
                    if (!useLeafStrip || selectedLeaf == null) {
                        baseProducts
                    } else {
                        baseProducts.filter { ShopCategoryTaxonomy.matchesLeafFilter(it, selectedLeaf!!) }
                    }
                }

                BrandProductsScreen(
                    title = title,
                    categories = categories,
                    allSubBrandProducts = allGroupProducts,
                    products = displayedProducts,
                    filterState = filterState,
                    cartItems = cartItems,
                    notifyProductIds = notifyProductIds,
                    etaMinutes = storeConfig.etaMinutes,
                    onBackClick = { navController.popBackStack() },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCategoryChipClick = {},
                    onOpenFilterSheet = { showFilterSheet = true },
                    onProductClick = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onAddClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onIncrementClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onDecrementClick = { prod, vid -> viewModel.decrementCart(prod.id, vid) },
                    onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                    brands = brands,
                    customChipLabels = if (useLeafStrip) null else (group?.subcategories ?: emptyList()),
                    selectedCustomChip = if (useLeafStrip) null else activeSub,
                    onCustomChipClick = if (useLeafStrip) {
                        null
                    } else {
                        { clickedSub ->
                            if (clickedSub != null && ShopCategoryTaxonomy.leafFiltersFor(clickedSub).isNotEmpty()) {
                                viewModel.selectShopCategoryFilter(shopCategory, clickedSub)
                                navController.navigate(QuickKartRoutes.subCategoryProducts(shopCategory, clickedSub))
                            } else {
                                viewModel.setSubCategoryWithinCurrentFilter(clickedSub)
                            }
                        }
                    },
                    leafChipLabels = if (useLeafStrip) leafChips else null,
                    selectedLeafChip = selectedLeaf,
                    onLeafChipClick = if (useLeafStrip) {
                        { clicked -> selectedLeaf = if (selectedLeaf == clicked) null else clicked }
                    } else null,
                    modifier = standardScreenModifier
                )
            }

            composable(
                route = QuickKartRoutes.PRODUCT_DETAIL,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { entry ->
                val productId = entry.arguments?.getString("productId")
                val product = products.firstOrNull { it.id == productId }
                if (product == null) {
                    Box(standardScreenModifier, contentAlignment = Alignment.Center) {
                        Text("Product not found")
                    }
                } else {
                    val productBrand = brands.firstOrNull { it.id == product.brandId }
                    val sameSubBrandCount = remember(products, product.brandId, product.subBrand) {
                        if (product.subBrand.isBlank()) 0
                        else products.count { it.brandId == product.brandId && it.subBrand == product.subBrand }
                    }
                    val targetSubBrand = if (sameSubBrandCount > 1) product.subBrand else null
                    val seeMoreBrandLabel = targetSubBrand
                        ?: productBrand?.name
                        ?: product.subBrand.ifBlank { "Brand" }

                    ProductDetailScreen(
                        product = product,
                        brand = productBrand,
                        category = categories.firstOrNull { it.id == product.categoryId },
                        cartItems = cartItems,
                        totalCartCount = cartSummary.totalCount,
                        isNotified = product.id in notifyProductIds,
                        isSaved = product.id in savedProductIds,
                        etaMinutes = storeConfig.etaMinutes,
                        seeMoreBrandLabel = seeMoreBrandLabel,
                        onBackClick = { navController.popBackStack() },
                        onSearchClick = {
                            viewModel.resetFiltersCompletely()
                            navController.navigate(QuickKartRoutes.CATALOG)
                        },
                        onCartClick = { navigateTopTab(QuickKartRoutes.CART) },
                        onSeeMoreBrandClick = {
                            viewModel.selectSubBrandFilter(product.brandId, targetSubBrand)
                            navController.navigate(QuickKartRoutes.brandProducts(product.brandId, targetSubBrand))
                        },
                        onSaveForLaterClick = { viewModel.toggleSaveForLater(product.id, product.name) },
                        onAddClick = { vid -> viewModel.addOrIncrementCart(product, vid) },
                        onIncrementClick = { vid -> viewModel.addOrIncrementCart(product, vid) },
                        onDecrementClick = { vid -> viewModel.decrementCart(product.id, vid) },
                        onNotifyClick = { viewModel.toggleNotifyMe(product.id, product.name) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            composable(QuickKartRoutes.ORDERS) {
                OrdersScreen(
                    orders = orders,
                    onOrderAgain = { order ->
                        viewModel.orderAgain(order) {
                            navigateTopTab(QuickKartRoutes.CART)
                        }
                    },
                    onBrowseGroceries = { navigateTopTab(QuickKartRoutes.HOME) },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.CART) {
                CartScreen(
                    cartSummary = cartSummary,
                    storeConfig = storeConfig,
                    brands = brands,
                    onIncrement = { pid, vid ->
                        products.firstOrNull { it.id == pid }?.let { prod ->
                            viewModel.addOrIncrementCart(prod, vid)
                        }
                    },
                    onDecrement = { pid, vid -> viewModel.decrementCart(pid, vid) },
                    onClearCart = viewModel::clearCart,
                    onBrowseProducts = { navigateTopTab(QuickKartRoutes.CATEGORIES) },
                    onCheckoutClick = {
                        viewModel.clearLastPlacedOrder()
                        navController.navigate(QuickKartRoutes.CHECKOUT)
                    },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.CHECKOUT) {
                CheckoutScreen(
                    cartSummary = cartSummary,
                    addresses = addresses,
                    deliverySlots = deliverySlots,
                    userProfile = userProfile,
                    isProcessing = isProcessingOrder,
                    placedOrder = lastPlacedOrder,
                    onBackClick = {
                        viewModel.clearLastPlacedOrder()
                        navController.popBackStack()
                    },
                    onAddAddress = viewModel::saveAddress,
                    onPlaceOrder = { addr, slot, method ->
                        viewModel.placeOrder(addr, slot, method) {}
                    },
                    onViewOrdersAfterSuccess = {
                        viewModel.clearLastPlacedOrder()
                        navController.navigate(QuickKartRoutes.ORDERS) {
                            popUpTo(QuickKartRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    onContinueShoppingAfterSuccess = {
                        viewModel.clearLastPlacedOrder()
                        navigateTopTab(QuickKartRoutes.HOME)
                    },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.ACCOUNT) {
                val savedProducts = remember(products, savedProductIds) {
                    products.filter { it.id in savedProductIds }
                }
                AccountScreen(
                    userProfile = userProfile,
                    addresses = addresses,
                    onUpdateProfile = viewModel::updateProfile,
                    onTopUpWallet = viewModel::topUpWallet,
                    onSaveAddress = viewModel::saveAddress,
                    onSetDefaultAddress = viewModel::setDefaultAddress,
                    onDeleteAddress = viewModel::deleteAddress,
                    onOpenAdminPanel = { navController.navigate(QuickKartRoutes.ADMIN) },
                    onLogout = viewModel::logout,
                    savedProducts = savedProducts,
                    onOpenSavedProduct = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onRemoveSavedProduct = { prod ->
                        viewModel.toggleSaveForLater(prod.id, prod.name)
                    },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.ADMIN) {
                AdminScreen(
                    products = products,
                    categories = categories,
                    brands = brands,
                    orders = orders,
                    deliverySlots = deliverySlots,
                    storeConfig = storeConfig,
                    onExitAdmin = { navController.popBackStack() },
                    onSaveProduct = viewModel::adminSaveProduct,
                    onDeleteProduct = viewModel::adminDeleteProduct,
                    onSaveCategory = viewModel::adminSaveCategory,
                    onDeleteCategory = viewModel::adminDeleteCategory,
                    onSaveBrand = viewModel::adminSaveBrand,
                    onDeleteBrand = viewModel::adminDeleteBrand,
                    onUpdateOrderStatus = viewModel::adminUpdateOrderStatus,
                    onSaveStoreConfig = viewModel::adminSaveStoreConfig,
                    onSaveDeliverySlot = viewModel::adminSaveDeliverySlot,
                    onDeleteDeliverySlot = viewModel::adminDeleteDeliverySlot,
                    modifier = standardScreenModifier
                )
            }
        }
    }

    if (showFilterSheet) {
        FilterSortBottomSheet(
            currentFilter = filterState,
            brands = brands,
            packSizes = availablePackSizes,
            onApply = { updated ->
                viewModel.applyFilterState(updated)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    if (showAddressPickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressPickerSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Choose Delivery Location", style = MaterialTheme.typography.titleLarge)
                    TextButton(onClick = {
                        showAddressPickerSheet = false
                        showNewAddressDialogFromPicker = true
                    }) {
                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Add New", color = QuickGreen)
                    }
                }
                addresses.forEach { addr ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (addr.isDefault) QuickGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (addr.isDefault) QuickGreen else MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setDefaultAddress(addr.id)
                                showAddressPickerSheet = false
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = QuickGreen)
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(addr.label, style = MaterialTheme.typography.titleSmall)
                                Text(addr.shortSummary, style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                            }
                            if (addr.isDefault) {
                                Icon(Icons.Rounded.CheckCircle, contentDescription = "Active", tint = QuickGreen)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
            }
        }
    }

    if (showNewAddressDialogFromPicker) {
        AddEditAddressDialog(
            initial = null,
            defaultPhone = userProfile.phone.ifEmpty { "9876543210" },
            defaultName = userProfile.name,
            onDismiss = { showNewAddressDialogFromPicker = false },
            onSave = { newAddr ->
                viewModel.saveAddress(newAddr)
                showNewAddressDialogFromPicker = false
            }
        )
    }
}

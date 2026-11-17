package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.Category
import com.example.model.DeliverySlot
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ProductVariant
import com.example.model.StoreConfig
import com.example.ui.components.BrandVisualIcon
import com.example.ui.components.ProductVisual
import com.example.ui.components.resolveSubBrandLogoUrl
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickGreenDark
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickRed
import com.example.util.FormatUtils
import java.util.UUID

@Composable
fun AdminScreen(
    products: List<Product>,
    categories: List<Category>,
    brands: List<Brand>,
    orders: List<OrderRecord>,
    deliverySlots: List<DeliverySlot>,
    storeConfig: StoreConfig,
    onExitAdmin: () -> Unit,
    onSaveProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onSaveCategory: (Category) -> Unit,
    onDeleteCategory: (String) -> Unit,
    onSaveBrand: (Brand) -> Unit,
    onDeleteBrand: (String) -> Unit,
    onUpdateOrderStatus: (String, OrderStatus) -> Unit,
    onSaveStoreConfig: (StoreConfig) -> Unit,
    onSaveDeliverySlot: (DeliverySlot) -> Unit,
    onDeleteDeliverySlot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Products (${products.size})", "Categories (${categories.size})", "Brands (${brands.size})", "Orders (${orders.size})", "Delivery & Slots")

    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var showCreateProduct by remember { mutableStateOf(false) }

    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var showCreateCategory by remember { mutableStateOf(false) }

    var editingBrand by remember { mutableStateOf<Brand?>(null) }
    var showCreateBrand by remember { mutableStateOf(false) }

    var showCreateSlot by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(color = QuickGreenDark, contentColor = Color.White) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "QuickKart Admin (/admin)",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Catalog, Margins, Inventory, Orders & Delivery Control",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        OutlinedButton(
                            onClick = onExitAdmin,
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier.testTag("admin_exit_button")
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Storefront")
                        }
                    }

                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = QuickGreenDark,
                        contentColor = Color.White,
                        edgePadding = 12.dp
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, color = Color.White) }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            when (selectedTab) {
                0 -> FloatingActionButton(
                    onClick = { showCreateProduct = true },
                    containerColor = QuickGreen,
                    contentColor = Color.White,
                    modifier = Modifier.navigationBarsPadding().testTag("admin_add_product_fab")
                ) { Icon(Icons.Rounded.Add, contentDescription = "Add Product") }
                1 -> FloatingActionButton(
                    onClick = { showCreateCategory = true },
                    containerColor = QuickGreen,
                    contentColor = Color.White,
                    modifier = Modifier.navigationBarsPadding()
                ) { Icon(Icons.Rounded.Add, contentDescription = "Add Category") }
                2 -> FloatingActionButton(
                    onClick = { showCreateBrand = true },
                    containerColor = QuickGreen,
                    contentColor = Color.White,
                    modifier = Modifier.navigationBarsPadding()
                ) { Icon(Icons.Rounded.Add, contentDescription = "Add Brand") }
                4 -> FloatingActionButton(
                    onClick = { showCreateSlot = true },
                    containerColor = QuickGreen,
                    contentColor = Color.White,
                    modifier = Modifier.navigationBarsPadding()
                ) { Icon(Icons.Rounded.Add, contentDescription = "Add Delivery Slot") }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> AdminProductsTab(
                products = products,
                categories = categories,
                brands = brands,
                onEdit = { editingProduct = it },
                onDelete = onDeleteProduct,
                modifier = Modifier.padding(innerPadding)
            )
            1 -> AdminCategoriesTab(
                categories = categories,
                onEdit = { editingCategory = it },
                onDelete = onDeleteCategory,
                modifier = Modifier.padding(innerPadding)
            )
            2 -> AdminBrandsTab(
                brands = brands,
                onEdit = { editingBrand = it },
                onDelete = onDeleteBrand,
                modifier = Modifier.padding(innerPadding)
            )
            3 -> AdminOrdersTab(
                orders = orders,
                onUpdateStatus = onUpdateOrderStatus,
                modifier = Modifier.padding(innerPadding)
            )
            4 -> AdminDeliveryTab(
                storeConfig = storeConfig,
                slots = deliverySlots,
                onSaveStoreConfig = onSaveStoreConfig,
                onToggleSlot = { slot -> onSaveDeliverySlot(slot.copy(isEnabled = !slot.isEnabled)) },
                onDeleteSlot = onDeleteDeliverySlot,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (showCreateProduct || editingProduct != null) {
        AdminProductDialog(
            initial = editingProduct,
            categories = categories,
            brands = brands,
            onDismiss = {
                showCreateProduct = false
                editingProduct = null
            },
            onSave = { prod ->
                onSaveProduct(prod)
                showCreateProduct = false
                editingProduct = null
            }
        )
    }

    if (showCreateCategory || editingCategory != null) {
        AdminCategoryDialog(
            initial = editingCategory,
            onDismiss = {
                showCreateCategory = false
                editingCategory = null
            },
            onSave = { cat ->
                onSaveCategory(cat)
                showCreateCategory = false
                editingCategory = null
            }
        )
    }

    if (showCreateBrand || editingBrand != null) {
        AdminBrandDialog(
            initial = editingBrand,
            onDismiss = {
                showCreateBrand = false
                editingBrand = null
            },
            onSave = { brand ->
                onSaveBrand(brand)
                showCreateBrand = false
                editingBrand = null
            }
        )
    }

    if (showCreateSlot) {
        AdminSlotDialog(
            onDismiss = { showCreateSlot = false },
            onSave = { slot ->
                onSaveDeliverySlot(slot)
                showCreateSlot = false
            }
        )
    }
}

@Composable
private fun AdminProductsTab(
    products: List<Product>,
    categories: List<Category>,
    brands: List<Brand>,
    onEdit: (Product) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var search by remember { mutableStateOf("") }
    val catMap = categories.associateBy { it.id }
    val brandMap = brands.associateBy { it.id }
    val visible = products.filter {
        search.isBlank() || it.name.contains(search, ignoreCase = true) ||
            (brandMap[it.brandId]?.name?.contains(search, ignoreCase = true) == true)
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Search catalog products...") },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 84.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(visible, key = { it.id }) { prod ->
                val v = prod.defaultVariant
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val prodBrand = brandMap[prod.brandId]
                        ProductVisual(
                            imageKey = prod.imageKey,
                            colorHex = prod.colorHex,
                            iconSize = 24.dp,
                            imageUrl = prod.imageUrl,
                            brandName = prod.subBrand.ifBlank { prodBrand?.name.orEmpty() },
                            brandLogoUrl = resolveSubBrandLogoUrl(prod.subBrand, prodBrand?.logoUrl.orEmpty()),
                            brandColorHex = prodBrand?.colorHex ?: 0xFF0C831FL,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(prod.name, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "${brandMap[prod.brandId]?.name ?: ""} • ${catMap[prod.categoryId]?.name ?: ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = QuickMuted
                            )
                            Text(
                                "${prod.variants.size} variant(s) • ${FormatUtils.inr(v.sellingPrice)} (MRP ${FormatUtils.inr(v.mrp)}) • Margin ${v.marginPercent}% • Stock ${prod.variants.sumOf { it.stock }}",
                                style = MaterialTheme.typography.labelSmall,
                                color = QuickGreen
                            )
                        }
                        IconButton(onClick = { onEdit(prod) }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "Edit Product")
                        }
                        IconButton(onClick = { onDelete(prod.id) }) {
                            Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete Product", tint = QuickRed)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminCategoriesTab(
    categories: List<Category>,
    onEdit: (Category) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = { it.id }) { cat ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(cat.name, style = MaterialTheme.typography.titleSmall)
                        Text("${cat.subtitle} • Key: ${cat.iconKey}", style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                    }
                    IconButton(onClick = { onEdit(cat) }) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit Category")
                    }
                    IconButton(onClick = { onDelete(cat.id) }) {
                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete Category", tint = QuickRed)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminBrandsTab(
    brands: List<Brand>,
    onEdit: (Brand) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(brands, key = { it.id }) { brand ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrandVisualIcon(brand = brand, size = 44.dp)
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(brand.name, style = MaterialTheme.typography.titleSmall)
                        Text("${brand.tagline} • ${brand.badgeText}", style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                    }
                    IconButton(onClick = { onEdit(brand) }) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit Brand")
                    }
                    IconButton(onClick = { onDelete(brand.id) }) {
                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete Brand", tint = QuickRed)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdminOrdersTab(
    orders: List<OrderRecord>,
    onUpdateStatus: (String, OrderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(orders, key = { it.id }) { order ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${order.id} • ${FormatUtils.inr(order.grandTotal)}", style = MaterialTheme.typography.titleSmall)
                        Text(FormatUtils.formatDateTime(order.createdAtMillis), style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${order.items.sumOf { it.quantity }} item(s) • ${order.deliverySlotLabel}", style = MaterialTheme.typography.bodySmall)
                    Text(order.addressSnapshot, style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                    Spacer(Modifier.height(8.dp))
                    Text("Update Order Status:", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OrderStatus.entries.forEach { st ->
                            FilterChip(
                                selected = order.status == st,
                                onClick = { onUpdateStatus(order.id, st) },
                                label = { Text(st.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = QuickGreenContainer,
                                    selectedLabelColor = QuickGreen
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminDeliveryTab(
    storeConfig: StoreConfig,
    slots: List<DeliverySlot>,
    onSaveStoreConfig: (StoreConfig) -> Unit,
    onToggleSlot: (DeliverySlot) -> Unit,
    onDeleteSlot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var fee by remember(storeConfig) { mutableStateOf(storeConfig.baseDeliveryFee.toInt().toString()) }
    var threshold by remember(storeConfig) { mutableStateOf(storeConfig.freeDeliveryThreshold.toInt().toString()) }
    var eta by remember(storeConfig) { mutableStateOf(storeConfig.etaMinutes.toString()) }
    var promo by remember(storeConfig) { mutableStateOf(storeConfig.promoBannerText) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Delivery Charges & Storefront Banner", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = fee,
                            onValueChange = { fee = it.filter(Char::isDigit) },
                            label = { Text("Delivery Fee (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = threshold,
                            onValueChange = { threshold = it.filter(Char::isDigit) },
                            label = { Text("Free Above (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = eta,
                            onValueChange = { eta = it.filter(Char::isDigit) },
                            label = { Text("ETA (mins)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = promo,
                            onValueChange = { promo = it },
                            label = { Text("Top Promo Text") },
                            modifier = Modifier.weight(2f)
                        )
                    }
                    Button(
                        onClick = {
                            onSaveStoreConfig(
                                StoreConfig(
                                    baseDeliveryFee = fee.toDoubleOrNull() ?: 25.0,
                                    freeDeliveryThreshold = threshold.toDoubleOrNull() ?: 199.0,
                                    etaMinutes = eta.toIntOrNull() ?: 10,
                                    promoBannerText = promo.ifBlank { "Free delivery above ₹199" }
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Delivery Settings", color = Color.White)
                    }
                }
            }
        }

        item {
            Text("Delivery Slots (${slots.size})", style = MaterialTheme.typography.titleMedium)
        }

        items(slots, key = { it.id }) { slot ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(slot.label, style = MaterialTheme.typography.titleSmall)
                        Text(
                            "${slot.timeRange} • Fee: ${if (slot.fee == 0.0) "FREE" else FormatUtils.inr(slot.fee)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickMuted
                        )
                    }
                    Switch(checked = slot.isEnabled, onCheckedChange = { onToggleSlot(slot) })
                    IconButton(onClick = { onDeleteSlot(slot.id) }) {
                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete Slot", tint = QuickRed)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdminProductDialog(
    initial: Product?,
    categories: List<Category>,
    brands: List<Brand>,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var selectedCatId by remember { mutableStateOf(initial?.categoryId ?: categories.firstOrNull()?.id.orEmpty()) }
    var selectedBrandId by remember { mutableStateOf(initial?.brandId ?: brands.firstOrNull()?.id.orEmpty()) }
    var imageKey by remember { mutableStateOf(initial?.imageKey ?: "tomato") }
    var imageUrl by remember { mutableStateOf(initial?.imageUrl ?: "") }
    var featured by remember { mutableStateOf(initial?.isFeatured ?: false) }

    val variants = remember {
        mutableStateListOf<ProductVariant>().apply {
            if (initial != null && initial.variants.isNotEmpty()) {
                addAll(initial.variants)
            } else {
                add(ProductVariant("v_${UUID.randomUUID().toString().take(6)}", "500 g", 100.0, 85.0, 20.0, 25))
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Product" else "Edit Product") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, singleLine = true)
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = imageKey, onValueChange = { imageKey = it }, label = { Text("Image Key (e.g. milk, tomato, atta)") }, singleLine = true)
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Product Image URL (optional)") }, singleLine = true)

                Text("Category:", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEach { c ->
                        FilterChip(
                            selected = selectedCatId == c.id,
                            onClick = { selectedCatId = c.id },
                            label = { Text(c.name) }
                        )
                    }
                }

                Text("Brand:", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    brands.forEach { b ->
                        FilterChip(
                            selected = selectedBrandId == b.id,
                            onClick = { selectedBrandId = b.id },
                            label = { Text(b.name) }
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Featured on Home")
                    Switch(checked = featured, onCheckedChange = { featured = it })
                }

                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Size Variants (${variants.size})", style = MaterialTheme.typography.titleSmall)
                    TextButton(onClick = {
                        variants.add(ProductVariant("v_${UUID.randomUUID().toString().take(6)}", "1 kg", 190.0, 159.0, 22.0, 15))
                    }) {
                        Text("+ Variant")
                    }
                }

                variants.forEachIndexed { idx, vItem ->
                    var label by remember(vItem.id) { mutableStateOf(vItem.label) }
                    var mrp by remember(vItem.id) { mutableStateOf(vItem.mrp.toInt().toString()) }
                    var sell by remember(vItem.id) { mutableStateOf(vItem.sellingPrice.toInt().toString()) }
                    var margin by remember(vItem.id) { mutableStateOf(vItem.marginPercent.toString()) }
                    var stock by remember(vItem.id) { mutableStateOf(vItem.stock.toString()) }

                    fun sync() {
                        variants[idx] = vItem.copy(
                            label = label,
                            mrp = mrp.toDoubleOrNull() ?: vItem.mrp,
                            sellingPrice = sell.toDoubleOrNull() ?: vItem.sellingPrice,
                            marginPercent = margin.toDoubleOrNull() ?: vItem.marginPercent,
                            stock = stock.toIntOrNull() ?: vItem.stock
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Text("Variant #${idx + 1}", style = MaterialTheme.typography.labelMedium)
                                if (variants.size > 1) {
                                    TextButton(onClick = { variants.removeAt(idx) }) {
                                        Text("Remove", color = QuickRed)
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = label,
                                onValueChange = { label = it; sync() },
                                label = { Text("Pack Size (e.g. 500 g)") },
                                singleLine = true
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = mrp,
                                    onValueChange = { mrp = it.filter(Char::isDigit); sync() },
                                    label = { Text("MRP ₹") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = sell,
                                    onValueChange = { sell = it.filter(Char::isDigit); sync() },
                                    label = { Text("Price ₹") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = margin,
                                    onValueChange = { margin = it; sync() },
                                    label = { Text("Margin %") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = stock,
                                    onValueChange = { stock = it.filter(Char::isDigit); sync() },
                                    label = { Text("Stock") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val saved = Product(
                        id = initial?.id ?: "p_${UUID.randomUUID().toString().take(6)}",
                        name = name.ifBlank { "New Grocery Item" },
                        description = description.ifBlank { "Fresh quality product delivered in minutes." },
                        categoryId = selectedCatId,
                        brandId = selectedBrandId,
                        subBrand = initial?.subBrand ?: "",
                        shopCategory = initial?.shopCategory ?: "",
                        subCategory = initial?.subCategory ?: "",
                        isVeg = initial?.isVeg ?: true,
                        imageKey = imageKey.ifBlank { "tomato" },
                        imageUrl = imageUrl.trim(),
                        colorHex = initial?.colorHex ?: 0xFFE8F7EA,
                        rating = initial?.rating ?: 4.6,
                        reviewCount = initial?.reviewCount ?: 25,
                        isFeatured = featured,
                        variants = variants.toList(),
                        reviews = initial?.reviews ?: emptyList()
                    )
                    onSave(saved)
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen)
            ) { Text("Save Product", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AdminCategoryDialog(
    initial: Category?,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var subtitle by remember { mutableStateOf(initial?.subtitle ?: "") }
    var iconKey by remember { mutableStateOf(initial?.iconKey ?: "veg") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Category" else "Edit Category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Category Name") }, singleLine = true)
                OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle") }, singleLine = true)
                OutlinedTextField(value = iconKey, onValueChange = { iconKey = it }, label = { Text("Icon Key (veg, fruits, dairy...)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        Category(
                            id = initial?.id ?: "cat_${UUID.randomUUID().toString().take(6)}",
                            name = name.ifBlank { "New Category" },
                            subtitle = subtitle.ifBlank { "Daily essentials" },
                            iconKey = iconKey.ifBlank { "veg" },
                            colorHex = initial?.colorHex ?: 0xFFE8F7EA,
                            sortOrder = initial?.sortOrder ?: 99
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen)
            ) { Text("Save", color = Color.White) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun AdminBrandDialog(
    initial: Brand?,
    onDismiss: () -> Unit,
    onSave: (Brand) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var tagline by remember { mutableStateOf(initial?.tagline ?: "") }
    var badge by remember { mutableStateOf(initial?.badgeText ?: "Featured") }
    var logoUrl by remember { mutableStateOf(initial?.logoUrl ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Brand" else "Edit Brand") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Brand Name") }, singleLine = true)
                OutlinedTextField(value = tagline, onValueChange = { tagline = it }, label = { Text("Tagline") }, singleLine = true)
                OutlinedTextField(value = badge, onValueChange = { badge = it }, label = { Text("Badge Text") }, singleLine = true)
                OutlinedTextField(value = logoUrl, onValueChange = { logoUrl = it }, label = { Text("Brand Logo URL (optional)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        Brand(
                            id = initial?.id ?: "brand_${UUID.randomUUID().toString().take(6)}",
                            name = name.ifBlank { "New Brand" },
                            tagline = tagline.ifBlank { "Quality Promise" },
                            badgeText = badge.ifBlank { "Trusted" },
                            colorHex = initial?.colorHex ?: 0xFF15803D,
                            logoUrl = logoUrl.trim()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen)
            ) { Text("Save", color = Color.White) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun AdminSlotDialog(
    onDismiss: () -> Unit,
    onSave: (DeliverySlot) -> Unit
) {
    var label by remember { mutableStateOf("") }
    var range by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("0") }
    var express by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Delivery Slot") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Slot Title (e.g. Late Night)") }, singleLine = true)
                OutlinedTextField(value = range, onValueChange = { range = it }, label = { Text("Time Range (e.g. 9:00 PM – 11:00 PM)") }, singleLine = true)
                OutlinedTextField(
                    value = fee,
                    onValueChange = { fee = it.filter(Char::isDigit) },
                    label = { Text("Slot Fee (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Express Slot")
                    Switch(checked = express, onCheckedChange = { express = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        DeliverySlot(
                            id = "slot_${UUID.randomUUID().toString().take(6)}",
                            label = label.ifBlank { "Custom Slot" },
                            timeRange = range.ifBlank { "4:00 PM – 6:00 PM" },
                            fee = fee.toDoubleOrNull() ?: 0.0,
                            isExpress = express,
                            isEnabled = true
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen)
            ) { Text("Add Slot", color = Color.White) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

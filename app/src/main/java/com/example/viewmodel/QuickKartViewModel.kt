package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SeedCatalogData
import com.example.data.repository.DemoPaymentGateway
import com.example.data.repository.PaymentGateway
import com.example.data.repository.PaymentMethodType
import com.example.data.repository.PaymentResult
import com.example.data.repository.QuickCommerceRepository
import com.example.data.repository.RazorpayOrderPayload
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.CartItemDetailed
import com.example.model.CartSummary
import com.example.model.Category
import com.example.model.DeliverySlot
import com.example.model.OrderLineItem
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ProductFilterState
import com.example.model.SavedAddress
import com.example.model.SortOption
import com.example.model.StoreConfig
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuickKartViewModel(
    private val repository: QuickCommerceRepository,
    private val paymentGateway: PaymentGateway = DemoPaymentGateway()
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _uiEvents = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val uiEvents: SharedFlow<String> = _uiEvents.asSharedFlow()

    private val _isProcessingOrder = MutableStateFlow(false)
    val isProcessingOrder: StateFlow<Boolean> = _isProcessingOrder.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<OrderRecord?>(null)
    val lastPlacedOrder: StateFlow<OrderRecord?> = _lastPlacedOrder.asStateFlow()

    val categories: StateFlow<List<Category>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val brands: StateFlow<List<Brand>> = repository.brands
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<Product>> = repository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val addresses: StateFlow<List<SavedAddress>> = repository.addresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderRecord>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deliverySlots: StateFlow<List<DeliverySlot>> = repository.deliverySlots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storeConfig: StateFlow<StoreConfig> = repository.storeConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SeedCatalogData.defaultStoreConfig)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SeedCatalogData.defaultUserProfile)

    val notifyProductIds: StateFlow<Set<String>> = repository.notifyProductIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val savedProductIds: StateFlow<Set<String>> = repository.savedProductIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _filterState = MutableStateFlow(ProductFilterState())
    val filterState: StateFlow<ProductFilterState> = _filterState.asStateFlow()

    val cartSummary: StateFlow<CartSummary> = combine(
        cartItems,
        products,
        storeConfig
    ) { items, allProducts, config ->
        val productMap = allProducts.associateBy { it.id }
        val detailed = items.mapNotNull { ci ->
            val prod = productMap[ci.productId] ?: return@mapNotNull null
            val variant = prod.variants.firstOrNull { it.id == ci.variantId } ?: return@mapNotNull null
            CartItemDetailed(prod, variant, ci.quantity)
        }
        val totalCount = detailed.sumOf { it.quantity }
        val mrpTotal = detailed.sumOf { it.lineMrpTotal }
        val itemTotal = detailed.sumOf { it.lineSellingTotal }
        val itemSavings = detailed.sumOf { it.lineSavings }
        val deliveryFee = when {
            detailed.isEmpty() -> 0.0
            itemTotal >= config.freeDeliveryThreshold -> 0.0
            else -> config.baseDeliveryFee
        }
        CartSummary(
            items = detailed,
            totalCount = totalCount,
            mrpTotal = mrpTotal,
            itemTotal = itemTotal,
            itemSavings = itemSavings,
            deliveryFee = deliveryFee,
            freeDeliveryThreshold = config.freeDeliveryThreshold,
            grandTotal = itemTotal + deliveryFee
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CartSummary(emptyList(), 0, 0.0, 0.0, 0.0, 0.0, 199.0, 0.0)
    )

    val filteredProducts: StateFlow<List<Product>> = combine(
        products,
        brands,
        categories,
        _filterState
    ) { allProducts, allBrands, allCategories, filter ->
        val brandNames = allBrands.associate { it.id to it.name.lowercase() }
        val catNames = allCategories.associate { it.id to it.name.lowercase() }
        val query = filter.query.trim().lowercase()

        val matched = allProducts.filter { prod ->
            val v = prod.defaultVariant
            val matchesQuery = query.isEmpty() ||
                prod.name.lowercase().contains(query) ||
                prod.description.lowercase().contains(query) ||
                prod.subBrand.lowercase().contains(query) ||
                prod.shopCategory.lowercase().contains(query) ||
                prod.subCategory.lowercase().contains(query) ||
                (brandNames[prod.brandId]?.contains(query) == true) ||
                (catNames[prod.categoryId]?.contains(query) == true)

            val matchesCategory = filter.categoryId == null || prod.categoryId == filter.categoryId
            val matchesBrandSingle = filter.brandId == null || prod.brandId == filter.brandId
            val matchesSubBrand = filter.subBrand == null || prod.subBrand == filter.subBrand
            val matchesShopCategory = filter.shopCategory == null || prod.shopCategory == filter.shopCategory
            val matchesSubCategory = filter.subCategory == null || prod.subCategory == filter.subCategory
            val matchesBrandsMulti = filter.selectedBrands.isEmpty() || prod.brandId in filter.selectedBrands
            val matchesMinPrice = filter.minPrice == null || prod.variants.any { it.sellingPrice >= filter.minPrice }
            val matchesMaxPrice = filter.maxPrice == null || prod.variants.any { it.sellingPrice <= filter.maxPrice }
            val matchesDiscount = filter.minDiscountPercent <= 0 || prod.variants.any { it.discountPercent >= filter.minDiscountPercent }
            val matchesRating = prod.rating >= filter.minRating
            val matchesPackSize = filter.selectedPackSizes.isEmpty() || prod.variants.any { it.label in filter.selectedPackSizes }
            val matchesStock = !filter.inStockOnly || prod.anyVariantInStock

            matchesQuery && matchesCategory && matchesBrandSingle && matchesSubBrand &&
                matchesShopCategory && matchesSubCategory && matchesBrandsMulti &&
                matchesMinPrice && matchesMaxPrice && matchesDiscount && matchesRating &&
                matchesPackSize && matchesStock
        }

        when (filter.sortOption) {
            SortOption.RELEVANCE -> matched.sortedWith(
                compareByDescending<Product> { it.anyVariantInStock }
                    .thenByDescending { it.isFeatured }
                    .thenByDescending { it.rating }
            )
            SortOption.PRICE_LOW_HIGH -> matched.sortedBy { it.defaultVariant.sellingPrice }
            SortOption.PRICE_HIGH_LOW -> matched.sortedByDescending { it.defaultVariant.sellingPrice }
            SortOption.SAVINGS_HIGH -> matched.sortedByDescending { it.defaultVariant.savingsAmount }
            SortOption.DISCOUNT_HIGH -> matched.sortedByDescending { it.defaultVariant.discountPercent }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availablePackSizes: StateFlow<List<String>> = products.combine(_filterState) { list, _ ->
        list.flatMap { p -> p.variants.map { it.label } }
            .distinct()
            .sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            _isReady.value = true
        }
    }

    // Auth
    fun verifyOtp(phone: String, otp: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val cleanedPhone = phone.filter { it.isDigit() }
        if (cleanedPhone.length != 10) {
            onError("Enter a valid 10-digit Indian mobile number")
            return
        }
        if (otp.trim() != "1234") {
            onError("Invalid OTP. Use demo OTP 1234")
            return
        }
        viewModelScope.launch {
            repository.loginWithPhone(cleanedPhone)
            onSuccess()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiEvents.emit("Logged out successfully")
        }
    }

    // Search & Filters
    fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(query = query) }
    }

    fun selectCategoryFilter(categoryId: String?) {
        _filterState.update {
            it.copy(
                categoryId = categoryId,
                brandId = null,
                subBrand = null,
                shopCategory = null,
                subCategory = null
            )
        }
    }

    fun selectBrandFilter(brandId: String?) {
        _filterState.update {
            it.copy(
                brandId = brandId,
                categoryId = null,
                subBrand = null,
                shopCategory = null,
                subCategory = null
            )
        }
    }

    fun selectSubBrandFilter(brandId: String, subBrand: String?) {
        _filterState.update {
            ProductFilterState(brandId = brandId, subBrand = subBrand)
        }
    }

    fun selectShopCategoryFilter(shopCategory: String, subCategory: String?) {
        _filterState.update {
            ProductFilterState(shopCategory = shopCategory, subCategory = subCategory)
        }
    }

    fun setSubCategoryWithinCurrentFilter(subCategory: String?) {
        _filterState.update { it.copy(subCategory = subCategory) }
    }

    fun setCategoryWithinCurrentFilter(categoryId: String?) {
        _filterState.update { it.copy(categoryId = categoryId) }
    }

    fun applyFilterState(newState: ProductFilterState) {
        _filterState.value = newState
    }

    fun clearAllFilters() {
        _filterState.update {
            ProductFilterState(
                query = it.query,
                categoryId = it.categoryId,
                brandId = it.brandId,
                subBrand = it.subBrand,
                shopCategory = it.shopCategory,
                subCategory = it.subCategory
            )
        }
    }

    fun resetFiltersCompletely() {
        _filterState.value = ProductFilterState()
    }

    // Cart
    fun getQuantityInCart(productId: String, variantId: String): Int {
        return cartItems.value.firstOrNull { it.productId == productId && it.variantId == variantId }?.quantity ?: 0
    }

    fun addOrIncrementCart(product: Product, variantId: String = product.defaultVariant.id) {
        val variant = product.variants.firstOrNull { it.id == variantId } ?: return
        if (!variant.isInStock) {
            toggleNotifyMe(product.id, product.name)
            return
        }
        val currentQty = getQuantityInCart(product.id, variant.id)
        if (currentQty >= variant.stock) {
            _uiEvents.tryEmit("Only ${variant.stock} left in stock for ${variant.label}")
            return
        }
        viewModelScope.launch {
            repository.setCartQuantity(product.id, variant.id, currentQty + 1)
        }
    }

    fun decrementCart(productId: String, variantId: String) {
        val currentQty = getQuantityInCart(productId, variantId)
        viewModelScope.launch {
            repository.setCartQuantity(productId, variantId, (currentQty - 1).coerceAtLeast(0))
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleNotifyMe(productId: String, productName: String) {
        viewModelScope.launch {
            val subscribed = repository.toggleNotifyProduct(productId)
            if (subscribed) {
                _uiEvents.emit("We'll notify you as soon as $productName is back in stock!")
            } else {
                _uiEvents.emit("Removed stock alert for $productName")
            }
        }
    }

    fun toggleSaveForLater(productId: String, productName: String) {
        viewModelScope.launch {
            val saved = repository.toggleSaveForLater(productId)
            if (saved) {
                _uiEvents.emit("Saved $productName for later")
            } else {
                _uiEvents.emit("Removed $productName from Saved")
            }
        }
    }

    fun orderAgain(order: OrderRecord, onNavigateToCart: () -> Unit) {
        viewModelScope.launch {
            val currentProducts = products.value.associateBy { it.id }
            var addedCount = 0
            order.items.forEach { line ->
                val prod = currentProducts[line.productId] ?: return@forEach
                val variant = prod.variants.firstOrNull { it.id == line.variantId } ?: prod.defaultVariant
                if (variant.isInStock) {
                    val existing = getQuantityInCart(prod.id, variant.id)
                    val targetQty = (existing + line.quantity).coerceAtMost(variant.stock)
                    repository.setCartQuantity(prod.id, variant.id, targetQty)
                    addedCount++
                }
            }
            if (addedCount > 0) {
                _uiEvents.emit("Added $addedCount item(s) from ${order.id} to your cart")
                onNavigateToCart()
            } else {
                _uiEvents.emit("Items from this order are currently out of stock")
            }
        }
    }

    // Addresses
    fun saveAddress(address: SavedAddress) {
        viewModelScope.launch {
            repository.saveAddress(address)
            _uiEvents.emit("Saved address '${address.label}'")
        }
    }

    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            repository.setDefaultAddress(addressId)
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            repository.deleteAddress(addressId)
            _uiEvents.emit("Address deleted")
        }
    }

    // Checkout & Payment
    fun placeOrder(
        address: SavedAddress,
        slot: DeliverySlot,
        paymentMethod: PaymentMethodType,
        onOrderSuccess: (OrderRecord) -> Unit
    ) {
        val summary = cartSummary.value
        if (summary.items.isEmpty()) {
            _uiEvents.tryEmit("Your cart is empty")
            return
        }
        val finalDeliveryFee = summary.deliveryFee + slot.fee
        val finalGrandTotal = summary.itemTotal + finalDeliveryFee

        if (paymentMethod == PaymentMethodType.WALLET && userProfile.value.walletBalance < finalGrandTotal) {
            _uiEvents.tryEmit("Insufficient QuickKart Wallet balance. Please top up or choose UPI/COD.")
            return
        }

        viewModelScope.launch {
            _isProcessingOrder.value = true
            val payload = RazorpayOrderPayload(
                amountPaise = (finalGrandTotal * 100).toLong(),
                orderReceipt = "rcpt_${System.currentTimeMillis()}",
                customerPhone = userProfile.value.phone.ifEmpty { address.phone },
                customerEmail = userProfile.value.email,
                methodType = paymentMethod
            )
            when (val result = paymentGateway.processPayment(payload)) {
                is PaymentResult.Success -> {
                    if (paymentMethod == PaymentMethodType.WALLET) {
                        val deducted = repository.deductWallet(finalGrandTotal)
                        if (!deducted) {
                            _isProcessingOrder.value = false
                            _uiEvents.emit("Wallet deduction failed")
                            return@launch
                        }
                    }
                    val orderItems = summary.items.map { ci ->
                        OrderLineItem(
                            productId = ci.product.id,
                            productName = ci.product.name,
                            variantId = ci.variant.id,
                            variantLabel = ci.variant.label,
                            mrp = ci.variant.mrp,
                            unitPrice = ci.variant.sellingPrice,
                            quantity = ci.quantity,
                            imageKey = ci.product.imageKey,
                            colorHex = ci.product.colorHex
                        )
                    }
                    val order = repository.placeOrder(
                        address = address,
                        slot = slot,
                        paymentMethodLabel = paymentMethod.title,
                        paymentRef = result.transactionRef,
                        items = orderItems,
                        itemTotal = summary.itemTotal,
                        deliveryFee = finalDeliveryFee,
                        totalSavings = summary.itemSavings,
                        grandTotal = finalGrandTotal
                    )
                    _lastPlacedOrder.value = order
                    _isProcessingOrder.value = false
                    onOrderSuccess(order)
                }
                is PaymentResult.Failure -> {
                    _isProcessingOrder.value = false
                    _uiEvents.emit("Payment failed: ${result.reason}")
                }
            }
        }
    }

    fun clearLastPlacedOrder() {
        _lastPlacedOrder.value = null
    }

    // Account
    fun updateProfile(name: String, email: String, notificationsEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateProfile(name, email, notificationsEnabled)
            _uiEvents.emit("Profile preferences updated")
        }
    }

    fun topUpWallet(amount: Double) {
        viewModelScope.launch {
            repository.topUpWallet(amount)
            _uiEvents.emit("₹${amount.toInt()} added to QuickKart Wallet")
        }
    }

    // Admin CRUD
    fun adminSaveProduct(product: Product) {
        viewModelScope.launch {
            repository.upsertProduct(product)
            _uiEvents.emit("Saved product '${product.name}'")
        }
    }

    fun adminDeleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            _uiEvents.emit("Product deleted")
        }
    }

    fun adminSaveCategory(category: Category) {
        viewModelScope.launch {
            repository.upsertCategory(category)
            _uiEvents.emit("Saved category '${category.name}'")
        }
    }

    fun adminDeleteCategory(categoryId: String) {
        viewModelScope.launch {
            repository.deleteCategory(categoryId)
            _uiEvents.emit("Category deleted")
        }
    }

    fun adminSaveBrand(brand: Brand) {
        viewModelScope.launch {
            repository.upsertBrand(brand)
            _uiEvents.emit("Saved brand '${brand.name}'")
        }
    }

    fun adminDeleteBrand(brandId: String) {
        viewModelScope.launch {
            repository.deleteBrand(brandId)
            _uiEvents.emit("Brand deleted")
        }
    }

    fun adminUpdateOrderStatus(orderId: String, status: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            _uiEvents.emit("Order $orderId updated to ${status.displayName}")
        }
    }

    fun adminSaveStoreConfig(config: StoreConfig) {
        viewModelScope.launch {
            repository.updateStoreConfig(config)
            _uiEvents.emit("Delivery settings saved")
        }
    }

    fun adminSaveDeliverySlot(slot: DeliverySlot) {
        viewModelScope.launch {
            repository.upsertDeliverySlot(slot)
            _uiEvents.emit("Saved delivery slot '${slot.label}'")
        }
    }

    fun adminDeleteDeliverySlot(slotId: String) {
        viewModelScope.launch {
            repository.deleteDeliverySlot(slotId)
            _uiEvents.emit("Delivery slot removed")
        }
    }
}

class QuickKartViewModelFactory(
    private val repository: QuickCommerceRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuickKartViewModel::class.java)) {
            return QuickKartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

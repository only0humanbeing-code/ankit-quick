# Bundle 02: State, Models, Utilities & Repository

---

## File: `app/src/main/java/com/example/model/Models.kt`
```kotlin
package com.example.model

import com.squareup.moshi.JsonClass
import kotlin.math.roundToInt

@JsonClass(generateAdapter = true)
data class Category(
    val id: String,
    val name: String,
    val subtitle: String,
    val iconKey: String,
    val colorHex: Long,
    val sortOrder: Int = 0
)

@JsonClass(generateAdapter = true)
data class Brand(
    val id: String,
    val name: String,
    val tagline: String,
    val badgeText: String,
    val colorHex: Long,
    val logoUrl: String = ""
)

@JsonClass(generateAdapter = true)
data class ProductVariant(
    val id: String,
    val label: String,
    val mrp: Double,
    val sellingPrice: Double,
    val marginPercent: Double,
    val stock: Int
) {
    val discountPercent: Int
        get() = if (mrp > 0.0 && mrp > sellingPrice) {
            (((mrp - sellingPrice) / mrp) * 100.0).roundToInt()
        } else 0

    val savingsAmount: Double
        get() = (mrp - sellingPrice).coerceAtLeast(0.0)

    val isInStock: Boolean
        get() = stock > 0
}

@JsonClass(generateAdapter = true)
data class ProductReview(
    val id: String,
    val authorName: String,
    val rating: Double,
    val comment: String,
    val dateLabel: String,
    val isVerified: Boolean = true
)

@JsonClass(generateAdapter = true)
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val brandId: String,
    val subBrand: String = "",
    val shopCategory: String = "",
    val subCategory: String = "",
    val isVeg: Boolean = true,
    val imageKey: String,
    val imageUrl: String = "",
    val colorHex: Long,
    val rating: Double,
    val reviewCount: Int,
    val isFeatured: Boolean,
    val variants: List<ProductVariant>,
    val reviews: List<ProductReview> = emptyList()
) {
    val defaultVariant: ProductVariant
        get() = variants.firstOrNull { it.isInStock } ?: variants.first()

    val anyVariantInStock: Boolean
        get() = variants.any { it.isInStock }
}

data class CartItem(
    val productId: String,
    val variantId: String,
    val quantity: Int
)

data class CartItemDetailed(
    val product: Product,
    val variant: ProductVariant,
    val quantity: Int
) {
    val lineMrpTotal: Double get() = variant.mrp * quantity
    val lineSellingTotal: Double get() = variant.sellingPrice * quantity
    val lineSavings: Double get() = (variant.mrp - variant.sellingPrice).coerceAtLeast(0.0) * quantity
}

data class CartSummary(
    val items: List<CartItemDetailed>,
    val totalCount: Int,
    val mrpTotal: Double,
    val itemTotal: Double,
    val itemSavings: Double,
    val deliveryFee: Double,
    val freeDeliveryThreshold: Double,
    val grandTotal: Double
) {
    val amountForFreeDelivery: Double
        get() = (freeDeliveryThreshold - itemTotal).coerceAtLeast(0.0)
}

@JsonClass(generateAdapter = true)
data class SavedAddress(
    val id: String,
    val label: String,
    val recipientName: String,
    val phone: String,
    val houseFlat: String,
    val streetArea: String,
    val landmark: String,
    val city: String,
    val pincode: String,
    val isDefault: Boolean = false
) {
    val shortSummary: String
        get() = "$houseFlat, $streetArea, $city - $pincode"
}

@JsonClass(generateAdapter = true)
data class DeliverySlot(
    val id: String,
    val label: String,
    val timeRange: String,
    val fee: Double,
    val isExpress: Boolean,
    val isEnabled: Boolean = true
)

enum class OrderStatus(val displayName: String) {
    PLACED("Placed"),
    PACKING("Packing"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

@JsonClass(generateAdapter = true)
data class OrderLineItem(
    val productId: String,
    val productName: String,
    val variantId: String,
    val variantLabel: String,
    val mrp: Double,
    val unitPrice: Double,
    val quantity: Int,
    val imageKey: String,
    val colorHex: Long
)

@JsonClass(generateAdapter = true)
data class OrderRecord(
    val id: String,
    val createdAtMillis: Long,
    val status: OrderStatus,
    val addressSnapshot: String,
    val deliverySlotLabel: String,
    val paymentMethodLabel: String,
    val paymentRef: String,
    val items: List<OrderLineItem>,
    val itemTotal: Double,
    val deliveryFee: Double,
    val totalSavings: Double,
    val grandTotal: Double
)

data class StoreConfig(
    val baseDeliveryFee: Double = 25.0,
    val freeDeliveryThreshold: Double = 199.0,
    val etaMinutes: Int = 10,
    val promoBannerText: String = "Free delivery above ₹199"
)

data class UserProfile(
    val phone: String = "",
    val name: String = "Aarav Sharma",
    val email: String = "aarav.sharma@example.in",
    val walletBalance: Double = 450.0,
    val notificationsEnabled: Boolean = true,
    val isLoggedIn: Boolean = false
)

enum class SortOption(val label: String) {
    RELEVANCE("Relevance"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    SAVINGS_HIGH("Highest Savings (₹)"),
    DISCOUNT_HIGH("Discount: High to Low")
}

data class ProductFilterState(
    val query: String = "",
    val categoryId: String? = null,
    val brandId: String? = null,
    val subBrand: String? = null,
    val shopCategory: String? = null,
    val subCategory: String? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minDiscountPercent: Int = 0,
    val minRating: Double = 0.0,
    val selectedBrands: Set<String> = emptySet(),
    val selectedPackSizes: Set<String> = emptySet(),
    val sortOption: SortOption = SortOption.RELEVANCE,
    val inStockOnly: Boolean = false
) {
    val activeFilterCount: Int
        get() {
            var count = 0
            if (minPrice != null || maxPrice != null) count++
            if (minDiscountPercent > 0) count++
            if (minRating > 0.0) count++
            if (selectedBrands.isNotEmpty()) count++
            if (selectedPackSizes.isNotEmpty()) count++
            if (inStockOnly) count++
            return count
        }
}
```

---

## File: `app/src/main/java/com/example/util/FormatUtils.kt`
```kotlin
package com.example.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object FormatUtils {
    private val inrFormat = NumberFormat.getNumberInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    fun inr(amount: Double): String {
        return "₹${inrFormat.format(amount.roundToInt())}"
    }

    fun inrExact(amount: Double): String {
        val rounded = (amount * 100.0).roundToInt() / 100.0
        return if (rounded % 1.0 == 0.0) {
            "₹${rounded.toInt()}"
        } else {
            "₹%.2f".format(Locale("en", "IN"), rounded)
        }
    }

    fun unitRateLabel(sellingPrice: Double, packLabel: String): String? {
        if (sellingPrice <= 0.0 || packLabel.isBlank()) return null
        val match = Regex("""(\d+(?:\.\d+)?)\s*(kg|g|ml|l)\b""", RegexOption.IGNORE_CASE)
            .find(packLabel) ?: return null
        val qty = match.groupValues[1].toDoubleOrNull() ?: return null
        if (qty <= 0.0) return null
        val unit = match.groupValues[2].lowercase(Locale.ROOT)
        val packMultiplier = if (!packLabel.contains('(')) {
            Regex("""pack\s*of\s*(\d+)""", RegexOption.IGNORE_CASE)
                .find(packLabel.substring(match.range.last + 1))
                ?.groupValues?.getOrNull(1)?.toIntOrNull()?.coerceAtLeast(1) ?: 1
        } else {
            1
        }
        val (baseAmount, baseUnit) = when (unit) {
            "kg" -> (qty * packMultiplier) to "kg"
            "g" -> ((qty * packMultiplier) / 1000.0) to "kg"
            "l" -> (qty * packMultiplier) to "L"
            "ml" -> ((qty * packMultiplier) / 1000.0) to "L"
            else -> return null
        }
        if (baseAmount <= 0.0) return null
        val unitPrice = sellingPrice / baseAmount
        return "(${inrExact(unitPrice)}/$baseUnit)"
    }

    fun formatDateTime(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("en", "IN"))
        return sdf.format(Date(millis))
    }
}
```

---

## File: `app/src/main/java/com/example/data/repository/PaymentGateway.kt`
```kotlin
package com.example.data.repository

import kotlinx.coroutines.delay
import java.util.UUID

enum class PaymentMethodType(
    val id: String,
    val title: String,
    val subtitle: String,
    val supportsRazorpay: Boolean
) {
    UPI("upi", "UPI (GPay, PhonePe, Paytm)", "Instant zero-fee UPI payment", true),
    CARD("card", "Credit / Debit Card", "Visa, Mastercard, RuPay", true),
    WALLET("wallet", "QuickKart Wallet", "Fast 1-tap wallet checkout", false),
    COD("cod", "Cash / UPI on Delivery", "Pay when order arrives", false)
}

data class RazorpayOrderPayload(
    val merchantName: String = "QuickKart India Pvt Ltd",
    val currency: String = "INR",
    val amountPaise: Long,
    val orderReceipt: String,
    val customerPhone: String,
    val customerEmail: String,
    val methodType: PaymentMethodType
)

sealed interface PaymentResult {
    data class Success(val transactionRef: String, val gatewayName: String) : PaymentResult
    data class Failure(val reason: String) : PaymentResult
}

interface PaymentGateway {
    suspend fun processPayment(payload: RazorpayOrderPayload): PaymentResult
}

class DemoPaymentGateway : PaymentGateway {
    override suspend fun processPayment(payload: RazorpayOrderPayload): PaymentResult {
        delay(450)
        val prefix = when (payload.methodType) {
            PaymentMethodType.UPI -> "RZP_DEMO_UPI"
            PaymentMethodType.CARD -> "RZP_DEMO_CARD"
            PaymentMethodType.WALLET -> "QK_WALLET"
            PaymentMethodType.COD -> "COD_ORDER"
        }
        val ref = "${prefix}_${UUID.randomUUID().toString().take(8).uppercase()}"
        return PaymentResult.Success(
            transactionRef = ref,
            gatewayName = if (payload.methodType.supportsRazorpay) "Razorpay Demo" else payload.methodType.title
        )
    }
}
```

---

## File: `app/src/main/java/com/example/data/repository/QuickCommerceRepository.kt`
```kotlin
package com.example.data.repository

import com.example.data.local.BrandDao
import com.example.data.local.CartDao
import com.example.data.local.CategoryDao
import com.example.data.local.NotifySubscriptionEntity
import com.example.data.local.ProductDao
import com.example.data.local.QuickKartDatabase
import com.example.data.local.SavedForLaterEntity
import com.example.data.local.SeedCatalogData
import com.example.data.local.StoreOpsDao
import com.example.data.local.toDomain
import com.example.data.local.toEntity
import com.example.model.Brand
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.DeliverySlot
import com.example.model.OrderLineItem
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.SavedAddress
import com.example.model.StoreConfig
import com.example.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.random.Random

interface QuickCommerceRepository {
    val categories: Flow<List<Category>>
    val brands: Flow<List<Brand>>
    val products: Flow<List<Product>>
    val cartItems: Flow<List<CartItem>>
    val addresses: Flow<List<SavedAddress>>
    val orders: Flow<List<OrderRecord>>
    val deliverySlots: Flow<List<DeliverySlot>>
    val storeConfig: Flow<StoreConfig>
    val userProfile: Flow<UserProfile>
    val notifyProductIds: Flow<Set<String>>
    val savedProductIds: Flow<Set<String>>

    suspend fun ensureSeeded()

    suspend fun loginWithPhone(phone: String)
    suspend fun logout()
    suspend fun updateProfile(name: String, email: String, notificationsEnabled: Boolean)
    suspend fun topUpWallet(amount: Double)
    suspend fun deductWallet(amount: Double): Boolean

    suspend fun setCartQuantity(productId: String, variantId: String, quantity: Int)
    suspend fun clearCart()

    suspend fun toggleNotifyProduct(productId: String): Boolean
    suspend fun toggleSaveForLater(productId: String): Boolean

    suspend fun saveAddress(address: SavedAddress)
    suspend fun setDefaultAddress(addressId: String)
    suspend fun deleteAddress(addressId: String)

    suspend fun placeOrder(
        address: SavedAddress,
        slot: DeliverySlot,
        paymentMethodLabel: String,
        paymentRef: String,
        items: List<OrderLineItem>,
        itemTotal: Double,
        deliveryFee: Double,
        totalSavings: Double,
        grandTotal: Double
    ): OrderRecord

    suspend fun upsertProduct(product: Product)
    suspend fun deleteProduct(productId: String)
    suspend fun upsertCategory(category: Category)
    suspend fun deleteCategory(categoryId: String)
    suspend fun upsertBrand(brand: Brand)
    suspend fun deleteBrand(brandId: String)
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)
    suspend fun updateStoreConfig(config: StoreConfig)
    suspend fun upsertDeliverySlot(slot: DeliverySlot)
    suspend fun deleteDeliverySlot(slotId: String)
}

class RoomQuickCommerceRepository(
    private val categoryDao: CategoryDao,
    private val brandDao: BrandDao,
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val storeOpsDao: StoreOpsDao
) : QuickCommerceRepository {

    constructor(database: QuickKartDatabase) : this(
        categoryDao = database.categoryDao(),
        brandDao = database.brandDao(),
        productDao = database.productDao(),
        cartDao = database.cartDao(),
        storeOpsDao = database.storeOpsDao()
    )

    override val categories: Flow<List<Category>> =
        categoryDao.observeAll().map { list -> list.map { it.toDomain() } }

    override val brands: Flow<List<Brand>> =
        brandDao.observeAll().map { list -> list.map { it.toDomain() } }

    override val products: Flow<List<Product>> =
        productDao.observeAll().map { list -> list.map { it.toDomain() } }

    override val cartItems: Flow<List<CartItem>> =
        cartDao.observeCartItems().map { list -> list.map { it.toDomain() } }

    override val addresses: Flow<List<SavedAddress>> =
        storeOpsDao.observeAddresses().map { list -> list.map { it.toDomain() } }

    override val orders: Flow<List<OrderRecord>> =
        storeOpsDao.observeOrders().map { list -> list.map { it.toDomain() } }

    override val deliverySlots: Flow<List<DeliverySlot>> =
        storeOpsDao.observeDeliverySlots().map { list -> list.map { it.toDomain() } }

    override val storeConfig: Flow<StoreConfig> =
        storeOpsDao.observeStoreConfig().map { it?.toDomain() ?: SeedCatalogData.defaultStoreConfig }

    override val userProfile: Flow<UserProfile> =
        storeOpsDao.observeUserProfile().map { it?.toDomain() ?: SeedCatalogData.defaultUserProfile }

    override val notifyProductIds: Flow<Set<String>> =
        storeOpsDao.observeNotifyProductIds().map { it.toSet() }

    override val savedProductIds: Flow<Set<String>> =
        storeOpsDao.observeSavedProductIds().map { it.toSet() }

    override suspend fun ensureSeeded() {
        if (categoryDao.count() == 0) {
            categoryDao.insertAll(SeedCatalogData.categories.map { it.toEntity() })
            brandDao.insertAll(SeedCatalogData.brands.map { it.toEntity() })
            productDao.insertAll(SeedCatalogData.products.map { it.toEntity() })
            storeOpsDao.insertAddresses(SeedCatalogData.defaultAddresses.map { it.toEntity() })
            storeOpsDao.insertDeliverySlots(SeedCatalogData.defaultDeliverySlots.map { it.toEntity() })
            storeOpsDao.upsertStoreConfig(SeedCatalogData.defaultStoreConfig.toEntity())
            storeOpsDao.upsertUserProfile(SeedCatalogData.defaultUserProfile.toEntity())
            storeOpsDao.insertOrders(SeedCatalogData.sampleOrders.map { it.toEntity() })
        } else {
            val existingIds = productDao.observeAll().first().map { it.id }.toSet()
            val missingProducts = SeedCatalogData.products.filter { it.id !in existingIds }
            if (missingProducts.isNotEmpty()) {
                productDao.insertAll(missingProducts.map { it.toEntity() })
            }
        }
    }

    override suspend fun loginWithPhone(phone: String) {
        val current = storeOpsDao.observeUserProfile().first()?.toDomain() ?: SeedCatalogData.defaultUserProfile
        storeOpsDao.upsertUserProfile(current.copy(phone = phone, isLoggedIn = true).toEntity())
    }

    override suspend fun logout() {
        val current = storeOpsDao.observeUserProfile().first()?.toDomain() ?: SeedCatalogData.defaultUserProfile
        storeOpsDao.upsertUserProfile(current.copy(isLoggedIn = false).toEntity())
    }

    override suspend fun updateProfile(name: String, email: String, notificationsEnabled: Boolean) {
        val current = storeOpsDao.observeUserProfile().first()?.toDomain() ?: SeedCatalogData.defaultUserProfile
        storeOpsDao.upsertUserProfile(
            current.copy(
                name = name.trim().ifEmpty { current.name },
                email = email.trim().ifEmpty { current.email },
                notificationsEnabled = notificationsEnabled
            ).toEntity()
        )
    }

    override suspend fun topUpWallet(amount: Double) {
        if (amount <= 0.0) return
        val current = storeOpsDao.observeUserProfile().first()?.toDomain() ?: SeedCatalogData.defaultUserProfile
        storeOpsDao.upsertUserProfile(current.copy(walletBalance = current.walletBalance + amount).toEntity())
    }

    override suspend fun deductWallet(amount: Double): Boolean {
        val current = storeOpsDao.observeUserProfile().first()?.toDomain() ?: SeedCatalogData.defaultUserProfile
        if (current.walletBalance < amount) return false
        storeOpsDao.upsertUserProfile(current.copy(walletBalance = current.walletBalance - amount).toEntity())
        return true
    }

    override suspend fun setCartQuantity(productId: String, variantId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteItem(productId, variantId)
        } else {
            val existing = cartDao.getItem(productId, variantId)
            if (existing != null) {
                cartDao.updateQuantity(productId, variantId, quantity)
            } else {
                cartDao.upsert(CartItem(productId, variantId, quantity).toEntity())
            }
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override suspend fun toggleNotifyProduct(productId: String): Boolean {
        val current = storeOpsDao.observeNotifyProductIds().first().toSet()
        return if (productId in current) {
            storeOpsDao.removeNotifySubscription(productId)
            false
        } else {
            storeOpsDao.addNotifySubscription(NotifySubscriptionEntity(productId))
            true
        }
    }

    override suspend fun toggleSaveForLater(productId: String): Boolean {
        val current = storeOpsDao.observeSavedProductIds().first().toSet()
        return if (productId in current) {
            storeOpsDao.removeSavedForLater(productId)
            false
        } else {
            storeOpsDao.addSavedForLater(SavedForLaterEntity(productId))
            true
        }
    }

    override suspend fun saveAddress(address: SavedAddress) {
        if (address.isDefault) {
            storeOpsDao.clearDefaultAddresses()
        }
        storeOpsDao.upsertAddress(address.toEntity())
    }

    override suspend fun setDefaultAddress(addressId: String) {
        storeOpsDao.clearDefaultAddresses()
        storeOpsDao.setDefaultAddress(addressId)
    }

    override suspend fun deleteAddress(addressId: String) {
        storeOpsDao.deleteAddress(addressId)
        val remaining = storeOpsDao.observeAddresses().first()
        if (remaining.isNotEmpty() && remaining.none { it.isDefault }) {
            storeOpsDao.setDefaultAddress(remaining.first().id)
        }
    }

    override suspend fun placeOrder(
        address: SavedAddress,
        slot: DeliverySlot,
        paymentMethodLabel: String,
        paymentRef: String,
        items: List<OrderLineItem>,
        itemTotal: Double,
        deliveryFee: Double,
        totalSavings: Double,
        grandTotal: Double
    ): OrderRecord {
        val orderId = "QK-${Random.nextInt(10000, 99999)}"
        val record = OrderRecord(
            id = orderId,
            createdAtMillis = System.currentTimeMillis(),
            status = OrderStatus.PLACED,
            addressSnapshot = "${address.label}: ${address.shortSummary}",
            deliverySlotLabel = "${slot.label} (${slot.timeRange})",
            paymentMethodLabel = paymentMethodLabel,
            paymentRef = paymentRef,
            items = items,
            itemTotal = itemTotal,
            deliveryFee = deliveryFee,
            totalSavings = totalSavings,
            grandTotal = grandTotal
        )
        storeOpsDao.upsertOrder(record.toEntity())

        val currentProducts = productDao.observeAll().first().map { it.toDomain() }
        val productMap = currentProducts.associateBy { it.id }
        items.groupBy { it.productId }.forEach { (pid, orderedLines) ->
            val prod = productMap[pid] ?: return@forEach
            val updatedVariants = prod.variants.map { variant ->
                val qtyOrdered = orderedLines.filter { it.variantId == variant.id }.sumOf { it.quantity }
                if (qtyOrdered > 0) {
                    variant.copy(stock = (variant.stock - qtyOrdered).coerceAtLeast(0))
                } else variant
            }
            productDao.upsert(prod.copy(variants = updatedVariants).toEntity())
        }

        cartDao.clearCart()
        return record
    }

    override suspend fun upsertProduct(product: Product) {
        productDao.upsert(product.toEntity())
    }

    override suspend fun deleteProduct(productId: String) {
        productDao.deleteById(productId)
    }

    override suspend fun upsertCategory(category: Category) {
        categoryDao.upsert(category.toEntity())
    }

    override suspend fun deleteCategory(categoryId: String) {
        categoryDao.deleteById(categoryId)
    }

    override suspend fun upsertBrand(brand: Brand) {
        brandDao.upsert(brand.toEntity())
    }

    override suspend fun deleteBrand(brandId: String) {
        brandDao.deleteById(brandId)
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        val existing = storeOpsDao.observeOrders().first().firstOrNull { it.id == orderId }?.toDomain() ?: return
        storeOpsDao.upsertOrder(existing.copy(status = status).toEntity())
    }

    override suspend fun updateStoreConfig(config: StoreConfig) {
        storeOpsDao.upsertStoreConfig(config.toEntity())
    }

    override suspend fun upsertDeliverySlot(slot: DeliverySlot) {
        storeOpsDao.upsertDeliverySlot(slot.toEntity())
    }

    override suspend fun deleteDeliverySlot(slotId: String) {
        storeOpsDao.deleteDeliverySlot(slotId)
    }
}
```

---

## File: `app/src/main/java/com/example/viewmodel/QuickKartViewModel.kt`
```kotlin
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
```

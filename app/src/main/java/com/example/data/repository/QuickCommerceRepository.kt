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

    // Auth & Profile
    suspend fun loginWithPhone(phone: String)
    suspend fun logout()
    suspend fun updateProfile(name: String, email: String, notificationsEnabled: Boolean)
    suspend fun topUpWallet(amount: Double)
    suspend fun deductWallet(amount: Double): Boolean

    // Cart
    suspend fun setCartQuantity(productId: String, variantId: String, quantity: Int)
    suspend fun clearCart()

    // Notify Me & Save For Later
    suspend fun toggleNotifyProduct(productId: String): Boolean
    suspend fun toggleSaveForLater(productId: String): Boolean

    // Addresses
    suspend fun saveAddress(address: SavedAddress)
    suspend fun setDefaultAddress(addressId: String)
    suspend fun deleteAddress(addressId: String)

    // Orders
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

    // Admin Catalog & Store Management
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

        // Deduct variant stock cleanly
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

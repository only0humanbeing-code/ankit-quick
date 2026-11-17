# Bundle 03: Room Database, Entities, DAOs & Category Taxonomy

---

## File: `app/src/main/java/com/example/data/local/QuickKartDatabase.kt`
```kotlin
package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        CategoryEntity::class,
        BrandEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        AddressEntity::class,
        OrderEntity::class,
        DeliverySlotEntity::class,
        StoreConfigEntity::class,
        UserProfileEntity::class,
        NotifySubscriptionEntity::class,
        SavedForLaterEntity::class
    ],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuickKartDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun brandDao(): BrandDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun storeOpsDao(): StoreOpsDao

    companion object {
        @Volatile
        private var INSTANCE: QuickKartDatabase? = null

        fun getInstance(context: Context): QuickKartDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuickKartDatabase::class.java,
                    "quickkart_local.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
```

---

## File: `app/src/main/java/com/example/data/local/Converters.kt`
```kotlin
package com.example.data.local

import androidx.room.TypeConverter
import com.example.model.OrderLineItem
import com.example.model.OrderStatus
import com.example.model.ProductReview
import com.example.model.ProductVariant
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val variantListType = Types.newParameterizedType(List::class.java, ProductVariant::class.java)
    private val variantAdapter = moshi.adapter<List<ProductVariant>>(variantListType)

    private val reviewListType = Types.newParameterizedType(List::class.java, ProductReview::class.java)
    private val reviewAdapter = moshi.adapter<List<ProductReview>>(reviewListType)

    private val orderItemListType = Types.newParameterizedType(List::class.java, OrderLineItem::class.java)
    private val orderItemAdapter = moshi.adapter<List<OrderLineItem>>(orderItemListType)

    @TypeConverter
    fun fromVariantList(value: List<ProductVariant>): String = variantAdapter.toJson(value)

    @TypeConverter
    fun toVariantList(json: String): List<ProductVariant> = variantAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromReviewList(value: List<ProductReview>): String = reviewAdapter.toJson(value)

    @TypeConverter
    fun toReviewList(json: String): List<ProductReview> = reviewAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromOrderItemList(value: List<OrderLineItem>): String = orderItemAdapter.toJson(value)

    @TypeConverter
    fun toOrderItemList(json: String): List<OrderLineItem> = orderItemAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toOrderStatus(name: String): OrderStatus =
        runCatching { OrderStatus.valueOf(name) }.getOrDefault(OrderStatus.PLACED)
}
```

---

## File: `app/src/main/java/com/example/data/local/Entities.kt`
```kotlin
package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.model.Brand
import com.example.model.DeliverySlot
import com.example.model.OrderLineItem
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.SavedAddress
import com.example.model.StoreConfig
import com.example.model.UserProfile

@Entity(
    tableName = "brands",
    indices = [Index(value = ["name"])]
)
data class BrandEntity(
    @PrimaryKey val id: String,
    val name: String,
    val tagline: String,
    val badgeText: String,
    val colorHex: Long,
    val logoUrl: String = ""
)

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey val id: String,
    val label: String,
    val recipientName: String,
    val phone: String,
    val houseFlat: String,
    val streetArea: String,
    val landmark: String,
    val city: String,
    val pincode: String,
    val isDefault: Boolean
)

@Entity(
    tableName = "orders",
    indices = [Index(value = ["createdAtMillis"])]
)
data class OrderEntity(
    @PrimaryKey val id: String,
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

@Entity(tableName = "delivery_slots")
data class DeliverySlotEntity(
    @PrimaryKey val id: String,
    val label: String,
    val timeRange: String,
    val fee: Double,
    val isExpress: Boolean,
    val isEnabled: Boolean
)

@Entity(tableName = "store_config")
data class StoreConfigEntity(
    @PrimaryKey val id: Int = 1,
    val baseDeliveryFee: Double,
    val freeDeliveryThreshold: Double,
    val etaMinutes: Int,
    val promoBannerText: String
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val phone: String,
    val name: String,
    val email: String,
    val walletBalance: Double,
    val notificationsEnabled: Boolean,
    val isLoggedIn: Boolean
)

@Entity(tableName = "notify_subscriptions")
data class NotifySubscriptionEntity(
    @PrimaryKey val productId: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_for_later")
data class SavedForLaterEntity(
    @PrimaryKey val productId: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)

fun BrandEntity.toDomain() = Brand(id, name, tagline, badgeText, colorHex, logoUrl)
fun Brand.toEntity() = BrandEntity(id, name, tagline, badgeText, colorHex, logoUrl)

fun AddressEntity.toDomain() = SavedAddress(
    id, label, recipientName, phone, houseFlat, streetArea, landmark, city, pincode, isDefault
)
fun SavedAddress.toEntity() = AddressEntity(
    id, label, recipientName, phone, houseFlat, streetArea, landmark, city, pincode, isDefault
)

fun OrderEntity.toDomain() = OrderRecord(
    id, createdAtMillis, status, addressSnapshot, deliverySlotLabel,
    paymentMethodLabel, paymentRef, items, itemTotal, deliveryFee, totalSavings, grandTotal
)
fun OrderRecord.toEntity() = OrderEntity(
    id, createdAtMillis, status, addressSnapshot, deliverySlotLabel,
    paymentMethodLabel, paymentRef, items, itemTotal, deliveryFee, totalSavings, grandTotal
)

fun DeliverySlotEntity.toDomain() = DeliverySlot(id, label, timeRange, fee, isExpress, isEnabled)
fun DeliverySlot.toEntity() = DeliverySlotEntity(id, label, timeRange, fee, isExpress, isEnabled)

fun StoreConfigEntity.toDomain() = StoreConfig(baseDeliveryFee, freeDeliveryThreshold, etaMinutes, promoBannerText)
fun StoreConfig.toEntity() = StoreConfigEntity(1, baseDeliveryFee, freeDeliveryThreshold, etaMinutes, promoBannerText)

fun UserProfileEntity.toDomain() = UserProfile(phone, name, email, walletBalance, notificationsEnabled, isLoggedIn)
fun UserProfile.toEntity() = UserProfileEntity(1, phone, name, email, walletBalance, notificationsEnabled, isLoggedIn)
```

---

## File: `app/src/main/java/com/example/data/local/ProductEntity.kt`
```kotlin
package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.model.Product
import com.example.model.ProductReview
import com.example.model.ProductVariant

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brand_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["brand_id"]),
        Index(value = ["sub_brand"]),
        Index(value = ["shop_category", "sub_category"]),
        Index(value = ["is_featured", "name"])
    ]
)
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "category_id")
    val categoryId: String,

    @ColumnInfo(name = "brand_id")
    val brandId: String,

    @ColumnInfo(name = "sub_brand")
    val subBrand: String = "",

    @ColumnInfo(name = "shop_category")
    val shopCategory: String = "",

    @ColumnInfo(name = "sub_category")
    val subCategory: String = "",

    @ColumnInfo(name = "is_veg")
    val isVeg: Boolean = true,

    @ColumnInfo(name = "image_key")
    val imageKey: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    @ColumnInfo(name = "color_hex")
    val colorHex: Long,

    @ColumnInfo(name = "rating")
    val rating: Double,

    @ColumnInfo(name = "review_count")
    val reviewCount: Int,

    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean,

    @ColumnInfo(name = "variants_json")
    val variants: List<ProductVariant>,

    @ColumnInfo(name = "reviews_json")
    val reviews: List<ProductReview>
)

data class ProductWithDetails(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity?,
    @Relation(
        parentColumn = "brand_id",
        entityColumn = "id"
    )
    val brand: BrandEntity?
)

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    description = description,
    categoryId = categoryId,
    brandId = brandId,
    subBrand = subBrand,
    shopCategory = shopCategory,
    subCategory = subCategory,
    isVeg = isVeg,
    imageKey = imageKey,
    imageUrl = imageUrl,
    colorHex = colorHex,
    rating = rating,
    reviewCount = reviewCount,
    isFeatured = isFeatured,
    variants = variants,
    reviews = reviews
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    categoryId = categoryId,
    brandId = brandId,
    subBrand = subBrand,
    shopCategory = shopCategory,
    subCategory = subCategory,
    isVeg = isVeg,
    imageKey = imageKey,
    imageUrl = imageUrl,
    colorHex = colorHex,
    rating = rating,
    reviewCount = reviewCount,
    isFeatured = isFeatured,
    variants = variants,
    reviews = reviews
)
```

---

## File: `app/src/main/java/com/example/data/local/CategoryEntity.kt`
```kotlin
package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.model.Category

@Entity(
    tableName = "categories",
    indices = [
        Index(value = ["sort_order", "name"])
    ]
)
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @ColumnInfo(name = "icon_key")
    val iconKey: String,

    @ColumnInfo(name = "color_hex")
    val colorHex: Long,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int
)

data class CategoryWithProducts(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val products: List<ProductEntity>
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    subtitle = subtitle,
    iconKey = iconKey,
    colorHex = colorHex,
    sortOrder = sortOrder
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    subtitle = subtitle,
    iconKey = iconKey,
    colorHex = colorHex,
    sortOrder = sortOrder
)
```

---

## File: `app/src/main/java/com/example/data/local/CartItemEntity.kt`
```kotlin
package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Relation
import com.example.model.CartItem

@Entity(
    tableName = "cart_items",
    primaryKeys = ["product_id", "variant_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["product_id"]),
        Index(value = ["added_at_millis"])
    ]
)
data class CartItemEntity(
    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "variant_id")
    val variantId: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "added_at_millis")
    val addedAtMillis: Long = System.currentTimeMillis()
)

data class CartItemWithProduct(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: ProductEntity?
)

fun CartItemEntity.toDomain(): CartItem = CartItem(
    productId = productId,
    variantId = variantId,
    quantity = quantity
)

fun CartItem.toEntity(addedAtMillis: Long = System.currentTimeMillis()): CartItemEntity =
    CartItemEntity(
        productId = productId,
        variantId = variantId,
        quantity = quantity,
        addedAtMillis = addedAtMillis
    )
```

---

## File: `app/src/main/java/com/example/data/local/ProductDao.kt`
```kotlin
package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY is_featured DESC, name ASC")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category_id = :categoryId ORDER BY is_featured DESC, name ASC")
    fun observeByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE brand_id = :brandId ORDER BY is_featured DESC, name ASC")
    fun observeByBrand(brandId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE is_featured = 1 ORDER BY rating DESC, name ASC")
    fun observeFeatured(): Flow<List<ProductEntity>>

    @Query(
        "SELECT * FROM products " +
            "WHERE name LIKE '%' || :query || '%' " +
            "OR description LIKE '%' || :query || '%' " +
            "ORDER BY is_featured DESC, rating DESC"
    )
    fun search(query: String): Flow<List<ProductEntity>>

    @Transaction
    @Query("SELECT * FROM products ORDER BY is_featured DESC, name ASC")
    fun observeAllWithDetails(): Flow<List<ProductWithDetails>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: String)
}
```

---

## File: `app/src/main/java/com/example/data/local/CategoryDao.kt`
```kotlin
package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sort_order ASC, name ASC")
    fun observeAll(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM categories ORDER BY sort_order ASC, name ASC")
    fun observeAllWithProducts(): Flow<List<CategoryWithProducts>>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CategoryEntity?

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: String)
}
```

---

## File: `app/src/main/java/com/example/data/local/BrandDao.kt`
```kotlin
package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {
    @Query("SELECT * FROM brands ORDER BY rowid ASC")
    fun observeAll(): Flow<List<BrandEntity>>

    @Query("SELECT * FROM brands WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BrandEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BrandEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: BrandEntity)

    @Query("DELETE FROM brands WHERE id = :id")
    suspend fun deleteById(id: String)
}
```

---

## File: `app/src/main/java/com/example/data/local/CartDao.kt`
```kotlin
package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY added_at_millis ASC")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Transaction
    @Query("SELECT * FROM cart_items ORDER BY added_at_millis ASC")
    fun observeCartWithProducts(): Flow<List<CartItemWithProduct>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeTotalQuantity(): Flow<Int>

    @Query("SELECT * FROM cart_items WHERE product_id = :productId AND variant_id = :variantId LIMIT 1")
    suspend fun getItem(productId: String, variantId: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE product_id = :productId AND variant_id = :variantId")
    suspend fun updateQuantity(productId: String, variantId: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE product_id = :productId AND variant_id = :variantId")
    suspend fun deleteItem(productId: String, variantId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
```

---

## File: `app/src/main/java/com/example/data/local/StoreOpsDao.kt`
```kotlin
package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreOpsDao {
    @Query("SELECT * FROM addresses ORDER BY isDefault DESC, label ASC")
    fun observeAddresses(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddresses(items: List<AddressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAddress(item: AddressEntity)

    @Query("UPDATE addresses SET isDefault = 0")
    suspend fun clearDefaultAddresses()

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultAddress(id: String)

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun deleteAddress(id: String)

    @Query("SELECT * FROM orders ORDER BY createdAtMillis DESC")
    fun observeOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(items: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrder(item: OrderEntity)

    @Query("SELECT * FROM delivery_slots ORDER BY isExpress DESC, id ASC")
    fun observeDeliverySlots(): Flow<List<DeliverySlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliverySlots(items: List<DeliverySlotEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDeliverySlot(item: DeliverySlotEntity)

    @Query("DELETE FROM delivery_slots WHERE id = :id")
    suspend fun deleteDeliverySlot(id: String)

    @Query("SELECT * FROM store_config WHERE id = 1")
    fun observeStoreConfig(): Flow<StoreConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStoreConfig(config: StoreConfigEntity)

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(profile: UserProfileEntity)

    @Query("SELECT productId FROM notify_subscriptions")
    fun observeNotifyProductIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotifySubscription(item: NotifySubscriptionEntity)

    @Query("DELETE FROM notify_subscriptions WHERE productId = :productId")
    suspend fun removeNotifySubscription(productId: String)

    @Query("SELECT productId FROM saved_for_later ORDER BY createdAtMillis DESC")
    fun observeSavedProductIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSavedForLater(item: SavedForLaterEntity)

    @Query("DELETE FROM saved_for_later WHERE productId = :productId")
    suspend fun removeSavedForLater(productId: String)
}
```

---

## File: `app/src/main/java/com/example/data/local/ShopCategoryTaxonomy.kt`
```kotlin
package com.example.data.local

import com.example.model.Product

data class ShopCategoryGroup(
    val id: String,
    val title: String,
    val iconKey: String,
    val allOptionLabel: String,
    val subcategories: List<String>
)

object ShopCategoryTaxonomy {

    val groups: List<ShopCategoryGroup> = listOf(
        ShopCategoryGroup(
            id = "shop_electronics",
            title = "Electronics",
            iconKey = "electronics",
            allOptionLabel = "All Electronics",
            subcategories = listOf(
                "Audio devices",
                "Electrical Accessories",
                "Personal Care & Grooming",
                "Smart Wearables"
            )
        ),
        ShopCategoryGroup(
            id = "shop_fruits_veg",
            title = "Fruits & Vegetables",
            iconKey = "fruits_veg",
            allOptionLabel = "All Fruits & Vegetables",
            subcategories = listOf(
                "Cuts & Sprouts",
                "Exotic Fruits & Veggies",
                "Flower Bouquets, Bunches",
                "Fresh Fruits",
                "Fresh Vegetables",
                "Herbs & Seasonings",
                "Organic Fruits & Vegetables"
            )
        ),
        ShopCategoryGroup(
            id = "shop_foodgrains",
            title = "Foodgrains, Oil & Masala",
            iconKey = "foodgrains",
            allOptionLabel = "All Foodgrains, Oil & Masala",
            subcategories = listOf(
                "Atta, Flours & Sooji",
                "Dals & Pulses",
                "Dry Fruits",
                "Edible Oils & Ghee",
                "Masalas & Spices",
                "Organic Staples",
                "Rice & Rice Products",
                "Salt, Sugar & Jaggery"
            )
        ),
        ShopCategoryGroup(
            id = "shop_bakery_dairy",
            title = "Bakery, Cakes & Dairy",
            iconKey = "bakery_dairy",
            allOptionLabel = "All Bakery, Cakes & Dairy",
            subcategories = listOf(
                "Bakery Snacks",
                "Breads & Buns",
                "Cakes & Pastries",
                "Cookies, Rusk & Khari",
                "Dairy",
                "Non Dairy"
            )
        ),
        ShopCategoryGroup(
            id = "shop_beverages",
            title = "Beverages",
            iconKey = "beverages",
            allOptionLabel = "All Beverages",
            subcategories = listOf(
                "Coffee",
                "Energy & Soft Drinks",
                "Fruit Juices & Drinks",
                "Nutrition Mix, Supplement",
                "Tea",
                "Water"
            )
        ),
        ShopCategoryGroup(
            id = "shop_snacks_branded",
            title = "Snacks & Branded Foods",
            iconKey = "snacks_branded",
            allOptionLabel = "All Snacks & Branded Foods",
            subcategories = listOf(
                "Biscuits & Cookies",
                "Breakfast Cereals",
                "Chocolates & Candies",
                "Indian Mithai",
                "Noodle, Pasta, Vermicelli",
                "Pickles & Chutney",
                "Ready To Cook & Eat",
                "Snacks & Namkeen",
                "Spreads, Sauces, Ketchup"
            )
        ),
        ShopCategoryGroup(
            id = "shop_beauty_hygiene",
            title = "Beauty & Hygiene",
            iconKey = "beauty_hygiene",
            allOptionLabel = "All Beauty & Hygiene",
            subcategories = listOf(
                "Bath & Hand Wash",
                "Feminine Hygiene",
                "Fragrances & Deos",
                "Hair Care",
                "Health & Medicine",
                "Makeup",
                "Men's Grooming",
                "Oral Care",
                "Sexual Wellness",
                "Skin Care"
            )
        ),
        ShopCategoryGroup(
            id = "shop_cleaning",
            title = "Cleaning & Household",
            iconKey = "cleaning_household",
            allOptionLabel = "All Cleaning & Household",
            subcategories = listOf(
                "All Purpose Cleaners",
                "Bins & Bathroom Ware",
                "Car & Shoe Care",
                "Detergents & Dishwash",
                "Disposables, Garbage Bag",
                "Fresheners & Repellents",
                "Mops, Brushes & Scrubs",
                "Party & Festive Needs",
                "Pooja Needs",
                "Stationery"
            )
        ),
        ShopCategoryGroup(
            id = "shop_kitchen_pets",
            title = "Kitchen, Garden & Pets",
            iconKey = "kitchen_garden_pets",
            allOptionLabel = "All Kitchen, Garden & Pets",
            subcategories = listOf(
                "Gardening",
                "Steel Utensils",
                "Storage & Accessories"
            )
        ),
        ShopCategoryGroup(
            id = "shop_eggs_meat",
            title = "Eggs, Meat & Fish",
            iconKey = "eggs_meat_fish",
            allOptionLabel = "All Eggs, Meat & Fish",
            subcategories = listOf(
                "Eggs",
                "Mutton & Lamb",
                "Pork & Other Meats",
                "Poultry",
                "Sausages, Bacon & Salami"
            )
        ),
        ShopCategoryGroup(
            id = "shop_gourmet",
            title = "Gourmet & World Food",
            iconKey = "gourmet_world",
            allOptionLabel = "All Gourmet & World Food",
            subcategories = listOf(
                "Cereals & Breakfast",
                "Chocolates & Biscuits",
                "Drinks & Beverages",
                "Oils & Vinegar",
                "Sauces, Spreads & Dips",
                "Snacks, Dry Fruits, Nuts",
                "Tinned & Processed Food"
            )
        ),
        ShopCategoryGroup(
            id = "shop_baby_care",
            title = "Baby Care",
            iconKey = "baby_care",
            allOptionLabel = "All Baby Care",
            subcategories = listOf(
                "Baby Accessories",
                "Baby Bath & Hygiene",
                "Baby Food & Formula",
                "Diapers & Wipes"
            )
        )
    )

    private val leafFiltersBySubCategory: Map<String, List<String>> = mapOf(
        "Dals & Pulses" to listOf(
            "Cereals & Millets",
            "Toor, Channa & Moong Dal",
            "Urad & Other Dals"
        ),
        "Atta, Flours & Sooji" to listOf(
            "Chakki & Sharbati Atta",
            "Besan, Sooji & Maida",
            "Millet & Multigrain"
        ),
        "Edible Oils & Ghee" to listOf(
            "Sunflower & Mustard Oil",
            "Pure Cow Ghee"
        ),
        "Masalas & Spices" to listOf(
            "Powdered Spices",
            "Blended Masalas"
        ),
        "Salt, Sugar & Jaggery" to listOf(
            "Iodised & Rock Salt",
            "Jaggery & Sugar"
        ),
        "Fresh Vegetables" to listOf(
            "Potato, Onion & Tomato",
            "Greens & Beans"
        ),
        "Fresh Fruits" to listOf(
            "Daily Fruits",
            "Seasonal & Citrus"
        ),
        "Dairy" to listOf(
            "Milk",
            "Butter & Paneer"
        ),
        "Tea" to listOf(
            "Leaf & Dust Tea",
            "CTC & Assam Tea"
        ),
        "Biscuits & Cookies" to listOf(
            "Glucose & Marie",
            "Cream & Choco Cookies"
        ),
        "Snacks & Namkeen" to listOf(
            "Bhujia & Sev",
            "Chips & Nachos",
            "Oats & Healthy Crunch"
        ),
        "Detergents & Dishwash" to listOf(
            "Detergent Powders",
            "Liquid & Matic Wash"
        ),
        "Bath & Hand Wash" to listOf(
            "Bathing Soaps",
            "Hand & Body Wash"
        ),
        "Health & Medicine" to listOf(
            "Antiseptics & First Aid",
            "Immunity & Chyawanprash"
        )
    )

    fun leafFiltersFor(subCategory: String?): List<String> =
        if (subCategory.isNullOrBlank()) emptyList() else leafFiltersBySubCategory[subCategory].orEmpty()

    fun matchesLeafFilter(product: Product, leafFilter: String): Boolean {
        val text = "${product.name} ${product.description} ${product.subBrand}".lowercase()
        return when (leafFilter) {
            "Cereals & Millets" ->
                listOf("soya", "poha", "millet", "ragi", "jowar", "bajra", "daliya", "quinoa", "oats", "sabudana")
                    .any { it in text }
            "Toor, Channa & Moong Dal" ->
                listOf("toor", "arhar", "chana", "channa", "moong", "mung", "besan")
                    .any { it in text }
            "Urad & Other Dals" ->
                listOf("urad", "masoor", "rajma", "lobhiya", "lobia", "moth", "kulthi", "matar", "mixed dal", "panchratna", "kabuli")
                    .any { it in text }

            "Chakki & Sharbati Atta" -> "atta" in text || "sharbati" in text || "wheat" in text
            "Besan, Sooji & Maida" -> listOf("besan", "sooji", "suji", "rava", "maida").any { it in text }
            "Millet & Multigrain" -> listOf("multigrain", "millet", "ragi", "jowar", "bajra").any { it in text }

            "Sunflower & Mustard Oil" -> "oil" in text
            "Pure Cow Ghee" -> "ghee" in text

            "Powdered Spices" -> listOf("turmeric", "haldi", "chilli", "mirch", "coriander", "dhaniya", "cumin").any { it in text }
            "Blended Masalas" -> listOf("garam", "masala", "chole", "sambhar", "pav bhaji").any { it in text }

            "Iodised & Rock Salt" -> "salt" in text
            "Jaggery & Sugar" -> listOf("jaggery", "gud", "sugar").any { it in text }

            "Potato, Onion & Tomato" -> listOf("potato", "onion", "tomato").any { it in text }
            "Greens & Beans" -> listOf("bhindi", "okra", "palak", "spinach", "coriander", "beans", "peas").any { it in text }

            "Daily Fruits" -> listOf("banana", "apple", "pomegranate").any { it in text }
            "Seasonal & Citrus" -> listOf("mango", "orange", "citrus").any { it in text }

            "Milk" -> "milk" in text
            "Butter & Paneer" -> "butter" in text || "paneer" in text || "cheese" in text

            "Leaf & Dust Tea" -> "red label" in text || "leaf" in text || "green" in text
            "CTC & Assam Tea" -> "gold" in text || "assam" in text || "ctc" in text

            "Glucose & Marie" -> listOf("parle-g", "gluco", "nutrichoice", "marie").any { it in text }
            "Cream & Choco Cookies" -> listOf("oreo", "bourbon", "good day", "hide & seek", "dark fantasy", "choco").any { it in text }

            "Bhujia & Sev" -> listOf("bhujia", "khatta meetha", "kaju mixture", "namkeen", "moong dal").any { it in text }
            "Chips & Nachos" -> listOf("lay's", "lays", "kurkure", "chips", "nacho").any { it in text }
            "Oats & Healthy Crunch" -> "oats" in text || "quaker" in text || "crunch" in text

            "Detergent Powders" -> "powder" in text || "tide" in text || "ariel" in text || "ghadi" in text
            "Liquid & Matic Wash" -> listOf("vim", "gel", "liquid", "matic", "dishwash").any { it in text }

            "Bathing Soaps" -> "soap" in text || "lux" in text || "lifebuoy" in text
            "Hand & Body Wash" -> "hand" in text || "body" in text || "wash" in text

            "Antiseptics & First Aid" -> "dettol" in text || "antiseptic" in text
            "Immunity & Chyawanprash" -> "chyawanprash" in text || "immunity" in text

            else -> leafFilter.lowercase() in text
        }
    }

    fun findByTitle(title: String): ShopCategoryGroup? =
        groups.firstOrNull { it.title == title }
}
```

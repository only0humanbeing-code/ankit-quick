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

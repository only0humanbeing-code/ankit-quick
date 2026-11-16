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

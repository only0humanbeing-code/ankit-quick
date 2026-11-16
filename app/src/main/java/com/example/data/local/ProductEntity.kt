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

/**
 * Room relation joining ProductEntity with its parent CategoryEntity and BrandEntity.
 */
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

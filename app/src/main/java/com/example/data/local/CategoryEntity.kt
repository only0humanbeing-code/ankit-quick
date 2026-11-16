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

/**
 * One-to-many Room relation linking a CategoryEntity to all ProductEntity rows
 * referencing it via products.category_id.
 */
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

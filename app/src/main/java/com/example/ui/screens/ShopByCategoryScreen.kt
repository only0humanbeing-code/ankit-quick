package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Egg
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.local.ShopCategoryGroup
import com.example.data.local.ShopCategoryTaxonomy
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickMuted

@Composable
fun ShopByCategoryScreen(
    totalProductCount: Int,
    onBackClick: () -> Unit,
    onSearchSubmit: (String) -> Unit,
    onSelectCategoryAll: (ShopCategoryGroup) -> Unit,
    onSelectSubCategory: (ShopCategoryGroup, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expandedGroupId by rememberSaveable { mutableStateOf<String?>(null) }

    val filteredGroups = remember(searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) {
            ShopCategoryTaxonomy.groups
        } else {
            ShopCategoryTaxonomy.groups.filter { group ->
                group.title.lowercase().contains(q) ||
                    group.subcategories.any { it.lowercase().contains(q) }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Green Header matching the reference Shop By Category screenshots
        Surface(
            color = QuickGreen,
            contentColor = Color.White,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .testTag("shop_by_category_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Shop By Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // White search bar inside header
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(44.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search categories or products",
                            tint = QuickMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search $totalProductCount+ products",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = QuickMuted
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF111827)
                                ),
                                cursorBrush = SolidColor(QuickGreen),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        if (searchQuery.isNotBlank()) {
                                            onSearchSubmit(searchQuery.trim())
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("shop_by_category_search_input")
                            )
                        }
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Clear search",
                                    tint = QuickMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Expandable Category Accordion List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (searchQuery.isNotBlank()) {
                item(key = "search_products_shortcut") {
                    Surface(
                        color = Color(0xFFF3F8EE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSearchSubmit(searchQuery.trim()) }
                            .testTag("shop_by_category_search_submit")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Search all products for \"${searchQuery.trim()}\"",
                                style = MaterialTheme.typography.titleSmall,
                                color = QuickGreen,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "Search products",
                                tint = QuickGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)
                }
            }

            items(filteredGroups, key = { it.id }) { group ->
                val isExpanded = expandedGroupId == group.id ||
                    (searchQuery.isNotBlank() && group.subcategories.any {
                        it.contains(searchQuery.trim(), ignoreCase = true)
                    })

                ShopCategoryAccordionItem(
                    group = group,
                    isExpanded = isExpanded,
                    searchQuery = searchQuery.trim(),
                    onToggleExpand = {
                        expandedGroupId = if (expandedGroupId == group.id) null else group.id
                    },
                    onSelectAll = { onSelectCategoryAll(group) },
                    onSelectSubCategory = { sub -> onSelectSubCategory(group, sub) }
                )
            }
        }
    }
}

@Composable
private fun ShopCategoryAccordionItem(
    group: ShopCategoryGroup,
    isExpanded: Boolean,
    searchQuery: String,
    onToggleExpand: () -> Unit,
    onSelectAll: () -> Unit,
    onSelectSubCategory: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleExpand)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .testTag("shop_category_row_${group.id}"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = shopCategoryOutlineIcon(group.iconKey),
                contentDescription = group.title,
                tint = Color(0xFF374151),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = group.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isExpanded) FontWeight.Bold else FontWeight.Medium,
                color = Color(0xFF1F2937),
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFF9CA3AF)),
                modifier = Modifier.size(26.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse ${group.title}" else "Expand ${group.title}",
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // "All <Category>" row
                SubCategoryRow(
                    label = group.allOptionLabel,
                    isHighlighted = false,
                    onClick = onSelectAll,
                    modifier = Modifier.testTag("shop_subcat_${group.id}_all")
                )

                val visibleSubs = if (searchQuery.isBlank() || group.title.contains(searchQuery, ignoreCase = true)) {
                    group.subcategories
                } else {
                    group.subcategories.filter { it.contains(searchQuery, ignoreCase = true) }
                }

                visibleSubs.forEach { subName ->
                    SubCategoryRow(
                        label = subName,
                        isHighlighted = false,
                        onClick = { onSelectSubCategory(subName) },
                        modifier = Modifier.testTag("shop_subcat_${group.id}_${subName}")
                    )
                }
            }
        }
    }
}

@Composable
private fun SubCategoryRow(
    label: String,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(start = 26.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Normal,
                color = Color(0xFF374151)
            )
        }
        HorizontalDivider(
            color = Color(0xFFF3F4F6),
            thickness = 0.8.dp,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

private fun shopCategoryOutlineIcon(iconKey: String): ImageVector = when (iconKey) {
    "electronics" -> Icons.Outlined.Devices
    "fruits_veg" -> Icons.Outlined.Eco
    "foodgrains" -> Icons.Outlined.Inventory2
    "bakery_dairy" -> Icons.Outlined.BakeryDining
    "beverages" -> Icons.Outlined.LocalCafe
    "snacks_branded" -> Icons.Outlined.Fastfood
    "beauty_hygiene" -> Icons.Outlined.Spa
    "cleaning_household" -> Icons.Outlined.CleaningServices
    "kitchen_garden_pets" -> Icons.Outlined.Pets
    "eggs_meat_fish" -> Icons.Outlined.Egg
    "gourmet_world" -> Icons.Outlined.Restaurant
    "baby_care" -> Icons.Outlined.ChildCare
    else -> Icons.Outlined.Inventory2
}

package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.ProductFilterState
import com.example.model.SortOption
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer

private data class PriceBucket(
    val label: String,
    val min: Int?,
    val max: Int?
)

private val priceBuckets = listOf(
    PriceBucket("Any Price", null, null),
    PriceBucket("Under ₹50", null, 50),
    PriceBucket("₹50 – ₹150", 50, 150),
    PriceBucket("₹150 – ₹300", 150, 300),
    PriceBucket("Above ₹300", 300, null)
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSortBottomSheet(
    currentFilter: ProductFilterState,
    brands: List<Brand>,
    packSizes: List<String>,
    onApply: (ProductFilterState) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var draft by remember(currentFilter) { mutableStateOf(currentFilter) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters & Sort",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = {
                        draft = ProductFilterState(
                            query = draft.query,
                            categoryId = draft.categoryId,
                            brandId = draft.brandId,
                            subBrand = draft.subBrand,
                            shopCategory = draft.shopCategory,
                            subCategory = draft.subCategory
                        )
                    },
                    modifier = Modifier.testTag("reset_filters_button")
                ) {
                    Text("Clear All", color = QuickGreen)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sort by
                FilterSectionTitle("Sort By")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SortOption.entries.forEach { option ->
                        FilterChip(
                            selected = draft.sortOption == option,
                            onClick = { draft = draft.copy(sortOption = option) },
                            label = { Text(option.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Price Range
                FilterSectionTitle("Price Range (₹)")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    priceBuckets.forEach { bucket ->
                        val isSelected = draft.minPrice == bucket.min && draft.maxPrice == bucket.max
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                draft = draft.copy(minPrice = bucket.min, maxPrice = bucket.max)
                            },
                            label = { Text(bucket.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Discount %
                FilterSectionTitle("Minimum Discount")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0 to "Any", 10 to "10%+ OFF", 15 to "15%+ OFF", 20 to "20%+ OFF", 25 to "25%+ OFF").forEach { (pct, label) ->
                        FilterChip(
                            selected = draft.minDiscountPercent == pct,
                            onClick = { draft = draft.copy(minDiscountPercent = pct) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Rating
                FilterSectionTitle("Customer Rating")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0.0 to "All Ratings", 4.0 to "4.0★ & above", 4.5 to "4.5★ & above", 4.7 to "4.7★ & above").forEach { (r, label) ->
                        FilterChip(
                            selected = draft.minRating == r,
                            onClick = { draft = draft.copy(minRating = r) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Brand
                FilterSectionTitle("Brand")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    brands.forEach { brand ->
                        val selected = brand.id in draft.selectedBrands
                        FilterChip(
                            selected = selected,
                            onClick = {
                                val next = if (selected) draft.selectedBrands - brand.id else draft.selectedBrands + brand.id
                                draft = draft.copy(selectedBrands = next)
                            },
                            label = { Text(brand.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // Pack Size
                FilterSectionTitle("Pack Size")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    packSizes.take(18).forEach { pack ->
                        val selected = pack in draft.selectedPackSizes
                        FilterChip(
                            selected = selected,
                            onClick = {
                                val next = if (selected) draft.selectedPackSizes - pack else draft.selectedPackSizes + pack
                                draft = draft.copy(selectedPackSizes = next)
                            },
                            label = { Text(pack) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = QuickGreenContainer,
                                selectedLabelColor = QuickGreen
                            )
                        )
                    }
                }

                // In Stock Only
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Show In-Stock Items Only",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = draft.inStockOnly,
                        onCheckedChange = { draft = draft.copy(inStockOnly = it) }
                    )
                }

                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { onApply(draft) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("apply_filters_button")
                ) {
                    Text("Apply Filters", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FilterSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

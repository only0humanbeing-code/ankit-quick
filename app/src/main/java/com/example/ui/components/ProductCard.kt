package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Brand
import com.example.model.Product
import com.example.ui.theme.QuickAmberContainer
import com.example.ui.theme.QuickBlue
import com.example.ui.theme.QuickGold
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.util.FormatUtils
import kotlin.math.roundToInt

@Composable
fun ProductCard(
    product: Product,
    quantityInCart: Int,
    isNotified: Boolean,
    onProductClick: () -> Unit,
    onAddClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    onNotifyClick: () -> Unit,
    modifier: Modifier = Modifier,
    brand: Brand? = null
) {
    val variant = product.defaultVariant
    val inStock = variant.isInStock

    Surface(
        modifier = modifier
            .testTag("product_card_${product.id}")
            .clickable(onClick = onProductClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
            ) {
                ProductVisual(
                    imageKey = product.imageKey,
                    colorHex = product.colorHex,
                    modifier = Modifier.matchParentSize(),
                    iconSize = 44.dp,
                    imageUrl = product.imageUrl,
                    brandName = brand?.name.orEmpty(),
                    brandLogoUrl = brand?.logoUrl.orEmpty(),
                    brandColorHex = brand?.colorHex ?: 0xFF0C831FL
                )

                if (variant.discountPercent > 0) {
                    Surface(
                        color = QuickBlue,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "${variant.discountPercent}% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // Margin % indicator badge
                Surface(
                    color = QuickGreenContainer.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${variant.marginPercent.roundToInt()}% Margin",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuickGreen,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                if (!inStock) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "OUT OF STOCK",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Pack size + rating row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = variant.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Rating",
                        tint = QuickGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${product.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(36.dp)
            )

            Spacer(Modifier.height(6.dp))

            // Price & Add / Stepper / Notify Me row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = FormatUtils.inr(variant.sellingPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (variant.mrp > variant.sellingPrice) {
                        Text(
                            text = FormatUtils.inr(variant.mrp),
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                AnimatedContent(
                    targetState = Triple(inStock, quantityInCart > 0, isNotified),
                    transitionSpec = {
                        (fadeIn(tween(180)) + scaleIn(initialScale = 0.92f, animationSpec = tween(180)))
                            .togetherWith(fadeOut(tween(140)) + scaleOut(targetScale = 0.92f, animationSpec = tween(140)))
                            .using(SizeTransform(clip = false))
                    },
                    label = "product_card_action"
                ) { (stockAvail, hasQty, notified) ->
                    if (!stockAvail) {
                        OutlinedButton(
                            onClick = onNotifyClick,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                if (notified) QuickGreen else QuickGold
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (notified) QuickGreenContainer else QuickAmberContainer.copy(alpha = 0.45f),
                                contentColor = if (notified) QuickGreen else QuickGold
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("notify_button_${product.id}")
                        ) {
                            Icon(
                                imageVector = if (notified) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                                contentDescription = "Notify Me",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = if (notified) "Notified" else "Notify Me",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    } else if (!hasQty) {
                        OutlinedButton(
                            onClick = onAddClick,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.2.dp, QuickGreen),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = QuickGreenContainer.copy(alpha = 0.55f),
                                contentColor = QuickGreen
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("add_button_${product.id}")
                        ) {
                            Text(
                                text = "ADD",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    } else {
                        Surface(
                            color = QuickGreen,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                IconButton(
                                    onClick = onDecrementClick,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("dec_button_${product.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Decrease quantity",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "$quantityInCart",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = onIncrementClick,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("inc_button_${product.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "Increase quantity",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

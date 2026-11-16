package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.ui.components.ProductVisual
import com.example.ui.theme.QuickAmberContainer
import com.example.ui.theme.QuickBlue
import com.example.ui.theme.QuickBlueContainer
import com.example.ui.theme.QuickGold
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickRed
import com.example.ui.theme.QuickRedContainer
import com.example.util.FormatUtils

@Composable
fun OrdersScreen(
    orders: List<OrderRecord>,
    onOrderAgain: (OrderRecord) -> Unit,
    onBrowseGroceries: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Orders",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${orders.size} order(s) placed",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuickMuted
                    )
                }
            }
        }

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.ReceiptLong,
                        contentDescription = null,
                        tint = QuickMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("No orders yet", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Your order history and quick reorder options will appear here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuickMuted
                    )
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = onBrowseGroceries,
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Start Shopping", color = Color.White)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.id }) { order ->
                    OrderHistoryCard(
                        order = order,
                        onOrderAgain = { onOrderAgain(order) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderHistoryCard(
    order: OrderRecord,
    onOrderAgain: () -> Unit
) {
    val (statusBg, statusFg) = when (order.status) {
        OrderStatus.DELIVERED -> QuickGreenContainer to QuickGreen
        OrderStatus.OUT_FOR_DELIVERY -> QuickBlueContainer to QuickBlue
        OrderStatus.PACKING, OrderStatus.PLACED -> QuickAmberContainer to QuickGold
        OrderStatus.CANCELLED -> QuickRedContainer to QuickRed
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.id,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = FormatUtils.formatDateTime(order.createdAtMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = QuickMuted
                    )
                }
                Surface(color = statusBg, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = order.status.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusFg,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Thumbnails row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(order.items) { line ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductVisual(
                                imageKey = line.imageKey,
                                colorHex = line.colorHex,
                                iconSize = 18.dp,
                                modifier = Modifier.size(34.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "${line.quantity}x ${line.productName}",
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${line.variantLabel} • ${FormatUtils.inr(line.unitPrice * line.quantity)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = QuickMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = "${order.deliverySlotLabel} • ${order.paymentMethodLabel}",
                style = MaterialTheme.typography.bodySmall,
                color = QuickMuted
            )
            Text(
                text = order.addressSnapshot,
                style = MaterialTheme.typography.bodySmall,
                color = QuickMuted,
                maxLines = 1
            )

            HorizontalDivider(Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Paid ${FormatUtils.inr(order.grandTotal)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (order.totalSavings > 0) {
                        Text(
                            text = "Saved ${FormatUtils.inr(order.totalSavings)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = QuickGreen
                        )
                    }
                }

                Button(
                    onClick = onOrderAgain,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                    modifier = Modifier
                        .height(40.dp)
                        .testTag("order_again_${order.id}")
                ) {
                    Icon(Icons.Rounded.Replay, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Order Again", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

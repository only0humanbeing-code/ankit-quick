package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.repository.PaymentMethodType
import com.example.model.CartSummary
import com.example.model.DeliverySlot
import com.example.model.OrderRecord
import com.example.model.SavedAddress
import com.example.model.UserProfile
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.util.FormatUtils
import java.util.UUID

@Composable
fun CheckoutScreen(
    cartSummary: CartSummary,
    addresses: List<SavedAddress>,
    deliverySlots: List<DeliverySlot>,
    userProfile: UserProfile,
    isProcessing: Boolean,
    placedOrder: OrderRecord?,
    onBackClick: () -> Unit,
    onAddAddress: (SavedAddress) -> Unit,
    onPlaceOrder: (SavedAddress, DeliverySlot, PaymentMethodType) -> Unit,
    onViewOrdersAfterSuccess: () -> Unit,
    onContinueShoppingAfterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enabledSlots = deliverySlots.filter { it.isEnabled }
    var selectedAddressId by remember(addresses) {
        mutableStateOf(addresses.firstOrNull { it.isDefault }?.id ?: addresses.firstOrNull()?.id)
    }
    var selectedSlotId by remember(enabledSlots) {
        mutableStateOf(enabledSlots.firstOrNull()?.id)
    }
    var selectedPayment by remember { mutableStateOf(PaymentMethodType.UPI) }
    var showAddAddressDialog by remember { mutableStateOf(false) }

    LaunchedEffect(addresses) {
        if (selectedAddressId == null || addresses.none { it.id == selectedAddressId }) {
            selectedAddressId = addresses.firstOrNull { it.isDefault }?.id ?: addresses.firstOrNull()?.id
        }
    }

    if (placedOrder != null) {
        OrderSuccessView(
            order = placedOrder,
            onViewOrders = onViewOrdersAfterSuccess,
            onContinueShopping = onContinueShoppingAfterSuccess
        )
        return
    }

    val selectedAddress = addresses.firstOrNull { it.id == selectedAddressId }
    val selectedSlot = enabledSlots.firstOrNull { it.id == selectedSlotId }
    val slotFee = selectedSlot?.fee ?: 0.0
    val finalTotal = cartSummary.grandTotal + slotFee

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Checkout",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 6.dp, shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = FormatUtils.inr(finalTotal),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = selectedPayment.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = QuickMuted
                        )
                    }

                    Button(
                        onClick = {
                            if (selectedAddress != null && selectedSlot != null) {
                                onPlaceOrder(selectedAddress, selectedSlot, selectedPayment)
                            }
                        },
                        enabled = !isProcessing && selectedAddress != null && selectedSlot != null && cartSummary.items.isNotEmpty(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("place_order_button")
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Processing...", color = Color.White)
                        } else {
                            Text(
                                text = "Place Order • ${FormatUtils.inr(finalTotal)}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Address Selection
            item {
                CheckoutSectionCard(
                    title = "1. Delivery Address",
                    action = {
                        TextButton(
                            onClick = { showAddAddressDialog = true },
                            modifier = Modifier.testTag("checkout_add_address_button")
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Add New", color = QuickGreen)
                        }
                    }
                ) {
                    if (addresses.isEmpty()) {
                        Text(
                            text = "No saved address yet. Add a delivery address to continue.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = QuickMuted
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            addresses.forEach { addr ->
                                val selected = addr.id == selectedAddressId
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selected) QuickGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(
                                        1.dp,
                                        if (selected) QuickGreen else MaterialTheme.colorScheme.outline
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedAddressId = addr.id }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selected,
                                            onClick = { selectedAddressId = addr.id },
                                            colors = RadioButtonDefaults.colors(selectedColor = QuickGreen)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Rounded.LocationOn,
                                                    contentDescription = null,
                                                    tint = QuickGreen,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(addr.label, style = MaterialTheme.typography.titleSmall)
                                            }
                                            Text(
                                                text = addr.shortSummary,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = QuickMuted
                                            )
                                            Text(
                                                text = "${addr.recipientName} • +91 ${addr.phone}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = QuickMuted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Delivery Slot
            item {
                CheckoutSectionCard(title = "2. Choose Delivery Slot") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        enabledSlots.forEach { slot ->
                            val selected = slot.id == selectedSlotId
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) QuickGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, if (selected) QuickGreen else MaterialTheme.colorScheme.outline),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSlotId = slot.id }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = { selectedSlotId = slot.id },
                                        colors = RadioButtonDefaults.colors(selectedColor = QuickGreen)
                                    )
                                    Icon(
                                        Icons.Rounded.Schedule,
                                        contentDescription = null,
                                        tint = if (slot.isExpress) QuickGreen else QuickMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(slot.label, style = MaterialTheme.typography.titleSmall)
                                        Text(slot.timeRange, style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                                    }
                                    Text(
                                        text = if (slot.fee == 0.0) "FREE" else "+${FormatUtils.inr(slot.fee)}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (slot.fee == 0.0) QuickGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Payment Method (Razorpay-ready abstraction)
            item {
                CheckoutSectionCard(title = "3. Payment Method") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.Security,
                                    contentDescription = null,
                                    tint = QuickGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Demo Payment Gateway • Architecture ready for Razorpay Checkout SDK",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        PaymentMethodType.entries.forEach { method ->
                            val selected = selectedPayment == method
                            val icon = when (method) {
                                PaymentMethodType.UPI -> Icons.Rounded.Payments
                                PaymentMethodType.CARD -> Icons.Rounded.CreditCard
                                PaymentMethodType.WALLET -> Icons.Rounded.Wallet
                                PaymentMethodType.COD -> Icons.Rounded.Payments
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selected) QuickGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, if (selected) QuickGreen else MaterialTheme.colorScheme.outline),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPayment = method }
                                    .testTag("payment_method_${method.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = { selectedPayment = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = QuickGreen)
                                    )
                                    Icon(icon, contentDescription = null, tint = QuickGreen, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(method.title, style = MaterialTheme.typography.titleSmall)
                                        Text(
                                            text = if (method == PaymentMethodType.WALLET) {
                                                "Available: ${FormatUtils.inr(userProfile.walletBalance)}"
                                            } else method.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = QuickMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Order Summary
            item {
                CheckoutSectionCard(title = "4. Order Summary (${cartSummary.totalCount} items)") {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Item Total", style = MaterialTheme.typography.bodyMedium, color = QuickMuted)
                            Text(FormatUtils.inr(cartSummary.itemTotal), style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery & Slot Fee", style = MaterialTheme.typography.bodyMedium, color = QuickMuted)
                            Text(
                                if (cartSummary.deliveryFee + slotFee == 0.0) "FREE" else FormatUtils.inr(cartSummary.deliveryFee + slotFee),
                                color = if (cartSummary.deliveryFee + slotFee == 0.0) QuickGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("You Save", style = MaterialTheme.typography.bodyMedium, color = QuickGreen)
                            Text(FormatUtils.inr(cartSummary.itemSavings), color = QuickGreen, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(Modifier.padding(vertical = 4.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Payable", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(FormatUtils.inr(finalTotal), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }

    if (showAddAddressDialog) {
        AddEditAddressDialog(
            initial = null,
            defaultPhone = userProfile.phone.ifEmpty { "9876543210" },
            defaultName = userProfile.name,
            onDismiss = { showAddAddressDialog = false },
            onSave = { newAddr ->
                onAddAddress(newAddr)
                selectedAddressId = newAddr.id
                showAddAddressDialog = false
            }
        )
    }
}

@Composable
private fun CheckoutSectionCard(
    title: String,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                action?.invoke()
            }
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun AddEditAddressDialog(
    initial: SavedAddress?,
    defaultPhone: String,
    defaultName: String,
    onDismiss: () -> Unit,
    onSave: (SavedAddress) -> Unit
) {
    var label by remember { mutableStateOf(initial?.label ?: "Home") }
    var recipient by remember { mutableStateOf(initial?.recipientName ?: defaultName) }
    var phone by remember { mutableStateOf(initial?.phone ?: defaultPhone) }
    var house by remember { mutableStateOf(initial?.houseFlat ?: "") }
    var street by remember { mutableStateOf(initial?.streetArea ?: "") }
    var landmark by remember { mutableStateOf(initial?.landmark ?: "") }
    var city by remember { mutableStateOf(initial?.city ?: "Bengaluru") }
    var pincode by remember { mutableStateOf(initial?.pincode ?: "560038") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add New Address" else "Edit Address") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Home", "Work", "Other").forEach { tag ->
                        val selected = label == tag
                        OutlinedButton(
                            onClick = { label = tag },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selected) QuickGreenContainer else Color.Transparent,
                                contentColor = if (selected) QuickGreen else QuickMuted
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(tag, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                OutlinedTextField(
                    value = recipient,
                    onValueChange = { recipient = it },
                    label = { Text("Recipient Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { c -> c.isDigit() }.take(10) },
                    label = { Text("Phone (10 digits)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = house,
                    onValueChange = { house = it },
                    label = { Text("Flat / House / Building") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Street / Area / Locality") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { pincode = it.filter { c -> c.isDigit() }.take(6) },
                        label = { Text("Pincode") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = landmark,
                    onValueChange = { landmark = it },
                    label = { Text("Landmark (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val saved = SavedAddress(
                        id = initial?.id ?: "addr_${UUID.randomUUID().toString().take(8)}",
                        label = label.ifBlank { "Home" },
                        recipientName = recipient.ifBlank { defaultName },
                        phone = phone.ifBlank { defaultPhone },
                        houseFlat = house.ifBlank { "Flat 101" },
                        streetArea = street.ifBlank { "Main Road" },
                        landmark = landmark,
                        city = city.ifBlank { "Bengaluru" },
                        pincode = pincode.ifBlank { "560001" },
                        isDefault = true
                    )
                    onSave(saved)
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen)
            ) {
                Text("Save Address", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun OrderSuccessView(
    order: OrderRecord,
    onViewOrders: () -> Unit,
    onContinueShopping: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "Order Placed",
                tint = QuickGreen,
                modifier = Modifier.size(84.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Order Placed Successfully!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Order ID: ${order.id} • ${order.deliverySlotLabel}",
                style = MaterialTheme.typography.titleSmall,
                color = QuickGreen
            )
            Spacer(Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Paid: ${FormatUtils.inr(order.grandTotal)} via ${order.paymentMethodLabel}")
                    Text("Ref: ${order.paymentRef}", style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                    Text("Delivering to: ${order.addressSnapshot}", style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                }
            }
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onViewOrders,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("order_success_track_button")
            ) {
                Text("View My Orders", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onContinueShopping,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Continue Shopping")
            }
        }
    }
}

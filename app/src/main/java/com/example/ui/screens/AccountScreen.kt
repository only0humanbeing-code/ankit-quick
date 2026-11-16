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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Product
import com.example.model.SavedAddress
import com.example.model.UserProfile
import com.example.ui.theme.QuickAmberContainer
import com.example.ui.theme.QuickGold
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickGreenDark
import com.example.ui.theme.QuickMuted
import com.example.ui.theme.QuickRed
import com.example.util.FormatUtils

@Composable
fun AccountScreen(
    userProfile: UserProfile,
    addresses: List<SavedAddress>,
    onUpdateProfile: (name: String, email: String, notificationsEnabled: Boolean) -> Unit,
    onTopUpWallet: (Double) -> Unit,
    onSaveAddress: (SavedAddress) -> Unit,
    onSetDefaultAddress: (String) -> Unit,
    onDeleteAddress: (String) -> Unit,
    onOpenAdminPanel: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    savedProducts: List<Product> = emptyList(),
    onOpenSavedProduct: (Product) -> Unit = {},
    onRemoveSavedProduct: (Product) -> Unit = {}
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddMoneyDialog by remember { mutableStateOf(false) }
    var showSavedPaymentsDialog by remember { mutableStateOf(false) }
    var showSavedItemsDialog by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<SavedAddress?>(null) }
    var showNewAddressDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Profile Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = QuickGreenContainer,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = QuickGreen,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+91 ${userProfile.phone.ifEmpty { "9876543210" }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = QuickGreen
                        )
                        Text(
                            text = userProfile.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickMuted
                        )
                    }
                    IconButton(onClick = { showEditProfileDialog = true }) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit Profile")
                    }
                }
            }
        }

        // Wallet Balance Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = QuickGreenContainer,
                border = BorderStroke(1.dp, QuickGreen.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AccountBalanceWallet,
                            contentDescription = "QuickKart Wallet",
                            tint = QuickGreenDark,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "QuickKart Wallet",
                                style = MaterialTheme.typography.labelLarge,
                                color = QuickGreenDark
                            )
                            Text(
                                text = FormatUtils.inr(userProfile.walletBalance),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = QuickGreenDark
                            )
                        }
                    }

                    Button(
                        onClick = { showAddMoneyDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                        modifier = Modifier.testTag("wallet_add_money_button")
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(4.dp))
                        Text("Add Money", color = Color.White)
                    }
                }
            }
        }

        // Admin Panel Entry Card (/admin area)
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = QuickAmberContainer.copy(alpha = 0.65f),
                border = BorderStroke(1.dp, QuickGold.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenAdminPanel)
                    .testTag("account_open_admin_button")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AdminPanelSettings,
                        contentDescription = "Admin Panel",
                        tint = QuickGold,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Admin Panel (/admin)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage 60+ products, variants, margin %, stock, orders & slots",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuickMuted
                        )
                    }
                    Icon(Icons.Rounded.ChevronRight, contentDescription = null)
                }
            }
        }

        // Saved Payments + Notifications
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSavedItemsDialog = true }
                            .padding(16.dp)
                            .testTag("account_saved_for_later_row"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Bookmark, contentDescription = null, tint = QuickGreen)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Saved For Later (${savedProducts.size})", style = MaterialTheme.typography.titleSmall)
                            Text(
                                text = if (savedProducts.isEmpty()) "Bookmark products to buy later" else "${savedProducts.size} bookmarked item(s)",
                                style = MaterialTheme.typography.bodySmall,
                                color = QuickMuted
                            )
                        }
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = QuickMuted)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSavedPaymentsDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.CreditCard, contentDescription = null, tint = QuickGreen)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Saved Payments", style = MaterialTheme.typography.titleSmall)
                            Text("UPI IDs, Cards & Razorpay ready", style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                        }
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = QuickMuted)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.NotificationsActive, contentDescription = null, tint = QuickGreen)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Notifications", style = MaterialTheme.typography.titleSmall)
                            Text("Order alerts & Back-in-Stock pings", style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                        }
                        Switch(
                            checked = userProfile.notificationsEnabled,
                            onCheckedChange = { enabled ->
                                onUpdateProfile(userProfile.name, userProfile.email, enabled)
                            }
                        )
                    }
                }
            }
        }

        // Saved Addresses Section
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Saved Addresses (${addresses.size})", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = { showNewAddressDialog = true }) {
                            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Add", color = QuickGreen)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    addresses.forEach { addr ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (addr.isDefault) QuickGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onSetDefaultAddress(addr.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    tint = if (addr.isDefault) QuickGreen else QuickMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(addr.label, style = MaterialTheme.typography.titleSmall)
                                        if (addr.isDefault) {
                                            Spacer(Modifier.width(6.dp))
                                            Text("DEFAULT", style = MaterialTheme.typography.labelSmall, color = QuickGreen)
                                        }
                                    }
                                    Text(addr.shortSummary, style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                                }
                                IconButton(onClick = { editingAddress = addr }) {
                                    Icon(Icons.Rounded.Edit, contentDescription = "Edit Address", modifier = Modifier.size(18.dp))
                                }
                                if (addresses.size > 1) {
                                    IconButton(onClick = { onDeleteAddress(addr.id) }) {
                                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete Address", tint = QuickRed, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Logout Button
        item {
            OutlinedButton(
                onClick = onLogout,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, QuickRed.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuickRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("logout_button")
            ) {
                Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showEditProfileDialog) {
        var name by remember { mutableStateOf(userProfile.name) }
        var email by remember { mutableStateOf(userProfile.email) }
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, singleLine = true)
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(name, email, userProfile.notificationsEnabled)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuickGreen)
                ) { Text("Save", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddMoneyDialog) {
        AlertDialog(
            onDismissRequest = { showAddMoneyDialog = false },
            title = { Text("Add Money to Wallet") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Instant demo top-up for testing Wallet checkout:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(250.0, 500.0, 1000.0).forEach { amt ->
                            Button(
                                onClick = {
                                    onTopUpWallet(amt)
                                    showAddMoneyDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = QuickGreen),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("+${FormatUtils.inr(amt)}", color = Color.White)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddMoneyDialog = false }) { Text("Close") }
            }
        )
    }

    if (showSavedItemsDialog) {
        AlertDialog(
            onDismissRequest = { showSavedItemsDialog = false },
            title = { Text("Saved For Later (${savedProducts.size})") },
            text = {
                if (savedProducts.isEmpty()) {
                    Text(
                        text = "No products saved yet. Tap \"Save For Later\" on any product page to bookmark it here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = QuickMuted
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        savedProducts.forEach { prod ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showSavedItemsDialog = false
                                        onOpenSavedProduct(prod)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(prod.name, style = MaterialTheme.typography.titleSmall)
                                        Text(
                                            text = "${prod.defaultVariant.label} • ${FormatUtils.inr(prod.defaultVariant.sellingPrice)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = QuickGreen
                                        )
                                    }
                                    IconButton(onClick = { onRemoveSavedProduct(prod) }) {
                                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Remove saved item", tint = QuickRed)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedItemsDialog = false }) { Text("Close") }
            }
        )
    }

    if (showSavedPaymentsDialog) {
        AlertDialog(
            onDismissRequest = { showSavedPaymentsDialog = false },
            title = { Text("Saved Payment Methods") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• UPI: aarav@okicici (Verified)", style = MaterialTheme.typography.bodyMedium)
                    Text("• RuPay Debit Card •••• 4821", style = MaterialTheme.typography.bodyMedium)
                    Text("• QuickKart Wallet: ${FormatUtils.inr(userProfile.walletBalance)}", style = MaterialTheme.typography.bodyMedium, color = QuickGreen)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Demo payment provider active. Razorpay SDK hooks are configured in PaymentGateway.kt.",
                        style = MaterialTheme.typography.bodySmall,
                        color = QuickMuted
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showSavedPaymentsDialog = false }) { Text("Done") }
            }
        )
    }

    if (showNewAddressDialog || editingAddress != null) {
        AddEditAddressDialog(
            initial = editingAddress,
            defaultPhone = userProfile.phone.ifEmpty { "9876543210" },
            defaultName = userProfile.name,
            onDismiss = {
                showNewAddressDialog = false
                editingAddress = null
            },
            onSave = { addr ->
                onSaveAddress(addr)
                showNewAddressDialog = false
                editingAddress = null
            }
        )
    }
}

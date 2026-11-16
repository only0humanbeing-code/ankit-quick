package com.example.data.repository

import kotlinx.coroutines.delay
import java.util.UUID

enum class PaymentMethodType(
    val id: String,
    val title: String,
    val subtitle: String,
    val supportsRazorpay: Boolean
) {
    UPI("upi", "UPI (GPay, PhonePe, Paytm)", "Instant zero-fee UPI payment", true),
    CARD("card", "Credit / Debit Card", "Visa, Mastercard, RuPay", true),
    WALLET("wallet", "QuickKart Wallet", "Fast 1-tap wallet checkout", false),
    COD("cod", "Cash / UPI on Delivery", "Pay when order arrives", false)
}

/**
 * Ready-to-plug Razorpay order payload structure.
 * When integrating com.razorpay:checkout, map this payload into Checkout().open(activity, JSONObject).
 */
data class RazorpayOrderPayload(
    val merchantName: String = "QuickKart India Pvt Ltd",
    val currency: String = "INR",
    val amountPaise: Long,
    val orderReceipt: String,
    val customerPhone: String,
    val customerEmail: String,
    val methodType: PaymentMethodType
)

sealed interface PaymentResult {
    data class Success(val transactionRef: String, val gatewayName: String) : PaymentResult
    data class Failure(val reason: String) : PaymentResult
}

interface PaymentGateway {
    suspend fun processPayment(payload: RazorpayOrderPayload): PaymentResult
}

/**
 * Demo payment gateway that simulates gateway authorization cleanly.
 * Swap with RazorpayPaymentGateway once live merchant keys are configured.
 */
class DemoPaymentGateway : PaymentGateway {
    override suspend fun processPayment(payload: RazorpayOrderPayload): PaymentResult {
        delay(450)
        val prefix = when (payload.methodType) {
            PaymentMethodType.UPI -> "RZP_DEMO_UPI"
            PaymentMethodType.CARD -> "RZP_DEMO_CARD"
            PaymentMethodType.WALLET -> "QK_WALLET"
            PaymentMethodType.COD -> "COD_ORDER"
        }
        val ref = "${prefix}_${UUID.randomUUID().toString().take(8).uppercase()}"
        return PaymentResult.Success(
            transactionRef = ref,
            gatewayName = if (payload.methodType.supportsRazorpay) "Razorpay Demo" else payload.methodType.title
        )
    }
}

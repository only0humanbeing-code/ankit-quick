package com.example.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object FormatUtils {
    private val inrFormat = NumberFormat.getNumberInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    fun inr(amount: Double): String {
        return "₹${inrFormat.format(amount.roundToInt())}"
    }

    fun inrExact(amount: Double): String {
        val rounded = (amount * 100.0).roundToInt() / 100.0
        return if (rounded % 1.0 == 0.0) {
            "₹${rounded.toInt()}"
        } else {
            "₹%.2f".format(Locale("en", "IN"), rounded)
        }
    }

    fun unitRateLabel(sellingPrice: Double, packLabel: String): String? {
        if (sellingPrice <= 0.0 || packLabel.isBlank()) return null
        val match = Regex("""(\d+(?:\.\d+)?)\s*(kg|g|ml|l)\b""", RegexOption.IGNORE_CASE)
            .find(packLabel) ?: return null
        val qty = match.groupValues[1].toDoubleOrNull() ?: return null
        if (qty <= 0.0) return null
        val unit = match.groupValues[2].lowercase(Locale.ROOT)
        // Only multiply by "Pack of N" when the pack count follows the single-unit size (e.g. "1 L Pack of 6"),
        // not when total weight is in parentheses (e.g. "Pack of 6 (420 g)").
        val packMultiplier = if (!packLabel.contains('(')) {
            Regex("""pack\s*of\s*(\d+)""", RegexOption.IGNORE_CASE)
                .find(packLabel.substring(match.range.last + 1))
                ?.groupValues?.getOrNull(1)?.toIntOrNull()?.coerceAtLeast(1) ?: 1
        } else {
            1
        }
        val (baseAmount, baseUnit) = when (unit) {
            "kg" -> (qty * packMultiplier) to "kg"
            "g" -> ((qty * packMultiplier) / 1000.0) to "kg"
            "l" -> (qty * packMultiplier) to "L"
            "ml" -> ((qty * packMultiplier) / 1000.0) to "L"
            else -> return null
        }
        if (baseAmount <= 0.0) return null
        val unitPrice = sellingPrice / baseAmount
        return "(${inrExact(unitPrice)}/$baseUnit)"
    }

    fun formatDateTime(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("en", "IN"))
        return sdf.format(Date(millis))
    }
}

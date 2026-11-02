package com.example.util

object NumberFormatUtils {
    /**
     * Automatically formats numeric input with thousand separator dots (.).
     * Examples:
     * - "1000" -> "1.000"
     * - "10000" -> "10.000"
     * - "100000" -> "100.000"
     * - "1000000" -> "1.000.000"
     */
    fun formatNominal(raw: String): String {
        val digitsOnly = raw.filter { it.isDigit() }
        if (digitsOnly.isEmpty()) return ""

        val cleanDigits = digitsOnly.trimStart('0').ifEmpty { "0" }
        val reversed = cleanDigits.reversed()
        val chunked = reversed.chunked(3)
        return chunked.joinToString(".").reversed()
    }

    /**
     * Determines whether a given column name represents a monetary or nominal field.
     */
    fun isNominalColumn(columnName: String): Boolean {
        val lower = columnName.lowercase().trim()
        return lower.contains("harga") ||
               lower.contains("price") ||
               lower.contains("nominal") ||
               lower.contains("biaya") ||
               lower.contains("tarif") ||
               lower.contains("uang") ||
               lower.contains("total") ||
               lower.contains("rp") ||
               lower.contains("cost") ||
               lower.contains("bayar") ||
               lower.contains("ongkir")
    }
}

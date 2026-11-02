package com.example.data

enum class AppCurrency(
    val code: String,
    val symbol: String,
    val currencyName: String,
    val country: String,
    val flag: String,
    val continent: String
) {
    // Asia Tenggara
    IDR("IDR", "Rp", "Rupiah", "Indonesia", "🇮🇩", "Asia Tenggara"),
    MYR("MYR", "RM", "Ringgit", "Malaysia", "🇲🇾", "Asia Tenggara"),
    SGD("SGD", "S$", "Dolar Singapura", "Singapura", "🇸🇬", "Asia Tenggara"),
    THB("THB", "฿", "Baht", "Thailand", "🇹🇭", "Asia Tenggara"),
    VND("VND", "₫", "Dong", "Vietnam", "🇻🇳", "Asia Tenggara"),
    PHP("PHP", "₱", "Peso", "Filipina", "🇵🇭", "Asia Tenggara"),
    BND("BND", "B$", "Dolar Brunei", "Brunei", "🇧🇳", "Asia Tenggara"),

    // Asia
    JPY("JPY", "¥", "Yen", "Jepang", "🇯🇵", "Asia"),
    KRW("KRW", "₩", "Won", "Korea Selatan", "🇰🇷", "Asia"),
    CNY("CNY", "¥", "Yuan", "Tiongkok", "🇨🇳", "Asia"),
    INR("INR", "₹", "Rupee", "India", "🇮🇳", "Asia"),
    SAR("SAR", "﷼", "Riyal", "Arab Saudi", "🇸🇦", "Asia"),
    AED("AED", "د.إ", "Dirham", "Uni Emirat Arab", "🇦🇪", "Asia"),

    // Eropa
    EUR("EUR", "€", "Euro", "Uni Eropa", "🇪🇺", "Eropa"),
    GBP("GBP", "£", "Poundsterling", "Inggris", "🇬🇧", "Eropa"),
    CHF("CHF", "CHF", "Franc", "Swiss", "🇨🇭", "Eropa"),
    RUB("RUB", "₽", "Rubel", "Rusia", "🇷🇺", "Eropa"),
    TRY("TRY", "₺", "Lira", "Turki", "🇹🇷", "Eropa"),

    // Amerika
    USD("USD", "$", "US Dollar", "Amerika Serikat", "🇺🇸", "Amerika"),
    CAD("CAD", "CA$", "Dollar Kanada", "Kanada", "🇨🇦", "Amerika"),
    BRL("BRL", "R$", "Real", "Brasil", "🇧🇷", "Amerika"),
    MXN("MXN", "Mex$", "Peso", "Meksiko", "🇲🇽", "Amerika"),

    // Afrika
    ZAR("ZAR", "R", "Rand", "Afrika Selatan", "🇿🇦", "Afrika"),
    EGP("EGP", "E£", "Pound Mesir", "Mesir", "🇪🇬", "Afrika"),
    NGN("NGN", "₦", "Naira", "Nigeria", "🇳🇬", "Afrika"),
    KES("KES", "KSh", "Shilling", "Kenya", "🇰🇪", "Afrika");

    companion object {
        val DEFAULT = IDR

        fun fromCode(code: String?): AppCurrency {
            if (code == null) return DEFAULT
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: DEFAULT
        }
    }
}

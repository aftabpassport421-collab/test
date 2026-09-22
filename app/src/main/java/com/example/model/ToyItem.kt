package com.example.model

data class ToyItem(
  val id: String,
  val name: String,
  val brand: String,
  val category: String,
  val ageRange: String,
  val priceQar: Double,
  val originalPriceQar: Double? = null,
  val rating: Double,
  val reviewsCount: Int,
  val badge: String? = null,
  val description: String,
  val features: List<String>,
  val inStock: Boolean = true,
  val popularScore: Int = 90,
  val iconEmoji: String = "🧸",
  val accentColorHex: Long = 0xFF4F46E5,
) {
  val discountPercent: Int?
    get() = if (originalPriceQar != null && originalPriceQar > priceQar) {
      (((originalPriceQar - priceQar) / originalPriceQar) * 100).toInt()
    } else null
}

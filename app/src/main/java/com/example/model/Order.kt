package com.example.model

enum class DeliveryType(val title: String, val priceQar: Double, val eta: String) {
  SAME_DAY_DOHA("Same-Day Doha Express", 25.0, "Within 3-5 Hours"),
  STANDARD_QATAR("Standard Qatar Delivery", 15.0, "Next Day Delivery")
}

enum class OrderStatus(val label: String, val stepIndex: Int) {
  CONFIRMED("Order Confirmed", 1),
  PREPARING("Packed at Doha Hub", 2),
  OUT_FOR_DELIVERY("Out for Delivery in Qatar", 3),
  DELIVERED("Delivered", 4)
}

data class Order(
  val id: String,
  val items: List<CartItem>,
  val subtotalQar: Double,
  val deliveryFeeQar: Double,
  val discountQar: Double,
  val totalQar: Double,
  val deliveryType: DeliveryType,
  val customerName: String,
  val phone: String,
  val addressOrStore: String,
  val city: String,
  val paymentMethod: String,
  val orderDateFormatted: String,
  val status: OrderStatus = OrderStatus.CONFIRMED,
  val estimatedArrival: String,
  val transactionRef: String = "TXN-${(100000..999999).random()}",
  val isPaid: Boolean = true
)

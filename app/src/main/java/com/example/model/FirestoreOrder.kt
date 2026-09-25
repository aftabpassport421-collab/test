package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Exact status values as specified by user:
 * "pending", "packaged", "shipping", "delivered"
 */
enum class FirestoreOrderStatus(
  val rawValue: String,
  val displayName: String,
  val stepIndex: Int,
  val iconEmoji: String,
  val description: String
) {
  PENDING(
    rawValue = "pending",
    displayName = "Pending Confirmation",
    stepIndex = 0,
    iconEmoji = "⏳",
    description = "Order received and queued at Doha distribution hub"
  ),
  PACKAGED(
    rawValue = "packaged",
    displayName = "Packaged & Inspected",
    stepIndex = 1,
    iconEmoji = "🎁",
    description = "Toys packed with premium gift ribbons and safety seals"
  ),
  SHIPPING(
    rawValue = "shipping",
    displayName = "Out for Delivery",
    stepIndex = 2,
    iconEmoji = "🚚",
    description = "Dispatched with Doha express courier van"
  ),
  DELIVERED(
    rawValue = "delivered",
    displayName = "Delivered",
    stepIndex = 3,
    iconEmoji = "✅",
    description = "Successfully delivered to customer doorstep"
  );

  companion object {
    fun fromRaw(value: String): FirestoreOrderStatus {
      return values().find { it.rawValue.equals(value.trim(), ignoreCase = true) } ?: PENDING
    }
  }
}

data class FirestoreOrderItem(
  val toyId: String = "",
  val name: String = "",
  val quantity: Int = 1,
  val priceQar: Double = 0.0,
  val iconEmoji: String = "🧸"
) {
  fun toMap(): Map<String, Any> {
    return mapOf(
      "toyId" to toyId,
      "name" to name,
      "quantity" to quantity,
      "priceQar" to priceQar,
      "iconEmoji" to iconEmoji
    )
  }

  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromMap(map: Map<String, Any?>): FirestoreOrderItem {
      return FirestoreOrderItem(
        toyId = map["toyId"] as? String ?: "",
        name = map["name"] as? String ?: "Toy Item",
        quantity = (map["quantity"] as? Number)?.toInt() ?: 1,
        priceQar = (map["priceQar"] as? Number)?.toDouble() ?: 0.0,
        iconEmoji = map["iconEmoji"] as? String ?: "🧸"
      )
    }
  }
}

/**
 * Firestore Order Document schema conforming to user requirements:
 * - orderId
 * - userId
 * - items
 * - totalAmount
 * - status (pending, packaged, shipping, delivered)
 * - timestamp
 */
data class FirestoreOrder(
  val orderId: String = "",
  val userId: String = "user_qatar_doha",
  val items: List<FirestoreOrderItem> = emptyList(),
  val totalAmount: Double = 0.0,
  val status: String = "pending",
  val timestamp: Long = System.currentTimeMillis(),
  val customerName: String = "Mohammed Al-Kuwari",
  val phone: String = "+974 5512 3456",
  val address: String = "Villa 28, Street 902, West Bay Lagoon",
  val city: String = "Doha",
  val paymentMethod: String = "Credit Card (Visa/NAPS)"
) {
  val orderStatus: FirestoreOrderStatus
    get() = FirestoreOrderStatus.fromRaw(status)

  val formattedTime: String
    get() {
      val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
      return sdf.format(Date(timestamp))
    }

  fun toMap(): Map<String, Any?> {
    return mapOf(
      "orderId" to orderId,
      "userId" to userId,
      "totalAmount" to totalAmount,
      "status" to status,
      "timestamp" to timestamp,
      "customerName" to customerName,
      "phone" to phone,
      "address" to address,
      "city" to city,
      "paymentMethod" to paymentMethod,
      "items" to items.map { item ->
        mapOf(
          "toyId" to item.toyId,
          "name" to item.name,
          "quantity" to item.quantity,
          "priceQar" to item.priceQar,
          "iconEmoji" to item.iconEmoji
        )
      }
    )
  }

  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromMap(id: String, map: Map<String, Any?>): FirestoreOrder {
      val rawItems = map["items"] as? List<Map<String, Any?>> ?: emptyList()
      val itemsList = rawItems.map { FirestoreOrderItem.fromMap(it) }

      return FirestoreOrder(
        orderId = (map["orderId"] as? String) ?: id,
        userId = (map["userId"] as? String) ?: "user_qatar_doha",
        items = itemsList,
        totalAmount = (map["totalAmount"] as? Number)?.toDouble() ?: 0.0,
        status = (map["status"] as? String) ?: "pending",
        timestamp = (map["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis(),
        customerName = (map["customerName"] as? String) ?: "Customer",
        phone = (map["phone"] as? String) ?: "",
        address = (map["address"] as? String) ?: "",
        city = (map["city"] as? String) ?: "Doha",
        paymentMethod = (map["paymentMethod"] as? String) ?: "Card"
      )
    }
  }
}

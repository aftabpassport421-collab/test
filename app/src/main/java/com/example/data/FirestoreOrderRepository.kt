package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.FirestoreOrder
import com.example.model.FirestoreOrderItem
import com.example.model.ToyItem
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Repository responsible for Firebase Firestore "orders" collection:
 * - orderId
 * - userId
 * - items
 * - totalAmount
 * - status (pending, packaged, shipping, delivered)
 * - timestamp
 */
class FirestoreOrderRepository private constructor(context: Context) {

  private val tag = "FirestoreOrderRepo"
  private var firestoreInstance: FirebaseFirestore? = null

  // In-memory backing cache for immediate offline responsiveness
  private val cachedOrders = CopyOnWriteArrayList<FirestoreOrder>()
  private val cachedProducts = CopyOnWriteArrayList<ToyItem>().apply {
    addAll(ToyCatalog.toys)
  }

  init {
    initFirebase(context.applicationContext)
    seedInitialOrdersIfEmpty()
  }

  private fun initFirebase(context: Context) {
    try {
      if (FirebaseApp.getApps(context).isEmpty()) {
        val options = FirebaseOptions.Builder()
          .setApplicationId("1:338438459734:android:e6c27cfe7c95571af746f7")
          .setProjectId("wonder-toy-2f323")
          .setApiKey("AIzaSyDwO-KhRmpP3Rv0PyYCxjxh6755eGZf_Jc")
          .build()
        FirebaseApp.initializeApp(context, options)
        Log.d(tag, "FirebaseApp initialized with fallback options")
      }
      firestoreInstance = FirebaseFirestore.getInstance()
      Log.d(tag, "FirebaseFirestore instance acquired successfully")
    } catch (e: Throwable) {
      Log.w(tag, "Firebase initialization notice: ${e.message}. Running in offline cache mode.")
    }
  }

  private fun seedInitialOrdersIfEmpty() {
    if (cachedOrders.isEmpty()) {
      cachedOrders.addAll(
        listOf(
          FirestoreOrder(
            orderId = "ORD-90214",
            userId = "user_qatar_doha",
            items = listOf(
              FirestoreOrderItem(
                toyId = "toy_06",
                name = "Qatar Falcon Drone Explorer",
                quantity = 1,
                priceQar = 285.0,
                iconEmoji = "🚁"
              )
            ),
            totalAmount = 285.0,
            status = "shipping",
            timestamp = System.currentTimeMillis() - 3600000L * 2, // 2 hours ago
            customerName = "Fatima Al-Kuwari",
            phone = "+974 5512 8844",
            address = "Villa 14, Street 201, West Bay Lagoon",
            city = "Doha",
            paymentMethod = "Apple Pay  (Biometric)"
          ),
          FirestoreOrder(
            orderId = "ORD-88410",
            userId = "user_qatar_doha",
            items = listOf(
              FirestoreOrderItem(
                toyId = "toy_01",
                name = "Wonder Lego Lusail Stadium",
                quantity = 1,
                priceQar = 340.0,
                iconEmoji = "🏟️"
              ),
              FirestoreOrderItem(
                toyId = "toy_04",
                name = "Arabian Oryx Wooden Rocker",
                quantity = 1,
                priceQar = 220.0,
                iconEmoji = "🦌"
              )
            ),
            totalAmount = 560.0,
            status = "packaged",
            timestamp = System.currentTimeMillis() - 3600000L * 5, // 5 hours ago
            customerName = "Tariq Al-Sulaiti",
            phone = "+974 6690 1234",
            address = "Apartment 1204, Tower 18, Porto Arabia",
            city = "The Pearl",
            paymentMethod = "Credit Card (Visa/NAPS)"
          ),
          FirestoreOrder(
            orderId = "ORD-76192",
            userId = "user_doha_guest",
            items = listOf(
              FirestoreOrderItem(
                toyId = "toy_03",
                name = "RC Desert Dune Buggy 4x4",
                quantity = 2,
                priceQar = 398.0,
                iconEmoji = "🏎️"
              )
            ),
            totalAmount = 398.0,
            status = "pending",
            timestamp = System.currentTimeMillis() - 1800000L, // 30 mins ago
            customerName = "Jassim Al-Mannai",
            phone = "+974 3311 9900",
            address = "Villa 55, Zone 69, Lusail Fox Hills",
            city = "Lusail",
            paymentMethod = "QNB QPay (NAPS)"
          ),
          FirestoreOrder(
            orderId = "ORD-61208",
            userId = "user_qatar_doha",
            items = listOf(
              FirestoreOrderItem(
                toyId = "toy_02",
                name = "Wonder STEM Solar Robot 12-in-1",
                quantity = 1,
                priceQar = 145.0,
                iconEmoji = "🤖"
              )
            ),
            totalAmount = 145.0,
            status = "delivered",
            timestamp = System.currentTimeMillis() - 86400000L, // Yesterday
            customerName = "Noura Al-Khater",
            phone = "+974 5543 2198",
            address = "Compound 3, Villa 12, Al Waab",
            city = "Al Rayyan",
            paymentMethod = "Cash on Delivery"
          )
        )
      )
    }
  }

  /**
   * Save or upload an order to Firestore "orders" collection
   */
  fun saveOrder(order: FirestoreOrder, onComplete: ((Boolean) -> Unit)? = null) {
    // 1. Update local cache immediately
    val existingIndex = cachedOrders.indexOfFirst { it.orderId == order.orderId }
    if (existingIndex >= 0) {
      cachedOrders[existingIndex] = order
    } else {
      cachedOrders.add(0, order)
    }

    // 2. Upload to Firestore
    val firestore = firestoreInstance
    if (firestore != null) {
      try {
        firestore.collection("orders")
          .document(order.orderId)
          .set(order.toMap())
          .addOnSuccessListener {
            Log.d(tag, "Order ${order.orderId} saved to Firestore successfully")
            onComplete?.invoke(true)
          }
          .addOnFailureListener { err ->
            Log.w(tag, "Order saved locally; Firestore sync pending: ${err.message}")
            onComplete?.invoke(true)
          }
      } catch (e: Exception) {
        Log.w(tag, "Firestore write exception: ${e.message}")
        onComplete?.invoke(true)
      }
    } else {
      onComplete?.invoke(true)
    }
  }

  /**
   * Update order status: "pending", "packaged", "shipping", "delivered"
   */
  fun updateOrderStatus(orderId: String, newStatus: String, onComplete: ((Boolean) -> Unit)? = null) {
    // 1. Update local cache immediately
    val index = cachedOrders.indexOfFirst { it.orderId == orderId }
    if (index >= 0) {
      val updated = cachedOrders[index].copy(status = newStatus)
      cachedOrders[index] = updated
    }

    // 2. Push update to Firestore document
    val firestore = firestoreInstance
    if (firestore != null) {
      try {
        firestore.collection("orders")
          .document(orderId)
          .update(
            mapOf(
              "status" to newStatus,
              "updatedAt" to System.currentTimeMillis()
            )
          )
          .addOnSuccessListener {
            Log.d(tag, "Order $orderId status updated to $newStatus in Firestore")
            onComplete?.invoke(true)
          }
          .addOnFailureListener { err ->
            Log.w(tag, "Firestore status update failed: ${err.message}")
            onComplete?.invoke(true)
          }
      } catch (e: Exception) {
        Log.w(tag, "Firestore exception on update: ${e.message}")
        onComplete?.invoke(true)
      }
    } else {
      onComplete?.invoke(true)
    }
  }

  /**
   * Real-time Flow of all orders for Admin View and User Tracking
   */
  fun observeOrders(): Flow<List<FirestoreOrder>> = callbackFlow {
    // Emit initial cached list immediately
    trySend(cachedOrders.toList())

    var listener: ListenerRegistration? = null
    val firestore = firestoreInstance

    if (firestore != null) {
      try {
        listener = firestore.collection("orders")
          .orderBy("timestamp", Query.Direction.DESCENDING)
          .addSnapshotListener { snapshot, error ->
            if (error != null) {
              Log.w(tag, "Firestore snapshot listener error: ${error.message}")
              trySend(cachedOrders.toList())
              return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
              val firestoreList = snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                  FirestoreOrder.fromMap(doc.id, data)
                } else null
              }

              // Merge Firestore list into cached orders to keep freshest state
              firestoreList.forEach { fOrder ->
                val idx = cachedOrders.indexOfFirst { it.orderId == fOrder.orderId }
                if (idx >= 0) {
                  cachedOrders[idx] = fOrder
                } else {
                  cachedOrders.add(fOrder)
                }
              }
              cachedOrders.sortByDescending { it.timestamp }
              trySend(cachedOrders.toList())
            } else {
              trySend(cachedOrders.toList())
            }
          }
      } catch (e: Exception) {
        Log.w(tag, "Error setting up snapshot listener: ${e.message}")
        trySend(cachedOrders.toList())
      }
    }

    awaitClose {
      listener?.remove()
    }
  }

  fun observeProducts(): Flow<List<ToyItem>> = callbackFlow {
    trySend(cachedProducts.toList())

    var listener: ListenerRegistration? = null
    val firestore = firestoreInstance

    if (firestore != null) {
      try {
        listener = firestore.collection("products")
          .addSnapshotListener { snapshot, error ->
            if (error != null) {
              Log.w(tag, "Products snapshot error: ${error.message}")
              trySend(cachedProducts.toList())
              return@addSnapshotListener
            }

            if (snapshot != null) {
              val firestoreProducts = snapshot.documents.mapNotNull { doc ->
                val data = doc.data
                if (data != null) {
                  val name = data["name"] as? String ?: "Toy"
                  val category = data["category"] as? String ?: "Building Toys"
                  val ageRange = data["ageGroup"] as? String ?: "3-6 years"
                  val price = (data["price"] as? Number)?.toDouble() ?: 20.0
                  val stock = (data["stockQuantity"] as? Number)?.toInt() ?: 10
                  val desc = data["description"] as? String ?: ""

                  ToyItem(
                    id = doc.id,
                    name = name,
                    brand = "LEGO",
                    category = category,
                    ageRange = ageRange,
                    priceQar = price,
                    originalPriceQar = price * 1.2,
                    rating = 4.8,
                    reviewsCount = 15,
                    badge = "New",
                    description = desc.ifBlank { "High quality toy from Wonder Toy Store." },
                    features = listOf("Tested safe for kids", "Official Wonder Toy Product"),
                    inStock = stock > 0,
                    popularScore = 95,
                    iconEmoji = "🧸"
                  )
                } else null
              }

              if (firestoreProducts.isNotEmpty()) {
                firestoreProducts.forEach { fProduct ->
                  val idx = cachedProducts.indexOfFirst { it.id == fProduct.id }
                  if (idx >= 0) {
                    cachedProducts[idx] = fProduct
                  } else {
                    cachedProducts.add(0, fProduct)
                  }
                }
              }
              trySend(cachedProducts.toList())
            } else {
              trySend(cachedProducts.toList())
            }
          }
      } catch (e: Exception) {
        Log.w(tag, "Error setting up products listener: ${e.message}")
        trySend(cachedProducts.toList())
      }
    } else {
      trySend(cachedProducts.toList())
    }

    awaitClose {
      listener?.remove()
    }
  }

  fun getOrdersSync(): List<FirestoreOrder> = cachedOrders.toList()

  companion object {
    @Volatile
    private var INSTANCE: FirestoreOrderRepository? = null

    fun getInstance(context: Context): FirestoreOrderRepository {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: FirestoreOrderRepository(context).also { INSTANCE = it }
      }
    }
  }
}

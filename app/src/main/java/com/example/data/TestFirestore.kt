package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.FirestoreOrder
import com.example.model.FirestoreOrderItem

object TestFirestore {
    private const val TAG = "TestFirestore"

    fun verifyFirestoreConnection(context: Context) {
        val dummyOrder = FirestoreOrder(
            orderId = "TEST-ORDER-${System.currentTimeMillis()}",
            userId = "test_user_doha",
            items = listOf(
                FirestoreOrderItem(
                    toyId = "toy_01",
                    name = "Wonder Lego Lusail Stadium",
                    quantity = 1,
                    priceQar = 340.0,
                    iconEmoji = "🏟️"
                )
            ),
            totalAmount = 340.0,
            status = "pending",
            timestamp = System.currentTimeMillis(),
            customerName = "Test Customer",
            phone = "+974 5555 1234",
            address = "Doha Test Address",
            city = "Doha",
            paymentMethod = "Test Payment"
        )

        Log.d(TAG, "Attempting to verify Firestore connection by saving dummy order: ${dummyOrder.orderId}")
        
        val repository = FirestoreOrderRepository.getInstance(context.applicationContext)
        repository.saveOrder(dummyOrder) { success ->
            if (success) {
                Log.d(TAG, "Firestore connection verified successfully! Order ${dummyOrder.orderId} saved.")
            } else {
                Log.e(TAG, "Firestore connection verification failed for order ${dummyOrder.orderId}.")
            }
        }
    }
}

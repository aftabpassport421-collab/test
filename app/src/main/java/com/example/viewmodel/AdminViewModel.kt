package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirestoreOrderRepository
import com.example.model.FirestoreOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {

  private val firestoreRepo = FirestoreOrderRepository.getInstance(application)

  private val _orders = MutableStateFlow<List<FirestoreOrder>>(firestoreRepo.getOrdersSync())
  val orders: StateFlow<List<FirestoreOrder>> = _orders.asStateFlow()

  init {
    viewModelScope.launch {
      firestoreRepo.observeOrders().collect { updatedOrders ->
        _orders.value = updatedOrders
      }
    }
  }

  fun updateOrderStatus(orderId: String, newStatus: String) {
    firestoreRepo.updateOrderStatus(orderId, newStatus)
  }
}

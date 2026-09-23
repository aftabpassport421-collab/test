package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirestoreOrderRepository
import com.example.data.ToyCatalog
import com.example.model.FirestoreOrder
import com.example.model.ToyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {

  private val firestoreRepo = FirestoreOrderRepository.getInstance(application)

  private val _orders = MutableStateFlow<List<FirestoreOrder>>(firestoreRepo.getOrdersSync())
  val orders: StateFlow<List<FirestoreOrder>> = _orders.asStateFlow()

  private val _products = MutableStateFlow<List<ToyItem>>(ToyCatalog.toys.toMutableList())
  val products: StateFlow<List<ToyItem>> = _products.asStateFlow()

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

  fun addProduct(toy: ToyItem) {
    _products.update { current -> listOf(toy) + current }
  }

  fun deleteProduct(toyId: String) {
    _products.update { current -> current.filter { it.id != toyId } }
  }

  fun toggleProductStock(toyId: String) {
    _products.update { current ->
      current.map { if (it.id == toyId) it.copy(inStock = !it.inStock) else it }
    }
  }
}

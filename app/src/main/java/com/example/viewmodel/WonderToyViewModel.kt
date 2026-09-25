package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirestoreOrderRepository
import com.example.data.ToyCatalog
import com.example.data.UserSessionManager
import com.example.model.CartItem
import com.example.model.DeliveryType
import com.example.model.FirestoreOrder
import com.example.model.FirestoreOrderItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.StoreBranch
import com.example.model.ToyItem
import com.example.model.UserProfile
import com.example.ui.dialogs.AppRoleMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SortOrder(val label: String) {
  POPULAR("Most Popular"),
  PRICE_LOW_HIGH("Price: Low to High"),
  PRICE_HIGH_LOW("Price: High to Low"),
  RATING("Customer Rating")
}

data class WonderToyUiState(
  val toys: List<ToyItem> = ToyCatalog.toys,
  val filteredToys: List<ToyItem> = ToyCatalog.toys.sortedByDescending { it.popularScore },
  val selectedCategory: String = "All Categories",
  val selectedAgeGroup: String = "All Ages",
  val selectedBrand: String = "All Brands",
  val searchQuery: String = "",
  val sortOrder: SortOrder = SortOrder.POPULAR,
  val wishlistIds: Set<String> = setOf("toy_01", "toy_06"),
  val cartItems: List<CartItem> = listOf(
    CartItem(toy = ToyCatalog.toys[0], quantity = 1, giftWrap = true),
    CartItem(toy = ToyCatalog.toys[3], quantity = 1, giftWrap = false)
  ),
  val promoCode: String = "WONDER10",
  val promoDiscountPercent: Double = 0.10,
  val promoMessage: String? = "10% Wonder discount applied!",
  val deliveryType: DeliveryType = DeliveryType.SAME_DAY_DOHA,
  val selectedStore: StoreBranch = ToyCatalog.branches.first(),
  val orders: List<Order> = listOf(
    Order(
      id = "WT-90214",
      items = listOf(CartItem(toy = ToyCatalog.toys[5], quantity = 1)),
      subtotalQar = 99.0,
      deliveryFeeQar = 25.0,
      discountQar = 0.0,
      totalQar = 124.0,
      deliveryType = DeliveryType.SAME_DAY_DOHA,
      customerName = "Fatima Al-Kuwari",
      phone = "+974 5512 8844",
      addressOrStore = "Villa 14, West Bay Lagoon",
      city = "Doha",
      paymentMethod = "Apple Pay  (Biometric)",
      orderDateFormatted = "Yesterday, 3:45 PM",
      status = OrderStatus.DELIVERED,
      estimatedArrival = "Delivered to reception",
      transactionRef = "APL-77412",
      isPaid = true
    )
  ),
  val selectedToyDetail: ToyItem? = null,
  val isCheckoutVisible: Boolean = false,
  val completedOrder: Order? = null,
  val firestoreOrders: List<FirestoreOrder> = emptyList(),
  val selectedTrackingOrder: FirestoreOrder? = null,
  val currentUserId: String = "user_qatar_doha",
  val userProfile: UserProfile = UserProfile(),
  val isAuthDialogVisible: Boolean = false
) {
  val cartSubtotalQar: Double
    get() = cartItems.sumOf { it.itemTotalQar }

  val discountAmountQar: Double
    get() = cartSubtotalQar * promoDiscountPercent

  val deliveryFeeQar: Double
    get() = if (cartItems.isEmpty()) 0.0 else deliveryType.priceQar

  val finalTotalQar: Double
    get() = (cartSubtotalQar - discountAmountQar + deliveryFeeQar).coerceAtLeast(0.0)

  val totalCartItemCount: Int
    get() = cartItems.sumOf { it.quantity }
}

class WonderToyViewModel(application: Application) : AndroidViewModel(application) {

  private val firestoreRepo = FirestoreOrderRepository.getInstance(application)
  private val userSessionManager = UserSessionManager(application)

  private val _uiState = MutableStateFlow(
    WonderToyUiState(
      userProfile = userSessionManager.getProfile(),
      firestoreOrders = firestoreRepo.getOrdersSync()
    )
  )
  val uiState: StateFlow<WonderToyUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      firestoreRepo.observeOrders().collect { updatedOrders ->
        _uiState.update { state ->
          state.copy(
            firestoreOrders = updatedOrders,
            selectedTrackingOrder = if (state.selectedTrackingOrder != null) {
              updatedOrders.find { it.orderId == state.selectedTrackingOrder.orderId } ?: state.selectedTrackingOrder
            } else null
          )
        }
      }
    }

    viewModelScope.launch {
      firestoreRepo.observeProducts().collect { firestoreProducts ->
        _uiState.update { state ->
          val combinedToys = if (firestoreProducts.isNotEmpty()) {
            val existingIds = firestoreProducts.map { it.id }.toSet()
            firestoreProducts + ToyCatalog.toys.filter { it.id !in existingIds }
          } else {
            ToyCatalog.toys
          }
          state.copy(
            toys = combinedToys,
            filteredToys = filterAndSortToys(
              combinedToys,
              state.selectedCategory,
              state.selectedAgeGroup,
              state.selectedBrand,
              state.searchQuery,
              state.sortOrder
            )
          )
        }
      }
    }
  }

  private fun filterAndSortToys(
    toys: List<ToyItem>,
    cat: String,
    age: String,
    brand: String,
    query: String,
    sort: SortOrder
  ): List<ToyItem> {
    val filtered = toys.filter { toy ->
      val matchesCategory = cat == "All Categories" || toy.category == cat
      val matchesAge = age == "All Ages" || toy.ageRange == age
      val matchesBrand = brand == "All Brands" || toy.brand.equals(brand, ignoreCase = true)
      val matchesQuery = query.isBlank() ||
        toy.name.contains(query, ignoreCase = true) ||
        toy.brand.contains(query, ignoreCase = true) ||
        toy.category.contains(query, ignoreCase = true) ||
        toy.description.contains(query, ignoreCase = true)
      matchesCategory && matchesAge && matchesBrand && matchesQuery
    }
    return when (sort) {
      SortOrder.POPULAR -> filtered.sortedByDescending { it.popularScore }
      SortOrder.PRICE_LOW_HIGH -> filtered.sortedBy { it.priceQar }
      SortOrder.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.priceQar }
      SortOrder.RATING -> filtered.sortedByDescending { it.rating }
    }
  }

  fun selectCategory(category: String) {
    _uiState.update { current ->
      current.copy(
        selectedCategory = category,
        filteredToys = filterAndSortToys(
          current.toys,
          category,
          current.selectedAgeGroup,
          current.selectedBrand,
          current.searchQuery,
          current.sortOrder
        )
      )
    }
  }

  fun selectAgeGroup(ageGroup: String) {
    _uiState.update { current ->
      current.copy(
        selectedAgeGroup = ageGroup,
        filteredToys = filterAndSortToys(
          current.toys,
          current.selectedCategory,
          ageGroup,
          current.selectedBrand,
          current.searchQuery,
          current.sortOrder
        )
      )
    }
  }

  fun selectBrand(brand: String) {
    _uiState.update { current ->
      current.copy(
        selectedBrand = brand,
        filteredToys = filterAndSortToys(
          current.toys,
          current.selectedCategory,
          current.selectedAgeGroup,
          brand,
          current.searchQuery,
          current.sortOrder
        )
      )
    }
  }

  fun setSearchQuery(query: String) {
    _uiState.update { current ->
      current.copy(
        searchQuery = query,
        filteredToys = filterAndSortToys(
          current.toys,
          current.selectedCategory,
          current.selectedAgeGroup,
          current.selectedBrand,
          query,
          current.sortOrder
        )
      )
    }
  }

  fun setSortOrder(sortOrder: SortOrder) {
    _uiState.update { current ->
      current.copy(
        sortOrder = sortOrder,
        filteredToys = filterAndSortToys(
          current.toys,
          current.selectedCategory,
          current.selectedAgeGroup,
          current.selectedBrand,
          current.searchQuery,
          sortOrder
        )
      )
    }
  }

  fun toggleWishlist(toyId: String) {
    _uiState.update { current ->
      val updated = current.wishlistIds.toMutableSet()
      if (updated.contains(toyId)) {
        updated.remove(toyId)
      } else {
        updated.add(toyId)
      }
      current.copy(wishlistIds = updated)
    }
  }

  fun addToCart(toy: ToyItem, giftWrap: Boolean = false) {
    _uiState.update { current ->
      val mutable = current.cartItems.toMutableList()
      val index = mutable.indexOfFirst { it.toy.id == toy.id }
      if (index >= 0) {
        val existing = mutable[index]
        mutable[index] = existing.copy(quantity = existing.quantity + 1)
      } else {
        mutable.add(CartItem(toy = toy, quantity = 1, giftWrap = giftWrap))
      }
      current.copy(cartItems = mutable)
    }
  }

  fun updateCartQuantity(toyId: String, newQty: Int) {
    if (newQty <= 0) {
      removeFromCart(toyId)
      return
    }
    _uiState.update { current ->
      val mutable = current.cartItems.toMutableList()
      val index = mutable.indexOfFirst { it.toy.id == toyId }
      if (index >= 0) {
        mutable[index] = mutable[index].copy(quantity = newQty)
      }
      current.copy(cartItems = mutable)
    }
  }

  fun toggleGiftWrap(toyId: String) {
    _uiState.update { current ->
      val mutable = current.cartItems.toMutableList()
      val index = mutable.indexOfFirst { it.toy.id == toyId }
      if (index >= 0) {
        mutable[index] = mutable[index].copy(giftWrap = !mutable[index].giftWrap)
      }
      current.copy(cartItems = mutable)
    }
  }

  fun removeFromCart(toyId: String) {
    _uiState.update { current ->
      current.copy(cartItems = current.cartItems.filter { it.toy.id != toyId })
    }
  }

  fun clearCart() {
    _uiState.update { current -> current.copy(cartItems = emptyList()) }
  }

  fun applyPromo(code: String) {
    val trimmed = code.trim().uppercase(Locale.ROOT)
    val (discountPercent, message) = when (trimmed) {
      "WONDER10" -> Pair(0.10, "10% Wonder discount applied!")
      "QATAR" -> Pair(0.15, "15% Qatar National celebration discount!")
      "TOYVIP" -> Pair(0.20, "20% VIP Toy Master discount applied!")
      "" -> Pair(0.0, null)
      else -> Pair(0.0, "Invalid promo code. Try WONDER10 or QATAR")
    }
    _uiState.update { current ->
      current.copy(
        promoCode = trimmed,
        promoDiscountPercent = discountPercent,
        promoMessage = message
      )
    }
  }

  fun setDeliveryType(type: DeliveryType) {
    _uiState.update { current -> current.copy(deliveryType = type) }
  }

  fun setSelectedStore(store: StoreBranch) {
    _uiState.update { current -> current.copy(selectedStore = store) }
  }

  fun showToyDetail(toy: ToyItem?) {
    _uiState.update { current -> current.copy(selectedToyDetail = toy) }
  }

  fun showCheckout(show: Boolean) {
    _uiState.update { current -> current.copy(isCheckoutVisible = show) }
  }

  fun dismissOrderSuccess() {
    _uiState.update { current -> current.copy(completedOrder = null) }
  }

  fun placeOrder(
    customerName: String,
    phone: String,
    addressOrArea: String,
    city: String,
    paymentMethod: String
  ): Order {
    val current = _uiState.value
    val currentCart = current.cartItems
    val subtotal = current.cartSubtotalQar
    val discount = current.discountAmountQar
    val deliveryFee = current.deliveryFeeQar
    val total = current.finalTotalQar

    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val currentDateStr = dateFormat.format(Date())
    val randomId = "WT-${(10000..99999).random()}"

    val locationText = "$addressOrArea, $city"

    val isPaid = !paymentMethod.contains("Cash", ignoreCase = true)
    val txnCode = when {
      paymentMethod.contains("Apple", ignoreCase = true) -> "APL-${(10000..99999).random()}"
      paymentMethod.contains("QPay", ignoreCase = true) -> "QPAY-${(10000..99999).random()}"
      paymentMethod.contains("Card", ignoreCase = true) -> "QNB-${(100000..999999).random()}"
      else -> "COD-${(1000..9999).random()}"
    }

    val order = Order(
      id = randomId,
      items = currentCart,
      subtotalQar = subtotal,
      deliveryFeeQar = deliveryFee,
      discountQar = discount,
      totalQar = total,
      deliveryType = current.deliveryType,
      customerName = customerName,
      phone = phone,
      addressOrStore = locationText,
      city = city,
      paymentMethod = paymentMethod,
      orderDateFormatted = currentDateStr,
      status = OrderStatus.CONFIRMED,
      estimatedArrival = current.deliveryType.eta,
      transactionRef = txnCode,
      isPaid = isPaid
    )

    // Also persist to Firebase Firestore collection "orders"
    val firestoreOrder = FirestoreOrder(
      orderId = randomId,
      userId = current.currentUserId,
      items = currentCart.map { cItem ->
        FirestoreOrderItem(
          toyId = cItem.toy.id,
          name = cItem.toy.name,
          quantity = cItem.quantity,
          priceQar = cItem.toy.priceQar,
          iconEmoji = cItem.toy.iconEmoji
        )
      },
      totalAmount = total,
      status = "pending",
      timestamp = System.currentTimeMillis(),
      customerName = customerName,
      phone = phone,
      address = addressOrArea,
      city = city,
      paymentMethod = paymentMethod
    )
    firestoreRepo.saveOrder(firestoreOrder)

    _uiState.update { state ->
      state.copy(
        orders = listOf(order) + state.orders,
        cartItems = emptyList(),
        isCheckoutVisible = false,
        completedOrder = order
      )
    }

    return order
  }

  fun updateOrderStatus(orderId: String, newStatus: String) {
    firestoreRepo.updateOrderStatus(orderId, newStatus)
  }

  fun addProduct(toy: ToyItem) {
    _uiState.update { state ->
      state.copy(toys = listOf(toy) + state.toys, filteredToys = listOf(toy) + state.filteredToys)
    }
  }

  fun deleteProduct(toyId: String) {
    _uiState.update { state ->
      state.copy(
        toys = state.toys.filter { it.id != toyId },
        filteredToys = state.filteredToys.filter { it.id != toyId }
      )
    }
  }

  fun toggleProductStock(toyId: String) {
    _uiState.update { state ->
      val updatedToys = state.toys.map { if (it.id == toyId) it.copy(inStock = !it.inStock) else it }
      state.copy(toys = updatedToys, filteredToys = updatedToys)
    }
  }

  fun showOrderTracking(order: FirestoreOrder?) {
    _uiState.update { it.copy(selectedTrackingOrder = order) }
  }

  fun showAuthDialog(show: Boolean) {
    _uiState.update { it.copy(isAuthDialogVisible = show) }
  }

  fun updateUserProfile(profile: UserProfile) {
    userSessionManager.saveProfile(profile)
    _uiState.update {
      it.copy(
        userProfile = profile,
        currentUserId = profile.id,
        isAuthDialogVisible = false
      )
    }
  }

  fun logoutUser() {
    userSessionManager.clearSession()
    val guest = UserProfile(
      id = "user_guest_${System.currentTimeMillis() % 10000}",
      name = "Guest User",
      phone = "+974 ",
      email = "",
      isRegistered = false,
      rewardsPoints = 0
    )
    _uiState.update { it.copy(userProfile = guest, isAuthDialogVisible = true) }
  }
}

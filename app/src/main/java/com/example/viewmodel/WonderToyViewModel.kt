package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ToyCatalog
import com.example.model.CartItem
import com.example.model.DeliveryType
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.StoreBranch
import com.example.model.ToyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

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
  val orders: List<Order> = emptyList(),
  val selectedToyDetail: ToyItem? = null,
  val isCheckoutVisible: Boolean = false,
  val completedOrder: Order? = null
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

class WonderToyViewModel : ViewModel() {

  private val _toys = MutableStateFlow(ToyCatalog.toys)
  private val _selectedCategory = MutableStateFlow("All Categories")
  private val _selectedAgeGroup = MutableStateFlow("All Ages")
  private val _selectedBrand = MutableStateFlow("All Brands")
  private val _searchQuery = MutableStateFlow("")
  private val _sortOrder = MutableStateFlow(SortOrder.POPULAR)
  private val _wishlistIds = MutableStateFlow(setOf("toy_01", "toy_06"))
  private val _cartItems = MutableStateFlow<List<CartItem>>(
    listOf(
      CartItem(toy = ToyCatalog.toys[0], quantity = 1, giftWrap = true),
      CartItem(toy = ToyCatalog.toys[3], quantity = 1, giftWrap = false)
    )
  )
  private val _promoCode = MutableStateFlow("WONDER10")
  private val _promoDiscountPercent = MutableStateFlow(0.10)
  private val _promoMessage = MutableStateFlow<String?>("10% Wonder discount applied!")
  private val _deliveryType = MutableStateFlow(DeliveryType.SAME_DAY_DOHA)
  private val _selectedStore = MutableStateFlow(ToyCatalog.branches.first())
  private val _orders = MutableStateFlow<List<Order>>(
    listOf(
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
        paymentMethod = "Apple Pay",
        orderDateFormatted = "Yesterday, 3:45 PM",
        status = OrderStatus.DELIVERED,
        estimatedArrival = "Delivered to reception"
      )
    )
  )
  private val _selectedToyDetail = MutableStateFlow<ToyItem?>(null)
  private val _isCheckoutVisible = MutableStateFlow(false)
  private val _completedOrder = MutableStateFlow<Order?>(null)

  val uiState: StateFlow<WonderToyUiState> = combine(
    combine(_toys, _selectedCategory, _selectedAgeGroup, _selectedBrand, _searchQuery) { toys, cat, age, brand, query ->
      toys.filter { toy ->
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
    },
    _sortOrder,
    _wishlistIds,
    _cartItems,
    combine(_promoCode, _promoDiscountPercent, _promoMessage, _deliveryType, _selectedStore) { pCode, pDisc, pMsg, deliv, store ->
      Quint(pCode, pDisc, pMsg, deliv, store)
    }
  ) { filtered, sort, wishlist, cart, quint ->
    val sorted = when (sort) {
      SortOrder.POPULAR -> filtered.sortedByDescending { it.popularScore }
      SortOrder.PRICE_LOW_HIGH -> filtered.sortedBy { it.priceQar }
      SortOrder.PRICE_HIGH_LOW -> filtered.sortedByDescending { it.priceQar }
      SortOrder.RATING -> filtered.sortedByDescending { it.rating }
    }

    WonderToyUiState(
      toys = _toys.value,
      filteredToys = sorted,
      selectedCategory = _selectedCategory.value,
      selectedAgeGroup = _selectedAgeGroup.value,
      selectedBrand = _selectedBrand.value,
      searchQuery = _searchQuery.value,
      sortOrder = sort,
      wishlistIds = wishlist,
      cartItems = cart,
      promoCode = quint.first,
      promoDiscountPercent = quint.second,
      promoMessage = quint.third,
      deliveryType = quint.fourth,
      selectedStore = quint.fifth,
      orders = _orders.value,
      selectedToyDetail = _selectedToyDetail.value,
      isCheckoutVisible = _isCheckoutVisible.value,
      completedOrder = _completedOrder.value
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = WonderToyUiState()
  )

  fun selectCategory(category: String) {
    _selectedCategory.value = category
  }

  fun selectAgeGroup(ageGroup: String) {
    _selectedAgeGroup.value = ageGroup
  }

  fun selectBrand(brand: String) {
    _selectedBrand.value = brand
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSortOrder(sortOrder: SortOrder) {
    _sortOrder.value = sortOrder
  }

  fun toggleWishlist(toyId: String) {
    val current = _wishlistIds.value.toMutableSet()
    if (current.contains(toyId)) {
      current.remove(toyId)
    } else {
      current.add(toyId)
    }
    _wishlistIds.value = current
  }

  fun addToCart(toy: ToyItem, giftWrap: Boolean = false) {
    val current = _cartItems.value.toMutableList()
    val index = current.indexOfFirst { it.toy.id == toy.id }
    if (index >= 0) {
      val existing = current[index]
      current[index] = existing.copy(quantity = existing.quantity + 1)
    } else {
      current.add(CartItem(toy = toy, quantity = 1, giftWrap = giftWrap))
    }
    _cartItems.value = current
  }

  fun updateCartQuantity(toyId: String, newQty: Int) {
    if (newQty <= 0) {
      removeFromCart(toyId)
      return
    }
    val current = _cartItems.value.toMutableList()
    val index = current.indexOfFirst { it.toy.id == toyId }
    if (index >= 0) {
      current[index] = current[index].copy(quantity = newQty)
      _cartItems.value = current
    }
  }

  fun toggleGiftWrap(toyId: String) {
    val current = _cartItems.value.toMutableList()
    val index = current.indexOfFirst { it.toy.id == toyId }
    if (index >= 0) {
      current[index] = current[index].copy(giftWrap = !current[index].giftWrap)
      _cartItems.value = current
    }
  }

  fun removeFromCart(toyId: String) {
    _cartItems.value = _cartItems.value.filter { it.toy.id != toyId }
  }

  fun clearCart() {
    _cartItems.value = emptyList()
  }

  fun applyPromo(code: String) {
    val trimmed = code.trim().uppercase(Locale.ROOT)
    _promoCode.value = trimmed
    when (trimmed) {
      "WONDER10" -> {
        _promoDiscountPercent.value = 0.10
        _promoMessage.value = "10% Wonder discount applied!"
      }
      "QATAR" -> {
        _promoDiscountPercent.value = 0.15
        _promoMessage.value = "15% Qatar National celebration discount!"
      }
      "TOYVIP" -> {
        _promoDiscountPercent.value = 0.20
        _promoMessage.value = "20% VIP Toy Master discount applied!"
      }
      "" -> {
        _promoDiscountPercent.value = 0.0
        _promoMessage.value = null
      }
      else -> {
        _promoDiscountPercent.value = 0.0
        _promoMessage.value = "Invalid promo code. Try WONDER10 or QATAR"
      }
    }
  }

  fun setDeliveryType(type: DeliveryType) {
    _deliveryType.value = type
  }

  fun setSelectedStore(store: StoreBranch) {
    _selectedStore.value = store
  }

  fun showToyDetail(toy: ToyItem?) {
    _selectedToyDetail.value = toy
  }

  fun showCheckout(show: Boolean) {
    _isCheckoutVisible.value = show
  }

  fun dismissOrderSuccess() {
    _completedOrder.value = null
  }

  fun placeOrder(
    customerName: String,
    phone: String,
    addressOrArea: String,
    city: String,
    paymentMethod: String
  ): Order {
    val currentCart = _cartItems.value
    val subtotal = currentCart.sumOf { it.itemTotalQar }
    val discount = subtotal * _promoDiscountPercent.value
    val deliveryFee = if (currentCart.isEmpty()) 0.0 else _deliveryType.value.priceQar
    val total = (subtotal - discount + deliveryFee).coerceAtLeast(0.0)

    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val currentDateStr = dateFormat.format(Date())
    val randomId = "WT-${(10000..99999).random()}"

    val locationText = "$addressOrArea, $city"

    val order = Order(
      id = randomId,
      items = currentCart,
      subtotalQar = subtotal,
      deliveryFeeQar = deliveryFee,
      discountQar = discount,
      totalQar = total,
      deliveryType = _deliveryType.value,
      customerName = customerName,
      phone = phone,
      addressOrStore = locationText,
      city = city,
      paymentMethod = paymentMethod,
      orderDateFormatted = currentDateStr,
      status = OrderStatus.CONFIRMED,
      estimatedArrival = _deliveryType.value.eta
    )

    _orders.value = listOf(order) + _orders.value
    _cartItems.value = emptyList()
    _isCheckoutVisible.value = false
    _completedOrder.value = order
    return order
  }
}

data class Quint<A, B, C, D, E>(
  val first: A,
  val second: B,
  val third: C,
  val fourth: D,
  val fifth: E
)

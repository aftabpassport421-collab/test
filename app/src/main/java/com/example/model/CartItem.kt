package com.example.model

data class CartItem(
  val toy: ToyItem,
  val quantity: Int = 1,
  val giftWrap: Boolean = false,
) {
  val itemTotalQar: Double
    get() = (toy.priceQar * quantity) + (if (giftWrap) 10.0 * quantity else 0.0)
}

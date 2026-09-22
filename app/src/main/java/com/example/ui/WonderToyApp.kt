package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.DeliveryType
import com.example.model.StoreBranch
import com.example.model.ToyItem
import com.example.ui.components.WonderToyTopBar
import com.example.ui.dialogs.CheckoutDialog
import com.example.ui.dialogs.OrderSuccessDialog
import com.example.ui.dialogs.ToyDetailDialog
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SortOrder
import com.example.viewmodel.WonderToyUiState
import com.example.viewmodel.WonderToyViewModel

enum class BottomTab(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
  CATALOG("Toys", Icons.Filled.Category, Icons.Outlined.Category),
  WISHLIST("Wishlist", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
  CART("Cart", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
  ORDERS("Orders", Icons.Filled.LocalShipping, Icons.Outlined.LocalShipping)
}

@Composable
fun WonderToyApp(
  viewModel: WonderToyViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()

  WonderToyAppContent(
    uiState = uiState,
    onSearchQueryChange = { query -> viewModel.setSearchQuery(query) },
    onSelectCategory = { cat -> viewModel.selectCategory(cat) },
    onSelectAgeGroup = { age -> viewModel.selectAgeGroup(age) },
    onSelectBrand = { brand -> viewModel.selectBrand(brand) },
    onSetSortOrder = { sort -> viewModel.setSortOrder(sort) },
    onClearFilters = {
      viewModel.selectCategory("All Categories")
      viewModel.selectAgeGroup("All Ages")
      viewModel.selectBrand("All Brands")
      viewModel.setSearchQuery("")
    },
    onToggleWishlist = { id -> viewModel.toggleWishlist(id) },
    onAddToCart = { toy, giftWrap -> viewModel.addToCart(toy, giftWrap) },
    onUpdateCartQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
    onToggleGiftWrap = { id -> viewModel.toggleGiftWrap(id) },
    onRemoveFromCart = { id -> viewModel.removeFromCart(id) },
    onApplyPromo = { code -> viewModel.applyPromo(code) },
    onSetDeliveryType = { type -> viewModel.setDeliveryType(type) },
    onSetSelectedStore = { store -> viewModel.setSelectedStore(store) },
    onShowToyDetail = { toy -> viewModel.showToyDetail(toy) },
    onShowCheckout = { show -> viewModel.showCheckout(show) },
    onDismissOrderSuccess = { viewModel.dismissOrderSuccess() },
    onPlaceOrder = { name, phone, address, city, payment ->
      viewModel.placeOrder(name, phone, address, city, payment)
    },
    modifier = modifier
  )
}

@Composable
fun WonderToyAppContent(
  uiState: WonderToyUiState,
  onSearchQueryChange: (String) -> Unit = {},
  onSelectCategory: (String) -> Unit = {},
  onSelectAgeGroup: (String) -> Unit = {},
  onSelectBrand: (String) -> Unit = {},
  onSetSortOrder: (SortOrder) -> Unit = {},
  onClearFilters: () -> Unit = {},
  onToggleWishlist: (String) -> Unit = {},
  onAddToCart: (ToyItem, Boolean) -> Unit = { _, _ -> },
  onUpdateCartQuantity: (String, Int) -> Unit = { _, _ -> },
  onToggleGiftWrap: (String) -> Unit = {},
  onRemoveFromCart: (String) -> Unit = {},
  onApplyPromo: (String) -> Unit = {},
  onSetDeliveryType: (DeliveryType) -> Unit = {},
  onSetSelectedStore: (StoreBranch) -> Unit = {},
  onShowToyDetail: (ToyItem?) -> Unit = {},
  onShowCheckout: (Boolean) -> Unit = {},
  onDismissOrderSuccess: () -> Unit = {},
  onPlaceOrder: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
  modifier: Modifier = Modifier
) {
  var currentTab by rememberSaveable { mutableStateOf(BottomTab.HOME) }

  Scaffold(
    topBar = {
      WonderToyTopBar(
        searchQuery = uiState.searchQuery,
        onSearchQueryChange = {
          onSearchQueryChange(it)
          if (it.isNotEmpty() && currentTab != BottomTab.CATALOG) {
            currentTab = BottomTab.CATALOG
          }
        },
        cartItemCount = uiState.totalCartItemCount,
        wishlistCount = uiState.wishlistIds.size,
        onCartClick = { currentTab = BottomTab.CART },
        onWishlistClick = { currentTab = BottomTab.WISHLIST },
        onLocationClick = null
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.testTag("bottom_nav_bar")
      ) {
        BottomTab.values().forEach { tab ->
          val isSelected = currentTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { currentTab = tab },
            icon = {
              if (tab == BottomTab.CART && uiState.totalCartItemCount > 0) {
                BadgedBox(
                  badge = {
                    Badge(
                      containerColor = IndigoPrimary,
                      contentColor = Color.White
                    ) {
                      Text("${uiState.totalCartItemCount}", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                ) {
                  Icon(
                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                    contentDescription = tab.label
                  )
                }
              } else if (tab == BottomTab.WISHLIST && uiState.wishlistIds.isNotEmpty()) {
                BadgedBox(
                  badge = {
                    Badge(
                      containerColor = CoralSecondary,
                      contentColor = Color.White
                    ) {
                      Text("${uiState.wishlistIds.size}", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                ) {
                  Icon(
                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                    contentDescription = tab.label
                  )
                }
              } else {
                Icon(
                  imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                  contentDescription = tab.label
                )
              }
            },
            label = {
              Text(
                text = tab.label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = IndigoPrimary,
              selectedTextColor = IndigoPrimary,
              indicatorColor = IndigoPrimary.copy(alpha = 0.15f),
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
          )
        }
      }
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        BottomTab.HOME -> {
          HomeScreen(
            toys = uiState.toys,
            wishlistIds = uiState.wishlistIds,
            selectedAgeGroup = uiState.selectedAgeGroup,
            onAgeGroupSelect = {
              onSelectAgeGroup(it)
              currentTab = BottomTab.CATALOG
            },
            onBrandSelect = {
              onSelectBrand(it)
              currentTab = BottomTab.CATALOG
            },
            onToyClick = { onShowToyDetail(it) },
            onWishlistToggle = { onToggleWishlist(it) },
            onAddToCart = { onAddToCart(it, false) },
            onExploreAllClick = { currentTab = BottomTab.CATALOG }
          )
        }
        BottomTab.CATALOG -> {
          CatalogScreen(
            toys = uiState.filteredToys,
            wishlistIds = uiState.wishlistIds,
            selectedCategory = uiState.selectedCategory,
            selectedAgeGroup = uiState.selectedAgeGroup,
            selectedBrand = uiState.selectedBrand,
            sortOrder = uiState.sortOrder,
            onCategorySelect = { onSelectCategory(it) },
            onAgeSelect = { onSelectAgeGroup(it) },
            onBrandSelect = { onSelectBrand(it) },
            onSortSelect = { onSetSortOrder(it) },
            onClearFilters = { onClearFilters() },
            onToyClick = { onShowToyDetail(it) },
            onWishlistToggle = { onToggleWishlist(it) },
            onAddToCart = { onAddToCart(it, false) }
          )
        }
        BottomTab.WISHLIST -> {
          val wishlistItems = uiState.toys.filter { uiState.wishlistIds.contains(it.id) }
          WishlistScreen(
            wishlistToys = wishlistItems,
            onToyClick = { onShowToyDetail(it) },
            onWishlistToggle = { onToggleWishlist(it) },
            onAddToCart = { onAddToCart(it, false) },
            onBrowseClick = { currentTab = BottomTab.CATALOG }
          )
        }
        BottomTab.CART -> {
          CartScreen(
            cartItems = uiState.cartItems,
            subtotalQar = uiState.cartSubtotalQar,
            deliveryFeeQar = uiState.deliveryFeeQar,
            discountQar = uiState.discountAmountQar,
            finalTotalQar = uiState.finalTotalQar,
            promoCode = uiState.promoCode,
            promoMessage = uiState.promoMessage,
            deliveryType = uiState.deliveryType,
            onQuantityChange = { toyId, qty -> onUpdateCartQuantity(toyId, qty) },
            onGiftWrapToggle = { toyId -> onToggleGiftWrap(toyId) },
            onRemoveItem = { toyId -> onRemoveFromCart(toyId) },
            onApplyPromo = { code -> onApplyPromo(code) },
            onDeliveryTypeChange = { type -> onSetDeliveryType(type) },
            onProceedToCheckout = { onShowCheckout(true) },
            onBrowseClick = { currentTab = BottomTab.CATALOG }
          )
        }
        BottomTab.ORDERS -> {
          OrdersScreen(
            orders = uiState.orders,
            onShopToysClick = { currentTab = BottomTab.CATALOG }
          )
        }
      }

      // Dialog 1: Toy Detail Bottom Sheet
      uiState.selectedToyDetail?.let { toy ->
        ToyDetailDialog(
          toy = toy,
          isWishlisted = uiState.wishlistIds.contains(toy.id),
          onDismiss = { onShowToyDetail(null) },
          onWishlistToggle = { onToggleWishlist(toy.id) },
          onAddToCart = { giftWrap ->
            onAddToCart(toy, giftWrap)
          },
          onDirectBuy = { giftWrap ->
            onAddToCart(toy, giftWrap)
            onShowCheckout(true)
          }
        )
      }

      // Dialog 2: Checkout Bottom Sheet
      if (uiState.isCheckoutVisible) {
        CheckoutDialog(
          subtotalQar = uiState.cartSubtotalQar,
          deliveryFeeQar = uiState.deliveryFeeQar,
          discountQar = uiState.discountAmountQar,
          finalTotalQar = uiState.finalTotalQar,
          currentDeliveryType = uiState.deliveryType,
          onDeliveryTypeChange = { onSetDeliveryType(it) },
          onDismiss = { onShowCheckout(false) },
          onConfirmOrder = { name, phone, address, city, payment ->
            onPlaceOrder(name, phone, address, city, payment)
          }
        )
      }

      // Dialog 3: Order Success Celebration Dialog
      uiState.completedOrder?.let { order ->
        OrderSuccessDialog(
          order = order,
          onDismiss = { onDismissOrderSuccess() },
          onViewOrders = {
            onDismissOrderSuccess()
            currentTab = BottomTab.ORDERS
          }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WonderToyAppPreview() {
  MyApplicationTheme {
    WonderToyAppContent(uiState = WonderToyUiState())
  }
}

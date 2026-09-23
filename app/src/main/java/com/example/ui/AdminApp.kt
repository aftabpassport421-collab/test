package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.FirestoreOrder
import com.example.model.ToyItem
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent
import com.example.ui.theme.StarGold
import com.example.viewmodel.AdminViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminApp(
  viewModel: AdminViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val orders by viewModel.orders.collectAsState()
  val products by viewModel.products.collectAsState()

  var selectedTab by remember { mutableStateOf("orders") } // "orders" or "products"
  var selectedOrderForTracking by remember { mutableStateOf<FirestoreOrder?>(null) }
  var selectedFilter by remember { mutableStateOf("all") }
  var searchQuery by remember { mutableStateOf("") }
  var isAddProductDialogOpen by remember { mutableStateOf(false) }

  if (selectedOrderForTracking != null) {
    OrderTrackingScreen(
      order = selectedOrderForTracking!!,
      onBackClick = { selectedOrderForTracking = null },
      onUpdateStatusTest = { newStatus ->
        viewModel.updateOrderStatus(selectedOrderForTracking!!.orderId, newStatus)
      },
      modifier = modifier.fillMaxSize()
    )
    return
  }

  val filteredOrders = remember(orders, selectedFilter, searchQuery) {
    orders.filter { order ->
      val matchesFilter = if (selectedFilter == "all") true else order.status.equals(selectedFilter, ignoreCase = true)
      val matchesSearch = if (searchQuery.isBlank()) true else {
        order.orderId.contains(searchQuery, ignoreCase = true) ||
          order.customerName.contains(searchQuery, ignoreCase = true) ||
          order.phone.contains(searchQuery, ignoreCase = true) ||
          order.city.contains(searchQuery, ignoreCase = true) ||
          order.items.any { it.name.contains(searchQuery, ignoreCase = true) }
      }
      matchesFilter && matchesSearch
    }
  }

  val filteredProducts = remember(products, searchQuery) {
    if (searchQuery.isBlank()) products else {
      products.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
          it.brand.contains(searchQuery, ignoreCase = true) ||
          it.category.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  val pendingCount = orders.count { it.status.equals("pending", ignoreCase = true) }
  val packagedCount = orders.count { it.status.equals("packaged", ignoreCase = true) }
  val shippingCount = orders.count { it.status.equals("shipping", ignoreCase = true) }
  val deliveredCount = orders.count { it.status.equals("delivered", ignoreCase = true) }
  val totalRevenue = orders.sumOf { it.totalAmount }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Wonder Admin Hub",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "🛠️ 🇶🇦", fontSize = 15.sp)
            }
            Text(
              text = "Orders & Inventory Control Center",
              fontSize = 11.sp,
              color = MintAccent,
              fontWeight = FontWeight.SemiBold
            )
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MintAccent.copy(alpha = 0.15f),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(MintAccent)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Live Sync Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MintAccent)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    floatingActionButton = {
      if (selectedTab == "products") {
        FloatingActionButton(
          onClick = { isAddProductDialogOpen = true },
          containerColor = IndigoPrimary,
          contentColor = Color.White,
          modifier = Modifier.testTag("admin_add_product_fab")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Toy Product", fontWeight = FontWeight.Bold)
          }
        }
      }
    },
    modifier = modifier.fillMaxSize().testTag("admin_app_root")
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Tab selector: Orders vs Products
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = { selectedTab = "orders" },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedTab == "orders") IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier.weight(1f).testTag("admin_tab_orders")
        ) {
          Icon(Icons.Filled.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Orders (${orders.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = { selectedTab = "products" },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedTab == "products") IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier.weight(1f).testTag("admin_tab_products")
        ) {
          Icon(Icons.Filled.Inventory, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Inventory (${products.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Content based on selected tab
      if (selectedTab == "orders") {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          // 1. Analytics & Counters
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(
                      text = "Qatar Fulfillment Hub",
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                      text = "${orders.size} Total Orders",
                      fontSize = 20.sp,
                      fontWeight = FontWeight.Black,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CoralSecondary.copy(alpha = 0.12f)
                  ) {
                    Column(
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                      horizontalAlignment = Alignment.End
                    ) {
                      Text("Total Revenue", fontSize = 10.sp, color = CoralSecondary, fontWeight = FontWeight.Bold)
                      Text(
                        text = "${totalRevenue.toInt()} QAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = CoralSecondary
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  AdminStatPill(count = pendingCount, label = "Pending", color = Color(0xFFF59E0B), icon = Icons.Filled.Pending)
                  AdminStatPill(count = packagedCount, label = "Packaged", color = IndigoPrimary, icon = Icons.Filled.Inventory)
                  AdminStatPill(count = shippingCount, label = "Shipping", color = Color(0xFF3B82F6), icon = Icons.Filled.LocalShipping)
                  AdminStatPill(count = deliveredCount, label = "Delivered", color = MintAccent, icon = Icons.Filled.CheckCircle)
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
          }

          // 2. Search Box
          item {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              placeholder = { Text("Search by customer, phone, order ID, toy...") },
              leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(onClick = { searchQuery = "" }) {
                    Text("✕", fontSize = 14.sp)
                  }
                }
              },
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.fillMaxWidth().testTag("admin_search_input")
            )

            Spacer(modifier = Modifier.height(12.dp))
          }

          // 3. Status Filter Chips
          item {
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              val filters = listOf(
                "all" to "All (${orders.size})",
                "pending" to "⏳ Pending ($pendingCount)",
                "packaged" to "📦 Packaged ($packagedCount)",
                "shipping" to "🚚 Shipping ($shippingCount)",
                "delivered" to "✅ Delivered ($deliveredCount)"
              )

              items(filters) { (key, label) ->
                FilterChip(
                  selected = selectedFilter == key,
                  onClick = { selectedFilter = key },
                  label = { Text(label, fontSize = 12.sp, fontWeight = if (selectedFilter == key) FontWeight.Bold else FontWeight.Normal) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IndigoPrimary,
                    selectedLabelColor = Color.White
                  ),
                  modifier = Modifier.testTag("admin_filter_$key")
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
          }

          // 4. Orders List
          if (filteredOrders.isEmpty()) {
            item {
              Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
              ) {
                Column(
                  modifier = Modifier.padding(24.dp).fillMaxWidth(),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text("📦", fontSize = 40.sp)
                  Spacer(modifier = Modifier.height(8.dp))
                  Text("No orders match this filter", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  Text("Customer orders will appear here in real-time.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          } else {
            items(filteredOrders, key = { it.orderId }) { order ->
              AdminOrderCard(
                order = order,
                onUpdateStatus = { newStatus -> viewModel.updateOrderStatus(order.orderId, newStatus) },
                onInspectTracking = { selectedOrderForTracking = order }
              )
              Spacer(modifier = Modifier.height(12.dp))
            }
          }
        }
      } else {
        // Products / Inventory Tab
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          item {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              placeholder = { Text("Search products by name, brand, category...") },
              leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(onClick = { searchQuery = "" }) {
                    Text("✕", fontSize = 14.sp)
                  }
                }
              },
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.fillMaxWidth().testTag("admin_product_search")
            )

            Spacer(modifier = Modifier.height(14.dp))
          }

          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Store Catalog (${filteredProducts.size} items)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Tap stock to toggle",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(10.dp))
          }

          items(filteredProducts, key = { it.id }) { product ->
            AdminProductCard(
              product = product,
              onToggleStock = { viewModel.toggleProductStock(product.id) },
              onDelete = { viewModel.deleteProduct(product.id) }
            )
            Spacer(modifier = Modifier.height(10.dp))
          }
        }
      }
    }

    if (isAddProductDialogOpen) {
      AddProductDialog(
        onDismiss = { isAddProductDialogOpen = false },
        onSave = { newToy ->
          viewModel.addProduct(newToy)
          isAddProductDialogOpen = false
        }
      )
    }
  }
}

@Composable
fun AdminStatPill(
  count: Int,
  label: String,
  color: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = color.copy(alpha = 0.10f)
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(3.dp))
        Text(text = "$count", fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
      }
      Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
fun AdminOrderCard(
  order: FirestoreOrder,
  onUpdateStatus: (String) -> Unit,
  onInspectTracking: () -> Unit
) {
  var isStatusMenuExpanded by remember { mutableStateOf(false) }

  val statusColor = when (order.status.lowercase()) {
    "pending" -> Color(0xFFF59E0B)
    "packaged" -> IndigoPrimary
    "shipping" -> Color(0xFF3B82F6)
    "delivered" -> MintAccent
    else -> Color.Gray
  }

  val statusLabel = when (order.status.lowercase()) {
    "pending" -> "⏳ Pending"
    "packaged" -> "📦 Packaged"
    "shipping" -> "🚚 Shipping"
    "delivered" -> "✅ Delivered"
    else -> order.status
  }

  val formattedTime = remember(order.timestamp) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
    sdf.format(Date(order.timestamp))
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth().testTag("admin_order_card_${order.orderId}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "#${order.orderId}",
              fontSize = 15.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = formattedTime,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = "Total: ${order.totalAmount.toInt()} QAR • ${order.paymentMethod}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CoralSecondary
          )
        }

        Box {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = statusColor.copy(alpha = 0.15f),
            modifier = Modifier
              .clickable { isStatusMenuExpanded = true }
              .testTag("status_dropdown_btn_${order.orderId}")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = statusLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("▾", fontSize = 12.sp, color = statusColor, fontWeight = FontWeight.Bold)
            }
          }

          DropdownMenu(
            expanded = isStatusMenuExpanded,
            onDismissRequest = { isStatusMenuExpanded = false }
          ) {
            val statusOptions = listOf(
              "pending" to "⏳ Mark as Pending",
              "packaged" to "📦 Mark as Packaged",
              "shipping" to "🚚 Mark as Shipping (Out for Delivery)",
              "delivered" to "✅ Mark as Delivered"
            )

            statusOptions.forEach { (statusKey, statusText) ->
              val isCurrent = order.status.equals(statusKey, ignoreCase = true)
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = statusText,
                      fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                      color = if (isCurrent) IndigoPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    if (isCurrent) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Icon(Icons.Filled.Check, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
                    }
                  }
                },
                onClick = {
                  isStatusMenuExpanded = false
                  onUpdateStatus(statusKey)
                },
                modifier = Modifier.testTag("admin_status_option_${order.orderId}_$statusKey")
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      Divider()
      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "WHO ORDERED 👤",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(4.dp))
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = order.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(3.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Call, contentDescription = null, tint = MintAccent, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "🇶🇦 ${order.phone}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = IndigoPrimary)
          }
          Spacer(modifier = Modifier.height(3.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = CoralSecondary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "${order.address}, ${order.city} (Qatar)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "WHAT HE ORDERED (${order.items.size} ITEMS) 🛍️",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(4.dp))
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
          .padding(10.dp)
      ) {
        order.items.forEach { item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Text(text = item.iconEmoji, fontSize = 18.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(text = item.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Qty: ${item.quantity} × ${item.priceQar.toInt()} QAR", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Text(
              text = "${(item.quantity * item.priceQar).toInt()} QAR",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = IndigoPrimary.copy(alpha = 0.08f),
          modifier = Modifier
            .clickable { onInspectTracking() }
            .testTag("admin_inspect_tracking_${order.orderId}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Preview Customer Tracking Stepper 🔍", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = IndigoPrimary)
          }
        }
      }
    }
  }
}

@Composable
fun AdminProductCard(
  product: ToyItem,
  onToggleStock: () -> Unit,
  onDelete: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth().testTag("admin_product_card_${product.id}")
  ) {
    Row(
      modifier = Modifier.padding(14.dp).fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = IndigoPrimary.copy(alpha = 0.1f),
          modifier = Modifier.size(44.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(text = product.iconEmoji, fontSize = 22.sp)
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = product.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${product.brand} • ${product.category}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${product.priceQar.toInt()} QAR",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = CoralSecondary
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (product.inStock) MintAccent.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f),
          modifier = Modifier.clickable { onToggleStock() }
        ) {
          Text(
            text = if (product.inStock) "In Stock ✓" else "Out of Stock ✕",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (product.inStock) MintAccent else Color.Red,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(28.dp).testTag("admin_delete_product_${product.id}")
        ) {
          Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}

@Composable
fun AddProductDialog(
  onDismiss: () -> Unit,
  onSave: (ToyItem) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var brand by remember { mutableStateOf("LEGO") }
  var category by remember { mutableStateOf("Building Sets") }
  var ageRange by remember { mutableStateOf("6-8 Years") }
  var priceStr by remember { mutableStateOf("149.0") }
  var description by remember { mutableStateOf("Exciting new toy for kids in Qatar.") }
  var iconEmoji by remember { mutableStateOf("🧸") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add New Toy Product 🧸", fontSize = 16.sp, fontWeight = FontWeight.Black) },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Toy Name") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("add_product_name")
        )

        OutlinedTextField(
          value = brand,
          onValueChange = { brand = it },
          label = { Text("Brand (e.g. LEGO, Hot Wheels)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("add_product_brand")
        )

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Category") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("add_product_category")
        )

        OutlinedTextField(
          value = priceStr,
          onValueChange = { priceStr = it },
          label = { Text("Price (QAR)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("add_product_price")
        )

        OutlinedTextField(
          value = iconEmoji,
          onValueChange = { iconEmoji = it },
          label = { Text("Icon Emoji (e.g. 🏎️, 🧱)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val price = priceStr.toDoubleOrNull() ?: 99.0
          val newToy = ToyItem(
            id = "toy_admin_${System.currentTimeMillis()}",
            name = if (name.isBlank()) "New Qatar Toy" else name,
            brand = brand,
            category = category,
            ageRange = ageRange,
            priceQar = price,
            rating = 5.0,
            reviewsCount = 1,
            badge = "NEW QATAR",
            description = description,
            features = listOf("Official Qatar import", "Safe for kids", "Fast delivery"),
            inStock = true,
            popularScore = 99,
            iconEmoji = if (iconEmoji.isBlank()) "🧸" else iconEmoji
          )
          onSave(newToy)
        },
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier.testTag("save_new_product_btn")
      ) {
        Text("Save & Publish to Store", color = Color.White, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

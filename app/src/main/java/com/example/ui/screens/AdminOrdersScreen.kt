package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.model.FirestoreOrder
import com.example.model.FirestoreOrderStatus
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen(
  orders: List<FirestoreOrder>,
  onUpdateStatus: (orderId: String, newStatus: String) -> Unit,
  onViewOrderTracking: (FirestoreOrder) -> Unit,
  onBackToStore: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf("all") }
  var searchQuery by remember { mutableStateOf("") }

  val filteredOrders = remember(orders, selectedFilter, searchQuery) {
    orders.filter { order ->
      val matchesFilter = if (selectedFilter == "all") true else order.status.equals(selectedFilter, ignoreCase = true)
      val matchesSearch = if (searchQuery.isBlank()) true else {
        order.orderId.contains(searchQuery, ignoreCase = true) ||
          order.customerName.contains(searchQuery, ignoreCase = true) ||
          order.city.contains(searchQuery, ignoreCase = true) ||
          order.phone.contains(searchQuery, ignoreCase = true)
      }
      matchesFilter && matchesSearch
    }
  }

  val pendingCount = orders.count { it.status.equals("pending", ignoreCase = true) }
  val packagedCount = orders.count { it.status.equals("packaged", ignoreCase = true) }
  val shippingCount = orders.count { it.status.equals("shipping", ignoreCase = true) }
  val deliveredCount = orders.count { it.status.equals("delivered", ignoreCase = true) }
  val totalRevenueQar = orders.sumOf { it.totalAmount }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Wonder Toy Admin Portal",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "🛠️", fontSize = 14.sp)
            }
            Text(
              text = "Firestore Live Database • Qatar Fulfillment",
              fontSize = 11.sp,
              color = MintAccent,
              fontWeight = FontWeight.SemiBold
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackToStore,
            modifier = Modifier.testTag("admin_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Store",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = IndigoPrimary.copy(alpha = 0.12f),
            modifier = Modifier
              .clickable { onBackToStore() }
              .padding(end = 6.dp)
              .testTag("admin_switch_to_user_app_btn")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Text("Customer App 🛍️", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MintAccent.copy(alpha = 0.15f),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MintAccent))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Firestore Online", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MintAccent)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    modifier = modifier
      .fillMaxSize()
      .testTag("admin_orders_screen")
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      // 1. Metrics & Counter Cards
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Orders Overview (${orders.size} Total)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Total: ${totalRevenueQar.toInt()} QAR",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IndigoPrimary
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              StatusCounterBox(
                title = "Pending",
                count = pendingCount,
                color = Color(0xFFFFB300),
                modifier = Modifier.weight(1f)
              )
              StatusCounterBox(
                title = "Packaged",
                count = packagedCount,
                color = Color(0xFF0288D1),
                modifier = Modifier.weight(1f)
              )
              StatusCounterBox(
                title = "Shipping",
                count = shippingCount,
                color = IndigoPrimary,
                modifier = Modifier.weight(1f)
              )
              StatusCounterBox(
                title = "Delivered",
                count = deliveredCount,
                color = MintAccent,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // 2. Search & Filter Bar
      item {
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search by Order ID, Customer, Phone, City...", fontSize = 12.sp) },
          leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          val filterOptions = listOf(
            "all" to "All (${orders.size})",
            "pending" to "Pending ($pendingCount)",
            "packaged" to "Packaged ($packagedCount)",
            "shipping" to "Shipping ($shippingCount)",
            "delivered" to "Delivered ($deliveredCount)"
          )

          items(filterOptions) { (key, label) ->
            FilterChip(
              selected = selectedFilter == key,
              onClick = { selectedFilter = key },
              label = { Text(label, fontSize = 11.sp, fontWeight = if (selectedFilter == key) FontWeight.Bold else FontWeight.Normal) },
              shape = RoundedCornerShape(8.dp),
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = IndigoPrimary,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }

      // 3. Orders List with Status Dropdown
      item {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Showing ${filteredOrders.size} Orders",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Tap dropdown to change status",
            fontSize = 11.sp,
            color = IndigoPrimary
          )
        }
      }

      if (filteredOrders.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Filled.Inventory2, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(48.dp))
              Spacer(modifier = Modifier.height(8.dp))
              Text("No orders match the selected filter", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      } else {
        items(filteredOrders, key = { it.orderId }) { order ->
          Spacer(modifier = Modifier.height(10.dp))
          AdminOrderCard(
            order = order,
            onUpdateStatus = { newStatus -> onUpdateStatus(order.orderId, newStatus) },
            onViewTracking = { onViewOrderTracking(order) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
      }
    }
  }
}

@Composable
private fun StatusCounterBox(
  title: String,
  count: Int,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = color.copy(alpha = 0.12f),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "$count",
        fontSize = 16.sp,
        fontWeight = FontWeight.Black,
        color = color
      )
      Text(
        text = title,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        color = color
      )
    }
  }
}

@Composable
private fun AdminOrderCard(
  order: FirestoreOrder,
  onUpdateStatus: (newStatus: String) -> Unit,
  onViewTracking: () -> Unit
) {
  var isDropdownOpen by remember { mutableStateOf(false) }

  val statusOptions = listOf(
    "pending" to ("Pending Confirmation" to Color(0xFFFFB300)),
    "packaged" to ("Packaged at Doha Hub" to Color(0xFF0288D1)),
    "shipping" to ("Out for Delivery" to IndigoPrimary),
    "delivered" to ("Delivered" to MintAccent)
  )

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_order_card_${order.orderId}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Order ID & Timestamp
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "#${order.orderId}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = order.formattedTime,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Dropdown Menu Button for Status Update
        Box {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = when (order.status.lowercase()) {
              "pending" -> Color(0xFFFFB300)
              "packaged" -> Color(0xFF0288D1)
              "shipping" -> IndigoPrimary
              "delivered" -> MintAccent
              else -> Color.Gray
            },
            modifier = Modifier
              .clickable { isDropdownOpen = true }
              .testTag("admin_status_dropdown_${order.orderId}")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = order.status.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Change Status",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          // Dropdown with the 4 statuses
          DropdownMenu(
            expanded = isDropdownOpen,
            onDismissRequest = { isDropdownOpen = false }
          ) {
            statusOptions.forEach { (statusKey, pair) ->
              val (statusLabel, statusColor) = pair
              val isCurrent = order.status.equals(statusKey, ignoreCase = true)

              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = statusKey.uppercase(),
                      fontSize = 12.sp,
                      fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                      color = if (isCurrent) statusColor else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "($statusLabel)",
                      fontSize = 11.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                },
                onClick = {
                  isDropdownOpen = false
                  onUpdateStatus(statusKey)
                },
                leadingIcon = {
                  if (isCurrent) {
                    Icon(
                      imageVector = Icons.Filled.Check,
                      contentDescription = "Current",
                      tint = statusColor,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Customer & Location
      Text(
        text = "Customer: ${order.customerName} • ${order.phone}",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Address: ${order.address}, ${order.city}",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Items list preview
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${order.items.size} item(s): " + order.items.joinToString { "${it.iconEmoji} ${it.name} (x${it.quantity})" },
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
      Divider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(8.dp))

      // Footer: Total & Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "${order.totalAmount.toInt()} QAR",
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
          )
          Text(
            text = order.paymentMethod,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        OutlinedButton(
          onClick = onViewTracking,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.testTag("admin_view_tracking_${order.orderId}")
        ) {
          Icon(Icons.Filled.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("User Tracking 📡", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

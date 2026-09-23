package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.OutlinedButton
import com.example.model.DeliveryType
import com.example.model.FirestoreOrder
import com.example.model.FirestoreOrderStatus
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

@Composable
fun OrdersScreen(
  orders: List<Order>,
  firestoreOrders: List<FirestoreOrder> = emptyList(),
  onShopToysClick: () -> Unit,
  onTrackFirestoreOrderClick: (FirestoreOrder) -> Unit = {},
  onOpenAdminClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val hasAnyOrders = orders.isNotEmpty() || firestoreOrders.isNotEmpty()

  if (!hasAnyOrders) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(32.dp)
        .testTag("orders_empty_view"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Filled.LocalShipping,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.size(72.dp)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "No Orders Yet",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "Explore the Wonder Toy catalog to place your first Qatar order!",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
      Spacer(modifier = Modifier.height(20.dp))
      Button(
        onClick = onShopToysClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier.testTag("orders_shop_btn")
      ) {
        Text("Shop Wonder Toy")
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("orders_screen_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "My Orders & Qatar Tracking 🇶🇦",
              fontSize = 18.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Real-time sync via Firebase Firestore 📡",
              fontSize = 11.sp,
              color = MintAccent,
              fontWeight = FontWeight.SemiBold
            )
          }

          OutlinedButton(
            onClick = onOpenAdminClick,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.testTag("orders_admin_portal_btn")
          ) {
            Text("Admin 🛠️", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      // Firestore Real-Time Orders
      if (firestoreOrders.isNotEmpty()) {
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Live Firestore Orders (${firestoreOrders.size})",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = IndigoPrimary
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MintAccent.copy(alpha = 0.15f)
            ) {
              Text(
                text = "REAL-TIME",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MintAccent,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        items(firestoreOrders, key = { "fs_${it.orderId}" }) { fOrder ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .testTag("firestore_order_card_${fOrder.orderId}")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              // Top Row: Order ID & Status Badge
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "#${fOrder.orderId}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = IndigoPrimary
                  )
                  Text(
                    text = fOrder.formattedTime,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = when (fOrder.status.lowercase()) {
                    "pending" -> Color(0xFFFFB300)
                    "packaged" -> Color(0xFF0288D1)
                    "shipping" -> IndigoPrimary
                    "delivered" -> MintAccent
                    else -> Color.Gray
                  }
                ) {
                  Text(
                    text = fOrder.status.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Customer & Destination
              Text(
                text = "${fOrder.customerName} • ${fOrder.address}, ${fOrder.city}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              // Items preview
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = fOrder.items.joinToString { "${it.iconEmoji} ${it.name} (x${it.quantity})" },
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
              )

              Divider(modifier = Modifier.padding(vertical = 8.dp))

              // Bottom Row: Total & Track Button
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "${fOrder.totalAmount.toInt()} QAR",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Text(
                    text = fOrder.paymentMethod,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Button(
                  onClick = { onTrackFirestoreOrderClick(fOrder) },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                  modifier = Modifier.testTag("track_firestore_order_${fOrder.orderId}")
                ) {
                  Icon(Icons.Filled.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Track Live Status 📡", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

@Composable
fun TrackingTimeline(currentStatus: OrderStatus) {
  val steps = listOf(
    OrderStatus.CONFIRMED to "Confirmed",
    OrderStatus.PREPARING to "Packing",
    OrderStatus.OUT_FOR_DELIVERY to "On Qatar Road",
    OrderStatus.DELIVERED to "Delivered"
  )

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    steps.forEachIndexed { index, (status, label) ->
      val isReached = currentStatus.stepIndex >= status.stepIndex

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .background(
              color = if (isReached) MintAccent else MaterialTheme.colorScheme.surfaceVariant,
              shape = CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          if (isReached) {
            Icon(
              imageVector = Icons.Filled.Check,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
          } else {
            Box(
              modifier = Modifier
                .size(6.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = label,
          fontSize = 9.sp,
          fontWeight = if (isReached) FontWeight.Bold else FontWeight.Normal,
          color = if (isReached) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      if (index < steps.size - 1) {
        val nextReached = currentStatus.stepIndex >= steps[index + 1].first.stepIndex
        Box(
          modifier = Modifier
            .weight(1f)
            .height(3.dp)
            .padding(horizontal = 4.dp)
            .background(
              if (nextReached) MintAccent else MaterialTheme.colorScheme.surfaceVariant,
              RoundedCornerShape(2.dp)
            )
        )
      }
    }
  }
}

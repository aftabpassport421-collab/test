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
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
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
import com.example.model.DeliveryType
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

@Composable
fun OrdersScreen(
  orders: List<Order>,
  onShopToysClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (orders.isEmpty()) {
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
        Text(
          text = "My Orders & Qatar Tracking 🇶🇦",
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Real-time dispatch from Wonder Toy Doha fulfillment hub",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))
      }

      items(orders, key = { it.id }) { order ->
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("order_card_${order.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = order.id,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = IndigoPrimary
                )
                Text(
                  text = order.orderDateFormatted,
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = when (order.status) {
                  OrderStatus.DELIVERED -> MintAccent.copy(alpha = 0.15f)
                  OrderStatus.OUT_FOR_DELIVERY -> CoralSecondary.copy(alpha = 0.15f)
                  else -> IndigoPrimary.copy(alpha = 0.15f)
                }
              ) {
                Text(
                  text = order.status.label,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = when (order.status) {
                    OrderStatus.DELIVERED -> MintAccent
                    OrderStatus.OUT_FOR_DELIVERY -> CoralSecondary
                    else -> IndigoPrimary
                  },
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step Progress Timeline
            TrackingTimeline(currentStatus = order.status)

            Spacer(modifier = Modifier.height(14.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // Items breakdown
            order.items.forEach { cartItem ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Text(text = cartItem.toy.iconEmoji, fontSize = 18.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "${cartItem.quantity}x ${cartItem.toy.name}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
                Text(
                  text = "${cartItem.itemTotalQar.toInt()} QAR",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            // Destination and Total row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Destination:",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = order.addressOrStore,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Total Paid:",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "${order.totalQar.toInt()} QAR",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Black,
                  color = MaterialTheme.colorScheme.primary
                )
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

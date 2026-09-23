package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.StarGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
  order: FirestoreOrder,
  onBackClick: () -> Unit,
  onUpdateStatusTest: ((newStatus: String) -> Unit)? = null,
  onOpenAdminView: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val currentStatus = order.orderStatus
  val currentStep = currentStatus.stepIndex

  val progressFraction by animateFloatAsState(
    targetValue = (currentStep + 1) / 4f,
    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
    label = "progress"
  )

  var showSimControls by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Live Order Tracking",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "📡", fontSize = 14.sp)
            }
            Text(
              text = "#${order.orderId} • Firestore Real-Time",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("tracking_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        },
        actions = {
          if (onOpenAdminView != null) {
            OutlinedButton(
              onClick = onOpenAdminView,
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              modifier = Modifier.padding(end = 12.dp)
            ) {
              Text("Admin 🛠️", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
      .testTag("order_tracking_screen")
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp)
    ) {
      // 1. Live Firestore Sync Status Header
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = when (currentStatus) {
              FirestoreOrderStatus.PENDING -> Color(0xFFFFF8E1)
              FirestoreOrderStatus.PACKAGED -> Color(0xFFE1F5FE)
              FirestoreOrderStatus.SHIPPING -> Color(0xFFEDE7F6)
              FirestoreOrderStatus.DELIVERED -> Color(0xFFE8F5E9)
            }
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MintAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Live Firestore Sync",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF2E7D32)
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (currentStatus) {
                  FirestoreOrderStatus.PENDING -> Color(0xFFFFB300)
                  FirestoreOrderStatus.PACKAGED -> Color(0xFF0288D1)
                  FirestoreOrderStatus.SHIPPING -> IndigoPrimary
                  FirestoreOrderStatus.DELIVERED -> MintAccent
                }
              ) {
                Text(
                  text = order.status.uppercase(),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = currentStatus.iconEmoji,
                fontSize = 32.sp
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = currentStatus.displayName,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF1E293B)
                )
                Text(
                  text = currentStatus.description,
                  fontSize = 12.sp,
                  color = Color(0xFF475569)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
              progress = { progressFraction },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = when (currentStatus) {
                FirestoreOrderStatus.PENDING -> Color(0xFFFFB300)
                FirestoreOrderStatus.PACKAGED -> Color(0xFF0288D1)
                FirestoreOrderStatus.SHIPPING -> IndigoPrimary
                FirestoreOrderStatus.DELIVERED -> MintAccent
              },
              trackColor = Color.White.copy(alpha = 0.6f)
            )
          }
        }
      }

      // 2. 4-Step Interactive Tracking Stepper
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Dispatch & Delivery Timeline",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Updated in real-time from Qatar distribution network",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            val steps = listOf(
              Triple("pending", "Order Received", "Payment verified & queued in Doha"),
              Triple("packaged", "Packaged & Ribboned", "Prepared at Wonder Toy hub with gift wrap"),
              Triple("shipping", "Out for Delivery", "Express van en route in ${order.city}"),
              Triple("delivered", "Delivered", "Handed to recipient with contactless signature")
            )

            steps.forEachIndexed { index, (stepKey, stepTitle, stepDesc) ->
              val isCompleted = index <= currentStep
              val isCurrent = index == currentStep

              Row(modifier = Modifier.fillMaxWidth()) {
                // Stepper Icon and Vertical Line
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  val circleColor by animateColorAsState(
                    targetValue = when {
                      isCurrent -> IndigoPrimary
                      isCompleted -> MintAccent
                      else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    label = "circle_color"
                  )

                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .clip(CircleShape)
                      .background(circleColor),
                    contentAlignment = Alignment.Center
                  ) {
                    if (isCompleted) {
                      Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                      )
                    } else {
                      Text(
                        text = "${index + 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                  }

                  if (index < steps.size - 1) {
                    Box(
                      modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(
                          if (index < currentStep) MintAccent
                          else MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                  }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Step content
                Column(modifier = Modifier.padding(bottom = if (index < steps.size - 1) 18.dp else 0.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = stepTitle,
                      fontSize = 13.sp,
                      fontWeight = if (isCurrent) FontWeight.Black else if (isCompleted) FontWeight.Bold else FontWeight.Medium,
                      color = if (isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isCurrent) {
                      Spacer(modifier = Modifier.width(8.dp))
                      Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = CoralSecondary.copy(alpha = 0.15f)
                      ) {
                        Text(
                          text = "CURRENT",
                          fontSize = 9.sp,
                          fontWeight = FontWeight.ExtraBold,
                          color = CoralSecondary,
                          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                      }
                    }
                  }
                  Text(
                    text = stepDesc,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }

      // 3. Customer & Qatar Delivery Details
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Recipient & Destination",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.Person, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(order.customerName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              Spacer(modifier = Modifier.width(12.dp))
              Icon(Icons.Filled.Call, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(order.phone, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
              Icon(Icons.Filled.LocationOn, contentDescription = null, tint = CoralSecondary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("${order.address}, ${order.city}, Qatar", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("Doha Metro Zone • Express Dispatch", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.Payment, contentDescription = null, tint = MintAccent, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Payment: ${order.paymentMethod}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }

      // 4. Ordered Items Breakdown
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
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
              Text(
                text = "Items in this Order (${order.items.sumOf { it.quantity }})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${order.totalAmount.toInt()} QAR",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            order.items.forEach { item ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = item.iconEmoji, fontSize = 18.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(text = item.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text(text = "Qty: ${item.quantity}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                }
                Text(
                  text = "${(item.priceQar * item.quantity).toInt()} QAR",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Paid", fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text("${order.totalAmount.toInt()} QAR", fontSize = 15.sp, fontWeight = FontWeight.Black, color = IndigoPrimary)
            }
          }
        }
      }

      // 5. Interactive Status Tester (Quick Toggle)
      if (onUpdateStatusTest != null) {
        item {
          Spacer(modifier = Modifier.height(14.dp))
          OutlinedButton(
            onClick = { showSimControls = !showSimControls },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = if (showSimControls) "Hide Live Test Controls ⚙️" else "Test Live Firestore Status Changes ⚡",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }

          AnimatedVisibility(visible = showSimControls) {
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "Simulate Firestore Real-Time Update:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  listOf("pending", "packaged", "shipping", "delivered").forEach { st ->
                    val isSel = order.status == st
                    Button(
                      onClick = { onUpdateStatusTest(st) },
                      shape = RoundedCornerShape(8.dp),
                      colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSel) IndigoPrimary else MaterialTheme.colorScheme.surface
                      ),
                      contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                      modifier = Modifier.weight(1f)
                    ) {
                      Text(
                        text = st.take(4).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
      }
    }
  }
}

package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.CartItem
import com.example.model.DeliveryType
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent

@Composable
fun CartScreen(
  cartItems: List<CartItem>,
  subtotalQar: Double,
  deliveryFeeQar: Double,
  discountQar: Double,
  finalTotalQar: Double,
  promoCode: String,
  promoMessage: String?,
  deliveryType: DeliveryType,
  onQuantityChange: (String, Int) -> Unit,
  onGiftWrapToggle: (String) -> Unit,
  onRemoveItem: (String) -> Unit,
  onApplyPromo: (String) -> Unit,
  onDeliveryTypeChange: (DeliveryType) -> Unit,
  onProceedToCheckout: () -> Unit,
  onBrowseClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var inputPromo by remember(promoCode) { mutableStateOf(promoCode) }

  if (cartItems.isEmpty()) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(32.dp)
        .testTag("cart_empty_view"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Filled.ShoppingCart,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.size(72.dp)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Your Shopping Cart is Empty",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "Add beloved toys to your cart for same-day delivery in Doha!",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
      Spacer(modifier = Modifier.height(20.dp))
      Button(
        onClick = onBrowseClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
        modifier = Modifier.testTag("cart_browse_btn")
      ) {
        Text("Start Shopping")
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("cart_screen_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
      // 1. Title & Item count
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Shopping Bag (${cartItems.size})",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
          )

          // Secure Checkout badge
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Filled.Security,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Secure Checkout",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
      }

      // 2. Cart Items List
      items(cartItems, key = { it.toy.id }) { item ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("cart_item_${item.toy.id}")
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Visual Box
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(item.toy.accentColorHex).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                Text(text = item.toy.iconEmoji, fontSize = 32.sp)
              }

              Spacer(modifier = Modifier.width(12.dp))

              // Title and Price
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.toy.brand.uppercase(),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(item.toy.accentColorHex)
                )
                Text(
                  text = item.toy.name,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  maxLines = 1,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "${item.toy.priceQar.toInt()} QAR each",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              // Stepper
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                  .padding(2.dp)
              ) {
                IconButton(
                  onClick = { onQuantityChange(item.toy.id, item.quantity - 1) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = if (item.quantity == 1) Icons.Filled.DeleteOutline else Icons.Filled.Remove,
                    contentDescription = "Decrease",
                    modifier = Modifier.size(16.dp),
                    tint = if (item.quantity == 1) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface
                  )
                }

                Text(
                  text = "${item.quantity}",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(
                  onClick = { onQuantityChange(item.toy.id, item.quantity + 1) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Increase",
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }

            // Gift wrap toggle row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                  checked = item.giftWrap,
                  onCheckedChange = { onGiftWrapToggle(item.toy.id) },
                  colors = CheckboxDefaults.colors(checkedColor = CoralSecondary),
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Gift Wrap (+10 QAR)",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Text(
                text = "${item.itemTotalQar.toInt()} QAR",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // 3. Promo Code Input
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Promo Voucher",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              OutlinedTextField(
                value = inputPromo,
                onValueChange = { inputPromo = it },
                placeholder = { Text("Code: WONDER10 or QATAR", fontSize = 12.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("promo_code_input")
              )
              Spacer(modifier = Modifier.width(8.dp))
              Button(
                onClick = { onApplyPromo(inputPromo) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.testTag("apply_promo_btn")
              ) {
                Text("Apply")
              }
            }

            if (promoMessage != null) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = promoMessage,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (discountQar > 0) MintAccent else Color(0xFFEF4444)
              )
            }
          }
        }
      }

      // 4. Delivery Selection
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "Delivery Method in Qatar",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          DeliveryType.values().forEach { dType ->
            val isSelected = deliveryType == dType
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier
                .weight(1f)
                .clickable { onDeliveryTypeChange(dType) }
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = when (dType) {
                    DeliveryType.SAME_DAY_DOHA -> "Same-Day Doha"
                    DeliveryType.STANDARD_QATAR -> "Qatar Standard"
                  },
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (dType.priceQar == 0.0) "FREE" else "${dType.priceQar.toInt()} QAR",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = if (isSelected) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      // 5. Cost Breakdown
      item {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Items Subtotal", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${subtotalQar.toInt()} QAR", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            if (discountQar > 0) {
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Promo Discount", fontSize = 13.sp, color = CoralSecondary)
                Text("-${discountQar.toInt()} QAR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoralSecondary)
              }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Qatar Shipping", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(
                if (deliveryFeeQar == 0.0) "FREE" else "${deliveryFeeQar.toInt()} QAR",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Total Amount", fontSize = 16.sp, fontWeight = FontWeight.Black)
              Text(
                "${finalTotalQar.toInt()} QAR",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }

      // 6. Direct Checkout Button
      item {
        Spacer(modifier = Modifier.height(18.dp))
        Button(
          onClick = onProceedToCheckout,
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("proceed_checkout_btn")
        ) {
          Icon(
            imageVector = Icons.Filled.Payment,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Proceed to Checkout • ${finalTotalQar.toInt()} QAR",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black
          )
        }

        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

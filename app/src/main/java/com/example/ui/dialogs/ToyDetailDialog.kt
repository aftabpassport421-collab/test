package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ToyItem
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent
import com.example.ui.theme.StarGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToyDetailDialog(
  toy: ToyItem,
  isWishlisted: Boolean,
  onDismiss: () -> Unit,
  onWishlistToggle: () -> Unit,
  onAddToCart: (giftWrap: Boolean) -> Unit,
  onDirectBuy: (giftWrap: Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var giftWrap by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    modifier = modifier.testTag("toy_detail_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
      // Header with close & wishlist
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primaryContainer
        ) {
          Text(
            text = toy.category,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }

        Row {
          IconButton(
            onClick = onWishlistToggle,
            modifier = Modifier
              .size(36.dp)
              .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
          ) {
            Icon(
              imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Wishlist",
              tint = if (isWishlisted) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(36.dp)
              .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
              .testTag("close_detail_sheet_btn")
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Visual Hero Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.verticalGradient(
              listOf(
                Color(toy.accentColorHex).copy(alpha = 0.25f),
                Color(toy.accentColorHex).copy(alpha = 0.08f)
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = toy.iconEmoji,
          fontSize = 80.sp
        )

        // Age Tag
        Surface(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(12.dp),
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ) {
          Text(
            text = "Age: ${toy.ageRange}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        // Availability badge
        Surface(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(12.dp),
          shape = RoundedCornerShape(8.dp),
          color = MintAccent
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Filled.FlashOn,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "In Stock • Qatar",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Brand & Name
      Text(
        text = toy.brand.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(toy.accentColorHex),
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = toy.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Rating & Reviews row
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Filled.Star,
          contentDescription = null,
          tint = StarGold,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${toy.rating}",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "(${toy.reviewsCount} Doha verified buyers)",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Price Row
      Row(
        verticalAlignment = Alignment.Bottom
      ) {
        Text(
          text = "${toy.priceQar.toInt()} ",
          fontSize = 26.sp,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = "QAR",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        if (toy.originalPriceQar != null) {
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "${toy.originalPriceQar.toInt()} QAR",
            fontSize = 15.sp,
            color = Color(0xFF94A3B8),
            textDecoration = TextDecoration.LineThrough
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = CoralSecondary.copy(alpha = 0.15f)
          ) {
            Text(
              text = "SAVE ${toy.discountPercent}%",
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              color = CoralSecondary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Description
      Text(
        text = "Product Overview",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = toy.description,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Key Features
      Text(
        text = "Key Highlights",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      toy.features.forEach { feature ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = MintAccent,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = feature,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Gift Wrap Checkbox
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Filled.CardGiftcard,
              contentDescription = null,
              tint = CoralSecondary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Premium Gift Wrapping (+10 QAR)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Festive ribbon with Wonder Toy card",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Checkbox(
            checked = giftWrap,
            onCheckedChange = { giftWrap = it },
            colors = CheckboxDefaults.colors(checkedColor = CoralSecondary)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons: Add to Cart & Buy Now (Direct access, no parental locks)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = {
            onAddToCart(giftWrap)
            onDismiss()
          },
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .testTag("detail_add_to_cart_btn")
        ) {
          Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Add to Cart", fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = {
            onDirectBuy(giftWrap)
            onDismiss()
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .testTag("detail_buy_now_btn")
        ) {
          Text(
            text = "Direct Buy",
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

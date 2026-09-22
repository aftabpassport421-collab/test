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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ToyCatalog
import com.example.model.ToyItem
import com.example.ui.components.BannerCard
import com.example.ui.components.ToyCard
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MyApplicationTheme

@Composable
fun HomeScreen(
  toys: List<ToyItem>,
  wishlistIds: Set<String>,
  selectedAgeGroup: String,
  onAgeGroupSelect: (String) -> Unit,
  onBrandSelect: (String) -> Unit,
  onToyClick: (ToyItem) -> Unit,
  onWishlistToggle: (String) -> Unit,
  onAddToCart: (ToyItem) -> Unit,
  onExploreAllClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val flashDeals = toys.filter { it.originalPriceQar != null }.take(4)
  val bestsellers = toys.filter { it.badge?.contains("Bestseller", ignoreCase = true) == true || it.popularScore >= 94 }.take(6)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_list"),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // 1. Hero Banner
    item {
      Box(modifier = Modifier.padding(16.dp)) {
        BannerCard(onExploreClick = onExploreAllClick)
      }
    }

    // 2. Qatar Fast Delivery Info Card
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .background(IndigoPrimary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.LocalShipping,
                  contentDescription = null,
                  tint = IndigoPrimary,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Direct Delivery Across Qatar",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Same-day in Doha • Safe doorstep arrival",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = MintAccent
            ) {
              Text(
                text = "ACTIVE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // 3. Iconic Brands Carousel
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Official Brands",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "100% Genuine",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = IndigoPrimary
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(ToyCatalog.brands.filter { it != "All Brands" }) { brand ->
            BrandChip(
              brand = brand,
              onClick = { onBrandSelect(brand) }
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
    }

    // 4. Shop By Age Filter
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Shop by Age",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(ToyCatalog.ageGroups) { age ->
            val isSelected = selectedAgeGroup == age
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clickable { onAgeGroupSelect(age) }
                .testTag("age_chip_$age")
            ) {
              Text(
                text = age,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
    }

    // 5. Flash Deals Section (Special QAR Discounts)
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.LocalOffer,
              contentDescription = null,
              tint = CoralSecondary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Special Offers & Deals",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Text(
            text = "View All",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CoralSecondary,
            modifier = Modifier.clickable { onExploreAllClick() }
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(flashDeals) { toy ->
            Box(modifier = Modifier.width(220.dp)) {
              ToyCard(
                toy = toy,
                isWishlisted = wishlistIds.contains(toy.id),
                onToyClick = { onToyClick(toy) },
                onWishlistToggle = { onWishlistToggle(toy.id) },
                onAddToCart = { onAddToCart(toy) }
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(24.dp))
    }

    // 6. Qatar Store Advantages Card
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Why Wonder Toy Qatar? 🇶🇦",
              fontSize = 15.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              PerkItem("🚀", "Same-Day", "Doha & Lusail")
              PerkItem("📦", "All Qatar", "Fast Delivery")
              PerkItem("🎁", "Gift Wrap", "Festive Bows")
              PerkItem("💳", "Easy Pay", "Apple Pay & Cards")
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(24.dp))
    }

    // 7. Bestsellers & Trending Toys
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            tint = IndigoPrimary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Trending Toys in Qatar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Text(
          text = "See All",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = IndigoPrimary,
          modifier = Modifier.clickable { onExploreAllClick() }
        )
      }
      Spacer(modifier = Modifier.height(10.dp))
    }

    // Grid of bestsellers in pairs
    items(bestsellers.chunked(2)) { pair ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        pair.forEach { toy ->
          Box(modifier = Modifier.weight(1f)) {
            ToyCard(
              toy = toy,
              isWishlisted = wishlistIds.contains(toy.id),
              onToyClick = { onToyClick(toy) },
              onWishlistToggle = { onWishlistToggle(toy.id) },
              onAddToCart = { onAddToCart(toy) }
            )
          }
        }
        if (pair.size == 1) {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }
}

@Composable
fun BrandChip(
  brand: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val iconEmoji = when (brand) {
    "LEGO" -> "🧱"
    "Barbie" -> "🎀"
    "Hot Wheels" -> "🏎️"
    "Marvel" -> "⚡"
    "Disney" -> "✨"
    "Nerf" -> "🎯"
    "Play-Doh" -> "🎨"
    "Pokémon" -> "⚡"
    "Hasbro" -> "🎲"
    "Transformers" -> "🤖"
    else -> "🧸"
  }

  Surface(
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    modifier = modifier
      .clickable { onClick() }
      .testTag("brand_chip_$brand")
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = iconEmoji, fontSize = 16.sp)
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = brand,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@Composable
fun PerkItem(icon: String, title: String, subtitle: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = icon, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = title,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = subtitle,
      fontSize = 9.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  MyApplicationTheme {
    HomeScreen(
      toys = ToyCatalog.toys,
      wishlistIds = setOf("toy_01"),
      selectedAgeGroup = "All Ages",
      onAgeGroupSelect = {},
      onBrandSelect = {},
      onToyClick = {},
      onWishlistToggle = {},
      onAddToCart = {},
      onExploreAllClick = {}
    )
  }
}


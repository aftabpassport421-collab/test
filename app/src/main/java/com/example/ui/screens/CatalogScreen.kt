package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ToyCatalog
import com.example.model.ToyItem
import com.example.ui.components.ToyCard
import com.example.ui.theme.CoralSecondary
import com.example.ui.theme.IndigoPrimary
import com.example.viewmodel.SortOrder

@Composable
fun CatalogScreen(
  toys: List<ToyItem>,
  wishlistIds: Set<String>,
  selectedCategory: String,
  selectedAgeGroup: String,
  selectedBrand: String,
  sortOrder: SortOrder,
  onCategorySelect: (String) -> Unit,
  onAgeSelect: (String) -> Unit,
  onBrandSelect: (String) -> Unit,
  onSortSelect: (SortOrder) -> Unit,
  onClearFilters: () -> Unit,
  onToyClick: (ToyItem) -> Unit,
  onWishlistToggle: (String) -> Unit,
  onAddToCart: (ToyItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var sortMenuExpanded by remember { mutableStateOf(false) }
  val hasActiveFilters = selectedCategory != "All Categories" || selectedAgeGroup != "All Ages" || selectedBrand != "All Brands"

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("catalog_screen_list"),
    contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
  ) {
    // 1. Categories Chips
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Categories",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(ToyCatalog.categories) { cat ->
            val isSelected = selectedCategory == cat
            Surface(
              shape = RoundedCornerShape(18.dp),
              color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clickable { onCategorySelect(cat) }
                .testTag("category_chip_$cat")
            ) {
              Text(
                text = cat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // 2. Brands and Ages Filters
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Brand dropdown or chips
          items(ToyCatalog.brands) { brand ->
            val isSelected = selectedBrand == brand
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = if (isSelected) CoralSecondary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier.clickable { onBrandSelect(brand) }
            ) {
              Text(
                text = brand,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(12.dp))
    }

    // 3. Count & Sort Bar
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "${toys.size} Toys Available",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (hasActiveFilters) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.errorContainer,
              modifier = Modifier.clickable { onClearFilters() }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Filled.Close,
                  contentDescription = "Clear",
                  tint = MaterialTheme.colorScheme.onErrorContainer,
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                  text = "Reset",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onErrorContainer
                )
              }
            }
          }
        }

        // Sort Selector
        Box {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .clickable { sortMenuExpanded = true }
              .testTag("sort_dropdown_btn")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Sort",
                tint = IndigoPrimary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = sortOrder.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          DropdownMenu(
            expanded = sortMenuExpanded,
            onDismissRequest = { sortMenuExpanded = false }
          ) {
            SortOrder.values().forEach { order ->
              DropdownMenuItem(
                text = { Text(order.label, fontSize = 13.sp) },
                onClick = {
                  onSortSelect(order)
                  sortMenuExpanded = false
                }
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(12.dp))
    }

    // 4. Products Grid or Empty State
    if (toys.isEmpty()) {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Filled.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(60.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No Toys Match Your Filters",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Try clearing filters or searching for another brand.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = onClearFilters,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
          ) {
            Text("Clear All Filters")
          }
        }
      }
    } else {
      items(toys.chunked(2)) { pair ->
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
}

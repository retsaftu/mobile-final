package app.shopping.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Products : BottomBarScreen("products", "Products", Icons.Rounded.Home)
    object Cart : BottomBarScreen("cart", "Cart", Icons.Rounded.ShoppingCart)
    object Orders : BottomBarScreen("orders", "Orders", Icons.Rounded.Menu)
    object Address : BottomBarScreen("address", "Address", Icons.Rounded.Person)
}
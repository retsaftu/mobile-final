package app.shopping.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.shopping.presentation.address.AddressesScreen
import app.shopping.presentation.address.AddressesViewModel
import app.shopping.presentation.cart.CartScreen
import app.shopping.presentation.cart.CartViewModel
import app.shopping.presentation.orderDetails.OrderDetailsScreen
import app.shopping.presentation.orderDetails.OrderDetailsViewModel
import app.shopping.presentation.orders.OrdersScreen
import app.shopping.presentation.orders.OrdersViewModel
import app.shopping.presentation.productDetails.ProductDetailsScreen
import app.shopping.presentation.productDetails.ProductDetailsViewModel
import app.shopping.presentation.products.ProductsScreen
import app.shopping.presentation.products.ProductsViewModel

@Composable
fun MainScreen(
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel,
    ordersViewModel: OrdersViewModel,
    productDetailsViewModel: ProductDetailsViewModel,
    addressViewModel: AddressesViewModel,
    orderDetailsViewModel: OrderDetailsViewModel,
    userId: Int
) {
    val navController = rememberNavController()

    val screens = listOf(
        BottomBarScreen.Products,
        BottomBarScreen.Cart,
        BottomBarScreen.Orders,
        BottomBarScreen.Address
    )

    val onAddToCart: (Int) -> Unit = { productId ->
        cartViewModel.addItemToCart(productId, 1)
    }

    val onProduct: (Int) -> Unit = { productId ->
        navController.navigate("product_details/$productId")
    }

    val onOrder: (Int) -> Unit = { orderId ->
        navController.navigate("order_details/${orderId}")
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                screens = screens
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = BottomBarScreen.Products.route) {
                composable(BottomBarScreen.Products.route) {
                    ProductsScreen(productsViewModel, onAddToCart, onProduct)
                }
                composable("product_details/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    ProductDetailsScreen(viewModel = productDetailsViewModel, productId = productId, userId = userId)
                }
                composable(BottomBarScreen.Cart.route) {
                    CartScreen(cartViewModel, userId)
                }
                composable(BottomBarScreen.Orders.route) {
                    OrdersScreen(ordersViewModel, userId, onOrder)
                }
                composable("order_details/{orderId}") { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId")?.toInt() ?: 0
                    OrderDetailsScreen(viewModel = orderDetailsViewModel, orderId = orderId, userId = userId)
                }
                composable(BottomBarScreen.Address.route) {
                    AddressesScreen(addressViewModel, userId)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    screens: List<BottomBarScreen>
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar {
        screens.forEach { screen ->
            val selected = currentDestination?.route == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    Icon(
                        painter = rememberVectorPainter(screen.icon),
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) }
            )
        }
    }
}
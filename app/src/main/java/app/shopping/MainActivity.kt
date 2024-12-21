package app.shopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import app.shopping.data.datasource.local.AppDatabase
import app.shopping.data.datasource.local.MockData
import app.shopping.data.repository.AddressRepository
import app.shopping.data.repository.CartRepository
import app.shopping.data.repository.CategoryRepository
import app.shopping.data.repository.OrderRepository
import app.shopping.data.repository.PaymentRepository
import app.shopping.data.repository.ProductRepository
import app.shopping.data.repository.ReviewRepository
import app.shopping.data.repository.UserRepository
import app.shopping.di.AddressesViewModelFactory
import app.shopping.di.AuthViewModelFactory
import app.shopping.di.CartViewModelFactory
import app.shopping.di.OrderDetailsViewModelFactory
import app.shopping.di.OrdersViewModelFactory
import app.shopping.di.ProductDetailsViewModelFactory
import app.shopping.di.ProductsViewModelFactory
import app.shopping.presentation.MainScreen
import app.shopping.presentation.address.AddressesViewModel
import app.shopping.presentation.cart.CartViewModel
import app.shopping.presentation.login.AuthState
import app.shopping.presentation.login.AuthViewModel
import app.shopping.presentation.login.SignInScreen
import app.shopping.presentation.login.SignUpScreen
import app.shopping.presentation.orderDetails.OrderDetailsViewModel
import app.shopping.presentation.orders.OrdersViewModel
import app.shopping.presentation.productDetails.ProductDetailsViewModel
import app.shopping.presentation.products.ProductsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var userRepository: UserRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var addressRepository: AddressRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                GlobalScope.launch(Dispatchers.IO) {
                    val categoryDao = this@MainActivity.db.categoryDao()
                    val productDao = this@MainActivity.db.productDao()
                    val userDao = this@MainActivity.db.userDao()
                    val reviewDao = this@MainActivity.db.reviewDao()
                    val orderDao = this@MainActivity.db.orderDao()
                    val orderItemDao = this@MainActivity.db.orderItemDao()
                    val paymentDao = this@MainActivity.db.paymentDao()

                    MockData.setup(userDao, categoryDao, productDao, reviewDao, orderDao, orderItemDao, paymentDao)
                }
            }
        }).build()

        userRepository = UserRepository(db.userDao())
        productRepository = ProductRepository(db.productDao())
        categoryRepository = CategoryRepository(db.categoryDao())
        orderRepository = OrderRepository(db.orderDao(), db.orderItemDao(), db.paymentDao(), db.shoppingCartDao(), db.cartItemDao())
        cartRepository = CartRepository(db.shoppingCartDao(), db.cartItemDao(), db.productDao())
        reviewRepository = ReviewRepository(db.reviewDao())
        paymentRepository = PaymentRepository(db.paymentDao())
        addressRepository = AddressRepository(db.userAddressDao())

        val authViewModelFactory = AuthViewModelFactory(userRepository)
        val productsViewModelFactory = ProductsViewModelFactory(productRepository, categoryRepository)
        val ordersViewModelFactory = OrdersViewModelFactory(orderRepository)
        val cartViewModelFactory = CartViewModelFactory(cartRepository, orderRepository)
        val productDetailsViewModelFactory = ProductDetailsViewModelFactory(productRepository, reviewRepository)
        val orderDetailsViewModelFactory = OrderDetailsViewModelFactory(orderRepository, paymentRepository)
        val addressViewModelFactory = AddressesViewModelFactory(addressRepository)

        setContent {
            var showSignUp by remember { mutableStateOf(false) }

            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            val authState by authViewModel.authState.collectAsState()

            val productsViewModel: ProductsViewModel = viewModel(factory = productsViewModelFactory)
            val ordersViewModel: OrdersViewModel = viewModel(factory = ordersViewModelFactory)
            val cartViewModel: CartViewModel = viewModel(factory = cartViewModelFactory)
            val productDetailsViewModel: ProductDetailsViewModel = viewModel(factory = productDetailsViewModelFactory)
            val orderDetailsViewModel: OrderDetailsViewModel = viewModel(factory = orderDetailsViewModelFactory)
            val addressViewModel: AddressesViewModel = viewModel(factory = addressViewModelFactory)

            when (authState) {
                is AuthState.Idle, is AuthState.Error, is AuthState.Loading -> {
                    if (showSignUp) {
                        SignUpScreen(
                            onSignUp = { username, email, password -> authViewModel.signUp(username, email, password) },
                            onNavigateToSignIn = { showSignUp = false },
                            authState = authState
                        )
                    } else {
                        SignInScreen(
                            onSignIn = { username, password -> authViewModel.signIn(username, password) },
                            onNavigateToSignUp = { showSignUp = true },
                            authState = authState
                        )
                    }
                }

                is AuthState.Success -> {
                    val user = (authState as AuthState.Success).user

                    MainScreen(
                        productsViewModel = productsViewModel,
                        cartViewModel = cartViewModel,
                        ordersViewModel = ordersViewModel,
                        productDetailsViewModel = productDetailsViewModel,
                        addressViewModel =  addressViewModel ,
                        orderDetailsViewModel = orderDetailsViewModel,
                        userId = user.id
                    )
                }
            }
        }
    }
}

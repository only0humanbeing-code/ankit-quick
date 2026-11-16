package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.QuickKartDatabase
import com.example.data.local.SeedCatalogData
import com.example.data.local.ShopCategoryTaxonomy
import com.example.data.repository.PaymentMethodType
import com.example.data.repository.RoomQuickCommerceRepository
import com.example.model.OrderLineItem
import com.example.model.OrderStatus
import com.example.model.ProductFilterState
import com.example.model.SavedAddress
import com.example.model.SortOption
import com.example.viewmodel.QuickKartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: QuickKartDatabase
    private lateinit var repository: RoomQuickCommerceRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, QuickKartDatabase::class.java)
            .allowMainThreadQueries()
            .setQueryExecutor(Runnable::run)
            .setTransactionExecutor(Runnable::run)
            .build()
        repository = RoomQuickCommerceRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `app name matches QuickKart`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("QuickKart", appName)
    }

    @Test
    fun `seed catalog contains 12 categories 23 manufacturers and 60 plus products with variants`() = runTest {
        repository.ensureSeeded()
        val categories = repository.categories.first()
        val brands = repository.brands.first()
        val products = repository.products.first()

        assertEquals(12, categories.size)
        assertEquals(23, brands.size)
        val brandNames = brands.map { it.name }.toSet()
        assertTrue(brandNames.containsAll(
            listOf(
                "Hindustan Unilever", "Dabur", "Reckitt Benckiser", "Tata Global Beverages",
                "ITC", "Mondelez", "Godrej", "Procter & Gamble", "Varun Beverages", "Marico",
                "Nestle", "Pepsico", "Britannia", "Colgate Palmolive", "Amul", "Ferrero",
                "RSPL", "Parle", "Adani Wilmar", "Zydus Wellness", "Haldiram's", "Kellogg's", "Bajaj"
            )
        ))
        assertFalse(brandNames.contains("FreshFarm"))
        assertFalse(brandNames.contains("Mother Dairy"))
        assertFalse(brandNames.contains("Paper Boat"))
        assertEquals(SeedCatalogData.products.size, products.size)
        assertTrue(products.size >= 60)
        assertTrue(products.all { it.variants.isNotEmpty() })
        assertTrue(products.any { !it.anyVariantInStock })
        brands.forEach { brand ->
            assertTrue("Missing products for ${brand.name}", products.any { it.brandId == brand.id })
        }
    }

    @Test
    fun `cart addresses and orders persist in Room repository`() = runTest {
        repository.ensureSeeded()

        val product = SeedCatalogData.products.first()
        val variant = product.defaultVariant
        repository.setCartQuantity(product.id, variant.id, 3)

        val cartItems = repository.cartItems.first()
        assertEquals(1, cartItems.size)
        assertEquals(3, cartItems.first().quantity)

        val newAddr = SavedAddress(
            id = "addr_test",
            label = "Parents",
            recipientName = "Meera Sharma",
            phone = "9876543211",
            houseFlat = "B-204",
            streetArea = "Koramangala 4th Block",
            landmark = "Near BDA Complex",
            city = "Bengaluru",
            pincode = "560034",
            isDefault = true
        )
        repository.saveAddress(newAddr)
        val addresses = repository.addresses.first()
        assertTrue(addresses.any { it.id == "addr_test" && it.isDefault })

        val slot = SeedCatalogData.defaultDeliverySlots.first()
        val order = repository.placeOrder(
            address = newAddr,
            slot = slot,
            paymentMethodLabel = PaymentMethodType.UPI.title,
            paymentRef = "RZP_DEMO_TEST",
            items = listOf(
                OrderLineItem(
                    productId = product.id,
                    productName = product.name,
                    variantId = variant.id,
                    variantLabel = variant.label,
                    mrp = variant.mrp,
                    unitPrice = variant.sellingPrice,
                    quantity = 3,
                    imageKey = product.imageKey,
                    colorHex = product.colorHex
                )
            ),
            itemTotal = variant.sellingPrice * 3,
            deliveryFee = 25.0,
            totalSavings = variant.savingsAmount * 3,
            grandTotal = variant.sellingPrice * 3 + 25.0
        )

        assertTrue(repository.cartItems.first().isEmpty())
        val orders = repository.orders.first()
        assertTrue(orders.any { it.id == order.id })

        repository.updateOrderStatus(order.id, OrderStatus.DELIVERED)
        assertEquals(OrderStatus.DELIVERED, repository.orders.first().first { it.id == order.id }.status)
    }

    @Test
    fun `login accepts demo OTP 1234 and rejects wrong OTP`() = runTest {
        repository.ensureSeeded()
        val vm = QuickKartViewModel(repository)

        var errorMsg: String? = null
        var loggedIn = false
        vm.verifyOtp("9876543210", "9999", { loggedIn = true }, { errorMsg = it })
        assertFalse(loggedIn)
        assertTrue(errorMsg != null)

        errorMsg = null
        vm.verifyOtp("9876543210", "1234", { loggedIn = true }, { errorMsg = it })
        assertTrue(loggedIn)
        assertEquals(null, errorMsg)
        assertTrue(repository.userProfile.first().isLoggedIn)
    }

    @Test
    fun `filter and sort state updates correctly`() = runTest {
        val filter = ProductFilterState(
            minPrice = 50,
            maxPrice = 200,
            minDiscountPercent = 15,
            minRating = 4.5,
            selectedBrands = setOf("brand_amul"),
            selectedPackSizes = setOf("500 g"),
            sortOption = SortOption.SAVINGS_HIGH,
            inStockOnly = true
        )
        assertEquals(6, filter.activeFilterCount)
    }

    @Test
    fun `product category and cart DAOs support relational queries and cascade delete`() = runTest {
        repository.ensureSeeded()

        val catDao = db.categoryDao()
        val prodDao = db.productDao()
        val cartDao = db.cartDao()

        assertEquals(12, catDao.count())
        assertEquals(SeedCatalogData.products.size, prodDao.count())

        val categoriesWithProducts = catDao.observeAllWithProducts().first()
        val dairyWithProducts = categoriesWithProducts.first { it.category.id == "cat_dairy" }
        assertEquals(5, dairyWithProducts.products.size)

        val dairyProducts = prodDao.observeByCategory("cat_dairy").first()
        assertEquals(5, dairyProducts.size)

        val searchResults = prodDao.search("Alphonso").first()
        assertTrue(searchResults.any { it.id == "p09" })

        val details = prodDao.observeAllWithDetails().first()
        val firstDetail = details.first { it.product.id == "p11" }
        assertEquals("Dairy & Bread", firstDetail.category?.name)
        assertEquals("Amul", firstDetail.brand?.name)

        // Cart DAO relational query & quantity update
        repository.setCartQuantity("p11", "p11_v1", 2)
        assertEquals(2, cartDao.observeTotalQuantity().first())
        val cartWithProducts = cartDao.observeCartWithProducts().first()
        assertEquals(1, cartWithProducts.size)
        assertEquals("Amul Taaza Toned Milk", cartWithProducts.first().product?.name)

        // Deleting product cascades to cart_items
        prodDao.deleteById("p11")
        assertEquals(0, cartDao.observeTotalQuantity().first())
    }

    @Test
    fun `manufacturer products group into sub-brands and filter cleanly`() = runTest {
        repository.ensureSeeded()
        val products = repository.products.first()
        val daburSubBrands = products
            .filter { it.brandId == "brand_dabur" }
            .map { it.subBrand }
            .toSet()

        assertTrue(daburSubBrands.contains("Real"))
        assertTrue(daburSubBrands.contains("Dabur Red"))
        assertTrue(daburSubBrands.contains("Dabur Honey"))

        val vm = QuickKartViewModel(repository)
        vm.selectSubBrandFilter("brand_dabur", "Real")
        assertEquals("brand_dabur", vm.filterState.value.brandId)
        assertEquals("Real", vm.filterState.value.subBrand)

        vm.setCategoryWithinCurrentFilter("cat_beverages")
        assertEquals("brand_dabur", vm.filterState.value.brandId)
        assertEquals("Real", vm.filterState.value.subBrand)
        assertEquals("cat_beverages", vm.filterState.value.categoryId)
    }

    @Test
    fun `every shop by category group and subcategory contains products`() = runTest {
        repository.ensureSeeded()
        val products = repository.products.first()

        assertEquals(12, ShopCategoryTaxonomy.groups.size)
        ShopCategoryTaxonomy.groups.forEach { group ->
            val groupProducts = products.filter { it.shopCategory == group.title }
            assertTrue("Missing products for ${group.title}", groupProducts.isNotEmpty())
            group.subcategories.forEach { sub ->
                val subProducts = groupProducts.filter { it.subCategory == sub }
                assertTrue("Missing products for ${group.title} -> $sub", subProducts.isNotEmpty())
            }
        }

        val vm = QuickKartViewModel(repository)
        vm.selectShopCategoryFilter("Electronics", "Audio devices")
        assertEquals("Electronics", vm.filterState.value.shopCategory)
        assertEquals("Audio devices", vm.filterState.value.subCategory)
        vm.setSubCategoryWithinCurrentFilter("Smart Wearables")
        assertEquals("Electronics", vm.filterState.value.shopCategory)
        assertEquals("Smart Wearables", vm.filterState.value.subCategory)
    }

    @Test
    fun `brands have internet logo urls and sub-brand resolver returns valid urls`() = runTest {
        repository.ensureSeeded()
        val brands = repository.brands.first()
        assertEquals(23, brands.size)
        assertTrue(brands.all { it.logoUrl.startsWith("https://") })

        val dabur = brands.first { it.id == "brand_dabur" }
        val kissanLogo = com.example.ui.components.resolveSubBrandLogoUrl("Kissan", dabur.logoUrl)
        assertTrue(kissanLogo.startsWith("https://"))
        assertTrue(kissanLogo.contains("commons.wikimedia.org"))

        val fallbackLogo = com.example.ui.components.resolveSubBrandLogoUrl("Custom Sub", dabur.logoUrl)
        assertEquals(dabur.logoUrl, fallbackLogo)
    }

    @Test
    fun `dals and pulses leaf filters match cereals millets toor chana moong and urad other dals`() = runTest {
        repository.ensureSeeded()
        val dalProducts = repository.products.first()
            .filter { it.shopCategory == "Foodgrains, Oil & Masala" && it.subCategory == "Dals & Pulses" }

        assertTrue(dalProducts.size >= 15)
        val leaves = ShopCategoryTaxonomy.leafFiltersFor("Dals & Pulses")
        assertEquals(
            listOf("Cereals & Millets", "Toor, Channa & Moong Dal", "Urad & Other Dals"),
            leaves
        )

        leaves.forEach { leaf ->
            val matched = dalProducts.filter { ShopCategoryTaxonomy.matchesLeafFilter(it, leaf) }
            assertTrue("Expected products for leaf filter $leaf", matched.isNotEmpty())
        }

        val soya = dalProducts.first { it.id == "p151" }
        assertFalse(soya.anyVariantInStock)
        assertTrue(ShopCategoryTaxonomy.matchesLeafFilter(soya, "Cereals & Millets"))
    }

    @Test
    fun `unitRateLabel calculates accurate per kg and per litre rates`() {
        assertEquals("(₹190/kg)", com.example.util.FormatUtils.unitRateLabel(95.0, "500 g"))
        assertEquals("(₹168/kg)", com.example.util.FormatUtils.unitRateLabel(168.0, "1 kg - Pouch"))
        assertEquals("(₹111.67/kg)", com.example.util.FormatUtils.unitRateLabel(3350.0, "30 kg"))
        assertEquals("(₹17/L)", com.example.util.FormatUtils.unitRateLabel(102.0, "1 L Pack of 6"))
        assertEquals(null, com.example.util.FormatUtils.unitRateLabel(199.0, "1 Unit"))
    }

    @Test
    fun `save for later toggles persistence in Room repository`() = runTest {
        repository.ensureSeeded()
        assertTrue(repository.savedProductIds.first().isEmpty())

        val savedNow = repository.toggleSaveForLater("p155")
        assertTrue(savedNow)
        assertTrue("p155" in repository.savedProductIds.first())

        val savedAfterRemove = repository.toggleSaveForLater("p155")
        assertFalse(savedAfterRemove)
        assertFalse("p155" in repository.savedProductIds.first())
    }
}

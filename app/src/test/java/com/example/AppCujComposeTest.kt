package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w412dp-h915dp-xxhdpi")
class AppCujComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `home renders shop by manufacturer navigates to company sub-brands and all bottom tabs`() {
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("past_orders_section")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Verify "Based on your Past Orders" bar renders ONLY Shop By Category options and scrolls right-to-left
        composeTestRule.onNodeWithTag("home_lazy_column")
            .performScrollToNode(hasTestTag("past_orders_section"))
        composeTestRule.onNodeWithText("Based on your Past Orders").assertIsDisplayed()
        composeTestRule.onNodeWithTag("past_orders_tab_top_picks").assertDoesNotExist()
        composeTestRule.onNodeWithTag("past_orders_tab_other").assertDoesNotExist()
        composeTestRule.onNodeWithTag("past_orders_tab_shop_electronics").assertIsDisplayed()

        composeTestRule.onNodeWithTag("past_orders_category_tabs")
            .performScrollToNode(hasTestTag("past_orders_tab_shop_snacks_branded"))
        composeTestRule.onNodeWithTag("past_orders_tab_shop_snacks_branded").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("past_orders_category_tabs")
            .performScrollToNode(hasTestTag("past_orders_tab_shop_stationery_toys_gifts"))
        composeTestRule.onNodeWithTag("past_orders_tab_shop_stationery_toys_gifts").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("past_orders_category_tabs")
            .performScrollToNode(hasTestTag("past_orders_tab_shop_ayurveda_wellness"))
        composeTestRule.onNodeWithTag("past_orders_tab_shop_ayurveda_wellness").performClick()
        composeTestRule.waitForIdle()

        // Scroll down past Shop by category to Shop by Manufacturer
        composeTestRule.onNodeWithTag("home_lazy_column")
            .performScrollToNode(hasText("Shop by category"))
        composeTestRule.onNodeWithText("Shop by category").assertIsDisplayed()

        composeTestRule.onNodeWithTag("home_lazy_column")
            .performScrollToNode(hasTestTag("home_brand_brand_hul"))
        composeTestRule.onNodeWithText("Shop by Manufacturer").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_brand_brand_hul").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag("sub_brand_tile_Surf Excel")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithText("Hindustan Unilever").assertIsDisplayed()
        composeTestRule.onNodeWithTag("sub_brand_tile_Surf Excel").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithText("Surf Excel")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Navigate bottom tabs: Categories -> Orders -> Cart -> Account -> Home
        composeTestRule.onNodeWithTag("bottom_nav_categories").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Shop By Category").assertIsDisplayed()

        // Verify renamed and newly added category options in Shop By Category panel
        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_electronics"))
        composeTestRule.onNodeWithText("Electronic Appliances, Home & Kitchen Essentials").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_bakery_dairy"))
        composeTestRule.onNodeWithText("Bakery, Cake, Dairy & Fresh, Frozen").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_beauty_hygiene"))
        composeTestRule.onNodeWithText("Body Care & Beauty").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_baby_care"))
        composeTestRule.onNodeWithText("Baby Care, Female & Hygiene").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_stationery_toys_gifts"))
        composeTestRule.onNodeWithText("Stationery, Toys & Gifts").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_paper_disposables"))
        composeTestRule.onNodeWithText("Paper Goods & Disposables").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_industrial_catering"))
        composeTestRule.onNodeWithText("Industrial & Catering").assertIsDisplayed()

        composeTestRule.onNodeWithTag("shop_by_category_list")
            .performScrollToNode(hasTestTag("shop_category_row_shop_ayurveda_wellness"))
        composeTestRule.onNodeWithText("Ayurveda, Health & Wellness").assertIsDisplayed()

        composeTestRule.onNodeWithTag("bottom_nav_orders").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("bottom_nav_cart").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("bottom_nav_account").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("bottom_nav_home").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("home_search_bar").assertIsDisplayed()
    }
}

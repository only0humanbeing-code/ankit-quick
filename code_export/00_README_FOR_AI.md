# QuickKart Android App — Complete Multi-File AI Reference Bundle

This folder (`/code_export`) contains the **complete, unmodified source code** of the QuickKart Android app (Kotlin + Jetpack Compose + Material 3 + Room + Navigation Compose + Coil), packaged into modular Markdown files sized specifically so you can upload or paste them cleanly into **Claude AI, ChatGPT, Gemini, or any other AI coding assistant** with zero loss of features.

## How to Use with Claude AI or Another AI Assistant
1. Download or open the files inside `/code_export/`.
2. Upload `00_README_FOR_AI.md` plus whichever bundle files you want the AI to inspect or edit (or upload all 9 bundles into a Claude Project / conversation).
3. Ask the AI to generate diffs, new screens, or updates against the exact file paths listed below.
4. For the actual runnable Android Studio project, use **Export as ZIP** from the AI Studio menu (which includes `res/drawable/*.jpg`, `res/font/*.ttf`, adaptive launcher icons, and Gradle wrappers).

## Bundle Index (100% Full App Code)
- `01_Config_Entry_Navigation_Theme.md` — Gradle setup, `libs.versions.toml`, `AndroidManifest.xml`, `strings.xml`, `MainActivity.kt`, `QuickKartApp.kt` (Navigation + Bottom Bar + Address Picker), and `ui/theme/` (`Color.kt`, `Theme.kt`, `Type.kt`).
- `02_State_Models_Repository.md` — `QuickKartViewModel.kt`, `Models.kt`, `FormatUtils.kt`, `QuickCommerceRepository.kt`, `PaymentGateway.kt`.
- `03_Database_DAOs_Taxonomy.md` — `QuickKartDatabase.kt` (v9), `Converters.kt`, all Room Entities & DAOs (`Product`, `Category`, `Brand`, `Cart`, `StoreOps`, `SavedForLater`), and `ShopCategoryTaxonomy.kt` (3-level category & leaf-filter hierarchy).
- `04_Catalog_Seed_Data.md` — `SeedCatalogData.kt` (complete product catalog, brands, categories, delivery slots, sample orders).
- `05_Product_Artwork.md` — `ProductArtwork.kt` (custom vector/Canvas brand & product pack illustrations).
- `06_Screens_Home_Categories_Brands.md` — `HomeScreen.kt`, `ShopByCategoryScreen.kt`, `ManufacturerBrandsScreen.kt`, `BrandProductsScreen.kt`.
- `07_Screens_ProductList_Detail_Components.md` — `ProductListScreen.kt`, `ProductDetailScreen.kt`, `ProductCard.kt`, `FilterSortSheet.kt`.
- `08_Screens_Cart_Checkout_Orders.md` — `CartScreen.kt`, `CheckoutScreen.kt`, `OrdersScreen.kt`.
- `09_Screens_Account_Login_Admin.md` — `AccountScreen.kt`, `LoginScreen.kt`, `AdminScreen.kt`.

## App Summary
- **Package / Namespace**: `com.example` (`applicationId = "com.aistudio.quickkart.indqkc"`)
- **Features**:
  - Phone + OTP demo login (`1234`)
  - Home dashboard with 8 shop-by-category tiles, Shop by Manufacturer grid, quick search, delivery address switcher, and live cart banner
  - 3-level category browsing (`ShopByCategory` -> `SubCategory` -> `LeafFilter` pill strip)
  - Brand/Sub-brand catalog views with `"See more <Brand> products"` entry from every Product Detail page
  - Product Detail screen with green top bar (Back, Search, Basket with badge), per-unit rate pricing (`₹/kg`, `₹/L`), interactive variant pack selector, collapsible details, reviews, and sticky 50/50 split bar (`Save For Later` + `Add to Cart` / `Notify Me`)
  - Persistent Room cart, delivery slot & UPI/Card/Wallet/COD checkout, order history with Order Again, Account screen with Saved For Later manager & Wallet, and full in-app Admin CRUD console.

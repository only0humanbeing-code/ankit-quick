# Bundle 04: Complete Catalog Seed Data (`SeedCatalogData.kt`)

---

## File: `app/src/main/java/com/example/data/local/SeedCatalogData.kt`
```kotlin
package com.example.data.local

import com.example.model.Brand
import com.example.model.Category
import com.example.model.DeliverySlot
import com.example.model.OrderLineItem
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ProductReview
import com.example.model.ProductVariant
import com.example.model.SavedAddress
import com.example.model.StoreConfig
import com.example.model.UserProfile

object SeedCatalogData {

    val categories: List<Category> = listOf(
        Category("cat_veg", "Fresh Vegetables", "Farm-picked daily", "veg", 0xFFE8F7EA, 1),
        Category("cat_fruits", "Fresh Fruits", "Seasonal & sweet", "fruits", 0xFFFFF1E6, 2),
        Category("cat_dairy", "Dairy & Bread", "Milk, curd, eggs", "dairy", 0xFFE8F3FF, 3),
        Category("cat_staples", "Atta, Rice & Dal", "Kitchen staples", "staples", 0xFFFEF6E4, 4),
        Category("cat_oil", "Masala & Oil", "Spices & ghee", "spices", 0xFFFFEBEB, 5),
        Category("cat_snacks", "Snacks & Namkeen", "Chips & bhujia", "snacks", 0xFFFFF4DE, 6),
        Category("cat_drinks", "Tea, Coffee & Juice", "Chai & coolers", "drinks", 0xFFEAF7F5, 7),
        Category("cat_bakery", "Biscuits & Cakes", "Cookies & rusks", "bakery", 0xFFFCEEF5, 8),
        Category("cat_breakfast", "Breakfast & Oats", "Cereals & spreads", "breakfast", 0xFFF0F4FF, 9),
        Category("cat_frozen", "Ice Cream & Frozen", "Tubs & momos", "frozen", 0xFFECF8FF, 10),
        Category("cat_cleaning", "Home & Cleaning", "Detergents & wipes", "cleaning", 0xFFECFDF5, 11),
        Category("cat_personal", "Personal Care", "Bath, hair & oral", "personal", 0xFFF5EEFF, 12)
    )

    private fun wikiLogo(fileName: String): String =
        "https://en.wikipedia.org/wiki/Special:FilePath/$fileName?width=480"

    val brands: List<Brand> = listOf(
        Brand("brand_hul", "Hindustan Unilever", "Brighter Everyday", "Top FMCG", 0xFF1F36C7, wikiLogo("Hindustan_Unilever_Logo.svg")),
        Brand("brand_dabur", "Dabur", "Celebrate Life", "Ayurvedic", 0xFF168341, wikiLogo("Dabur_Logo.svg")),
        Brand("brand_reckitt", "Reckitt Benckiser", "Hygiene & Care", "Trusted", 0xFFE60067, wikiLogo("Reckitt_logo.svg")),
        Brand("brand_tata", "Tata Global Beverages", "Unpolished & Tea", "Tata Trust", 0xFF1E56A0, wikiLogo("TATA-CONSUMER-PRODUCTS_BLUE_LOGO_Feb_13.png")),
        Brand("brand_itc", "ITC", "Aashirvaad & Sunfeast", "Bestseller", 0xFF0A3868, wikiLogo("ITC_Limited_Logo.svg")),
        Brand("brand_mondelez", "Mondelez", "Cadbury & Oreo", "Sweet Joy", 0xFF51247A, wikiLogo("Mondelez_international_2012_logo.svg")),
        Brand("brand_godrej", "Godrej", "Good Knight & Care", "Heritage", 0xFFC01F76, wikiLogo("Godrej_Logo.svg")),
        Brand("brand_pg", "Procter & Gamble", "Tide, Ariel & Pampers", "Global Care", 0xFF003DA5, wikiLogo("Procter_%26_Gamble_logo.svg")),
        Brand("brand_varun", "Varun Beverages", "Refreshing Drinks", "Chilled", 0xFF14286E, wikiLogo("Varun_Beverages_logo_2026.png")),
        Brand("brand_marico", "Marico", "Saffola & Parachute", "Healthy Living", 0xFF1E6BB8, wikiLogo("Marico_Logo.svg")),
        Brand("brand_nestle", "Nestle", "Good Food, Good Life", "Iconic", 0xFF005596, wikiLogo("Nestle_textlogo_blue.png")),
        Brand("brand_pepsico", "Pepsico", "Lay's & Quaker", "Snacks", 0xFF004B93, wikiLogo("Pepsico_logo.png")),
        Brand("brand_britannia", "Britannia", "Eat Healthy Think Better", "Bakery King", 0xFFD81E27, wikiLogo("Britannia_Industries_logo.svg")),
        Brand("brand_colgate", "Colgate Palmolive", "Smile & Oral Care", "#1 Oral", 0xFF0078C8, wikiLogo("Colgate_Palmolive.png")),
        Brand("brand_amul", "Amul", "The Taste of India", "Dairy Giant", 0xFF1E293B, wikiLogo("Amul_official_logo.svg")),
        Brand("brand_ferrero", "Ferrero", "Rocher & Nutella", "Gourmet", 0xFF5D3A1A, wikiLogo("Ferrero_-_logo_red_(Italy,_1964-).svg")),
        Brand("brand_rspl", "RSPL", "Ghadi Detergent", "Value Care", 0xFFC82829, wikiLogo("Ghadi_Detergent_Logo.png")),
        Brand("brand_parle", "Parle", "Parle-G & Monaco", "India's Own", 0xFFE11D2E, wikiLogo("Parle_Products_logo.svg")),
        Brand("brand_adani", "Adani Wilmar", "Fortune Foods", "Kitchen King", 0xFF0E7490, wikiLogo("Adani_Wilmar.svg")),
        Brand("brand_zydus", "Zydus Wellness", "Glucon-D & Nutralite", "Wellness", 0xFF0284C7, wikiLogo("Zydus_Lifesciences.svg")),
        Brand("brand_haldiram", "Haldiram's", "Authentic Snacks", "Classic", 0xFFC81E2B, wikiLogo("Haldiram's_Logo_SVG.svg")),
        Brand("brand_kellogg", "Kellogg's", "Corn Flakes & Chocos", "Breakfast", 0xFFD3122A, wikiLogo("Kellogg's-Logo.svg")),
        Brand("brand_bajaj", "Bajaj", "Almond Drops Hair Care", "Trusted Oil", 0xFFD93829, wikiLogo("Bajaj_Consumer_Care_logo.png"))
    )

    private fun v(
        pid: String,
        idx: Int,
        label: String,
        mrp: Double,
        sell: Double,
        margin: Double,
        stock: Int
    ) = ProductVariant("${pid}_v$idx", label, mrp, sell, margin, stock)

    private fun reviewsFor(name: String, baseRating: Double): List<ProductReview> = listOf(
        ProductReview(
            id = "rev_${name.hashCode()}_1",
            authorName = "Priya Nair",
            rating = (baseRating + 0.2).coerceAtMost(5.0),
            comment = "Super fresh $name delivered in 9 minutes! Packaging was neat and sealed.",
            dateLabel = "2 days ago",
            isVerified = true
        ),
        ProductReview(
            id = "rev_${name.hashCode()}_2",
            authorName = "Rohan Kulkarni",
            rating = baseRating.coerceAtLeast(3.8),
            comment = "Great value for money compared to local store MRP. Will reorder.",
            dateLabel = "5 days ago",
            isVerified = true
        ),
        ProductReview(
            id = "rev_${name.hashCode()}_3",
            authorName = "Ananya Verma",
            rating = (baseRating - 0.1).coerceAtLeast(3.5),
            comment = "Good quality and accurate weight. QuickKart delivery partner was polite.",
            dateLabel = "1 week ago",
            isVerified = true
        )
    )

    private fun resolveSubBrand(id: String, brandId: String): String = when (id) {
        "p01", "p02", "p03", "p04", "p05" -> "ITC Farmland Veggies"
        "p06", "p07", "p08", "p09", "p10" -> "ITC Farmland Fruits"
        "p11", "p34" -> "Amul Taaza & Kool"
        "p12", "p43", "p113" -> "Amul Butter & Spread"
        "p13", "p78" -> "Nestle a+"
        "p14" -> "Amul Paneer"
        "p15", "p40", "p77" -> "Britannia Bread & Cakes"
        "p16", "p20", "p76" -> "Aashirvaad"
        "p17", "p19", "p72", "p73", "p74", "p85", "p114" -> "Tata Sampann"
        "p18", "p21", "p25", "p112" -> "Fortune Oils & Rice"
        "p22" -> "Amul Ghee"
        "p23", "p24", "p75" -> "Tata Salt & Spices"
        "p26" -> "Haldiram's Bhujia"
        "p27", "p28", "p30", "p84", "p86", "p87", "p115" -> "Haldiram's Namkeen"
        "p29" -> "Lay's & Quaker"
        "p31" -> "Tata Tea"
        "p32", "p33", "p80" -> "Tropicana & Aquafina"
        "p35", "p111" -> "Real"
        "p36" -> "Good Day"
        "p37" -> "Bourbon"
        "p38" -> "NutriChoice"
        "p39" -> "Toastea"
        "p41", "p45", "p81" -> "Dabur Honey"
        "p42", "p44", "p109", "p118" -> "Tata Soulfull"
        "p46", "p49", "p83" -> "Amul Ice Cream & Choco"
        "p47", "p48", "p50" -> "Saffola & Parachute"
        "p51", "p52", "p55", "p99" -> "Odonil"
        "p53", "p54", "p94", "p96", "p97" -> "Lizol & Harpic"
        "p56" -> "Dabur Red"
        "p57", "p60", "p92" -> "Dabur Amla"
        "p58", "p89", "p91" -> "Gulabari"
        "p59", "p88", "p95", "p98" -> "Lifebuoy & Surf Excel"
        "p61", "p62", "p63", "p64", "p65", "p100", "p102", "p103" -> "Tata Consumer Picks"
        "p66", "p67", "p68", "p69", "p70", "p71", "p101" -> "ITC Farmland Fresh"
        "p79" -> "Tata Coffee"
        "p82" -> "Tata Copper+"
        "p90" -> "Dabur Chyawanprash"
        "p93" -> "Bajaj Almond Drops"
        "p104", "p105", "p106", "p107", "p108" -> "Godrej Real Good"
        "p110" -> "Ferrero Rocher"
        "p116", "p119" -> "Pampers & Whisper"
        "p117" -> "Palmolive & Colgate"
        "p120" -> "Kissan"
        "p121" -> "Red Label"
        "p122" -> "Horlicks"
        "p123" -> "Lux"
        "p124" -> "Dettol"
        "p125" -> "Cadbury"
        "p126" -> "Oreo"
        "p127" -> "Bournvita"
        "p128" -> "Good Knight"
        "p129" -> "Tide"
        "p130" -> "Ariel"
        "p131" -> "Sting"
        "p132" -> "Saffola"
        "p133" -> "MAGGI"
        "p134" -> "Nescafe"
        "p135" -> "KitKat"
        "p136" -> "Lay's"
        "p137" -> "Kurkure"
        "p138" -> "Colgate"
        "p139" -> "Nutella"
        "p140" -> "Ghadi"
        "p141" -> "Parle-G"
        "p142" -> "Hide & Seek"
        "p143" -> "Monaco"
        "p144" -> "Sugar Free"
        "p145" -> "Glucon-D"
        "p146" -> "Nutralite"
        "p147" -> "Kellogg's Corn Flakes"
        "p148" -> "Kellogg's Chocos"
        "p149" -> "Bajaj Almond Drops"
        "p150" -> "Sunfeast"
        "p151", "p155", "p158", "p160", "p164", "p166" -> "Fortune"
        "p152", "p156", "p157", "p161", "p162", "p163", "p165" -> "Tata Sampann"
        "p153", "p159", "p167" -> "Aashirvaad"
        "p154" -> "Saffola"
        else -> brands.firstOrNull { it.id == brandId }?.name ?: "QuickKart"
    }

    private fun resolveShopCategoryAndSub(id: String): Pair<String, String> = when (id) {
        "p01", "p02", "p03", "p04" -> "Fruits & Vegetables" to "Fresh Vegetables"
        "p05" -> "Fruits & Vegetables" to "Herbs & Seasonings"
        "p06", "p07", "p08", "p09", "p10" -> "Fruits & Vegetables" to "Fresh Fruits"
        "p11", "p12", "p13", "p14" -> "Bakery, Cakes & Dairy" to "Dairy"
        "p15" -> "Bakery, Cakes & Dairy" to "Breads & Buns"
        "p16", "p20" -> "Foodgrains, Oil & Masala" to "Atta, Flours & Sooji"
        "p17", "p19" -> "Foodgrains, Oil & Masala" to "Dals & Pulses"
        "p18" -> "Foodgrains, Oil & Masala" to "Rice & Rice Products"
        "p21", "p22", "p25" -> "Foodgrains, Oil & Masala" to "Edible Oils & Ghee"
        "p23", "p24" -> "Foodgrains, Oil & Masala" to "Masalas & Spices"
        "p26", "p27", "p28", "p29", "p30" -> "Snacks & Branded Foods" to "Snacks & Namkeen"
        "p31" -> "Beverages" to "Tea"
        "p32", "p33", "p35" -> "Beverages" to "Fruit Juices & Drinks"
        "p34" -> "Beverages" to "Energy & Soft Drinks"
        "p36", "p37", "p38" -> "Snacks & Branded Foods" to "Biscuits & Cookies"
        "p39" -> "Bakery, Cakes & Dairy" to "Cookies, Rusk & Khari"
        "p40" -> "Bakery, Cakes & Dairy" to "Cakes & Pastries"
        "p41", "p43", "p45" -> "Snacks & Branded Foods" to "Spreads, Sauces, Ketchup"
        "p42", "p44" -> "Snacks & Branded Foods" to "Breakfast Cereals"
        "p46", "p47", "p48", "p49", "p50" -> "Gourmet & World Food" to "Tinned & Processed Food"
        "p51", "p54" -> "Cleaning & Household" to "Detergents & Dishwash"
        "p52", "p55" -> "Cleaning & Household" to "Fresheners & Repellents"
        "p53" -> "Cleaning & Household" to "All Purpose Cleaners"
        "p56" -> "Beauty & Hygiene" to "Oral Care"
        "p57", "p60" -> "Beauty & Hygiene" to "Hair Care"
        "p58" -> "Beauty & Hygiene" to "Skin Care"
        "p59" -> "Beauty & Hygiene" to "Bath & Hand Wash"
        else -> "Snacks & Branded Foods" to "Snacks & Namkeen"
    }

    private fun p(
        id: String,
        name: String,
        desc: String,
        cat: String,
        brand: String,
        img: String,
        colorHex: Long,
        rating: Double,
        reviewCount: Int,
        featured: Boolean,
        variants: List<ProductVariant>,
        shopCategoryOverride: String? = null,
        subCategoryOverride: String? = null,
        isVeg: Boolean = true
    ): Product {
        val (defaultShopCat, defaultSubCat) = resolveShopCategoryAndSub(id)
        return Product(
            id = id,
            name = name,
            description = desc,
            categoryId = cat,
            brandId = brand,
            subBrand = resolveSubBrand(id, brand),
            shopCategory = shopCategoryOverride ?: defaultShopCat,
            subCategory = subCategoryOverride ?: defaultSubCat,
            isVeg = isVeg,
            imageKey = img,
            colorHex = colorHex,
            rating = rating,
            reviewCount = reviewCount,
            isFeatured = featured,
            variants = variants,
            reviews = reviewsFor(name, rating)
        )
    }

    val products: List<Product> = listOf(
        p("p01", "Hybrid Red Tomato", "Juicy, firm, farm-harvested red tomatoes ideal for curries, dals, and fresh salads.", "cat_veg", "brand_itc", "tomato", 0xFFFFE4E6, 4.6, 1240, true, listOf(v("p01", 1, "500 g", 36.0, 24.0, 22.0, 45), v("p01", 2, "1 kg", 68.0, 44.0, 24.0, 30))),
        p("p02", "Nashik Red Onion", "Crisp, pungent Nashik red onions sorted and cleaned for everyday Indian cooking.", "cat_veg", "brand_itc", "onion", 0xFFFCE7F3, 4.5, 2110, true, listOf(v("p02", 1, "1 kg", 55.0, 38.0, 19.0, 60), v("p02", 2, "2 kg", 108.0, 72.0, 21.0, 25))),
        p("p03", "Agra Jyoti Potato", "Freshly dug, thin-skinned potatoes great for aloo paratha, sabzi, and fries.", "cat_veg", "brand_itc", "potato", 0xFFFEF3C7, 4.4, 1890, false, listOf(v("p03", 1, "1 kg", 44.0, 32.0, 18.0, 50), v("p03", 2, "2 kg", 84.0, 59.0, 20.0, 28))),
        p("p04", "Tender Bhindi (Okra)", "Young, snap-fresh green lady finger hand-sorted for minimal seeds and crisp stir-fry.", "cat_veg", "brand_itc", "okra", 0xFFDCFCE7, 4.7, 680, false, listOf(v("p04", 1, "250 g", 28.0, 19.0, 25.0, 22), v("p04", 2, "500 g", 54.0, 36.0, 26.0, 14))),
        p("p05", "Fresh Coriander Bunch", "Aromatic dhaniya leaves with tender stems, triple-washed and packed chilled.", "cat_veg", "brand_itc", "coriander", 0xFFD1FAE5, 4.8, 950, false, listOf(v("p05", 1, "100 g", 20.0, 12.0, 32.0, 0), v("p05", 2, "250 g", 45.0, 28.0, 30.0, 0))),
        p("p06", "Robusta Banana", "Naturally ripened sweet bananas packed with potassium and morning energy.", "cat_fruits", "brand_itc", "banana", 0xFFFEF9C3, 4.6, 1520, true, listOf(v("p06", 1, "6 pcs", 48.0, 35.0, 20.0, 40), v("p06", 2, "12 pcs", 92.0, 66.0, 22.0, 18))),
        p("p07", "Shimla Royal Gala Apple", "Crunchy, naturally sweet orchard apples with vibrant crimson skin.", "cat_fruits", "brand_itc", "apple", 0xFFFEE2E2, 4.7, 890, true, listOf(v("p07", 1, "4 pcs (500 g)", 140.0, 109.0, 18.0, 24), v("p07", 2, "1 kg", 270.0, 209.0, 19.5, 16))),
        p("p08", "Bhagwa Pomegranate (Anar)", "Deep ruby-red juicy arils bursting with antioxidants and sweet-tart flavor.", "cat_fruits", "brand_itc", "pomegranate", 0xFFFCE7F3, 4.5, 540, false, listOf(v("p08", 1, "2 pcs (450 g)", 135.0, 99.0, 21.0, 20), v("p08", 2, "4 pcs (900 g)", 260.0, 189.0, 22.0, 10))),
        p("p09", "Ratnagiri Alphonso Mango", "GI-tagged rich, saffron-fleshed Alphonso mangoes, carbide-free ripened.", "cat_fruits", "brand_itc", "mango", 0xFFFFEDD5, 4.9, 1340, true, listOf(v("p09", 1, "2 pcs (500 g)", 250.0, 199.0, 24.0, 15), v("p09", 2, "6 pcs (1.5 kg)", 720.0, 569.0, 25.0, 8))),
        p("p10", "Nagpur Sweet Orange", "Easy-peel citrus oranges loaded with vitamin C, fresh from Nagpur groves.", "cat_fruits", "brand_itc", "orange", 0xFFFFEDD5, 4.3, 410, false, listOf(v("p10", 1, "500 g", 65.0, 49.0, 19.0, 30), v("p10", 2, "1 kg", 125.0, 89.0, 20.0, 16))),
        p("p11", "Amul Taaza Toned Milk", "Pasteurized toned milk with 3% fat, ideal for daily tea, coffee, and curd.", "cat_dairy", "brand_amul", "milk", 0xFFE0F2FE, 4.8, 4200, true, listOf(v("p11", 1, "500 ml", 28.0, 27.0, 8.5, 90), v("p11", 2, "1 L Tetra", 74.0, 69.0, 10.0, 55))),
        p("p12", "Amul Pasteurised Butter", "Iconic creamy salted butter perfect for pav bhaji, toast, and parathas.", "cat_dairy", "brand_amul", "butter", 0xFFFEF3C7, 4.9, 5100, true, listOf(v("p12", 1, "100 g", 60.0, 56.0, 11.0, 65), v("p12", 2, "500 g", 285.0, 265.0, 12.5, 25))),
        p("p13", "Nestle a+ Nourish Probiotic Dahi", "Thick, creamy probiotic curd set hygienically for lunch, raita, and lassi.", "cat_dairy", "brand_nestle", "curd", 0xFFE0F2FE, 4.7, 1980, false, listOf(v("p13", 1, "200 g Cup", 28.0, 25.0, 14.0, 40), v("p13", 2, "400 g Pack", 54.0, 48.0, 15.0, 35))),
        p("p14", "Amul Fresh Malai Paneer", "Soft, melt-in-mouth cottage cheese cubes rich in protein and calcium.", "cat_dairy", "brand_amul", "paneer", 0xFFF0F9FF, 4.7, 2310, true, listOf(v("p14", 1, "200 g", 95.0, 86.0, 16.0, 38), v("p14", 2, "500 g", 225.0, 199.0, 17.5, 14))),
        p("p15", "Britannia 100% Whole Wheat Bread", "Soft zero-maida whole wheat slices baked fresh every morning.", "cat_dairy", "brand_britannia", "bread", 0xFFFEF3C7, 4.5, 1760, false, listOf(v("p15", 1, "400 g", 55.0, 50.0, 13.0, 30), v("p15", 2, "800 g Family", 105.0, 92.0, 14.5, 12))),
        p("p16", "Aashirvaad Shudh Chakki Atta", "100% whole wheat flour ground with traditional chakki process for soft rotis.", "cat_staples", "brand_itc", "atta", 0xFFFEF3C7, 4.8, 3800, true, listOf(v("p16", 1, "5 kg", 295.0, 249.0, 14.0, 30), v("p16", 2, "10 kg", 575.0, 479.0, 15.5, 18))),
        p("p17", "Tata Sampann Unpolished Toor Dal", "High-protein unpolished arhar dal retaining natural taste and nutrients.", "cat_staples", "brand_tata", "dal", 0xFFFFEDD5, 4.7, 1490, true, listOf(v("p17", 1, "500 g", 118.0, 95.0, 17.0, 42), v("p17", 2, "1 kg", 230.0, 182.0, 18.5, 28))),
        p("p18", "Fortune Rozana Basmati Rice", "Aged medium-long grain basmati rice ideal for everyday pulao and steamed rice.", "cat_staples", "brand_adani", "rice", 0xFFFEF9C3, 4.6, 1620, false, listOf(v("p18", 1, "1 kg", 115.0, 89.0, 19.0, 36), v("p18", 2, "5 kg", 545.0, 419.0, 20.5, 15))),
        p("p19", "Tata Sampann Thick Poha", "Flattened rice flakes that stay fluffy without turning mushy for morning kanda poha.", "cat_staples", "brand_tata", "poha", 0xFFFEF3C7, 4.5, 980, false, listOf(v("p19", 1, "500 g", 62.0, 48.0, 21.0, 44), v("p19", 2, "1 kg", 120.0, 91.0, 22.0, 22))),
        p("p20", "Aashirvaad Select Sharbati Atta", "Premium MP Sharbati wheat flour making sweet, pillowy phulkas that stay soft longer.", "cat_staples", "brand_itc", "atta", 0xFFFFEDD5, 4.8, 1120, false, listOf(v("p20", 1, "5 kg", 345.0, 299.0, 15.0, 0))),
        p("p21", "Fortune Sunlite Refined Oil", "Light, vitamin-enriched sunflower oil for crisp frying and daily tadka.", "cat_oil", "brand_adani", "oil", 0xFFFEF9C3, 4.7, 2940, true, listOf(v("p21", 1, "1 L Pouch", 165.0, 139.0, 12.0, 48), v("p21", 2, "5 L Jar", 820.0, 689.0, 13.5, 14))),
        p("p22", "Amul Pure Cow Ghee", "Rich golden danedar cow ghee with traditional aroma for sweets and rotis.", "cat_oil", "brand_amul", "ghee", 0xFFFEF3C7, 4.8, 3150, true, listOf(v("p22", 1, "500 ml", 350.0, 315.0, 14.0, 30), v("p22", 2, "1 L Tin", 685.0, 619.0, 15.0, 20))),
        p("p23", "Tata Sampann Turmeric Powder", "Salem haldi with 3% natural curcumin content and vibrant golden colour.", "cat_oil", "brand_tata", "spice", 0xFFFEF08A, 4.7, 1340, false, listOf(v("p23", 1, "200 g", 78.0, 62.0, 24.0, 50), v("p23", 2, "500 g", 185.0, 145.0, 25.0, 25))),
        p("p24", "Tata Sampann Garam Masala", "Chef-crafted blend of roasted whole spices with locked natural oils.", "cat_oil", "brand_tata", "spice", 0xFFFEE2E2, 4.6, 870, false, listOf(v("p24", 1, "100 g", 96.0, 79.0, 26.0, 42), v("p24", 2, "200 g", 184.0, 149.0, 27.0, 18))),
        p("p25", "Fortune Kachi Ghani Mustard Oil", "Cold-pressed pungent mustard oil perfect for North & East Indian delicacies.", "cat_oil", "brand_adani", "oil", 0xFFFFEDD5, 4.6, 1580, false, listOf(v("p25", 1, "1 L Bottle", 185.0, 155.0, 13.0, 32), v("p25", 2, "2 L Jar", 365.0, 299.0, 14.5, 16))),
        p("p26", "Haldiram's Aloo Bhujia", "Crispy, spiced potato & besan sev strands — India's all-time teatime favourite.", "cat_snacks", "brand_haldiram", "namkeen", 0xFFFFEDD5, 4.8, 3600, true, listOf(v("p26", 1, "200 g", 55.0, 49.0, 20.0, 60), v("p26", 2, "400 g", 105.0, 92.0, 22.0, 35), v("p26", 3, "1 kg Party", 250.0, 209.0, 24.0, 15))),
        p("p27", "Haldiram's Khatta Meetha", "Sweet and tangy medley of crispy boondi, sev, green peas, and peanuts.", "cat_snacks", "brand_haldiram", "namkeen", 0xFFFEF3C7, 4.7, 1920, false, listOf(v("p27", 1, "200 g", 50.0, 44.0, 21.0, 45), v("p27", 2, "400 g", 98.0, 84.0, 22.0, 22))),
        p("p28", "Haldiram's Salted Moong Dal", "Golden fried split yellow moong dal lightly tossed in rock salt.", "cat_snacks", "brand_haldiram", "snacks", 0xFFFEF9C3, 4.6, 1420, false, listOf(v("p28", 1, "200 g", 60.0, 52.0, 20.0, 50), v("p28", 2, "400 g", 118.0, 99.0, 21.5, 20))),
        p("p29", "Quaker Rolled Multigrain Oats Crunch", "Wholesome quick-cooking energy grain bites for light snacking.", "cat_snacks", "brand_pepsico", "oats", 0xFFF3E8FF, 4.5, 690, true, listOf(v("p29", 1, "400 g", 110.0, 89.0, 24.0, 28), v("p29", 2, "1 kg", 225.0, 179.0, 25.0, 12))),
        p("p30", "Haldiram's Classic Kaju Mixture", "Rich festive namkeen loaded with whole roasted cashews and spiced potato sticks.", "cat_snacks", "brand_haldiram", "namkeen", 0xFFFFEDD5, 4.7, 880, false, listOf(v("p30", 1, "200 g", 110.0, 94.0, 23.0, 0))),
        p("p31", "Tata Tea Gold Assam Blend", "Rich CTC tea leaves blended with 15% long aromatic Assam leaves.", "cat_drinks", "brand_tata", "tea", 0xFFE0F2FE, 4.8, 3400, true, listOf(v("p31", 1, "250 g", 160.0, 138.0, 16.0, 50), v("p31", 2, "500 g", 315.0, 265.0, 17.5, 30), v("p31", 3, "1 kg", 610.0, 499.0, 19.0, 16))),
        p("p32", "Tropicana 100% Valencia Orange Juice", "No-added-sugar pure fruit goodness pressed from ripe oranges.", "cat_drinks", "brand_varun", "juice", 0xFFFFEDD5, 4.7, 1780, true, listOf(v("p32", 1, "200 ml", 35.0, 30.0, 22.0, 60), v("p32", 2, "1 L Pack", 140.0, 115.0, 24.0, 25))),
        p("p33", "Tropicana Guava Nectar", "Velvety pink guava fruit beverage chilled and refreshing for afternoons.", "cat_drinks", "brand_varun", "juice", 0xFFDCFCE7, 4.6, 1140, false, listOf(v("p33", 1, "200 ml", 25.0, 20.0, 25.0, 70), v("p33", 2, "1 L Tetra", 110.0, 89.0, 26.0, 24))),
        p("p34", "Amul Kool Kesar Milk", "Chilled saffron-flavoured toned milk in a ready-to-sip bottle.", "cat_drinks", "brand_amul", "milkshake", 0xFFFEF3C7, 4.6, 1450, false, listOf(v("p34", 1, "180 ml", 25.0, 23.0, 16.0, 55), v("p34", 2, "Pack of 6", 150.0, 132.0, 18.0, 18))),
        p("p35", "Dabur Real Mixed Fruit Juice", "No added preservatives blend of 9 real fruits rich in vitamins.", "cat_drinks", "brand_dabur", "juice", 0xFFFCE7F3, 4.5, 2240, false, listOf(v("p35", 1, "1 L Tetra", 130.0, 105.0, 20.0, 40), v("p35", 2, "2 x 1 L Combo", 260.0, 199.0, 22.0, 20))),
        p("p36", "Britannia Good Day Butter", "Crisp golden butter cookies baked for evening chai dipping.", "cat_bakery", "brand_britannia", "biscuit", 0xFFFEF3C7, 4.7, 2900, true, listOf(v("p36", 1, "200 g", 45.0, 38.0, 18.0, 64), v("p36", 2, "600 g Mega", 125.0, 99.0, 20.0, 28))),
        p("p37", "Britannia Bourbon Chocolate", "Rich chocolate cream sandwiched between crunchy sugar-sprinkled biscuits.", "cat_bakery", "brand_britannia", "cookie", 0xFFEDE9FE, 4.8, 2150, false, listOf(v("p37", 1, "150 g", 40.0, 34.0, 19.0, 48), v("p37", 2, "300 g Twin", 78.0, 64.0, 20.5, 24))),
        p("p38", "Britannia NutriChoice Digestive", "High-fibre whole wheat biscuits crafted for mindful snacking.", "cat_bakery", "brand_britannia", "biscuit", 0xFFDCFCE7, 4.5, 1680, false, listOf(v("p38", 1, "250 g", 60.0, 50.0, 18.0, 38), v("p38", 2, "1 kg Saver", 220.0, 175.0, 20.0, 15))),
        p("p39", "Britannia Toastea Premium Rusk", "Twice-baked crispy elaichi milk rusks for a comforting morning cuppa.", "cat_bakery", "brand_britannia", "rusk", 0xFFFFEDD5, 4.6, 1390, false, listOf(v("p39", 1, "300 g", 55.0, 46.0, 19.0, 40), v("p39", 2, "700 g Value", 120.0, 96.0, 21.0, 20))),
        p("p40", "Britannia Gobbles Fruit Cake", "Soft, moist sponge bar studded with colourful tutti-frutti bits.", "cat_bakery", "brand_britannia", "cake", 0xFFFCE7F3, 4.4, 720, false, listOf(v("p40", 1, "120 g", 40.0, 35.0, 22.0, 32), v("p40", 2, "240 g Pack", 78.0, 66.0, 23.0, 14))),
        p("p41", "Dabur Wild Forest Honey", "100% pure NMR-tested natural honey collected from wild Indian forests.", "cat_breakfast", "brand_dabur", "honey", 0xFFFEF3C7, 4.8, 3300, true, listOf(v("p41", 1, "250 g", 145.0, 118.0, 22.0, 42), v("p41", 2, "500 g", 275.0, 219.0, 23.5, 25), v("p41", 3, "1 kg Bottle", 495.0, 389.0, 25.0, 12))),
        p("p42", "Tata Soulfull Masala Oats", "Desi classic masala oats fortified with millets for a quick 3-minute breakfast.", "cat_breakfast", "brand_tata", "oats", 0xFFDCFCE7, 4.5, 1120, false, listOf(v("p42", 1, "400 g", 150.0, 119.0, 21.0, 34), v("p42", 2, "1 kg Pouch", 330.0, 259.0, 23.0, 16))),
        p("p43", "Amul Choco Spread", "Velvety hazelnut-cocoa spread made with real milk solids and zero palm oil.", "cat_breakfast", "brand_amul", "spread", 0xFFF3E8FF, 4.6, 890, false, listOf(v("p43", 1, "200 g Jar", 140.0, 119.0, 19.0, 28), v("p43", 2, "400 g Jar", 265.0, 224.0, 20.0, 14))),
        p("p44", "Tata Soulfull Crunchy Ragi Bites", "Millet-packed choco-filled pillows kids love with chilled milk.", "cat_breakfast", "brand_tata", "cereal", 0xFFE0F2FE, 4.6, 760, false, listOf(v("p44", 1, "250 g", 155.0, 125.0, 22.0, 25), v("p44", 2, "500 g", 295.0, 235.0, 23.0, 10))),
        p("p45", "Dabur Crunchy Peanut Butter", "High-protein roasted peanut butter with crunchy bits for smoothies & toast.", "cat_breakfast", "brand_dabur", "spread", 0xFFFFEDD5, 4.7, 940, false, listOf(v("p45", 1, "340 g", 180.0, 139.0, 24.0, 26), v("p45", 2, "900 g Tub", 399.0, 299.0, 26.0, 12))),
        p("p46", "Amul Gold Belgian Chocolate Tub", "Rich, velvety Belgian dark chocolate ice cream made from real milk.", "cat_frozen", "brand_amul", "icecream", 0xFFEDE9FE, 4.8, 1890, true, listOf(v("p46", 1, "750 ml Tub", 240.0, 205.0, 21.0, 24), v("p46", 2, "1.25 L Party", 360.0, 299.0, 22.0, 12))),
        p("p47", "Saffola Cold-Pressed Extra Virgin Blend", "Heart-friendly wholesome cold-filtered culinary blend.", "cat_frozen", "brand_marico", "oil", 0xFFFFEDD5, 4.7, 1140, false, listOf(v("p47", 1, "500 ml", 210.0, 175.0, 20.0, 20), v("p47", 2, "1 L Bottle", 390.0, 319.0, 22.0, 10))),
        p("p48", "ITC Master Chef Frozen Green Peas", "Flash-frozen sweet green peas ready to cook in minutes all year round.", "cat_frozen", "brand_itc", "peas", 0xFFDCFCE7, 4.6, 2040, false, listOf(v("p48", 1, "500 g", 115.0, 95.0, 18.0, 35), v("p48", 2, "1 kg", 215.0, 175.0, 19.5, 22))),
        p("p49", "Amul Tricone Butterscotch", "Crunchy wafer cone filled with creamy butterscotch and roasted cashews.", "cat_frozen", "brand_amul", "cone", 0xFFFEF3C7, 4.7, 1560, false, listOf(v("p49", 1, "120 ml Single", 45.0, 42.0, 18.0, 38), v("p49", 2, "Pack of 4", 180.0, 159.0, 20.0, 14))),
        p("p50", "Amul Punjabi Kesar Pista Kulfi", "Traditional slow-simmered rabri kulfi stick topped with saffron & pistachios.", "cat_frozen", "brand_amul", "kulfi", 0xFFFEF9C3, 4.8, 920, false, listOf(v("p50", 1, "60 ml Stick", 35.0, 32.0, 22.0, 0))),
        p("p51", "Dabur Odonil Room Freshener", "Long-lasting lavender mist fragrance block keeping bathrooms fresh 24x7.", "cat_cleaning", "brand_dabur", "freshener", 0xFFF3E8FF, 4.5, 1430, false, listOf(v("p51", 1, "50 g", 62.0, 52.0, 25.0, 45), v("p51", 2, "Pack of 4", 240.0, 189.0, 27.0, 18))),
        p("p52", "Dabur Sanitize Disinfectant Spray", "Kills 99.9% germs on high-touch surfaces, sofas, and parcel boxes.", "cat_cleaning", "brand_dabur", "spray", 0xFFE0F2FE, 4.4, 640, false, listOf(v("p52", 1, "200 ml", 160.0, 119.0, 28.0, 30), v("p52", 2, "500 ml", 320.0, 239.0, 30.0, 15))),
        p("p53", "Vim Lemon Dishwash Gel", "Power of 100 lemons concentrated grease-cutting dishwash liquid.", "cat_cleaning", "brand_hul", "dishwash", 0xFFFEF9C3, 4.6, 890, true, listOf(v("p53", 1, "500 ml", 115.0, 95.0, 24.0, 42), v("p53", 2, "1.5 L Pouch", 295.0, 239.0, 26.0, 20))),
        p("p54", "Lizol Floral Disinfectant Floor Cleaner", "Kills 99.9% germs and leaves marble and tile floors sparkling clean.", "cat_cleaning", "brand_reckitt", "cleaner", 0xFFDCFCE7, 4.7, 1760, false, listOf(v("p54", 1, "1 L Bottle", 215.0, 179.0, 22.0, 35), v("p54", 2, "2 L Can", 399.0, 329.0, 24.0, 18))),
        p("p55", "Dabur Odopic Dishwash Bar", "Compact power scrub bar with active lemon minerals for sparkling steel utensils.", "cat_cleaning", "brand_dabur", "soap", 0xFFDCFCE7, 4.3, 520, false, listOf(v("p55", 1, "200 g", 25.0, 21.0, 24.0, 60), v("p55", 2, "Pack of 3", 75.0, 59.0, 26.0, 28))),
        p("p56", "Dabur Red Ayurvedic Paste", "Clinically proven 13-herb dental formula for strong gums and fresh breath.", "cat_personal", "brand_dabur", "toothpaste", 0xFFFEE2E2, 4.7, 2650, true, listOf(v("p56", 1, "200 g", 135.0, 109.0, 22.0, 55), v("p56", 2, "400 g Saver", 255.0, 199.0, 24.0, 28))),
        p("p57", "Dabur Vatika Enriched Coconut Oil", "Hibiscus & amla infused coconut hair oil for thicker, lustrous hair.", "cat_personal", "brand_dabur", "hairoil", 0xFFDCFCE7, 4.6, 1820, false, listOf(v("p57", 1, "300 ml", 175.0, 139.0, 23.0, 38), v("p57", 2, "500 ml", 280.0, 215.0, 25.0, 20))),
        p("p58", "Dabur Gulabari Rose Water", "100% pure steam-distilled Kannauj rose water toner for natural glow.", "cat_personal", "brand_dabur", "skincare", 0xFFFCE7F3, 4.7, 1540, false, listOf(v("p58", 1, "120 ml", 65.0, 54.0, 26.0, 44), v("p58", 2, "250 ml", 115.0, 92.0, 28.0, 24))),
        p("p59", "Lifebuoy Total 10 Germ Protection Soap", "Trusted Activ Silver+ formula protecting your family from daily germs.", "cat_personal", "brand_hul", "soap", 0xFFFEE2E2, 4.6, 1670, false, listOf(v("p59", 1, "125 g Single", 40.0, 36.0, 20.0, 48), v("p59", 2, "Pack of 4", 155.0, 129.0, 22.0, 22))),
        p("p60", "Dabur Amla Hair Oil", "Classic Indian gooseberry hair nourishment strengthening roots from within.", "cat_personal", "brand_dabur", "hairoil", 0xFFDCFCE7, 4.6, 2110, false, listOf(v("p60", 1, "275 ml", 155.0, 129.0, 21.0, 40), v("p60", 2, "550 ml", 290.0, 235.0, 23.0, 18))),
        p("p61", "boAt BassHeads 100 Wired Earphones", "Hawk-inspired in-ear wired earphones with HD mic and deep bass.", "cat_cleaning", "brand_tata", "headphones", 0xFFDBEAFE, 4.5, 980, false, listOf(v("p61", 1, "1 Unit", 999.0, 399.0, 18.0, 18)), "Electronics", "Audio devices"),
        p("p62", "Duracell Ultra Alkaline AA Batteries", "Long-lasting power for remotes, toys, and wireless accessories.", "cat_cleaning", "brand_tata", "electrical", 0xFFE0F2FE, 4.8, 1240, false, listOf(v("p62", 1, "Pack of 4", 180.0, 155.0, 20.0, 42)), "Electronics", "Electrical Accessories"),
        p("p63", "Havells 4-Socket Spike Guard Extension", "Surge-protected 1.5m extension board with master switch.", "cat_cleaning", "brand_tata", "electrical", 0xFFE0F2FE, 4.6, 410, false, listOf(v("p63", 1, "1.5 m Cord", 650.0, 449.0, 19.0, 15)), "Electronics", "Electrical Accessories"),
        p("p64", "Philips Series 1000 Beard Trimmer", "Self-sharpening stainless blades with 45 min cordless runtime.", "cat_personal", "brand_tata", "grooming", 0xFFE2E8F0, 4.6, 860, false, listOf(v("p64", 1, "1 Unit", 1495.0, 1099.0, 16.0, 12)), "Electronics", "Personal Care & Grooming"),
        p("p65", "Noise ColorFit Pulse Smartwatch", "1.4-inch HD display smartwatch with SpO2 and heart rate tracking.", "cat_personal", "brand_tata", "watch", 0xFFE0E7FF, 4.4, 710, false, listOf(v("p65", 1, "Jet Black", 4999.0, 1499.0, 18.0, 9)), "Electronics", "Smart Wearables"),
        p("p66", "Farmland Moong & Chana Sprouts Mix", "Washed, ready-to-eat high-protein sprouted green moong and kala chana.", "cat_veg", "brand_itc", "okra", 0xFFDCFCE7, 4.7, 540, false, listOf(v("p66", 1, "200 g", 45.0, 35.0, 22.0, 34)), "Fruits & Vegetables", "Cuts & Sprouts"),
        p("p67", "Farmland Exotic Green Broccoli", "Crisp nutrient-rich broccoli florets ideal for stir-fries and salads.", "cat_veg", "brand_itc", "okra", 0xFFDCFCE7, 4.6, 620, false, listOf(v("p67", 1, "250 g", 80.0, 58.0, 21.0, 22)), "Fruits & Vegetables", "Exotic Fruits & Veggies"),
        p("p68", "Imported Hass Avocado", "Creamy ripe-to-eat imported avocado rich in healthy fats.", "cat_fruits", "brand_itc", "apple", 0xFFDCFCE7, 4.7, 490, false, listOf(v("p68", 1, "1 Pc", 150.0, 119.0, 19.0, 16)), "Fruits & Vegetables", "Exotic Fruits & Veggies"),
        p("p69", "Fresh Marigold & Rose Puja Garland", "Hand-strung fresh orange marigold and rose garland for daily puja.", "cat_veg", "brand_itc", "flower", 0xFFFFE4E6, 4.8, 390, false, listOf(v("p69", 1, "1 Garland", 60.0, 45.0, 25.0, 28)), "Fruits & Vegetables", "Flower Bouquets, Bunches"),
        p("p70", "Farm Ginger & Pudina Combo", "Aromatic desi adrak paired with fresh mint leaves.", "cat_veg", "brand_itc", "coriander", 0xFFD1FAE5, 4.6, 770, false, listOf(v("p70", 1, "200 g Combo", 40.0, 29.0, 24.0, 45)), "Fruits & Vegetables", "Herbs & Seasonings"),
        p("p71", "Organic Baby Spinach (Palak)", "Pesticide-free tender organic spinach leaves, triple washed.", "cat_veg", "brand_itc", "okra", 0xFFDCFCE7, 4.7, 510, false, listOf(v("p71", 1, "250 g", 55.0, 42.0, 22.0, 30)), "Fruits & Vegetables", "Organic Fruits & Vegetables"),
        p("p72", "Tata Sampann California Almonds", "100% pure whole crunchy badam packed with natural vitamin E.", "cat_staples", "brand_tata", "dryfruit", 0xFFFEF3C7, 4.8, 1320, true, listOf(v("p72", 1, "250 g", 340.0, 249.0, 18.0, 35)), "Foodgrains, Oil & Masala", "Dry Fruits"),
        p("p73", "Tata Sampann Whole Cashews (Kaju)", "Jumbo W320 creamy whole cashews for snacking and rich gravies.", "cat_staples", "brand_tata", "dryfruit", 0xFFFEF3C7, 4.7, 940, false, listOf(v("p73", 1, "250 g", 360.0, 279.0, 17.0, 26)), "Foodgrains, Oil & Masala", "Dry Fruits"),
        p("p74", "Tata Sampann Organic Kabuli Chana", "Certified organic bold white chickpeas, unpolished and wholesome.", "cat_staples", "brand_tata", "dal", 0xFFFEF3C7, 4.7, 610, false, listOf(v("p74", 1, "500 g", 135.0, 112.0, 15.0, 40)), "Foodgrains, Oil & Masala", "Organic Staples"),
        p("p75", "Tata Salt Vacuum Iodised", "Desh ka namak — pure free-flowing iodised crystal salt.", "cat_oil", "brand_tata", "spice", 0xFFE0F2FE, 4.9, 4120, false, listOf(v("p75", 1, "1 kg", 28.0, 26.0, 12.0, 120)), "Foodgrains, Oil & Masala", "Salt, Sugar & Jaggery"),
        p("p76", "Aashirvaad Organic Kolhapuri Jaggery Powder", "Chemical-free natural sugarcane gud powder for chai and desserts.", "cat_oil", "brand_itc", "atta", 0xFFFEF3C7, 4.6, 530, false, listOf(v("p76", 1, "500 g", 85.0, 69.0, 19.0, 38)), "Foodgrains, Oil & Masala", "Salt, Sugar & Jaggery"),
        p("p77", "Britannia Garlic Croutons & Khari", "Crisp flaky oven-baked masala puff snacks for evening tea.", "cat_bakery", "brand_britannia", "rusk", 0xFFFFEDD5, 4.5, 480, false, listOf(v("p77", 1, "200 g", 65.0, 54.0, 18.0, 30)), "Bakery, Cakes & Dairy", "Bakery Snacks"),
        p("p78", "Nestle a+ Plant Almond Milk", "Lactose-free unsweetened roasted almond beverage.", "cat_drinks", "brand_nestle", "milk", 0xFFE0F2FE, 4.5, 340, false, listOf(v("p78", 1, "1 L", 260.0, 219.0, 18.0, 18)), "Bakery, Cakes & Dairy", "Non Dairy"),
        p("p79", "Tata Coffee Grand Classic Instant", "Rich flavour-locked coffee blend with freshly roasted chicory notes.", "cat_drinks", "brand_tata", "coffee", 0xFFFEF3C7, 4.7, 1180, false, listOf(v("p79", 1, "50 g Jar", 145.0, 119.0, 20.0, 34)), "Beverages", "Coffee"),
        p("p80", "Aquafina Packaged Drinking Water", "Pure crisp hydration in an easy-carry bottle.", "cat_drinks", "brand_varun", "water", 0xFFE0F2FE, 4.6, 720, false, listOf(v("p80", 1, "1 L", 20.0, 19.0, 22.0, 68)), "Beverages", "Energy & Soft Drinks"),
        p("p81", "Dabur Chyawanprash Nutri-Mix Granules", "Tasty chocolate immunity & energy nutrition drink mix for kids.", "cat_drinks", "brand_dabur", "honey", 0xFFFEF3C7, 4.6, 510, false, listOf(v("p81", 1, "500 g", 245.0, 205.0, 19.0, 24)), "Beverages", "Nutrition Mix, Supplement"),
        p("p82", "Tata Copper+ Natural Mineral Water", "Copper-charged hydration bottle for everyday wellness.", "cat_drinks", "brand_tata", "water", 0xFFE0F2FE, 4.7, 1650, false, listOf(v("p82", 1, "1 L Pack of 6", 120.0, 102.0, 18.0, 45)), "Beverages", "Water"),
        p("p83", "Amul Belgian Dark Chocolate Bar", "Rich 55% single-origin dark chocolate crafted with fine cocoa.", "cat_snacks", "brand_amul", "chocolate", 0xFFEDE9FE, 4.8, 1890, false, listOf(v("p83", 1, "150 g", 130.0, 114.0, 16.0, 38)), "Snacks & Branded Foods", "Chocolates & Candies"),
        p("p84", "Haldiram's Rasgulla Celebration Tin", "Soft spongy cottage cheese balls soaked in light sugar syrup.", "cat_snacks", "brand_haldiram", "cake", 0xFFFFEDD5, 4.7, 1410, false, listOf(v("p84", 1, "1 kg Tin", 260.0, 225.0, 17.0, 22)), "Snacks & Branded Foods", "Indian Mithai"),
        p("p85", "Tata Sampann 100% Durum Wheat Pasta", "High-protein non-sticky fusilli pasta for quick family meals.", "cat_snacks", "brand_tata", "noodles", 0xFFFFEDD5, 4.6, 680, false, listOf(v("p85", 1, "400 g", 125.0, 99.0, 20.0, 32)), "Snacks & Branded Foods", "Noodle, Pasta, Vermicelli"),
        p("p86", "Haldiram's Tangy Mango Achar", "Homestyle sun-matured spiced raw mango pickle in mustard oil.", "cat_snacks", "brand_haldiram", "spice", 0xFFFFE4E6, 4.6, 520, false, listOf(v("p86", 1, "400 g Jar", 140.0, 115.0, 21.0, 26)), "Snacks & Branded Foods", "Pickles & Chutney"),
        p("p87", "Haldiram's Ready Dal Makhani Meal", "Heat-and-eat creamy black lentils simmered with butter.", "cat_snacks", "brand_haldiram", "dal", 0xFFFFEDD5, 4.5, 430, false, listOf(v("p87", 1, "300 g", 135.0, 112.0, 19.0, 24)), "Snacks & Branded Foods", "Ready To Cook & Eat"),
        p("p88", "Whisper Ultra Clean XL+ Sanitary Pads", "Ultra-thin breathable cottony wings pads for all-day comfort.", "cat_personal", "brand_pg", "soap", 0xFFFCE7F3, 4.7, 1640, false, listOf(v("p88", 1, "XL 15 Pads", 180.0, 149.0, 22.0, 36)), "Beauty & Hygiene", "Feminine Hygiene"),
        p("p89", "Dabur Gulabari Mist Body Deo", "Alcohol-free floral rose mist deodorant for long-lasting freshness.", "cat_personal", "brand_dabur", "skincare", 0xFFFCE7F3, 4.5, 380, false, listOf(v("p89", 1, "150 ml", 210.0, 165.0, 24.0, 25)), "Beauty & Hygiene", "Fragrances & Deos"),
        p("p90", "Dabur Chyawanprash 2X Immunity", "Ayurvedic blend of 40+ herbs and fresh amla for daily immunity.", "cat_personal", "brand_dabur", "medicine", 0xFFDCFCE7, 4.8, 2940, true, listOf(v("p90", 1, "500 g", 235.0, 199.0, 19.0, 42)), "Beauty & Hygiene", "Health & Medicine"),
        p("p91", "Dabur Gulabari Lip & Cheek Tint", "Shea butter & rosewater natural hydrating matte tint.", "cat_personal", "brand_dabur", "skincare", 0xFFFCE7F3, 4.5, 290, false, listOf(v("p91", 1, "4.5 g", 199.0, 159.0, 24.0, 18)), "Beauty & Hygiene", "Makeup"),
        p("p92", "Dabur Charcoal Men's Face Wash", "Deep pore cleansing activated charcoal scrub wash for men.", "cat_personal", "brand_dabur", "grooming", 0xFFE2E8F0, 4.6, 510, false, listOf(v("p92", 1, "100 g", 165.0, 129.0, 25.0, 28)), "Beauty & Hygiene", "Men's Grooming"),
        p("p93", "Bajaj Herbal Relaxing Wellness Oil", "Soothing almond & vitamin-E body wellness massage oil.", "cat_personal", "brand_bajaj", "hairoil", 0xFFF5EEFF, 4.6, 220, false, listOf(v("p93", 1, "100 ml", 210.0, 169.0, 22.0, 20)), "Beauty & Hygiene", "Sexual Wellness"),
        p("p94", "Harpic Power Plus Toilet Cleaner", "10x faster lime-scale & germ removal thick disinfecting gel.", "cat_cleaning", "brand_reckitt", "cleaner", 0xFFE0F2FE, 4.7, 2310, false, listOf(v("p94", 1, "1 L", 215.0, 179.0, 22.0, 36)), "Cleaning & Household", "Bins & Bathroom Ware"),
        p("p95", "Surf Excel Matic Front Load Liquid", "Tough stain removal liquid detergent protecting washing machine drums.", "cat_cleaning", "brand_hul", "spray", 0xFFE0F2FE, 4.8, 1880, false, listOf(v("p95", 1, "1 L", 225.0, 189.0, 21.0, 30)), "Cleaning & Household", "Car & Shoe Care"),
        p("p96", "Godrej Ezee Liquid Detergent for Winterwear", "pH-neutral micro-conditioner gentle liquid wash for delicates.", "cat_cleaning", "brand_godrej", "cleaner", 0xFFFCE7F3, 4.7, 990, false, listOf(v("p96", 1, "500 g", 115.0, 95.0, 24.0, 48)), "Cleaning & Household", "Disposables, Garbage Bag"),
        p("p97", "RSPL Xpert Dishwash Bar & Scrubber", "Active ultra-degreasing lemon gel bar for spotless stainless pans.", "cat_cleaning", "brand_rspl", "dishwash", 0xFFDCFCE7, 4.7, 940, false, listOf(v("p97", 1, "Pack of 3", 90.0, 69.0, 28.0, 60)), "Cleaning & Household", "Mops, Brushes & Scrubs"),
        p("p98", "Cadbury Celebrations Festive Gift Box", "Assorted Dairy Milk chocolates gift pack perfect for celebrations.", "cat_snacks", "brand_mondelez", "chocolate", 0xFFFCE7F3, 4.8, 1350, false, listOf(v("p98", 1, "180 g Box", 175.0, 149.0, 20.0, 25)), "Cleaning & Household", "Party & Festive Needs"),
        p("p99", "Dabur Sandalwood Agarbatti & Camphor", "Pure chandan incense sticks with smokeless bhimseni camphor.", "cat_cleaning", "brand_dabur", "freshener", 0xFFFFEDD5, 4.8, 1120, false, listOf(v("p99", 1, "100 Sticks", 95.0, 75.0, 25.0, 50)), "Cleaning & Household", "Pooja Needs"),
        p("p100", "ITC Classmate A4 Notebooks Combo", "2 spiral 180-page crisp eco-friendly paper notebooks with pens.", "cat_cleaning", "brand_itc", "stationery", 0xFFDBEAFE, 4.8, 1620, false, listOf(v("p100", 1, "Combo Pack", 180.0, 149.0, 22.0, 36)), "Cleaning & Household", "Stationery"),
        p("p101", "Godrej Garden Organic Manure Bag", "Nutrient-dense natural compost mix for balcony pots and herbs.", "cat_veg", "brand_godrej", "garden", 0xFFDCFCE7, 4.7, 480, false, listOf(v("p101", 1, "2 kg Bag", 150.0, 109.0, 24.0, 28)), "Kitchen, Garden & Pets", "Gardening"),
        p("p102", "Tata Stainless Steel Insulated Tiffin 3-Tier", "Food-grade rustproof steel lunch box with leak-lock lids.", "cat_cleaning", "brand_tata", "kitchen", 0xFFE2E8F0, 4.6, 410, false, listOf(v("p102", 1, "3 Containers", 699.0, 499.0, 20.0, 14)), "Kitchen, Garden & Pets", "Steel Utensils"),
        p("p103", "Airtight Modular Kitchen Jar Set", "BPA-free stackable transparent jars for pulses and snacks.", "cat_cleaning", "brand_tata", "kitchen", 0xFFE0F2FE, 4.7, 560, false, listOf(v("p103", 1, "Set of 3", 399.0, 289.0, 22.0, 20)), "Kitchen, Garden & Pets", "Storage & Accessories"),
        p("p104", "Farm Protein White Eggs (12 Pack)", "Protein-rich clean white eggs packed in cushioned pulp tray.", "cat_breakfast", "brand_godrej", "eggs", 0xFFFEF3C7, 4.8, 2450, true, listOf(v("p104", 1, "Pack of 6", 54.0, 46.0, 16.0, 52), v("p104", 2, "Pack of 12", 105.0, 88.0, 18.0, 30)), "Eggs, Meat & Fish", "Eggs", isVeg = false),
        p("p105", "Godrej Real Good Curry Cut Mutton", "Tender farm-sourced bone-in curry cut, hygienically chilled.", "cat_frozen", "brand_godrej", "meat", 0xFFFFE4E6, 4.7, 890, false, listOf(v("p105", 1, "500 g", 480.0, 429.0, 15.0, 15)), "Eggs, Meat & Fish", "Mutton & Lamb", isVeg = false),
        p("p106", "Godrej Real Good Smoked Deli Slices", "Juicy tender seasoned protein strips ready for pan sear.", "cat_frozen", "brand_godrej", "meat", 0xFFFFE4E6, 4.6, 210, false, listOf(v("p106", 1, "250 g", 295.0, 259.0, 16.0, 10)), "Eggs, Meat & Fish", "Pork & Other Meats", isVeg = false),
        p("p107", "Godrej Real Good Chicken Breast Boneless", "Skinless tender chicken breast fillets, zero added hormones.", "cat_frozen", "brand_godrej", "meat", 0xFFFFE4E6, 4.8, 1740, true, listOf(v("p107", 1, "500 g", 240.0, 199.0, 17.0, 28)), "Eggs, Meat & Fish", "Poultry", isVeg = false),
        p("p108", "Godrej Real Good Herb Chicken Sausages", "Juicy breakfast chicken sausages mildly spiced with black pepper.", "cat_frozen", "brand_godrej", "meat", 0xFFFFE4E6, 4.6, 420, false, listOf(v("p108", 1, "250 g", 185.0, 155.0, 18.0, 22)), "Eggs, Meat & Fish", "Sausages, Bacon & Salami", isVeg = false),
        p("p109", "Kellogg's Fruit, Nut & Seeds Muesli", "Crunchy multigrain flakes loaded with cranberries, almonds and raisins.", "cat_breakfast", "brand_kellogg", "cereal", 0xFFE0F2FE, 4.8, 1740, true, listOf(v("p109", 1, "500 g", 345.0, 279.0, 20.0, 24)), "Gourmet & World Food", "Cereals & Breakfast"),
        p("p110", "Ferrero Rocher Hazelnut Praline Truffles", "Crisp wafer sphere filled with roasted hazelnut cream and milk chocolate.", "cat_bakery", "brand_ferrero", "chocolate", 0xFFFEF3C7, 4.9, 2340, true, listOf(v("p110", 1, "16 Pcs Box", 549.0, 449.0, 18.0, 16)), "Gourmet & World Food", "Chocolates & Biscuits"),
        p("p111", "Real Swiss Peach Sparkling Cooler", "Refreshing European peach botanical sparkling beverage.", "cat_drinks", "brand_dabur", "juice", 0xFFFFEDD5, 4.6, 390, false, listOf(v("p111", 1, "330 ml Can", 95.0, 79.0, 20.0, 28)), "Gourmet & World Food", "Drinks & Beverages"),
        p("p112", "Fortune Extra Virgin Olive Oil", "First cold-pressed Mediterranean olive oil for salads and dressings.", "cat_oil", "brand_adani", "oil", 0xFFDCFCE7, 4.8, 920, false, listOf(v("p112", 1, "500 ml", 799.0, 599.0, 19.0, 18)), "Gourmet & World Food", "Oils & Vinegar"),
        p("p113", "Amul Cream Cheese & Herb Dip", "Silky smooth cream cheese dip with garlic & chives.", "cat_breakfast", "brand_amul", "spread", 0xFFFEF3C7, 4.7, 450, false, listOf(v("p113", 1, "150 g Tub", 160.0, 135.0, 20.0, 18)), "Gourmet & World Food", "Sauces, Spreads & Dips"),
        p("p114", "Tata Sampann Roasted California Pistachios", "Naturally opened crunchy green pista lightly seasoned with sea salt.", "cat_snacks", "brand_tata", "dryfruit", 0xFFDCFCE7, 4.8, 810, false, listOf(v("p114", 1, "200 g", 340.0, 275.0, 18.0, 25)), "Gourmet & World Food", "Snacks, Dry Fruits, Nuts"),
        p("p115", "Haldiram's Sliced Jalapenos Jar", "Crisp tangy pickled green jalapenos for pizzas, nachos and wraps.", "cat_snacks", "brand_haldiram", "spice", 0xFFDCFCE7, 4.6, 370, false, listOf(v("p115", 1, "340 g", 180.0, 145.0, 21.0, 20)), "Gourmet & World Food", "Tinned & Processed Food"),
        p("p116", "Pampers Baby Soft Gentle Wipes", "Aloe vera enriched alcohol-free wet wipes for newborn skin.", "cat_personal", "brand_pg", "baby", 0xFFE0F2FE, 4.8, 1220, false, listOf(v("p116", 1, "72 Wipes", 199.0, 159.0, 20.0, 22)), "Baby Care", "Baby Accessories"),
        p("p117", "Palmolive Milk & Honey Baby Wash", "Hypoallergenic tear-free chamomile wash gentle on delicate skin.", "cat_personal", "brand_colgate", "baby", 0xFFE0F2FE, 4.8, 980, false, listOf(v("p117", 1, "250 ml", 240.0, 195.0, 21.0, 30)), "Baby Care", "Baby Bath & Hygiene"),
        p("p118", "Tata Soulfull Ragi & Apple Kids Cereal", "Iron-fortified smooth cereal blend with real apple for growing kids.", "cat_breakfast", "brand_tata", "baby", 0xFFFEF3C7, 4.7, 760, false, listOf(v("p118", 1, "300 g", 265.0, 229.0, 16.0, 28)), "Baby Care", "Baby Food & Formula"),
        p("p119", "Pampers Active Baby Pants Diapers (M)", "12-hour leak-lock cottony breathable diaper pants for 7–12 kg.", "cat_personal", "brand_pg", "baby", 0xFFE0F2FE, 4.9, 2540, true, listOf(v("p119", 1, "M • 42 Pants", 649.0, 499.0, 20.0, 32)), "Baby Care", "Diapers & Wipes"),
        p("p120", "Kissan Fresh Tomato Ketchup", "100% real juicy tomatoes blended into India's favourite samosa partner.", "cat_snacks", "brand_hul", "spread", 0xFFFEE2E2, 4.8, 2890, false, listOf(v("p120", 1, "900 g Bottle", 145.0, 119.0, 18.0, 44)), "Snacks & Branded Foods", "Spreads, Sauces, Ketchup"),
        p("p121", "Brooke Bond Red Label Tea", "Strong brisk chai leaves for warm family conversations.", "cat_drinks", "brand_hul", "tea", 0xFFFEE2E2, 4.7, 3210, false, listOf(v("p121", 1, "500 g", 285.0, 245.0, 16.0, 40)), "Beverages", "Tea"),
        p("p122", "Horlicks Classic Malt Nutrition", "Clinically proven growth & immunity nutrition drink with milk.", "cat_drinks", "brand_hul", "honey", 0xFFE0F2FE, 4.7, 1950, false, listOf(v("p122", 1, "500 g Jar", 275.0, 235.0, 18.0, 28)), "Beverages", "Nutrition Mix, Supplement"),
        p("p123", "Lux Soft Touch French Rose & Almond Oil Soap", "Silky perfumed lather for glowing soft skin.", "cat_personal", "brand_hul", "soap", 0xFFFCE7F3, 4.6, 1410, false, listOf(v("p123", 1, "Pack of 4", 160.0, 134.0, 20.0, 38)), "Beauty & Hygiene", "Bath & Hand Wash"),
        p("p124", "Dettol Original Antiseptic Liquid", "Trusted first-aid & home disinfection antiseptic shield.", "cat_personal", "brand_reckitt", "spray", 0xFFDCFCE7, 4.9, 4120, true, listOf(v("p124", 1, "550 ml", 230.0, 199.0, 18.0, 50)), "Beauty & Hygiene", "Health & Medicine"),
        p("p125", "Cadbury Dairy Milk Silk Oreo", "Creamy Silk chocolate loaded with crunchy Oreo cookie bits.", "cat_snacks", "brand_mondelez", "chocolate", 0xFFEDE9FE, 4.9, 3450, true, listOf(v("p125", 1, "130 g", 190.0, 165.0, 19.0, 42)), "Snacks & Branded Foods", "Chocolates & Candies"),
        p("p126", "Oreo Original Vanilla Creme Biscuits", "Twist, lick & dunk iconic cocoa cookies with sweet vanilla creme.", "cat_bakery", "brand_mondelez", "cookie", 0xFFE0F2FE, 4.8, 2240, false, listOf(v("p126", 1, "120 g", 35.0, 30.0, 20.0, 55)), "Snacks & Branded Foods", "Biscuits & Cookies"),
        p("p127", "Cadbury Bournvita Pro-Health Drink", "Chocolatey malted drink packed with vitamin D, iron & zinc.", "cat_drinks", "brand_mondelez", "coffee", 0xFFEDE9FE, 4.7, 1840, false, listOf(v("p127", 1, "500 g Jar", 265.0, 229.0, 18.0, 30)), "Beverages", "Nutrition Mix, Supplement"),
        p("p128", "Good Knight Gold Flash Liquid Vaporiser", "Dual-mode mosquito protection active flash machine + refill.", "cat_cleaning", "brand_godrej", "freshener", 0xFFFEE2E2, 4.7, 2110, false, listOf(v("p128", 1, "Machine + 45 ml", 115.0, 99.0, 24.0, 45)), "Cleaning & Household", "Fresheners & Repellents"),
        p("p129", "Tide Plus Extra Power Jasmine & Rose", "Brilliant whiteness detergent powder for everyday laundry.", "cat_cleaning", "brand_pg", "cleaner", 0xFFFFEDD5, 4.7, 2320, false, listOf(v("p129", 1, "2 kg Pack", 275.0, 229.0, 20.0, 35)), "Cleaning & Household", "Detergents & Dishwash"),
        p("p130", "Ariel Matic Complete Detergent", "1-wash stain removal tough-stain formula for automatic machines.", "cat_cleaning", "brand_pg", "cleaner", 0xFFE0F2FE, 4.8, 1980, false, listOf(v("p130", 1, "2 kg Pack", 510.0, 429.0, 18.0, 24)), "Cleaning & Household", "Detergents & Dishwash"),
        p("p131", "Sting Energy Drink Red Berry", "Refreshing sparkling energy boost with caffeine & B-vitamins.", "cat_drinks", "brand_varun", "juice", 0xFFFEE2E2, 4.6, 2610, false, listOf(v("p131", 1, "250 ml Bottle", 20.0, 20.0, 20.0, 80)), "Beverages", "Energy & Soft Drinks"),
        p("p132", "Parachute 100% Pure Coconut Oil", "Unrefined natural coconut oil crafted from finest sun-dried coconuts.", "cat_personal", "brand_marico", "hairoil", 0xFFE0F2FE, 4.9, 4310, true, listOf(v("p132", 1, "500 ml Bottle", 230.0, 199.0, 19.0, 48)), "Beauty & Hygiene", "Hair Care"),
        p("p133", "MAGGI 2-Minute Masala Noodles", "India's iconic hot masala noodles ready in 2 minutes.", "cat_snacks", "brand_nestle", "noodles", 0xFFFEF3C7, 4.9, 5400, true, listOf(v("p133", 1, "Pack of 6 (420 g)", 90.0, 84.0, 14.0, 80)), "Snacks & Branded Foods", "Noodle, Pasta, Vermicelli"),
        p("p134", "Nescafe Classic Instant Coffee", "100% pure roasted coffee granules for a rich morning brew.", "cat_drinks", "brand_nestle", "coffee", 0xFFFFE4E6, 4.8, 3190, false, listOf(v("p134", 1, "90 g Jar", 360.0, 319.0, 17.0, 36)), "Beverages", "Coffee"),
        p("p135", "Nestle KitKat 4-Finger Crisp Wafer", "Crispy wafer bars covered in creamy milk chocolate.", "cat_snacks", "brand_nestle", "chocolate", 0xFFFEE2E2, 4.8, 2870, false, listOf(v("p135", 1, "41.5 g Pack of 3", 90.0, 81.0, 18.0, 50)), "Snacks & Branded Foods", "Chocolates & Candies"),
        p("p136", "Lay's India's Magic Masala Potato Chips", "Crunchy spiced potato wafers bursting with chatpata masala.", "cat_snacks", "brand_pepsico", "snacks", 0xFFE0F2FE, 4.8, 4100, true, listOf(v("p136", 1, "90 g Family", 50.0, 45.0, 20.0, 65)), "Snacks & Branded Foods", "Snacks & Namkeen"),
        p("p137", "Kurkure Masala Munch", "Spicy crunchy corn-rice puffs cooked with tangy desi masala.", "cat_snacks", "brand_pepsico", "namkeen", 0xFFFFEDD5, 4.7, 3290, false, listOf(v("p137", 1, "90 g", 25.0, 22.0, 22.0, 70)), "Snacks & Branded Foods", "Snacks & Namkeen"),
        p("p138", "Colgate Strong Teeth Anticavity Toothpaste", "Amino Shakti formula adding natural calcium for stronger teeth.", "cat_personal", "brand_colgate", "toothpaste", 0xFFFEE2E2, 4.8, 4800, true, listOf(v("p138", 1, "200 g", 130.0, 108.0, 21.0, 60)), "Beauty & Hygiene", "Oral Care"),
        p("p139", "Nutella Hazelnut Spread with Cocoa", "Authentic silky roasted hazelnut spread for toast, pancakes & waffles.", "cat_breakfast", "brand_ferrero", "spread", 0xFFFFEDD5, 4.9, 3620, false, listOf(v("p139", 1, "350 g Jar", 395.0, 349.0, 17.0, 28)), "Snacks & Branded Foods", "Spreads, Sauces, Ketchup"),
        p("p140", "Ghadi Detergent Washing Powder", "Pehle istemaal karein phir vishwas karein — bright clean wash.", "cat_cleaning", "brand_rspl", "cleaner", 0xFFFEE2E2, 4.6, 2150, false, listOf(v("p140", 1, "3 kg Value Pack", 215.0, 189.0, 22.0, 40)), "Cleaning & Household", "Detergents & Dishwash"),
        p("p141", "Parle-G Original Gluco Biscuits", "India's timeless wheat & milk energy biscuit loved across generations.", "cat_bakery", "brand_parle", "biscuit", 0xFFFEF3C7, 4.9, 6200, true, listOf(v("p141", 1, "800 g Family Pack", 95.0, 84.0, 18.0, 75)), "Snacks & Branded Foods", "Biscuits & Cookies"),
        p("p142", "Parle Hide & Seek Choco Chip Cookies", "Rich chocolate-moulded cookies packed with real choco chips.", "cat_bakery", "brand_parle", "cookie", 0xFFEDE9FE, 4.8, 2890, false, listOf(v("p142", 1, "200 g", 60.0, 52.0, 20.0, 44)), "Snacks & Branded Foods", "Biscuits & Cookies"),
        p("p143", "Parle Monaco Classic Salted Biscuits", "Light crispy oven-baked salted crackers ideal for party toppings.", "cat_bakery", "brand_parle", "biscuit", 0xFFFFEDD5, 4.6, 1740, false, listOf(v("p143", 1, "200 g", 40.0, 35.0, 19.0, 48)), "Bakery, Cakes & Dairy", "Bakery Snacks"),
        p("p144", "Sugar Free Gold Low Calorie Sweetener", "Aspartame-based zero-sugar sweetener pellets for chai & coffee.", "cat_breakfast", "brand_zydus", "medicine", 0xFFE0F2FE, 4.7, 1650, false, listOf(v("p144", 1, "500 Pellets", 310.0, 265.0, 21.0, 32)), "Beverages", "Nutrition Mix, Supplement"),
        p("p145", "Glucon-D Tangy Orange Instant Energy", "Glucose-C instant energy drink replenishing summer vitality.", "cat_drinks", "brand_zydus", "juice", 0xFFFFEDD5, 4.8, 2940, false, listOf(v("p145", 1, "500 g", 215.0, 179.0, 22.0, 40)), "Beverages", "Energy & Soft Drinks"),
        p("p146", "Nutralite Choco Spread Calcium Rich", "Healthy zero-trans-fat spread with added vitamins & calcium.", "cat_breakfast", "brand_zydus", "spread", 0xFFDCFCE7, 4.6, 890, false, listOf(v("p146", 1, "275 g", 150.0, 125.0, 22.0, 24)), "Snacks & Branded Foods", "Spreads, Sauces, Ketchup"),
        p("p147", "Kellogg's Corn Flakes Original", "Golden crispy sun-ripened corn flakes with iron & 8 essential vitamins.", "cat_breakfast", "brand_kellogg", "cereal", 0xFFFEF3C7, 4.8, 3420, true, listOf(v("p147", 1, "475 g", 210.0, 179.0, 19.0, 36)), "Snacks & Branded Foods", "Breakfast Cereals"),
        p("p148", "Kellogg's Chocos Chocolaty Crunchy Bites", "Whole-grain wheat shells in rich chocolatey taste for kids breakfast.", "cat_breakfast", "brand_kellogg", "cereal", 0xFFEDE9FE, 4.8, 2810, false, listOf(v("p148", 1, "385 g", 199.0, 169.0, 20.0, 30)), "Snacks & Branded Foods", "Breakfast Cereals"),
        p("p149", "Bajaj Almond Drops Non-Sticky Hair Oil", "Light sweet almond oil enriched with 6x vitamin E for smooth hair.", "cat_personal", "brand_bajaj", "hairoil", 0xFFFEF3C7, 4.8, 3680, true, listOf(v("p149", 1, "300 ml Glass", 215.0, 179.0, 22.0, 42)), "Beauty & Hygiene", "Hair Care"),
        p("p150", "Sunfeast Dark Fantasy Choco Fills", "Original molten chocolate cream baked inside crisp dark crust cookies.", "cat_bakery", "brand_itc", "cookie", 0xFFEDE9FE, 4.8, 3190, false, listOf(v("p150", 1, "300 g Carton", 170.0, 139.0, 21.0, 34)), "Snacks & Branded Foods", "Biscuits & Cookies"),
        p("p151", "Fortune Soya Chunks (High Protein)", "Soft, juicy protein-rich soya chunks crafted for curries and pulao.", "cat_staples", "brand_adani", "oats", 0xFFFFEDD5, 4.7, 1420, false, listOf(v("p151", 1, "1 kg - Pouch", 210.0, 168.0, 25.0, 0)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p152", "Tata Sampann Unpolished Ragi & Foxtail Millet", "Naturally gluten-free fibre-rich millets for wholesome khichdi and dosas.", "cat_staples", "brand_tata", "oats", 0xFFFEF3C7, 4.7, 680, false, listOf(v("p152", 1, "500 g", 85.0, 72.0, 18.0, 36), v("p152", 2, "1 kg", 165.0, 138.0, 20.0, 20)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p153", "Aashirvaad Nature's Super Foods Jowar & Bajra Millet", "Stone-cleaned traditional Indian sorghum and pearl millet grains.", "cat_staples", "brand_itc", "poha", 0xFFFEF3C7, 4.6, 510, false, listOf(v("p153", 1, "500 g", 78.0, 64.0, 19.0, 32), v("p153", 2, "1 kg", 148.0, 119.0, 20.0, 18)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p154", "Saffola Fittify High-Protein Soya Granules", "Quick-cook textured soy keema granules packed with 52% plant protein.", "cat_staples", "brand_marico", "oats", 0xFFDCFCE7, 4.6, 440, false, listOf(v("p154", 1, "200 g", 68.0, 54.0, 20.0, 42), v("p154", 2, "500 g", 155.0, 124.0, 21.0, 22)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p155", "Fortune Chana Dal Best", "Bold golden Bengal gram split lentils sorted for rich aroma and fast cooking.", "cat_staples", "brand_adani", "dal", 0xFFFEF3C7, 4.8, 1920, true, listOf(v("p155", 1, "1 kg", 132.0, 112.0, 16.0, 46), v("p155", 2, "5 kg", 640.0, 545.0, 17.5, 20), v("p155", 3, "30 kg", 3900.0, 3350.0, 18.0, 8)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p156", "Tata Sampann Unpolished Chana Dal", "Farm-fresh chana dal retaining natural bran protein and authentic taste.", "cat_staples", "brand_tata", "dal", 0xFFFEF3C7, 4.7, 1340, false, listOf(v("p156", 1, "500 g", 78.0, 64.0, 18.0, 52), v("p156", 2, "1 kg", 150.0, 122.0, 19.0, 30)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p157", "Tata Sampann Yellow Moong Dal", "Light, easy-to-digest unpolished split yellow moong lentils for daily khichdi.", "cat_staples", "brand_tata", "dal", 0xFFFEF9C3, 4.8, 1760, true, listOf(v("p157", 1, "500 g", 98.0, 82.0, 17.0, 48), v("p157", 2, "1 kg", 190.0, 159.0, 18.5, 28)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p158", "Fortune Moong Sabut (Whole Green Gram)", "Nutrient-dense whole green moong ideal for sprouting and Punjabi sabut dal.", "cat_staples", "brand_adani", "dal", 0xFFDCFCE7, 4.7, 940, false, listOf(v("p158", 1, "1 kg", 195.0, 175.0, 16.0, 36), v("p158", 2, "30 kg", 5850.0, 5250.0, 17.0, 6)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p159", "Aashirvaad Desi Toor / Arhar Dal", "Hand-picked Maharashtra & MP origin tur dal sorted to uniform grain size.", "cat_staples", "brand_itc", "dal", 0xFFFEF3C7, 4.7, 1120, false, listOf(v("p159", 1, "1 kg", 198.0, 165.0, 18.0, 38), v("p159", 2, "2 kg", 390.0, 324.0, 19.0, 16)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p160", "Fortune Urad Whole Regular (Sabut)", "Creamy black matpe whole urad ideal for slow-simmered restaurant dal makhani.", "cat_staples", "brand_adani", "dal", 0xFFE2E8F0, 4.7, 890, false, listOf(v("p160", 1, "1 kg", 185.0, 158.0, 17.0, 34), v("p160", 2, "30 kg", 5400.0, 4700.0, 18.0, 6)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p161", "Tata Sampann Unpolished Urad Gota Whole", "Pure white skinned urad gota crafted for fluffy crispy idli & dosa batter.", "cat_staples", "brand_tata", "dal", 0xFFFEF9C3, 4.8, 1280, false, listOf(v("p161", 1, "500 g", 112.0, 94.0, 18.0, 40), v("p161", 2, "1 kg", 218.0, 182.0, 19.0, 24)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p162", "Tata Sampann Lobhiya Safed (Black-Eyed Beans)", "Clean creamy black-eyed peas rich in iron and soluble dietary fibre.", "cat_staples", "brand_tata", "dal", 0xFFFEF3C7, 4.6, 610, false, listOf(v("p162", 1, "500 g", 92.0, 76.0, 18.0, 32), v("p162", 2, "1 kg", 178.0, 146.0, 19.5, 18)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p163", "Tata Sampann Kashmiri Rajma Chitra", "Speckled Himalayan kidney beans that turn melt-in-mouth buttery for rajma chawal.", "cat_staples", "brand_tata", "dal", 0xFFFFE4E6, 4.8, 1690, true, listOf(v("p163", 1, "500 g", 118.0, 96.0, 19.0, 44), v("p163", 2, "1 kg", 228.0, 184.0, 20.0, 22)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p164", "Fortune Red Masoor Dal Unpolished", "Quick-cooking pink split red lentils for comforting homestyle tadka dal.", "cat_staples", "brand_adani", "dal", 0xFFFFEDD5, 4.7, 990, false, listOf(v("p164", 1, "500 g", 84.0, 69.0, 18.0, 46), v("p164", 2, "1 kg", 162.0, 134.0, 19.5, 26)), "Foodgrains, Oil & Masala", "Dals & Pulses"),
        p("p165", "Tata Sampann 100% Chana Dal Fine Besan", "Gram flour freshly ground solely from pure unpolished chana dal for fluffy pakoras.", "cat_staples", "brand_tata", "atta", 0xFFFEF3C7, 4.8, 2120, false, listOf(v("p165", 1, "500 g", 75.0, 62.0, 19.0, 48), v("p165", 2, "1 kg", 145.0, 118.0, 20.0, 24)), "Foodgrains, Oil & Masala", "Atta, Flours & Sooji"),
        p("p166", "Fortune Chakki Fresh Sooji (Semolina)", "Coarse golden suji ideal for fluffy morning upma, suji halwa, and crisp rava dosa.", "cat_staples", "brand_adani", "atta", 0xFFFEF9C3, 4.7, 1340, false, listOf(v("p166", 1, "500 g", 45.0, 38.0, 17.0, 52), v("p166", 2, "1 kg", 88.0, 72.0, 18.0, 28)), "Foodgrains, Oil & Masala", "Atta, Flours & Sooji"),
        p("p167", "Aashirvaad Nature's Super Foods Multigrain Atta", "Blend of wheat, soya, chana, oat, maize and psyllium husk for daily fibre.", "cat_staples", "brand_itc", "atta", 0xFFFFEDD5, 4.8, 1890, true, listOf(v("p167", 1, "5 kg", 365.0, 315.0, 15.0, 28)), "Foodgrains, Oil & Masala", "Atta, Flours & Sooji")
    )

    val defaultAddresses: List<SavedAddress> = listOf(
        SavedAddress(
            id = "addr_home",
            label = "Home",
            recipientName = "Aarav Sharma",
            phone = "9876543210",
            houseFlat = "Flat 402, Greenview Residency",
            streetArea = "12th Main, Indiranagar",
            landmark = "Near Metro Station",
            city = "Bengaluru",
            pincode = "560038",
            isDefault = true
        ),
        SavedAddress(
            id = "addr_work",
            label = "Work",
            recipientName = "Aarav Sharma",
            phone = "9876543210",
            houseFlat = "Floor 6, Horizon Tech Park",
            streetArea = "Outer Ring Road, Bellandur",
            landmark = "Opposite EcoSpace",
            city = "Bengaluru",
            pincode = "560103",
            isDefault = false
        )
    )

    val defaultDeliverySlots: List<DeliverySlot> = listOf(
        DeliverySlot("slot_express", "Express 10-Min", "Arrives in 10–15 mins", 0.0, isExpress = true, isEnabled = true),
        DeliverySlot("slot_evening", "Today Evening", "6:00 PM – 8:00 PM", 0.0, isExpress = false, isEnabled = true),
        DeliverySlot("slot_morning", "Tomorrow Morning", "7:00 AM – 9:00 AM", 0.0, isExpress = false, isEnabled = true),
        DeliverySlot("slot_afternoon", "Tomorrow Afternoon", "12:00 PM – 2:00 PM", 0.0, isExpress = false, isEnabled = true)
    )

    val defaultStoreConfig = StoreConfig(
        baseDeliveryFee = 25.0,
        freeDeliveryThreshold = 199.0,
        etaMinutes = 10,
        promoBannerText = "Free delivery on orders ₹199+"
    )

    val defaultUserProfile = UserProfile(
        phone = "",
        name = "Aarav Sharma",
        email = "aarav.sharma@example.in",
        walletBalance = 450.0,
        notificationsEnabled = true,
        isLoggedIn = false
    )

    val sampleOrders: List<OrderRecord> = listOf(
        OrderRecord(
            id = "QK-84921",
            createdAtMillis = System.currentTimeMillis() - 86_400_000L * 2,
            status = OrderStatus.DELIVERED,
            addressSnapshot = "Flat 402, Greenview Residency, 12th Main, Indiranagar, Bengaluru - 560038",
            deliverySlotLabel = "Express 10-Min (10–15 mins)",
            paymentMethodLabel = "UPI (GPay / PhonePe)",
            paymentRef = "DEMO_UPI_992841",
            items = listOf(
                OrderLineItem("p11", "Amul Taaza Toned Milk", "p11_v2", "1 L Tetra", 74.0, 69.0, 2, "milk", 0xFFE0F2FE),
                OrderLineItem("p01", "Hybrid Red Tomato", "p01_v2", "1 kg", 68.0, 44.0, 1, "tomato", 0xFFFFE4E6),
                OrderLineItem("p26", "Haldiram's Aloo Bhujia", "p26_v2", "400 g", 105.0, 92.0, 1, "namkeen", 0xFFFFEDD5)
            ),
            itemTotal = 274.0,
            deliveryFee = 0.0,
            totalSavings = 47.0,
            grandTotal = 274.0
        ),
        OrderRecord(
            id = "QK-84958",
            createdAtMillis = System.currentTimeMillis() - 3_600_000L * 3,
            status = OrderStatus.OUT_FOR_DELIVERY,
            addressSnapshot = "Flat 402, Greenview Residency, 12th Main, Indiranagar, Bengaluru - 560038",
            deliverySlotLabel = "Express 10-Min (10–15 mins)",
            paymentMethodLabel = "QuickKart Wallet",
            paymentRef = "DEMO_WALLET_318022",
            items = listOf(
                OrderLineItem("p16", "Aashirvaad Shudh Chakki Atta", "p16_v1", "5 kg", 295.0, 249.0, 1, "atta", 0xFFFEF3C7),
                OrderLineItem("p12", "Amul Pasteurised Butter", "p12_v1", "100 g", 60.0, 56.0, 2, "butter", 0xFFFEF3C7)
            ),
            itemTotal = 361.0,
            deliveryFee = 0.0,
            totalSavings = 54.0,
            grandTotal = 361.0
        )
    )
}
```

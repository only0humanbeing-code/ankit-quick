package com.example.data.local

import com.example.model.Product

data class ShopCategoryGroup(
    val id: String,
    val title: String,
    val iconKey: String,
    val allOptionLabel: String,
    val subcategories: List<String>
)

/**
 * Complete 12-department Shop By Category accordion hierarchy matching the reference catalog.
 */
object ShopCategoryTaxonomy {

    val groups: List<ShopCategoryGroup> = listOf(
        ShopCategoryGroup(
            id = "shop_electronics",
            title = "Electronics",
            iconKey = "electronics",
            allOptionLabel = "All Electronics",
            subcategories = listOf(
                "Audio devices",
                "Electrical Accessories",
                "Personal Care & Grooming",
                "Smart Wearables"
            )
        ),
        ShopCategoryGroup(
            id = "shop_fruits_veg",
            title = "Fruits & Vegetables",
            iconKey = "fruits_veg",
            allOptionLabel = "All Fruits & Vegetables",
            subcategories = listOf(
                "Cuts & Sprouts",
                "Exotic Fruits & Veggies",
                "Flower Bouquets, Bunches",
                "Fresh Fruits",
                "Fresh Vegetables",
                "Herbs & Seasonings",
                "Organic Fruits & Vegetables"
            )
        ),
        ShopCategoryGroup(
            id = "shop_foodgrains",
            title = "Foodgrains, Oil & Masala",
            iconKey = "foodgrains",
            allOptionLabel = "All Foodgrains, Oil & Masala",
            subcategories = listOf(
                "Atta, Flours & Sooji",
                "Dals & Pulses",
                "Dry Fruits",
                "Edible Oils & Ghee",
                "Masalas & Spices",
                "Organic Staples",
                "Rice & Rice Products",
                "Salt, Sugar & Jaggery"
            )
        ),
        ShopCategoryGroup(
            id = "shop_bakery_dairy",
            title = "Bakery, Cakes & Dairy",
            iconKey = "bakery_dairy",
            allOptionLabel = "All Bakery, Cakes & Dairy",
            subcategories = listOf(
                "Bakery Snacks",
                "Breads & Buns",
                "Cakes & Pastries",
                "Cookies, Rusk & Khari",
                "Dairy",
                "Non Dairy"
            )
        ),
        ShopCategoryGroup(
            id = "shop_beverages",
            title = "Beverages",
            iconKey = "beverages",
            allOptionLabel = "All Beverages",
            subcategories = listOf(
                "Coffee",
                "Energy & Soft Drinks",
                "Fruit Juices & Drinks",
                "Nutrition Mix, Supplement",
                "Tea",
                "Water"
            )
        ),
        ShopCategoryGroup(
            id = "shop_snacks_branded",
            title = "Snacks & Branded Foods",
            iconKey = "snacks_branded",
            allOptionLabel = "All Snacks & Branded Foods",
            subcategories = listOf(
                "Biscuits & Cookies",
                "Breakfast Cereals",
                "Chocolates & Candies",
                "Indian Mithai",
                "Noodle, Pasta, Vermicelli",
                "Pickles & Chutney",
                "Ready To Cook & Eat",
                "Snacks & Namkeen",
                "Spreads, Sauces, Ketchup"
            )
        ),
        ShopCategoryGroup(
            id = "shop_beauty_hygiene",
            title = "Beauty & Hygiene",
            iconKey = "beauty_hygiene",
            allOptionLabel = "All Beauty & Hygiene",
            subcategories = listOf(
                "Bath & Hand Wash",
                "Feminine Hygiene",
                "Fragrances & Deos",
                "Hair Care",
                "Health & Medicine",
                "Makeup",
                "Men's Grooming",
                "Oral Care",
                "Sexual Wellness",
                "Skin Care"
            )
        ),
        ShopCategoryGroup(
            id = "shop_cleaning",
            title = "Cleaning & Household",
            iconKey = "cleaning_household",
            allOptionLabel = "All Cleaning & Household",
            subcategories = listOf(
                "All Purpose Cleaners",
                "Bins & Bathroom Ware",
                "Car & Shoe Care",
                "Detergents & Dishwash",
                "Disposables, Garbage Bag",
                "Fresheners & Repellents",
                "Mops, Brushes & Scrubs",
                "Party & Festive Needs",
                "Pooja Needs",
                "Stationery"
            )
        ),
        ShopCategoryGroup(
            id = "shop_kitchen_pets",
            title = "Kitchen, Garden & Pets",
            iconKey = "kitchen_garden_pets",
            allOptionLabel = "All Kitchen, Garden & Pets",
            subcategories = listOf(
                "Gardening",
                "Steel Utensils",
                "Storage & Accessories"
            )
        ),
        ShopCategoryGroup(
            id = "shop_eggs_meat",
            title = "Eggs, Meat & Fish",
            iconKey = "eggs_meat_fish",
            allOptionLabel = "All Eggs, Meat & Fish",
            subcategories = listOf(
                "Eggs",
                "Mutton & Lamb",
                "Pork & Other Meats",
                "Poultry",
                "Sausages, Bacon & Salami"
            )
        ),
        ShopCategoryGroup(
            id = "shop_gourmet",
            title = "Gourmet & World Food",
            iconKey = "gourmet_world",
            allOptionLabel = "All Gourmet & World Food",
            subcategories = listOf(
                "Cereals & Breakfast",
                "Chocolates & Biscuits",
                "Drinks & Beverages",
                "Oils & Vinegar",
                "Sauces, Spreads & Dips",
                "Snacks, Dry Fruits, Nuts",
                "Tinned & Processed Food"
            )
        ),
        ShopCategoryGroup(
            id = "shop_baby_care",
            title = "Baby Care",
            iconKey = "baby_care",
            allOptionLabel = "All Baby Care",
            subcategories = listOf(
                "Baby Accessories",
                "Baby Bath & Hygiene",
                "Baby Food & Formula",
                "Diapers & Wipes"
            )
        )
    )

    private val leafFiltersBySubCategory: Map<String, List<String>> = mapOf(
        "Dals & Pulses" to listOf(
            "Cereals & Millets",
            "Toor, Channa & Moong Dal",
            "Urad & Other Dals"
        ),
        "Atta, Flours & Sooji" to listOf(
            "Chakki & Sharbati Atta",
            "Besan, Sooji & Maida",
            "Millet & Multigrain"
        ),
        "Edible Oils & Ghee" to listOf(
            "Sunflower & Mustard Oil",
            "Pure Cow Ghee"
        ),
        "Masalas & Spices" to listOf(
            "Powdered Spices",
            "Blended Masalas"
        ),
        "Salt, Sugar & Jaggery" to listOf(
            "Iodised & Rock Salt",
            "Jaggery & Sugar"
        ),
        "Fresh Vegetables" to listOf(
            "Potato, Onion & Tomato",
            "Greens & Beans"
        ),
        "Fresh Fruits" to listOf(
            "Daily Fruits",
            "Seasonal & Citrus"
        ),
        "Dairy" to listOf(
            "Milk",
            "Butter & Paneer"
        ),
        "Tea" to listOf(
            "Leaf & Dust Tea",
            "CTC & Assam Tea"
        ),
        "Biscuits & Cookies" to listOf(
            "Glucose & Marie",
            "Cream & Choco Cookies"
        ),
        "Snacks & Namkeen" to listOf(
            "Bhujia & Sev",
            "Chips & Nachos",
            "Oats & Healthy Crunch"
        ),
        "Detergents & Dishwash" to listOf(
            "Detergent Powders",
            "Liquid & Matic Wash"
        ),
        "Bath & Hand Wash" to listOf(
            "Bathing Soaps",
            "Hand & Body Wash"
        ),
        "Health & Medicine" to listOf(
            "Antiseptics & First Aid",
            "Immunity & Chyawanprash"
        )
    )

    fun leafFiltersFor(subCategory: String?): List<String> =
        if (subCategory.isNullOrBlank()) emptyList() else leafFiltersBySubCategory[subCategory].orEmpty()

    fun matchesLeafFilter(product: Product, leafFilter: String): Boolean {
        val text = "${product.name} ${product.description} ${product.subBrand}".lowercase()
        return when (leafFilter) {
            "Cereals & Millets" ->
                listOf("soya", "poha", "millet", "ragi", "jowar", "bajra", "daliya", "quinoa", "oats", "sabudana")
                    .any { it in text }
            "Toor, Channa & Moong Dal" ->
                listOf("toor", "arhar", "chana", "channa", "moong", "mung", "besan")
                    .any { it in text }
            "Urad & Other Dals" ->
                listOf("urad", "masoor", "rajma", "lobhiya", "lobia", "moth", "kulthi", "matar", "mixed dal", "panchratna", "kabuli")
                    .any { it in text }

            "Chakki & Sharbati Atta" -> "atta" in text || "sharbati" in text || "wheat" in text
            "Besan, Sooji & Maida" -> listOf("besan", "sooji", "suji", "rava", "maida").any { it in text }
            "Millet & Multigrain" -> listOf("multigrain", "millet", "ragi", "jowar", "bajra").any { it in text }

            "Sunflower & Mustard Oil" -> "oil" in text
            "Pure Cow Ghee" -> "ghee" in text

            "Powdered Spices" -> listOf("turmeric", "haldi", "chilli", "mirch", "coriander", "dhaniya", "cumin").any { it in text }
            "Blended Masalas" -> listOf("garam", "masala", "chole", "sambhar", "pav bhaji").any { it in text }

            "Iodised & Rock Salt" -> "salt" in text
            "Jaggery & Sugar" -> listOf("jaggery", "gud", "sugar").any { it in text }

            "Potato, Onion & Tomato" -> listOf("potato", "onion", "tomato").any { it in text }
            "Greens & Beans" -> listOf("bhindi", "okra", "palak", "spinach", "coriander", "beans", "peas").any { it in text }

            "Daily Fruits" -> listOf("banana", "apple", "pomegranate").any { it in text }
            "Seasonal & Citrus" -> listOf("mango", "orange", "citrus").any { it in text }

            "Milk" -> "milk" in text
            "Butter & Paneer" -> "butter" in text || "paneer" in text || "cheese" in text

            "Leaf & Dust Tea" -> "red label" in text || "leaf" in text || "green" in text
            "CTC & Assam Tea" -> "gold" in text || "assam" in text || "ctc" in text

            "Glucose & Marie" -> listOf("parle-g", "gluco", "nutrichoice", "marie").any { it in text }
            "Cream & Choco Cookies" -> listOf("oreo", "bourbon", "good day", "hide & seek", "dark fantasy", "choco").any { it in text }

            "Bhujia & Sev" -> listOf("bhujia", "khatta meetha", "kaju mixture", "namkeen", "moong dal").any { it in text }
            "Chips & Nachos" -> listOf("lay's", "lays", "kurkure", "chips", "nacho").any { it in text }
            "Oats & Healthy Crunch" -> "oats" in text || "quaker" in text || "crunch" in text

            "Detergent Powders" -> "powder" in text || "tide" in text || "ariel" in text || "ghadi" in text
            "Liquid & Matic Wash" -> listOf("vim", "gel", "liquid", "matic", "dishwash").any { it in text }

            "Bathing Soaps" -> "soap" in text || "lux" in text || "lifebuoy" in text
            "Hand & Body Wash" -> "hand" in text || "body" in text || "wash" in text

            "Antiseptics & First Aid" -> "dettol" in text || "antiseptic" in text
            "Immunity & Chyawanprash" -> "chyawanprash" in text || "immunity" in text

            else -> leafFilter.lowercase() in text
        }
    }

    fun findByTitle(title: String): ShopCategoryGroup? =
        groups.firstOrNull { it.title == title }
}

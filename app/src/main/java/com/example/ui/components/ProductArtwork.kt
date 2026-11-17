package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.BakeryDining
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.BreakfastDining
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Egg
import androidx.compose.material.icons.rounded.ElectricalServices
import androidx.compose.material.icons.rounded.FaceRetouchingNatural
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Grass
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Icecream
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalDrink
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.RiceBowl
import androidx.compose.material.icons.rounded.Sanitizer
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material.icons.rounded.SoupKitchen
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Watch
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.model.Brand
import com.example.ui.theme.QuickAmber
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenDark

@Composable
fun QuickKartLogoBadge(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    lightOnGreen: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(if (compact) 30.dp else 44.dp),
            shape = RoundedCornerShape(if (compact) 9.dp else 13.dp),
            color = if (lightOnGreen) Color.White else QuickGreen,
            border = if (lightOnGreen) BorderStroke(1.5.dp, Color(0xFFDC2626)) else null,
            shadowElevation = if (lightOnGreen) 2.dp else 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (lightOnGreen) {
                            Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF3F8EB)))
                        } else {
                            Brush.linearGradient(listOf(QuickGreen, QuickGreenDark))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ShoppingBasket,
                    contentDescription = "QuickKart logo",
                    tint = if (lightOnGreen) QuickGreenDark else Color.White,
                    modifier = Modifier.size(if (compact) 18.dp else 26.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = null,
                    tint = if (lightOnGreen) Color(0xFFDC2626) else QuickAmber,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(if (compact) 11.dp else 16.dp)
                )
            }
        }
        Spacer(Modifier.width(if (compact) 7.dp else 12.dp))
        Text(
            text = "QuickKart",
            style = if (compact) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if (lightOnGreen) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

private enum class PackagingShape {
    PRODUCE,
    CARTON,
    JAR,
    POUCH,
    TUB,
    BOTTLE,
    OBJECT
}

private data class VisualSpec(
    val icon: ImageVector,
    val accent: Color,
    val secondary: Color,
    val tag: String,
    val shape: PackagingShape
)

private fun resolveProductVisualSpec(imageKey: String): VisualSpec = when (imageKey.lowercase()) {
    "tomato" -> VisualSpec(Icons.Rounded.LocalFlorist, Color(0xFFDC2626), Color(0xFF16A34A), "FARM FRESH", PackagingShape.PRODUCE)
    "onion" -> VisualSpec(Icons.Rounded.Eco, Color(0xFF9333EA), Color(0xFFC084FC), "NASHIK", PackagingShape.PRODUCE)
    "potato" -> VisualSpec(Icons.Rounded.Spa, Color(0xFFB45309), Color(0xFFD97706), "SORTED", PackagingShape.PRODUCE)
    "okra" -> VisualSpec(Icons.Rounded.Grass, Color(0xFF15803D), Color(0xFF4ADE80), "TENDER", PackagingShape.PRODUCE)
    "coriander" -> VisualSpec(Icons.Rounded.Eco, Color(0xFF059669), Color(0xFF34D399), "CHILLED", PackagingShape.PRODUCE)
    "banana" -> VisualSpec(Icons.Rounded.Restaurant, Color(0xFFCA8A04), Color(0xFFFACC15), "RIPE", PackagingShape.PRODUCE)
    "apple" -> VisualSpec(Icons.Rounded.LocalFlorist, Color(0xFFE11D48), Color(0xFF16A34A), "SHIMLA", PackagingShape.PRODUCE)
    "pomegranate" -> VisualSpec(Icons.Rounded.LocalFlorist, Color(0xFFBE123C), Color(0xFFFB7185), "RUBY", PackagingShape.PRODUCE)
    "mango" -> VisualSpec(Icons.Rounded.Spa, Color(0xFFEA580C), Color(0xFFFBBF24), "ALPHONSO", PackagingShape.PRODUCE)
    "orange" -> VisualSpec(Icons.Rounded.LocalFlorist, Color(0xFFF97316), Color(0xFF16A34A), "CITRUS", PackagingShape.PRODUCE)
    "milk" -> VisualSpec(Icons.Rounded.LocalDrink, Color(0xFF0284C7), Color(0xFF38BDF8), "CHILLED", PackagingShape.CARTON)
    "butter" -> VisualSpec(Icons.Rounded.BreakfastDining, Color(0xFFD97706), Color(0xFFDC2626), "CREAMY", PackagingShape.TUB)
    "curd" -> VisualSpec(Icons.Rounded.SoupKitchen, Color(0xFF0369A1), Color(0xFF7DD3FC), "DAHI", PackagingShape.TUB)
    "paneer" -> VisualSpec(Icons.Rounded.Egg, Color(0xFF0EA5E9), Color(0xFFBAE6FD), "MALAI", PackagingShape.TUB)
    "bread" -> VisualSpec(Icons.Rounded.BakeryDining, Color(0xFFB45309), Color(0xFFF59E0B), "BAKED", PackagingShape.POUCH)
    "atta" -> VisualSpec(Icons.Rounded.Inventory2, Color(0xFFA16207), Color(0xFFF59E0B), "CHAKKI", PackagingShape.POUCH)
    "dal" -> VisualSpec(Icons.Rounded.RiceBowl, Color(0xFFD97706), Color(0xFF1D4ED8), "UNPOLISHED", PackagingShape.POUCH)
    "rice" -> VisualSpec(Icons.Rounded.RiceBowl, Color(0xFFCA8A04), Color(0xFFFEF08A), "BASMATI", PackagingShape.POUCH)
    "poha" -> VisualSpec(Icons.Rounded.RiceBowl, Color(0xFFB45309), Color(0xFFFDE68A), "FLUFFY", PackagingShape.POUCH)
    "oil" -> VisualSpec(Icons.Rounded.WaterDrop, Color(0xFFEAB308), Color(0xFFFDE047), "PURE", PackagingShape.BOTTLE)
    "ghee" -> VisualSpec(Icons.Rounded.WaterDrop, Color(0xFFD97706), Color(0xFFFBBF24), "DANEDAR", PackagingShape.JAR)
    "spice" -> VisualSpec(Icons.Rounded.Whatshot, Color(0xFFDC2626), Color(0xFFF97316), "AROMA", PackagingShape.POUCH)
    "namkeen" -> VisualSpec(Icons.Rounded.Fastfood, Color(0xFFEA580C), Color(0xFFFACC15), "CRISPY", PackagingShape.POUCH)
    "snacks" -> VisualSpec(Icons.Rounded.Fastfood, Color(0xFFD97706), Color(0xFFFB923C), "CRUNCH", PackagingShape.POUCH)
    "makhana" -> VisualSpec(Icons.Rounded.Spa, Color(0xFF7C3AED), Color(0xFFC4B5FD), "ROASTED", PackagingShape.POUCH)
    "tea" -> VisualSpec(Icons.Rounded.LocalCafe, Color(0xFF0F766E), Color(0xFF2DD4BF), "ASSAM", PackagingShape.POUCH)
    "juice" -> VisualSpec(Icons.Rounded.LocalDrink, Color(0xFFEA580C), Color(0xFFFDBA74), "PULP", PackagingShape.CARTON)
    "milkshake" -> VisualSpec(Icons.Rounded.Coffee, Color(0xFFD97706), Color(0xFFFDE68A), "KESAR", PackagingShape.BOTTLE)
    "biscuit" -> VisualSpec(Icons.Rounded.Cookie, Color(0xFFB45309), Color(0xFFEF4444), "CRUNCHY", PackagingShape.TUB)
    "cookie" -> VisualSpec(Icons.Rounded.Cookie, Color(0xFF6D28D9), Color(0xFFA78BFA), "CHOCO", PackagingShape.TUB)
    "rusk" -> VisualSpec(Icons.Rounded.BakeryDining, Color(0xFFC2410C), Color(0xFFFDBA74), "ELAICHI", PackagingShape.POUCH)
    "cake" -> VisualSpec(Icons.Rounded.Cake, Color(0xFFDB2777), Color(0xFFF9A8D4), "SOFT", PackagingShape.TUB)
    "honey" -> VisualSpec(Icons.Rounded.WaterDrop, Color(0xFFD97706), Color(0xFFF59E0B), "WILD", PackagingShape.JAR)
    "oats" -> VisualSpec(Icons.Rounded.BreakfastDining, Color(0xFF15803D), Color(0xFF86EFAC), "MILLET", PackagingShape.POUCH)
    "spread" -> VisualSpec(Icons.Rounded.BreakfastDining, Color(0xFF7C3AED), Color(0xFFD8B4FE), "SMOOTH", PackagingShape.JAR)
    "cereal" -> VisualSpec(Icons.Rounded.BreakfastDining, Color(0xFF0284C7), Color(0xFF93C5FD), "RAGI", PackagingShape.TUB)
    "icecream" -> VisualSpec(Icons.Rounded.Icecream, Color(0xFF7C3AED), Color(0xFFC4B5FD), "TUB", PackagingShape.TUB)
    "peas" -> VisualSpec(Icons.Rounded.AcUnit, Color(0xFF16A34A), Color(0xFF86EFAC), "FROZEN", PackagingShape.POUCH)
    "cone" -> VisualSpec(Icons.Rounded.Icecream, Color(0xFFD97706), Color(0xFFFDE68A), "CRUNCH", PackagingShape.TUB)
    "kulfi" -> VisualSpec(Icons.Rounded.Icecream, Color(0xFFB45309), Color(0xFFFCD34D), "RABRI", PackagingShape.TUB)
    "freshener" -> VisualSpec(Icons.Rounded.Spa, Color(0xFF9333EA), Color(0xFFD8B4FE), "FRESH", PackagingShape.BOTTLE)
    "spray" -> VisualSpec(Icons.Rounded.Sanitizer, Color(0xFF0284C7), Color(0xFF7DD3FC), "99.9%", PackagingShape.BOTTLE)
    "dishwash" -> VisualSpec(Icons.Rounded.CleaningServices, Color(0xFFCA8A04), Color(0xFFFDE047), "LEMON", PackagingShape.BOTTLE)
    "cleaner" -> VisualSpec(Icons.Rounded.CleaningServices, Color(0xFF059669), Color(0xFF6EE7B7), "PINE", PackagingShape.BOTTLE)
    "soap" -> VisualSpec(Icons.Rounded.Sanitizer, Color(0xFF10B981), Color(0xFFA7F3D0), "HERBAL", PackagingShape.TUB)
    "toothpaste" -> VisualSpec(Icons.Rounded.FaceRetouchingNatural, Color(0xFFDC2626), Color(0xFFFCA5A5), "AYURVEDA", PackagingShape.TUB)
    "hairoil" -> VisualSpec(Icons.Rounded.WaterDrop, Color(0xFF15803D), Color(0xFF86EFAC), "AMLA", PackagingShape.BOTTLE)
    "skincare" -> VisualSpec(Icons.Rounded.LocalFlorist, Color(0xFFDB2777), Color(0xFFFBCFE8), "ROSE", PackagingShape.BOTTLE)
    "headphones" -> VisualSpec(Icons.Rounded.Headphones, Color(0xFF1D4ED8), Color(0xFF93C5FD), "AUDIO", PackagingShape.OBJECT)
    "electrical" -> VisualSpec(Icons.Rounded.ElectricalServices, Color(0xFF0F766E), Color(0xFF5EEAD4), "POWER", PackagingShape.OBJECT)
    "watch" -> VisualSpec(Icons.Rounded.Watch, Color(0xFF4338CA), Color(0xFFA5B4FC), "SMART", PackagingShape.OBJECT)
    "grooming" -> VisualSpec(Icons.Rounded.FaceRetouchingNatural, Color(0xFF1E293B), Color(0xFF94A3B8), "GROOM", PackagingShape.OBJECT)
    "eggs" -> VisualSpec(Icons.Rounded.Egg, Color(0xFFD97706), Color(0xFFFDE68A), "FARM EGGS", PackagingShape.TUB)
    "meat" -> VisualSpec(Icons.Rounded.Restaurant, Color(0xFFB91C1C), Color(0xFFFCA5A5), "FRESH CUT", PackagingShape.TUB)
    "coffee" -> VisualSpec(Icons.Rounded.Coffee, Color(0xFF78350F), Color(0xFFD97706), "ROASTED", PackagingShape.JAR)
    "water" -> VisualSpec(Icons.Rounded.WaterDrop, Color(0xFF0284C7), Color(0xFF7DD3FC), "MINERAL", PackagingShape.BOTTLE)
    "chocolate" -> VisualSpec(Icons.Rounded.Cookie, Color(0xFF6D28D9), Color(0xFFC4B5FD), "SILK", PackagingShape.TUB)
    "noodles" -> VisualSpec(Icons.Rounded.SoupKitchen, Color(0xFFEA580C), Color(0xFFFDBA74), "2-MIN", PackagingShape.POUCH)
    "kitchen" -> VisualSpec(Icons.Rounded.SoupKitchen, Color(0xFF475569), Color(0xFFCBD5E1), "STEEL", PackagingShape.OBJECT)
    "garden" -> VisualSpec(Icons.Rounded.Park, Color(0xFF15803D), Color(0xFF86EFAC), "ORGANIC", PackagingShape.POUCH)
    "baby" -> VisualSpec(Icons.Rounded.ChildCare, Color(0xFF0284C7), Color(0xFFBAE6FD), "GENTLE", PackagingShape.POUCH)
    "medicine" -> VisualSpec(Icons.Rounded.MedicalServices, Color(0xFF059669), Color(0xFF6EE7B7), "IMMUNITY", PackagingShape.JAR)
    "stationery" -> VisualSpec(Icons.Rounded.Edit, Color(0xFF2563EB), Color(0xFF93C5FD), "SCHOOL", PackagingShape.OBJECT)
    "flower" -> VisualSpec(Icons.Rounded.LocalFlorist, Color(0xFFE11D48), Color(0xFFFDA4AF), "FRESH BLOOM", PackagingShape.PRODUCE)
    "dryfruit" -> VisualSpec(Icons.Rounded.Spa, Color(0xFFB45309), Color(0xFFFDE68A), "PREMIUM", PackagingShape.POUCH)
    else -> VisualSpec(Icons.Rounded.LocalGroceryStore, QuickGreen, Color(0xFF86EFAC), "FRESH", PackagingShape.POUCH)
}

@Composable
fun ProductVisual(
    imageKey: String,
    colorHex: Long,
    modifier: Modifier = Modifier,
    iconSize: Dp = 46.dp,
    showTag: Boolean = false,
    imageUrl: String = "",
    brandName: String = "",
    brandLogoUrl: String = "",
    brandColorHex: Long = 0xFF0C831FL
) {
    val bg = Color(colorHex)
    val spec = resolveProductVisualSpec(imageKey)
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White, bg.copy(alpha = 0.92f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotBlank()) {
            CrispInternetLogoImage(
                url = imageUrl,
                contentDescription = imageKey,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                minIntrinsicPx = 64f
            ) {
                ProductPackagingArtwork(
                    spec = spec,
                    iconSize = iconSize,
                    imageKey = imageKey,
                    brandName = brandName,
                    brandLogoUrl = brandLogoUrl
                )
            }
        } else {
            ProductPackagingArtwork(
                spec = spec,
                iconSize = iconSize,
                imageKey = imageKey,
                brandName = brandName,
                brandLogoUrl = brandLogoUrl
            )
        }

        if (brandName.isNotBlank() && !showTag) {
            BrandCornerLogoPill(
                brandName = brandName,
                logoUrl = brandLogoUrl,
                accentColor = Color(brandColorHex),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
            )
        }

        if (showTag) {
            Surface(
                color = spec.accent.copy(alpha = 0.14f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = spec.tag,
                    style = MaterialTheme.typography.labelSmall,
                    color = spec.accent,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun ProductPackagingArtwork(
    spec: VisualSpec,
    iconSize: Dp,
    imageKey: String,
    brandName: String = "",
    brandLogoUrl: String = ""
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * 0.5f
            val cy = h * 0.5f

            // Soft ambient halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(spec.accent.copy(alpha = 0.16f), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = size.minDimension * 0.48f
                ),
                radius = size.minDimension * 0.48f,
                center = Offset(cx, cy)
            )

            // Ground contact shadow
            drawOval(
                color = Color.Black.copy(alpha = 0.10f),
                topLeft = Offset(cx - w * 0.26f, h * 0.80f),
                size = Size(w * 0.52f, h * 0.09f)
            )

            when (spec.shape) {
                PackagingShape.PRODUCE -> {
                    val r = size.minDimension * 0.29f
                    // Produce silhouette with radial highlight
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(spec.secondary.copy(alpha = 0.55f), spec.accent.copy(alpha = 0.88f)),
                            center = Offset(cx - r * 0.25f, cy - r * 0.22f),
                            radius = r * 1.4f
                        ),
                        radius = r,
                        center = Offset(cx, cy + h * 0.03f)
                    )
                    // Specular gloss crescent
                    drawOval(
                        color = Color.White.copy(alpha = 0.42f),
                        topLeft = Offset(cx - r * 0.55f, cy - r * 0.55f),
                        size = Size(r * 0.45f, r * 0.24f)
                    )
                    // Stem/leaf accent on top
                    val leaf = Path().apply {
                        moveTo(cx, cy - r * 0.82f)
                        quadraticTo(cx + r * 0.45f, cy - r * 1.25f, cx + r * 0.65f, cy - r * 0.72f)
                        quadraticTo(cx + r * 0.22f, cy - r * 0.62f, cx, cy - r * 0.82f)
                        close()
                    }
                    drawPath(leaf, color = Color(0xFF15803D))
                }
                PackagingShape.CARTON -> {
                    val boxW = w * 0.42f
                    val boxH = h * 0.58f
                    val left = cx - boxW * 0.5f
                    val top = cy - boxH * 0.45f
                    // Gable top
                    val roof = Path().apply {
                        moveTo(left, top + boxH * 0.16f)
                        lineTo(left + boxW * 0.18f, top)
                        lineTo(left + boxW * 0.82f, top)
                        lineTo(left + boxW, top + boxH * 0.16f)
                        close()
                    }
                    drawPath(roof, color = spec.accent.copy(alpha = 0.85f))
                    // Carton body
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(Color.White, spec.accent.copy(alpha = 0.24f))),
                        topLeft = Offset(left, top + boxH * 0.14f),
                        size = Size(boxW, boxH * 0.86f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                    // Accent ribbon stripe
                    drawRect(
                        color = spec.accent.copy(alpha = 0.88f),
                        topLeft = Offset(left, top + boxH * 0.42f),
                        size = Size(boxW, boxH * 0.26f)
                    )
                }
                PackagingShape.JAR -> {
                    val jarW = w * 0.44f
                    val jarH = h * 0.54f
                    val left = cx - jarW * 0.5f
                    val top = cy - jarH * 0.42f
                    // Metallic cap
                    drawRoundRect(
                        color = spec.secondary,
                        topLeft = Offset(cx - jarW * 0.34f, top),
                        size = Size(jarW * 0.68f, jarH * 0.18f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    // Jar body
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(spec.accent.copy(alpha = 0.78f), spec.secondary.copy(alpha = 0.92f))),
                        topLeft = Offset(left, top + jarH * 0.16f),
                        size = Size(jarW, jarH * 0.84f),
                        cornerRadius = CornerRadius(18f, 18f)
                    )
                    // Label band
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.92f),
                        topLeft = Offset(left + jarW * 0.08f, top + jarH * 0.38f),
                        size = Size(jarW * 0.84f, jarH * 0.34f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
                PackagingShape.BOTTLE -> {
                    val bW = w * 0.34f
                    val bH = h * 0.62f
                    val left = cx - bW * 0.5f
                    val top = cy - bH * 0.46f
                    // Bottle cap & neck
                    drawRoundRect(
                        color = spec.accent,
                        topLeft = Offset(cx - bW * 0.22f, top),
                        size = Size(bW * 0.44f, bH * 0.14f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        brush = Brush.horizontalGradient(listOf(spec.accent.copy(alpha = 0.85f), spec.secondary.copy(alpha = 0.75f))),
                        topLeft = Offset(left, top + bH * 0.16f),
                        size = Size(bW, bH * 0.84f),
                        cornerRadius = CornerRadius(16f, 16f)
                    )
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.88f),
                        topLeft = Offset(left + bW * 0.10f, top + bH * 0.40f),
                        size = Size(bW * 0.80f, bH * 0.34f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
                PackagingShape.POUCH -> {
                    val pW = w * 0.48f
                    val pH = h * 0.58f
                    val left = cx - pW * 0.5f
                    val top = cy - pH * 0.45f
                    // Standing pouch silhouette
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(spec.accent.copy(alpha = 0.90f), spec.secondary.copy(alpha = 0.82f))),
                        topLeft = Offset(left, top),
                        size = Size(pW, pH),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    // Top seal crimp line
                    drawLine(
                        color = Color.White.copy(alpha = 0.45f),
                        start = Offset(left + 6f, top + pH * 0.12f),
                        end = Offset(left + pW - 6f, top + pH * 0.12f),
                        strokeWidth = 2.5f
                    )
                    // White label window
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.92f),
                        topLeft = Offset(left + pW * 0.12f, top + pH * 0.26f),
                        size = Size(pW * 0.76f, pH * 0.46f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                }
                PackagingShape.TUB -> {
                    val tW = w * 0.54f
                    val tH = h * 0.44f
                    val left = cx - tW * 0.5f
                    val top = cy - tH * 0.36f
                    // Lid rim
                    drawRoundRect(
                        color = spec.accent,
                        topLeft = Offset(left - 4f, top),
                        size = Size(tW + 8f, tH * 0.24f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                    // Tub body
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(Color.White, spec.secondary.copy(alpha = 0.35f))),
                        topLeft = Offset(left, top + tH * 0.20f),
                        size = Size(tW, tH * 0.80f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )
                    drawRect(
                        color = spec.accent.copy(alpha = 0.22f),
                        topLeft = Offset(left, top + tH * 0.48f),
                        size = Size(tW, tH * 0.22f)
                    )
                }
                PackagingShape.OBJECT -> {
                    drawCircle(
                        brush = Brush.linearGradient(listOf(Color.White, spec.secondary.copy(alpha = 0.35f))),
                        radius = size.minDimension * 0.32f,
                        center = Offset(cx, cy)
                    )
                }
            }
        }

        // Centered crisp badge icon or authentic embedded brand logo on packaging artwork
        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.96f),
            border = BorderStroke(0.8.dp, spec.accent.copy(alpha = 0.25f)),
            shadowElevation = 2.dp,
            modifier = Modifier.size((iconSize * 0.92f).coerceAtLeast(30.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                if (spec.shape != PackagingShape.PRODUCE && brandLogoUrl.isNotBlank()) {
                    CrispInternetLogoImage(
                        url = brandLogoUrl,
                        contentDescription = brandName.ifBlank { imageKey },
                        contentScale = ContentScale.Fit,
                        minIntrinsicPx = 48f,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = spec.icon,
                            contentDescription = imageKey,
                            tint = spec.accent,
                            modifier = Modifier.size((iconSize * 0.56f).coerceAtLeast(18.dp))
                        )
                    }
                } else {
                    Icon(
                        imageVector = spec.icon,
                        contentDescription = imageKey,
                        tint = spec.accent,
                        modifier = Modifier.size((iconSize * 0.56f).coerceAtLeast(18.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun BrandCornerLogoPill(
    brandName: String,
    logoUrl: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(7.dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke(0.8.dp, accentColor.copy(alpha = 0.35f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (logoUrl.isNotBlank()) {
                CrispInternetLogoImage(
                    url = logoUrl,
                    contentDescription = "$brandName logo",
                    contentScale = ContentScale.Fit,
                    minIntrinsicPx = 48f,
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                ) {
                    BrandDotMonogram(brandName = brandName, accentColor = accentColor, size = 14.dp)
                }
                Spacer(Modifier.width(3.dp))
            }
            Text(
                text = brandName.split(" ").first(),
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@Composable
private fun BrandDotMonogram(
    brandName: String,
    accentColor: Color,
    size: Dp
) {
    Surface(
        shape = CircleShape,
        color = accentColor,
        modifier = Modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = brandName.firstOrNull()?.uppercaseChar()?.toString() ?: "Q",
                fontSize = (size.value * 0.55f).sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
fun CategoryVisualIcon(
    iconKey: String,
    colorHex: Long,
    modifier: Modifier = Modifier,
    size: Dp = 54.dp
) {
    val (icon, tint) = when (iconKey) {
        "veg" -> Icons.Rounded.Eco to Color(0xFF15803D)
        "fruits" -> Icons.Rounded.LocalFlorist to Color(0xFFEA580C)
        "dairy" -> Icons.Rounded.LocalDrink to Color(0xFF0284C7)
        "staples" -> Icons.Rounded.RiceBowl to Color(0xFFB45309)
        "spices" -> Icons.Rounded.Whatshot to Color(0xFFDC2626)
        "snacks" -> Icons.Rounded.Fastfood to Color(0xFFD97706)
        "drinks" -> Icons.Rounded.LocalCafe to Color(0xFF0F766E)
        "bakery" -> Icons.Rounded.Cookie to Color(0xFFDB2777)
        "breakfast" -> Icons.Rounded.BreakfastDining to Color(0xFF2563EB)
        "frozen" -> Icons.Rounded.Icecream to Color(0xFF0284C7)
        "cleaning" -> Icons.Rounded.CleaningServices to Color(0xFF059669)
        "personal" -> Icons.Rounded.FaceRetouchingNatural to Color(0xFF7C3AED)
        else -> Icons.Rounded.LocalGroceryStore to QuickGreen
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(colorHex), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = tint.copy(alpha = 0.13f),
            modifier = Modifier.size(size * 0.72f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = iconKey,
                    tint = tint,
                    modifier = Modifier.size(size * 0.44f)
                )
            }
        }
    }
}

@Composable
fun CrispInternetLogoImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    minIntrinsicPx: Float = 64f,
    fallback: @Composable () -> Unit
) {
    if (url.isBlank() || url.contains(".svg", ignoreCase = true)) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) { fallback() }
        return
    }
    val context = LocalContext.current
    val imageRequest = remember(context, url) {
        ImageRequest.Builder(context)
            .data(url)
            .size(256)
            .allowHardware(true)
            .addHeader("User-Agent", "QuickKartAndroid/1.0 (https://quickkart.in)")
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
    val painter = rememberAsyncImagePainter(
        model = imageRequest,
        filterQuality = FilterQuality.Medium
    )
    val state = painter.state
    val isSharpLoaded = state is AsyncImagePainter.State.Success &&
        (painter.intrinsicSize.width <= 0f || painter.intrinsicSize.width >= minIntrinsicPx)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = if (isSharpLoaded) 1f else 0f,
            modifier = Modifier.fillMaxSize()
        )
        if (!isSharpLoaded) {
            fallback()
        }
    }
}

@Composable
fun BrandVisualIcon(
    brand: Brand,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    fillCardWidth: Boolean = false
) {
    val cardModifier = if (fillCardWidth) {
        modifier
            .fillMaxWidth()
            .aspectRatio(1.22f)
    } else {
        modifier.size(size)
    }

    Surface(
        modifier = cardModifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF5EB),
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (fillCardWidth) 6.dp else 4.dp),
            contentAlignment = Alignment.Center
        ) {
            CrispInternetLogoImage(
                url = brand.logoUrl,
                contentDescription = "${brand.name} logo",
                contentScale = ContentScale.Fit,
                minIntrinsicPx = 64f,
                modifier = Modifier.fillMaxSize()
            ) {
                BrandCustomEmblemFallback(brand = brand, compact = !fillCardWidth && size <= 40.dp)
            }
        }
    }
}

@Composable
fun BrandCustomEmblemFallback(
    brand: Brand,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val brandColor = Color(brand.colorHex)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (brand.id) {
            "brand_hul" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "U",
                        fontSize = if (compact) 16.sp else 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF1F36C7)
                    )
                    if (!compact) {
                        Text(
                            text = "Hindustan Unilever Ltd.",
                            fontSize = 6.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1F36C7),
                            maxLines = 1
                        )
                    }
                }
            }
            "brand_dabur" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.size(if (compact) 16.dp else 26.dp)) {
                        val w = size.width
                        val h = size.height
                        drawCircle(
                            color = Color(0xFF16A34A),
                            radius = w * 0.36f,
                            center = Offset(w * 0.5f, h * 0.36f)
                        )
                        drawCircle(
                            color = Color(0xFF22C55E),
                            radius = w * 0.24f,
                            center = Offset(w * 0.28f, h * 0.44f)
                        )
                        drawCircle(
                            color = Color(0xFF15803D),
                            radius = w * 0.24f,
                            center = Offset(w * 0.72f, h * 0.44f)
                        )
                        drawRoundRect(
                            color = Color(0xFFEA580C),
                            topLeft = Offset(w * 0.43f, h * 0.52f),
                            size = Size(w * 0.14f, h * 0.42f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }
                    if (!compact) {
                        Text(
                            text = "Dabur",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF166534)
                        )
                    }
                }
            }
            "brand_reckitt" -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (compact) "r" else "reckitt",
                        fontSize = if (compact) 14.sp else 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE60067)
                    )
                    if (!compact) {
                        Spacer(Modifier.width(2.dp))
                        Canvas(modifier = Modifier.size(14.dp)) {
                            drawCircle(
                                color = Color(0xFFE60067),
                                radius = size.minDimension * 0.42f,
                                style = Stroke(width = 3.2f)
                            )
                            drawCircle(
                                color = Color(0xFFE60067),
                                radius = size.minDimension * 0.2f
                            )
                        }
                    }
                }
            }
            "brand_tata" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "T",
                        fontSize = if (compact) 14.sp else 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E56A0)
                    )
                    if (!compact) {
                        Text(
                            text = "TATA",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color(0xFF1E56A0)
                        )
                        Text(
                            text = "CONSUMER",
                            fontSize = 5.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
            }
            "brand_itc" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.size(if (compact) 18.dp else 26.dp)) {
                        val p = Path().apply {
                            moveTo(size.width * 0.5f, 0f)
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            close()
                        }
                        drawPath(p, color = Color(0xFF0A3868))
                        val inner = Path().apply {
                            moveTo(size.width * 0.5f, size.height * 0.34f)
                            lineTo(size.width * 0.72f, size.height * 0.85f)
                            lineTo(size.width * 0.28f, size.height * 0.85f)
                            close()
                        }
                        drawPath(inner, color = Color(0xFFFFF5EB))
                    }
                    if (!compact) {
                        Text(
                            text = "ITC Limited",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0A3868)
                        )
                    }
                }
            }
            "brand_mondelez" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (compact) "M" else "Mondelēz",
                        fontSize = if (compact) 14.sp else 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF6B21A8)
                    )
                    if (!compact) {
                        Text(
                            text = "International",
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF9333EA)
                        )
                    }
                }
            }
            "brand_godrej" -> {
                Text(
                    text = if (compact) "G" else "Godrej",
                    fontSize = if (compact) 16.sp else 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFC01F76)
                )
            }
            "brand_pg" -> {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF003DA5),
                    modifier = Modifier.size(if (compact) 26.dp else 44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "P&G",
                            fontSize = if (compact) 9.sp else 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            color = Color.White
                        )
                    }
                }
            }
            "brand_varun" -> {
                Text(
                    text = "VB",
                    fontSize = if (compact) 14.sp else 24.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF14286E)
                )
            }
            "brand_marico" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "M",
                        fontSize = if (compact) 14.sp else 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF65A30D)
                    )
                    if (!compact) {
                        Text(
                            text = "marico",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E6BB8)
                        )
                    }
                }
            }
            "brand_nestle" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (compact) "N" else "Nestlé",
                        fontSize = if (compact) 14.sp else 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF005596)
                    )
                    if (!compact) {
                        Text(
                            text = "Good Food, Good Life",
                            fontSize = 5.5.sp,
                            color = Color(0xFF475569),
                            maxLines = 1
                        )
                    }
                }
            }
            "brand_pepsico" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.size(if (compact) 16.dp else 22.dp)) {
                        drawCircle(color = Color(0xFF004B93), radius = size.minDimension * 0.45f)
                        drawArc(
                            color = Color(0xFFE11D2E),
                            startAngle = 190f,
                            sweepAngle = 160f,
                            useCenter = true
                        )
                    }
                    if (!compact) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "PEPSICO",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.6.sp,
                            color = Color(0xFF004B93)
                        )
                    }
                }
            }
            "brand_britannia" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = Color(0xFFD81E27),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (compact) "BRIT" else "BRITANNIA",
                            fontSize = if (compact) 7.sp else 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (!compact) {
                        Spacer(Modifier.height(1.dp))
                        Box(
                            Modifier
                                .width(46.dp)
                                .height(2.dp)
                                .background(Color(0xFF16A34A))
                        )
                    }
                }
            }
            "brand_colgate" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "CP",
                        fontSize = if (compact) 13.sp else 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0078C8)
                    )
                    if (!compact) {
                        Text(
                            text = "COLGATE-PALMOLIVE",
                            fontSize = 5.2.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827),
                            maxLines = 1
                        )
                    }
                }
            }
            "brand_amul" -> {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF111827)),
                    modifier = Modifier.size(if (compact) 26.dp else 44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Amul",
                            fontSize = if (compact) 7.sp else 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
            "brand_ferrero" -> {
                Surface(
                    color = Color(0xFF5D3A1A).copy(alpha = 0.08f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (compact) "FER" else "FERRERO",
                        fontSize = if (compact) 8.sp else 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 0.8.sp,
                        color = Color(0xFF5D3A1A),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
            "brand_rspl" -> {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFC82829)),
                    modifier = Modifier.size(if (compact) 26.dp else 44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "RSPL",
                            fontSize = if (compact) 7.sp else 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC82829)
                        )
                    }
                }
            }
            "brand_parle" -> {
                Surface(
                    color = Color(0xFFE11D2E),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 3.dp, bottomEnd = 3.dp)
                ) {
                    Text(
                        text = if (compact) "P" else "PARLE",
                        fontSize = if (compact) 11.sp else 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = if (compact) 7.dp else 10.dp, vertical = 3.dp)
                    )
                }
            }
            "brand_adani" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (compact) "aw" else "adani",
                        fontSize = if (compact) 12.sp else 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0E7490)
                    )
                    if (!compact) {
                        Text(
                            text = "wilmar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
            }
            "brand_zydus" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (compact) "Z" else "Zydus",
                        fontSize = if (compact) 13.sp else 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0369A1)
                    )
                    if (!compact) {
                        Text(
                            text = "Wellness",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
            }
            "brand_haldiram" -> {
                Surface(
                    color = Color(0xFFC81E2B),
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFF59E0B))
                ) {
                    Text(
                        text = if (compact) "HR" else "Haldiram's",
                        fontSize = if (compact) 9.sp else 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = if (compact) 6.dp else 9.dp, vertical = 4.dp)
                    )
                }
            }
            "brand_kellogg" -> {
                Text(
                    text = if (compact) "K" else "Kellogg's",
                    fontSize = if (compact) 14.sp else 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFD3122A)
                )
            }
            "brand_bajaj" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (compact) "b" else "bajaj",
                        fontSize = if (compact) 14.sp else 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFD93829)
                    )
                    if (!compact) {
                        Text(
                            text = "CONSUMER CARE",
                            fontSize = 5.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626),
                            maxLines = 1
                        )
                    }
                }
            }
            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.Storefront,
                        contentDescription = brand.name,
                        tint = brandColor,
                        modifier = Modifier.size(if (compact) 16.dp else 24.dp)
                    )
                    if (!compact) {
                        Text(
                            text = brand.name,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = brandColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private fun subBrandFaviconPng(domain: String): String =
    "https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://$domain&size=256"

fun resolveSubBrandLogoUrl(subBrandName: String, manufacturerLogoUrl: String): String {
    val key = subBrandName.trim().lowercase()
    if (key.isBlank()) return manufacturerLogoUrl

    // High-resolution verified Wikimedia PNG wordmarks where available
    val wikiFile = when {
        "kissan" in key -> "Kissan_logo.png"
        key == "dove" -> "Dove_logo.png"
        "lifebuoy" in key -> "Logo_Lifebuoy.png"
        key == "dettol" -> "Dettol_brand_wordmark.png"
        key == "doritos" -> "Logo_Doritos.png"
        key == "quaker" -> "Quaker_oats_textlogo.png"
        else -> null
    }
    if (wikiFile != null) {
        return "https://commons.wikimedia.org/wiki/Special:FilePath/$wikiFile?width=256"
    }

    // Official brand domain 256px PNGs (fast CDN, native PNG — never SVG)
    val brandDomain = when {
        // Hindustan Unilever sub-brands
        "surf excel" in key -> "www.surfexcel.in"
        "horlicks" in key -> "www.horlicks.in"
        "lakm" in key -> "www.lakmeindia.com"
        "pond" in key -> "www.ponds.in"
        key == "vaseline" -> "www.vaseline.com"
        key == "lipton" -> "www.lipton.com"
        key == "knorr" -> "www.knorr.com"
        "tresemm" in key -> "www.tresemme.com"
        key == "axe" -> "www.axe.com"
        "rexona" in key -> "www.rexona.com"
        "hellmann" in key -> "www.hellmanns.com"
        "close up" in key || key == "closeup" -> "www.closeup.com"
        "pepsodent" in key -> "www.pepsodent.in"
        "sunsilk" in key -> "www.sunsilk.in"
        "glow & lovely" in key -> "www.glowandlovelycareers.in"
        "clinic plus" in key -> "www.clinicplus.in"
        "domex" in key -> "www.domex.in"
        "brook" in key || "red label" in key || "taj mahal" in key || key == "bru" ||
            key == "vim" || key == "rin" || key == "comfort" || key == "wheel" ||
            key == "boost" || key == "pears" || "indulekha" in key ||
            key == "sunlight" || "lever ayush" in key -> "www.hul.co.in"

        // Tata Consumer & Croma
        "tetley" in key -> "www.tetley.in"
        "soulfull" in key -> "www.tatasoulfull.com"
        "croma" in key -> "www.croma.com"
        "tata" in key || "himalayan" in key -> "www.tataconsumer.com"

        // ITC Limited sub-brands
        "aashirvaad" in key -> "www.aashirvaad.com"
        "sunfeast" in key || "yippee" in key -> "www.sunfeastworld.com"
        "bingo" in key -> "www.bingosnacks.com"
        "classmate" in key -> "www.classmateshop.com"
        "savlon" in key -> "www.savlon.in"
        "vivel" in key || "fiama" in key || "engage" in key || "candyman" in key ||
            "b natural" in key || "nimyle" in key || "mangaldeep" in key ||
            "itc " in key -> "www.itcstore.in"

        // Nestle sub-brands
        "maggi" in key -> "www.maggi.in"
        "nescafe" in key -> "www.nescafe.com"
        "kitkat" in key -> "www.kitkat.com"
        "nestea" in key || "nestle" in key -> "www.nestle.in"

        // Mondelez sub-brands
        "cadbury" in key -> "www.cadbury.co.uk"
        "oreo" in key -> "www.oreo.com"
        "bournvita" in key -> "www.mondelezinternational.com"

        // Procter & Gamble sub-brands
        "pampers" in key -> "www.pampers.com"
        "whisper" in key -> "www.whisper.co.in"
        "tide" in key -> "www.tide.com"
        "ariel" in key -> "www.ariel.in"

        // PepsiCo & Varun Beverages
        "lay's" in key || key == "lays" -> "www.lays.com"
        "tropicana" in key -> "www.tropicana.com"
        "aquafina" in key -> "www.aquafina.com"
        "kurkure" in key || "uncle chipps" in key || "sting" in key -> "www.pepsicoindia.co.in"

        // Reckitt Benckiser
        "durex" in key -> "www.durexindia.com"
        "harpic" in key || "lizol" in key || "veet" in key || "moov" in key ||
            "colin" in key || "vanish" in key || "airwick" in key ||
            "cherry blossom" in key -> "www.reckitt.com"

        // Dabur
        key == "real" -> "www.realfruitpower.com"
        "odonil" in key || "odopic" in key || "gulabari" in key ||
            "dabur" in key -> "www.dabur.com"

        // Godrej
        "good knight" in key || "godrej" in key || key == "hit" -> "www.godrejcp.com"

        // Marico
        "saffola" in key -> "www.saffolalife.com"
        "parachute" in key || "hair & care" in key || "set wet" in key ||
            key == "livon" -> "www.marico.com"

        // Britannia
        "good day" in key || "bourbon" in key || "nutrichoice" in key ||
            "toastea" in key || "britannia" in key -> "www.britannia.co.in"

        // Amul
        "amul" in key -> "www.amul.com"

        // Colgate Palmolive
        "colgate" in key -> "www.colgate.com"
        "palmolive" in key -> "www.palmolive.com"

        // Ferrero
        "ferrero" in key -> "www.ferrerorocher.com"
        "nutella" in key -> "www.nutella.com"

        // RSPL
        key == "ghadi" -> "www.ghadi.in"
        key == "xpert" -> "www.rspl.in"

        // Parle
        "parle" in key || "hide & seek" in key || "monaco" in key -> "www.parleproducts.com"

        // Adani Wilmar
        "fortune" in key -> "www.fortunefoods.com"

        // Zydus Wellness
        "sugar free" in key || "glucon-d" in key || "nutralite" in key -> "www.zyduswellness.com"

        // Haldiram's, Kellogg's, Bajaj
        "haldiram" in key -> "www.haldirams.com"
        "kellogg" in key -> "www.kelloggs.com"
        "bajaj" in key -> "www.bajajconsumercare.com"

        else -> return manufacturerLogoUrl
    }
    return subBrandFaviconPng(brandDomain)
}

@Composable
fun SubBrandVisualCard(
    subBrandName: String,
    manufacturer: Brand,
    modifier: Modifier = Modifier
) {
    val accentColor = Color(manufacturer.colorHex)
    val directLogoUrl = resolveSubBrandLogoUrl(subBrandName, "")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFDF5EE),
        border = BorderStroke(0.6.dp, Color(0xFFF3E4D3)),
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            if (directLogoUrl.isNotBlank()) {
                CrispInternetLogoImage(
                    url = directLogoUrl,
                    contentDescription = "$subBrandName brand logo",
                    contentScale = ContentScale.Fit,
                    minIntrinsicPx = 48f,
                    modifier = Modifier.fillMaxSize()
                ) {
                    SubBrandWordmarkEmblem(
                        subBrandName = subBrandName,
                        fallbackColor = accentColor
                    )
                }
            } else {
                SubBrandWordmarkEmblem(
                    subBrandName = subBrandName,
                    fallbackColor = accentColor
                )
            }
        }
    }
}

@Composable
private fun SubBrandWordmarkEmblem(
    subBrandName: String,
    fallbackColor: Color
) {
    val key = subBrandName.trim().lowercase()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            "surf excel" in key -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(color = Color(0xFFEC4899), shape = CircleShape) {
                        Text(
                            text = "Surf",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 1.dp)
                        )
                    }
                    Text(
                        text = "excel",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1D4ED8)
                    )
                }
            }
            key == "vim" -> {
                Surface(
                    color = Color(0xFF16A34A),
                    shape = CircleShape,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Vim",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFFACC15)
                        )
                    }
                }
            }
            key == "rin" -> {
                Surface(
                    color = Color(0xFF0057B8),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Rin",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
            key == "dove" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Dove",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF1E3A8A)
                    )
                    Box(
                        Modifier
                            .width(22.dp)
                            .height(2.5.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD97706))
                    )
                }
            }
            "kissan" in key -> {
                Surface(color = Color(0xFFD91E2A), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "kissan",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            key == "bru" -> {
                Surface(color = Color(0xFF166534), shape = CircleShape) {
                    Text(
                        text = "BRU",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFEF08A),
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp)
                    )
                }
            }
            "red label" in key -> {
                Surface(
                    color = Color(0xFFC81E2B),
                    shape = CircleShape,
                    border = BorderStroke(1.2.dp, Color(0xFFF59E0B))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Red",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            color = Color.White
                        )
                        Text(
                            text = "Label",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            color = Color.White
                        )
                    }
                }
            }
            "taj mahal" in key -> {
                Surface(
                    color = Color(0xFF1E3A8A),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.2.dp, Color(0xFFF59E0B))
                ) {
                    Text(
                        text = "TAJ\nMAHAL",
                        fontSize = 8.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFDE68A),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            "indulekha" in key -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF15803D))
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "indulekha",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3F2E26)
                    )
                }
            }
            key == "comfort" -> {
                Text(
                    text = "Comfort",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF1D4ED8)
                )
            }
            key == "wheel" -> {
                Surface(color = Color(0xFF16A34A), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "Wheel",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            "lifebuoy" in key -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Lifebuoy",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            key == "boost" -> {
                Surface(color = Color(0xFFEA580C), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "BOOST",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFFEF08A),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            key == "lux" -> {
                Text(
                    text = "LUX",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFB48332),
                    letterSpacing = 1.sp
                )
            }
            key == "pears" -> {
                Text(
                    text = "Pears",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF6B3E26)
                )
            }
            "horlicks" in key -> {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.2.dp, Color(0xFF1E3A8A))
                ) {
                    Text(
                        text = "Horlicks",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF1E3A8A),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                    )
                }
            }
            "glow & lovely" in key -> {
                Text(
                    text = "Glow&\nLovely",
                    fontSize = 10.sp,
                    lineHeight = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFBE185D)
                )
            }
            "clinic plus" in key -> {
                Text(
                    text = "CLINIC\nPLUS+",
                    fontSize = 10.sp,
                    lineHeight = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF0369A1)
                )
            }
            "close up" in key -> {
                Text(
                    text = "closeup",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFDC2626)
                )
            }
            key == "domex" -> {
                Surface(color = Color(0xFF1E3A8A), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "Domex",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            key == "lipton" -> {
                Surface(color = Color(0xFFFACC15), shape = CircleShape) {
                    Text(
                        text = "Lipton",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB91C1C),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
            "tresemm" in key -> {
                Surface(color = Color(0xFF111827), shape = RoundedCornerShape(4.dp)) {
                    Text(
                        text = "TRESemmé",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 6.dp)
                    )
                }
            }
            key == "sunsilk" -> {
                Surface(color = Color(0xFFDB2777), shape = CircleShape) {
                    Text(
                        text = "sunsilk",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 6.dp)
                    )
                }
            }
            key == "pepsodent" -> {
                Text(
                    text = "Pepsodent",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1D4ED8)
                )
            }
            "pond" in key -> {
                Text(
                    text = "POND'S",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF1F2937)
                )
            }
            "lakm" in key -> {
                Text(
                    text = "LAKMÉ",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp,
                    color = Color(0xFF111827)
                )
            }
            key == "vaseline" -> {
                Surface(color = Color(0xFF1E3A8A), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Vaseline",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            key == "knorr" -> {
                Surface(color = Color(0xFF15803D), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Knorr",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            "tata tea" in key || "tetley" in key || "tata coffee" in key || "himalayan" in key -> {
                val badgeBg = when {
                    "gold" in key -> Color(0xFF111827)
                    "agni" in key -> Color(0xFFB91C1C)
                    "chakra" in key -> Color(0xFFDC2626)
                    "kanan" in key -> Color(0xFF166534)
                    "tetley" in key -> Color(0xFF0284C7)
                    "himalayan" in key -> Color(0xFFBE185D)
                    else -> Color(0xFF1E56A0)
                }
                Surface(color = badgeBg, shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = subBrandName.replace("Tata Tea ", "Tea\n"),
                        fontSize = 8.5.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            "tata salt" in key -> {
                Surface(color = Color(0xFF451A03), shape = CircleShape) {
                    Text(
                        text = "TATA\nSalt",
                        fontSize = 9.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFDE68A),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            "tata sampann" in key -> {
                Surface(color = Color(0xFF065F46), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "TATA\nsampann",
                        fontSize = 8.5.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            "tata soulfull" in key -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "TATA\nSOULFULL",
                        fontSize = 7.5.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                    )
                }
            }
            "aashirvaad" in key -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF59E0B))
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "AASHIRVAAD",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFDC2626)
                    )
                }
            }
            "yippee" in key -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "YiPPee!",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFFACC15),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "sunfeast" in key -> {
                Surface(
                    color = Color.White,
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, Color(0xFFDC2626))
                ) {
                    Text(
                        text = "Sunfeast",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    )
                }
            }
            "bingo" in key -> {
                Surface(
                    color = Color(0xFFFACC15),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.4.dp, Color(0xFFDC2626))
                ) {
                    Text(
                        text = "BINGO!",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            "vivel" in key || "savlon" in key || "fiama" in key || "nimyle" in key || "b natural" in key || "engage" in key -> {
                val color = when {
                    "vivel" in key -> Color(0xFF7C3AED)
                    "savlon" in key -> Color(0xFFEA580C)
                    "nimyle" in key || "b natural" in key -> Color(0xFF15803D)
                    "engage" in key -> Color(0xFF0284C7)
                    else -> Color(0xFF111827)
                }
                Text(
                    text = subBrandName,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    color = color
                )
            }
            key == "maggi" -> {
                Surface(
                    color = Color(0xFFFACC15),
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, Color(0xFFDC2626))
                ) {
                    Text(
                        text = "Maggi",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
            "nescafe" in key -> {
                Surface(
                    color = if ("gold" in key) Color(0xFF78350F) else Color(0xFF111827),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if ("gold" in key) "NESCAFÉ\nGOLD" else "NESCAFÉ",
                        fontSize = 8.5.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = if ("gold" in key) Color(0xFFFDE68A) else Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                    )
                }
            }
            "nestle a+" in key -> {
                Surface(color = Color(0xFF0284C7), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Nestle\na+",
                        fontSize = 9.5.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            key == "nestea" -> {
                Surface(color = Color(0xFF0284C7), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "NESTEA",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFEF08A),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            key == "kitkat" -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(10.dp)) {
                    Text(
                        text = "KitKat",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            "dabur red" in key || "dabur amla" in key || "dabur honey" in key || "dabur" in key || key == "real" || key == "odonil" || "gulabari" in key -> {
                val badgeBg = when {
                    "red" in key -> Color(0xFFDC2626)
                    key == "real" -> Color(0xFF0284C7)
                    key == "odonil" -> Color(0xFF1D4ED8)
                    "gulabari" in key -> Color(0xFFDB2777)
                    "honey" in key -> Color(0xFFD97706)
                    else -> Color(0xFF15803D)
                }
                Surface(color = badgeBg, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = subBrandName.replace("Dabur ", ""),
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            key == "harpic" -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "HARPIC",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            key == "lizol" -> {
                Surface(color = Color(0xFF0284C7), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "Lizol",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            key == "dettol" -> {
                Surface(
                    color = Color(0xFF15803D),
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, Color.White)
                ) {
                    Text(
                        text = "Dettol",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 6.dp)
                    )
                }
            }
            key == "veet" -> {
                Text(
                    text = "Veet",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF0284C7)
                )
            }
            key == "durex" -> {
                Surface(color = Color(0xFF0284C7), shape = RoundedCornerShape(10.dp)) {
                    Text(
                        text = "durex",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            key == "moov" -> {
                Surface(color = Color(0xFF6B21A8), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "moov",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            key == "colin" -> {
                Text(
                    text = "Colin",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFDC2626)
                )
            }
            key == "vanish" -> {
                Text(
                    text = "Vanish",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFDB2777)
                )
            }
            key == "airwick" -> {
                Surface(color = Color(0xFF16A34A), shape = CircleShape) {
                    Text(
                        text = "AIR\nWICK",
                        fontSize = 8.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
            "godrej no.1" in key -> {
                Surface(color = Color(0xFF15803D), shape = CircleShape) {
                    Text(
                        text = "No.1",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp)
                    )
                }
            }
            key == "hit" -> {
                Surface(color = Color(0xFFFACC15), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "HIT",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF111827),
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }
            "godrej aer" in key -> {
                Text(
                    text = "aer",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF0F766E)
                )
            }
            "protekt" in key -> {
                Surface(color = Color(0xFF0EA5E9), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "protekt",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "good knight" in key -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Good\nknight",
                        fontSize = 8.5.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    )
                }
            }
            "parachute" in key -> {
                Surface(
                    color = if ("advansed" in key) Color(0xFF15803D) else Color(0xFF0284C7),
                    shape = RoundedCornerShape(7.dp)
                ) {
                    Text(
                        text = if ("advansed" in key) "Parachute\nADVANSED" else "Parachute",
                        fontSize = 8.5.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            "hair & care" in key -> {
                Text(
                    text = "Hair&\nCare",
                    fontSize = 11.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1F2937)
                )
            }
            "set wet" in key -> {
                Surface(color = Color(0xFF16A34A), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "SET\nWET",
                        fontSize = 9.5.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            key == "livon" -> {
                Surface(color = Color(0xFFDB2777), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "LIVON",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            "lay's" in key -> {
                Surface(
                    color = Color(0xFFFACC15),
                    shape = CircleShape,
                    border = BorderStroke(1.2.dp, Color(0xFFDC2626))
                ) {
                    Text(
                        text = "Lay's",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 6.dp)
                    )
                }
            }
            key == "kurkure" -> {
                Surface(color = Color(0xFF1E3A8A), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "Kurkure",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            key == "doritos" -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "Doritos",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "uncle chipps" in key -> {
                Surface(color = Color(0xFF0284C7), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "Uncle\nChipps",
                        fontSize = 8.5.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            key == "quaker" -> {
                Surface(color = Color(0xFF1E3A8A), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "QUAKER",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "glucon-d" in key -> {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(1.2.dp, Color(0xFFDC2626))
                ) {
                    Text(
                        text = "Glucon-D",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            "sugar free" in key -> {
                Text(
                    text = "Sugar\nFree",
                    fontSize = 11.5.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF0284C7)
                )
            }
            "nutralite" in key -> {
                Surface(color = Color(0xFF16A34A), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "Nutralite",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            "cadbury" in key -> {
                Surface(color = Color(0xFF4C1D95), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Cadbury",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            key == "oreo" -> {
                Surface(
                    color = Color(0xFF0057B8),
                    shape = CircleShape,
                    border = BorderStroke(1.2.dp, Color.White)
                ) {
                    Text(
                        text = "OREO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            "bournvita" in key -> {
                Surface(color = Color(0xFF92400E), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "Bourn\nVita",
                        fontSize = 9.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFDE68A),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            key == "tide" -> {
                Surface(
                    color = Color(0xFFF97316),
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, Color(0xFFFACC15))
                ) {
                    Text(
                        text = "Tide",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF1E3A8A),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            key == "ariel" -> {
                Surface(color = Color(0xFF047857), shape = CircleShape) {
                    Text(
                        text = "ARIEL",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 6.dp)
                    )
                }
            }
            "pampers" in key -> {
                Surface(color = Color(0xFF0D9488), shape = RoundedCornerShape(9.dp)) {
                    Text(
                        text = "Pampers",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "whisper" in key -> {
                Surface(color = Color(0xFFBE185D), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "whisper",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "tropicana" in key -> {
                Text(
                    text = "Tropicana",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF15803D)
                )
            }
            "aquafina" in key -> {
                Surface(color = Color(0xFF0284C7), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "AQUAFINA",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            key == "sting" -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "STING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFFACC15),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            "saffola" in key -> {
                Surface(color = Color(0xFFF59E0B), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Saffola",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            key == "colgate" -> {
                Surface(color = Color(0xFFDC2626), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Colgate",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            "palmolive" in key -> {
                Surface(color = Color(0xFF15803D), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Palmolive",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            "amul" in key -> {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.4.dp, Color(0xFFDC2626))
                ) {
                    Text(
                        text = "Amul",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            "ferrero" in key -> {
                Surface(
                    color = Color(0xFF451A03),
                    shape = RoundedCornerShape(7.dp),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                ) {
                    Text(
                        text = "FERRERO\nROCHER",
                        fontSize = 7.5.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFDE68A),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                    )
                }
            }
            "nutella" in key -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "n", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF111827))
                    Text(text = "utella", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                }
            }
            key == "ghadi" -> {
                Surface(color = Color(0xFFC82829), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "GHADI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFACC15),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
            "parle" in key || "hide & seek" in key || "monaco" in key -> {
                Surface(color = Color(0xFFE11D2E), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = subBrandName,
                        fontSize = 9.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "fortune" in key -> {
                Surface(color = Color(0xFF0E7490), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "Fortune",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFDE047),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
            "kellogg" in key -> {
                Text(
                    text = "Kellogg's",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFD3122A)
                )
            }
            "haldiram" in key -> {
                Surface(color = Color(0xFFC81E2B), shape = CircleShape) {
                    Text(
                        text = "Haldiram's",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    )
                }
            }
            "bajaj" in key -> {
                Surface(
                    color = Color(0xFFFFF7ED),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFD97706))
                ) {
                    Text(
                        text = "Bajaj\nAlmond",
                        fontSize = 9.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFB45309),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }
            "good day" in key || "bourbon" in key || "nutrichoice" in key || "toastea" in key || "britannia" in key -> {
                Surface(color = Color(0xFFD81E27), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = subBrandName.replace(" Bread & Cakes", ""),
                        fontSize = 8.5.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            "classmate" in key -> {
                Text(
                    text = "Classmate",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFF1D4ED8)
                )
            }
            "croma" in key -> {
                Surface(color = Color(0xFF111827), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = "croma",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2DD4BF),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }
            "itc" in key -> {
                Surface(color = Color(0xFF0A3868), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = subBrandName.replace("ITC ", "ITC\n"),
                        fontSize = 7.5.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                    )
                }
            }
            "tata" in key -> {
                Surface(color = Color(0xFF1E56A0), shape = RoundedCornerShape(7.dp)) {
                    Text(
                        text = subBrandName,
                        fontSize = 8.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                    )
                }
            }
            key == "xpert" || key == "odopic" || key == "candyman" || key == "mangaldeep" || "cherry blossom" in key -> {
                val pillColor = when {
                    key == "xpert" -> Color(0xFF15803D)
                    key == "odopic" -> Color(0xFF0284C7)
                    key == "candyman" -> Color(0xFFDC2626)
                    key == "mangaldeep" -> Color(0xFFEA580C)
                    else -> Color(0xFF1F2937)
                }
                Surface(color = pillColor, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = subBrandName,
                        fontSize = 8.5.sp,
                        lineHeight = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
            else -> {
                Surface(
                    color = fallbackColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, fallbackColor.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = subBrandName,
                        fontSize = 9.5.sp,
                        lineHeight = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = fallbackColor,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeCategoryClusterArtwork(
    iconKey: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.12f),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Warm cream card covering top 76% of the tile so products overlap neatly at bottom like the reference
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.78f)
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFFF6EC), Color(0xFFFFEEDF))
                    )
                )
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            val w = size.width
            val h = size.height

            // Soft realistic ambient ground shadow at product base
            drawOval(
                color = Color(0x22000000),
                topLeft = Offset(w * 0.14f, h * 0.87f),
                size = Size(w * 0.72f, h * 0.08f)
            )

            fun drawCrispPack(
                left: Float,
                top: Float,
                pw: Float,
                ph: Float,
                topColor: Color,
                bottomColor: Color,
                corner: Float = 9f,
                borderColor: Color = Color(0x28000000)
            ) {
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(topColor, bottomColor), startY = top, endY = top + ph),
                    topLeft = Offset(left, top),
                    size = Size(pw, ph),
                    cornerRadius = CornerRadius(corner, corner)
                )
                // Specular highlight strip on left edge
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.24f),
                    topLeft = Offset(left + pw * 0.08f, top + ph * 0.06f),
                    size = Size(pw * 0.14f, ph * 0.84f),
                    cornerRadius = CornerRadius(corner * 0.6f, corner * 0.6f)
                )
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(left, top),
                    size = Size(pw, ph),
                    cornerRadius = CornerRadius(corner, corner),
                    style = Stroke(width = 1.3f)
                )
            }

            fun drawSculptedBottle(
                left: Float,
                top: Float,
                bw: Float,
                bh: Float,
                bodyTop: Color,
                bodyBottom: Color,
                capColor: Color,
                labelColor: Color,
                labelBand: Color
            ) {
                // Bottle cap
                val capW = bw * 0.36f
                val capH = bh * 0.14f
                drawRoundRect(
                    color = capColor,
                    topLeft = Offset(left + (bw - capW) / 2f, top),
                    size = Size(capW, capH),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                // Sculpted shoulder + body Path
                val neckTop = top + capH - 1f
                val shoulderY = top + bh * 0.28f
                val bottomY = top + bh
                val bodyPath = Path().apply {
                    moveTo(left + bw * 0.34f, neckTop)
                    lineTo(left + bw * 0.66f, neckTop)
                    quadraticTo(left + bw * 0.96f, neckTop + (shoulderY - neckTop) * 0.45f, left + bw, shoulderY)
                    lineTo(left + bw, bottomY - 10f)
                    quadraticTo(left + bw, bottomY, left + bw - 10f, bottomY)
                    lineTo(left + 10f, bottomY)
                    quadraticTo(left, bottomY, left, bottomY - 10f)
                    lineTo(left, shoulderY)
                    quadraticTo(left + bw * 0.04f, neckTop + (shoulderY - neckTop) * 0.45f, left + bw * 0.34f, neckTop)
                    close()
                }
                drawPath(
                    path = bodyPath,
                    brush = Brush.horizontalGradient(listOf(bodyTop, bodyBottom, bodyTop.copy(alpha = 0.92f)), startX = left, endX = left + bw)
                )
                drawPath(path = bodyPath, color = Color(0x28000000), style = Stroke(width = 1.3f))
                // Glossy bottle sheen
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.28f),
                    topLeft = Offset(left + bw * 0.14f, shoulderY),
                    size = Size(bw * 0.14f, (bottomY - shoulderY) * 0.78f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // Product front label
                drawRoundRect(
                    color = labelColor,
                    topLeft = Offset(left + bw * 0.12f, top + bh * 0.40f),
                    size = Size(bw * 0.76f, bh * 0.38f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
                drawRoundRect(
                    color = labelBand,
                    topLeft = Offset(left + bw * 0.16f, top + bh * 0.48f),
                    size = Size(bw * 0.68f, bh * 0.12f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
            }

            when (iconKey) {
                "home_beauty_hygiene" -> {
                    // Green feminine hygiene pack (left-back)
                    drawCrispPack(w * 0.11f, h * 0.18f, w * 0.42f, h * 0.69f, Color(0xFF16A34A), Color(0xFF14532D), corner = 8f)
                    drawRoundRect(
                        color = Color(0xFF065F46),
                        topLeft = Offset(w * 0.11f, h * 0.18f),
                        size = Size(w * 0.42f, h * 0.14f),
                        cornerRadius = CornerRadius(7f, 7f)
                    )
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w * 0.16f, h * 0.40f),
                        size = Size(w * 0.30f, h * 0.20f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    drawRoundRect(
                        color = Color(0xFF7C3AED),
                        topLeft = Offset(w * 0.19f, h * 0.45f),
                        size = Size(w * 0.22f, h * 0.06f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Deep blue vaporub jar (center-front)
                    drawCrispPack(w * 0.42f, h * 0.48f, w * 0.23f, h * 0.41f, Color(0xFF2563EB), Color(0xFF1E3A8A), corner = 6f)
                    drawRoundRect(
                        color = Color(0xFF0F766E),
                        topLeft = Offset(w * 0.43f, h * 0.42f),
                        size = Size(w * 0.21f, h * 0.08f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = Color(0xFFDBEAFE),
                        topLeft = Offset(w * 0.45f, h * 0.58f),
                        size = Size(w * 0.17f, h * 0.18f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Circular soft blue cream tub (right-front)
                    drawCircle(
                        brush = Brush.radialGradient(listOf(Color(0xFFE0F2FE), Color(0xFF93C5FD))),
                        radius = w * 0.21f,
                        center = Offset(w * 0.69f, h * 0.64f)
                    )
                    drawCircle(
                        color = Color(0x28000000),
                        radius = w * 0.21f,
                        center = Offset(w * 0.69f, h * 0.64f),
                        style = Stroke(1.3f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.13f,
                        center = Offset(w * 0.69f, h * 0.62f)
                    )
                    drawRoundRect(
                        color = Color(0xFF0284C7),
                        topLeft = Offset(w * 0.61f, h * 0.59f),
                        size = Size(w * 0.16f, h * 0.045f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                }

                "home_cleaning_household" -> {
                    // Tall blue detergent pouch (back center)
                    drawCrispPack(w * 0.30f, h * 0.12f, w * 0.38f, h * 0.74f, Color(0xFF1E40AF), Color(0xFF1E3A8A), corner = 9f)
                    drawRoundRect(
                        color = Color(0xFF16A34A),
                        topLeft = Offset(w * 0.30f, h * 0.12f),
                        size = Size(w * 0.38f, h * 0.11f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawCircle(
                        color = Color(0xFF38BDF8),
                        radius = w * 0.125f,
                        center = Offset(w * 0.49f, h * 0.48f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.085f,
                        center = Offset(w * 0.49f, h * 0.48f)
                    )
                    // White sculpted detergent bottle (right)
                    drawSculptedBottle(
                        left = w * 0.61f, top = h * 0.24f, bw = w * 0.28f, bh = h * 0.65f,
                        bodyTop = Color(0xFFFFFFFF), bodyBottom = Color(0xFFE5E7EB),
                        capColor = Color(0xFF1D4ED8), labelColor = Color(0xFFF0FDF4), labelBand = Color(0xFF16A34A)
                    )
                    // Vibrant orange detergent pouch (left-front)
                    drawCrispPack(w * 0.10f, h * 0.44f, w * 0.33f, h * 0.45f, Color(0xFFFB923C), Color(0xFFEA580C), corner = 8f)
                    drawCircle(color = Color(0xFFFEF08A), radius = w * 0.105f, center = Offset(w * 0.265f, h * 0.66f))
                    drawCircle(color = Color(0xFF1D4ED8), radius = w * 0.06f, center = Offset(w * 0.265f, h * 0.66f))
                }

                "home_snacks_branded" -> {
                    // Tall beige/red namkeen pack (back)
                    drawCrispPack(w * 0.17f, h * 0.10f, w * 0.56f, h * 0.72f, Color(0xFFFEF3C7), Color(0xFFFDE68A), corner = 8f)
                    drawRoundRect(
                        color = Color(0xFFB91C1C),
                        topLeft = Offset(w * 0.17f, h * 0.10f),
                        size = Size(w * 0.56f, h * 0.16f),
                        cornerRadius = CornerRadius(7f, 7f)
                    )
                    // Crunchy bhujia window + red logo strip
                    drawRoundRect(
                        color = Color(0xFFF59E0B),
                        topLeft = Offset(w * 0.22f, h * 0.34f),
                        size = Size(w * 0.46f, h * 0.24f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    // Yellow snack box (middle right)
                    drawCrispPack(w * 0.39f, h * 0.44f, w * 0.46f, h * 0.32f, Color(0xFFFDE047), Color(0xFFEAB308), corner = 6f)
                    drawRoundRect(
                        color = Color(0xFF1D4ED8),
                        topLeft = Offset(w * 0.46f, h * 0.50f),
                        size = Size(w * 0.30f, h * 0.09f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    // Royal blue horizontal cookie pack (front bottom)
                    drawCrispPack(w * 0.22f, h * 0.67f, w * 0.62f, h * 0.22f, Color(0xFF2563EB), Color(0xFF1E3A8A), corner = 7f)
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w * 0.28f, h * 0.72f),
                        size = Size(w * 0.28f, h * 0.10f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawCircle(color = Color(0xFF1E293B), radius = w * 0.075f, center = Offset(w * 0.70f, h * 0.78f))
                    drawCircle(color = Color.White, radius = w * 0.03f, center = Offset(w * 0.70f, h * 0.78f))
                }

                "home_gourmet_world" -> {
                    // Rich crimson pasta box (left)
                    drawCrispPack(w * 0.14f, h * 0.15f, w * 0.46f, h * 0.74f, Color(0xFFB91C1C), Color(0xFF7F1D1D), corner = 7f)
                    drawRoundRect(
                        color = Color(0xFF166534),
                        topLeft = Offset(w * 0.21f, h * 0.21f),
                        size = Size(w * 0.32f, h * 0.13f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    // Transparent pasta showcase window
                    drawRoundRect(
                        color = Color(0xFFFEF08A),
                        topLeft = Offset(w * 0.20f, h * 0.42f),
                        size = Size(w * 0.34f, h * 0.36f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    // Penne strokes inside window
                    for (i in 0..3) {
                        drawRoundRect(
                            color = Color(0xFFD97706),
                            topLeft = Offset(w * (0.24f + i * 0.065f), h * 0.47f),
                            size = Size(w * 0.04f, h * 0.24f),
                            cornerRadius = CornerRadius(3f, 3f)
                        )
                    }
                    // Tall sculpted dark green olive oil bottle (right)
                    drawSculptedBottle(
                        left = w * 0.56f, top = h * 0.18f, bw = w * 0.28f, bh = h * 0.71f,
                        bodyTop = Color(0xFF166534), bodyBottom = Color(0xFF052E16),
                        capColor = Color(0xFFDC2626), labelColor = Color(0xFFFEF9C3), labelBand = Color(0xFF15803D)
                    )
                }

                "home_beverages" -> {
                    // Soft cream-orange spout pouch (left)
                    drawSculptedBottle(
                        left = w * 0.14f, top = h * 0.18f, bw = w * 0.36f, bh = h * 0.70f,
                        bodyTop = Color(0xFFFFEDD5), bodyBottom = Color(0xFFFED7AA),
                        capColor = Color(0xFFEA580C), labelColor = Color.White, labelBand = Color(0xFFF97316)
                    )
                    drawCircle(color = Color(0xFFEA580C), radius = w * 0.09f, center = Offset(w * 0.32f, h * 0.64f))
                    // Frosted pink fruit juice bottle (right)
                    drawSculptedBottle(
                        left = w * 0.53f, top = h * 0.22f, bw = w * 0.31f, bh = h * 0.66f,
                        bodyTop = Color(0xFFFCE7F3), bodyBottom = Color(0xFFF9A8D4),
                        capColor = Color(0xFFDB2777), labelColor = Color.White, labelBand = Color(0xFFEC4899)
                    )
                }

                "home_baby_care" -> {
                    // Teal & sunshine yellow diaper pack
                    drawCrispPack(w * 0.16f, h * 0.14f, w * 0.68f, h * 0.75f, Color(0xFF14B8A6), Color(0xFF0F766E), corner = 10f)
                    drawRoundRect(
                        color = Color(0xFFFACC15),
                        topLeft = Offset(w * 0.16f, h * 0.14f),
                        size = Size(w * 0.68f, h * 0.15f),
                        cornerRadius = CornerRadius(9f, 9f)
                    )
                    // Heart crest brand badge + white illustration box
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w * 0.22f, h * 0.36f),
                        size = Size(w * 0.56f, h * 0.44f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                    drawCircle(color = Color(0xFF0D9488), radius = w * 0.11f, center = Offset(w * 0.43f, h * 0.49f))
                    drawCircle(color = Color(0xFFFECACA), radius = w * 0.09f, center = Offset(w * 0.62f, h * 0.60f))
                    drawRoundRect(
                        color = Color(0xFF1D4ED8),
                        topLeft = Offset(w * 0.25f, h * 0.67f),
                        size = Size(w * 0.15f, h * 0.09f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                "home_bakery_dairy" -> {
                    // Golden whole-wheat bread loaf bag (left)
                    drawCrispPack(w * 0.20f, h * 0.11f, w * 0.42f, h * 0.68f, Color(0xFFE09F3E), Color(0xFFB45309), corner = 11f)
                    drawRoundRect(
                        color = Color(0xFF78350F),
                        topLeft = Offset(w * 0.25f, h * 0.30f),
                        size = Size(w * 0.32f, h * 0.24f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = Color(0xFFFEF3C7),
                        topLeft = Offset(w * 0.29f, h * 0.35f),
                        size = Size(w * 0.24f, h * 0.06f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    // Golden baked muffin (right)
                    drawCircle(
                        brush = Brush.radialGradient(listOf(Color(0xFFFCD34D), Color(0xFFD97706))),
                        radius = w * 0.145f,
                        center = Offset(w * 0.68f, h * 0.52f)
                    )
                    // Pink sliced cake bar pack (front bottom)
                    drawCrispPack(w * 0.16f, h * 0.64f, w * 0.66f, h * 0.25f, Color(0xFFF43F5E), Color(0xFFBE185D), corner = 7f)
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w * 0.21f, h * 0.68f),
                        size = Size(w * 0.26f, h * 0.14f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = Color(0xFFFEF08A),
                        topLeft = Offset(w * 0.53f, h * 0.68f),
                        size = Size(w * 0.22f, h * 0.16f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                "home_pharmacy_store" -> {
                    // Amber Dettol antiseptic bottle (left)
                    drawSculptedBottle(
                        left = w * 0.16f, top = h * 0.28f, bw = w * 0.26f, bh = h * 0.60f,
                        bodyTop = Color(0xFF9A3412), bodyBottom = Color(0xFF7C2D12),
                        capColor = Color(0xFF1F2937), labelColor = Color.White, labelBand = Color(0xFF15803D)
                    )
                    // Herbal honey-cough syrup bottle/jar (right)
                    drawCrispPack(w * 0.46f, h * 0.22f, w * 0.38f, h * 0.66f, Color(0xFFFEF3C7), Color(0xFFFDE68A), corner = 8f)
                    drawRoundRect(
                        color = Color(0xFFDC2626),
                        topLeft = Offset(w * 0.48f, h * 0.15f),
                        size = Size(w * 0.34f, h * 0.12f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    drawRoundRect(
                        color = Color(0xFFDC2626),
                        topLeft = Offset(w * 0.50f, h * 0.36f),
                        size = Size(w * 0.30f, h * 0.18f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    // Front blue balm jar
                    drawCrispPack(w * 0.34f, h * 0.66f, w * 0.24f, h * 0.23f, Color(0xFF2563EB), Color(0xFF1E3A8A), corner = 6f)
                    drawRoundRect(
                        color = Color(0xFF0F766E),
                        topLeft = Offset(w * 0.35f, h * 0.61f),
                        size = Size(w * 0.22f, h * 0.06f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                }

                "home_foodgrains_oil" -> {
                    // Textured wheat atta sack (left)
                    drawCrispPack(w * 0.13f, h * 0.16f, w * 0.48f, h * 0.72f, Color(0xFFD4A36A), Color(0xFFB07D42), corner = 8f)
                    // White top pouch seal
                    drawRoundRect(
                        color = Color(0xFFF9FAFB),
                        topLeft = Offset(w * 0.11f, h * 0.11f),
                        size = Size(w * 0.52f, h * 0.09f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    // Red central Aashirvaad-style label
                    drawRoundRect(
                        color = Color(0xFFDC2626),
                        topLeft = Offset(w * 0.20f, h * 0.29f),
                        size = Size(w * 0.34f, h * 0.43f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    drawCircle(color = Color(0xFFFDE047), radius = w * 0.06f, center = Offset(w * 0.37f, h * 0.53f))
                    // Bright sunflower oil pouch (right)
                    drawCrispPack(w * 0.53f, h * 0.32f, w * 0.35f, h * 0.57f, Color(0xFFFDE047), Color(0xFFEAB308), corner = 7f)
                    drawCircle(color = Color(0xFFB45309), radius = w * 0.05f, center = Offset(w * 0.61f, h * 0.41f))
                    drawRoundRect(
                        color = Color(0xFFEA580C),
                        topLeft = Offset(w * 0.57f, h * 0.54f),
                        size = Size(w * 0.27f, h * 0.10f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    drawCircle(color = Color(0xFF16A34A), radius = w * 0.045f, center = Offset(w * 0.62f, h * 0.72f))
                }
            }
        }
    }
}


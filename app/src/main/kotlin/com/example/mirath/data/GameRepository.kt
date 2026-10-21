package com.example.mirath.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HistoricalGame(
    val id: String,
    val title: String,
    val englishTitle: String,
    val era: String,
    val region: String,
    val dateStr: String,
    val avgDuration: String,
    val description: String,
    val rulesSummary: String,
    val iconName: String,
    val imageUrl: String
)

data class ArticleItem(
    val id: String,
    val title: String,
    val author: String,
    val date: String,
    val summary: String,
    val category: String
)

object GameRepository {
    val games = listOf(
        HistoricalGame(
            id = "ur",
            title = "لعبة أور الملكية",
            englishTitle = "The Royal Game of Ur",
            era = "العصر البرونزي المبكر",
            region = "بلاد الرافدين (العراق الحالي)",
            dateStr = "2600 ق.م",
            avgDuration = "18 دقيقة",
            description = "أقدم لعبة لوحية استراتيجية كاملة القواعد في التاريخ الإنساني، اكتشفت في المقابر الملكية في أور بواسطة السير ليونارد وولي.",
            rulesSummary = "سباق على اللوح المكون من 20 مربعاً مع حجارة نرد هرمية وخانات روزيت آمنة تمنح لفة إضافية.",
            iconName = "grid_goldenratio",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Royal_Game_of_Ur_%28British_Museum%29.jpg/640px-Royal_Game_of_Ur_%28British_Museum%29.jpg"
        ),
        HistoricalGame(
            id = "senet",
            title = "سينيت المصرية",
            englishTitle = "Senet (Game of Passing)",
            era = "العصر البرونزي المبكر",
            region = "مصر القديمة",
            dateStr = "3100 ق.م",
            avgDuration = "15 دقيقة",
            description = "لعبة العبور والرحلة إلى العالم الآخر عند الفراعنة، وجدت مقابر توت عنخ آمون مرصعة بألواح السينيت.",
            rulesSummary = "تحريك القطع الـ 5 عبر 30 مربعاً وفق رميات عصي السينيت مع مربعات خاصة للحماية والعودة للوراء.",
            iconName = "view_column",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Senet_game_board_Louvre_N1387.jpg/640px-Senet_game_board_Louvre_N1387.jpg"
        ),
        HistoricalGame(
            id = "shatranj",
            title = "الشطرنج الصولي",
            englishTitle = "Abbasid Shatranj",
            era = "العصور الوسطى والإسلامية",
            region = "بغداد العباسية",
            dateStr = "850 م",
            avgDuration = "26 دقيقة",
            description = "نسخة العصر العباسي من لعبة الشطرنج القديمة، حيث ازدهرت المؤلفات وتحليلات المسائل والصليات.",
            rulesSummary = "حركة الفيل والفرس والرخ وفق القواعد العباسية الكلاسيكية قبل تحولها للشطرنج الحديث.",
            iconName = "shield",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Afrasiab_chess_pieces.jpg/640px-Afrasiab_chess_pieces.jpg"
        ),
        HistoricalGame(
            id = "hnefatafl",
            title = "هنيفاتافل الفايكنغ",
            englishTitle = "Hnefatafl (Viking Chess)",
            era = "العصور الوسطى",
            region = "الشمال الإسكندنافي",
            dateStr = "750 م",
            avgDuration = "22 دقيقة",
            description = "لعبة معركة الملك الإسكندنافية غير المتكافئة، حيث يحاول المهاجمون محاصرة الملك بينما يحميه حرسه.",
            rulesSummary = "المهاجمون يفوقون العدد أضعافاً، والملك يحاول الوصول إلى إحدى زوايا اللوح للأمان.",
            iconName = "crop_square",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Hnefatafl_board.svg/640px-Hnefatafl_board.svg.png"
        ),
        HistoricalGame(
            id = "seega",
            title = "لعبة السيجا",
            englishTitle = "Traditional Seega",
            era = "التراث الشعبي والتقليدي",
            region = "مصر وبلاد الشام",
            dateStr = "تراث حي",
            avgDuration = "11 دقيقة",
            description = "لعبة تكتيكية شعبية تعتمد على الإحاطة والأسر المتبادل على لوح 5×5 أو 7×7 محفور على الأرض أو الحجر.",
            rulesSummary = "وضع القطع ثم تحريكها وأسر قطع الخصم بالقفز فوقها أو محاصرتها أفقياً وعمودياً.",
            iconName = "apps",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Mancala_board.jpg/640px-Mancala_board.jpg"
        ),
        HistoricalGame(
            id = "hawalis",
            title = "الحواليس العُمانية",
            englishTitle = "Omani Hawalis",
            era = "التراث الشعبي والخليجي",
            region = "سلطنة عُمان والخليج العربي",
            dateStr = "مانكالا تقليدية",
            avgDuration = "14 دقيقة",
            description = "من أشهر ألعاب المانكالا ذات الأربعة صفوف في شبه الجزيرة العربية، تتطلب حسابات عميقة لتوزيع الحصى.",
            rulesSummary = "توزيع الحصى في الحفر الأربع والثلاثين مع قواعد الالتقاط المضاعف والتغذية المتبادلة.",
            iconName = "grain",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Mancala_board.jpg/640px-Mancala_board.jpg"
        )
    )

    // Real match counts initialized to 0, incremented only when real users play
    private val _matchCounts = MutableStateFlow(mapOf<String, Int>(
        "ur" to 0,
        "senet" to 0,
        "shatranj" to 0,
        "hnefatafl" to 0,
        "seega" to 0,
        "hawalis" to 0
    ))
    val matchCounts: StateFlow<Map<String, Int>> = _matchCounts.asStateFlow()

    fun recordMatchPlayed(gameId: String) {
        _matchCounts.update { current ->
            val currentCount = current[gameId] ?: 0
            current + (gameId to (currentCount + 1))
        }
    }

    val articles = listOf(
        ArticleItem(
            id = "art-1",
            title = "فك طلاسم الألواح المسمارية لأور: اكتشافات د. إرفينغ فينكل",
            author = "فريق التوثيق الأثري",
            date = "12 أبريل 2025",
            summary = "كيف ساعدت الألواح الطينية المحفوظة في المتحف البريطاني في إعادة إحياء قواعد اللعب المفقودة منذ 4 آلاف عام.",
            category = "تحقيق علمي"
        ),
        ArticleItem(
            id = "art-2",
            title = "ألعاب البادية والسيجا: الذاكرة الحية للمجتمعات العربية",
            author = "د. سالم الحارثي",
            date = "03 مارس 2025",
            summary = "توثيق ميداني لألعاب الرمل والحجارة التي مارسها البدو في شبه الجزيرة العربية كأداة تواصل وتفكير استراتيجي.",
            category = "تراث شعبي"
        ),
        ArticleItem(
            id = "art-3",
            title = "الرياضيات والاحتمالات في النرد الهرمي السومري",
            author = "قسم المحاكاة والتحليل",
            date = "20 يناير 2025",
            summary = "دراسة إحصائية مقارنة بين نتائج أكثر من 60,000 جولة محاكاة ونظرية الاحتمالات النردية القديمة.",
            category = "أبحاث كمية"
        )
    )
}

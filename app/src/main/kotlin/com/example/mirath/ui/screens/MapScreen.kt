package com.example.mirath.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class RegionItem(
    val name: String,
    val count: Int,
    val percentage: String,
    val highlights: String
)

@Composable
fun MapScreen() {
    val regions = listOf(
        RegionItem("الشرق الأدنى وبلاد الرافدين", 8, "33.3%", "أور الملكية، لوح تل باصم، النرد المسماري"),
        RegionItem("مصر القديمة وشمال إفريقيا", 6, "25.0%", "سينيت توت عنخ آمون، مهين، 20 خانة بسقارة"),
        RegionItem("آسيا والشرق الأقصى", 4, "16.7%", "وي تشي (جو)، الشطرنج الهندي (تشاتورانجا)"),
        RegionItem("أوروبا والشمال الإسكندنافي", 3, "12.5%", "هنيفاتافل غوكستاد، لودوس لاترانكولوروم"),
        RegionItem("الجزيرة العربية والخليج", 2, "8.3%", "الحواليس، لعبة الطاب في البادية"),
        RegionItem("أمريكا الوسطى والميزوأمريكا", 1, "4.2%", "باتولي (Patolli) الأزتيكية المنسية")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "الخريطة التفاعلية والانتشار الحضاري",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "التوزيع الجغرافي لأصول الألعاب التاريخية",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "رصد مراكز النشوء الحضاري الأولى للألواح والرقاع التراثية المسجلة في أرشيف المنصة وتوزعها عبر 6 أقاليم كبرى.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(regions) { region ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = region.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "${region.count} ألعاب (${region.percentage})",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "الألواح والمكتشفات البارزة: ${region.highlights}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

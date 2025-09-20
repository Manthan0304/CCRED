package com.example.ccred_3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ccred_3.data.ProjectStatus

@Composable
fun StatusChip(status: ProjectStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        ProjectStatus.APPROVED -> Triple(Color(0xFFE8F5E8), Color(0xFF2E7D32), "Approved")
        ProjectStatus.PENDING -> Triple(Color(0xFFFFF3E0), Color(0xFFFF9800), "Pending")
        ProjectStatus.REJECTED -> Triple(Color(0xFFFFEBEE), Color(0xFFF44336), "Rejected")
        ProjectStatus.UNDER_REVIEW -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), "Under Review")
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

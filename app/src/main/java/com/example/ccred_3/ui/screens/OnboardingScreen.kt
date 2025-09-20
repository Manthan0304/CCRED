package com.example.ccred_3.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "C_CRED",
            description = "Carbon Credit Registry for Ecosystem Development - Track and manage your environmental impact",
            icon = Icons.Default.Eco,
            color = Color(0xFF2E7D32)
        ),
        OnboardingPage(
            title = "Submit Projects",
            description = "Upload geotagged photos and project details to get your carbon savings verified and earn credits",
            icon = Icons.Default.Upload,
            color = Color(0xFF1976D2)
        ),
        OnboardingPage(
            title = "Ecosystem Impact",
            description = "Monitor ecosystem development through detailed analytics and carbon credit tracking",
            icon = Icons.Default.Analytics,
            color = Color(0xFF7B1FA2)
        )
    )

    var currentPage by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        while (true) {
            delay(3000)
            currentPage = (currentPage + 1) % pages.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        pages[currentPage].color.copy(alpha = 0.1f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Animated Icon
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(animationSpec = tween(1000)) + fadeIn(animationSpec = tween(1000))
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = pages[currentPage].color.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(60.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = pages[currentPage].icon,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = pages[currentPage].color
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Animated Title
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(500))
                },
                label = "title"
            ) { page ->
                Text(
                    text = pages[page].title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = pages[page].color,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Description
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(500))
                },
                label = "description"
            ) { page ->
                Text(
                    text = pages[page].description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Page Indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                pages.forEachIndexed { index, _ ->
                    val isSelected = index == currentPage
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.2f else 1f,
                        animationSpec = tween(300),
                        label = "indicator_scale"
                    )
                    val color by animateColorAsState(
                        targetValue = if (isSelected) pages[currentPage].color else Color.Gray,
                        animationSpec = tween(300),
                        label = "indicator_color"
                    )

                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 12.dp else 8.dp)
                            .scale(scale)
                            .background(
                                color = color,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            // Get Started Button
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = tween(1000)
                ) + fadeIn(animationSpec = tween(1000))
            ) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = pages[currentPage].color
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

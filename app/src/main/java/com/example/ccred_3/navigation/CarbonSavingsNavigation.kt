package com.example.ccred_3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ccred_3.data.CarbonSavingsData
import com.example.ccred_3.ui.screens.CarbonSavingsListScreen
import com.example.ccred_3.ui.screens.MinistryDetailScreen

@Composable
fun CarbonSavingsNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "carbon_savings_list"
    ) {
        composable("carbon_savings_list") {
            CarbonSavingsListScreen(
                onMinistryClick = { ministry ->
                    navController.navigate("ministry_detail/${ministry.ministry}")
                }
            )
        }
        
        composable("ministry_detail/{ministryName}") { backStackEntry ->
            val ministryName = backStackEntry.arguments?.getString("ministryName") ?: ""
            // For now, we'll find the ministry by name. In a real app, you'd pass the ID
            val ministry = getMinistryByName(ministryName)
            if (ministry != null) {
                MinistryDetailScreen(
                    ministry = ministry,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

// Helper function to find ministry by name
// In a real app, this would be handled by the ViewModel or repository
private fun getMinistryByName(name: String): CarbonSavingsData? {
    return com.example.ccred_3.data.DummyCarbonData.ministriesData.find { it.ministry == name }
}

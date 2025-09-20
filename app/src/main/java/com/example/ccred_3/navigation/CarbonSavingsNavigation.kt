package com.example.ccred_3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ccred_3.ui.screens.MainScreen
import com.example.ccred_3.viewmodel.CarbonCreditsViewModel

@Composable
fun CarbonSavingsNavigation(
    navController: NavHostController = rememberNavController()
) {
    val carbonCreditsViewModel: CarbonCreditsViewModel = viewModel()
    
    MainScreen(
        navController = navController,
        viewModel = carbonCreditsViewModel
    )
}

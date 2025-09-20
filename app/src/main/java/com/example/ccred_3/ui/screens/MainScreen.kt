package com.example.ccred_3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ccred_3.data.CarbonProject
import com.example.ccred_3.data.CarbonSavingsData
import com.example.ccred_3.ui.components.BottomNavigationBar
import com.example.ccred_3.viewmodel.CarbonCreditsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: CarbonCreditsViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    when (route) {
                        "dashboard" -> navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = false }
                        }
                        "ministries" -> navController.navigate("ministries") {
                            popUpTo("ministries") { inclusive = false }
                        }
                        "add_project" -> navController.navigate("add_project")
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    onAddProjectClick = {
                        navController.navigate("add_project")
                    },
                    onProjectClick = { project ->
                        navController.navigate("project_details/${project.id}")
                    },
                    viewModel = viewModel
                )
            }
            
            composable("ministries") {
                CarbonSavingsListScreen(
                    onMinistryClick = { ministry ->
                        navController.navigate("ministry_detail/${ministry.ministry}")
                    },
                    viewModel = com.example.ccred_3.viewmodel.CarbonSavingsViewModel()
                )
            }
            
            composable("add_project") {
                AddProjectScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onProjectSubmitted = {
                        navController.popBackStack()
                    },
                    viewModel = viewModel
                )
            }
            
            composable("project_details/{projectId}") { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
                val project = getProjectById(projectId)
                if (project != null) {
                    ProjectDetailsScreen(
                        project = project,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
            
            composable("ministry_detail/{ministryName}") { backStackEntry ->
                val ministryName = backStackEntry.arguments?.getString("ministryName") ?: ""
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
}

// Helper functions
private fun getProjectById(projectId: String): CarbonProject? {
    return com.example.ccred_3.data.DummyCarbonData.userDashboard.recentProjects.find { it.id == projectId }
}

private fun getMinistryByName(name: String): CarbonSavingsData? {
    return com.example.ccred_3.data.DummyCarbonData.ministriesData.find { it.ministry == name }
}

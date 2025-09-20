package com.example.ccred_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ccred_3.data.CarbonProject
import com.example.ccred_3.data.ProjectStatus
import com.example.ccred_3.data.ProjectSubmission
import com.example.ccred_3.data.UserDashboard
import com.example.ccred_3.data.DummyCarbonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CarbonCreditsViewModel : ViewModel() {
    private val _userDashboard = MutableStateFlow(DummyCarbonData.userDashboard)
    val userDashboard: StateFlow<UserDashboard> = _userDashboard.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _submissionResult = MutableStateFlow<String?>(null)
    val submissionResult: StateFlow<String?> = _submissionResult.asStateFlow()

    fun submitProject(projectSubmission: ProjectSubmission) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _submissionResult.value = null

            try {
                // Simulate API call delay
                kotlinx.coroutines.delay(2000)

                // Create new project with pending status
                val newProject = CarbonProject(
                    id = UUID.randomUUID().toString(),
                    projectName = projectSubmission.projectName,
                    description = projectSubmission.description,
                    carbonSaved = projectSubmission.carbonSaved,
                    acresOfLand = projectSubmission.acresOfLand,
                    location = projectSubmission.location,
                    latitude = projectSubmission.latitude,
                    longitude = projectSubmission.longitude,
                    imageUri = projectSubmission.imageUri,
                    submissionDate = java.time.LocalDate.now().toString(),
                    status = ProjectStatus.PENDING,
                    creditsAwarded = 0,
                    reviewNotes = null
                )

                // Update dashboard with new project
                val currentDashboard = _userDashboard.value
                val updatedProjects = listOf(newProject) + currentDashboard.recentProjects
                val updatedDashboard = currentDashboard.copy(
                    recentProjects = updatedProjects,
                    pendingCredits = currentDashboard.pendingCredits + (projectSubmission.carbonSaved * 10).toInt()
                )

                _userDashboard.value = updatedDashboard
                _submissionResult.value = "Project submitted successfully! Your project is now under review."
            } catch (e: Exception) {
                _submissionResult.value = "Failed to submit project. Please try again."
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun clearSubmissionResult() {
        _submissionResult.value = null
    }

    fun getProjectById(projectId: String): CarbonProject? {
        return _userDashboard.value.recentProjects.find { it.id == projectId }
    }
}

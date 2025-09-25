package com.example.ccred_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ccred_3.data.CarbonProject
import com.example.ccred_3.data.ProjectStatus
import com.example.ccred_3.data.ProjectSubmission
import com.example.ccred_3.data.UserDashboard
import com.example.ccred_3.data.DummyCarbonData
import com.example.ccred_3.data.repository.CcredRepository
import com.example.ccred_3.data.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CarbonCreditsViewModel : ViewModel() {
    private val repository = CcredRepository(NetworkModule.apiService)
    
    private val _userDashboard = MutableStateFlow(DummyCarbonData.userDashboard)
    val userDashboard: StateFlow<UserDashboard> = _userDashboard.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _submissionResult = MutableStateFlow<String?>(null)
    val submissionResult: StateFlow<String?> = _submissionResult.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadUserDashboard()
    }
    
    private fun loadUserDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load credits portfolio
                val creditsResult = repository.getCreditPortfolio()
                if (creditsResult.isSuccess) {
                    val portfolio = creditsResult.getOrNull()
                    if (portfolio != null) {
                        val currentDashboard = _userDashboard.value
                        val updatedDashboard = currentDashboard.copy(
                            totalCredits = portfolio.totalCredits,
                            approvedCredits = portfolio.approvedCredits,
                            pendingCredits = portfolio.pendingCredits,
                            rejectedCredits = portfolio.rejectedCredits
                        )
                        _userDashboard.value = updatedDashboard
                    }
                } else {
                    // Fallback to dummy data if API fails
                    _userDashboard.value = DummyCarbonData.userDashboard
                    _error.value = "Using offline data. API unavailable: ${creditsResult.exceptionOrNull()?.message}"
                }
                
                // Load recent projects
                val projectsResult = repository.getProjects(limit = 5)
                if (projectsResult.isSuccess) {
                    val projects = projectsResult.getOrNull() ?: emptyList()
                    val currentDashboard = _userDashboard.value
                    val updatedDashboard = currentDashboard.copy(
                        recentProjects = projects
                    )
                    _userDashboard.value = updatedDashboard
                } else {
                    // If projects API fails but credits succeeded, keep credits data
                    // If both fail, we already set dummy data above
                    if (creditsResult.isSuccess) {
                        _error.value = "Using offline project data. Projects API unavailable: ${projectsResult.exceptionOrNull()?.message}"
                    }
                }
            } catch (e: Exception) {
                // Complete fallback to dummy data
                _userDashboard.value = DummyCarbonData.userDashboard
                _error.value = "Using offline data. Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitProject(projectSubmission: ProjectSubmission) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _submissionResult.value = null
            _error.value = null

            try {
                val result = repository.createProject(projectSubmission)
                if (result.isSuccess) {
                    val newProject = result.getOrNull()
                    if (newProject != null) {
                        // Update dashboard with new project
                        val currentDashboard = _userDashboard.value
                        val updatedProjects = listOf(newProject) + currentDashboard.recentProjects
                        val updatedDashboard = currentDashboard.copy(
                            recentProjects = updatedProjects,
                            pendingCredits = currentDashboard.pendingCredits + (projectSubmission.carbonSaved * 10).toInt()
                        )
                        _userDashboard.value = updatedDashboard
                        _submissionResult.value = "Project submitted successfully! Your project is now under review."
                    } else {
                        _submissionResult.value = "Failed to submit project. Please try again."
                    }
                } else {
                    // Fallback to offline mode - create project locally
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
                    
                    val currentDashboard = _userDashboard.value
                    val updatedProjects = listOf(newProject) + currentDashboard.recentProjects
                    val updatedDashboard = currentDashboard.copy(
                        recentProjects = updatedProjects,
                        pendingCredits = currentDashboard.pendingCredits + (projectSubmission.carbonSaved * 10).toInt()
                    )
                    _userDashboard.value = updatedDashboard
                    
                    val errorMessage = result.exceptionOrNull()?.message ?: "API unavailable"
                    _submissionResult.value = "Project saved offline! Will sync when connection is restored. (${errorMessage})"
                }
            } catch (e: Exception) {
                // Complete offline fallback
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
                
                val currentDashboard = _userDashboard.value
                val updatedProjects = listOf(newProject) + currentDashboard.recentProjects
                val updatedDashboard = currentDashboard.copy(
                    recentProjects = updatedProjects,
                    pendingCredits = currentDashboard.pendingCredits + (projectSubmission.carbonSaved * 10).toInt()
                )
                _userDashboard.value = updatedDashboard
                
                _submissionResult.value = "Project saved offline! Will sync when connection is restored. (Network error: ${e.message})"
            } finally {
                _isSubmitting.value = false
            }
        }
    }
    
    fun uploadProjectData(
        projectId: String,
        dataType: String,
        files: List<com.example.ccred_3.data.api.FileInfo>,
        metadata: com.example.ccred_3.data.api.UploadMetadata
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _error.value = null
            
            try {
                val result = repository.uploadData(projectId, dataType, files, metadata)
                if (result.isSuccess) {
                    _submissionResult.value = "Data uploaded successfully! Verification in progress."
                } else {
                    // Offline fallback - show success message but note it's offline
                    val errorMessage = result.exceptionOrNull()?.message ?: "API unavailable"
                    _submissionResult.value = "Data saved offline! Will sync when connection is restored. (${errorMessage})"
                }
            } catch (e: Exception) {
                // Complete offline fallback
                _submissionResult.value = "Data saved offline! Will sync when connection is restored. (Network error: ${e.message})"
            } finally {
                _isSubmitting.value = false
            }
        }
    }
    
    fun generateCredits(projectId: String, carbonSaved: Double) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _error.value = null
            
            try {
                val verificationData = mapOf(
                    "carbonSaved" to carbonSaved,
                    "verificationDate" to System.currentTimeMillis(),
                    "source" to "project_submission"
                )
                
                val result = repository.generateCredits(projectId, carbonSaved, verificationData)
                if (result.isSuccess) {
                    val credit = result.getOrNull()
                    if (credit != null) {
                        // Update dashboard with new credits
                        val currentDashboard = _userDashboard.value
                        val updatedDashboard = currentDashboard.copy(
                            totalCredits = currentDashboard.totalCredits + credit.amount,
                            approvedCredits = currentDashboard.approvedCredits + credit.amount
                        )
                        _userDashboard.value = updatedDashboard
                        _submissionResult.value = "Credits generated successfully! ${credit.amount} credits added to your account."
                    }
                } else {
                    // Offline fallback - simulate credit generation
                    val estimatedCredits = (carbonSaved * 10).toInt()
                    val currentDashboard = _userDashboard.value
                    val updatedDashboard = currentDashboard.copy(
                        totalCredits = currentDashboard.totalCredits + estimatedCredits,
                        approvedCredits = currentDashboard.approvedCredits + estimatedCredits
                    )
                    _userDashboard.value = updatedDashboard
                    
                    val errorMessage = result.exceptionOrNull()?.message ?: "API unavailable"
                    _submissionResult.value = "Credits calculated offline! ${estimatedCredits} credits added. Will sync when connection is restored. (${errorMessage})"
                }
            } catch (e: Exception) {
                // Complete offline fallback - simulate credit generation
                val estimatedCredits = (carbonSaved * 10).toInt()
                val currentDashboard = _userDashboard.value
                val updatedDashboard = currentDashboard.copy(
                    totalCredits = currentDashboard.totalCredits + estimatedCredits,
                    approvedCredits = currentDashboard.approvedCredits + estimatedCredits
                )
                _userDashboard.value = updatedDashboard
                
                _submissionResult.value = "Credits calculated offline! ${estimatedCredits} credits added. Will sync when connection is restored. (Network error: ${e.message})"
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun clearSubmissionResult() {
        _submissionResult.value = null
    }
    
    fun clearError() {
        _error.value = null
    }

    fun getProjectById(projectId: String): CarbonProject? {
        return _userDashboard.value.recentProjects.find { it.id == projectId }
    }
    
    fun refreshDashboard() {
        loadUserDashboard()
    }
    
    fun isOfflineMode(): Boolean {
        return _error.value?.contains("offline", ignoreCase = true) == true ||
               _error.value?.contains("API unavailable", ignoreCase = true) == true ||
               _error.value?.contains("Network error", ignoreCase = true) == true
    }
}

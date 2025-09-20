package com.example.ccred_3.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ccred_3.data.ProjectSubmission
import com.example.ccred_3.viewmodel.CarbonCreditsViewModel
import com.example.ccred_3.ui.components.SuccessDialog
import com.example.ccred_3.utils.LocationManager
import com.example.ccred_3.utils.rememberImagePicker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddProjectScreen(
    onBackClick: () -> Unit,
    onProjectSubmitted: () -> Unit,
    viewModel: CarbonCreditsViewModel = viewModel()
) {
    val context = LocalContext.current
    var projectName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var carbonSaved by remember { mutableStateOf("") }
    var acresOfLand by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    var isLocationLoading by remember { mutableStateOf(false) }

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val submissionResult by viewModel.submissionResult.collectAsState()

    // Location permission
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Image picker
    val imagePicker = rememberImagePicker(context) { uri ->
        imageUri = uri
    }

    // Location manager
    val locationManager = remember { LocationManager(context) }

    // Function to get current location
    fun getCurrentLocation() {
        if (locationPermissionState.status.isGranted) {
            isLocationLoading = true
            // Simulate location fetching - in real implementation, this would be in a coroutine
            // For demo purposes, we'll set some dummy coordinates
            latitude = "28.6139"
            longitude = "77.2090"
            location = "New Delhi, India"
            isLocationLoading = false
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    // Show success dialog when project is submitted
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(submissionResult) {
        if (submissionResult != null && !isSubmitting) {
            showSuccessDialog = true
        }
    }
    
    if (showSuccessDialog) {
        SuccessDialog(
            title = "Project Submitted!",
            message = submissionResult ?: "Your project has been submitted successfully and is now under review.",
            onDismiss = {
                showSuccessDialog = false
                viewModel.clearSubmissionResult()
                onProjectSubmitted()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top App Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Submit New Project",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Project Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Project Information",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = projectName,
                        onValueChange = { projectName = it },
                        label = { Text("Project Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Project Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = carbonSaved,
                            onValueChange = { carbonSaved = it },
                            label = { Text("Carbon Saved (tons)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )

                        OutlinedTextField(
                            value = acresOfLand,
                            onValueChange = { acresOfLand = it },
                            label = { Text("Acres of Land") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }
            }

            // Location Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Location Information",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Button(
                            onClick = { getCurrentLocation() },
                            enabled = !isLocationLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            modifier = Modifier.height(40.dp)
                        ) {
                            if (isLocationLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Get Current Location",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Auto", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location Name") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location"
                            )
                        },
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it },
                            label = { Text("Latitude") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )

                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it },
                            label = { Text("Longitude") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }

                    if (locationPermissionState.status.shouldShowRationale) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Location permission is needed to automatically detect your project location. Please grant permission to use this feature.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Text(
                        text = "💡 Tip: Use 'Auto' button to get your current location, or manually enter coordinates from Google Maps",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Photo Upload Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Project Photo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Photo selection buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { imagePicker.pickImage() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = "Gallery",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Gallery", style = MaterialTheme.typography.bodySmall)
                        }
                        
                        Button(
                            onClick = { imagePicker.takePhoto() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Camera", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    // Photo preview
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (imageUri != null) Color(0xFFE8F5E8) else Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (imageUri != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Photo Added",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color(0xFF2E7D32)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Photo Added ✓",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "Tap buttons above to change",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Add Photo",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No photo selected",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Use buttons above to select",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    if (!imagePicker.hasPermission) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Storage permission needed to access photos. ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE65100),
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    onClick = { imagePicker.requestPermission() }
                                ) {
                                    Text("Grant", color = Color(0xFFE65100))
                                }
                            }
                        }
                    }

                    Text(
                        text = "📸 Take a geotagged photo of your project site for verification",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Submit Button
            Button(
                onClick = {
                    if (validateForm(projectName, description, carbonSaved, acresOfLand, location, latitude, longitude)) {
                        val submission = ProjectSubmission(
                            projectName = projectName,
                            description = description,
                            carbonSaved = carbonSaved.toDouble(),
                            acresOfLand = acresOfLand.toDouble(),
                            location = location,
                            latitude = latitude.toDouble(),
                            longitude = longitude.toDouble(),
                            imageUri = imageUri
                        )
                        viewModel.submitProject(submission)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isSubmitting && validateForm(projectName, description, carbonSaved, acresOfLand, location, latitude, longitude),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text(
                        text = "Submit Project",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Validation errors
            if (!validateForm(projectName, description, carbonSaved, acresOfLand, location, latitude, longitude)) {
                Text(
                    text = "Please fill in all required fields with valid data",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }
    }
}

private fun validateForm(
    projectName: String,
    description: String,
    carbonSaved: String,
    acresOfLand: String,
    location: String,
    latitude: String,
    longitude: String
): Boolean {
    return projectName.isNotBlank() &&
            description.isNotBlank() &&
            carbonSaved.isNotBlank() && carbonSaved.toDoubleOrNull() != null && carbonSaved.toDouble() > 0 &&
            acresOfLand.isNotBlank() && acresOfLand.toDoubleOrNull() != null && acresOfLand.toDouble() > 0 &&
            location.isNotBlank() &&
            latitude.isNotBlank() && latitude.toDoubleOrNull() != null &&
            longitude.isNotBlank() && longitude.toDoubleOrNull() != null
}

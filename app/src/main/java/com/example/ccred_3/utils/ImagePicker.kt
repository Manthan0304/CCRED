package com.example.ccred_3.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun rememberImagePicker(
    context: Context,
    onImageSelected: (String) -> Unit
): ImagePickerState {
    var hasPermission by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Check permissions
    LaunchedEffect(Unit) {
        hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it.toString()
            onImageSelected(it.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri?.let { onImageSelected(it) }
        }
    }

    return ImagePickerState(
        hasPermission = hasPermission,
        imageUri = imageUri,
        requestPermission = { permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) },
        pickImage = { imagePickerLauncher.launch("image/*") },
        takePhoto = { 
            val photoFile = File.createTempFile(
                "photo_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}",
                ".jpg",
                context.cacheDir
            )
            val photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            imageUri = photoUri.toString()
            cameraLauncher.launch(photoUri)
        }
    )
}

data class ImagePickerState(
    val hasPermission: Boolean,
    val imageUri: String?,
    val requestPermission: () -> Unit,
    val pickImage: () -> Unit,
    val takePhoto: () -> Unit
)

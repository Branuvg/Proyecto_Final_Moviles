package android.azadevs.pokedex.screen.camara

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

//hola

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*


@Composable
fun CameraScreen() {
    // Estado para almacenar la imagen capturada
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    // Lanzador para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Actualizar la imagen capturada
        capturedImage = bitmap
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar la imagen capturada o un mensaje
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .border(2.dp, Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            capturedImage?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier.fillMaxSize()
                )
            } ?: Text(
                text = "¡Captura una imagen!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para abrir la cámara
        Button(
            onClick = { cameraLauncher.launch(null) },
            modifier = Modifier
                .size(64.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Camera,
                contentDescription = "Abrir cámara"
            )
        }
    }
}


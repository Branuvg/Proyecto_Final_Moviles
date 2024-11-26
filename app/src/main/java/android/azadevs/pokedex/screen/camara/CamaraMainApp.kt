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

@Composable
fun CamaraMainApp() {
    // Variable para almacenar la imagen capturada
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    // Configurar el lanzador para iniciar la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Guardar la imagen capturada
        capturedImage = bitmap
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para abrir la cámara
        IconButton(
            onClick = {
                cameraLauncher.launch(null) // Se usa null porque no necesitas un URI para TakePicturePreview
            },
            modifier = Modifier
                .size(64.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Camera,
                contentDescription = "Abrir cámara",
                //tint = MaterialTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar la imagen capturada o un texto predeterminado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(2.dp, Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (capturedImage != null) {
                Image(
                    bitmap = capturedImage!!.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "Pokémon", modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


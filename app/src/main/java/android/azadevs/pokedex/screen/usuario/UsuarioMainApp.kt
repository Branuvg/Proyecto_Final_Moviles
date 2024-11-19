package android.azadevs.pokedex.screen.usuario

import android.azadevs.pokedex.Contorno
import android.azadevs.pokedex.R
import android.azadevs.pokedex.ui.theme.PokédexTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UsuarioMainApp() {
    Contorno()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileDes()
        Menu()
    }
}


@Composable
fun ProfileDes(){
    val avatar = painterResource(id = R.drawable.perfil_pic)

    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {

        Box (
            Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        {
            Image(painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
                    .absoluteOffset(0.dp, 40.dp))
        }

        Text(text = "Nombre",
            style = TextStyle(
                fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(50.dp)
        )
    }
}

@Composable
fun Menu(){
    val not = painterResource(id = R.drawable.notificaciones)
    val emer = painterResource(id = R.drawable.emergencia)
    val yo = painterResource(id = R.drawable.perfil)
    val settings = painterResource(id = R.drawable.ajustes)


    Column (
        Modifier.fillMaxWidth()
    )
    {
        Row {
            Image(
                painter = yo,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
            )

            Text(
                text = "Editar Perfil",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(20.dp)
            )
        }


        Row {
            Image(
                painter = emer,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
            )

            Text(
                text = "Reset Password",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(20.dp)
            )
        }

        Row {
            Image(
                painter = settings,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
            )

            Text(
                text = "Ajustes",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(20.dp)
            )
        }


        Row {
            Image(
                painter = not,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
            )

            Text(
                text = "Notificaciones",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioPreview() {
    PokédexTheme {
        UsuarioMainApp()
    }
}
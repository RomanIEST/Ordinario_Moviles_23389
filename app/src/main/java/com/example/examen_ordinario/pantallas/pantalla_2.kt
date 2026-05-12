package com.example.examen_ordinario.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.examen_ordinario.R
import com.example.examen_ordinario.api.JokeResponse
import com.example.examen_ordinario.api.RetrofitClient
import com.example.examen_ordinario.data.JokeDataStore
import kotlinx.coroutines.launch

data class PeliculaSerie(val nombre: String, val imagenRes: Int)

private val listaPeliculas = listOf(
    PeliculaSerie("McQuade, el lobo solitario", R.drawable.mcquade),
    PeliculaSerie("Prisioneros de guerra", R.drawable.prisionero),
    PeliculaSerie("Walker, Texas Ranger", R.drawable.texas_ranger),
    PeliculaSerie("Logan's War: Bound by Honor", R.drawable.logan)
)

@Composable
fun Pantalla_2(navController: NavController) {
    var joke by remember { mutableStateOf<JokeResponse?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    suspend fun cargarChiste() {
        try {
            cargando = true
            error = null
            joke = RetrofitClient.service.getRandomJoke()
        } catch (e: Exception) {
            error = e.message
        } finally {
            cargando = false
        }
    }

    LaunchedEffect(Unit) { cargarChiste() }

    Pantalla2Content(
        peliculas = listaPeliculas,
        joke = joke,
        error = error,
        cargando = cargando,
        onRegresar = { navController.popBackStack() },
        onGuardar = {
            val frase = joke?.value
            if (!frase.isNullOrBlank()) {
                scope.launch {
                    JokeDataStore.guardarFrase(context, frase)
                    Toast.makeText(context, "Chiste guardado", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onRefrescar = { scope.launch { cargarChiste() } }
    )
}

@Composable
fun Pantalla2Content(
    peliculas: List<PeliculaSerie>,
    joke: JokeResponse?,
    error: String?,
    cargando: Boolean,
    onRegresar: () -> Unit,
    onGuardar: () -> Unit,
    onRefrescar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Series y Películas Famosas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(peliculas) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.width(140.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = item.imagenRes),
                            contentDescription = item.nombre,
                            modifier = Modifier
                                .width(140.dp)
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = item.nombre,
                            minLines = 2,
                            maxLines = 3,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chiste aleatorio",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                when {
                    cargando -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null -> {
                        Text(
                            text = "Error: $error",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    joke != null -> {
                        AsyncImage(
                            model = joke.icon_url,
                            contentDescription = "icono chuck",
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = joke.value,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ID: ${joke.id}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onRegresar) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Regresar",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onGuardar) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Guardar",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onRefrescar) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refrescar",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@Composable
fun Pantalla2Preview() {
    Pantalla2Content(
        peliculas = listaPeliculas,
        joke = JokeResponse(
            id = "abc123",
            value = "Chuck Norris no necesita previews, los previews lo necesitan a él.",
            icon_url = ""
        ),
        error = null,
        cargando = false,
        onRegresar = {},
        onGuardar = {},
        onRefrescar = {}
    )
}

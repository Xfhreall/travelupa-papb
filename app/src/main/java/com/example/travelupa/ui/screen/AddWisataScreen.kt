package com.example.travelupa.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.travelupa.data.model.JenisTempat
import com.example.travelupa.data.model.Kategori
import com.example.travelupa.data.model.Provinsi
import com.example.travelupa.data.model.TempatWisata
import com.example.travelupa.data.repository.WisataRepository
import com.example.travelupa.ui.theme.GradientTealEnd
import com.example.travelupa.ui.theme.GradientTealStart
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWisataScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { WisataRepository() }
    
    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    var selectedKategori by remember { mutableStateOf<Kategori?>(null) }
    var selectedJenisTempat by remember { mutableStateOf<JenisTempat?>(null) }
    var selectedProvinsi by remember { mutableStateOf<Provinsi?>(null) }
    
    var kategoriExpanded by remember { mutableStateOf(false) }
    var jenisTempatExpanded by remember { mutableStateOf(false) }
    var provinsiExpanded by remember { mutableStateOf(false) }
    
    var useUrlInput by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
        if (uri != null) {
            imageUrl = ""
        }
    }
    
    val scrollState = rememberScrollState()
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(GradientTealStart, GradientTealEnd)
    )

    val isFormValid = nama.isNotBlank() && 
                      deskripsi.isNotBlank() && 
                      selectedKategori != null && 
                      selectedJenisTempat != null && 
                      selectedProvinsi != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Tambah Wisata Baru",
                        fontWeight = FontWeight.SemiBold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Gambar Thumbnail",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !useUrlInput,
                            onClick = { useUrlInput = false },
                            label = { Text("Upload File") },
                            leadingIcon = {
                                Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        )
                        FilterChip(
                            selected = useUrlInput,
                            onClick = { useUrlInput = true },
                            label = { Text("URL Gambar") },
                            leadingIcon = {
                                Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        )
                    }
                    
                    if (useUrlInput) {
                        OutlinedTextField(
                            value = imageUrl,
                            onValueChange = { 
                                imageUrl = it
                                selectedImageUri = null
                            },
                            label = { Text("URL Gambar") },
                            placeholder = { Text("https://example.com/image.jpg") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        ImageRequest.Builder(context)
                                            .data(selectedImageUri)
                                            .crossfade(true)
                                            .build()
                                    ),
                                    contentDescription = "Selected image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Tap untuk pilih gambar",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    if (useUrlInput && imageUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context)
                                        .data(imageUrl)
                                        .crossfade(true)
                                        .build()
                                ),
                                contentDescription = "Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Informasi Wisata",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama Tempat *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = deskripsi,
                        onValueChange = { deskripsi = it },
                        label = { Text("Deskripsi *") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 5
                    )
                    
                    OutlinedTextField(
                        value = harga,
                        onValueChange = { harga = it.filter { c -> c.isDigit() } },
                        label = { Text("Harga Tiket (Rp)") },
                        placeholder = { Text("0 untuk gratis") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        prefix = { Text("Rp ") }
                    )
                    
                    Text(
                        "Koordinat Lokasi",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it.filter { c -> c.isDigit() || c == '.' || c == '-' } },
                            label = { Text("Latitude") },
                            placeholder = { Text("-7.250") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it.filter { c -> c.isDigit() || c == '.' || c == '-' } },
                            label = { Text("Longitude") },
                            placeholder = { Text("112.750") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )
                    }
                    
                    ExposedDropdownMenuBox(
                        expanded = kategoriExpanded,
                        onExpandedChange = { kategoriExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedKategori?.nama ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Kategori *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kategoriExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = kategoriExpanded,
                            onDismissRequest = { kategoriExpanded = false }
                        ) {
                            repository.getKategoriList().forEach { kategori ->
                                DropdownMenuItem(
                                    text = { Text(kategori.nama) },
                                    onClick = {
                                        selectedKategori = kategori
                                        kategoriExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    ExposedDropdownMenuBox(
                        expanded = jenisTempatExpanded,
                        onExpandedChange = { jenisTempatExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedJenisTempat?.nama ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jenis Tempat *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = jenisTempatExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = jenisTempatExpanded,
                            onDismissRequest = { jenisTempatExpanded = false }
                        ) {
                            repository.getJenisTempatList().forEach { jenis ->
                                DropdownMenuItem(
                                    text = { Text(jenis.nama) },
                                    onClick = {
                                        selectedJenisTempat = jenis
                                        jenisTempatExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    ExposedDropdownMenuBox(
                        expanded = provinsiExpanded,
                        onExpandedChange = { provinsiExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedProvinsi?.nama ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Provinsi *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = provinsiExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = provinsiExpanded,
                            onDismissRequest = { provinsiExpanded = false }
                        ) {
                            repository.getProvinsiList().forEach { provinsi ->
                                DropdownMenuItem(
                                    text = { Text(provinsi.nama) },
                                    onClick = {
                                        selectedProvinsi = provinsi
                                        provinsiExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        
                        try {
                            val finalImageUrl = if (selectedImageUri != null) {
                                val storage = FirebaseStorage.getInstance()
                                val imageRef = storage.reference.child("wisata/${UUID.randomUUID()}.jpg")
                                imageRef.putFile(selectedImageUri!!).await()
                                imageRef.downloadUrl.await().toString()
                            } else if (imageUrl.isNotBlank()) {
                                imageUrl
                            } else {
                                null
                            }
                            
                            val wisata = TempatWisata(
                                nama = nama,
                                deskripsi = deskripsi,
                                gambarUrl = finalImageUrl,
                                kategoriId = selectedKategori!!.id,
                                jenisTempatId = selectedJenisTempat!!.id,
                                provinsiId = selectedProvinsi!!.id,
                                harga = harga.toLongOrNull() ?: 0,
                                latitude = latitude.toDoubleOrNull() ?: 0.0,
                                longitude = longitude.toDoubleOrNull() ?: 0.0
                            )
                            
                            val result = repository.addWisata(wisata)
                            if (result.isSuccess) {
                                onSuccess()
                            } else {
                                errorMessage = "Gagal menyimpan data: ${result.exceptionOrNull()?.message}"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid && !isLoading,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Menyimpan...")
                } else {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simpan Wisata", fontWeight = FontWeight.SemiBold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

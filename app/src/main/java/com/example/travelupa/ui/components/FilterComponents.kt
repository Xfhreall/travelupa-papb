package com.example.travelupa.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.travelupa.data.model.*
import com.example.travelupa.data.repository.WisataRepository

@Composable
fun ActiveFilterRow(
    filterState: FilterState,
    onAddFilterClick: () -> Unit,
    onRemoveFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = false,
            onClick = onAddFilterClick,
            label = { 
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Filter",
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Filter")
                }
            },
            leadingIcon = {
                if (filterState.hasActiveFilters) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Text(filterState.filterCount.toString())
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        )
        
        filterState.conditions.forEach { condition ->
            InputChip(
                selected = true,
                onClick = {},
                label = { 
                    Text(
                        text = condition.toDisplayString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) 
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .clickable { onRemoveFilter(condition.id) }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    isVisible: Boolean,
    repository: WisataRepository,
    onDismiss: () -> Unit,
    onApplyFilter: (FilterCondition) -> Unit
) {
    var selectedField by remember { mutableStateOf(FilterField.KATEGORI) }
    var selectedOperator by remember { mutableStateOf(FilterOperator.CONTAINS) }
    var selectedValue by remember { mutableStateOf("") }
    var selectedDisplayValue by remember { mutableStateOf("") }
    
    LaunchedEffect(selectedField) {
        selectedOperator = FilterOperator.getOperatorsForField(selectedField).first()
        selectedValue = ""
        selectedDisplayValue = ""
    }
    
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Tambah Filter",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Pilih Field",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                FieldSelector(
                    selectedField = selectedField,
                    onFieldSelected = { selectedField = it }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val operators = FilterOperator.getOperatorsForField(selectedField)
                if (operators.size > 1) {
                    Text(
                        text = "Operator",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OperatorSelector(
                        operators = operators,
                        selectedOperator = selectedOperator,
                        onOperatorSelected = { selectedOperator = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                Text(
                    text = "Nilai",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                ValueInput(
                    field = selectedField,
                    repository = repository,
                    value = selectedValue,
                    onValueChange = { value, displayValue ->
                        selectedValue = value
                        selectedDisplayValue = displayValue
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            if (selectedValue.isNotEmpty()) {
                                onApplyFilter(
                                    FilterCondition(
                                        field = selectedField,
                                        operator = selectedOperator,
                                        value = selectedValue,
                                        displayValue = selectedDisplayValue.ifEmpty { selectedValue }
                                    )
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedValue.isNotEmpty()
                    ) {
                        Text("Terapkan")
                    }
                }
            }
        }
    }
}

@Composable
private fun FieldSelector(
    selectedField: FilterField,
    onFieldSelected: (FilterField) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterField.entries.forEach { field ->
            FilterChip(
                selected = field == selectedField,
                onClick = { onFieldSelected(field) },
                label = { Text(field.displayName) }
            )
        }
    }
}

@Composable
private fun OperatorSelector(
    operators: List<FilterOperator>,
    selectedOperator: FilterOperator,
    onOperatorSelected: (FilterOperator) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        operators.forEach { operator ->
            FilterChip(
                selected = operator == selectedOperator,
                onClick = { onOperatorSelected(operator) },
                label = { Text(operator.displayName) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ValueInput(
    field: FilterField,
    repository: WisataRepository,
    value: String,
    onValueChange: (value: String, displayValue: String) -> Unit
) {
    when (field) {
        FilterField.NAMA -> {
            OutlinedTextField(
                value = value,
                onValueChange = { onValueChange(it, it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan nama...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
        
        FilterField.KATEGORI -> {
            val items = repository.getKategoriList()
            DropdownSelector(
                items = items,
                selectedValue = value,
                onValueSelected = { id -> 
                    val item = items.find { it.id == id }
                    onValueChange(id, item?.nama ?: id)
                },
                itemToId = { it.id },
                itemToName = { it.nama },
                placeholder = "Pilih kategori"
            )
        }
        
        FilterField.JENIS_TEMPAT -> {
            val items = repository.getJenisTempatList()
            DropdownSelector(
                items = items,
                selectedValue = value,
                onValueSelected = { id -> 
                    val item = items.find { it.id == id }
                    onValueChange(id, item?.nama ?: id)
                },
                itemToId = { it.id },
                itemToName = { it.nama },
                placeholder = "Pilih jenis tempat"
            )
        }
        
        FilterField.PROVINSI -> {
            val items = repository.getProvinsiList()
            DropdownSelector(
                items = items,
                selectedValue = value,
                onValueSelected = { id -> 
                    val item = items.find { it.id == id }
                    onValueChange(id, item?.nama ?: id)
                },
                itemToId = { it.id },
                itemToName = { it.nama },
                placeholder = "Pilih provinsi"
            )
        }
        
        FilterField.HARGA -> {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() }
                    val formatted = if (filtered.isNotEmpty()) {
                        "Rp ${java.text.NumberFormat.getInstance(java.util.Locale("id", "ID")).format(filtered.toLong())}"
                    } else ""
                    onValueChange(filtered, formatted)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan harga...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("Rp ") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownSelector(
    items: List<T>,
    selectedValue: String,
    onValueSelected: (String) -> Unit,
    itemToId: (T) -> String,
    itemToName: (T) -> String,
    placeholder: String
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedItem = items.find { itemToId(it) == selectedValue }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemToName(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp)
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemToName(item)) },
                    onClick = {
                        onValueSelected(itemToId(item))
                        expanded = false
                    }
                )
            }
        }
    }
}

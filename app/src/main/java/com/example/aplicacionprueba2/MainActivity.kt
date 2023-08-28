package com.example.aplicacionprueba2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.aplicacionprueba2.db.Products
import com.example.aplicacionprueba2.db.appDataBase
import com.example.aplicacionprueba2.ui.theme.AplicacionPrueba2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            val daoProducts = appDataBase.getInstance(this@MainActivity).daoProducts()
            val numberOfProducts = daoProducts.countAllProducts()
            if (numberOfProducts != null && numberOfProducts == 0){
                daoProducts.insert(Products(0, "Vodka", 8000, "botilleria", false))
                daoProducts.insert(Products(0, "Maní", 2000, "snacks", false))
                daoProducts.insert(Products(0, "Queso Azul", 8000, "lacteos", false))
                daoProducts.insert(Products(0, "Cerveza", 5000, "botilleria", false))
                daoProducts.insert(Products(0, "Jamón Serrano", 8000, "fiambreria", false))
            }
        }
        setContent {
            ProductsListUi()
        }
    }
}

@Composable
fun ProductsListUi() {
    val context = LocalContext.current
    val daoProducts = appDataBase.getInstance(context).daoProducts()
    var products by remember { mutableStateOf(emptyList<Products>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            products = withContext(Dispatchers.IO) {
                daoProducts.select()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Lista de Productos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(100.dp)
                    )

                    val boughtState = remember { mutableStateOf(product.bought) }

                    IconButton(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                product.bought = !product.bought
                                daoProducts.update(product)
                                boughtState.value = product.bought
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        val icon = if (boughtState.value) Icons.Default.ShoppingCart else Icons.Outlined.ShoppingCart
                        Icon(
                            imageVector = icon,
                            contentDescription = "Buy",
                            tint = Color.Black
                        )
                    }

                    IconButton(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                daoProducts.delete(product)
                                products = products.filter { it != product }
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                val intent = Intent(context, Add::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(
                text = "Crear Producto",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


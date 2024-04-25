package gonzalez.alan.peliculas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gonzalez.alan.peliculas.dataclass.Producto

class InventarioActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_inventario)

            val productos = listOf(
                Producto("Producto 1", "Descripción del producto 1", 1 ),
                Producto("Producto 2", "Descripción del producto 2", 1),
            )

            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            val adapter = ProductoAdapter(productos)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
}


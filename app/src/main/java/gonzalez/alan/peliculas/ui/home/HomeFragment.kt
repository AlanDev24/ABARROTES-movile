package gonzalez.alan.peliculas.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gonzalez.alan.peliculas.ProductoAdapter
import gonzalez.alan.peliculas.R
import gonzalez.alan.peliculas.databinding.FragmentHomeBinding
import gonzalez.alan.peliculas.dataclass.Producto

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductosDB(view)

        val btnActualizar: Button = view.findViewById(R.id.btnActualizar)
        btnActualizar.setOnClickListener {
            actualizarProductosEnFirebase();
        }
    }

    private fun getProductosDB(view: View){
        val database = FirebaseDatabase.getInstance().reference.child("productos")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productos = mutableListOf<Producto>()

                for (productoSnapshot in dataSnapshot.children) {
                    val producto = productoSnapshot.getValue(Producto::class.java)
                    producto?.let {
                        productos.add(it)
                    }
                }

                Log.i("productos",productos.toString());

                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
                val adapter = ProductoAdapter(productos)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

            }


            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Error al obtener productos: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarProductosEnFirebase() {
        val recyclerView: RecyclerView = view?.findViewById(R.id.recyclerView) ?: return
        val adapter = recyclerView.adapter as? ProductoAdapter ?: return

        val productosActualizados = adapter.getProductosUpdated()
        val database = FirebaseDatabase.getInstance().reference.child("productos")

        productosActualizados.forEach { (productName, producto) ->
            database.orderByChild("nombre").equalTo(productName).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { productSnapshot ->
                        productSnapshot.ref.setValue(producto)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                   Toast.makeText(requireContext(), "Error al actualizar los productos", Toast.LENGTH_SHORT).show()
                }
            })
        }
        Toast.makeText(requireContext(), "Productos actualizados", Toast.LENGTH_SHORT).show()
    }
}

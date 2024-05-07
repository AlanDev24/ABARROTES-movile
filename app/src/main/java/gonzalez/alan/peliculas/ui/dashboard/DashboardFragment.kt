package gonzalez.alan.peliculas.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import gonzalez.alan.peliculas.R
import gonzalez.alan.peliculas.databinding.FragmentDashboardBinding
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var etNombreProducto: EditText
    private lateinit var etDescripcionProducto: EditText
    private lateinit var etStock: EditText
    private lateinit var etPrecio: EditText

    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        etNombreProducto = binding.etNombreProducto
        etDescripcionProducto = binding.etDescripcionProducto
        etStock = binding.etStock
        etPrecio = binding.etPrecio

        binding.btnSeleccionarImagen.setOnClickListener {
            openImageChooser()
        }

        binding.btnRegistrarProducto.setOnClickListener {
            registrarProducto()
        }

        storageReference = FirebaseStorage.getInstance().reference.child("images")

        return root
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let {
                imageUri = it
                binding.ivProducto.setImageURI(it)
            }
        }
    }

    private fun registrarProducto() {
        val nombreProducto = etNombreProducto.text.toString().trim()
        val descripcionProducto = etDescripcionProducto.text.toString().trim()
        val stock = etStock.text.toString().trim()
        val precio = etPrecio.text.toString().trim()

        if (nombreProducto.isEmpty() || descripcionProducto.isEmpty() || stock.isEmpty() || precio.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
        } else {
            val stockNum = stock.toIntOrNull()
            val precioNum = precio.toDoubleOrNull()

            if (stockNum == null || precioNum == null) {
                Toast.makeText(requireContext(), "Ingrese valores válidos para stock y precio", Toast.LENGTH_SHORT).show()
            } else {
                uploadImageToFirebase(nombreProducto, descripcionProducto, stockNum, precioNum)
            }
        }
    }

    private fun uploadImageToFirebase(nombre: String, descripcion: String, stock: Int, precio: Double) {
        val imageRef = storageReference.child(UUID.randomUUID().toString())
        imageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    registrarProductoEnFirebase(nombre, descripcion, stock, precio, uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error al subir la imagen: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registrarProductoEnFirebase(nombre: String, descripcion: String, stock: Int, precio: Double, imageUrl: String) {
        val database = FirebaseDatabase.getInstance().reference
        val productoData = hashMapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "stock" to stock,
            "precio" to precio,
            "imageUrl" to imageUrl
        )

        database.child("productos").push().setValue(productoData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Producto agregado correctamente", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al agregar el producto", Toast.LENGTH_SHORT).show()
            }
    }

    private fun limpiarCampos() {
        etNombreProducto.text.clear()
        etDescripcionProducto.text.clear()
        etStock.text.clear()
        etPrecio.text.clear()
        binding.ivProducto.setImageResource(R.drawable.default_image)
        imageUri = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
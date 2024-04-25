package gonzalez.alan.peliculas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gonzalez.alan.peliculas.dataclass.Producto

class ProductoAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]

        //holder.imgProducto.setImageResource(producto.imagenResId)
        holder.tvNombreProducto.text = producto.nombre
        holder.tvDescripcion.text = producto.descripcion
        holder.tvStock.text = producto.stock.toString()

        holder.btnMenos.setOnClickListener {
            if (producto.stock > 0) {
                producto.stock--
                holder.tvStock.text = producto.stock.toString()
            }
        }

        holder.btnMas.setOnClickListener {
            producto.stock++
            holder.tvStock.text = producto.stock.toString()
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnMenos: Button = itemView.findViewById(R.id.btnMenos)
        val btnMas: Button = itemView.findViewById(R.id.btnMas)
    }
}

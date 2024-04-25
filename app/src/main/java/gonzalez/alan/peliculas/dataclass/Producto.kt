package gonzalez.alan.peliculas.dataclass

data class Producto(
    val nombre: String,
    val descripcion: String,
    //val imagenResId: Int,
    var stock: Int
)

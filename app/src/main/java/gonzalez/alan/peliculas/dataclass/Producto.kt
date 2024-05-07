package gonzalez.alan.peliculas.dataclass

data class Producto(
    val nombre: String = "",
    val descripcion: String = "",
    //val imagenResId: Int = 0,
    var stock: Int = 0,
    var precio: Int = 0
)

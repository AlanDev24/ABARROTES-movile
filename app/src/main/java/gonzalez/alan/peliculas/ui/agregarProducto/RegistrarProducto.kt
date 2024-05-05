package gonzalez.alan.peliculas.ui.agregarProducto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import gonzalez.alan.peliculas.databinding.FragmentDashboardBinding
import gonzalez.alan.peliculas.databinding.FragmentRegistrarProductoBinding

class RegistrarProducto : Fragment() {

    private var _binding: FragmentRegistrarProductoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(RegistrarProductoViweModel::class.java)

        _binding = FragmentRegistrarProductoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.btnRegistrarProducto
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
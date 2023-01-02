package ipca.pdm.pdmprojetofinal18476.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment() {
    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

}
package ipca.pdm.pdmprojetofinal18476.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.LoginActivity
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentOptionsBinding
import ipca.pdm.pdmprojetofinal18476.helpers.getIntentWithoutHistory

class OptionsFragment : Fragment() {
    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.buttonEditValues.setOnClickListener {
            var dialog = EditUserSettingsDialogFragment()

            dialog.show(childFragmentManager, "customDialog")
        }

        binding.buttonLogout.setOnClickListener {
            auth.signOut()

            startActivity(
                getIntentWithoutHistory(
                    requireContext(),
                    LoginActivity::class.java)
            )
        }

        return binding.root
    }

}
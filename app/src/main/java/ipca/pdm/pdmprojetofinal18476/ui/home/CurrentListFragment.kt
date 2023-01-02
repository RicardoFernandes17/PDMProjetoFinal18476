package ipca.pdm.pdmprojetofinal18476.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.R
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentCalendarListBinding
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentCurrentListBinding
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement


class CurrentListFragment : Fragment() {

    private var _binding: FragmentCurrentListBinding? = null
    private val binding get() = _binding!!

    var item: DailyCountElement = DailyCountElement(2000.00,0.00)

    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("users")
            .document(currentUser!!.uid)
            .collection("calories")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }


            }

    }

}
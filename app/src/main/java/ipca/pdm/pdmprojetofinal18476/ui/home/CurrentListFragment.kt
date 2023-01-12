package ipca.pdm.pdmprojetofinal18476.ui.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentCurrentListBinding
import ipca.pdm.pdmprojetofinal18476.helpers.createEmptyCalorieLog
import ipca.pdm.pdmprojetofinal18476.helpers.getCaloriesCollection
import ipca.pdm.pdmprojetofinal18476.helpers.toShort
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement
import java.util.Date


class CurrentListFragment : Fragment() {

    private var _binding: FragmentCurrentListBinding? = null
    private val binding get() = _binding!!

    var date: String = Date().toShort()

    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentListBinding.inflate(inflater, container, false)

        binding.buttonRegisterActivity.setOnClickListener {
            var dialog = CreateCategoryLogDialogFragment()

            dialog.show(childFragmentManager, "customDialog")

            dialog.setFragmentResultListener("addCalLog") { requestKey, bundle ->
                getCurrentElement()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentElement()

    }

    private fun getCurrentElement() {
        getCaloriesCollection(db, currentUser!!).whereEqualTo("date", date).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val item = DailyCountElement.fromDoc(documents.first())
                    setInfoValue(item)
                } else {
                    createEmptyCalorieLog(db, currentUser, requireActivity()) { item ->
                        setInfoValue(item)
                    }
                }

            }

    }


    private fun setInfoValue(item: DailyCountElement) {
        val burnedWithGoal = item.goal!! + item.burned!!
        val difference: Int = (burnedWithGoal - item.counter!!).toInt()
        val percentage: Int = ((item.counter!! / burnedWithGoal) * 100.00).toInt()

        binding.textViewConsumedCalories.text = item.counter.toString()
        binding.textViewTotalCalories.text = burnedWithGoal.toString()
        binding.textViewDeficit.text = difference.toString()
        binding.textViewPercentage.text = "${percentage}%"
        binding.progressBar.progress = percentage

        if (percentage >= 100) binding.textViewPercentage.setTextColor(Color.RED)
        binding.textViewDeficit.setTextColor(if (difference >= 200) Color.GREEN else if (difference >= 0) Color.YELLOW else Color.RED)

    }
}
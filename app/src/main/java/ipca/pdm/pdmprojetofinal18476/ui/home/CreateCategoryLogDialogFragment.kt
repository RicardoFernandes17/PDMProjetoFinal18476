package ipca.pdm.pdmprojetofinal18476.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.R
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentCreateCategoryLogDialogListDialogBinding
import ipca.pdm.pdmprojetofinal18476.helpers.getCalorieDeficitGoal
import ipca.pdm.pdmprojetofinal18476.helpers.getCaloriesCollection
import ipca.pdm.pdmprojetofinal18476.helpers.toShort
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement
import java.util.*


class CreateCategoryLogDialogFragment : DialogFragment() {

    private var _binding: FragmentCreateCategoryLogDialogListDialogBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentCreateCategoryLogDialogListDialogBinding.inflate(inflater, container, false)

        currentUser = Firebase.auth.currentUser!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textButtonView.setOnClickListener {
            dismiss()
        }

        binding.addRemoveSwitch.setOnClickListener {
            val switch : Boolean = binding.addRemoveSwitch.isChecked
            binding.iconView.setImageDrawable(resources.getDrawable(if (switch) R.drawable.ic_baseline_sports_gymnastics_24 else R.drawable.ic_baseline_fastfood_24))
        }

        binding.buttonAddCalorie.setOnClickListener {
            val qtd: Double = binding.editTextNumber.text.toString().toDouble()
            val isTraining: Boolean = binding.addRemoveSwitch.isChecked

            val date: String = Date().toShort()

            getCaloriesCollection(db, currentUser!!).whereEqualTo("date", date).get()
                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {
                        val itemToUpdate = DailyCountElement.fromDoc(documents.first())
                        if (isTraining)
                            itemToUpdate.burned = (itemToUpdate.burned ?: 0.00) + qtd
                        else
                            itemToUpdate.counter = (itemToUpdate.counter ?: 0.00) + qtd


                        getCaloriesCollection(db, currentUser!!).document(itemToUpdate.uid!!)
                            .update(itemToUpdate.toHashMap())
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "CategoryLog",
                                    "DocumentSnapshot updated with ID: ${itemToUpdate.uid}"
                                )
                            }.addOnFailureListener { e ->
                                onCreateEditError(e)
                            }

                    } else {
                        val item =
                            DailyCountElement(
                                if (isTraining) qtd else 0.00,
                                getCalorieDeficitGoal(db, currentUser),
                                if (isTraining) 0.00 else qtd,
                                date
                            )

                        getCaloriesCollection(db, currentUser!!).add(item.toHashMap())
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "CategoryLog",
                                    "DocumentSnapshot written with ID: ${documentReference.id}"
                                )

                            }.addOnFailureListener { e ->
                                onCreateEditError(e)
                            }
                    }

                }

            val result = "result"

            setFragmentResult("addCalLog", bundleOf("sucess" to result))
            dismiss()
        }
    }

    private fun onCreateEditError(e: Exception) {
        Log.w(
            "CategoryLog", "Error adding document", e
        )
        Toast.makeText(
            activity, "Falha de conexão", Toast.LENGTH_SHORT
        ).show()
    }
}
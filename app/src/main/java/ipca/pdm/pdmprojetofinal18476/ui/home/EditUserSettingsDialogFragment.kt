package ipca.pdm.pdmprojetofinal18476.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.databinding.FragmentEditUserSettingsDialogListDialogBinding
import ipca.pdm.pdmprojetofinal18476.helpers.editCurrentGoal
import ipca.pdm.pdmprojetofinal18476.helpers.getSettingsCollection
import ipca.pdm.pdmprojetofinal18476.types.UserDefinitionsElement


class EditUserSettingsDialogFragment : DialogFragment() {

    private var _binding: FragmentEditUserSettingsDialogListDialogBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentEditUserSettingsDialogListDialogBinding.inflate(inflater, container, false)
        currentUser = Firebase.auth.currentUser!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSettingsCollection(db, currentUser).get()
            .addOnSuccessListener { documents ->

                if (!documents.isEmpty) {
                    val item = UserDefinitionsElement.fromDoc(documents.first())

                    binding.editTextNumber.setText("${item.goal}")
                } else {
                    val item = UserDefinitionsElement(2000.00)

                    getSettingsCollection(db, currentUser)
                        .add(item.toHashMap()).addOnSuccessListener {
                            binding.editTextNumber.setText("${item.goal}")
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                activity, "Falha de conexão", Toast.LENGTH_SHORT
                            ).show()

                            dismiss()
                        }
                }
            }

        binding.buttonAddCalorie.setOnClickListener {
            getSettingsCollection(db, currentUser).get()
                .addOnSuccessListener { documents ->

                    val goal = binding.editTextNumber.text.toString().toDouble()
                    if (!documents.isEmpty) {
                        val itemToUpdate = UserDefinitionsElement.fromDoc(documents.first())
                        itemToUpdate.goal = goal

                        getSettingsCollection(db, currentUser)
                            .document(itemToUpdate.uid!!).update(itemToUpdate.toHashMap())
                            .addOnSuccessListener {
                                dismiss()
                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    activity, "Falha de conexão", Toast.LENGTH_SHORT
                                ).show()
                                dismiss()
                            }

                    } else {

                        val item = UserDefinitionsElement(goal)

                        getSettingsCollection(db, currentUser )
                            .add(item.toHashMap()).addOnSuccessListener { document ->
                                dismiss()

                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    activity, "Falha de conexão", Toast.LENGTH_SHORT
                                ).show()
                                dismiss()
                            }
                    }

                    editCurrentGoal(goal,db, currentUser, activity)

                }
            dismiss()
        }
    }
}
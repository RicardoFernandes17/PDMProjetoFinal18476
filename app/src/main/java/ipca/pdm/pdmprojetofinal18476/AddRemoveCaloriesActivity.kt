package ipca.pdm.pdmprojetofinal18476

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.databinding.ActivityAddRemoveCaloriesBinding
import ipca.pdm.pdmprojetofinal18476.shared.CommonActivity
import ipca.pdm.pdmprojetofinal18476.helpers.toServerFormat
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AddRemoveCaloriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRemoveCaloriesBinding

    private val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRemoveCaloriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val currentUser = Firebase.auth.currentUser

        val items = arrayListOf<String>()



        binding.buttonAddCalorie.setOnClickListener {

            val qtd: Double = binding.editTextNumber.text.toString().toDouble()
            val isTraining: Boolean = binding.addRemoveSwitch.isChecked
            val date= LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val dateString : String = formatter.format(date)


            db.collection("users")
                .document(currentUser?.uid!!)
                .collection("calories")
                .whereEqualTo("date", dateString)
                .get()
                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {
                        val itemToUpdate = DailyCountElement.fromDoc(documents.first())
                        if (isTraining) {
                            itemToUpdate.goal = (itemToUpdate.goal ?: 2000.00) + qtd
                        }
                        else{
                            itemToUpdate.counter = (itemToUpdate.counter ?: 2000.00) + qtd
                        }

                        db.collection("users")
                            .document(currentUser?.uid!!)
                            .collection("calories")
                            .document(itemToUpdate.uid)
                            .update(itemToUpdate.toHashMap())
                            .addOnSuccessListener { documentReference ->
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(
                                    this@AddRemoveCaloriesActivity,
                                    "Falha de conexão",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    } else {
                        var initialCounter = 0.00;
                        var initialGoal = 2000.00;
                        if(isTraining){
                            initialGoal += qtd
                        } else
                        {
                            initialCounter += qtd
                        }


                        val now = LocalDateTime.now()
                        val item = DailyCountElement(initialGoal, initialCounter,dateString)

                        db.collection("users")
                            .document(currentUser?.uid!!)
                            .collection("calories")
                            .add(item.toHashMap())
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot written with ID: ${documentReference.id}"
                                )
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                                Toast.makeText(
                                    this@AddRemoveCaloriesActivity,
                                    "Falha de conexão",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }


                }


        }
    }

    companion object {
        const val TAG = "AddItemActivity"
    }
}
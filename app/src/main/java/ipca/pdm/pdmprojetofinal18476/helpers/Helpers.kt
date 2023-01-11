package ipca.pdm.pdmprojetofinal18476.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import ipca.pdm.pdmprojetofinal18476.types.DailyCountElement
import ipca.pdm.pdmprojetofinal18476.types.UserDefinitionsElement
import java.text.SimpleDateFormat
import java.util.*


fun Date.toShort(): String {
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return format.format(this)
}

enum class LightIndicatorStatus {
    GOOD, WARNING, BAD
}


fun getCaloriesCollection(database: FirebaseFirestore, user: FirebaseUser): CollectionReference {
    return database.collection("users").document(user.uid).collection("calories")
}

fun getSettingsCollection(database: FirebaseFirestore, user: FirebaseUser): CollectionReference {
    return database.collection("users").document(user.uid).collection("settings")
}

fun getCalorieDeficitGoal(database: FirebaseFirestore, user: FirebaseUser , callback: (Double)->Unit ) {


    getSettingsCollection(database, user).get().addOnSuccessListener { documents ->
        if (!documents.isEmpty) {
                val item = UserDefinitionsElement.fromDoc(documents.first())

                callback.invoke(item.goal!!)
            }
        }


}

fun setDefaultDateValues(
    item: DailyCountElement,
    database: FirebaseFirestore,
    user: FirebaseUser,
    activity: FragmentActivity
): Any {
    getCaloriesCollection(database, user).add(item.toHashMap())
        .addOnSuccessListener { documentReference ->
            Log.d(
                "CategoryLog", "DocumentSnapshot written with ID: ${documentReference.id}"
            )

        }.addOnFailureListener { e ->
            Log.w(
                "CategoryLog", "Error adding document", e
            )
            Toast.makeText(
                activity, "Falha de conexão", Toast.LENGTH_SHORT
            ).show()
        }

    return true
}

fun createEmptyCalorieLog(
    database: FirebaseFirestore, user: FirebaseUser, activity: FragmentActivity, callback: (DailyCountElement) -> Unit
) {
    getCalorieDeficitGoal(database, user) {
        val date: String = Date().toShort()
        var item = DailyCountElement(0.00, it, 0.00, date)
        setDefaultDateValues(item, database, user, activity)
        callback.invoke(item)
    }

}

fun editCurrentGoal(
    goal: Double, database: FirebaseFirestore, user: FirebaseUser, activity: FragmentActivity?
) {
    val date: String = Date().toShort()
    getCaloriesCollection(database, user).whereEqualTo("date", date).get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val itemToUpdate = DailyCountElement.fromDoc(documents.first())
                itemToUpdate.goal = goal

                getCaloriesCollection(database, user).document(itemToUpdate.uid)
                    .update(itemToUpdate.toHashMap()).addOnSuccessListener {

                    }.addOnFailureListener { _ ->
                        Toast.makeText(
                            activity, "Falha de conexão", Toast.LENGTH_SHORT
                        ).show()
                    }

            }
        }
}

fun getIntentWithoutHistory(context: Context, classes: Class<*>): Intent {
    val intent: Intent = Intent(context, classes)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}
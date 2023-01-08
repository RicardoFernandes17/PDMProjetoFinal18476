package ipca.pdm.pdmprojetofinal18476.types

import com.google.firebase.firestore.DocumentSnapshot


data class DailyCountElement (
    var burned: Double?,
    var goal: Double?,
    var counter: Double?,
    var date: String?,
    var uid         : String = ""
) {


    fun toHashMap(): java.util.HashMap<String, Any?> {
        return hashMapOf(
            "burned" to burned,
            "goal" to goal,
            "counter" to counter,
            "date" to date,
        )
    }

    companion object {
        fun fromDoc(documentSnapshot: DocumentSnapshot): DailyCountElement {
            return DailyCountElement(
                documentSnapshot.getDouble("burned"),
                documentSnapshot.getDouble("goal"),
                documentSnapshot.getDouble("counter"),
                documentSnapshot.getString("date"),
                documentSnapshot.id
            )
        }
    }


}

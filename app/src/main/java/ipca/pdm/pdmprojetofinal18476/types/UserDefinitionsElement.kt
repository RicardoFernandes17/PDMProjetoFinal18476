package ipca.pdm.pdmprojetofinal18476.types

import com.google.firebase.firestore.DocumentSnapshot


data class UserDefinitionsElement (
    var goal: Double?,
    var uid         : String = ""
) {


    fun toHashMap(): java.util.HashMap<String, Any?> {
        return hashMapOf(
            "goal" to goal,
        )
    }

    companion object {
        fun fromDoc(documentSnapshot: DocumentSnapshot): UserDefinitionsElement {
            return UserDefinitionsElement(
                documentSnapshot.getDouble("goal"),
                documentSnapshot.id
            )
        }
    }


}

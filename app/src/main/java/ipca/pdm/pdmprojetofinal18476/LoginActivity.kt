package ipca.pdm.pdmprojetofinal18476

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString();
            val password = binding.editTextPassword.text.toString();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if( task.isSuccessful){
                    Log.d(TAG, "Sucess")
                    Toast.makeText(baseContext, "Tá bom", Toast.LENGTH_SHORT).show()
                }
                else{

                    Log.w(TAG, "Error")
                    Toast.makeText(baseContext, "Tá mal", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



    companion object{
        const val TAG = "LoginActivity"
    }
}
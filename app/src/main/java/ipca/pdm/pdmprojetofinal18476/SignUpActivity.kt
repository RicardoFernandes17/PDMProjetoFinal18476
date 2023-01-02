package ipca.pdm.pdmprojetofinal18476

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.databinding.ActivityLoginBinding
import ipca.pdm.pdmprojetofinal18476.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString();
            val password = binding.editTextPassword.text.toString();
            val confirm = binding.editTextConfirmPassword.text.toString()

            if (email != null && password != null && confirm != null && password == confirm) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(LoginActivity.TAG, "Sucess")
                            Toast.makeText(
                                baseContext,
                                "Conta criada com sucesso! Por favor, faça login.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    LoginActivity::class.java
                                )
                            )

                        } else {
                            Log.w(LoginActivity.TAG, "Error")
                            Toast.makeText(baseContext, "Algo deu errado", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                if (email == null || password == null || confirm == null) {
                    Log.w(LoginActivity.TAG, "Error")
                    Toast.makeText(baseContext, "Insira todos os campos!", Toast.LENGTH_SHORT)
                        .show()
                }
                if (password != confirm) {
                    Log.w(LoginActivity.TAG, "Error")
                    Toast.makeText(baseContext, "As passwords não coicidem", Toast.LENGTH_SHORT)
                        .show()


                }
            }

        }

        binding.textButtonLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@SignUpActivity,
                    LoginActivity::class.java
                )
            )
        }
    }

}
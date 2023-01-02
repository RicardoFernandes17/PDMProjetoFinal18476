package ipca.pdm.pdmprojetofinal18476.shared

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ipca.pdm.pdmprojetofinal18476.LoginActivity
import ipca.pdm.pdmprojetofinal18476.R

open class CommonActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {

            R.id.logoutButton -> {

                auth.signOut()
                startActivity(
                    Intent(this@CommonActivity,
                        LoginActivity::class.java)
                )
                true
            }
            else -> {
                false
            }
        }
    }
}
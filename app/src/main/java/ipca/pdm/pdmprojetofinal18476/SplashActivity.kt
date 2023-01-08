package ipca.pdm.pdmprojetofinal18476

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import ipca.pdm.pdmprojetofinal18476.helpers.getIntentWithoutHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task->
            if(!task.isSuccessful) {
                Log.w(Tag, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            Log.d(Tag, token)
        })

        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            val currentUser = Firebase.auth.currentUser
            
            if(currentUser != null){
                startActivity(getIntentWithoutHistory(this@SplashActivity,MainActivity::class.java))
            }else{
                startActivity(getIntentWithoutHistory(this@SplashActivity,LoginActivity::class.java))
            }
        }
    }

    companion object  {
        const val Tag = "Splash Activity"
    }
}
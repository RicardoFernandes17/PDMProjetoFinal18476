package ipca.pdm.pdmprojetofinal18476

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ipca.pdm.pdmprojetofinal18476.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setSupportActionBar(binding.toolbar)

        //val navController = findNavController(R.id.nav)


    }
}
package my.com.toru.torumeetfb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val instance:FirebaseRemoteConfig by lazy{
        FirebaseRemoteConfig.getInstance()
    }

    private var isFetched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configSetting = FirebaseRemoteConfigSettings.Builder()
                                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                                .build()
        instance.apply {
            setConfigSettings(configSetting)
            setDefaults(R.xml.toru_remote_config_default)
        }

        main_btn_config.setOnClickListener {
            if(isFetched){
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            }
            else{
                Toast.makeText(this@MainActivity, "Not fetched, Wait for a while.", Toast.LENGTH_SHORT).show()
            }
        }

        fetch()
    }

    private fun fetch(){
        instance.fetch(1)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this@MainActivity, "Fetch Succeeded", Toast.LENGTH_SHORT).show()
                        instance.activateFetched()
                        isFetched = true
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Fetch Failed", Toast.LENGTH_SHORT).show()
                    }
                    displayMessageToBtn()
                }
    }

    private fun displayMessageToBtn(){
        val stringFromConfig = instance.getString("test_button_text")
        Log.w("MainActivity", "stringfromconfig = $stringFromConfig")
        main_btn_config.text = stringFromConfig
    }
}
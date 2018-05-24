package my.com.toru.torumeetfb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class InitActivity : AppCompatActivity() {

    private val firebaseConfigInstance:FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        hideStatusBar()

        initFirebaseRemoteConfig()
    }


    private fun initFirebaseRemoteConfig(){
        val fbConfigSetting = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build()

        firebaseConfigInstance.apply {
            setDefaults(R.xml.toru_remote_config_default)
            setConfigSettings(fbConfigSetting)
        }

        firebaseConfigInstance.fetch(1).addOnCompleteListener {
            if(it.isComplete){
                firebaseConfigInstance.activateFetched()
            }
            val keyForNext = firebaseConfigInstance.getString("init_to_next_activity")
            val next = when(keyForNext){
                "MainActivity" -> MainActivity::class.java
                else -> SecondActivity::class.java
            }

            startActivity(Intent(this@InitActivity, next))
            finish()
        }
    }
}

fun AppCompatActivity.hideStatusBar(){
    this.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
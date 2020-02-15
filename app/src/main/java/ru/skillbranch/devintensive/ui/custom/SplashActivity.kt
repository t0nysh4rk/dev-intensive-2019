package ru.skillbranch.devintensive.ui.custom

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.skillbranch.devintensive.ui.profile.ProfileActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashActivity, ProfileActivity::class.java))

        finish()
    }
}
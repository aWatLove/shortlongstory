package com.example.lab8

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance().getReference("User")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)


        findViewById<Button>(R.id.loginBTNSignin).setOnClickListener{
            var login = findViewById<EditText>(R.id.loginETLogin)
            var password = findViewById<EditText>(R.id.loginETPassword)
            db.child(login.text.toString()).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    if (password.text.toString() == snapshot.child("pwd").value.toString()) {
                        // Сохраняем логин в SharedPreferences
                        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            putString("user_login", login.text.toString())
                            apply()
                        }
                        val i = Intent(this@MainActivity, MainActivity3::class.java)
                        i.putExtra("user", login.text.toString())
                        startActivity(i)
                        Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Пароль неверный", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Логин неверный", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка чтения данных из базы данных: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.loginBTNSignup).setOnClickListener{
            val i = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(i)
        }
    }
}
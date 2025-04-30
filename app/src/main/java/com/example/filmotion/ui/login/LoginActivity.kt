package com.example.filmotion.ui.login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmotion.R
import com.example.filmotion.ui.register.RegisterActivity
import com.example.filmotion.data.model.LoginResponse
import com.example.filmotion.data.api.RetrofitClient
import com.example.filmotion.ui.main.MainActivity
import com.example.filmotion.utils.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private var logeado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        registerButton = findViewById(R.id.buttonGoToRegister)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginUser(email: String, password: String) {
        val apiService = RetrofitClient.create(this)
        val call = apiService.loginUser(email, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.e("LoginActivity", "Respuesta en bruto: $loginResponse")

                    if (loginResponse != null && loginResponse.status == "success") {
                        Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()

                        // Guardar token y email correctamente
                        val prefs = PreferencesManager(this@LoginActivity)
                        prefs.saveToken(loginResponse.token ?: "")
                        Log.d("LoginActivity", "Token guardado: ${prefs.getToken()}")

                        prefs.saveEmail(loginResponse.email ?: "")

                        // Ir a MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, loginResponse?.message ?: "Error", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("LoginActivity", "Error en la respuesta: $errorBody")
                    Toast.makeText(this@LoginActivity, "Error en la conexión, intenta nuevamente", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error en la conexión", t)
                Toast.makeText(this@LoginActivity, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
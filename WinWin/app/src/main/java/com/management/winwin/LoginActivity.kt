package com.management.winwin

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import com.management.winwin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityLoginBinding? = null
    private lateinit var idText: AppCompatEditText
    private lateinit var passwordText: AppCompatEditText
    private var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        idText = binding!!.idText
        passwordText = binding!!.passwordText

        binding!!.loginBtn.setOnClickListener(this)
        binding!!.joinBtn.setOnClickListener(this)
        binding!!.findBtn.setOnClickListener(this)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.loginBtn -> {
                val check = login()
                if(!check)
                    return
                Toast.makeText(this,"로그인 성공", Toast.LENGTH_SHORT).show()
                Log.d("LogInSuccess", "ID : " + idText.text.toString() + "\nPW : " + passwordText.text.toString())

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.joinBtn -> {
                Log.d("LoginActivity", "JoinBtn")
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
            }

            R.id.findBtn -> {
                Log.d("LoginActivity", "FindBtn")
                val intent = Intent(this, FindActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            quitAction()
        }
    }


    private fun quitAction() {
        if(System.currentTimeMillis() > waitTime + 2000) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(baseContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다. " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show()
            return
        }
        if(System.currentTimeMillis() <= waitTime + 2000) {
            finish()
        }
    }

    override fun onBackPressed() {
        quitAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun login():Boolean {
        if(idText.text!!.isBlank() || idText.text!!.isEmpty()) {
            Log.d("LoginActivity", "ID Fail")
            Toast.makeText(baseContext, "아이디를 확인해 주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if(passwordText.text!!.isBlank() || passwordText.text!!.isEmpty()) {
            Log.d("LoginActivity", "Password Fail")
            Toast.makeText(baseContext, "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
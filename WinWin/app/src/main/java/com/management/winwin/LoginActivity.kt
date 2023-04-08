package com.management.winwin

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.management.winwin.databinding.ActivityLoginBinding
import com.management.winwin.preference.Application.Companion.prefs
import com.management.winwin.startServer.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import kotlin.concurrent.schedule
import android.app.Application as Application


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var idText: AppCompatEditText
    private lateinit var passwordText: AppCompatEditText
    private var waitTime = 0L
    private var contentHasLoaded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startLoadingContent()
        setupSplashScreen(splashScreen)

        idText = binding.idText
        passwordText = binding.passwordText

        binding.loginBtn.setOnClickListener(this)
        binding.joinBtn.setOnClickListener(this)
        binding.findBtn.setOnClickListener(this)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        }

        idFocusListener()
        passwordFocusListener()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.loginBtn -> {
                //val check = valid()

                //if(!check) {
                //    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                //    return
                //}

                requestToServer(binding.idText.text.toString(), binding.passwordText.text.toString())
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
            Toast.makeText(baseContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
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
    }

    private fun valid():Boolean {
        if(binding.idText.text!!.isEmpty() || binding.passwordText.text!!.isEmpty()) {
            return false
        }

        var validID = false
        var validPassword = false

        if(!binding.idText.text.isNullOrEmpty() && binding.idContainer.error == null) {
            validID = true
        }
        if(!binding.passwordText.text.isNullOrEmpty() && binding.passwordContainer.error == null) {
            validPassword = true
        }

        return validID && validPassword
    }

    private fun idFocusListener() {
        binding.idText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.idContainer.error = validEmail()
            }
        }
    }

    private fun validEmail():String? {
        val emailText = binding.idText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return resources.getString(R.string.emailError)
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.passwordContainer.error = validPassword()
            }
        }
    }

    private fun validPassword():String? {
        val passwordText = binding.passwordText.text.toString()

        if(!passwordText.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$])(?=\\S+$).*$".toRegex())) {
            return "1개 이상의 대,소문자 및 숫자, !@#\$를 포함해야 합니다."
        }
        if(passwordText.length < 8) {
            return "비밀번호는 최소 8자리 이상입니다."
        }
        if(passwordText.length > 20) {
            return "비밀번호는 최대 20자리 입니다."
        }
        return null
    }

    private fun startLoadingContent() {
        Timer().schedule(1500) {
            contentHasLoaded = true
        }
    }

    private fun setupSplashScreen(splashScreen: SplashScreen) {
        val content:View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object: ViewTreeObserver.OnPreDrawListener{
                override fun onPreDraw(): Boolean {
                    return if(contentHasLoaded) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else{
                        false
                    }
                }
            }
        )
/*
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideBack = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.view.width.toFloat()
            ).apply {
                interpolator = DecelerateInterpolator()
                duration = 800L
                doOnEnd { splashScreenView.remove() }
            }
            slideBack.start()
        }*/
    }

    private fun requestToServer(userName:String, password:String) {
        val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
        val info = RequestLogin(userName, password)

        retIn.requestLogin(info).enqueue(object:
            Callback<ResponseLogin> {
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("서버 통신 실패", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseLogin>,
                response: retrofit2.Response<ResponseLogin>) {
                if(response.code() == 200) {
                    val loginResponse = response.body()
                    if(loginResponse != null) {
                        val token = response.headers()["Authorization"].toString()
                        val code = loginResponse.code
                        val message = loginResponse.message
                        prefs.setString("token", token)

                        Log.d("로그인 통신 성공", "token : $token code : $code message : $message token : $token")
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                else {
                    val jsonObject = JSONObject(response.errorBody()!!.string())
                    val code = jsonObject.get("code")
                    val message = jsonObject.get("message")

                    Log.e("로그인 실패 4XX", "error : ${response.code()}" + " message: $message")
                    if(code == 101002) {
                        Toast.makeText(this@LoginActivity, "아이디를 확인해 주세요", Toast.LENGTH_SHORT).show()
                    }
                    else if(code == 101003) {
                        Toast.makeText(this@LoginActivity, "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
package com.management.winwin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.management.winwin.databinding.ActivityJoinBinding
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityJoinBinding
    private lateinit var textId:AppCompatEditText
    private lateinit var textPW:AppCompatEditText
    private lateinit var textRePW:AppCompatEditText
    private lateinit var textEmail:AppCompatEditText
    private lateinit var textName:AppCompatEditText
    private lateinit var checkDuplicate:AppCompatButton
    private lateinit var joinBtn:AppCompatButton
    private var dupCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textId = binding.IDText
        textPW = binding.passwordText
        textRePW = binding.rePasswordText
        textEmail = binding.emailText
        textName = binding.nameText
        checkDuplicate = binding.dupCheckBtn
        joinBtn = binding.joinBtn


        idFocusListener()
        emailFocusListener()
        passwordFocusListener()
        nameFocusListener()
        rePasswordFocusListener()

        checkDuplicate.setOnClickListener(this)
        joinBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.dupCheckBtn-> {
                if(binding.IDText.text!!.isEmpty() || binding.IDContainer.error != null) {
                    Toast.makeText(baseContext, "아이디를 확인해 주세요", Toast.LENGTH_SHORT).show()
                    return
                }
                Log.d("JoinActivity", "ID : " + textId.text.toString())
                dupCheck = true
            }

            R.id.joinBtn->{
                val check = valid()
                if(!check) {
                    Toast.makeText(baseContext, "회원가입 정보를 확인해 주세요", Toast.LENGTH_SHORT).show()
                    return
                }
                if(!dupCheck) {
                    Toast.makeText(baseContext, "중복확인을 해주세요", Toast.LENGTH_SHORT).show()
                    return
                }

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                Log.d("LoginInfo", "${binding.IDText.text.toString()}\n" +
                        "${binding.passwordText.text.toString()}\n" + "${binding.rePasswordText.text.toString()}\n" +
                "${binding.emailText.text.toString()}\n" + "${binding.nameText.text.toString()}\n")
            }
        }
    }

    private fun valid():Boolean {
        val validID = binding.IDContainer.error == null
        val validPassword = binding.passwordContainer.error == null
        val validRePassword = binding.rePasswordContainer.error == null
        val validEmail = binding.emailContainer.error == null
        val validName = binding.nameContainer.error == null

        return validID && validPassword && validRePassword && validEmail && validName
    }


    private fun idFocusListener() {
        binding.IDText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.IDContainer.error = validID()
            }
        }
    }

    private fun validID():String? {
        val idText = binding.IDText.text.toString()
        if(idText.length > 10) {
            return "아이디는 최대 10자입니다."
        }
        if(idText.length < 4) {
            return "아이디는 최소 4자입니다."
        }
        if(!idText.matches(".*[0-9].*".toRegex())) {
            return "아이디는 1개 숫자를 포함합니다."
        }
        if(!idText.matches(".*[a-zA-z].*".toRegex())) {
            return "아이디는 1개 이상의 대, 소문자를 포함합니다."
        }

        return null
    }

    private fun emailFocusListener() {
        binding.emailText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.emailContainer.error = validEmail()
            }
        }
    }

    private fun validEmail():String? {
        val emailText = binding.emailText.text.toString()
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

    private fun rePasswordFocusListener() {
        binding.rePasswordText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.rePasswordContainer.error = validRePassword()
            }
        }

    }

    private fun validPassword():String? {
        val passwordText = binding.passwordText.text.toString()
        if(passwordText.length < 4) {
            return "비밀번호는 최소 4자리 이상입니다."
        }
        if(passwordText.length > 20) {
            return "비밀번호는 최대 20자리 입니다."
        }

        if(!passwordText.matches(".*[0-9].*".toRegex())) {
            return "비밀번호는 1개 숫자를 포함합니다."
        }
        if(!passwordText.matches(".*[a-zA-z].*".toRegex())) {
            return "비밀번호는 1개 이상의 대, 소문자를 포함합니다."
        }
        if(!passwordText.matches(".*[@#$%^&!].*".toRegex())) {
            return "비밀번호는 !@#$%^& 중 하나를 포함해야 합니다."
        }
        return null
    }

    private fun validRePassword():String? {
        val passwordText = binding.passwordText.text.toString()
        val rePasswordText = binding.rePasswordText.text.toString()

        if(passwordText.isEmpty()) {
            return "비밀번호를 입력해 주세요"
        }

        if(passwordText != rePasswordText) {
            return "비밀번호가 일치하지 않습니다."
        }
        return null
    }

    private fun nameFocusListener() {
        binding.nameText.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.nameContainer.error = validName()
            }
        }
    }

    private fun validName():String? {
        val nameText = binding.nameText.text.toString()
        if(nameText.isEmpty()) {
            return "이름을 입력해 주세요"
        }

        if(!nameText.matches("^[가-힣]+$".toRegex())) {
            return "이름은 한글만 입력 가능합니다."
        }
        return null
    }
}
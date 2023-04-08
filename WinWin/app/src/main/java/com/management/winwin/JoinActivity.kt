package com.management.winwin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.management.winwin.databinding.ActivityJoinBinding
import com.management.winwin.startServer.*
import retrofit2.Call
import retrofit2.Callback

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

        setActionBar()
        //textId = binding.IDText
        textPW = binding.passwordText
        textRePW = binding.rePasswordText
        textEmail = binding.emailText
        textName = binding.nameText
        checkDuplicate = binding.dupCheckBtn
        joinBtn = binding.joinBtn

        //idFocusListener()
        emailFocusListener()
        passwordFocusListener()
        nameFocusListener()
        rePasswordFocusListener()

        checkDuplicate.setOnClickListener(this)
        joinBtn.setOnClickListener(this)

    }

    private fun setActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return false
        }
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.dupCheckBtn-> {
                dupCheck = false
                val check = checkDuplicate()
                if(!check) {
                    Toast.makeText(this, "아이디를 확인해 주세요", Toast.LENGTH_SHORT).show()
                }

                requestToServer(binding.emailText.text.toString())
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

                requestToServer(binding.emailText.text.toString(), binding.passwordText.text.toString(), binding.nameText.text.toString())
            }
        }
    }

    private fun valid():Boolean {
        //var validID = false
        var validPassword = false
        var validRePassword = false
        var validEmail = false
        var validName = false

        //if(!binding.IDText.text.isNullOrEmpty() && binding.IDContainer.error == null) {
            //validID = true
        //}
        if(!binding.passwordText.text.isNullOrEmpty() && binding.passwordContainer.error == null) {
            validPassword = true
        }
        if(!binding.rePasswordText.text.isNullOrEmpty() && binding.rePasswordContainer.error == null) {
            validRePassword = true
        }
        if(!binding.emailText.text.isNullOrEmpty() && binding.EmailContainer.error == null) {
            validEmail = true
        }
        if(!binding.nameText.text.isNullOrEmpty() && binding.nameContainer.error == null) {
            validName = true
        }

        return validEmail && validPassword && validRePassword && validName
    }

    private fun checkDuplicate():Boolean {
        val email = binding.emailText.text
        if(email.isNullOrEmpty() || email.isNullOrBlank()) {
            return false
        }

        if(validEmail() == null) {
            return true
        }

        return false
    }

    /*
    private fun idFocusListener() {
        binding.IDText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.IDContainer.error = validID()
            }

        })
    }*/

    /*
    private fun validID():String? {
        val idText = binding.IDText.text.toString()
        if(!idText.matches("^[0-9a-zA-Z]*$".toRegex())) {
           return "아이디는 1개 이상의 대,소문자 및 숫자만 가능합니다."
        }
        if(idText.length < 4) {
            return "아이디는 최소 4자 이상입니다."
        }
        if(idText.length > 10) {
            return "아이디는 최대 10자 이하입니다."
        }
        return null
    }*/

    private fun emailFocusListener() {
        binding.emailText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.EmailContainer.error = validEmail()
            }

        })
    }

    private fun validEmail():String? {
        val emailText = binding.emailText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return resources.getString(R.string.emailError)
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.passwordContainer.error = validPassword()
            }

        })
    }

    private fun rePasswordFocusListener() {
        binding.rePasswordText.addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.rePasswordContainer.error = validRePassword()
            }

        })
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
        binding.nameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.nameContainer.error = validName()
            }

        })
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

    private fun requestToServer(userName:String, password:String, email:String) {
        Log.e("서버 request", "$userName $password $email")
        val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
        val info = RequestJoin(userName, password, email)

        retIn.requestJoin(info).enqueue(object :
            Callback<ResponseJoin> {
            override fun onFailure(call: Call<ResponseJoin>, t: Throwable) {
                Log.e("서버 통신 실패", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseJoin>,
                response: retrofit2.Response<ResponseJoin>
            ) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        val joinResponse = response.body()
                        val code = joinResponse!!.code
                        val message = joinResponse.message
                        val data = joinResponse.data
                        Log.d("서버 통신 성공", "code : $code message : $message data : $data")
                        Toast.makeText(this@JoinActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.e("서버 통신 성공 4XX", "code : ${response.code()}" + " message : ${response.errorBody()?.string()}")
                    Toast.makeText(this@JoinActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun requestToServer(userName:String) {
        val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
        val info = RequestDuplicate(userName)

        retIn.requestDuplicate(info).enqueue(object: Callback<ResponseDuplicate> {
            override fun onResponse(
                call: Call<ResponseDuplicate>,
                response: retrofit2.Response<ResponseDuplicate>
            ) {
                if(response.code() == 200) {
                    val duplicateResponse = response.body()
                    if(duplicateResponse != null) {
                        val code = duplicateResponse.code
                        val message = duplicateResponse.message
                        val data = duplicateResponse.data
                        Log.d("서버 통신 중복확인 성공", "code : $code message : $message data: $data")

                        if(code == 103001) {
                            Toast.makeText(this@JoinActivity, message, Toast.LENGTH_SHORT).show()
                        }
                        else if(code == 103002) {
                            Toast.makeText(this@JoinActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseDuplicate>, t: Throwable) {
                Log.e("서버 통신 중복확인 실패", "${t.message}")
            }
        })
    }
}
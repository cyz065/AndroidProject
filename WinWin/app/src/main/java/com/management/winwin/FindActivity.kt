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
import com.management.winwin.Dialog.FailedDialog
import com.management.winwin.Dialog.FailedDialogInterface
import com.management.winwin.Dialog.SuccessDialog
import com.management.winwin.Dialog.SuccessDialogInterface
import com.management.winwin.databinding.ActivityFindBinding
import com.management.winwin.startServer.*
import retrofit2.Call
import retrofit2.Callback
import java.util.Random

class FindActivity : AppCompatActivity(), View.OnClickListener, SuccessDialogInterface, FailedDialogInterface{
    private lateinit var binding:ActivityFindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        binding.findCheckBtn.setOnClickListener(this)

        emailFocusListener()
        nameFocusListener()
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.findCheckBtn -> {
                if(valid()) {
                    val dialog = SuccessDialog(this)
                    dialog.isCancelable = false
                    dialog.show(this.supportFragmentManager, "ConfirmDialog")

                    Toast.makeText(this,"확인", Toast.LENGTH_SHORT).show()
                }

                else {
                    val dialog = FailedDialog(this)
                    dialog.isCancelable = false
                    dialog.show(this.supportFragmentManager, "FailDialog")

                    Toast.makeText(this, "모든 정보를 입력하세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun valid():Boolean {
        var validEmail = false
        var validName = false

        if(!binding.FindNameText.text.isNullOrEmpty() && binding.FindNameInput.error == null) {
            validName = true
        }
        if(!binding.FindEmailText.text.isNullOrEmpty() && binding.FindEmailInput.error == null) {
            validEmail = true
        }

        return validEmail && validName
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
        binding.FindEmailText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.FindEmailInput.error = validEmail()
            }

        })
    }

    private fun validEmail():String? {
        val emailText = binding.FindEmailText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return resources.getString(R.string.emailError)
        }
        return null
    }

    private fun nameFocusListener() {
        binding.FindNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.FindEmailInput.error = validName()
            }

        })
    }

    private fun validName():String? {
        val nameText = binding.FindNameText.text.toString()
        if(nameText.isEmpty()) {
            return "이름을 입력해 주세요"
        }

        if(!nameText.matches("^[가-힣]+$".toRegex())) {
            return "이름은 한글만 입력 가능합니다."
        }
        return null
    }

    private fun setActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar2))
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

    override fun onConfirmButtonClick() {
        Toast.makeText(this, "이메일 전송 완료", Toast.LENGTH_SHORT).show()
    }

    override fun onFailButtonClick() {
        Toast.makeText(this, "가입 정보 확인", Toast.LENGTH_SHORT).show()
    }
}
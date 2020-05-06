package com.jstudio.app.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jstudio.app.App
import com.jstudio.app.R
import com.jstudio.app.http.LoadState
import com.jstudio.app.list.ListActivity
import com.jstudio.jkit.ToastKit
import com.jstudio.jkit.onClick
import com.jstudio.jkit.parseToJson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var isAccountValid = false
    var isPasswordValid = false
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        accountTextField.editText?.doAfterTextChanged {
            val account = it?.toString() ?: ""
            isAccountValid = if (account.startsWith("1").not() || account.length > 11) {
                accountTextField.error = "Phone number is incorrect"
                false
            } else {
                accountTextField.error = null
                account.length == 11
            }
            signIn.isEnabled = isAccountValid && isPasswordValid
        }
        passwordTextField.editText?.doAfterTextChanged {
            val password = it?.toString()
            isPasswordValid = password.isNullOrEmpty().not()
            signIn.isEnabled = isAccountValid && isPasswordValid
        }

        signIn.onClick {
            val account = accountTextField.editText?.text?.toString()
            val password = passwordTextField.editText?.text?.toString()
            if (account.isNullOrBlank() || account.startsWith("1").not() || account.length != 11) {
                ToastKit.show("Account is incorrect")
                return@onClick
            }
            if (password.isNullOrEmpty()) {
                ToastKit.show("Please fill in password")
                return@onClick
            }
            viewModel.signIn("name" to account, "pwd" to password)
        }

        viewModel.loadState.observe(this, Observer {
            when (it) {
                is LoadState.Start -> {
                    progressBar.show()
                }
                is LoadState.Success -> {
                    Log.d(javaClass.simpleName, "success")
                    ListActivity.startListActivity(this)
                    finish()
                }
                is LoadState.Fail -> {
                    Log.d(javaClass.simpleName, "fail")
                }
                is LoadState.End -> {
                    progressBar.hide()
                }
            }
        })
        viewModel.userData.observe(this, Observer {
            App.user = it
            Log.d(javaClass.simpleName, parseToJson(it))
        })
    }
}

package me.yokeyword.sample.flow.ui.fragment.account

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import me.yokeyword.fragmentation.hideSoftInput
import me.yokeyword.fragmentation.pop
import me.yokeyword.fragmentation.start
import me.yokeyword.sample.R
import me.yokeyword.sample.base.toast
import me.yokeyword.sample.flow.base.BaseBackFragment

/**
 * Created by YoKeyword on 16/2/14.
 */
class LoginFragment : BaseBackFragment() {
    private var mEtAccount: EditText? = null
    private var mEtPassword: EditText? = null
    private var mBtnLogin: Button? = null
    private var mBtnRegister: Button? = null

    private var mOnLoginSuccessListener: OnLoginSuccessListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginSuccessListener) {
            mOnLoginSuccessListener = context
        } else {
            throw RuntimeException("$context must implement OnLoginSuccessListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        initView(view)

        return view
    }

    private fun initView(view: View) {
        val toolbar = view.findViewById<View>(R.id.toolbar) as Toolbar
        mEtAccount = view.findViewById<View>(R.id.et_account) as EditText
        mEtPassword = view.findViewById<View>(R.id.et_password) as EditText
        mBtnLogin = view.findViewById<View>(R.id.btn_login) as Button
        mBtnRegister = view.findViewById<View>(R.id.btn_register) as Button

        toolbar.setTitle(R.string.login)
        initToolbarNav(toolbar)

        mBtnLogin!!.setOnClickListener(View.OnClickListener {
            val strAccount = mEtAccount!!.text.toString()
            val strPassword = mEtPassword!!.text.toString()
            if (TextUtils.isEmpty(strAccount.trim { it <= ' ' })) {
                toast(R.string.error_username)
                return@OnClickListener
            }
            if (TextUtils.isEmpty(strPassword.trim { it <= ' ' })) {
                toast( R.string.error_pwd)
                return@OnClickListener
            }

            // 登录成功
            mOnLoginSuccessListener!!.onLoginSuccess(strAccount)
            pop()
        })

        mBtnRegister!!.setOnClickListener { start(RegisterFragment.newInstance()) }
    }

    override fun onDetach() {
        super.onDetach()
        mOnLoginSuccessListener = null
    }

    interface OnLoginSuccessListener {
        fun onLoginSuccess(account: String)
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        hideSoftInput()
    }

    companion object {

        fun newInstance(): LoginFragment {

            val args = Bundle()

            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

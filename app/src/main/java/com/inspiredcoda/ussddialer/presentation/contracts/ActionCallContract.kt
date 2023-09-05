package com.inspiredcoda.ussddialer.presentation.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract


/**
 * Accepts the phone number or USSD code to be called and return a boolean
 * if successful.
 * */
class ActionCallContract : ActivityResultContract<String, Boolean>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_CALL, Uri.parse("tel:$input"))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }


}
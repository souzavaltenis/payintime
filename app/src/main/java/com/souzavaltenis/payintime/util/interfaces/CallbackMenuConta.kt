package com.souzavaltenis.payintime.util.interfaces

import android.widget.Button

interface CallbackMenuConta {
    fun notifyAction(button: Button, position: Int)
}
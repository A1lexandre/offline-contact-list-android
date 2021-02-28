package com.android.contactlistsqllite.application

import android.app.Application
import com.android.contactlistsqllite.helper.HelperDB

class ContactApplication: Application() {

    lateinit var helperDB: HelperDB

    override fun onCreate() {
        super.onCreate()
        helperDB = HelperDB(this)
        instance = this
    }

    companion object {
        lateinit var instance: ContactApplication
    }

}
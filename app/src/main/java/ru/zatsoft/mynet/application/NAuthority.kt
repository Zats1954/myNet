package ru.zatsoft.mynet.application

import android.app.Application
import ru.zatsoft.mynet.auth.AppAuth

class NAuthority: Application() {
    override fun onCreate(){
        super.onCreate()
        AppAuth.initApp(this)
    }
}
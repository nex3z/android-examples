package com.nex3z.examples.architecturecomponents

import android.app.Application
import com.nex3z.examples.architecturecomponents.data.Repository

class App : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        repository = Repository(applicationContext)
    }

}
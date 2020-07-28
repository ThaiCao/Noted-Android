package com.noted.noted

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.noted.noted.repositories.TaskRepo
import com.noted.noted.utils.AlarmUtils
import com.noted.noted.utils.Extensions
import com.noted.noted.viewmodel.TasksFragmentViewModel
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AppController : Application(){

    private  val notiChannelIds = arrayListOf("alarm")
    override fun onCreate() {
        super.onCreate()
        val extensionsModule = module {
            single { Extensions() }
            single { AlarmUtils() }
            viewModel { TasksFragmentViewModel(get())}
            single { TaskRepo() }
        }
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@AppController)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            modules(listOf(extensionsModule))
        }


        Realm.init(this)

        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiChannelIds.forEach {
                val name = "Noted notification"
                val descriptionText = "Noted notification"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(it, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

        }
    }


}
package com.keralarecipemaster.user.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.keralarecipemaster.user.domain.db.FamousLocationDao

class LocationService(val famousLocationDao: FamousLocationDao) : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val locations = famousLocationDao.getAllFamousLocations()
        /*  withContext(Dispatchers.IO) {
            locations.collect {
            }
        }*/
        Log.d("LocationServiceLogs", "Latitude : $locations.")
        return super.onStartCommand(intent, flags, startId)
    }
}

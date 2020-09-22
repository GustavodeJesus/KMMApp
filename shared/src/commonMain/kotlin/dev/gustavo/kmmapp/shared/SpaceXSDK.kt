package dev.gustavo.kmmapp.shared

import dev.gustavo.kmmapp.shared.cache.Database
import dev.gustavo.kmmapp.shared.cache.DatabaseDriverFactory
import dev.gustavo.kmmapp.shared.entity.RocketLaunch
import dev.gustavo.kmmapp.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()


    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }

}
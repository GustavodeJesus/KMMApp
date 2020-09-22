package dev.gustavo.kmmapp.shared.cache.shared

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import dev.gustavo.kmmapp.shared.cache.AppDatabase
import devgustavokmmappshared.cache.AppDatabaseQueries
import devgustavokmmappshared.cache.Rocket
import devgustavokmmappshared.cache.SelectAllLaunchesInfo
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

internal val KClass<AppDatabase>.schema: SqlDriver.Schema
  get() = AppDatabaseImpl.Schema

internal fun KClass<AppDatabase>.newInstance(driver: SqlDriver): AppDatabase =
    AppDatabaseImpl(driver)

private class AppDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), AppDatabase {
  override val appDatabaseQueries: AppDatabaseQueriesImpl = AppDatabaseQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE Launch (
          |flightNumber INTEGER NOT NULL,
          |missionName TEXT NOT NULL,
          |launchYear INTEGER NOT NULL DEFAULT 0,
          |rocketId TEXT NOT NULL,
          |details TEXT,
          |launchSuccess INTEGER DEFAULT NULL,
          |launchDateUTC TEXT NOT NULL,
          |missionPatchUrl TEXT,
          |articleUrl TEXT
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE Rocket (
          |id TEXT NOT NULL PRIMARY KEY,
          |name TEXT NOT NULL,
          |type TEXT NOT NULL
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class AppDatabaseQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), AppDatabaseQueries {
  internal val selectRocketById: MutableList<Query<*>> = copyOnWriteList()

  internal val selectAllLaunchesInfo: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectRocketById(id: String, mapper: (
    id: String,
    name: String,
    type: String
  ) -> T): Query<T> = SelectRocketByIdQuery(id) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!
    )
  }

  override fun selectRocketById(id: String): Query<Rocket> = selectRocketById(id, ::Rocket)

  override fun <T : Any> selectAllLaunchesInfo(mapper: (
    flightNumber: Long,
    missionName: String,
    launchYear: Int,
    rocketId: String,
    details: String?,
    launchSuccess: Boolean?,
    launchDateUTC: String,
    missionPatchUrl: String?,
    articleUrl: String?,
    id: String?,
    name: String?,
    type: String?
  ) -> T): Query<T> = Query(-1049898817, selectAllLaunchesInfo, driver, "AppDatabase.sq",
      "selectAllLaunchesInfo", """
  |SELECT Launch.*, Rocket.*
  |FROM Launch
  |LEFT JOIN Rocket ON Rocket.id == Launch.rocketId
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt(),
      cursor.getString(3)!!,
      cursor.getString(4),
      cursor.getLong(5)?.let { it == 1L },
      cursor.getString(6)!!,
      cursor.getString(7),
      cursor.getString(8),
      cursor.getString(9),
      cursor.getString(10),
      cursor.getString(11)
    )
  }

  override fun selectAllLaunchesInfo(): Query<SelectAllLaunchesInfo> =
      selectAllLaunchesInfo(::SelectAllLaunchesInfo)

  override fun insertLaunch(
    flightNumber: Long,
    missionName: String,
    launchYear: Int,
    rocketId: String,
    details: String?,
    launchSuccess: Boolean?,
    launchDateUTC: String,
    missionPatchUrl: String?,
    articleUrl: String?
  ) {
    driver.execute(868369985, """
    |INSERT INTO Launch(flightNumber, missionName, launchYear, rocketId, details, launchSuccess, launchDateUTC, missionPatchUrl, articleUrl)
    |VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimMargin(), 9) {
      bindLong(1, flightNumber)
      bindString(2, missionName)
      bindLong(3, launchYear.toLong())
      bindString(4, rocketId)
      bindString(5, details)
      bindLong(6, launchSuccess?.let { if (it) 1L else 0L })
      bindString(7, launchDateUTC)
      bindString(8, missionPatchUrl)
      bindString(9, articleUrl)
    }
    notifyQueries(868369985, {database.appDatabaseQueries.selectAllLaunchesInfo})
  }

  override fun insertRocket(
    id: String,
    name: String,
    type: String
  ) {
    driver.execute(1052535138, """
    |INSERT INTO Rocket(id, name, type)
    |VALUES(?, ?, ?)
    """.trimMargin(), 3) {
      bindString(1, id)
      bindString(2, name)
      bindString(3, type)
    }
    notifyQueries(1052535138, {database.appDatabaseQueries.selectRocketById +
        database.appDatabaseQueries.selectAllLaunchesInfo})
  }

  override fun removeAllLaunches() {
    driver.execute(1732825065, """DELETE FROM Launch""", 0)
    notifyQueries(1732825065, {database.appDatabaseQueries.selectAllLaunchesInfo})
  }

  override fun removeAllRockets() {
    driver.execute(361671383, """DELETE FROM Rocket""", 0)
    notifyQueries(361671383, {database.appDatabaseQueries.selectRocketById +
        database.appDatabaseQueries.selectAllLaunchesInfo})
  }

  private inner class SelectRocketByIdQuery<out T : Any>(
    @JvmField
    val id: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectRocketById, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(1416065751, """
    |SELECT * FROM Rocket
    |WHERE id = ?
    """.trimMargin(), 1) {
      bindString(1, id)
    }

    override fun toString(): String = "AppDatabase.sq:selectRocketById"
  }
}

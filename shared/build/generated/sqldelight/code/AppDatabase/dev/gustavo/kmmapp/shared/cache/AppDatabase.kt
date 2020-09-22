package dev.gustavo.kmmapp.shared.cache

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import dev.gustavo.kmmapp.shared.cache.shared.newInstance
import dev.gustavo.kmmapp.shared.cache.shared.schema
import devgustavokmmappshared.cache.AppDatabaseQueries

interface AppDatabase : Transacter {
  val appDatabaseQueries: AppDatabaseQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = AppDatabase::class.schema

    operator fun invoke(driver: SqlDriver): AppDatabase = AppDatabase::class.newInstance(driver)}
}

package devgustavokmmappshared.cache

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

interface AppDatabaseQueries : Transacter {
  fun <T : Any> selectRocketById(id: String, mapper: (
    id: String,
    name: String,
    type: String
  ) -> T): Query<T>

  fun selectRocketById(id: String): Query<Rocket>

  fun <T : Any> selectAllLaunchesInfo(mapper: (
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
  ) -> T): Query<T>

  fun selectAllLaunchesInfo(): Query<SelectAllLaunchesInfo>

  fun insertLaunch(
    flightNumber: Long,
    missionName: String,
    launchYear: Int,
    rocketId: String,
    details: String?,
    launchSuccess: Boolean?,
    launchDateUTC: String,
    missionPatchUrl: String?,
    articleUrl: String?
  )

  fun insertRocket(
    id: String,
    name: String,
    type: String
  )

  fun removeAllLaunches()

  fun removeAllRockets()
}

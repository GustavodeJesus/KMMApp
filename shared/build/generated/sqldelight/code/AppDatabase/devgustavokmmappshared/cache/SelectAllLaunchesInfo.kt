package devgustavokmmappshared.cache

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

data class SelectAllLaunchesInfo(
  val flightNumber: Long,
  val missionName: String,
  val launchYear: Int,
  val rocketId: String,
  val details: String?,
  val launchSuccess: Boolean?,
  val launchDateUTC: String,
  val missionPatchUrl: String?,
  val articleUrl: String?,
  val id: String?,
  val name: String?,
  val type: String?
) {
  override fun toString(): String = """
  |SelectAllLaunchesInfo [
  |  flightNumber: $flightNumber
  |  missionName: $missionName
  |  launchYear: $launchYear
  |  rocketId: $rocketId
  |  details: $details
  |  launchSuccess: $launchSuccess
  |  launchDateUTC: $launchDateUTC
  |  missionPatchUrl: $missionPatchUrl
  |  articleUrl: $articleUrl
  |  id: $id
  |  name: $name
  |  type: $type
  |]
  """.trimMargin()
}

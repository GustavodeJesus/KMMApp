package devgustavokmmappshared.cache

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

data class Launch(
  val flightNumber: Long,
  val missionName: String,
  val launchYear: Int,
  val rocketId: String,
  val details: String?,
  val launchSuccess: Boolean?,
  val launchDateUTC: String,
  val missionPatchUrl: String?,
  val articleUrl: String?
) {
  override fun toString(): String = """
  |Launch [
  |  flightNumber: $flightNumber
  |  missionName: $missionName
  |  launchYear: $launchYear
  |  rocketId: $rocketId
  |  details: $details
  |  launchSuccess: $launchSuccess
  |  launchDateUTC: $launchDateUTC
  |  missionPatchUrl: $missionPatchUrl
  |  articleUrl: $articleUrl
  |]
  """.trimMargin()
}

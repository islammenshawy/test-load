package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PeakLoad extends Simulation {

  val httpConf = http
    .baseURL("http://www.onol.wip.gidapps.com")

  val scn = scenario("PeakLoad")
    .group("Request") {
      exec(http("search page")
        .get(session => "/browse/search.do?searchText=shirt&"))
    }

  setUp(
    scn.inject(constantUsersPerSec(75) during(5 seconds))
  ).protocols(httpConf)
}
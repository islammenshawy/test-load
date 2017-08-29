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
        .get(session => "/browse/search_br.do?searchText=shirt&" + scala.util.Random.nextInt))
    }

  setUp(
    scn.inject(atOnceUsers(10))
  ).protocols(httpConf)
}
package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PeakLoad extends Simulation {

  val httpConf = http
    .baseURL("http://brm-staging-core-0.brsrvr.com")

  val scn = scenario("PeakLoad")
    .group("Request") {
      exec(http("search page")
        .get(session => "/api/v1/core/?account_id=6063&auth_key=&domain_key=oldnavy&request_id=7839579868130&_br_uid_2=uid=7596393004555:_uid=5727955944386:v=11.8:ts=1480613947786:hc=108&url=www.bloomique.com&ref_url=www.bloomique.com&request_type=search&rows=200&start=0&facet.limit=300&fl=pid,title,brand,price,sale_price,promotions,thumb_image,sku_thumb_images,sku_swatch_images,sku_color_group,url,price_range,sale_price_range,description,is_live,score&fq=sale_price:%5B8+TO+17%5D&realm=staging&br_origin=searchbox&l=red+shirt&q=red+shirts&search_type=keyword&stats.field=sale_price")
        .check(jsonPath("$..numFound").ofType[Int].greaterThan(0)))
    }

  setUp(
    scn.inject(constantUsersPerSec(10) during(60 seconds))
  ).protocols(httpConf)
}
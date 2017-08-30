package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.util.Properties.envOrElse
import scala.concurrent.duration._
import scala.collection.mutable._

class PeakLoad extends Simulation {
  val brandAccountMap = Map("ON" -> "6063","GAP" -> "5468")
  val threadThroughputMap = Map(
    "ON_staging" -> Map("threads" -> "10", "throughput" -> "60"),
    "ON_prod" -> Map("threads" -> "135", "throughput" -> "20"),
    "GAP_staging" -> Map("threads" -> "10", "throughput" -> "60"),
    "GAP_prod" -> Map("threads" -> "10", "throughput" -> "20")
  )

  val baseUrl = envOrElse("TARGET_URL", "http://brm-staging-core-0.brsrvr.com")
  val brand = envOrElse("BRAND_PARAM", "ON")
  val environment = envOrElse("ENV","staging")
  val envBrandCombKey = brand + "_" + environment;

  //Format the http url to add the brand key and param
  val httpConf = http.baseURL(baseUrl)
  var httpUrlNoBase = "/api/v1/core/?account_id=%s&auth_key=&domain_key=oldnavy&request_id=7839579868130&_br_uid_2=uid=7596393004555:_uid=5727955944386:v=11.8:ts=1480613947786:hc=108&url=www.bloomique.com&ref_url=www.bloomique.com&request_type=search&rows=200&start=0&facet.limit=300&fl=pid,title,brand,price,sale_price,promotions,thumb_image,sku_thumb_images,sku_swatch_images,sku_color_group,url,price_range,sale_price_range,description,is_live,score&fq=sale_price:%%5B8+TO+17%%5D&realm=staging&br_origin=searchbox&l=red+shirt&q=red+shirts&search_type=keyword&stats.field=sale_price"
  val httpUrlWithParam = httpUrlNoBase.format(brandAccountMap(brand))

  //Get the paramters for how many threads and throughput for this brand/env combination.
  val threads = threadThroughputMap(envBrandCombKey)("threads").toInt;
  val throughput = threadThroughputMap(envBrandCombKey)("throughput").toInt;


  val scn = scenario("PeakLoad")
    .group("Request") {
      exec(http("search page")
        .get(session => httpUrlWithParam)
        .check(jsonPath("$..numFound").ofType[Int].greaterThan(0)))
    }

  setUp(
    scn.inject(constantUsersPerSec(threads) during(throughput seconds))
  ).protocols(httpConf)
}
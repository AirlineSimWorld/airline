package com.patson.model
import FlightType._

/**
 * Cost base model
 */
object Pricing {
  //base 100
  //200 km = 100 + 50
  //1000 km = 150 + 100 = 250  (800 * 0.125) 
  //2000 km = 250 + 100 = 350  (1000 * 0.1)
  //10000 km = 350 + 400 = 750 (8000 * 0.05)
  val modifierBrackets = List((200, 0.25),(800, 0.125),(1000, 0.1),(Int.MaxValue, 0.05))
  val INTERNATIONAL_PRICE_MULTIPLIER = 1.25
  val INTERCONTINENTAL_PRICE_MULTIPLIER = 2
  
  def computeStandardPrice(link : Link, linkClass : LinkClass) : Int = {
    computeStandardPrice(link.distance, Computation.getFlightType(link.from, link.to), linkClass)
  }
  def computeStandardPrice(distance : Int, flightType : FlightType, linkClass : LinkClass) : Int = {
    var remainDistance = distance
    var price = 100.0
    for (priceBracket <- modifierBrackets if(remainDistance > 0)) {
      if (priceBracket._1 >= remainDistance) {
        price += remainDistance * priceBracket._2
      } else {
        price += priceBracket._1 * priceBracket._2
      }
      remainDistance -= priceBracket._1
    }
    ((flightType match {
      case SHORT_HAUL_INTERNATIONAL | LONG_HAUL_INTERNATIONAL => (price * INTERNATIONAL_PRICE_MULTIPLIER)
      case SHORT_HAUL_INTERCONTINENTAL | LONG_HAUL_INTERCONTINENTAL | ULTRA_LONG_HAUL_INTERCONTINENTAL => (price * INTERCONTINENTAL_PRICE_MULTIPLIER)
      case _ => price
    }) * linkClass.priceMultiplier).toInt
    
  }
}
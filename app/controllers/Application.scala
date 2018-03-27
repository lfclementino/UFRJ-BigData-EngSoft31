package controllers

import play.api._
import play.api.mvc._
import javax.inject._
import play.api.routing._
import play.api.libs.json._
import play.api.Play.current
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.types._
import org.apache.spark.sql._
import org.apache.spark.sql.types.IntegerType

import models._

@Singleton
class Application @Inject() extends Controller {

   val spark = SparkSession.builder()
		  .master("local")
		  .appName("Fifa18")		  
		  .getOrCreate()
   import spark.implicits._
   val df = spark.read.option("header","true").option("treatEmptyValuesAsNulls", "true").csv("/home/bigdata/Downloads/app/public/fifa18.csv")


	df.createOrReplaceTempView("jogadores");

	val jogadorDf = spark.sql("SELECT player_id as id, name, club, club_logo, flag, nationality, photo, league, age, CAST(eur_value as int), CAST(eur_release_clause as int), CAST(overall as int), CAST(potential as int)  FROM jogadores Order by overall Desc, eur_value Desc LIMIT 20");

  
   
  def index = Action {
        var listaLocal : scala.collection.mutable.Buffer[Jogador] = listaJogadoresSpark()

    	Ok(views.html.index(listaLocal))
  }
    
  def ajaxCall = Action { implicit request =>
	  var listaLocal = listaJogadoresSpark()
	  	  
	  val jsArr: JsValue = Json.toJson(listaLocal)	  
	  Ok(jsArr)
  }
  
  def player(id: String) = Action { implicit request =>
	var listaLocalID : scala.collection.mutable.Buffer[Jogador] = listaJogadorIDSpark(id)
	println(id)
	println(listaLocalID.length)
	Ok(views.html.player("Player ID: " + id,listaLocalID))
  
  }
  
  def search(name: String) = Action { implicit request =>
	var listaLocalSearch : scala.collection.mutable.Buffer[Jogador] = searchJogadorNameSpark(name)
	println(name)
	println(listaLocalSearch.length)
	Ok(views.html.search("Search for: " + name,listaLocalSearch))
  
  }

  def javascriptRoutes = Action { implicit request =>
	  import routes.javascript._
	  Ok(
		JavaScriptReverseRouter("jsRoutes")(
		  routes.javascript.Application.ajaxCall
		)
	  ).as("text/javascript")
  }
  
    private def listaJogadoresSpark() : scala.collection.mutable.Buffer[Jogador] = {
	val listaIni = scala.collection.mutable.Buffer[Jogador]()

    jogadorDf.collect().foreach(result => {
		if(result(10) != null) {
		listaIni += new Jogador(result.getString(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getInt(9), result.getInt(10), result.getInt(11), result.getInt(12))
	}})	
	listaIni
  }
 

  private def listaJogadorIDSpark(id: String) : scala.collection.mutable.Buffer[Jogador] = {
	val listaIni = scala.collection.mutable.Buffer[Jogador]()
	 
	val jogadorIdDf = spark.sql(s"SELECT player_id as id, name, club, club_logo, flag, nationality, photo, league, age, CAST(eur_value as int), CAST(eur_release_clause as int), CAST(overall as int), CAST(potential as int)  FROM jogadores WHERE player_id = '$id' Order by overall Desc, eur_value")
	
    jogadorIdDf.collect().foreach(result => {
		listaIni += new Jogador(result.getString(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getInt(9), result.getInt(10), result.getInt(11), result.getInt(12))
	})	
	listaIni
  }

  private def searchJogadorNameSpark(name: String) : scala.collection.mutable.Buffer[Jogador] = {
	val listaIni = scala.collection.mutable.Buffer[Jogador]()
	 
	val jogadorIdDf = spark.sql(s"SELECT player_id as id, name, club, club_logo, flag, nationality, photo, league, age, CAST(eur_value as int), CAST(eur_release_clause as int), CAST(overall as int), CAST(potential as int)  FROM jogadores WHERE LOWER(name) like LOWER('%$name%') Order by overall Desc, eur_value Desc")
	
    jogadorIdDf.collect().foreach(result => {
		if(result(10) != null) {
		listaIni += new Jogador(result.getString(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7), result.getString(8), result.getInt(9), result.getInt(10), result.getInt(11), result.getInt(12))
	}})	
	listaIni
  }
 }

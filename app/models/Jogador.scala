package models

import play.api._
import play.api.libs.json._
import play.api.Play.current

class Jogador(val _id: String, val _name: String, val _club: String, val _club_logo: String, val _flag: String, val _nationality: String, val _photo: String, val _league: String, val _age: String, val _eur_value: Int, val _eur_release_clause: Int, val _overall: Int, val _potential: Int) {
   var id: String = _id
   var name: String = _name
   var club: String = _club
   var club_logo: String = _club_logo
   var flag: String = _flag
   var nationality: String = _nationality
   var photo: String = _photo
   var league: String = _league
   var age: String = _age
   var eur_value: Int = _eur_value
   var eur_release_clause: Int = _eur_release_clause
   var overall: Int = _overall
   var potential: Int = _potential
}
/*

"ID", "name", "club", "club_logo", "flag", "nationality", "photo", "league", "age", "eur_value", "eur_release_clause", "overall", "potential"
*/
object Jogador {
    
    implicit object JogadorFormat extends Format[Jogador] {

        // convert from jogador object to JSON (serializing to JSON)
        def writes(jogador: Jogador): JsValue = {
            
            val jogadorSeq = Seq(
		"Id" -> JsString(jogador.id),
                "Name" -> JsString(jogador.name),
                "Club" -> JsString(jogador.club),
                "ClubLogo" -> JsString(jogador.club_logo),
		"Flag" -> JsString(jogador.flag),
		"Nationality" -> JsString(jogador.nationality),
		"Photo" -> JsString(jogador.photo),
		"League" -> JsString(jogador.league),
		"Age" -> JsString(jogador.age),
		"Eur_value" -> JsNumber(jogador.eur_value),
		"Eur_release" -> JsNumber(jogador.eur_release_clause),
		"Overall" -> JsNumber(jogador.overall),
		"Potential" -> JsNumber(jogador.potential)
            )

            JsObject(jogadorSeq)

        }
		
	// convert from JSON string to a Localizacao object (de-serializing from JSON)
        // (i don't need this method; just here to satisfy the api)
        def reads(json: JsValue): JsResult[Jogador] = {
            JsSuccess(new Jogador("","","","","","","","","",0,0,0,0))
        }

    }

}

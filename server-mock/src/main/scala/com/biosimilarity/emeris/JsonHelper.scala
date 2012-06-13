package com.biosimilarity.emeris


import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import AgentDataSet._
import net.model3.chrono.DateTime
import net.model3.chrono.DateFormatter

object JsonHelper {
  
  val dateFormatter = new DateFormatter("yyyy-MM-dd HH:mm:ss");

  trait JsonCapable {
    def jsonClass = {
      val classname = getClass.getName
      classname.substring(classname.lastIndexOf("$")+1)
    }
    def asJValue: JValue = decompose(this)
    def asJsonStr: String = pretty(render(asJValue))
  }
    
  val typeHints  = XTypeHints(SocketProtocol.jsonTypes ++ AgentDataSet.jsonTypes)

  implicit val formats = Serialization.formats(typeHints) + UidSerializer + DateTimeSerializer

	object UidSerializer extends Serializer[Uid] {
        private val UidClass = classOf[Uid]

        def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Uid] = {
          case (TypeInfo(UidClass, _), json) => json match {
            case JString(value) => Uid(value)
            case x => throw new MappingException("Can't convert " + x + " to Uid")
          }
        }

        def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
          case x: Uid => JString(x.value)
        }
    }

    object DateTimeSerializer extends Serializer[DateTime] {
      
        private val UidClass = classOf[DateTime]

        def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DateTime] = {
          case (TypeInfo(UidClass, _), json) => json match {
            case JString(value) => dateFormatter.parse(value)
            case x => throw new MappingException("Can't convert " + x + " to DateTime")
          }
        }

        def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
          case x: DateTime => 
            JString(
                dateFormatter.format(x)
                )
        }
    }
	
	case class XTypeHints(hints: List[Class[_]]) extends TypeHints {
		def hintFor(clazz: Class[_]) = clazz.getName.substring(clazz.getName.lastIndexOf("$")+1)
		def classFor(hint: String) = hints find (hintFor(_) == hint)
	}
	
	def parseJson[T](json: String)(implicit mf: Manifest[T]) = {
		import net.liftweb.json._
		import net.liftweb.json.JsonDSL._
		parse(json).extract[T]
	} 
	
	def decompose(a: Any) = Extraction.decompose(a)

}
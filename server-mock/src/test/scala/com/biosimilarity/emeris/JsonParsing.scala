package com.biosimilarity.emeris

object JsonParsing extends App {

  
  val m2 = SocketProtocol.parseJson("""
      {
  "jsonClass":"Message","body":{
    "jsonClass":"CreateNodes","nodes":[
      {
        "jsonClass":"Link","to":"9jeiQZbk9MYbKlU5SVRfpoZ6I8wfktJ8","from":"l6gOng8sMqndNbY743bGig4brx3Pzf3P","created":"2012-05-01 12:39:36","uid":"w42U10GLoMs7gDt5XbNgX2DzuET6p7GA"
      }
    ]
  },"sender":"UakWfcfpKI45l7hT5Qek0T7aWyW3vKIj","uid":"cXtsumb5LIEM4wAPKQJY0MDGHCSYwkxT"
}
""")

	println(m2)
  
}
package client


object GetPutClient extends BaseClientApp {

  lazy val getMessage = """
{ "headers" : [
    ":clientUri:"
    , "agent://localhost/kvdbDispatchStore2"
    , "f5e9e43b-c590-4285-a66c-96faef286aa3"
    , "e64c44b5-dfff-4a0e-8e39-49458fd99683"
    , { "response" : null }
  ]
  , "body" : { 
    "getRequest" : { 
      "ask" : {
        "node" : [{ "machine" : ["sl390"] }, { "os" : ["Ubuntu","11.04"] } ] 
      } 
    } 
  } 
}
"""    

  lazy val putMessage = """
{ "headers" : [
    ":clientUri:"
    , "agent://localhost/kvdbDispatchStore2"
    , "f5e9e43b-c590-4285-a66c-96faef286aa3"
    , "e64c44b5-dfff-4a0e-8e39-49458fd99683"
    , { "response" : null }
  ]
  , "body" : { 
    "putRequest" : { 
      "tell" : [ { "node" : [{ "machine" : ["sl390"] }, { "os" : ["Ubuntu","11.04"] } ] }, "running" ] 
    } 
  } 
}
"""  

  def sendit(msg: String) = rawSend(msg.replace(":clientUri:",clientUri))

  def run = {
    (1 to 5) foreach { i =>
      sendit(getMessage)
      sendit(putMessage)
    }
    val waiter = new Object
    waiter.synchronized {
    	waiter.wait(999999)
    }
    // sendPut ("helloWorld", "The Hello World Value")
    // sendGet("helloWorld")
  }

}

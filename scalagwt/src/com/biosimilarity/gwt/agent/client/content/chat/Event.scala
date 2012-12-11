/*
 * Copyright 2012 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.biosimilarity.gwt.agent.client.content.chat

import java.io.Serializable
import java.util.Date

/**
 * @author p.havelaar
 */
class Event(
  val src : String,
  val author : String,
  val message : String
  //val time : Date
) extends Serializable {
  override def toString() : String = {
    (
      "{"
      + "event" + ":"
      + "{"
      + "src" + ":" + src 
      + ","
      + "author" + ":" + author
      + ","
      + "message" + ":" + message
      + "}"
      + "}"
    )
  }
}

object Event {
  def apply(
    src : String, author : String, message : String //, time : Date
  ) : Event = {
    new Event(
      src, author, message //, time
    )
  }
  // def apply( author : String, message : String ) : Event = {
//     new Event( author, message, new Date() )
//   }
}

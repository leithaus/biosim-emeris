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
package com.biosimilarity.iacontainer.client

import org.atmosphere.gwt.server.AtmosphereGwtHandler
import org.atmosphere.gwt.server.GwtAtmosphereResource

import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import java.io.IOException
import java.io.Serializable
import java.util.List
import java.util.logging.Level
import java.util.logging.Logger
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.BroadcasterFactory

/**
 * @author p.havelaar
 */
class ChatHandler()
extends AtmosphereGwtHandler {
  override def init( servletConfig : ServletConfig ) : Unit = {
    super.init(servletConfig);
    Logger.getLogger("").setLevel(Level.INFO);
    Logger.getLogger("org.atmosphere.gwt").setLevel(Level.ALL);
    Logger.getLogger("org.atmosphere.samples").setLevel(Level.ALL);
    Logger.getLogger("").getHandlers()( 0 ).setLevel(Level.ALL);
    logger.trace("Updated logging levels");
  }

  override def doComet( resource : GwtAtmosphereResource ) : Int = {
    val room : String = resource.getRequest().getPathInfo()
    val broadcaster : Broadcaster =
      BroadcasterFactory.getDefault().lookup( room, true )
    resource.getAtmosphereResource().setBroadcaster( broadcaster )
    300000
  }

  override def cometTerminated(
    cometResponse : GwtAtmosphereResource,
    serverInitiated : Boolean
  ) : Unit = {
    super.cometTerminated( cometResponse, serverInitiated )
    logger.info( "Comet disconnected" )
  }

  override def doPost(
    postRequest : HttpServletRequest,
    postResponse : HttpServletResponse,
    messages : List[_],
    cometResource : GwtAtmosphereResource
  ) : Unit = {
    broadcast( messages, cometResource );
  }
}

package com.biosimilarity.emeris

import m3.gwt.lang.DateFormatter
import net.model3.text.ThreadSafeSimpleDateFormat
import java.util.Date
import biosim.client.utils.BiosimSerializer

object BiosimServerSerializer extends BiosimSerializer {
	
  setDateFormatter(new DateFormatter {
    val formatter = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def parse(s: String) = formatter.parse(s)
	def format(d: Date) = formatter.format(d)
  });
  
}


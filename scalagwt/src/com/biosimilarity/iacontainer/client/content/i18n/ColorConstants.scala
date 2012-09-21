/*
 * Copyright 2008 Google Inc.
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
package com.biosimilarity.iacontainer.client.content.i18n

import com.google.gwt.i18n.client.ConstantsWithLookup

/**
 * Internationalized constants used to demonstrate {@link ConstantsWithLookup}.
 */
trait ColorConstants extends ConstantsWithLookup {
  def black(): String

  def blue(): String

  def green(): String

  def grey(): String

  def lightGrey(): String

  def red(): String

  def white(): String

  def yellow(): String
}
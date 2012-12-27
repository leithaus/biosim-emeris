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
package com.biosimilarity.gwt.agent.client

import com.google.gwt.i18n.client.Constants
import com.biosimilarity.gwt.agent.client.content.chat.CwChatInputText
import com.biosimilarity.gwt.agent.client.content.i18n.CwConstantsExample
import com.biosimilarity.gwt.agent.client.content.i18n.CwConstantsWithLookupExample
import com.biosimilarity.gwt.agent.client.content.i18n.CwDateTimeFormat
import com.biosimilarity.gwt.agent.client.content.i18n.CwDictionaryExample
import com.biosimilarity.gwt.agent.client.content.i18n.CwMessagesExample
import com.biosimilarity.gwt.agent.client.content.i18n.CwNumberFormat
import com.biosimilarity.gwt.agent.client.content.i18n.CwPluralFormsExample
import com.biosimilarity.gwt.agent.client.content.lists.CwListBox
import com.biosimilarity.gwt.agent.client.content.lists.CwMenuBar
import com.biosimilarity.gwt.agent.client.content.lists.CwStackPanel
import com.biosimilarity.gwt.agent.client.content.lists.CwSuggestBox
import com.biosimilarity.gwt.agent.client.content.lists.CwTree
import com.biosimilarity.gwt.agent.client.content.other.CwAnimation
import com.biosimilarity.gwt.agent.client.content.other.CwCookies
import com.biosimilarity.gwt.agent.client.content.other.CwFrame
import com.biosimilarity.gwt.agent.client.content.panels.CwAbsolutePanel
import com.biosimilarity.gwt.agent.client.content.panels.CwDecoratorPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwDisclosurePanel
import com.biosimilarity.gwt.agent.client.content.panels.CwDockPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwFlowPanel
//import com.biosimilarity.gwt.agent.client.content.chat.CwPhloPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwHorizontalPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwHorizontalSplitPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwTabPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwVerticalPanel
import com.biosimilarity.gwt.agent.client.content.panels.CwVerticalSplitPanel
import com.biosimilarity.gwt.agent.client.content.popups.CwBasicPopup
import com.biosimilarity.gwt.agent.client.content.popups.CwDialogBox
import com.biosimilarity.gwt.agent.client.content.tables.CwFlexTable
import com.biosimilarity.gwt.agent.client.content.tables.CwGrid
import com.biosimilarity.gwt.agent.client.content.text.CwBasicText
import com.biosimilarity.gwt.agent.client.content.text.CwRichText
import com.biosimilarity.gwt.agent.client.content.widgets.CwBasicButton
import com.biosimilarity.gwt.agent.client.content.widgets.CwCheckBox
import com.biosimilarity.gwt.agent.client.content.widgets.CwCustomButton
import com.biosimilarity.gwt.agent.client.content.widgets.CwDatePicker
import com.biosimilarity.gwt.agent.client.content.widgets.CwFileUpload
import com.biosimilarity.gwt.agent.client.content.widgets.CwHyperlink
import com.biosimilarity.gwt.agent.client.content.widgets.CwRadioButton

object ShowcaseConstants {
  /**
   * The path to source code for examples, raw files, and style definitions.
   */
  val DST_SOURCE = "gwtShowcaseSource/"

  /**
   * The destination folder for parsed source code from Showcase examples.
   */
  val DST_SOURCE_EXAMPLE = DST_SOURCE + "java/"

  /**
   * The destination folder for raw files that are included in entirety.
   */
  val DST_SOURCE_RAW = DST_SOURCE + "raw/"

  /**
   * The destination folder for parsed CSS styles used in Showcase examples.
   */
  val DST_SOURCE_STYLE = DST_SOURCE + "css/"

  /**
   * Link to GWT homepage.
   */
  //val GWT_HOMEPAGE = "http://code.google.com/webtoolkit/"
  val GWT_HOMEPAGE = "http://qualityofid.com/"

  /**
   * Link to GWT examples page.
   */
  //val GWT_EXAMPLES = GWT_HOMEPAGE + "examples/"
  val GWT_EXAMPLES = GWT_HOMEPAGE + "principles/"

  /**
   * The available style themes that the user can select.
   */
  val STYLE_THEMES: Array[String] = Array("standard", "chrome", "dark")
}

/**
 * Constants used throughout the showcase.
 */
trait ShowcaseConstants extends Constants with
    ContentWidget.CwConstants with CwChatInputText.CwConstants with CwCheckBox.CwConstants with
    CwRadioButton.CwConstants with CwBasicButton.CwConstants with
    CwCustomButton.CwConstants with CwListBox.CwConstants with
    CwSuggestBox.CwConstants with CwTree.CwConstants with CwMenuBar.CwConstants with
    CwFlowPanel.CwConstants /* with CwPhloPanel.CwConstants */ with CwDisclosurePanel.CwConstants with
    CwTabPanel.CwConstants with CwDockPanel.CwConstants with
    CwHorizontalPanel.CwConstants with CwHorizontalSplitPanel.CwConstants with
    CwVerticalPanel.CwConstants with CwVerticalSplitPanel.CwConstants with
    CwBasicPopup.CwConstants with CwDialogBox.CwConstants with CwGrid.CwConstants with
    CwFlexTable.CwConstants with CwBasicText.CwConstants with CwRichText.CwConstants with
    CwFileUpload.CwConstants with CwAbsolutePanel.CwConstants with
    CwHyperlink.CwConstants with CwFrame.CwConstants with CwStackPanel.CwConstants with
    CwCookies.CwConstants with CwNumberFormat.CwConstants with
    CwDateTimeFormat.CwConstants with CwMessagesExample.CwConstants with
    CwConstantsExample.CwConstants with CwConstantsWithLookupExample.CwConstants with
    CwDictionaryExample.CwConstants with CwDecoratorPanel.CwConstants with
    CwAnimation.CwConstants with CwDatePicker.CwConstants with
    CwPluralFormsExample.CwConstants {

  def categoryI18N(): String

  def categoryLists(): String

  def categoryOther(): String

  def categoryPanels(): String

  def categoryPopups(): String

  def categoryTables(): String

  def categoryTextInput(): String

  def categoryWidgets(): String

  def categoryAgent() : String

  /**
   * @return text for the link to more examples
   */
  def mainLinkExamples(): String

  /**
   * @return text for the link to the GWT homepage
   */
  def mainLinkHomepage(): String

  /**
   * @return the title of the main menu
   */
  def mainMenuTitle(): String

  /**
   * @return the sub title of the application
   */
  def mainSubTitle(): String

  /**
   * @return the title of the application
   */
  def mainTitle(): String
}

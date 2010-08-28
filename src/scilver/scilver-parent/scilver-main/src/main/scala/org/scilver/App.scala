package org.scilver

import swing.{SimpleSwingApplication, MainFrame, Button}

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object App extends SimpleSwingApplication {
  override def top = new MainFrame {
    title = "Hello!"

    contents = new Button {
      text = "click me"
    }
  }
}

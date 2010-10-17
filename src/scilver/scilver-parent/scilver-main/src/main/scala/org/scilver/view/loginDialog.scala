package org.scilver.view

import org.scilver.{Authentication, tr, App}

/**
 * @author eav
 * Date: 17.10.2010
 * Time: 15:59:13
 */
private class LoginDialog(loginCallback: String => Option[Authentication]) extends CallbackFromStringDialog(loginCallback) {
  title = App.title

  protected def headTitle = tr("Please enter your user name")

  override protected def onCancel = App.shutdown
}

object loginDialog {
  def apply(callback: String => Option[Authentication]) = {
    val ld = new LoginDialog(callback)
    ld.visible = true
    ld.getResult
  }
}

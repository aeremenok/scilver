package org.scilver.view

import org.scilver.{Authentication, tr, App}
import java.awt.Dimension

/**
 * @author eav
 * Date: 17.10.2010
 * Time: 15:59:13
 */
object loginDialogs {
  def requestLogin(callback: String => Option[Authentication]) = showDialog(tr("Please enter your user name"), callback)

  def requestPin(callback: String => Option[Authentication]) = showDialog(tr("Please enter pin"), callback)

  private def showDialog(title: String, callback: String => Option[Authentication]) = {
    val dialog = new ScilverLoginDialog(title, callback)
    dialog.visible = true
    dialog.getResult
  }
}

private class ScilverLoginDialog(val headTitle: String,
                                 loginCallback: String => Option[Authentication])
        extends CallbackFromStringDialog(loginCallback) {
  title = App.title

  override protected def onCancel = {
    super.onCancel
    App.shutdown
  }

  minimumSize = new Dimension(250, 100)
}

package org.scilver.view

import org.scilver.{tr, App, Authentication}

/**
 * @author eav
 * Date: 17.10.2010
 * Time: 20:44:20
 */

private class PinDialog(pinCallback: String => Option[Authentication]) extends CallbackFromStringDialog(pinCallback) {
  title = App.title

  protected def headTitle = tr("Please enter pin")

  override protected def onCancel = App.shutdown
}

object pinDialog {
  def apply(callback: String => Option[Authentication]) = {
    val pd = new PinDialog(callback)
    pd.visible = true
    pd.getResult
  }
}

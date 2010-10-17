package org.scilver.view

import org.scilver.tr
import swing._
import FlowPanel.Alignment.Right
import BorderPanel.Position._

/**
 * @author eav
 * Date: 17.10.2010
 * Time: 20:02:01
 */

abstract class CallbackDialog[F, T](heavyLogic: F => T) extends Dialog(null) {
  modal = true
  
  private var result: T = _

  protected def userInput: Option[F]

  protected def busyLabel: BusyLabel

  protected implicit def buttonOf(a: Action) = new Button(a)

  protected def onOk = userInput match {
    case Some(i) =>
      okAction.enabled = false
      cancelAction.enabled = false
      busyLabel.execute(heavyLogic(i), {
        calculated: T =>
          result = calculated; visible = false
      })

    case None => onCancel
  }

  protected def onCancel {visible = false}

  private object okAction extends Action(tr("OK")) {
    def apply() = onOk
  }

  private object cancelAction extends Action(tr("Cancel")) {
    def apply() = onCancel
  }

  protected val buttonPanel = new FlowPanel(Right)(okAction, cancelAction)

  def getResult = result
}

abstract class CallbackFromStringDialog[T](heavyLogic: String => T) extends CallbackDialog(heavyLogic) {
  private val nameField = new TextField
  protected val busyLabel = new BusyLabel(25)

  contents = new BorderPanel {
    add(new Label(headTitle), North)
    add(nameField, Center)
    add(Component.wrap(busyLabel), East)
    add(buttonPanel, South)
  }

  protected def userInput = nameField.text match {
    case null => None
    case "" => None
    case name: String => Some(name)
  }

  override protected def onOk = {
    nameField.enabled = false
    super.onOk
  }

  protected def headTitle: String

  pack
  centerOnScreen
}

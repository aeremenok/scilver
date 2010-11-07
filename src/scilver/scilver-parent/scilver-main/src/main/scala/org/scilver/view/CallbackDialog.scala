package org.scilver.view

import org.scilver.tr
import swing._
import FlowPanel.Alignment.Right
import BorderPanel.Position._
import java.awt.event.{KeyEvent, KeyAdapter}
import javax.swing.{BoxLayout, JPanel, Box, JComponent}
import javax.swing.event.{DocumentEvent, DocumentListener}

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

  protected object okAction extends Action(tr("OK")) {
    def apply() = onOk
  }

  protected object cancelAction extends Action(tr("Cancel")) {
    def apply() = onCancel
  }

  protected val buttonPanel = new FlowPanel(Right)(okAction, cancelAction)

  def getResult = result
}

abstract class CallbackFromStringDialog[T](heavyLogic: String => T) extends CallbackDialog(heavyLogic) {
  implicit def fromSwing(c: JComponent) = Component.wrap(c)

  private val textField = new TextField
  protected val busyLabel = new BusyLabel(25)

  contents = new BorderPanel {
    add(new Label(headTitle), North)
    add(new JPanel() {
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS))

      add(Box.createHorizontalStrut(10))
      add(textField.peer)
      add(Box.createHorizontalStrut(10))
    }, Center)
    add(buttonPanel, South)

    add(busyLabel, East)
  }

  protected def userInput = textField.text match {
    case null => None
    case "" => None
    case name: String => Some(name)
  }

  override protected def onOk = {
    textField.enabled = false
    super.onOk
  }

  protected def headTitle: String

  textField.peer.addKeyListener(new KeyAdapter {
    override def keyPressed(e: KeyEvent) = e.getKeyCode match {
      case KeyEvent.VK_ENTER => onOk
      case KeyEvent.VK_ESCAPE => onCancel
      case _ => {}
    }
  })

  okAction.enabled = false
  textField.peer.getDocument.addDocumentListener(new DocumentListener {
    def changedUpdate(e: DocumentEvent) = toggle

    def removeUpdate(e: DocumentEvent) = toggle

    def insertUpdate(e: DocumentEvent) = toggle

    private def toggle {okAction.enabled = (userInput != None)}
  })

  pack
  centerOnScreen
}

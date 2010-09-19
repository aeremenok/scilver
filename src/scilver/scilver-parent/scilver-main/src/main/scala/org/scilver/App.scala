package org.scilver

import scala.swing._
import javax.swing.UIManager

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object App extends SwingApplication {
  val version = "0.1";
  val title = "Scilver v." + version;
  lazy val auth = authentication.login

  override def startup(args: Array[String]) {
    initLaf
    mainFrame.visible = true
  }

  def initLaf =
    if (!setLaf("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"))
      setLaf(UIManager.getSystemLookAndFeelClassName)

  def setLaf(laf: String): Boolean =
    try {
      UIManager.setLookAndFeel(laf); true
    }
    catch {
      case _ => false
    }
}

object mainFrame extends MainFrame {
  title = App.title

  contents = new BorderPanel {
    import BorderPanel.Position._
    add(toolBar, North)
    add(new Button("Center"), Center)
  }

  maximize
}

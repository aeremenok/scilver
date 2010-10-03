package org.scilver

import db.HibernateConnector
import scala.swing._
import javax.swing.UIManager
import org.jdesktop.swingx.JXPanel
import java.awt.Color
import org.jdesktop.swingx.painter._
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object App extends SwingApplication {
  val version = "0.1";
  val title = "Scilver v." + version;
  private var auth: Authentication = _

  override def startup(args: Array[String]) {
    log.init
    initLaf

    authentication.login match {
      case a: Some[Authentication] => {
        auth = a.get
        mainFrame.visible = true
      }
      case _ => shutdown
    }
  }

  override def shutdown() {
    log.debug("closing the app")
    HibernateConnector.shutdown
  }

  def user = auth.user

  def initLaf =
    if (!setLaf("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"))
      setLaf(UIManager.getSystemLookAndFeelClassName)

  def setLaf(laf: String): Boolean =
    try {
      UIManager.setLookAndFeel(laf);
      true
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
    add(Component.wrap(new MyPanel), Center)
  }

  maximize
}

class MyPanel extends JXPanel {
  setBackgroundPainter(createPainter)

  def createPainter = {
    val rp1 = new RectanglePainter(20, 20, 20, 20, 20, 20);
    rp1.setFillPaint(Color.ORANGE);
    rp1.setBorderPaint(Color.ORANGE.darker);
    rp1.setStyle(AbstractAreaPainter.Style.BOTH);
    rp1.setBorderWidth(5);
    rp1.setAntialiasing(true);
    new CompoundPainter(rp1, new GlossPainter());
  }
}

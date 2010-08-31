package org.scilver

import scala.swing._
import org.jdesktop.swingx.JXLoginPane
import org.jdesktop.swingx.auth.{LoginEvent, LoginAdapter}
import javax.swing.{UIManager, JToolBar}

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object App extends SwingApplication {
  val version = "0.1";
  val title = "Scilver v." + version;

  def initLaf {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")
    }
    catch {
      case _ =>
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
        }
        catch {
          case _ => {}
        }
    }
  }

  override def startup(args: Array[String]) {
    initLaf

    authentication.addLoginListener(new LoginAdapter {
      override def loginSucceeded(source: LoginEvent) {
        mainFrame.visible = true
      }
    })

    val lf = JXLoginPane.showLoginFrame(authentication)
    lf.setTitle(title)
    lf.setVisible(true)
  }
}

object mainFrame extends MainFrame {
  title = App.title

  contents = new BorderPanel {
    import BorderPanel.Position._
    add(Component.wrap(toolBar), North)
    add(new Button("Center"), Center)
  }

  maximize
}

object toolBar extends JToolBar {
  def add(a: Action) = super.add(a.peer)

  add(postAction)
}

object postAction extends Action(i18n.tr("Post")) {
  def apply() {
    println("post")
  }
}

object i18n {
  def tr(text: String) = text // todo
}

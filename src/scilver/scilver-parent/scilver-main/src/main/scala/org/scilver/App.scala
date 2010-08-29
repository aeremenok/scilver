package org.scilver

import scala.swing._
import org.jdesktop.swingx.JXLoginPane
import org.jdesktop.swingx.auth.LoginService
import java.lang.String
import twitter4j.Twitter

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object App extends SimpleSwingApplication {
  val version = "0.1";
  override def top = new MainFrame {
    title = "Scilver v." + version;
    contents = Component.wrap(new JXLoginPane(twitterLoginService))
    centerOnScreen
  }
}

object mainPanel extends BorderPanel {
  import BorderPanel.Position._
  add(new Button("North"), North)
  add(new Button("Center"), Center)
}

object twitterLoginService extends LoginService {
  def authenticate(login: String, pass: Array[Char], server: String) =
    new Twitter(login, new String(pass)).test
}

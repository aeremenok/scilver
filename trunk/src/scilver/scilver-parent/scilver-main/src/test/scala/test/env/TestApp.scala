package test.env

import org.scilver.{authentication, mainFrame, App}
import twitter4j.{User, Twitter}

/**
 * @author eav
 * Date: 05.09.2010
 * Time: 12:09:58
 */

object TestApp
{
  def startup(twitter: Twitter, user: User) {
    App.initLaf
    authentication.twitter = twitter
    authentication.user = user
    mainFrame.visible = true
  }
}

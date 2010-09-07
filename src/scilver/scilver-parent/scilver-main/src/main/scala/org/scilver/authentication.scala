package org.scilver

import twitter4j.{TwitterException, User, TwitterFactory, Twitter}

import twitter4j.http.AccessToken
import java.awt.Desktop
import java.net.URI
import swing.Dialog

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object authentication {
  def login = {
    val twitter = new TwitterFactory().getInstance();
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")
    enterPin(twitter)
  }

  private def enterPin(twitter: Twitter): Credentials = {
    val request = twitter.getOAuthRequestToken
    Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))

    Dialog.showInput(message = i18n.tr("Enter pin"), initial = "") match {
      case None => enterPin(twitter)
      case pin: Some[String] =>
        try {
          val accessToken = twitter.getOAuthAccessToken(request, pin.get)
          val user = twitter.verifyCredentials
          Credentials(null, user, accessToken)
        }
        catch {
          case e: TwitterException => {
            println(e.getMessage)
            enterPin(twitter)
          }
        }
    }
  }
}

case class Credentials(twitter: Twitter, user: User, accessToken: AccessToken)









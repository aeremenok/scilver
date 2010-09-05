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
  var twitter: Twitter = null
  var user: User = null
  var accessToken: AccessToken = null

  def authenticate: Boolean =
  {
    twitter = new TwitterFactory().getInstance();
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")

    val request = twitter.getOAuthRequestToken
    Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))

    val pin = Dialog.showInput(message = i18n.tr("Enter pin"), initial = "")

    user = null
    try {
      accessToken = twitter.getOAuthAccessToken(request, pin.get)
      user = twitter.verifyCredentials
    }
    catch {
      case e: TwitterException => println(e.getMessage)
    }
    user != null
  }
}










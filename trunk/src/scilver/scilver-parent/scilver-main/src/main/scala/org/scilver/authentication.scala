package org.scilver

import java.lang.String
import twitter4j.{TwitterFactory, Twitter}
import twitter4j.http.AuthorizationFactory;
import org.jdesktop.swingx.auth.LoginService

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object authentication extends LoginService {
  var twitter: Twitter = null

  def authenticate(login: String, pass: Array[Char], server: String): Boolean =
  {
    val tf = new TwitterFactory
    twitter = tf.getInstance(login, new String(pass));
    twitter.test
  }
}










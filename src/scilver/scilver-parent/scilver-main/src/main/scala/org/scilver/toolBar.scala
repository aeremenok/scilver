package org.scilver

import scala.swing._
import javax.swing.ImageIcon

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object toolBar extends BoxPanel(Orientation.Horizontal) {
  def +=(a: Action) = contents += new Button(a) {
    name = a.title
  }

  this += profileAction
  this += tweetAction
  this += followersAction
  this += followingAction
}

import App.auth.user

object tweetAction extends Action(i18n tr "Tweet") {
  def apply = Dialog.showMessage(toolBar, "Tweet") // todo
}

object profileAction extends Action(user.getScreenName) {
  icon = new ImageIcon(user.getProfileImageURL)
  def apply = Dialog.showMessage(toolBar, "User profile") // todo
}

object followersAction extends Action(i18n.tr("Followers")) {
  def apply = Dialog.showMessage(toolBar, "Followers") // todo
}

object followingAction extends Action(i18n.tr("Following")){
  def apply = Dialog.showMessage(toolBar, "Following") // todo
}

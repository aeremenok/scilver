package org.scilver.view

import scala.swing._
import javax.swing.{Box, ImageIcon}
import org.scilver.{App, tr}

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object toolBar extends BoxPanel(Orientation.Horizontal) {
  def +=(a: Action) =
    contents += new Button(a) {name = a.title}

  this += profileAction
  this += tweetAction
  this += followersAction
  this += followingAction

  peer.add(Box.createHorizontalGlue)

  contents += Component.wrap(tasks)
}

object tweetAction extends Action(tr("Tweet")) {
  def apply = Dialog.showMessage(toolBar, "Tweet") // todo
}

object profileAction extends Action(App.user.getScreenName) {
  icon = new ImageIcon(App.user.getProfileImageURL)
  def apply = Dialog.showMessage(toolBar, "User profile") // todo
}

object followersAction extends Action(tr("Followers")) {
  def apply = Dialog.showMessage(toolBar, "Followers") // todo
}

object followingAction extends Action(tr("Following")) {
  def apply = Dialog.showMessage(toolBar, "Following") // todo
}

package org.scilver

import javax.swing.table.DefaultTableModel
import java.awt.Color
import scala.swing._
import javax.swing.{SwingConstants, ImageIcon}
import org.jdesktop.swingx.JXLabel
import org.jdesktop.swingx.painter._
import twitter4j.{Paging, Status, ResponseList}

/**
 * @author eav
 * Date: 05.10.2010
 * Time: 21:56:10
 */
object TimelineView extends Table {
  model = TimelineModel

  TimelineModel.timeline = {
    // todo
    val t1 = App.twitter.getFriendsTimeline
    val t2 = App.twitter.getFriendsTimeline(new Paging(2))
    t1.addAll(t2)
    t1
  }

  override protected def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int) = {
    new StatusRenderer(apply(row, column).asInstanceOf[Status])
  }

  peer.setTableHeader(null)

  rowHeight = 100
}

object TimelineModel extends DefaultTableModel(Array[Object]("Main"), 0) {
  private var tl: ResponseList[Status] = _

  def timeline_=(tl: ResponseList[Status]) {
    this.tl = tl
    fireTableDataChanged
  }

  def timeline = this.tl

  override def isCellEditable(row: Int, column: Int) = false

  override def getValueAt(row: Int, column: Int) = tl.get(row)

  override def getRowCount =
    if (tl != null) tl.size else 0
}

case class StatusRenderer(status: Status) extends BorderPanel {
  import BorderPanel.Position._

  add(Component.wrap(new TweetLabel(status)), Center)
  val userIcon = new IconLabel(status)

  if (App.user == status.getUser)
    add(userIcon, East) else
    add(userIcon, West)
}

class TweetLabel(status: Status) extends JXLabel {
  setText(status.getText)
  setHorizontalTextPosition(SwingConstants.CENTER)
  setHorizontalAlignment(SwingConstants.CENTER)

  setBackgroundPainter(createPainter)
  def createPainter = {
    val rp1 = new RectanglePainter(5, 5, 5, 20, 20, 20);
    rp1.setFillPaint(Color.ORANGE);
    rp1.setBorderPaint(Color.ORANGE.darker);
    rp1.setStyle(AbstractAreaPainter.Style.BOTH);
    rp1.setBorderWidth(3);
    rp1.setAntialiasing(true);
    new CompoundPainter(rp1, new GlossPainter());
  }
}

class IconLabel(status: Status) extends Label {
  icon = new ImageIcon(status.getUser.getProfileImageURL)
}

package org.scilver

import scala.swing._
import twitter4j.{Paging, Status}
import javax.swing._
import table.{TableCellRenderer, TableCellEditor}
import java.awt.event.{MouseMotionAdapter, MouseEvent}
import view.{ExpandableTable, StatusLabel, Clickable, RollingTableModel}
import java.awt.BorderLayout

/**
 * @author eav
 * Date: 05.10.2010
 * Time: 21:56:10
 */
object TimelineView extends JTable(new TimelineModel) with ExpandableTable[Status] {
  setTableHeader(null)
  setRowHeight(75)

  setDefaultRenderer(classOf[Status], statusEditor)
  setDefaultEditor(classOf[Status], statusEditor)

  setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  addMouseMotionListener(new MouseMotionAdapter {
    override def mouseMoved(e: MouseEvent) = {
      val (row, column) = cellAtPoint(e.getPoint)
      editCellAt(row, column)
    }
  })

  def table = this.asInstanceOf[JTable]

  def tableModel = getModel.asInstanceOf[TimelineModel]

  def cellAtPoint(p: Point) = (
          convertRowIndexToModel(rowAtPoint(p)),
          convertColumnIndexToModel(columnAtPoint(p))
          )
}

class TimelineModel extends RollingTableModel[Status]("Main") {
  private var currentPage: Int = 0

  protected def loadPortion = {
    currentPage += 1
    App.twitter.getFriendsTimeline(new Paging(currentPage))
  }

  override def getColumnClass(columnIndex: Int) = classOf[Status]

  override def isCellEditable(row: Int, column: Int) = true

  override def setValueAt(aValue: AnyRef, row: Int, column: Int) = {}
}

object statusEditor extends AbstractCellEditor with TableCellEditor with TableCellRenderer {
  def getCellEditorValue = null

  def getTableCellEditorComponent(table: JTable, value: AnyRef, isSelected: Boolean, row: Int, column: Int) =
    StatusPanel(value.asInstanceOf[Status])

  def getTableCellRendererComponent(table: JTable, value: AnyRef, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int) =
    getTableCellEditorComponent(table, value, isSelected, row, column)
}

case class StatusPanel(status: Status) extends JPanel(new BorderLayout) {
  add(StatusLabel(status.getText), BorderLayout.CENTER)

  private val userIcon = IconLabel(status)
  if (App.user == status.getUser)
    add(userIcon, BorderLayout.EAST) else
    add(userIcon, BorderLayout.WEST)
}

case class IconLabel(status: Status) extends JLabel with Clickable {
  setIcon(new ImageIcon(status.getUser.getProfileImageURL))

  def onClick(e: MouseEvent) = Dialog.showMessage(parent = null, message = status.getUser.getScreenName + "'s Profile")
}

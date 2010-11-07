package org.scilver.view

import javax.swing.table.DefaultTableModel
import javax.swing.event.{ChangeEvent, ChangeListener}
import java.util.{ArrayList, List => JList}
import javax.swing.{JTable, JScrollPane}

/**
 * @author eav
 * Date: 05.10.2010
 * Time: 21:56:10
 */
class ListTableModel[T](titles: Object*) extends DefaultTableModel(titles.toArray[Object], 0) {
  protected var rows: JList[T] = new ArrayList[T]()

  def data = this.rows

  def data_=(dataList: JList[T]) {
    this.rows = dataList
    fireTableDataChanged
  }

  override def isCellEditable(row: Int, column: Int) = false

  override def getValueAt(row: Int, column: Int) = rows.get(row).asInstanceOf[Object]

  override def getRowCount = if (rows != null) rows.size else 0
}

abstract class RollingTableModel[T](titles: Object*) extends ListTableModel[T](titles) {
  def expand = tasks.execute(loadPortion, append)

  protected def loadPortion: JList[T]

  private def append(portion: JList[T]) {
    val sizeBefore = rows.size
    rows.addAll(portion)
    fireTableRowsInserted(sizeBefore, portion.size)
  }
}

trait ExpandableTable[T] {
  def tableModel: RollingTableModel[T]

  def table: JTable
}

case class ExpandScrollPane(expandable: ExpandableTable[_]) extends JScrollPane(expandable.table) with ChangeListener {
  val TRESHOLD = 0.8f
  val tableModel = expandable.tableModel
  val scrollBarModel = verticalScrollBar.getModel

  scrollBarModel.addChangeListener(this)

  def stateChanged(e: ChangeEvent) {
    val value = scrollBarModel.getValue
    val maximum = scrollBarModel.getMaximum - scrollBarModel.getExtent
    if (maximum > 0 && value / maximum > TRESHOLD)
      tableModel.expand
  }
}

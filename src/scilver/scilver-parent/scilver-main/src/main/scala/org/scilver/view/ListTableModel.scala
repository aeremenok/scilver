package org.scilver.view

import javax.swing.table.DefaultTableModel
import scala.swing.Table
import javax.swing.event.{ChangeEvent, ChangeListener}
import javax.swing.JScrollPane
import java.util.{ArrayList, List => JList}

/**
 * @author eav
 * Date: 05.10.2010
 * Time: 21:56:10
 */
class ListTableModel[T](titles: Array[Object]) extends DefaultTableModel(titles, 0) {
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

abstract class RollingTableModel[T](titles: Array[Object]) extends ListTableModel[T](titles) {
  def expand = tasks.execute(loadPortion, append)

  protected def loadPortion: JList[T]

  private def append(portion: JList[T]) {
    val sizeBefore = rows.size
    rows.addAll(portion)
    fireTableRowsInserted(sizeBefore, portion.size)
  }
}

case class ExpandScrollPane(table: Table) extends JScrollPane(table.peer) with ChangeListener {
  val TRESHOLD = 0.8f
  val tableModel = table.model.asInstanceOf[RollingTableModel[_]]
  val scrollBarModel = verticalScrollBar.getModel

  scrollBarModel.addChangeListener(this)

  def stateChanged(e: ChangeEvent) {
    val value = scrollBarModel.getValue
    val maximum = scrollBarModel.getMaximum - scrollBarModel.getExtent
    if (maximum > 0 && value / maximum > TRESHOLD)
      tableModel.expand
  }
}

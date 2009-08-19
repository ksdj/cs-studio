package org.csstudio.display.rdbtable.ui;

import org.csstudio.display.rdbtable.model.RDBTableModel;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/** A (lazy) content provider that connects the RDBTableModel
 *  to an Eclipse TableViewer
 *  
 *  @author Kay Kasemir
 */
public class RDBTableModelContentProvider implements ILazyContentProvider
{
    /** Model with data */
    private RDBTableModel model;
    
    /** TableViewer in which to display the model data */
    private TableViewer table_viewer;
    
    /** We happen to know that this is called by the editor via
     *  TableViewer.setInput(RDBTableModel).
     *  It's also called with a <code>null</code> input
     *  when the application shuts down.
     */
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
    {
        table_viewer = (TableViewer) viewer;
        model = (RDBTableModel) newInput;
        // Setting the item count causes a 'refresh' of the table
        table_viewer.setItemCount(model == null ? 0 : model.getRowCount());
    }

    /** Called by viewer; we have to update the given row of the TableViewer
     *  with the corresponding Model element (RDBTableRow)
     *  @param row Row to update
     */
    public void updateElement(final int row)
    {
        table_viewer.replace(model.getRow(row), row);
    }

    /** {@inheritDoc} */
    public void dispose()
    {
        // Nothing to dispose
    }
}

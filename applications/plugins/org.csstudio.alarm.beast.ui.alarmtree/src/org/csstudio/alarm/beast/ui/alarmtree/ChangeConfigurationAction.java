package org.csstudio.alarm.beast.ui.alarmtree;

import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModel;
import org.csstudio.alarm.beast.ui.clientmodel.AlarmClientModelConfigListener;
import org.csstudio.platform.logging.CentralLogger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

/** (Toolbar) action that shows the currently selected alarm configuration name
 *  and allows selection of a different alarm configuration
 *  @author Kay Kasemir
 */
public class ChangeConfigurationAction extends Action implements IMenuCreator, AlarmClientModelConfigListener
{
	final private AlarmClientModel model;
	private Menu menu;

	public ChangeConfigurationAction(final AlarmClientModel model)
    {
		super(model.getConfigurationName(), IAction.AS_DROP_DOWN_MENU);
		this.model = model;
		setMenuCreator(this);
    }

	/** Invoked when user selects the action (clicks on 'main' section of the button)
	 *  {@inheritDoc}
	 */
	@Override
    public void runWithEvent(final Event event)
    {
		// User clicked on the action's main section, not the small drop-down indicator.
		// Execute drop-down behavior anyway, based on code copied from
		// Eclipse 3.6.1 ActionContributionItem.ActionContributionItem.handleWidgetSelection
		final ToolItem item = (ToolItem) event.widget;
		final Menu menu = getMenu(item.getParent());
		final Point point = item.getParent().toDisplay(0, item.getBounds().height);
		menu.setLocation(point.x, point.y);
		menu.setVisible(true);
    }

	/** Dispose menu (in fact there was one) */
	private void disposeMenu()
    {
	    if (menu == null)
			return;
		menu.dispose();
		menu = null;
    }

	/** Get menu for the drop-down section of the action
	 *  @see IMenuCreator
	 */
	public Menu getMenu(final Control parent)
    {
		disposeMenu();
		menu = new Menu(parent);
		// GUI shows menu even when the action is disabled.
		// Hack around this by showing an empty menu
		if (isEnabled())
		{
			try
			{
				final String current = model.getConfigurationName();
				final String names[] = model.listConfigurations();
				for (String name : names)
				{	// Note item text later used to set model config
					final MenuItem item = new MenuItem(menu, SWT.RADIO);
					item.setText(name);
					if (current.equals(name))
					{	// Display current config. as selected, but no function
						item.setSelection(true);
					}
					else
					{	// Selecting _other_ config updates the model's config name
						item.addSelectionListener(new SelectionAdapter()
						{
							@Override
	                        public void widgetSelected(final SelectionEvent e)
	                        {
								// Prohibit more changes while loading new config
								ChangeConfigurationAction.this.setEnabled(false);
								// Use item text to set model name
								final String new_config = item.getText();
								model.setConfigurationName(new_config, ChangeConfigurationAction.this);
	                        }
						});
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				CentralLogger.getInstance().getLogger(this).error(ex);
			}
		}
	    return menu;
    }

	/** Never called?
	 *  @see IMenuCreator
	 */
	public Menu getMenu(final Menu parent)
    {
		// Never called?
	    return null;
    }

	/** Dispose menu (in fact there was one)
     *  @see IMenuCreator
     */
    public void dispose()
    {
    	disposeMenu();
    }

    /** @see AlarmClientModelConfigListener */
	public void newAlarmConfiguration(final AlarmClientModel model)
    {
		setText(model.getConfigurationName());
		setEnabled(true);
    }
}
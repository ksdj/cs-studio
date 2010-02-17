package org.csstudio.sds.components.ui.internal.editparts;

import org.csstudio.platform.ui.util.CustomMediaFactory;
import org.csstudio.sds.components.model.BooleanSwitchModel;
import org.csstudio.sds.components.ui.internal.figures.BoolSwitchFigure;
import org.csstudio.sds.ui.editparts.AbstractWidgetEditPart;
import org.csstudio.sds.ui.editparts.ExecutionMode;
import org.csstudio.sds.ui.editparts.IWidgetPropertyChangeHandler;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.RGB;

/**
 * 
 * @author Kai Meyer (C1 WPS)
 *
 */
public class BooleanSwitchEditPart extends AbstractWidgetEditPart {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IFigure doCreateFigure() {
		final BooleanSwitchModel model = (BooleanSwitchModel) getWidgetModel();
		BoolSwitchFigure figure = new BoolSwitchFigure();
		figure.setEffect3D(model.get3dEffect());
		figure.setBooleanValue(model.getValue());
		figure.setOffColor(getRgb(model.getOffColor()));
		figure.setOnColor(getRgb(model.getOnColor()));
		figure.setShowBooleanLabel(model.getShowLabels());
		figure.setOnLabel(model.getOnLabel());
		figure.setOffLabel(model.getOffLabel());
		figure.addBoolControlListener(new IBoolControlListener() {
			public void valueChanged(boolean newValue) {
				if (getExecutionMode() == ExecutionMode.RUN_MODE) {
					model.setValue(newValue);
				}
			}
		});
		figure.setRunMode(getExecutionMode() == ExecutionMode.RUN_MODE);
		figure.setToggle(true);
		return figure;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void registerPropertyChangeHandlers() {
		// value
		IWidgetPropertyChangeHandler valueHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setBooleanValue((Boolean)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_VALUE, valueHandler);
		
		// 3d effect
		IWidgetPropertyChangeHandler effectHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setEffect3D((Boolean)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_3D_EFFECT, effectHandler);
		
		// on color
		IWidgetPropertyChangeHandler onColorHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setOnColor(getRgb((String) newValue));
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_ON_COLOR, onColorHandler);
		
		// off color
		IWidgetPropertyChangeHandler offColorHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setOffColor(getRgb((String) newValue));
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_OFF_COLOR, offColorHandler);
		
		// on label
		IWidgetPropertyChangeHandler onLabelHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setOnLabel((String) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_ON_LABEL, onLabelHandler);
		
		// on label
		IWidgetPropertyChangeHandler offLabelHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setOffLabel((String) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_OFF_LABEL, offLabelHandler);
		
		// value
		IWidgetPropertyChangeHandler showLabelHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				BoolSwitchFigure switchFigure = (BoolSwitchFigure) refreshableFigure;
				switchFigure.setShowBooleanLabel((Boolean)newValue);
				return true;
			}
		};
		setPropertyChangeHandler(BooleanSwitchModel.PROP_LABEL_VISIBLE, showLabelHandler);
	}

}

package org.csstudio.opibuilder.widgets.editparts;

import org.csstudio.opibuilder.editparts.AbstractPVWidgetEditPart;
import org.csstudio.opibuilder.editparts.ExecutionMode;
import org.csstudio.opibuilder.model.AbstractWidgetModel;
import org.csstudio.opibuilder.properties.IWidgetPropertyChangeHandler;
import org.csstudio.opibuilder.util.OPIColor;
import org.csstudio.opibuilder.widgets.figures.ByteMonitorFigure;
import org.csstudio.opibuilder.widgets.model.ByteMonitorModel;
import org.csstudio.opibuilder.widgets.model.LEDModel;
import org.csstudio.platform.data.IValue;
import org.eclipse.draw2d.IFigure;

/**
 * This class implements the widget edit part for the Byte Monitor widget.  This
 * displays the bits in a value as s series of LEDs 
 * @author hammonds
 *
 */
public class ByteMonitorEditPart extends AbstractPVWidgetEditPart {
	

	/* (non-Javadoc)
	 * @see org.csstudio.opibuilder.editparts.AbstractPVWidgetEditPart#getValue()
	 */
	@Override
	public Object getValue() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.csstudio.opibuilder.editparts.AbstractPVWidgetEditPart#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		((ByteMonitorFigure)getFigure()).setValue(value);
	}

	/* (non-Javadoc)
	 * @see org.csstudio.opibuilder.editparts.AbstractBaseEditPart#doCreateFigure()
	 */
	@Override
	protected IFigure doCreateFigure() {
		ByteMonitorModel model = (ByteMonitorModel) getWidgetModel();
		
		ByteMonitorFigure fig = new ByteMonitorFigure();
		setModel(model);
		setFigure(fig);
		activate();
		fig.setStartBit(((Integer)model.getPropertyValue(ByteMonitorModel.PROP_START_BIT)) );
		fig.setNumBits(((Integer)model.getPropertyValue(ByteMonitorModel.PROP_NUM_BITS)) );
		fig.setHorizontal(((Boolean)model.getPropertyValue(ByteMonitorModel.PROP_HORIZONTAL)) );
		fig.setReverseBits(((Boolean)model.getPropertyValue(ByteMonitorModel.PROP_BIT_REVERSE)) );
		fig.setSquareLED(((Boolean)model.getPropertyValue(ByteMonitorModel.PROP_SQUARE_LED)) );
		fig.setOnColor(((OPIColor)model.getPropertyValue(ByteMonitorModel.PROP_ON_COLOR)).getRGBValue() );
		fig.setOffColor(((OPIColor)model.getPropertyValue(ByteMonitorModel.PROP_OFF_COLOR)).getRGBValue() );
		fig.setValue(new Integer(0x1111));
		fig.drawValue();

		return fig;
	}

	/* (non-Javadoc)
	 * @see org.csstudio.opibuilder.editparts.AbstractBaseEditPart#registerPropertyChangeHandlers()
	 */
	@Override
	protected void registerPropertyChangeHandlers() {
		super.registerBasePropertyChangeHandlers();
		getFigure().setEnabled(getWidgetModel().isEnabled() && 
				(getExecutionMode() == ExecutionMode.RUN_MODE));		
		
		removeAllPropertyChangeHandlers(AbstractWidgetModel.PROP_ENABLED);
		
		//enable
		IWidgetPropertyChangeHandler enableHandler = new IWidgetPropertyChangeHandler(){
			public boolean handleChange(Object oldValue, Object newValue,
					IFigure figure) {
				if(getExecutionMode() == ExecutionMode.RUN_MODE)
					figure.setEnabled((Boolean)newValue);
				return false;
			}
		};		
		setPropertyChangeHandler(AbstractWidgetModel.PROP_ENABLED, enableHandler);
		
		// PV_Value
		IWidgetPropertyChangeHandler pvhandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue, final IFigure refreshableFigure) {
				if((newValue != null) && (newValue instanceof IValue) ){
					setValue(new Integer((new Double(((IValue)newValue).format())).intValue()));					
				}
				else {
					System.out.println("Not an IValue or null" );
				}
				return false;
			}
		};
		setPropertyChangeHandler(ByteMonitorModel.PROP_PVVALUE, pvhandler);
		
		// on color
		IWidgetPropertyChangeHandler colorHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				ByteMonitorFigure figure = (ByteMonitorFigure) refreshableFigure;
				figure.setOnColor(((OPIColor) newValue).getRGBValue());
				figure.drawValue();
				return true;
			}
		};
		setPropertyChangeHandler(ByteMonitorModel.PROP_ON_COLOR, colorHandler);
		
		// off color
		colorHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				ByteMonitorFigure figure = (ByteMonitorFigure) refreshableFigure;
				figure.setOffColor(((OPIColor) newValue).getRGBValue());
				figure.drawValue();
				return true;
			}
		};
		setPropertyChangeHandler(ByteMonitorModel.PROP_OFF_COLOR, colorHandler);
		
		
		//change orientation of the bit display
		IWidgetPropertyChangeHandler horizontalHandler = new IWidgetPropertyChangeHandler() {

			public boolean handleChange(Object oldValue, Object newValue,
					IFigure refreshableFigure) {
				ByteMonitorFigure figure = (ByteMonitorFigure)refreshableFigure;
				figure.setHorizontal((Boolean)newValue);
				figure.drawValue();
				return true;
			}
		};
		
		setPropertyChangeHandler(ByteMonitorModel.PROP_HORIZONTAL, horizontalHandler);
		
		//change the display order of the bits
		IWidgetPropertyChangeHandler reverseBitsHandler = new IWidgetPropertyChangeHandler() {

			public boolean handleChange(Object oldValue, Object newValue,
					IFigure refreshableFigure) {
				ByteMonitorFigure figure = (ByteMonitorFigure)refreshableFigure;
				figure.setReverseBits((Boolean)newValue);
				figure.drawValue();
				return true;
			}
		};
		
		setPropertyChangeHandler(ByteMonitorModel.PROP_BIT_REVERSE, reverseBitsHandler);
		
		//Set the bit to use as a starting point
		IWidgetPropertyChangeHandler startBitHandler = new IWidgetPropertyChangeHandler() {

			public boolean handleChange(Object oldValue, Object newValue,
					IFigure refreshableFigure) {
				ByteMonitorFigure figure = (ByteMonitorFigure)refreshableFigure;
				int maxBits = figure.getMAX_BITS();
				int numBits = figure.getNumBits();
/*
 * 				if ((Integer)newValue < maxBits && ((Integer)newValue + numBits) < maxBits){
 */
					figure.setStartBit((Integer)newValue);
/*				}
				else {
					((ByteMonitorFigure)figure).setInvalidBits();
				}
				*/
				figure.drawValue();
					return true;
			}
		};
		
		setPropertyChangeHandler(ByteMonitorModel.PROP_START_BIT, startBitHandler);
		
		//Set the number of bits to display
		IWidgetPropertyChangeHandler numBitsHandler = new IWidgetPropertyChangeHandler() {

			public boolean handleChange(Object oldValue, Object newValue,
					IFigure refreshableFigure) {
				ByteMonitorFigure figure = (ByteMonitorFigure)refreshableFigure;
				figure.setNumBits((Integer)newValue);
				figure.drawValue();
				return true;
			}
		};
		
		setPropertyChangeHandler(ByteMonitorModel.PROP_NUM_BITS, numBitsHandler);
		
		//Sqaure LED
		IWidgetPropertyChangeHandler squareLEDHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				ByteMonitorFigure bm = (ByteMonitorFigure) refreshableFigure;
				bm.setSquareLED((Boolean) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(LEDModel.PROP_SQUARE_LED, squareLEDHandler);	
		
		
		//effect 3D
		IWidgetPropertyChangeHandler effect3DHandler = new IWidgetPropertyChangeHandler() {
			public boolean handleChange(final Object oldValue,
					final Object newValue,
					final IFigure refreshableFigure) {
				ByteMonitorFigure bmFig = (ByteMonitorFigure) refreshableFigure;
				bmFig.setEffect3D((Boolean) newValue);
				return true;
			}
		};
		setPropertyChangeHandler(LEDModel.PROP_EFFECT3D, effect3DHandler);	
		

	}

}
/*
		* Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron,
		* Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
		*
		* THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
		* WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT
		NOT LIMITED
		* TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE
		AND
		* NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
		BE LIABLE
		* FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
		CONTRACT,
		* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
		SOFTWARE OR
		* THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE
		DEFECTIVE
		* IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING,
		REPAIR OR
		* CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART
		OF THIS LICENSE.
		* NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS
		DISCLAIMER.
		* DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
		ENHANCEMENTS,
		* OR MODIFICATIONS.
		* THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION,
		MODIFICATION,
		* USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE
		DISTRIBUTION OF THIS
		* PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU
		MAY FIND A COPY
		* AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
		*/
package org.csstudio.utility.quickstart;

import org.csstudio.platform.logging.CentralLogger;
import org.csstudio.sds.ui.runmode.RunModeService;
import org.csstudio.utility.quickstart.preferences.PreferenceConstants;
import org.csstudio.utility.quickstart.preferences.PreferenceValidator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;


/**
 * TODO (jhatje) :
 *
 * @author jhatje
 * @author $Author: jhatje $
 * @version $Revision: 1.2.2.2 $
 * @since 05.08.2010
 */
public class DisplayAutoStart implements Runnable {

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
        final PreferenceValidator preferenceValidator = new PreferenceValidator();
        final String quickstartFiles = preferenceStore.getString(PreferenceConstants.SDS_FILES);
        final String[] array = quickstartFiles.split(";");
        try {
            waitForWorkbench();
        } catch (final InterruptedException e) {
            CentralLogger.getInstance().error(this, "Quickstart startup error, " + e.getMessage());
        }
        for (final String element : array) {
            final String[] ItemFromPreferences = element.split("\\?");
            final String[] checkedPrefItem = preferenceValidator.checkPreferenceValidity(ItemFromPreferences);
            if (checkedPrefItem[2].equals("true")) {
                final IPath sdsFilePath = Path.fromOSString(checkedPrefItem[0]);
                CentralLogger.getInstance().debug(this, "open: " + sdsFilePath);
                PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        RunModeService.getInstance().openDisplayShellInRunMode(sdsFilePath);
                    }
                });
            }
        }
    }

    /**
     * Wait until the workbench is available to start SDS displays.
     *
     * @throws InterruptedException
     */
    private void waitForWorkbench() throws InterruptedException {
        boolean workbenchNotAvailable = true;
        while (workbenchNotAvailable) {
            try {
                final IWorkbench workbench = PlatformUI.getWorkbench();
                if (workbench != null) {
                    workbenchNotAvailable = false;
                }
            } catch (final IllegalStateException e) {
                // TODO (jhatje) : what shall happen here?
            }
            Thread.sleep(1000);
        }
    }



}

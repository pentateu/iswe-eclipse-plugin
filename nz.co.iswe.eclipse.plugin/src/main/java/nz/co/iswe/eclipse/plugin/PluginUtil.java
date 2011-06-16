package nz.co.iswe.eclipse.plugin;

import nz.co.iswe.eclipse.plugin.properties.FieldEditorOverlayPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;

public class PluginUtil {

	private static final String TRUE = "true";

	public static String getOverlayedPreferenceValue(IPreferenceStore store,
			IResource resource, String pageId, String name) {
		IProject project = resource.getProject();
		String value = null;
		if (useProjectSettings(project, pageId)) {
			value = getProperty(resource, pageId, name);
		}
		if (value != null)
			return value;
		return store.getString(name);
	}

	private static boolean useProjectSettings(IResource resource, String pageId) {
		String use = getProperty(resource, pageId,
				FieldEditorOverlayPage.USEPROJECTSETTINGS);
		return TRUE.equals(use);
	}

	private static String getProperty(IResource resource, String pageId,
			String key) {
		try {
			return resource
					.getPersistentProperty(new QualifiedName(pageId, key));
		} catch (CoreException e) {
		}
		return null;
	}
}

package com.axonivy.connector.keycloak.enums;

import org.apache.commons.lang3.StringUtils;


import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

/**
 * Interface to use with class which have their instance names defined in the
 * CMS.
 *
 * These are typically enumerations.
 */
public interface HasCmsName {
	
	/**
	 * Get the name of this instance.
	 *
	 * @return name of this instance.
	 */
	String name();

	/**
	 * The CMS Folder path
	 * @return
	 */
	String cmsRootPath();
	
	/**
	 * Return the name entry of the instance.
	 *
	 * @return
	 */
	default String getCmsName() {
		return getCms(name());
	}
	
	/**
	 * Lookup the entry for an instance in the Ivy Cms.
	 *
	 * If the entry is not found, then return the name of the entry.
	 *
	 * @param entry
	 * @return CMS entry
	 */
	default String getCms(String name) {
		final String cmsPath = String.format("%s/%s", cmsRootPath(), name);
		String result = Ivy.cms().co(cmsPath);
		if (StringUtils.isEmpty(result)) {
			Logger.getLogger(HasCmsName.class).warn("Missing CMS entry for '" + cmsPath + "'");
			result = name;
		}
		return result;
	}
}

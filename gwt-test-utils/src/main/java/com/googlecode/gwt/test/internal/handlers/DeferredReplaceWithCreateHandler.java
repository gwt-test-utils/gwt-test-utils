package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.ModuleData;
import com.googlecode.gwt.test.internal.ModuleData.ReplaceWithData;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DeferredReplaceWithCreateHandler implements GwtCreateHandler {

    public Object create(Class<?> classLiteral) throws Exception {

        String moduleName = GwtConfig.get().getTestedModuleName();

        List<ReplaceWithData> replaceWithList = ModuleData.get(moduleName).getReplaceWithListMap().get(
                classLiteral.getName().replaceAll("\\$", "."));

        // not handled by a <replace-with> element in any .gwt.xml file
        if (replaceWithList == null) {
            return null;
        }

        String replaceWith = getReplaceWithClass(replaceWithList);

        return (replaceWith != null && !replaceWith.equals(classLiteral.getCanonicalName()))
                ? GWT.create(GwtReflectionUtils.getClass(replaceWith)) : null;

    }

    private String getReplaceWithClass(List<ReplaceWithData> replaceWithList) {

        // the default <replace-with>, with no filter
        ReplaceWithData defaultReplaceWith = null;

        Set<String> clientProperyNames = GwtConfig.get().getModuleRunner().getClientPropertyNames();

        for (ReplaceWithData replaceWithData : replaceWithList) {

            if ((clientProperyNames.size() == 0)) {

                if (isDefault(replaceWithData)) {
                    // default case : nothing is specified
                    return replaceWithData.getReplaceWith();
                }

                // not a <replace-with> element to use
                continue;

            } else if (isDefault(replaceWithData)) {
                // save the default <replace-with>, to use
                defaultReplaceWith = replaceWithData;
                continue;

            }

            // validate every when-property-is match

            if (replaceWithData.hasWhenPropertyIs()) {
                boolean validateProperties = true;
                Iterator<String> it = clientProperyNames.iterator();
                while (it.hasNext() && validateProperties) {
                    String propertyName = it.next();
                    String value = GwtConfig.get().getModuleRunner().getClientProperty(propertyName);
                    validateProperties = replaceWithData.whenPropertyIsMatch(propertyName, value);
                }
                if (!validateProperties) {
                    continue;
                }
            }

            // validate at least one any/when-property-is matches
            boolean validateAnyProperty = !replaceWithData.hasAnyWhenPropertyIs();
            Iterator<String> it = clientProperyNames.iterator();
            while (it.hasNext() && !validateAnyProperty) {
                String propertyName = it.next();
                String value = GwtConfig.get().getModuleRunner().getClientProperty(propertyName);

                validateAnyProperty = replaceWithData.anyMatch(propertyName, value);
            }

            if (validateAnyProperty) {
                return replaceWithData.getReplaceWith();
            }
        }

        return (defaultReplaceWith != null) ? defaultReplaceWith.getReplaceWith() : null;
    }

    private boolean isDefault(ReplaceWithData replaceWithData) {
        return !replaceWithData.hasAnyWhenPropertyIs() && !replaceWithData.hasWhenPropertyIs();
    }
}

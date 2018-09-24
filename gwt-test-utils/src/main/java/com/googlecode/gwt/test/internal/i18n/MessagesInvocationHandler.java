package com.googlecode.gwt.test.internal.i18n;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.i18n.client.Messages.*;
import com.google.gwt.i18n.client.PluralRule;
import com.google.gwt.i18n.client.impl.plurals.DefaultRule;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.googlecode.gwt.test.exceptions.GwtTestI18NException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("deprecation")
class MessagesInvocationHandler extends LocalizableResourceInvocationHandler {

    MessagesInvocationHandler(Class<? extends LocalizableResource> proxiedClass) {
        super(proxiedClass);
    }

    @Override
    protected Object extractDefaultValue(Method method, Object[] args) {
        DefaultMessage defaultMessage = method.getAnnotation(DefaultMessage.class);
        Annotation messageAnnotation = getMessageAnnotation(method);
        String valuePattern = null;
        if (messageAnnotation != null) {
            String key = extractPluralCountAndSelectValues(messageAnnotation, method, args,
                    getLocale());
            String[] values = getMessageAnnotationValues(messageAnnotation);
            valuePattern = getAnnotationValues(values).get(key);
        }
        if (valuePattern == null && defaultMessage != null) {
            valuePattern = defaultMessage.value();
        }
        if (valuePattern != null) {
            String result = format(valuePattern, args, getLocale());

            // handle SafeHtml return type
            return method.getReturnType() == SafeHtml.class ? SafeHtmlUtils.fromTrustedString(result)
                    : result;
        }

        return null;
    }

    @Override
    protected Object extractFromProperties(Properties localizedProperties, Method method, Object[] args, Locale locale) {
        Annotation messageAnnotation = getMessageAnnotation(method);

        String key = messageAnnotation == null ? getKey(method) : getSpecificKey(messageAnnotation,
                method, args, locale);

        String result = extractProperty(localizedProperties, key);
        if (result != null) {

            result = format(result, args, locale);

            // handle SafeHtml return type
            return method.getReturnType() == SafeHtml.class ? SafeHtmlUtils.fromTrustedString(result)
                    : result;
        }

        return null;
    }

    private String extractProperty(Properties properties, String key) {
        String result = properties.getProperty(key);
        if (result == null) {
            result = properties.getProperty(key.replaceAll("_", "."));
        }
        return result;
    }

    /**
     * Get the {@link PluralCount} and/or {@link Select} value which correspond to the method call.
     * if there are many @PluralCount and/or @Select annotated args, the corresponding values are
     * appended with the '|' separator.
     *
     * @param messageAnnotation The annotation which has been detected. Can be a
     *                          {@link AlternateMessage} or a {@link PluralText}
     * @param method            The i18n called method
     * @param args              The parameter passed to the i18n method during the call
     * @param locale            The locale to use
     * @return A String in which are appended all the {@link PluralCount} and {@link Select} value,
     * or null if there is no such annotations
     */
    @SuppressWarnings("unchecked")
    private String extractPluralCountAndSelectValues(Annotation messageAnnotation, Method method,
                                                     Object[] args, Locale locale) {
        Annotation[][] annotations = method.getParameterAnnotations();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] childArray = annotations[i];
            for (Annotation aChildArray : childArray) {
                if (PluralCount.class.isAssignableFrom(aChildArray.getClass())) {
                    PluralCount pluralCount = (PluralCount) aChildArray;
                    Class<? extends PluralRule> pluralRuleClass = pluralCount.value();
                    int count = (Integer) args[i];

                    String pluralRuleClassName = pluralRuleClass != PluralRule.class
                            ? pluralRuleClass.getName() : DefaultRule.class.getName();
                    pluralRuleClassName += "_" + locale.getLanguage();

                    try {
                        Class<? extends PluralRule> acutalRule = (Class<? extends PluralRule>) GwtReflectionUtils.getClass(pluralRuleClassName);
                        PluralRule ruleInstance = acutalRule.newInstance();

                        sb.append(ruleInstance.pluralForms()[ruleInstance.select(count)].getName()).append(
                                "|");

                    } catch (ClassNotFoundException e) {
                        throw new GwtTestI18NException("Cannot find PluralRule for method '"
                                + method.getDeclaringClass().getSimpleName() + "." + method.getName()
                                + "()'. Expected PluralRule : '" + pluralRuleClassName + "'");
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new GwtTestI18NException("Error during instanciation of class '"
                                + pluralRuleClassName + "'", e);
                    }
                } else if (Select.class.isAssignableFrom(aChildArray.getClass())) {
                    sb.append(args[i]).append("|");
                }
            }
        }

        if (sb.length() == 0) {
            throw new GwtTestI18NException("Bad configuration of '" + method.getDeclaringClass() + "."
                    + method.getName() + "' : a @" + messageAnnotation.getClass().getSimpleName()
                    + " is declared but no @" + PluralCount.class.getSimpleName() + " or @"
                    + Select.class.getSimpleName() + " set on any method parameter'");
        } else {
            return sb.substring(0, sb.length() - 1);
        }

    }

    private String format(String pattern, Object[] args, Locale locale) {
        MessageFormat format = locale == null ? new MessageFormat(pattern) : new MessageFormat(
                pattern, locale);

        return format.format(args);
    }

    /**
     * Convert the {@link AlternateMessage#value()} or {@link PluralText#value()} string array to a
     * map of possible values
     *
     * @param annotationValues the array value returned by {@link AlternateMessage#value()} or
     *                         {@link PluralText#value()}
     * @return a map of named values
     */
    private Map<String, String> getAnnotationValues(String[] annotationValues) {
        Map<String, String> pluralForms = new HashMap<>();

        for (int i = 0; i < annotationValues.length; i++) {
            pluralForms.put(annotationValues[i], annotationValues[++i]);
        }

        return pluralForms;
    }

    /**
     * Return an instance of {@link AlternateMessage} or {@link PluralText} if the i18n method is
     * annotated, null otherwise.
     *
     * @param method The current processed i18n method
     * @return an instance of {@link AlternateMessage} or {@link PluralText} if the i18n method is
     * annotated, null otherwise.
     */
    private Annotation getMessageAnnotation(Method method) {
        Annotation specificMessageAnnotation = method.getAnnotation(AlternateMessage.class);
        if (specificMessageAnnotation == null) {
            specificMessageAnnotation = method.getAnnotation(PluralText.class);
        }
        return specificMessageAnnotation;
    }

    private String[] getMessageAnnotationValues(Annotation messageAnnotation) {
        if (messageAnnotation instanceof AlternateMessage) {
            return ((AlternateMessage) messageAnnotation).value();
        } else if (messageAnnotation instanceof PluralText) {
            return ((PluralText) messageAnnotation).value();
        }

        return null;
    }

    private String getSpecificKey(Annotation specificMessageAnnotation, Method method,
                                  Object[] args, Locale locale) {
        String pluralCountValue = extractPluralCountAndSelectValues(specificMessageAnnotation,
                method, args, locale);

        return method.getName() + "[" + pluralCountValue + "]";
    }

}

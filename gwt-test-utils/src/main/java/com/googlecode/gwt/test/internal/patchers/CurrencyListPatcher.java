package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.i18n.client.CurrencyData;
import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.impl.CurrencyDataImpl;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.utils.GwtPropertiesHelper;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.*;

@PatchClass(CurrencyList.class)
class CurrencyListPatcher {

    private static class CurrencyDataHolder implements AfterTestCallback {

        private final Map<Locale, CurrencyData> currencyDatas = new HashMap<>();

        private CurrencyDataHolder() {
            AfterTestCallbackManager.get().registerCallback(this);
        }

        public void afterTest() {
            currencyDatas.clear();
        }

        CurrencyData getCurrencyData(Locale locale) {
            CurrencyData currencyData = currencyDatas.get(locale);
            if (currencyData == null) {
                currencyData = createCurrencyData(locale);
                currencyDatas.put(locale, currencyData);
            }

            return currencyData;
        }

        private CurrencyData createCurrencyData(Locale locale) {
            Properties currencyData = GwtPropertiesHelper.get().getLocalizedProperties(
                    "com/google/gwt/i18n/client/impl/cldr/CurrencyData", locale);
            Properties currencyExtra = GwtPropertiesHelper.get().getProperties(
                    "com/google/gwt/i18n/client/constants/CurrencyExtra");
            Properties numberConstants = GwtPropertiesHelper.get().getLocalizedProperties(
                    "com/google/gwt/i18n/client/constants/NumberConstantsImpl", locale);
            Set<Object> keySet = currencyData.keySet();
            String[] currencies = new String[keySet.size()];
            keySet.toArray(currencies);
            Arrays.sort(currencies);

            String defCurrencyCode = numberConstants.getProperty("defCurrencyCode");
            if (defCurrencyCode == null) {
                defCurrencyCode = "USD";
            }

            CurrencyData defCurrencyData = new CurrencyDataImpl(defCurrencyCode, defCurrencyCode, 2,
                    "");

            for (String currencyCode : currencies) {
                String currencyEntry = currencyData.getProperty(currencyCode);
                String[] currencySplit = currencyEntry.split("\\|");
                String currencySymbol = null;
                if (currencySplit.length > 1 && currencySplit[1].length() > 0) {
                    currencySymbol = currencySplit[1];
                }
                int currencyFractionDigits = 2;
                if (currencySplit.length > 2 && currencySplit[2].length() > 0) {
                    try {
                        currencyFractionDigits = Integer.valueOf(currencySplit[2]);
                    } catch (NumberFormatException ignored) {
                    }
                }

                int currencyFlags = currencyFractionDigits;
                String extraData = currencyExtra.getProperty(currencyCode);
                String portableSymbol = "";
                if (extraData != null) {
                    // CurrencyExtra contains up to 3 fields separated by |
                    // 0 - portable currency symbol
                    // 1 - space-separated flags regarding currency symbol
                    // positioning/spacing
                    // 2 - override of CLDR-derived currency symbol
                    String[] extraSplit = extraData.split("\\|");
                    portableSymbol = extraSplit[0];
                    if (extraSplit.length > 1) {
                        if (extraSplit[1].contains("SymPrefix")) {
                            currencyFlags |= CurrencyDataImpl.POS_FIXED_FLAG;
                        } else if (extraSplit[1].contains("SymSuffix")) {
                            currencyFlags |= CurrencyDataImpl.POS_FIXED_FLAG
                                    | CurrencyDataImpl.POS_SUFFIX_FLAG;
                        }
                        if (extraSplit[1].contains("ForceSpace")) {
                            currencyFlags |= CurrencyDataImpl.SPACING_FIXED_FLAG
                                    | CurrencyDataImpl.SPACE_FORCED_FLAG;
                        } else if (extraSplit[1].contains("ForceNoSpace")) {
                            currencyFlags |= CurrencyDataImpl.SPACING_FIXED_FLAG;
                        }
                    }
                    // If a non-empty override is supplied, use it for the currency
                    // symbol.
                    if (extraSplit.length > 2 && extraSplit[2].length() > 0) {
                        currencySymbol = extraSplit[2];
                    }
                    // If we don't have a currency symbol yet, use the portable
                    // symbol if
                    // supplied.
                    if (currencySymbol == null && portableSymbol.length() > 0) {
                        currencySymbol = portableSymbol;
                    }
                }
                // If all else fails, use the currency code as the symbol.
                if (currencySymbol == null) {
                    currencySymbol = currencyCode;
                }

                if (currencyCode.equals(defCurrencyCode)) {
                    return new CurrencyDataImpl(currencyCode, currencySymbol, currencyFlags,
                            portableSymbol);
                }
            }

            return defCurrencyData;
        }

    }

    private static final CurrencyDataHolder CURRENCY_DATA_HOLDER = new CurrencyDataHolder();

    @PatchMethod
    static CurrencyData getDefaultJava(CurrencyList currencyList) {
        Locale locale = GwtConfig.get().getModuleRunner().getLocale();
        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        return CURRENCY_DATA_HOLDER.getCurrencyData(locale);
    }

}

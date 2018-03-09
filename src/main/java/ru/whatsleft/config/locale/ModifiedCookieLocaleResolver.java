package ru.whatsleft.config.locale;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class ModifiedCookieLocaleResolver extends CookieLocaleResolver {

    /**
     * Locale resolver first searches for Accept-language header, then, if null, falls back to default locale
     *
     * @param request
     * @return
     */
    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        Locale defaultLocale = request.getLocale();
        if (defaultLocale == null) {
            defaultLocale = getDefaultLocale();
        }
        return defaultLocale;
    }
}

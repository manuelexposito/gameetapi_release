package com.salesianostriana.gameetapi.errors.model.messages;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public static String getMessageForLocale(String messageKey, Locale locale){

        return ResourceBundle.getBundle("errors", locale).getString(messageKey);

    }

}

package com.teamwe.personalizedreader;

import com.teamwe.personalizedreader.model.Category;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by igorpetrovski on 2/9/16.
 */
public class GlobalInfo {

    public static String NEWS_TITLE = "newsTitle";
    public static String NEWS_URL = "newsUrl";
    public static String LIST_NEWS = "listNews";
    public static String SERVER_IP = "http://mojpersonalizirancitac.appspot.com/";



    public static String PERSONALIZATION_STR = "isPersonalized";
    public static final String APP_HAS_BEEN_OPEN = "notFirstTime";
    public static final String CALL_FROM_MAIN_ACTIVITY = "callFromMainActivity";

    public static String CAT_SPECIFICATION_PREF="categoriesSpecification";
    public static final String SOURCES_SPECIFICATION_PREF = "sourcesSpecification";

    public static final String SELECTED_SOURCES = "wantedSources";
    public static final String SELECTED_CATEGORIES = "wantedCategories";

    public static final String UNSELECTED_CATEGORIES = "unwantedCategories";

    public static final int HOW_MUCH_TOP_NEWS = 23;

    public static Random rg = new Random();

    public static String configureDate(long datePostedMillis) {



        if(0 == datePostedMillis) {
            return " - ";
        }

        StringBuilder textToRet = new StringBuilder();
        long diffInSecs = (System.currentTimeMillis() - datePostedMillis) / 1000;
        long diffInMins = diffInSecs / 60;
        long diffInHours = diffInSecs / (60 * 60);
        long diffInDays = diffInSecs / (24 * 60 * 60);

        if (diffInSecs < 120) {
            return " - објавено сега";
        }

        if (diffInHours == 0) { // znachi mozhe da se izrazime vo minuti

            if (diffInMins <= 0)  // vo sluchaj da e losh chasovnikot na telefonot da ne se dava gjubre podatok kako -27m
                return "";

            return String.format(" - пред %d минути", diffInMins);
        }

        if (diffInDays == 0) { // mozhe da se irazime vo satovi

            if (diffInHours <= 0)  // vo sluchaj da e losh chasovnikot na telefonot
                return "";         // da ne se dava gjubre podatok kako -27h


            return String.format(" - пред %d час%c", diffInHours, diffInHours == 1 ? ' ':'а');
        }


        if (diffInDays <= 0)  // vo sluchaj da e losh chasovnikot na telefonot
            return "";         // da ne se dava gjubre podatok kako -27h


        return String.format(" - пред %d ден%c", diffInDays, diffInDays == 1 ? ' ':'а');

    }
}

package com.teamwe.personalizedreader.mynews;

import com.teamwe.personalizedreader.model.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by igorpetrovski on 2/9/16.
 */
public class GlobalInfo {

    public static String NEWS_TITLE = "newsTitle";
    public static String NEWS_URL = "newsUrl";
    public static String LIST_NEWS = "listNews";
    public static String SERVER_IP = "192.168.0.101:8083";
    public static List<Category> CATEGORIES = new ArrayList<Category>() {{
        add(new Category("MAKEDONIJA","Македонија"));
        add(new Category("EKONOMIJA", "Економија"));
        add(new Category("SVET", "Свет"));
        add(new Category("FUDBAL", "Фудбал"));
        add(new Category("ZDRAVJE", "Здравје"));
        add(new Category("TEHNOLOGIJA","Технологија"));

    }};


    public static Set<String> SOME_SOURCES = new HashSet<String>(){{

        for(int i=0; i<43; i++) {
            add("" + i);
        }

    }};

    public static String PERSONALIZATION_STR = "isPersonalized";
    public static final String APP_HAS_BEEN_OPEN = "notFirstTime";

    public static String CAT_SPECIFICATION_PREF="categoriesSpecification";
    public static final String SOURCES_SPECIFICATION_PREF = "sourcesSpecification";
    public static final String WANTED_SOURCES = "wantedSources";
}

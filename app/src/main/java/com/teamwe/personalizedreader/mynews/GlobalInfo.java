package com.teamwe.personalizedreader.mynews;

import com.teamwe.personalizedreader.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorpetrovski on 2/9/16.
 */
public class GlobalInfo {


    public static String SERVER_IP = "192.168.43.109:8080";
    public static List<Category> CATEGORIES = new ArrayList<Category>() {{
        add(new Category("MAKEDONIJA","Македонија"));
        add(new Category("EKONOMIJA", "Економија"));
        add(new Category("SVET", "Свет"));
        add(new Category("FUDBAL", "Фудбал"));
        add(new Category("ZDRAVJE", "Здравје"));
        add(new Category("TEHNOLOGIJA","Технологија"));

    }};

    public static String PERSONALIZATION_STR = "isPersonalized";
    public static final String APP_HAS_BEEN_OPEN = "notFirstTime";

    public static String CAT_SPECIFICATION_PREF="categoriesSpecification";
}

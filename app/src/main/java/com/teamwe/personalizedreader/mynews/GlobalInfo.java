package com.teamwe.personalizedreader.mynews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorpetrovski on 2/9/16.
 */
public class GlobalInfo {


    public static String SERVER_IP = "192.168.0.103:8083";
    public static List<Category> CATEGORIES = new ArrayList<Category>() {{
        add(new Category("MAKEDONIJA","Македонија"));
        add(new Category("EKONOMIJA", "Економија"));
        add(new Category("SVET", "Свет"));
        add(new Category("FUDBAL", "Фудбал"));
        add(new Category("ZDRAVJE", "Здравје"));
        add(new Category("TEHNOLOGIJA","Технологија"));

    }};

}

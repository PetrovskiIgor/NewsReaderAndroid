package com.teamwe.personalizedreader.mynews;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teamwe.personalizedreader.adapters.ClusterAdapter;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.tasks.GetNewsTask;
import com.teamwe.personalizedreader.tasks.OnNewsHere;

import java.util.ArrayList;
import java.util.List;


public class FragmentNews extends Fragment {

    ListView listViewNews;
    ClusterAdapter adapter;
    List<Cluster> clusters;

    Activity parent;

    Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View toRet = inflater.inflate(R.layout.fragment_fragment_news, container, false);

        listViewNews = (ListView)toRet.findViewById(R.id.listViewNews);
        clusters = new ArrayList<>();




//        ArrayList<NewsPost> listPosts = new ArrayList<NewsPost> ();
//        listPosts.add(new NewsPost("http://www.makdenes.org/content/article/27524103.html","Макденес","Јанева не коментира дали обвинителките оддавале тајни"));
//        listPosts.add(new NewsPost("http://www.vest.mk/default.asp?ItemID=D89CE6232DD21840AC022BDF3B47EA58","Вест","ЗА ДВЕТЕ ИЗБРКАНИ ОБВИНИТЕЛКИ НЕМА ЗАКОНСКА РЕГУЛАТИВА"));
//        listPosts.add(new NewsPost("http://24vesti.mk/janeva-jas-odluchuvam-koj-kje-raboti-vo-mojot-tim","24Вести","Јанева: Јас одлучувам кој ќе работи во мојот тим"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost> ();
//        listPosts.add(new NewsPost("http://mk.voanews.com/content/voa-macedonian-strike-diplomats-skopje/3171777.html","Воаденес","Дипломатски штрајк во Македонија"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost>();
//        listPosts.add(new NewsPost("http://novatv.mk/index.php?navig=8&cat=2&vest=27023","Нова","ПИОМ со долг од 440 милиони денари за само две недели"));
//        listPosts.add(new NewsPost("http://faktor.mk/2016/02/01/piom-tone-vo-milionski-dolgovi-ke-stradaat-li-penziite/","Фактор","ПИОМ тоне во милионски долгови, ќе страдаат ли пензиите?"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost> ();
//        listPosts.add(new NewsPost("http://alsat.mk/News/234981/tachi-se-zakani-so-napushtawe-na-pregovorite-od-przhino","Алсат","Тачи се закани со напуштање на преговорите од Пржино"));
//        listPosts.add(new NewsPost("http://24vesti.mk/tachi-na-chekor-da-go-napushti-przhinskiot-dogovor","24Вести","Тачи на чекор да го напушти пржинскиот договор"));
//        listPosts.add(new NewsPost("http://telma.com.mk/vesti/tachi-mozhno-e-povlekuvanje-na-dpa-od-pregovorite","Телма","Тачи: Можно е повлекување на ДПА од преговорите"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost> ();
//        listPosts.add(new NewsPost("http://www.makdenes.org/content/article/27524103.html","Макденес","Јанева не коментира дали обвинителките оддавале тајни"));
//        listPosts.add(new NewsPost("http://www.vest.mk/default.asp?ItemID=D89CE6232DD21840AC022BDF3B47EA58","Вест","ЗА ДВЕТЕ ИЗБРКАНИ ОБВИНИТЕЛКИ НЕМА ЗАКОНСКА РЕГУЛАТИВА"));
//        listPosts.add(new NewsPost("http://24vesti.mk/janeva-jas-odluchuvam-koj-kje-raboti-vo-mojot-tim","24Вести","Јанева: Јас одлучувам кој ќе работи во мојот тим"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost> ();
//        listPosts.add(new NewsPost("http://mk.voanews.com/content/voa-macedonian-strike-diplomats-skopje/3171777.html","Воаденес","Дипломатски штрајк во Македонија"));
//        listPosts.add(new NewsPost("http://www.vest.mk/default.asp?ItemID=B3FFF0ACC0E89B4395552CFB968E88BB","Вест","ДИПЛОМАТИТЕ БАРААТ ПРОФЕСИОНАЛИЗАМ ВО СЛУЖБАТА"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost>();
//        listPosts.add(new NewsPost("http://novatv.mk/index.php?navig=8&cat=2&vest=27023","Нова","ПИОМ со долг од 440 милиони денари за само две недели"));
//        listPosts.add(new NewsPost("http://faktor.mk/2016/02/01/piom-tone-vo-milionski-dolgovi-ke-stradaat-li-penziite/","Фактор","ПИОМ тоне во милионски долгови, ќе страдаат ли пензиите?"));
//        clusters.add(new Cluster(listPosts));
//
//        listPosts = new ArrayList<NewsPost> ();
//        listPosts.add(new NewsPost("http://alsat.mk/News/234981/tachi-se-zakani-so-napushtawe-na-pregovorite-od-przhino","Алсат","Тачи се закани со напуштање на преговорите од Пржино"));
//        listPosts.add(new NewsPost("http://24vesti.mk/tachi-na-chekor-da-go-napushti-przhinskiot-dogovor","24Вести","Тачи на чекор да го напушти пржинскиот договор"));
//        listPosts.add(new NewsPost("http://telma.com.mk/vesti/tachi-mozhno-e-povlekuvanje-na-dpa-od-pregovorite","Телма","Тачи: Можно е повлекување на ДПА од преговорите"));
//        clusters.add(new Cluster(listPosts));


        adapter = new ClusterAdapter(parent,clusters);

        listViewNews.setAdapter(adapter);


        new GetNewsTask(new OnNewsHere() {
            @Override
            public void onTaskCompleted(List<Cluster> list) {
                clusters = list;
                adapter = new ClusterAdapter(parent,clusters);
                listViewNews.setAdapter(adapter);
            }
        }).execute(category);


        this.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.startDialog(clusters.get(position));
            }
        });

        return toRet;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.parent = context;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

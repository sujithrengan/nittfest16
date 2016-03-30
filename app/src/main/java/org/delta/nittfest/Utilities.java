package org.delta.nittfest;

import android.content.SharedPreferences;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by HP on 03-02-2016.
 */
public class Utilities {

    public static int status;
    public static int locked=0;

    public static int gcm_registered = 0;
    public static int pid;
    public static String url_gcm = "http://api.nittfest.in/simple-gcm/register.php";


    public static String url_scores="http://api.nittfest.in/leaderboard";
    public static Department[] departments;
    public static Events[] events;
    public static HashMap<Integer,Integer> eventMap;
    public static SharedPreferences sp;
    public static String url_auth="http://api.nittfest.in/betting/auth";
    public static String username;
    public static String password;
    public static int credits_available;
    public static String url_getprofile="http://api.nittfest.in/betting/getProfile";
    public static String url_getevents="http://api.nittfest.in/betting/getEventsStatus";
    public static String url_getdistribution="http://api.nittfest.in/betting/betDistribution";
    public static String url_placebet="http://api.nittfest.in/betting/placeBet";

    static class ScoreComparator implements Comparator<Department> {
        @Override
        public int compare(Department department, Department department2) {

            return Float.compare(department2.score,department.score);
        }
    }
    public static void sortScores()
    {

        for(int i=0;i<departments.length;i++)
        {
            departments[i].old_position=i;
        }

        Arrays.sort(departments, new ScoreComparator());
    }
    public static String getCluster(int event_id){
       switch(event_id){
           case 56:
           case 61: return "Hindi Lits";

           case 35:return "Design and Media";
           default: return "Culturals";


       }
    }



}

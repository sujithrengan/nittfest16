package org.delta.nittfest;

import android.content.SharedPreferences;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by HP on 03-02-2016.
 */
public class Utilities {

    public static String status;
    public static int locked=0;
    public static String url_scores="http://192.168.1.104/nittfest/scores.php";
    public static Department[] departments;
    public static SharedPreferences sp;

    static class ScoreComparator implements Comparator<Department> {
        @Override
        public int compare(Department department, Department department2) {

            return Float.compare(department2.score,department.score);
        }
    }
    public static void sortScores()
    {



        Arrays.sort(departments, new ScoreComparator());
    }

    public static void init(DBController db)
    {
        departments=new Department[12];

    if(locked==0) {
        for (int i = 0; i < 12; i++)
            departments[i].old_position=i;
    }
        else departments=db.getAllScores();
    }
}



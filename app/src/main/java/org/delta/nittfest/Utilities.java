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
    public static String url_scores="https://8f543305.ngrok.io/nittfest/scores.php";
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

        for(int i=0;i<departments.length;i++)
        {
            departments[i].old_position=i;
        }

        Arrays.sort(departments, new ScoreComparator());
    }
}



package org.delta.nittfest;

/**
 * Created by HP on 05-02-2016.
 */
public class Department {

    public String name;
    public float score;
    //public int cur_position;
    public int old_position;

    public void SetDepartment(String name,float score)
    {
        this.name=name;
        this.score=score;

    }



    Department(String name,float score,int old_position)
    {
        this.name=name;
        this.score=score;
        this.old_position=old_position;
    }
}

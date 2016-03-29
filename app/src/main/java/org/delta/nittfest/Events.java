package org.delta.nittfest;

/**
 * Created by bharath on 29/3/16.
 */
public class Events {

    //private variables
    int _id;
    String _name;
    String _cluster;
    int _status;
    int _credits;
    public Events(){

    }

    // constructor
    public Events(int id, String name, String cluster,int status,int credits){
        this._id = id;
        this._name = name;
        this._cluster = cluster;
        this._status = status;
        this._credits = credits;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_cluster() {
        return _cluster;
    }

    public void set_cluster(String _cluster) {
        this._cluster = _cluster;
    }

    public int get_status() {
        return _status;
    }

    public void set_status(int _status) {
        this._status = _status;
    }

    public int get_credits() {
        return _credits;
    }

    public void set_credits(int _credits) {
        this._credits = _credits;
    }
}
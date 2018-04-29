package com.example.a15520.newdictionary_ver2;

/**
 * Created by 15520 on 12/15/2017.
 */

public class IdiomModel
{
    public IdiomModel(String _idiom, String _mean) {
        this._idiom = _idiom;
        this._mean = _mean;
    }

    private String _idiom;
    private String _mean;

    public String get_idiom() {
        return _idiom;
    }

    public void set_idiom(String _idiom) {
        this._idiom = _idiom;
    }

    public String get_mean() {
        return _mean;
    }

    public void set_mean(String _mean) {
        this._mean = _mean;
    }
}

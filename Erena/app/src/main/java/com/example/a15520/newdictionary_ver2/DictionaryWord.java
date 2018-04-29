package com.example.a15520.newdictionary_ver2;

/**
 * Created by 15520 on 11/25/2017.
 */
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;


public class DictionaryWord {
    public DictionaryWord() {
        this._dictionary = new HashMap<>();
    }

    private HashMap<String, String> _dictionary;

    public void Add(String Word, String Mean) {
        _dictionary.put(Word, Mean);
    }

    public String Find(String Word) {
        if (_dictionary.get(Word) == null)
            return "Khong tim thay";
        return _dictionary.get(Word);
    }

    public boolean IsContain(String Key){
        return _dictionary.containsKey(Key);
    }
}
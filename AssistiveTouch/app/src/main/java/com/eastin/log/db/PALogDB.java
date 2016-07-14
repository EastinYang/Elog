package com.eastin.log.db;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by Eastin on 16/7/10.
 */
public class PALogDB {
    protected static LiteOrm liteOrm;

    public PALogDB(Context context) {
        if(liteOrm==null)
        {
            liteOrm = LiteOrm.newSingleInstance(context, "palog.db");
        }
        liteOrm.setDebugged(true);
    }

}

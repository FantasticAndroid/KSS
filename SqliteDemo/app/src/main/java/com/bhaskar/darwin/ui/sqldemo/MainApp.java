package com.bhaskar.darwin.ui.sqldemo;

import android.app.Application;

import com.bhaskar.darwin.ui.sqldemo.sqlimpl.OfferSqlAdapter;

public class MainApp extends Application {
    private OfferSqlAdapter offerSqlAdapter;
    @Override
    public void onCreate() {
        super.onCreate();
        initMegaDataBase();
    }

    /**
     * Initialize and open DataBase with Database ADAPTER to connect to the
     * database when app start. Each and every database operation perform using
     * dbAdapter
     */
    private void initMegaDataBase() {
        offerSqlAdapter = new OfferSqlAdapter(this, getString(R.string.mega_offer_db_name),
                getResources().getInteger(R.integer.mega_offer_db_version));
        offerSqlAdapter.open();
    }

    public OfferSqlAdapter getMegaOfferAdapter() {
        if (offerSqlAdapter == null) {
            initMegaDataBase();
        }
        return offerSqlAdapter;
    }
}

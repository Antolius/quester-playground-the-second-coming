package com.quester.experiment.dagger2experiment.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.quester.experiment.dagger2experiment.R;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storable;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import java.io.File;
import java.util.List;

public class StorageTestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_test);

        Storage storage = SimpleStorage.getInternalStorage(this);
        storage.createDirectory("Quests", false);
        List<File> files = storage.getFiles("Quests", OrderType.DATE);
        //napravi activity koji
        //stvori direktorij u internal storage,
        //zatim stvori nekoliko fajlova,
        //zatim ih prikaže u listi
        //te prikaže content na odabir fajla

        //dodatno:
        //dohvati fajlove u external storage
        //pretraži ih u potrazi za questovima
        //prikaži ih u listi
        //instaliraj odabrani quest
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_storage_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

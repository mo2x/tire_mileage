package com.example.tiremileage.net;

import android.content.Context;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.tiremileage.Repository;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

public class DBCreator extends RoomDatabase.Callback  {
    Context context;

    public DBCreator(Context context){
        this.context = context;
    }
    @Override
    public void onCreate(@NotNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        JSONParser parser = new JSONParser();
        try {
            Repository repository = new Repository();
            repository.insert(context, parser.getTires(context));
            repository.insert(context, parser.getTracks(context));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

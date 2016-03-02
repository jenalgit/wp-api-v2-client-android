package com.afrozaar.wp_api_v2_client_android.data.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.afrozaar.wp_api_v2_client_android.data.WordPressDatabase;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/02/11.
 */
public abstract class WpDatabaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private WordPressDatabase database;

    private DatabaseTaskCallback<Result> callback;

    protected WpDatabaseTask(Context context, DatabaseTaskCallback<Result> callback) {
        database = new WordPressDatabase(context);

        this.callback = callback;
    }

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return exec();
        } catch (Exception e) {
            cancel(true);
            if (callback != null) {
                callback.onTaskFailure(e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (callback != null) {
            if (result != null) {
                callback.onTaskSuccess(result);
            } else {
                callback.onTaskResultNull();
            }
        }
    }

    protected abstract Result exec() throws Exception;

    protected SQLiteDatabase getReadableDatabase() {
        return database.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase() {
        return database.getWritableDatabase();
    }
}
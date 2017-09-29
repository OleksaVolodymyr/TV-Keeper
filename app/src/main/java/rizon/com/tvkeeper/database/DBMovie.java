package rizon.com.tvkeeper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rizon.com.tvkeeper.model.Movie;
import rizon.com.tvkeeper.net.Utils;


public class DBMovie extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MovieDB.db";
    private static final String DB_TABLE_NAME = "Movie";
    private static final String MOVIE_COLUMN_ID = "id";
    private static final String MOVIE_COLUMN_IMDBID = "imdbID";
    private static final String MOVIE_COLUMN_TITLE = "title";
    private static final String MOVIE_COLUMN_YEAR = "year";
    private static final String MOVIE_COLUMN_RATED = "rated";
    private static final String MOVIE_COLUMN_RELEASED = "released";
    private static final String MOVIE_COLUMN_RUNTIME = "runtime";
    private static final String MOVIE_COLUMN_GENRE = "genre";
    private static final String MOVIE_COLUMN_PLOT = "plot";
    private static final String MOVIE_COLUMN_POSTER = "poster";
    private static final String MOVIE_COLUMN_RATING = "rating";
    private Context context;

    public DBMovie(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME + " ( " + MOVIE_COLUMN_ID +
                        " integer primary key autoincrement, " +
                        MOVIE_COLUMN_IMDBID + " integer, " + MOVIE_COLUMN_TITLE + " text, "
                        + MOVIE_COLUMN_YEAR + " text, " + MOVIE_COLUMN_RATED + " text, " +
                        MOVIE_COLUMN_RELEASED + " text, " + MOVIE_COLUMN_RUNTIME + " text, " +
                        MOVIE_COLUMN_GENRE + " text, " + MOVIE_COLUMN_PLOT + " text, " +
                        MOVIE_COLUMN_POSTER + " text, " + MOVIE_COLUMN_RATING + " text" +
                        ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DB_TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(final Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_COLUMN_IMDBID, movie.getImdbID());
        contentValues.put(MOVIE_COLUMN_TITLE, movie.getTitle());
        contentValues.put(MOVIE_COLUMN_YEAR, movie.getYear());
        contentValues.put(MOVIE_COLUMN_RATED, movie.getRated());
        contentValues.put(MOVIE_COLUMN_RELEASED, movie.getReleased());
        contentValues.put(MOVIE_COLUMN_RUNTIME, movie.getRuntime());
        contentValues.put(MOVIE_COLUMN_GENRE, movie.getGenre());
        contentValues.put(MOVIE_COLUMN_PLOT, movie.getPlot());
        contentValues.put(MOVIE_COLUMN_RATING, movie.getRating());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos;
                    fos = context.openFileOutput(movie.getTitle() + ".jpg",
                            Context.MODE_PRIVATE);
                    fos.write(downloadImage(movie.getPosterPath()));
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println(context.getFileStreamPath(movie.getTitle() + ".jpg"));
        contentValues.put(MOVIE_COLUMN_POSTER, context.getFileStreamPath(movie.getTitle() + "" +
                ".jpg").toString());
        db.insert(DB_TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + DB_TABLE_NAME);
    }

    public void createTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    public void update(int id) {

    }

    public int deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE_NAME, "id = " + id, null);
    }

    public List<Movie> getAllMovieFromDB() {
        List<Movie> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Movie", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                list.add((Movie) Utils.toInstance(res, Movie.class));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return list;
    }

    private byte[] downloadImage(String path) {
        URL url = null;
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        try {
            url = new URL(path);
            in = new BufferedInputStream(url.openStream());
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


}

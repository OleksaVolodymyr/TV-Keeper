package rizon.com.tvkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import rizon.com.tvkeeper.R;
import rizon.com.tvkeeper.model.Movie;


public class CustomAdapter<T> extends ArrayAdapter<T> {
    private Movie movie;
    private List<T> list;
    private Activity activity;


    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T>
            objects) {
        super(context, resource, objects);
        this.list = objects;
        this.activity = (Activity) context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        movie = (Movie) list.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.item_element, parent, false);
        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(movie.getTitle());
        TextView year = (TextView) row.findViewById(R.id.year);
        year.setText(movie.getYear());
        TextView imdbID = (TextView) row.findViewById(R.id.imdbID);
        imdbID.setText("ImdbID: " + movie.getImdbID());
        TextView released = (TextView) row.findViewById(R.id.released);
        released.setText("Released: " + movie.getReleased());
        TextView genre = (TextView) row.findViewById(R.id.genre);
        genre.setText(movie.getGenre());
        TextView rating = (TextView) row.findViewById(R.id.rating);
        rating.setText(new Double(movie.getRating()).toString());
        TextView rated = (TextView) row.findViewById(R.id.rated);
        rated.setText(movie.getRated());
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(movie.getPosterPath());
//                    decodeStream(new URL(movie.getPosterPath())
//                    .openConnection().getInputStream());

        imageView.setImageBitmap(bitmap);
//        new DownloadImageTask((ImageView) row.findViewById(R.id.imageView))
//                .execute(movie.getPosterPath());
        this.notifyDataSetChanged();
        return row;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(movie.getPosterPath())
                        .openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);

        }
    }
}

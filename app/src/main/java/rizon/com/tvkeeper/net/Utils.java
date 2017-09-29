package rizon.com.tvkeeper.net;

import android.database.Cursor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import rizon.com.tvkeeper.annotation.Column;
import rizon.com.tvkeeper.model.Movie;


public class Utils {

    public String getJsonFromRequest(String imdbID) {
        String address = "http://imdbapi.net/api?id=" + imdbID +
                "&key=1kvouUVrQ3QsVqDXh1r6hOR3HRYWsv";
        URL url = null;
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                return null;
            }
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static <T> T toInstance(Cursor cursor, Class<?> clazz)
            throws InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        @SuppressWarnings("unchecked")
        T element = (T) clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                Object value = cursor.getString(cursor.getColumnIndex(field.getAnnotation(Column
                        .class).value()));
                field.set(element, parse(field, value.toString()));
            }
        }
        return element;
    }

    private static Object parse(Field field, String value)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Object parseValue = null;
        if (field.getType().isPrimitive()) {
            if (field.getType().equals(int.class)) {
                parseValue = Integer.parseInt(value);
            } else if (field.getClass().equals(double.class)) {
                parseValue = Double.parseDouble(value);
            } else if (field.getClass().equals(float.class)) {
                parseValue = Float.parseFloat(value);
            } else if (field.getClass().equals(byte.class)) {
                parseValue = Byte.parseByte(value);
            } else if (field.getClass().equals(boolean.class)) {
                parseValue = Boolean.parseBoolean(value);
            } else if (field.getClass().equals(short.class)) {
                parseValue = Short.parseShort(value);
            } else if (field.getClass().equals(long.class)) {
                parseValue = Long.parseLong(value);
            }
        } else {
            parseValue = field.getType().getConstructor(String.class).newInstance(value);
        }
        return parseValue;
    }

    public boolean isMovieAlreadyExist(String imdbID, List<Movie> movies) {
        boolean exist = false;
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getImdbID().equals(imdbID.trim())) {
                exist = true;
            }
        }
        return exist;
    }
}

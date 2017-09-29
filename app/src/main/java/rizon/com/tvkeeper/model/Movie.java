package rizon.com.tvkeeper.model;


import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import rizon.com.tvkeeper.annotation.Column;

@JsonIgnoreProperties(value = {"director", "writers", "actors", "country", "language", "metascore",
        "opening_weekend",
        "budget", "gross", "production", "type", "status", "votes"})
public class Movie implements Serializable {
    @Column("id")
    private Integer id;
    @JsonProperty(value = "imdb_id")
    @Column("imdbID")
    private String imdbID;
    @JsonProperty(value = "title")
    @Column("title")
    private String title;
    @JsonProperty(value = "year")
    @Column("year")
    private String year;
    @JsonProperty(value = "rated")
    @Column("rated")
    private String rated;
    @JsonProperty(value = "released")
    @Column("released")
    private String released;
    @JsonProperty(value = "runtime")
    @Column("runtime")
    private String runtime;
    @JsonProperty(value = "genre")
    @Column("genre")
    private String genre;
    @JsonProperty(value = "plot")
    @Column("plot")
    private String plot;
    @JsonProperty(value = "poster")
    @Column("poster")
    private String posterPath;
    @JsonProperty(value = "rating")
    @Column("rating")
    private Double rating;

    public Movie() {}

    public Movie(String imdbID, String title, String year, String rated, String released,
                 String runtime, String genre, String plot, String posterPath, Double rating) {
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.plot = plot;
        this.posterPath = posterPath;
        this.rating = rating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", rated='" + rated + '\'' +
                ", released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genre='" + genre + '\'' +
                ", plot='" + plot + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return this.getImdbID().equals(obj.toString().trim());
    }
}

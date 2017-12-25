package com.example.cz.firstapllicatonnanodegree.Model;

/**
 * Created by cz on 25/12/2017.
 */

public class Movie_model {
    String  poster,overview, release_date,vote_average,title;


    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }
}

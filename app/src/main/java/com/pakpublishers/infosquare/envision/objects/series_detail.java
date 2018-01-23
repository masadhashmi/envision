package com.pakpublishers.infosquare.envision.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by M Asad Hashmi on 10/10/2016.
 */

public class series_detail {
    String view_list_datetime;
    String
            titlecard;
    String
            team2;
    String
            team1;
    String
            subtitled;
    String
            status;
    String
            station_id;
    String
            star_rating;
    String
            show_year;
    String
            show_type_id;
    String
            show_type;
    String
            show_name;
    String
            show_id;
    String
            show_guest;
    String
            showcard;
    String
            series_premiere;
    String
            series_id;
    String
            series_finale;
    String
            season_premiere;
    String
            season_finale;
    String
            schedule_id;
    String
            repeat_time;
    String
            recording_id;
    String
            rating;
    String
            property;
    String
            programming;
    String
            poster;
    String
            overview;
    String
            Nnew;
    String
            movie_still;
    String
            movie_poster;
    String
            location;
    String
            live_url_ios;
    String
            live_url;
    String
            live;
    String
            list_datetime;
    String
            listing_id;
    String
            league;
    String
            in_progress;
    String
            hd;
    String
            event;
    String
            episodic;
    String
            episode_title;
    String
            episode_part_num;
    String
            episode_parts;
    String
            episode_number;
    String
            educational;
    String
            duration;
    String
            director;
    String
            descriptive_video;
    String
            description;
    String
            cast;
    String
            captioned;
    String
            breakout_level;
    String
            blackWhite;

    public void parse(JSONObject ch) throws JSONException {


        try{setview_list_datetime(ch.get("view_list_datetime").toString());}catch (Exception er){}
        try{setTitlecard(ch.get("titlecard").toString());}catch (Exception er){}
        try{setTeam2(ch.get("team2").toString());}catch (Exception er){}
        try{setTeam1(ch.get("team1").toString());}catch (Exception er){}
        try{setSubtitled(ch.get("subtitled").toString());}catch (Exception er){}
        try{setStatus(ch.get("status").toString());}catch (Exception er){}
        try{setStation_id(ch.get("station_id").toString());}catch (Exception er){}
        try{setStar_rating(ch.get("star_rating").toString());}catch (Exception er){}
        try{setShow_year(ch.get("show_year").toString());}catch (Exception er){}
        try{setShow_type_id(ch.get("show_type_id").toString());}catch (Exception er){}
        try{setShow_type(ch.get("show_type").toString());}catch (Exception er){}
        try{setShow_name(ch.get("show_name").toString());}catch (Exception er){}
        try{setShow_id(ch.get("show_id").toString());}catch (Exception er){}
        try{setShow_guest(ch.get("show_guest").toString());}catch (Exception er){}
        try{setShowcard(ch.get("showcard").toString());}catch (Exception er){}
        try{setSeries_premiere(ch.get("series_premiere").toString());}catch (Exception er){}
        try{setSeries_id(ch.get("series_id").toString());}catch (Exception er){}
        try{setSeries_finale(ch.get("series_finale").toString());}catch (Exception er){}
        try{setSeason_premiere(ch.get("season_premiere").toString());}catch (Exception er){}
        try{setSeason_finale(ch.get("season_finale").toString());}catch (Exception er){}
        try{setSchedule_id(ch.get("schedule_id").toString());}catch (Exception er){}
        try{setRepeat_time(ch.get("repeat_time").toString());}catch (Exception er){}
        try{setRecording_id(ch.get("recording_id").toString());}catch (Exception er){}
        try{setRating(ch.get("rating").toString());}catch (Exception er){}
        try{setProperty(ch.get("property").toString());}catch (Exception er){}
        try{setProgramming(ch.get("programming").toString());}catch (Exception er){}
        try{setPoster(ch.get("poster").toString());}catch (Exception er){}
        try{setNnew(ch.get("new").toString());}catch (Exception er){}
        try{setMovie_still(ch.get("movie_still").toString());}catch (Exception er){}
        try{setMovie_poster(ch.get("movie_poster").toString());}catch (Exception er){}
        try{setLocation(ch.get("location").toString());}catch (Exception er){}
        try{setLive(ch.get("live").toString());}catch (Exception er){}
        try{setList_datetime(ch.get("list_datetime").toString());}catch (Exception er){}
        try{setListing_id(ch.get("listing_id").toString());}catch (Exception er){}
        try{setLeague(ch.get("league").toString());}catch (Exception er){}
        try{setIn_progress(ch.get("in_progress").toString());}catch (Exception er){}
        try{setHd(ch.get("hd").toString());}catch (Exception er){}
        try{setEvent(ch.get("event").toString());}catch (Exception er){}
        try{setEpisodic(ch.get("episodic").toString());}catch (Exception er){}
        try{setEpisode_title(ch.get("episode_title").toString());}catch (Exception er){}
        try{setEpisode_part_num(ch.get("episode_part_num").toString());}catch (Exception er){}
        try{setEpisode_parts(ch.get("episode_parts").toString());}catch (Exception er){}
        try{setEpisode_number(ch.get("episode_number").toString());}catch (Exception er){}
        //try{setepiso(ch.get("episode_no").toString());}catch (Exception er){}
        try{setEducational(ch.get("educational").toString());}catch (Exception er){}
        try{setDuration(ch.get("duration").toString());}catch (Exception er){}
        try{setDirector(ch.get("director").toString());}catch (Exception er){}
        try{setDescriptive_video(ch.get("descriptive_video").toString());}catch (Exception er){}
        try{setDescription(ch.get("description").toString());}catch (Exception er){}
        try{setCast(ch.get("cast").toString());}catch (Exception er){}
        try{setCaptioned(ch.get("captioned").toString());}catch (Exception er){}
        try{setBreakout_level(ch.get("breakout_level").toString());}catch (Exception er){}
        try{setBlackWhite(ch.get("blackWhite").toString());}catch (Exception er){}
        try{ setLive_url(ch.get("live_url").toString()); }catch (Exception er){}
        //set(ch.get("").toString());








    }

    public String getview_list_datetime() {
        return view_list_datetime;
    }

    public void setview_list_datetime(String etime) {
        this.view_list_datetime = view_list_datetime;
    }

    public String getTitlecard() {
        return titlecard;
    }

    public void setTitlecard(String titlecard) {
        this.titlecard = titlecard;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getSubtitled() {
        return subtitled;
    }

    public void setSubtitled(String subtitled) {
        this.subtitled = subtitled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(String star_rating) {
        this.star_rating = star_rating;
    }

    public String getShow_year() {
        return show_year;
    }

    public void setShow_year(String show_year) {
        this.show_year = show_year;
    }

    public String getShow_type_id() {
        return show_type_id;
    }

    public void setShow_type_id(String show_type_id) {
        this.show_type_id = show_type_id;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public String getShow_guest() {
        return show_guest;
    }

    public void setShow_guest(String show_guest) {
        this.show_guest = show_guest;
    }

    public String getShowcard() {
        return showcard;
    }

    public void setShowcard(String showcard) {
        this.showcard = showcard;
    }

    public String getSeries_premiere() {
        return series_premiere;
    }

    public void setSeries_premiere(String series_premiere) {
        this.series_premiere = series_premiere;
    }

    public String getSeries_id() {
        return series_id;
    }

    public void setSeries_id(String series_id) {
        this.series_id = series_id;
    }

    public String getSeries_finale() {
        return series_finale;
    }

    public void setSeries_finale(String series_finale) {
        this.series_finale = series_finale;
    }

    public String getSeason_premiere() {
        return season_premiere;
    }

    public void setSeason_premiere(String season_premiere) {
        this.season_premiere = season_premiere;
    }

    public String getSeason_finale() {
        return season_finale;
    }

    public void setSeason_finale(String season_finale) {
        this.season_finale = season_finale;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getRepeat_time() {
        return repeat_time;
    }

    public void setRepeat_time(String repeat_time) {
        this.repeat_time = repeat_time;
    }

    public String getRecording_id() {
        return recording_id;
    }

    public void setRecording_id(String recording_id) {
        this.recording_id = recording_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProgramming() {
        return programming;
    }

    public void setProgramming(String programming) {
        this.programming = programming;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getNnew() {
        return Nnew;
    }

    public void setNnew(String nnew) {
        Nnew = nnew;
    }

    public String getMovie_still() {
        return movie_still;
    }

    public void setMovie_still(String movie_still) {
        this.movie_still = movie_still;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster = movie_poster;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLive_url_ios() {
        return live_url_ios;
    }

    public void setLive_url_ios(String live_url_ios) {
        this.live_url_ios = live_url_ios;
    }

    public String getLive_url() {
        return live_url;
    }

    public void setLive_url(String live_url) {
        this.live_url = live_url;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getList_datetime() {
        return list_datetime;
    }

    public void setList_datetime(String list_datetime) {
        this.list_datetime = list_datetime;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getIn_progress() {
        return in_progress;
    }

    public void setIn_progress(String in_progress) {
        this.in_progress = in_progress;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEpisodic() {
        return episodic;
    }

    public void setEpisodic(String episodic) {
        this.episodic = episodic;
    }

    public String getEpisode_title() {
        return episode_title;
    }

    public void setEpisode_title(String episode_title) {
        this.episode_title = episode_title;
    }

    public String getEpisode_part_num() {
        return episode_part_num;
    }

    public void setEpisode_part_num(String episode_part_num) {
        this.episode_part_num = episode_part_num;
    }

    public String getEpisode_parts() {
        return episode_parts;
    }

    public void setEpisode_parts(String episode_parts) {
        this.episode_parts = episode_parts;
    }

    public String getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(String episode_number) {
        this.episode_number = episode_number;
    }

    public String getEducational() {
        return educational;
    }

    public void setEducational(String educational) {
        this.educational = educational;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescriptive_video() {
        return descriptive_video;
    }

    public void setDescriptive_video(String descriptive_video) {
        this.descriptive_video = descriptive_video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCaptioned() {
        return captioned;
    }

    public void setCaptioned(String captioned) {
        this.captioned = captioned;
    }

    public String getBreakout_level() {
        return breakout_level;
    }

    public void setBreakout_level(String breakout_level) {
        this.breakout_level = breakout_level;
    }

    public String getBlackWhite() {
        return blackWhite;
    }

    public void setBlackWhite(String blackWhite) {
        this.blackWhite = blackWhite;
    }
}

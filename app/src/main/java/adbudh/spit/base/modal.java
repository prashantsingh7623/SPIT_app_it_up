package adbudh.spit.base;

import java.io.Serializable;

public class modal {
    private String event_name, event_date, posterUri, event_desc,
            event_committee, event_duration, event_time, event_venue, event_link, event_contact_name, event_contact_number;

    public modal() {
    }

    public modal(String event_name, String event_date, String posterUri, String event_desc, String event_committee, String event_duration, String event_paid, String event_time, String event_venue, String event_link, String event_contact_name, String event_contact_number) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.posterUri = posterUri;
        this.event_desc = event_desc;
        this.event_committee = event_committee;
        this.event_duration = event_duration;
        this.event_time = event_time;
        this.event_venue = event_venue;
        this.event_link = event_link;
        this.event_contact_name = event_contact_name;
        this.event_contact_number = event_contact_number;
    }

    public String getEvent_link() {
        return event_link;
    }

    public void setEvent_link(String event_link) {
        this.event_link = event_link;
    }

    public String getEvent_contact_name() {
        return event_contact_name;
    }

    public void setEvent_contact_name(String event_contact_name) {
        this.event_contact_name = event_contact_name;
    }

    public String getEvent_contact_number() {
        return event_contact_number;
    }

    public void setEvent_contact_number(String event_contact_number) {
        this.event_contact_number = event_contact_number;
    }


    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_committee() {
        return event_committee;
    }

    public void setEvent_committee(String event_committee) {
        this.event_committee = event_committee;
    }

    public String getEvent_duration() {
        return event_duration;
    }

    public void setEvent_duration(String event_duration) {
        this.event_duration = event_duration;
    }


    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_venue() {
        return event_venue;
    }

    public void setEvent_venue(String event_venue) {
        this.event_venue = event_venue;
    }
}

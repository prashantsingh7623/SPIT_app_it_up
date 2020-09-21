package adbudh.spit.admin;

import java.io.Serializable;

public class VolunteerData implements Serializable {
    public String volunteer_name;
    public  String volunteer_contact;
    public String volunteer_job;

    public VolunteerData(String volunteer_name, String volunteer_contact, String volunteer_job) {
        this.volunteer_name = volunteer_name;
        this.volunteer_contact = volunteer_contact;
        this.volunteer_job = volunteer_job;
    }

    public VolunteerData() {
    }

    public String getVolunteer_name() {
        return volunteer_name;
    }

    public void setVolunteer_name(String volunteer_name) {
        this.volunteer_name = volunteer_name;
    }

    public String getVolunteer_contact() {
        return volunteer_contact;
    }

    public void setVolunteer_contact(String volunteer_contact) {
        this.volunteer_contact = volunteer_contact;
    }

    public String getVolunteer_job() {
        return volunteer_job;
    }

    public void setVolunteer_job(String volunteer_job) {
        this.volunteer_job = volunteer_job;
    }
}

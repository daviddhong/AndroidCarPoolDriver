package android.carpooldriver;

public class RiderRequestTicket {

    public String t_from, t_to;


    public RiderRequestTicket() {
    }
    public RiderRequestTicket(String tickfrom, String ticketto) {
        this.t_from = tickfrom;
        this.t_to = ticketto;
    }


    public String getticketfrom() {
        return t_from;
    }
    public void setticketfrom(String ticketto) {
        this.t_from = ticketto;
    }



    public String getticketto() {
        return t_to;
    }
    public void setticketto(String ticketto) {
        this.t_to = ticketto;
    }


}

package android.carpooldriver;

public class RiderRequestTicket {

    public String From, To;


    public RiderRequestTicket() {
    }
    public RiderRequestTicket(String tickfrom, String ticketto) {
        this.From = tickfrom;
        this.To = ticketto;
    }


    public String getticketfrom() {
        return From;
    }
    public void setticketfrom(String ticketto) {
        this.From = ticketto;
    }



    public String getticketto() {
        return To;
    }
    public void setticketto(String ticketto) {
        this.To = ticketto;
    }


}

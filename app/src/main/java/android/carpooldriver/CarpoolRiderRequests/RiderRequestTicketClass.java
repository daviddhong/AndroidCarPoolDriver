package android.carpooldriver.CarpoolRiderRequests;

public class RiderRequestTicketClass {

    public String From, To;


    public RiderRequestTicketClass() {
    }
    public RiderRequestTicketClass(String tickfrom, String ticketto) {
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

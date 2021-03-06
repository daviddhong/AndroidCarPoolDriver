package android.carpooldriver.Fragments.AllAvailableCarpoolTicketsFragment.CarpoolRiderRequestsContent;

public class RiderRequestTicketClass {

    public String From, To, NumberOfSeats, Price, Date, Time;


    public RiderRequestTicketClass() {
    }
    public RiderRequestTicketClass(String tickfrom, String tickto,
                                   String ticknum, String tickprice,
                                   //        String tickuid
                                   String tickdate, String ticktime) {
        this.From = tickfrom;
        this.To = tickto;
        this.NumberOfSeats = ticknum;
        this.Price = tickprice;
        this.Date = tickdate;
        this.Time = ticktime;
        //        this.uid = tickuid;
    }


    public String getticketfrom() {
        return From;
    }

    public String getticketto() {
        return To;
    }

    public String getticketnumberofseats() {
        return NumberOfSeats;
    }

    public String getticketprice() {
        return Price;
    }

    public String getticketdate() {
        return Date;
    }

    public String gettickettime() {
        return Time;
    }

//    public String getticketuid() {
//        return uid;
//    }

//    public void setticketuid(String ticketuid) {
//        this.uid = ticketuid;
//    }


    public void setticketfrom(String ticketfrom) {
        this.From = ticketfrom;
    }

    public void setticketto(String ticketto) {
        this.To = ticketto;
    }

    public void setticketnumberofseats(String ticketnum) {
        this.NumberOfSeats = ticketnum;
    }

    public void setticketprice(String ticketprice) {
        this.Price = ticketprice;
    }

    public void setticketdate(String ticketdate) {
        this.Date = ticketdate;
    }

    public void settickettime(String tickettime) {
        this.Time = tickettime;
    }




}

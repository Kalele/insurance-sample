package hepstar.sample.application;

public class TravelInformation {

    private String startdate;
    private String enddate;
    private String departurecountry;
    private String covercountry;

    public TravelInformation() {}
    public TravelInformation(String startdate, String enddate, String departurecountry, String covercountry) {
        this.startdate = startdate;
        this.enddate = enddate;
        this.departurecountry = departurecountry;
        this.covercountry = covercountry;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDeparturecountry() {
        return departurecountry;
    }

    public void setDeparturecountry(String departurecountry) {
        this.departurecountry = departurecountry;
    }

    public String getCovercountry() {
        return covercountry;
    }

    public void setCovercountry(String covercountry) {
        this.covercountry = covercountry;
    }
}
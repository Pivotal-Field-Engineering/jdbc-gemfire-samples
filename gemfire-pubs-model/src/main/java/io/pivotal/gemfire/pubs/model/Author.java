package io.pivotal.gemfire.pubs.model;

public class Author {

    private String auId;
    private String auLname;
    private String auFname;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private Integer processed;

    @Override
    public String toString() {
        return "Author{" +
                "auId='" + auId + '\'' +
                ", auLname='" + auLname + '\'' +
                ", auFname='" + auFname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", processed=" + processed +
                '}';
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    public String getAuLname() {
        return auLname;
    }

    public void setAuLname(String auLname) {
        this.auLname = auLname;
    }

    public String getAuFname() {
        return auFname;
    }

    public void setAuFname(String auFname) {
        this.auFname = auFname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }
}

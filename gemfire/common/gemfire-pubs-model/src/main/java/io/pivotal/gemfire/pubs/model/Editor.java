package io.pivotal.gemfire.pubs.model;

public class Editor {

    private String edId;
    private String edLname;
    private String edFname;
    private String edPos;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;

    @Override
    public String toString() {
        return "Editor{" +
                "edId='" + edId + '\'' +
                ", edLname='" + edLname + '\'' +
                ", edFname='" + edFname + '\'' +
                ", edPos='" + edPos + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", edBoss='" + edBoss + '\'' +
                '}';
    }

    public String getEdId() {
        return edId;
    }

    public void setEdId(String edId) {
        this.edId = edId;
    }

    public String getEdLname() {
        return edLname;
    }

    public void setEdLname(String edLname) {
        this.edLname = edLname;
    }

    public String getEdFname() {
        return edFname;
    }

    public void setEdFname(String edFname) {
        this.edFname = edFname;
    }

    public String getEdPos() {
        return edPos;
    }

    public void setEdPos(String edPos) {
        this.edPos = edPos;
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

    public String getEdBoss() {
        return edBoss;
    }

    public void setEdBoss(String edBoss) {
        this.edBoss = edBoss;
    }

    private String edBoss;

}

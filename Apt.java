// Jarod Miller

public class Apt {

    private String streetAddy;
    private String aptNumber;
    private String city;
    private String zipCode;
    private int rent;
    private int sqFootage;

    Apt(String streetAddy, String aptNumber, String city, String zipCode, int rent, int sqFootage) {
        this.streetAddy = streetAddy;
        this.aptNumber = aptNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.rent = rent;
        this.sqFootage = sqFootage;
    }



    public String getStreetAddy() { return streetAddy; }
    public void setStreetAddy(String newStreetAddy) {
        this.streetAddy = newStreetAddy;
    }
    public String getAptNumber() { return aptNumber; }
    public void setAptNumber(String newAptNumber) {
        this.aptNumber = newAptNumber;
    }
    public String getCity() { return city; }
    public void setCity(String newCity) {
        this.city = newCity;
    }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String newZipCode) {
        this.zipCode = newZipCode;
    }
    public int getRent() { return rent; }
    public void setRent(int newRent) {
        this.rent = newRent;
    }
    public int getSqFootage() { return sqFootage; }
    public void setSqFootage(int newSqFootage) {
        this.sqFootage = newSqFootage;
    }
}
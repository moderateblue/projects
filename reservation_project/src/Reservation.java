import javafx.beans.property.SimpleStringProperty;

public class Reservation {
    private SimpleStringProperty rmNum;
    private SimpleStringProperty name;
    private SimpleStringProperty rmType;
    private SimpleStringProperty guests;
    private SimpleStringProperty dateIn;
    private SimpleStringProperty dateOut;
    private SimpleStringProperty phone;
    private SimpleStringProperty prepaid;

    public Reservation(String rmNum, String name, String rmType, String guests, String dateIn, String dateOut, String phone, String prepaid) {
        this.rmNum = new SimpleStringProperty(rmNum);
        this.name = new SimpleStringProperty(name);
        this.rmType = new SimpleStringProperty(rmType);
        this.guests = new SimpleStringProperty(guests);
        this.dateIn = new SimpleStringProperty(dateIn);
        this.dateOut = new SimpleStringProperty(dateOut);
        this.phone = new SimpleStringProperty(phone);
        this.prepaid = new SimpleStringProperty(prepaid);
    }

    public String getRmNum() {
        return rmNum.get();
    }

    public String getName() {
        return name.get();
    }

    public String getRmType() {
        return rmType.get();
    }

    public String getGuests() {
        return guests.get();
    }

    public String getDateIn() {
        return dateIn.get();
    }

    public String getDateOut() {
        return dateOut.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getPrepaid() {
        return prepaid.get();
    }
}

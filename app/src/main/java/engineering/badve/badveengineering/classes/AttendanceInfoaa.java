package engineering.badve.badveengineering.classes;

/**
 * Created by Amol on 14-February-18.
 */

public class AttendanceInfoaa
{
    private String id;
    private String shift_name;
    private String out_time;
    private String in_time;
    private String barcode_number;
    private String rfid_number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getBarcode_number() {
        return barcode_number;
    }

    public void setBarcode_number(String barcode_number) {
        this.barcode_number = barcode_number;
    }

    public String getRfid_number() {
        return rfid_number;
    }

    public void setRfid_number(String rfid_number) {
        this.rfid_number = rfid_number;
    }
}

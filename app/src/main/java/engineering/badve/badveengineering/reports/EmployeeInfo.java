package engineering.badve.badveengineering.reports;

/**
 * Created by Amol on 21-February-18.
 */

public class EmployeeInfo
{
    private String id;
    private String rfidNumber;
    private String bnumber;
    private String designation;
    private String contractor_code;
    private String cellid;
    private String date;
    private String sstatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getBnumber() {
        return bnumber;
    }

    public void setBnumber(String bnumber) {
        this.bnumber = bnumber;
    }

    public String getContractor_code() {
        return contractor_code;
    }

    public void setContractor_code(String contractor_code) {
        this.contractor_code = contractor_code;
    }

    public String getCellid() {
        return cellid;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}

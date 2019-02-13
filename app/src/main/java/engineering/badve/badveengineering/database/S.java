package engineering.badve.badveengineering.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.ArrayList;

import engineering.badve.badveengineering.classes.AttendanceInfoaa;
import engineering.badve.badveengineering.classes.FetchData;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.BookingDetailsInformation;
import engineering.badve.badveengineering.pojo.CellIdInformation;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.reports.AttendadanceInfo;
import engineering.badve.badveengineering.reports.EmployeeInfo;
import engineering.badve.badveengineering.reports.ShiftInfo;

/**
 * Created by snsystem_amol on 3/12/2017.
 */

public class S
{
    public static ArrayList<EmployeeInfo> getEmployeeDetails()
    {


        ArrayList<EmployeeInfo> BookingDe_DATA = new ArrayList<>();


        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectEmployeeTable();


            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String ID = cursor.getString(cursor.getColumnIndex(MyApplication.db.ID));
                    String RFID_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.RFID_NUMBER));
                    String BARCODE_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.BARCODE_NUMBER));
                    String DESIGNATION = cursor.getString(cursor.getColumnIndex(MyApplication.db.DESIGNATION));
                    String CONTRACTOR_CODE = cursor.getString(cursor.getColumnIndex(MyApplication.db.CONTRACTOR_CODE));
                    String CELL_IDDD = cursor.getString(cursor.getColumnIndex(MyApplication.db.CELL_IDDD));
                    String DATE_COLUMN = cursor.getString(cursor.getColumnIndex(MyApplication.db.DATE_COLUMN));
                    String SYNCH_STATUS = cursor.getString(cursor.getColumnIndex(MyApplication.db.SYNCH_STATUS));


                    EmployeeInfo bookingDetai = new EmployeeInfo();

                    bookingDetai.setId(ID);
                    bookingDetai.setRfidNumber(RFID_NUMBER);
                    bookingDetai.setBnumber(BARCODE_NUMBER);
                    bookingDetai.setDesignation(DESIGNATION);
                    bookingDetai.setContractor_code(CONTRACTOR_CODE);
                    bookingDetai.setCellid(CELL_IDDD);
                    bookingDetai.setDate(DATE_COLUMN);
                    bookingDetai.setSstatus(SYNCH_STATUS);

                    BookingDe_DATA.add(bookingDetai);


                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return BookingDe_DATA;




    }
    public static ArrayList<AttendadanceInfo> getAttendanceDetails()
    {


        ArrayList<AttendadanceInfo> BookingDe_DATA = new ArrayList<>();


        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectAttendanceTable();


            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String ID = cursor.getString(cursor.getColumnIndex(MyApplication.db.ID));
                    String RFID_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.RFID_NUMBER));
                    String BARCODE_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.BARCODE_NUMBER));
                    String IN_TIME = cursor.getString(cursor.getColumnIndex(MyApplication.db.IN_TIME));
                    String OUT_TIME = cursor.getString(cursor.getColumnIndex(MyApplication.db.OUT_TIME));
                    String SHIFT_NAME = cursor.getString(cursor.getColumnIndex(MyApplication.db.SHIFT_NAME));
                    String SYNCH_STATUS = cursor.getString(cursor.getColumnIndex(MyApplication.db.SYNCH_STATUS));
                    String TIME_COLUMN = cursor.getString(cursor.getColumnIndex(MyApplication.db.TIME_COLUMN));


                    AttendadanceInfo bookingDetai = new AttendadanceInfo();

                    bookingDetai.setId(ID);
                    bookingDetai.setRfidNumber(RFID_NUMBER);
                    bookingDetai.setBnumber(BARCODE_NUMBER);
                    bookingDetai.setIntime(IN_TIME);
                    bookingDetai.setOuttime(OUT_TIME);
                    bookingDetai.setName(SHIFT_NAME);
                    bookingDetai.setSstatus(SYNCH_STATUS);
                    bookingDetai.setTime(TIME_COLUMN);

                    BookingDe_DATA.add(bookingDetai);


                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return BookingDe_DATA;




    }
    public static ArrayList<ShiftInfo> getShiftDetails()
    {


        ArrayList<ShiftInfo> BookingDe_DATA = new ArrayList<>();


        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectShiftTable();


            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String ID = cursor.getString(cursor.getColumnIndex(MyApplication.db.ID));
                    String SHIFT_NAME = cursor.getString(cursor.getColumnIndex(MyApplication.db.SHIFT_NAME));
                    String FROM_TIME = cursor.getString(cursor.getColumnIndex(MyApplication.db.FROM_TIME));
                    String TO_TIME = cursor.getString(cursor.getColumnIndex(MyApplication.db.TO_TIME));
                    String DATE_COLUMN = cursor.getString(cursor.getColumnIndex(MyApplication.db.DATE_COLUMN));
                    String SYNCH_STATUS = cursor.getString(cursor.getColumnIndex(MyApplication.db.SYNCH_STATUS));

                    ShiftInfo bookingDetai = new ShiftInfo();

                    bookingDetai.setId(ID);
                    bookingDetai.setName(SHIFT_NAME);
                    bookingDetai.setFromTime(FROM_TIME);
                    bookingDetai.setToTime(TO_TIME);
                    bookingDetai.setDate(DATE_COLUMN);
                    bookingDetai.setSstatus(SYNCH_STATUS);

                    BookingDe_DATA.add(bookingDetai);


                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return BookingDe_DATA;




    }
    public static ArrayList<BookingDetailsInformation> getBookingDetails()
    {


        ArrayList<BookingDetailsInformation> BookingDe_DATA = new ArrayList<>();


        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectBookingTable();


            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String ID = cursor.getString(cursor.getColumnIndex(MyApplication.db.ID));
                    String DESIGNATION = cursor.getString(cursor.getColumnIndex(MyApplication.db.DESIGNATION));
                    String TARGET = cursor.getString(cursor.getColumnIndex(MyApplication.db.TARGET));
                    String INCOUNT = cursor.getString(cursor.getColumnIndex(MyApplication.db.INCOUNT));
                    String OUTCOUNT = cursor.getString(cursor.getColumnIndex(MyApplication.db.OUTCOUNT));
                    String SHIFT_NAME = cursor.getString(cursor.getColumnIndex(MyApplication.db.SHIFT_NAME));
                    String CONTRACTOR_CODE = cursor.getString(cursor.getColumnIndex(MyApplication.db.CONTRACTOR_CODE));
                    String CELL_IDDD = cursor.getString(cursor.getColumnIndex(MyApplication.db.CELL_IDDD));
                    String IDD = cursor.getString(cursor.getColumnIndex(MyApplication.db.IDD));
                    String DATE_COLUMN = cursor.getString(cursor.getColumnIndex(MyApplication.db.DATE_COLUMN));
                    String SYNCH_STATUS = cursor.getString(cursor.getColumnIndex(MyApplication.db.SYNCH_STATUS));
                    String TOLLERANCE = cursor.getString(cursor.getColumnIndex(MyApplication.db.TOLLERANCE));
                    String UPDATED_TARGET = cursor.getString(cursor.getColumnIndex(MyApplication.db.UPDATED_TARGET));

                    BookingDetailsInformation bookingDetai = new BookingDetailsInformation();

                    bookingDetai.setIDTextView(ID);
                    bookingDetai.setDESIGNATIONTextView(DESIGNATION);
                    bookingDetai.setTARGETTextView(TARGET);
                    bookingDetai.setINCOUNTextView(INCOUNT);
                    bookingDetai.setOUTCOUNTTextView(OUTCOUNT);
                    bookingDetai.setSHIFT_NAMETextView(SHIFT_NAME);
                    bookingDetai.setCONTRACTOR_CODETextView(CONTRACTOR_CODE);
                    bookingDetai.setCELL_IDDDTextView(CELL_IDDD);
                    bookingDetai.setIDDTextView(IDD);
                    bookingDetai.setDATE_COLUMNTextView(DATE_COLUMN);
                    bookingDetai.setSYNCH_STATUSTextView(SYNCH_STATUS);
                    bookingDetai.setTOLLERANCETextView(TOLLERANCE);
                    bookingDetai.setUPDATED_TARGETTextView(UPDATED_TARGET);

                    BookingDe_DATA.add(bookingDetai);


                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return BookingDe_DATA;




    }
    public static boolean checkCellAlready(String cellId,String message,Context context)
    {
        boolean status = false;

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.checkCellAlready(cellId);

            if(cursor.getCount()>0)
            {
                T.tE(context,message);
                status = false;
            }
            else
            {
                status = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }

    public static ArrayList<CellInfo> getAllCellIDForJsonArray()
    {


        ArrayList<CellInfo> CELL_ID_DATA = new ArrayList<>();

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectAllCellId();

            if(cursor.getCount() > 0)
            {
                while(cursor.moveToNext())
                {
                    String cell_id = cursor.getString(cursor.getColumnIndex(MyApplication.db.CELL_IDDD));
                    String cell_Name = cursor.getString(cursor.getColumnIndex(MyApplication.db.CELL_NAME));

                    CellInfo cellInfo = new CellInfo();

                    cellInfo.setCell_id(cell_id);
                    cellInfo.setCell_name(cell_Name);

                    CELL_ID_DATA.add(cellInfo);

                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return CELL_ID_DATA;




    }
    public static String getCellCount()
    {


        String CELL_COUNT = null;

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectCellCount();

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    CELL_COUNT = cursor.getString(cursor.getColumnIndex("cCount"));

                }
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return CELL_COUNT;


    }

    public static ArrayList<CellIdInformation> getAllCellID(Context context)
    {

        ArrayList<CellIdInformation> CELL_ID_DATA = new ArrayList<>();


        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.selectAllCellId();

            //get device id
            final SharedPreferences[] preferences = {context.getSharedPreferences(Constants.NAVIGATION, 0)};
            SharedPreferences.Editor editor = preferences[0].edit();
            editor.commit();

            String cellId = preferences[0].getString("cell_id","0");

            if(!cellId.equals("0"))
            {
                CellIdInformation cellIdInformation = new CellIdInformation();

                cellIdInformation.setCellId(cellId);

                CELL_ID_DATA.add(cellIdInformation);

            }

            if(cursor.getCount()>0)
            {

                while(cursor.moveToNext())
                {
                    String DESIGNATION = cursor.getString(cursor.getColumnIndex(MyApplication.db.CELL_ID));

                    CellIdInformation cellIdInformation = new CellIdInformation();

                    cellIdInformation.setCellId(DESIGNATION);

                    CELL_ID_DATA.add(cellIdInformation);

                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return CELL_ID_DATA;




    }

    public static String checkAvailShift(DBHelper db)
    {
        String status = "2";

        try
        {

            Cursor cursor = null;

            cursor = db.getfromTimeTotimeShiftName1();

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }
    public static String getShiftTimeLast(DBHelper db)
    {


        String FROM_TIME = null;

        try
        {

            Cursor cursor = null;

            cursor = db.getShiftTimeLast();

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    FROM_TIME = cursor.getString(cursor.getColumnIndex(db.TO_TIME));

                }
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return FROM_TIME;


    }
    //function fo get shift details with from time and to time
    public static ArrayList<String> getfromTimeTotimeShiftName(DBHelper db)
    {
        ArrayList<String> fromTimeTotimeShiftName = new ArrayList<>();
        try
        {
            Cursor cursor = null;
            cursor = db.getfromTimeTotimeShiftName();
            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String SHIFT_NAME = cursor.getString(cursor.getColumnIndex(db.SHIFT_NAME));
                    String FROM_TIME = cursor.getString(cursor.getColumnIndex(db.FROM_TIME));
                    String TO_TIME = cursor.getString(cursor.getColumnIndex(db.TO_TIME));

                    fromTimeTotimeShiftName.add(SHIFT_NAME+"#"+FROM_TIME+"#"+TO_TIME);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return fromTimeTotimeShiftName;
    }
    public static String getShiftTimeUsingName(DBHelper db, String shiftName)
    {


        String FROM_TIME = null;

        try
        {

            Cursor cursor = null;

            cursor = db.getShiftTimeUsingName(shiftName);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    FROM_TIME = cursor.getString(cursor.getColumnIndex(db.FROM_TIME));

                }
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return FROM_TIME;


    }

    public static String checkInOutEntry(DBHelper db,
                                          String rfid,
                                          String systemDate)
    {
        String status = "2";

        try
        {

            Cursor cursor = null;

            cursor = db.checkInOutEntsry(rfid,"0",systemDate);

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }


        }
        catch (Exception e)
        {
            status = "2";
            e.printStackTrace();
        }

        return status;
    }

    public static String getInEntryTime(DBHelper db, String rfid_number,String systemDate)
    {


        String ENTRY_TIME = null;

        try
        {

            Cursor cursor = null;

            cursor = db.getInEntryTime(rfid_number,systemDate);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String [] datee = cursor.getString(cursor.getColumnIndex(db.IN_TIME)).split(" ");
                    ENTRY_TIME = datee[1];

                }
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ENTRY_TIME;


    }
    public static String getOutEntryTime(DBHelper db, String rfid_number,String systemDate)
    {


        String ENTRY_TIME = "NA";

        try
        {

            Cursor cursor = null;

            cursor = db.getOutEntryTime(rfid_number,systemDate);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String [] datee = cursor.getString(cursor.getColumnIndex(db.OUT_TIME)).split(" ");
                    ENTRY_TIME = datee[1];

                }
            }
            else
            {
                ENTRY_TIME = "NA";
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ENTRY_TIME;


    }

    public static String getAllBookingDetailsNotShiftWise(DBHelper db,String systemDate)
    {

        ArrayList<String> BOOKING_DATA = new ArrayList<>();

        String returnString = null;
        try
        {

            Cursor cursor = null;

            //cursor = db.getAllBookingDetails(shiftNAme);
            cursor = db.getAllBookingDetailsNotShiftWise(systemDate);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String DESIGNATION = cursor.getString(cursor.getColumnIndex(db.DESIGNATION));
                    String TARGET = cursor.getString(cursor.getColumnIndex(db.TARGET));
                    String INCOUNT = cursor.getString(cursor.getColumnIndex(db.INCOUNT));
                    String OUTCOUNT = cursor.getString(cursor.getColumnIndex(db.OUTCOUNT));
                    String SHIFT_NAME = cursor.getString(cursor.getColumnIndex(db.SHIFT_NAME));
                    String DATE_COLUMN = cursor.getString(cursor.getColumnIndex(db.DATE_COLUMN));

                    String data = DESIGNATION+"#"+TARGET+"#"+INCOUNT+"#"+OUTCOUNT+"#"+SHIFT_NAME+"#"+DATE_COLUMN;

                    BOOKING_DATA.add(data);

                }
            }


            returnString = composeData(BOOKING_DATA);






        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnString;




    }


    private static String composeData(ArrayList<String> booking_data)
    {
        
        StringBuffer stringBuffer = new StringBuffer();
        
        for(int i = 0; i < booking_data.size(); i++)
        {
            stringBuffer.append(booking_data.get(i)+"@");
        }
        
        return stringBuffer.toString();
    }



    public static String checkDesignationAvailForBooking(DBHelper db,
                                                          String empDesignationType,
                                                          String contractor_code,
                                                          String cellId,
                                                          String systemDate)
    {
        String status = "2";

        try
        {

            Cursor cursor = null;

            cursor = db.checkDesignationAvailForBooking(empDesignationType,contractor_code,cellId,systemDate);

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }

    public static String getInCount(String empDesignationType,
                                    String shiftName,
                                    String cellId,
                                    String systemDate)
    {
        String inCount = "NA";

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getInCount(empDesignationType,shiftName,cellId,systemDate);

            if(cursor.getCount() > 0)
            {
                if(cursor.moveToNext())
                {
                    inCount = cursor.getString(cursor.getColumnIndex(MyApplication.db.INCOUNT));



                }
            }
            else
            {
                inCount = "NA";
            }



            cursor.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return inCount;
    }
    public static String getInCountTollerence(String empDesignationType,
                                              String CONTRACTOR_CODEv,
                                              String shiftName,
                                              String cellId,
                                              String systemDate)
    {
        String inCount = null;

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getInCountTollerence(
                    empDesignationType,
                    CONTRACTOR_CODEv,
                    shiftName,
                    cellId,
                    systemDate);

            if(cursor.moveToNext())
            {
                inCount = cursor.getString(cursor.getColumnIndex(MyApplication.db.INCOUNT));



            }

            cursor.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return inCount;
    }
    public static String getTargetCount(String empDesignationType,
                                        String shiftName,
                                        String cellId,String systemDate)
    {
        String TARGET = "NA";

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getTargetCount(empDesignationType,shiftName,cellId,systemDate);

            if(cursor.getCount() > 0)
            {
                if(cursor.moveToNext())
                {
                    TARGET = cursor.getString(cursor.getColumnIndex(MyApplication.db.TARGET));
                }
            }
            else
            {
                TARGET = "NA";
            }



            cursor.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return TARGET;
    }

    public static String getTargetCountTollerence(String empDesignationType,
                                                  String CONTRACTOR_CODE,
                                                  String shiftName,
                                                  String cellId,
                                                  String systemDate)
    {
        String TARGET = null;

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getTargetCountTollerence(
                    empDesignationType,
                    CONTRACTOR_CODE,
                    shiftName,
                    cellId,
                    systemDate);

            if(cursor.getCount() > 0)
            {
                if(cursor.moveToNext())
                {
                    TARGET = cursor.getString(cursor.getColumnIndex(MyApplication.db.UPDATED_TARGET));
                }
            }
            else
            {
                TARGET = "NA";
            }



            cursor.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return TARGET;
    }
    public static ArrayList<String> selectRfidNumbers(String synchStatus, String systemDate)
    {


        ArrayList<String> ATTENDANCE_INFO = new ArrayList<>();

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getAttendanceInfoRFID(synchStatus,systemDate);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String RFID_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.RFID_NUMBER));


                    ATTENDANCE_INFO.add(RFID_NUMBER);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ATTENDANCE_INFO;
    }


    public static ArrayList<AttendanceInfoaa> getAttendanceDetailsOffline(String rfidNumber,
                                                                          String synchStatus,
                                                                          String systemDate)
    {


        ArrayList<AttendanceInfoaa> ATTENDANCE_INFO = new ArrayList<>();

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getAttendanceInfo(rfidNumber,synchStatus,systemDate);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String ID = cursor.getString(cursor.getColumnIndex(MyApplication.db.ID));
                    String SHIFT_NAME = cursor.getString(cursor.getColumnIndex(MyApplication.db.SHIFT_NAME));
                    String OUT_TIME = cursor.getString(cursor.getColumnIndex(MyApplication.db.OUT_TIME));
                    String IN_TIME = cursor.getString(cursor.getColumnIndex(MyApplication.db.IN_TIME));
                    String BARCODE_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.BARCODE_NUMBER));
                    String RFID_NUMBER = cursor.getString(cursor.getColumnIndex(MyApplication.db.RFID_NUMBER));


                    AttendanceInfoaa attendanceInfo = new AttendanceInfoaa();

                    attendanceInfo.setId(ID);
                    attendanceInfo.setShift_name(SHIFT_NAME);
                    attendanceInfo.setOut_time(OUT_TIME);
                    attendanceInfo.setIn_time(IN_TIME);
                    attendanceInfo.setBarcode_number(BARCODE_NUMBER);
                    attendanceInfo.setRfid_number(RFID_NUMBER);


                    ATTENDANCE_INFO.add(attendanceInfo);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ATTENDANCE_INFO;
    }
    public static String returnBarcodeType(DBHelper db, String rfidNumber,String cellId,String systemDate)
    {


        String designation_and_contractortype = "NA";

        try
        {

            Cursor cursor = null;
            cursor = db.returnBarcodeType(rfidNumber,cellId,systemDate);
            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                   String DESIGNATION = cursor.getString(cursor.getColumnIndex(db.DESIGNATION));
                   String CONTRACTOR_CODE = cursor.getString(cursor.getColumnIndex(db.CONTRACTOR_CODE));

                   designation_and_contractortype = DESIGNATION+"#"+CONTRACTOR_CODE;
                }
            }
            else
            {
                designation_and_contractortype = "NA";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return designation_and_contractortype;

    }

    public static String getAllTargetTotal(DBHelper db,String systemDate)
    {


        String global_details = null;

        try
        {

            Cursor cursor = null;

            cursor = db.getTargetSum(systemDate);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String totalTargets = cursor.getString(cursor.getColumnIndex("targets"));
                    String incount = cursor.getString(cursor.getColumnIndex("incounttt"));
                    String outcount = cursor.getString(cursor.getColumnIndex("outcounttt"));

                    global_details = totalTargets +"#"+incount+"#"+outcount;

                }
            }
            else
            {
                global_details  = "NA";
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return global_details;


    }
    public static String checkBarcodeAvailable(DBHelper db,String rfid_number,String cellId,String systemDate)
    {
        String status = "2";

        try
        {
            Cursor cursor = null;
            cursor = db.checkBarcodeAvail(rfid_number,cellId,systemDate);

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }

    public static String checkBarcodeAvailableIAttendance(String rfid_number,String systemDate)
    {
        String status = "2";

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.checkBarcodeAvailableIAttendance(rfid_number,systemDate);

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        return status;
    }
    public static String checkDataAvailable()
    {
        String status = "2";

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.checkDataAvailable(T.getSystemDate());

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        return status;
    }


    public static String getBarcodeNumber(String rfid_number,String systemDate)
    {
        String bNumberCellId = "NA";
        //String cellID = null;

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.returnBarcodeNumber(rfid_number,systemDate);

            if(cursor.getCount()>0)
            {
                if(cursor.moveToNext())
                {
                    //String employee_id = cursor.getString(cursor.getColumnIndex(MyApplication.db.ID));
                    String bNumber = cursor.getString(cursor.getColumnIndex(MyApplication.db.BARCODE_NUMBER));
                    String cellID = cursor.getString(cursor.getColumnIndex(MyApplication.db.CELL_IDDD));

                    bNumberCellId = bNumber+"#"+cellID;


                }
            }
            else
            {
                bNumberCellId = "NA";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return bNumberCellId;
    }
    public static String getShiftName(String rfid_number,String systemDate)
    {
        String SHIFT_NAME = "NA";

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.getShiftName(rfid_number,systemDate);

            if(cursor.getCount()>0)
            {
                if(cursor.moveToNext())
                {
                    SHIFT_NAME = cursor.getString(cursor.getColumnIndex(MyApplication.db.SHIFT_NAME));
                }
            }
            else
            {
                SHIFT_NAME = "NA";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return SHIFT_NAME;
    }
    public static String getInCountHere(String cellId,
                                        String shift,
                                        String designation,
                                        String contractorCode,
                                        String date)
    {
        String INCOUNT = "NA";

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.getInCountHere(cellId,shift,designation,contractorCode,date);

            if(cursor.getCount()>0)
            {

                INCOUNT = String.valueOf(cursor.getCount());
            }
            else
            {
                INCOUNT = "NA";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return INCOUNT;
    }

    public static String cehckInoutEntry(DBHelper db,String rfidNumber,String systemDate)
    {
        String status = "2";

        try
        {

            Cursor cursor = null;
            cursor = db.checkInOutEntry(rfidNumber,systemDate);
            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));
                Integer ddd = Integer.valueOf(counterCheck);
                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }
    public static String outAttendanceAlready(String rfidNumber,String systemDate)
    {
        String status = "2";
        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.outAttendanceAlready(rfidNumber,systemDate,"0");
            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));
                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }
    public static String checkAlreadyBooking(String desgnation,
                                              String shiftName,
                                              String contractorCode,
                                              String cellId,
                                              String systemDate,
                                              String serverId)
    {
        String status = "2";

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.getAlreadyBooking(desgnation,shiftName,contractorCode,cellId,systemDate,serverId);



            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }

        }
        catch (Exception e)
        {

            status = "2";
            e.printStackTrace();
        }

        return status;
    }

    public static String checkEmployeeAlreadyEntryf(String RFID_no,
                                                    String bnumber,
                                                    String systemDate)
    {
        String status = "2";

        try
        {
            Cursor cursor = null;
            cursor = MyApplication.db.checkAlreadyEmployeef(RFID_no,bnumber,systemDate);

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }
    public static String checkShiftAlreadyEntry(String shift_name,
                                                    String shift_from,
                                                    String shift_to,
                                                    String systemDate,
                                                    String syncStatus)
    {
        String status = "2";

        try
        {

            Cursor cursor = null;

            cursor = MyApplication.db.checkShiftAlreadyEntry(shift_name,shift_from,shift_to,systemDate,syncStatus);

            if(cursor.moveToNext())
            {
                String counterCheck = cursor.getString(cursor.getColumnIndex(MyApplication.db.RETURN_COUNT));

                Integer ddd = Integer.valueOf(counterCheck);

                if(ddd > 0)
                {
                    status = "1";
                }
                else
                {
                    status = "0";
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return status;
    }
}

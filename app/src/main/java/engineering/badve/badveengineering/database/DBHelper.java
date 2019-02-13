package engineering.badve.badveengineering.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import engineering.badve.badveengineering.classes.T;

/**
 * Created by Admin on 10/7/2016.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "BADAVEDB";
    private static final int DATABASE_VERSION = 1; //v2.2

    Context context;

    // Tables
    static final String TABLE_SHIFT_DETAILS = "shift";
    static final String TABLE_EMP_DETAILS = "employee";
    static final String TABLE_BOOKING_DETAILS = "booking";
    static final String TABLE_ATTENDANCE = "attendance";
    static final String TABLE_CELL = "cell";

    static final String TOLLERANCE = "tollerance";
    static final String RETURN_COUNT = "returnCountt";
    static final String UPDATED_TARGET = "updated_target";


    static final String TABLE_STRINGS = "stringss";


    public static final String DATE_COLUMN = "date";

    public static final String TIME_COLUMN = "time";

    public static final String STRING_NAME = "name";

    // Tables Column

    //shift
    public static final String ID = "id";
    public static final String SHIFT_NAME = "name";
    public static final String FROM_TIME = "fromtime";
    public static final String TO_TIME = "totime";

    //employee
    //private static final String ID = "id";
    public static final String RFID_NUMBER = "rfidNumber";
    public static final String BARCODE_NUMBER = "bnumber";
    public static final String DESIGNATION = "designation";

    // attendance
    //private static final String BARCODE_NUMBER = "bnumber";
    public static final String IN_TIME = "intime";
    public static final String OUT_TIME = "outtime";
    //private static final String SHIFT_NAME = "name";
    public static final String SYNCH_STATUS = "sstatus";
    public static final String CELL_NAME = "cellName";

    //booking
    //private static final String ID = "id";
    //private static final String DESIGNATION = "designation";
    public static final String TARGET = "intarget";
    public static final String INCOUNT = "incount";
    public static final String OUTCOUNT = "outcount";

    //cell
    public static final String CELL_ID = "outcount";

    public static final String CELL_IDDD = "cellId";

    public static final String CONTRACTOR_CODE = "contractor_code";


    public static final String ENTRY_TIME = "time";
    public static final String IDD = "idd";

    //shift
    private static final String CREATE_TABLE_STRINGS = "create table " + TABLE_STRINGS + "(" +
            ID + " Integer Primary Key AutoIncrement ," +
            STRING_NAME + " TEXT)";


    //shift
    private static final String CREATE_TABLE_SHIFT = "create table " + TABLE_SHIFT_DETAILS + "(" +
            ID + " Integer Primary Key AutoIncrement ," +
            SHIFT_NAME + " TEXT ," +
            FROM_TIME + " TEXT ," +
            TO_TIME + " TEXT ," +
            DATE_COLUMN + " TEXT ," +
            SYNCH_STATUS + " TEXT)";

    //employee
    private static final String CREATE_TABLE_EMPLOYEE = "create table " + TABLE_EMP_DETAILS + "(" +
            ID + " Integer Primary Key AutoIncrement ," +
            RFID_NUMBER + " TEXT ," +
            BARCODE_NUMBER + " TEXT ," +
            DESIGNATION + " TEXT ," +
            CONTRACTOR_CODE + " TEXT ," +
            CELL_IDDD + " TEXT ," +
            DATE_COLUMN + " TEXT ," +
            SYNCH_STATUS + " TEXT)";
    //booking
    private static final String CREATE_TABLE_BOOKING = "create table " + TABLE_BOOKING_DETAILS + "(" +
            ID + " Integer Primary Key AutoIncrement ," +
            DESIGNATION + " TEXT ," +
            TARGET + " TEXT ," +
            INCOUNT + " TEXT ," +
            OUTCOUNT + " TEXT ," +
            SHIFT_NAME + " TEXT ," +
            CONTRACTOR_CODE + " TEXT ," +
            CELL_IDDD + " TEXT ," +
            IDD + " TEXT ," +
            DATE_COLUMN + " TEXT ," +
            SYNCH_STATUS + " TEXT ," +
            TOLLERANCE + " TEXT ," +
            UPDATED_TARGET + " TEXT)";



    //attendance
        private static final String CREATE_TABLE_ATTENDANCE = "create table " + TABLE_ATTENDANCE + "(" +
            ID + " Integer Primary Key AutoIncrement ," +
            RFID_NUMBER + " TEXT ," +
            BARCODE_NUMBER + " TEXT ," +
            IN_TIME + " TEXT ," +
            OUT_TIME + " TEXT ," +
            SHIFT_NAME + " TEXT ," +
            SYNCH_STATUS + " TEXT ," +
            ENTRY_TIME + " TEXT)";//consiter this is DATE_COLUMN



    //cell
    private static final String CREATE_TABLE_CELL = "create table " + TABLE_CELL+ "(" +
            ID + " Integer Primary Key AutoIncrement ," +
            CELL_IDDD + " TEXT ," +
            CELL_NAME + " TEXT ," +
            DATE_COLUMN + " TEXT ," +
            SYNCH_STATUS + " TEXT)";



    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_SHIFT);
        db.execSQL(CREATE_TABLE_EMPLOYEE);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_BOOKING);
        db.execSQL(CREATE_TABLE_STRINGS);
        db.execSQL(CREATE_TABLE_CELL);

    }
    public void clearTable(String todayDate,String yesterdayDate)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("delete from "+TABLE_SHIFT_DETAILS+" WHERE "+DATE_COLUMN+" <> '"+todayDate+"' AND "+DATE_COLUMN+" <> '"+yesterdayDate+"'");
        dbb.execSQL("delete from "+TABLE_EMP_DETAILS+" WHERE "+DATE_COLUMN+" <> '"+todayDate+"' AND "+DATE_COLUMN+" <> '"+yesterdayDate+"'");
        dbb.execSQL("delete from "+TABLE_BOOKING_DETAILS+" WHERE "+DATE_COLUMN+" <> '"+todayDate+"' AND "+DATE_COLUMN+" <> '"+yesterdayDate+"'");
        dbb.execSQL("delete from "+TABLE_ATTENDANCE+" WHERE "+ENTRY_TIME+" <> '"+todayDate+"' AND "+ENTRY_TIME+" <> '"+yesterdayDate+"'");
        //comment because cell id not to delete
        //dbb.execSQL("delete from "+TABLE_CELL+" WHERE "+DATE_COLUMN+" <> '"+todayDate+"' AND "+DATE_COLUMN+" <> '"+yesterdayDate+"'");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

       /* if(newVersion == 4 && oldVersion == 3)
        {
            db.execSQL("ALTER TABLE "+TABLE_BOOKING_DETAILS+" ADD COLUMN "+TOLLERANCE+" TEXT");
            db.execSQL("ALTER TABLE "+TABLE_BOOKING_DETAILS+" ADD COLUMN "+UPDATED_TARGET+" TEXT");
        }
        */
    }

    public void insertCellIds(String cellId,String cellName)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CELL_IDDD, cellId);
        values.put(CELL_NAME, cellName);
        values.put(DATE_COLUMN, T.getSystemDate());
        values.put(SYNCH_STATUS, "0");

        db.insert(TABLE_CELL, null, values);

    }

    public Cursor checkCellAlready(String cellId)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_CELL+" WHERE "+CELL_IDDD+" ='"+cellId+"'",null);
        return cursor;
    }
    public void deleteCellId(String cellId)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("DELETE FROM "+TABLE_CELL+" WHERE "+CELL_IDDD+" ='"+cellId+"'");

    }
    public void deleteBookingData(String id)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("DELETE FROM "+TABLE_BOOKING_DETAILS+" WHERE "+ID+" ='"+id+"'");

    }
    public void deleteAttendanceData(String id)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("DELETE FROM "+TABLE_ATTENDANCE+" WHERE "+ID+" ='"+id+"'");

    }
    public void deleteShiftData(String id)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("DELETE FROM "+TABLE_SHIFT_DETAILS+" WHERE "+ID+" ='"+id+"'");

    }
    public void deleteBookingAndEmployeeCellWise(String cellId)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("DELETE FROM "+TABLE_EMP_DETAILS+" WHERE "+CELL_IDDD+" ='"+cellId+"'");
        dbb.execSQL("DELETE FROM "+TABLE_BOOKING_DETAILS+" WHERE "+CELL_IDDD+" ='"+cellId+"'");

    }
    public Cursor selectCellCount()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT("+CELL_IDDD+") AS cCount from "+TABLE_CELL,null);
        return cursor;
    }
    public Cursor selectAllCellId()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_CELL,null);
        return cursor;
    }


    public Cursor selectBookingTable()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_BOOKING_DETAILS,null);
        return cursor;
    }
    public Cursor selectAttendanceTable()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_ATTENDANCE,null);
        return cursor;
    }
    public Cursor selectShiftTable()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_SHIFT_DETAILS,null);
        return cursor;
    }
    public Cursor selectEmployeeTable()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_EMP_DETAILS,null);
        return cursor;
    }


    public Cursor checkInOutEntsry(String rfid, String outTime,String systemDate)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" ='"+rfid+"' AND "+OUT_TIME+" ='"+outTime+"' AND "+ENTRY_TIME+" = '"+systemDate+"'",null);
        return cursor;
    }


    public void increaseInCount(
            String designationType,
            String shiftName,
            String cONTRACTOR_CODE,
            String cellId,
            String systemDate)
    {



        try
        {

            SQLiteDatabase dbb = this.getReadableDatabase();
            dbb.execSQL("UPDATE "+TABLE_BOOKING_DETAILS+" SET "+INCOUNT+" = "+INCOUNT+" + 1 " +
                    "WHERE "+DESIGNATION+" = '"+designationType+"' " +
                    "AND "+SHIFT_NAME+" ='"+shiftName+"' " +
                    "AND "+CONTRACTOR_CODE+" ='"+cONTRACTOR_CODE+"' " +
                    "AND "+CELL_IDDD+" ='"+cellId+"' " +
                    "AND "+DATE_COLUMN+" = '"+systemDate+"'");
        }
        catch (Exception e)
        {
            Log.e("EXCEPTION",""+e);
        }





    }
    public void increaseOutCount(String designationType, String shiftName,String cONTRACTOR_CODE,String cellId,String systemDate)
    {


        try
        {

            SQLiteDatabase dbb = this.getReadableDatabase();
            dbb.execSQL("UPDATE "+TABLE_BOOKING_DETAILS+" SET "+OUTCOUNT+" = "+OUTCOUNT+" + 1 WHERE "+DESIGNATION+" = '"+designationType+"' AND "+SHIFT_NAME+" = '"+shiftName+"' AND "+CONTRACTOR_CODE+" ='"+cONTRACTOR_CODE+"' AND "+CELL_IDDD+" ='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'");
        }
        catch (Exception e)
        {
            Log.e("EXCEPTION",""+e);
        }





    }
    public void updateInCount(String designationType, String shiftName,String cONTRACTOR_CODE,String cellId,String systemDate,String inCount)
    {


        try
        {

            SQLiteDatabase dbb = this.getReadableDatabase();
            dbb.execSQL("UPDATE "+TABLE_BOOKING_DETAILS+" SET "+INCOUNT+" = '"+inCount+"' WHERE "+DESIGNATION+" = '"+designationType+"' AND "+SHIFT_NAME+" = '"+shiftName+"' AND "+CONTRACTOR_CODE+" ='"+cONTRACTOR_CODE+"' AND "+CELL_IDDD+" ='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'");
        }
        catch (Exception e)
        {
            Log.e("EXCEPTION",""+e);
        }





    }

    public void updateOutCount(String designationType, String shiftName,String cONTRACTOR_CODE,String cellId,String systemDate,String outCount)
    {


        try
        {

            SQLiteDatabase dbb = this.getReadableDatabase();
            dbb.execSQL("UPDATE "+TABLE_BOOKING_DETAILS+" SET "+OUTCOUNT+" = '"+outCount+"' WHERE "+DESIGNATION+" = '"+designationType+"' AND "+SHIFT_NAME+" = '"+shiftName+"' AND "+CONTRACTOR_CODE+" ='"+cONTRACTOR_CODE+"' AND "+CELL_IDDD+" ='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'");
        }
        catch (Exception e)
        {
            Log.e("EXCEPTION",""+e);
        }

    }

    public void updateOutCount(String query)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL(query);
    }

    public Cursor getInCountHere(String cellId,
                                 String shift,
                                 String designation,
                                 String contractorCode,
                                 String date)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("select DISTINCT a."+BARCODE_NUMBER+" AS counttt from "+TABLE_ATTENDANCE+" a,"+TABLE_EMP_DETAILS+" e " +
                "where a."+BARCODE_NUMBER+"=e."+BARCODE_NUMBER+" " +
                "AND a."+RFID_NUMBER+"=e."+RFID_NUMBER+" AND a."+TIME_COLUMN+"='"+date+"' " +
                "AND e."+CONTRACTOR_CODE+"='"+contractorCode+"' AND e."+DESIGNATION+"='"+designation+"' AND a."+SHIFT_NAME+"='"+shift+"' AND " +
                "e."+CELL_IDDD+"='"+cellId+"'",null);
        return cursor;
    }



    public Cursor getAllBookingDetailsNotShiftWise(String systemDate)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+DESIGNATION+",SUM("+TARGET+") AS "+TARGET+",SUM("+INCOUNT+") AS "+INCOUNT+",SUM("+OUTCOUNT+") AS "+OUTCOUNT+","+SHIFT_NAME+","+DATE_COLUMN+" FROM "+TABLE_BOOKING_DETAILS+" WHERE "+DATE_COLUMN+" = '"+systemDate+"' GROUP BY "+DESIGNATION+","+SHIFT_NAME+" ORDER BY "+SHIFT_NAME+" ASC",null);
        return cursor;
    }

    public Cursor returnBarcodeType(String rfid_number,String cellId,String systemDate)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+DESIGNATION+","+CONTRACTOR_CODE+" from "+TABLE_EMP_DETAILS+" WHERE "+RFID_NUMBER+" ='"+rfid_number+"' AND "+CELL_IDDD+" ='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }



    //check in out entry
    public Cursor checkInOutEntry(String rfid_number,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS  "+RETURN_COUNT+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" = '"+rfid_number+"' AND "+ENTRY_TIME+" ='"+systemDate+"'",null);
        return cursor;
    }
    public Cursor outAttendanceAlready(String rfid_number,String systemDate,String outTime)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS  "+RETURN_COUNT+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" = '"+rfid_number+"' AND "+ENTRY_TIME+" ='"+systemDate+"' AND "+OUT_TIME+" <> '"+outTime+"'",null);
        return cursor;
    }

    //ceheck employee within cell
    public Cursor checkBarcodeAvail(String rfid_number,String cellId,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS  "+RETURN_COUNT+" from "+TABLE_EMP_DETAILS+" WHERE "+RFID_NUMBER+" = '"+rfid_number+"' AND "+CELL_IDDD+"='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }

    public Cursor checkBarcodeAvailableIAttendance(String rfid_number,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" = '"+rfid_number+"' AND "+TIME_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }
    public Cursor checkDataAvailable(String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_BOOKING_DETAILS+" WHERE "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }

    public Cursor returnBarcodeNumber(String rfid_number,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+BARCODE_NUMBER+","+CELL_IDDD+" from "+TABLE_EMP_DETAILS+" WHERE "+RFID_NUMBER+" = '"+rfid_number+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }

    public Cursor getShiftName(String rfid_number,
                               String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+SHIFT_NAME+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" = '"+rfid_number+"' AND "+ENTRY_TIME+" = '"+systemDate+"'",null);
        return cursor;
    }
    //ceheck employee within cell
    public Cursor getInCount(String empDesignationType,String shiftName,String cellId,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT SUM("+INCOUNT+") AS "+INCOUNT+" from "+TABLE_BOOKING_DETAILS+" WHERE "+DESIGNATION+" ='"+empDesignationType+"' AND "+SHIFT_NAME+" ='"+shiftName+"' AND "+CELL_IDDD+" = '"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }

    public Cursor getInCountTollerence(String empDesignationType,
                                       String CONTRACTOR_CODEv,
                                       String shiftName,
                                       String cellId,
                                       String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+INCOUNT+" from "+TABLE_BOOKING_DETAILS+" WHERE "+DESIGNATION+" ='"+empDesignationType+"' AND "+SHIFT_NAME+" ='"+shiftName+"' AND "+CONTRACTOR_CODE+" ='"+CONTRACTOR_CODEv+"' AND "+CELL_IDDD+" = '"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }
    public Cursor checkAlreadyEmployeef(String RFID_no,
                                       String bnumber,
                                       String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_EMP_DETAILS+" WHERE "+RFID_NUMBER+" = '"+RFID_no+"'" +
                "AND "+BARCODE_NUMBER+" = '"+bnumber+"'" +
                "AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }

    public Cursor checkShiftAlreadyEntry(String shift_name,
                                         String shift_from,
                                         String shift_to,
                                         String systemDate,
                                         String syncStatus)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_SHIFT_DETAILS+" WHERE "+SHIFT_NAME+" = '"+shift_name+"'" +
                "AND "+FROM_TIME+" = '"+shift_from+"'" +
                "AND "+TO_TIME+" = '"+shift_to+"'" +
                "AND "+DATE_COLUMN+" = '"+systemDate+"'" +
                "AND "+SYNCH_STATUS+" = '"+syncStatus+"'",null);
        return cursor;
    }

    public Cursor getTargetCount(String empDesignationType,String shiftName,String cellId,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT SUM("+TARGET+") AS "+TARGET+" from "+TABLE_BOOKING_DETAILS+" WHERE "+DESIGNATION+" ='"+empDesignationType+"' AND "+SHIFT_NAME+" ='"+shiftName+"' AND "+CELL_IDDD+" ='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }
    public Cursor getTargetCountTollerence(
            String empDesignationType,
            String CONTRACTOR_CODEv,
            String shiftName,
            String cellId,
            String systemDate)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+UPDATED_TARGET+" from "+TABLE_BOOKING_DETAILS+" WHERE "+DESIGNATION+" ='"+empDesignationType+"' AND "+SHIFT_NAME+" ='"+shiftName+"' AND "+CELL_IDDD+" ='"+cellId+"' AND "+CONTRACTOR_CODE+" ='"+CONTRACTOR_CODEv+"' AND "+DATE_COLUMN+" = '"+systemDate+"'",null);
        return cursor;
    }
    public Cursor checkDesignationAvailForBooking(String empDesignationType, String contractor_code,String cellId,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_BOOKING_DETAILS+" WHERE "+DESIGNATION+" ='"+empDesignationType+"' AND "+CONTRACTOR_CODE+" ='"+contractor_code+"' AND "+CELL_IDDD+" = '"+cellId+"' AND "+DATE_COLUMN+" ='"+systemDate+"'",null);
        return cursor;
    }

    public Cursor getShiftTimeLast()
    {


        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+TO_TIME+" from "+TABLE_SHIFT_DETAILS+" WHERE "+DATE_COLUMN+" = '"+T.getSystemDate()+"' ORDER BY "+ID+" DESC LIMIT 1",null);
        return cursor;
    }
    public Cursor getfromTimeTotimeShiftName()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        Cursor cursor;
        cursor = dbb.rawQuery("SELECT DISTINCT "+SHIFT_NAME+","+FROM_TIME+","+TO_TIME+" FROM "+TABLE_SHIFT_DETAILS+" WHERE "+DATE_COLUMN+" = '"+T.getSystemDate()+"'",null);
        return cursor;
    }
    public Cursor getfromTimeTotimeShiftName1()
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" FROM "+TABLE_SHIFT_DETAILS+" WHERE "+DATE_COLUMN+" = '"+T.getSystemDate()+"'",null);
        return cursor;
    }
    public Cursor getShiftTimeUsingName(String shiftName)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+FROM_TIME+" from "+TABLE_SHIFT_DETAILS+" WHERE "+SHIFT_NAME+" ='"+shiftName+"'",null);
        return cursor;
    }

    public Cursor getInEntryTime(String rfidNumber,String systemDate)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+IN_TIME+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" ='"+rfidNumber+"' AND "+ENTRY_TIME+" = '"+systemDate+"'",null);
        return cursor;
    }
    public Cursor getOutEntryTime(String rfidNumber,
                                  String systemDate)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+OUT_TIME+" from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" ='"+rfidNumber+"' AND "+ENTRY_TIME+" = '"+systemDate+"'",null);
        return cursor;
    }
    //in entry
    public void insertInEntry(String rfidNumber,
                              String bNumber,
                              String intime,
                              String outtime,
                              String shiftName,
                              String synchstatus,
                              String entryTime)
    {




        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RFID_NUMBER, rfidNumber);
        values.put(BARCODE_NUMBER, bNumber);
        values.put(IN_TIME, intime);
        values.put(OUT_TIME, outtime);
        values.put(SHIFT_NAME, shiftName);
        values.put(SYNCH_STATUS, synchstatus);
        values.put(ENTRY_TIME, entryTime);

        db.insert(TABLE_ATTENDANCE, null, values);

    }

    //out entry
    public void updateOutEntry(String rfidNumber,
                               String bNumber,
                               String outtime,
                               String shiftName,
                               String synchstatus,
                               String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("UPDATE "+TABLE_ATTENDANCE+" SET "+RFID_NUMBER+"='"+rfidNumber+"',"+BARCODE_NUMBER+"='"+bNumber+"',"+OUT_TIME+"='"+outtime+"',"+SHIFT_NAME+"='"+shiftName+"',"+SYNCH_STATUS+"='"+synchstatus+"' WHERE "+RFID_NUMBER+"='"+rfidNumber+"' AND "+ENTRY_TIME+" = '"+systemDate+"'");

    }
    public void updateYesterdayOutEntry(String rfidNumber,
                               String outtime,
                               String synchstatus,
                               String systemDate)
    {

    SQLiteDatabase dbb = this.getReadableDatabase();
    dbb.execSQL("UPDATE "+TABLE_ATTENDANCE+" SET "+RFID_NUMBER+"='"+rfidNumber+"',"+OUT_TIME+"='"+outtime+"',"+SYNCH_STATUS+"='"+synchstatus+"' WHERE "+RFID_NUMBER+"='"+rfidNumber+"' AND "+ENTRY_TIME+" = '"+systemDate+"'");

    }

    public Cursor getAlreadyBooking(String desgnation,
                                    String shiftName,
                                    String contractorCode,
                                    String cellId,
                                    String systemDate,
                                    String serverId)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT COUNT(*) AS "+RETURN_COUNT+" from "+TABLE_BOOKING_DETAILS+" WHERE " +
                ""+DESIGNATION+" ='"+desgnation+"' " +
                "AND "+SHIFT_NAME+" ='"+shiftName+"' " +
                "AND "+CONTRACTOR_CODE+" ='"+contractorCode+"' " +
                "AND "+CELL_IDDD+" ='"+cellId+"' " +
                "AND "+DATE_COLUMN+" = '"+systemDate+"' " +
                "AND "+IDD+" = '"+serverId+"'",null);
        return cursor;
    }

    public void updateTarget(String inTarget,
                             String desgnation,
                             String shiftName,
                             String contractorCode,
                             String cellId,
                             String systemDate,
                             String tollerence,
                             String updatedTarget)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("UPDATE "+TABLE_BOOKING_DETAILS+" SET '"+TARGET+"' = '"+inTarget+"'," +
                "'"+UPDATED_TARGET+"' = '"+updatedTarget+"'," +
                "'"+TOLLERANCE+"' = '"+tollerence+"' " +
                "WHERE "+DESIGNATION+" ='"+desgnation+"' " +
                "AND "+SHIFT_NAME+" ='"+shiftName+"' " +
                "AND "+CONTRACTOR_CODE+" ='"+contractorCode+"' " +
                "AND "+CELL_IDDD+" ='"+cellId+"' AND "+DATE_COLUMN+" = '"+systemDate+"'");




    }
    public Cursor getTargetSum(String systemDate)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT SUM("+TARGET+") AS targets,SUM(incount) AS incounttt,SUM(outcount) AS outcounttt FROM "+TABLE_BOOKING_DETAILS+" WHERE "+DATE_COLUMN+" ='"+systemDate+"'",null);
        return cursor;
    }

    //shift details
    public void insertShiftDetails(String name,
                                  String fromtime,
                                  String totime,
                                   String systemDate,
                                   String synchStatus)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SHIFT_NAME, name);
        values.put(FROM_TIME, fromtime);
        values.put(TO_TIME, totime);
        values.put(DATE_COLUMN, systemDate);
        values.put(SYNCH_STATUS, synchStatus);


        db.insert(TABLE_SHIFT_DETAILS, null, values);

    }

    //employee details
    public void insertEmployeeDetails(String RFID_no,
                                      String bnumber,
                                      String designation,
                                      String contractor_code,
                                      String cellId,
                                      String systemDate,
                                      String syncStatus)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RFID_NUMBER, RFID_no);
        values.put(BARCODE_NUMBER, bnumber);
        values.put(DESIGNATION, designation);
        values.put(CONTRACTOR_CODE, contractor_code);
        values.put(CELL_IDDD, cellId);
        values.put(DATE_COLUMN, systemDate);
        values.put(SYNCH_STATUS, syncStatus);

        db.insert(TABLE_EMP_DETAILS, null, values);

    }

    public void updateEmployeeDetails(String RFID_no,
                                      String bnumber,
                                      String designation,
                                      String contractor_code,
                                      String cellId,
                                      String systemDate)
    {


        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("UPDATE "+TABLE_EMP_DETAILS+" SET '"+RFID_NUMBER+"' = '"+RFID_no+"','"+BARCODE_NUMBER+"' = '"+bnumber+"','"+DESIGNATION+"' = '"+designation+"','"+CONTRACTOR_CODE+"' = '"+contractor_code+"','"+CELL_IDDD+"' = '"+cellId+"'" +
                " WHERE "+BARCODE_NUMBER+" ='"+bnumber+"' AND "+DATE_COLUMN+" ='"+systemDate+"'");

    }
    //booking details
    public void insertBookingDetails(String designation,
                                     String target,
                                     String incount,
                                     String outcount,
                                     String shiftname,
                                     String contractor_code,
                                     String cell_id,
                                     String id,
                                     String systemDate,
                                     String synchStatus,
                                     String tollerence,
                                     String updated_target)
    {



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DESIGNATION, designation);
        values.put(TARGET, target);
        values.put(INCOUNT, incount);
        values.put(OUTCOUNT, outcount);
        values.put(SHIFT_NAME, shiftname);
        values.put(CONTRACTOR_CODE, contractor_code);
        values.put(CELL_IDDD, cell_id);
        values.put(IDD, id);
        values.put(DATE_COLUMN,systemDate);
        values.put(SYNCH_STATUS,synchStatus);

        values.put(TOLLERANCE,tollerence);
        values.put(UPDATED_TARGET,updated_target);


        db.insert(TABLE_BOOKING_DETAILS, null, values);

    }

    public Cursor getAttendanceInfoRFID(String synchStatus,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT "+RFID_NUMBER+" from "+TABLE_ATTENDANCE+" WHERE "+SYNCH_STATUS+"= '"+synchStatus+"' AND "+ENTRY_TIME+" = '"+systemDate+"'",null);
        return cursor;
    }
    public Cursor getAttendanceInfo(String rfidNumber,String synchStatus,String systemDate)
    {

        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor;
        cursor = dbb.rawQuery("SELECT * from "+TABLE_ATTENDANCE+" WHERE "+SYNCH_STATUS+"= '"+synchStatus+"' AND "+RFID_NUMBER+" = '"+rfidNumber+"' AND "+ENTRY_TIME+" = '"+systemDate+"'",null);
        return cursor;
    }


    public void deleteAttendanceEntry(String rfidNumber, String date)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("delete from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" ='"+rfidNumber+"' AND "+ENTRY_TIME+" ='"+date+"'");

    }
    public void deleteAttendanceEntry(String rfidNumber, String date,String outtime)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("delete from "+TABLE_ATTENDANCE+" WHERE "+RFID_NUMBER+" ='"+rfidNumber+"' AND "+ENTRY_TIME+" ='"+date+"' AND "+OUT_TIME+" <> '"+outtime+"'");

    }

    public void fireQuery(String query)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL(query);

    }
    public void updateSynchAttendanceStatus(String rfid, String synchStatus,String systemDate)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.execSQL("UPDATE "+TABLE_ATTENDANCE+" SET '"+SYNCH_STATUS+"' = '"+synchStatus+"' WHERE "+RFID_NUMBER+" = '"+rfid+"' AND "+ENTRY_TIME+" = '"+systemDate+"'");
    }

}

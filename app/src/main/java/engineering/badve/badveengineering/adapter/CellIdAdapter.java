package engineering.badve.badveengineering.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.activity.AddCellActivity;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.dashboard.HomeActivity;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.CellIdInformation;
import engineering.badve.badveengineering.pojo.CellInfo;
import engineering.badve.badveengineering.pojo.TypeInOutTargetInfo;

/**
 * Created by snsystem_amol on 12/07/2017.
 */


public class CellIdAdapter extends RecyclerView.Adapter<CellIdAdapter.ViewHolderCarLog> {

    private ArrayList<CellInfo> carlogInformation = new ArrayList<>();
    private LayoutInflater layoutInflater;

    View view ;

    public CellIdAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);

    }

    public void setCellId(ArrayList<CellInfo> countryInformationsdata) {
        this.carlogInformation = countryInformationsdata;
        notifyItemRangeChanged(0, countryInformationsdata.size());
    }

    @Override
    public ViewHolderCarLog onCreateViewHolder(ViewGroup parent, int viewType) {

        view = layoutInflater.inflate(R.layout.cell_id_row, parent, false);
        ViewHolderCarLog viewHolderScheduleholde = new ViewHolderCarLog(view);
        return viewHolderScheduleholde;
    }

    @Override
    public void onBindViewHolder(ViewHolderCarLog holder, int position) {
        try
        {

            CellInfo carLogInformation = carlogInformation.get(position);

            holder.cellIdRow.setText(carLogInformation.getCell_id());
            holder.cellNameIdRow.setText(carLogInformation.getCell_name());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount()
    {
        return carlogInformation.size();
    }

    class ViewHolderCarLog extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView
                cellIdRow,
                cellNameIdRow;

        private Button
                removeCellId;


        public ViewHolderCarLog(final View itemView)
        {
            super(itemView);

            try
            {
                cellIdRow = (TextView) itemView.findViewById(R.id.cellIdRow);
                cellNameIdRow = (TextView) itemView.findViewById(R.id.cellNameIdRow);
                removeCellId  = (Button) itemView.findViewById(R.id.removeCellId);

                itemView.setOnClickListener(this);
                removeCellId.setOnClickListener(this);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(final View v)
        {
            try
            {

                if (v.getId() == removeCellId.getId())
                {
                    CellInfo cellIdInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));

                    ArrayList<CellInfo> cell_id = S.getAllCellIDForJsonArray();

                    CellInfo cellInfo = cell_id.get(0);

                    String cellId = cellInfo.getCell_id();

                    if(cellId.equals(cellIdInformation.getCell_id().toString()))
                    {

                        T.t(v.getContext(),"Warning ! problem to delete this main cell id.");

                    }
                    else
                    {
                        if(manageTimesAndShifts())
                        {
                            //shift running

                            T.tET(v.getContext(),"Shift is running.You dont have permission to delete the cell");
                        }
                        else
                        {
                            AddCellActivity.isChangeMenu = true;
                            if (v.getContext() instanceof AddCellActivity)
                            {
                                ((AddCellActivity) v.getContext()).deleteCellId(cellIdInformation.getCell_id().toString());

                            }
                        }
                       // T.t(v.getContext(),"ready to delete.");


                    }

                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                T.t(v.getContext(),""+e);
            }

        }

    }

    private Boolean manageTimesAndShifts()
    {

        boolean status = false;

        ArrayList<String> SHIFT_NAMES = new ArrayList<>();

        try
        {
            String [] systemDateArray = T.getSystemDateTime().split(" ");
            String systemDate = systemDateArray[0];


            ArrayList<String> fromTimeTotimeShiftName = S.getfromTimeTotimeShiftName(MyApplication.db);

            String [] shiftname = new String[fromTimeTotimeShiftName.size()];
            String [] timessFrom = new String[fromTimeTotimeShiftName.size()];
            String [] timessTo = new String[fromTimeTotimeShiftName.size()];


            for(int  i = 0; i < fromTimeTotimeShiftName.size(); i++)
            {
                String [] fromTimeTotimeShiftNameData = fromTimeTotimeShiftName.get(i).split("#");

                shiftname[i] = fromTimeTotimeShiftNameData[0];
                timessFrom[i] = fromTimeTotimeShiftNameData[1];
                timessTo[i] = fromTimeTotimeShiftNameData[2];
            }

            for(int i = 0; i < timessFrom.length; i++)
            {
                String shiftTimesFrom = convertHrsTosecond(timessFrom[i]);
                String shiftTimesTo = convertHrsTosecond(timessTo[i]);
                String getSystemTime = T.getSystemTimeTwentyFourHrs();
                String systemTime = convertHrsTosecond(getSystemTime);

                if(Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesFrom) && Integer.valueOf(systemTime) <= Integer.valueOf(shiftTimesTo))
                {
                    SHIFT_NAMES.add(shiftname[i]);
                }
                else if(Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesFrom) && Integer.valueOf(systemTime) >= Integer.valueOf(shiftTimesTo))
                {
                    //T.t(HomeActivity.this,""+shiftname[i]);
                    SHIFT_NAMES.add(shiftname[i]);
                }
            }

            if(SHIFT_NAMES.isEmpty())
            {

                status = false;
                //T.tI(HomeActivity.this,"Shift details not found");

            }
            else
            {
                status = true;
               // txt_shift_name.setText(SHIFT_NAMES.get(SHIFT_NAMES.size() - 1));
              //  BeplLog.e("Shift_name",""+SHIFT_NAMES.get(SHIFT_NAMES.size() - 1));
            }
        }
        catch (Exception e)
        {

        }

        return  status;


    }
    private static String convertHrsTosecond(String timeFormatString)
    {
        String timeSplit[] = timeFormatString.split(":");
        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 +  Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);

        return String.valueOf(seconds);
    }


}

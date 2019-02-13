package engineering.badve.badveengineering.reportadapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.activity.AddCellActivity;
import engineering.badve.badveengineering.adapter.CellIdAdapter;
import engineering.badve.badveengineering.classes.MyApplication;
import engineering.badve.badveengineering.classes.T;
import engineering.badve.badveengineering.database.S;
import engineering.badve.badveengineering.interfaces.Constants;
import engineering.badve.badveengineering.pojo.BookingDetailsInformation;
import engineering.badve.badveengineering.pojo.CellIdInformation;
import engineering.badve.badveengineering.reports.TableBookingDetailsActivity;

/**
 * Created by sns003 on 19-Dec-17.
 */

public class BookingDetailsAdapter extends RecyclerView.Adapter<BookingDetailsAdapter.ViewHolderCarLog> {

    private ArrayList<BookingDetailsInformation> carlogInformation = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private TableBookingDetailsActivity activity;

    View view ;

    public BookingDetailsAdapter(Context context)
    {
        layoutInflater = LayoutInflater.from(context);

    }

    public void setCellId(ArrayList<BookingDetailsInformation> countryInformationsdata,TableBookingDetailsActivity activity) {
        this.carlogInformation = countryInformationsdata;
        this.activity = activity;
        notifyItemRangeChanged(0, countryInformationsdata.size());
    }

    @Override
    public BookingDetailsAdapter.ViewHolderCarLog onCreateViewHolder(ViewGroup parent, int viewType) {

        view = layoutInflater.inflate(R.layout.booking_details_row, parent, false);
        ViewHolderCarLog viewHolderScheduleholde = new ViewHolderCarLog(view);
        return viewHolderScheduleholde;
    }

    @Override
    public void onBindViewHolder(ViewHolderCarLog holder, int position) {
        try
        {

            BookingDetailsInformation carLogInformation = carlogInformation.get(position);


            holder.IDTextView.setText(carLogInformation.getIDTextView());
            holder.DESIGNATIONTextView.setText(carLogInformation.getDESIGNATIONTextView());
            holder.TARGETTextView.setText(carLogInformation.getTARGETTextView());
            holder.INCOUNTextView.setText(carLogInformation.getINCOUNTextView());
            holder.OUTCOUNTTextView.setText(carLogInformation.getOUTCOUNTTextView());
            holder.SHIFT_NAMETextView.setText(carLogInformation.getSHIFT_NAMETextView());
            holder.CONTRACTOR_CODETextView.setText(carLogInformation.getCONTRACTOR_CODETextView());
            holder.CELL_IDDDTextView.setText(carLogInformation.getCELL_IDDDTextView());
            holder.IDDTextView.setText(carLogInformation.getIDDTextView());
            holder.DATE_COLUMNTextView.setText(carLogInformation.getDATE_COLUMNTextView());
            holder.SYNCH_STATUSTextView.setText(carLogInformation.getSYNCH_STATUSTextView());
            holder.TOLLERANCETextView.setText(carLogInformation.getTOLLERANCETextView());
            holder.UPDATED_TARGETTextView.setText(carLogInformation.getUPDATED_TARGETTextView());


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

                IDTextView,
                DESIGNATIONTextView,
                TARGETTextView,
                INCOUNTextView,
                OUTCOUNTTextView,
                SHIFT_NAMETextView,
                CONTRACTOR_CODETextView,
                CELL_IDDDTextView,
                IDDTextView,
                DATE_COLUMNTextView,
                SYNCH_STATUSTextView,
                TOLLERANCETextView,
                UPDATED_TARGETTextView;
        private Button

                updateButton;

        private LinearLayout
                removeCellId;


        public ViewHolderCarLog(final View itemView)
        {
            super(itemView);

            try
            {



                        updateButton = (Button) itemView.findViewById(R.id.updateButton);

                        IDTextView = (TextView) itemView.findViewById(R.id.IDTextView);
                        DESIGNATIONTextView = (TextView) itemView.findViewById(R.id.DESIGNATIONTextView);
                        TARGETTextView = (TextView) itemView.findViewById(R.id.TARGETTextView);
                        INCOUNTextView = (TextView) itemView.findViewById(R.id.INCOUNTextView);
                        OUTCOUNTTextView = (TextView) itemView.findViewById(R.id.OUTCOUNTTextView);
                        SHIFT_NAMETextView = (TextView) itemView.findViewById(R.id.SHIFT_NAMETextView);
                        CONTRACTOR_CODETextView = (TextView) itemView.findViewById(R.id.CONTRACTOR_CODETextView);
                        CELL_IDDDTextView = (TextView) itemView.findViewById(R.id.CELL_IDDDTextView);
                        IDDTextView = (TextView) itemView.findViewById(R.id.IDDTextView);
                        DATE_COLUMNTextView = (TextView) itemView.findViewById(R.id.DATE_COLUMNTextView);
                        SYNCH_STATUSTextView = (TextView) itemView.findViewById(R.id.SYNCH_STATUSTextView);
                        TOLLERANCETextView = (TextView) itemView.findViewById(R.id.TOLLERANCETextView);
                        UPDATED_TARGETTextView = (TextView) itemView.findViewById(R.id.UPDATED_TARGETTextView);


                itemView.setOnClickListener(this);

                updateButton.setOnClickListener(this);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        BookingDetailsInformation bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
                        activity.deleteDataId(bookingDetailsInformation.getIDTextView());

                        return false;
                    }
                });


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

               /* if (v.getId() == deletButton.getId())
                {
                    BookingDetailsInformation bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
                    activity.deleteDataId(bookingDetailsInformation.getIDTextView());
                }*/
                if (v.getId() == updateButton.getId())
                {
                    BookingDetailsInformation bookingDetailsInformation = carlogInformation.get(Integer.valueOf(getAdapterPosition()));
                    activity.updateDataId(bookingDetailsInformation.getIDTextView());
                }




            }
            catch (Exception e)
            {
                e.printStackTrace();
                T.t(v.getContext(),""+e);
            }

        }

    }


    private static String convertHrsTosecond(String timeFormatString)
    {
        String timeSplit[] = timeFormatString.split(":");
        int seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 +  Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);

        return String.valueOf(seconds);
    }


}

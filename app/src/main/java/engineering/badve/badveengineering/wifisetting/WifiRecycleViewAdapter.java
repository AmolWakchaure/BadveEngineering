package engineering.badve.badveengineering.wifisetting;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import engineering.badve.badveengineering.R;

/**
 * Created by USER1 on 5/10/2017.
 */

public class WifiRecycleViewAdapter extends RecyclerView.Adapter<WifiRecyclerViewHolder> {

    private List<ScanResult> wifiScanList;
    private WifiConfigurationActivity.OnListItemClick onListItemClick;

    public WifiRecycleViewAdapter(List<ScanResult> wifiScanList, WifiConfigurationActivity.OnListItemClick onListItemClick){
        this.wifiScanList = wifiScanList;
        this.onListItemClick = onListItemClick;
    }

    public void setData( List<ScanResult> wifiScanList){
        this.wifiScanList = wifiScanList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return wifiScanList.size();
    }

    @Override
    public WifiRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wifi_list_item_layout, parent, false);

        return new WifiRecyclerViewHolder(itemView, onListItemClick);
    }

    @Override
    public void onBindViewHolder(WifiRecyclerViewHolder holder, int position) {
        holder.setData(wifiScanList.get(position));
    }


}

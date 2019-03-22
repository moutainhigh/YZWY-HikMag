package com.example.yzwy.lprmag.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzwy.lprmag.R;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.PTZCommand;
import com.example.yzwy.lprmag.bean.PresetBean;
import com.example.yzwy.lprmag.util.Tools;
import com.example.yzwy.lprmag.wifimess.model.SendOrder;
import com.example.yzwy.lprmag.wifimess.thread.ConnectThread;

import java.util.List;

import static com.hikvision.netsdk.PTZPresetCmd.SET_PRESET;

public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.ViewHolder> {

    private final ConnectThread connectThread;
    private List<PresetBean> mPresetList;
    private int m_iPlayID = -1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mPresetView;
        TextView tv_serialNumber_itPreset;
        TextView tv_persetNum_itpreset;
        TextView btn_goto_itPreset;
        TextView btn_setPreset_itPreset;
        TextView btn_delete_itPreset;
        TextView tv_geomagneticAddress_itpreset;

        public ViewHolder(View view) {
            super(view);
            mPresetView = view;
            tv_serialNumber_itPreset = (TextView) view.findViewById(R.id.tv_serialNumber_itPreset);
            tv_persetNum_itpreset = (TextView) view.findViewById(R.id.tv_persetNum_itpreset);
            tv_geomagneticAddress_itpreset = (TextView) view.findViewById(R.id.tv_geomagneticAddress_itpreset);
            btn_goto_itPreset = (Button) view.findViewById(R.id.btn_goto_itPreset);
            btn_setPreset_itPreset = (Button) view.findViewById(R.id.btn_setPreset_itPreset);
            btn_delete_itPreset = (Button) view.findViewById(R.id.btn_delete_itPreset);
        }
    }

    public PresetAdapter(List<PresetBean> fruitList, int m_iPlayID, ConnectThread connectThread) {
        this.mPresetList = fruitList;
        this.m_iPlayID = m_iPlayID;
        this.connectThread = connectThread;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preset, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.mPresetView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                PresetBean presetBean = mPresetList.get(position);
//                Toast.makeText(v.getContext(), "you clicked view  列表  " + presetBean.getCameraEquipmentNum(), Toast.LENGTH_SHORT).show();
//            }
//        });

        /**
         * 跳转预置点
         * */
        holder.btn_goto_itPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PresetBean presetBean = mPresetList.get(position);
                int PerSetNum = Integer.valueOf(presetBean.getPersetNumber());

                boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, PTZCommand.GOTO_PRESET, PerSetNum);
                if (b) {
                    Tools.Toast(v.getContext(), "预置点转到成功");
                } else {

                    Tools.Toast(v.getContext(), "预置点转到失败");
                }
            }
        });

        /**
         * 设置预置点
         * */
        holder.btn_setPreset_itPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PresetBean presetBean = mPresetList.get(position);
                int PerSetNum = Integer.valueOf(presetBean.getPersetNumber());

                boolean b = HCNetSDK.getInstance().NET_DVR_PTZPreset(m_iPlayID, SET_PRESET, PerSetNum);
                if (b) {
                    Tools.Toast(v.getContext(), "预置点修改成功");
                } else {

                    Tools.Toast(v.getContext(), "预置点修改失败");
                }
            }
        });

        /**
         * 删除预置点
         * */
        holder.btn_delete_itPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PresetBean presetBean = mPresetList.get(position);
                connectThread.sendData(SendOrder.deletePersetData(presetBean.getPersetNumber()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PresetBean presetBean = mPresetList.get(position);


        holder.tv_serialNumber_itPreset.setText(presetBean.getRK3399AddressNumber());
        holder.tv_persetNum_itpreset.setText(presetBean.getPersetNumber());
        holder.tv_geomagneticAddress_itpreset.setText(presetBean.getGeomagnetismAddressNumber());
    }


    /**
     * =========================================================
     * 返回多少个布局
     */
    @Override
    public int getItemCount() {
        return mPresetList.size();
    }

    /**
     * =========================================================
     * 返回具体item 的 item ID 号
     */
    @Override
    public long getItemId(int id) {

        return id;
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 2);
    }

}
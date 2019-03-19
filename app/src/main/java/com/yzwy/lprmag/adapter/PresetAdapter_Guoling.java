//package com.yzwy.lprmag.adapter;
//
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.yzwy.lprmag.R;
//import com.yzwy.lprmag.bean.PresetBean;
//
//import java.util.List;
//
//public class PresetAdapter_Guoling extends RecyclerView.Adapter<PresetAdapter_Guoling.ViewHolder> {
//
//    private List<PresetBean> mPresetList;
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        View mPresetView;
//        TextView tv_serialNumber_itPreset;
//        TextView tv_cameraEquipmentNum_itpreset;
//        TextView btn_goto_itPreset;
//        TextView btn_setPreset_itPreset;
//        TextView btn_delete_itPreset;
//
//        public ViewHolder(View view) {
//            super(view);
//            mPresetView = view;
//            tv_serialNumber_itPreset = (TextView) view.findViewById(R.id.tv_serialNumber_itPreset);
//            tv_cameraEquipmentNum_itpreset = (TextView) view.findViewById(R.id.tv_cameraEquipmentNum_itpreset);
//            btn_goto_itPreset = (Button) view.findViewById(R.id.btn_goto_itPreset);
//            btn_setPreset_itPreset = (Button) view.findViewById(R.id.btn_setPreset_itPreset);
//            btn_delete_itPreset = (Button) view.findViewById(R.id.btn_delete_itPreset);
//        }
//    }
//
//    public PresetAdapter_Guoling(List<PresetBean> fruitList) {
//        mPresetList = fruitList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preset, parent, false);
//        final ViewHolder holder = new ViewHolder(view);
//        holder.mPresetView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                PresetBean presetBean = mPresetList.get(position);
//                Toast.makeText(v.getContext(), "you clicked view  列表  " + presetBean.getCameraEquipmentNum(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.btn_goto_itPreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                PresetBean presetBean = mPresetList.get(position);
//                Toast.makeText(v.getContext(), "you clicked 调用 " + presetBean.getCameraEquipmentNum(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.btn_setPreset_itPreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                PresetBean presetBean = mPresetList.get(position);
//                Toast.makeText(v.getContext(), "you clicked 设置 " + presetBean.getCameraEquipmentNum(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.btn_delete_itPreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                PresetBean presetBean = mPresetList.get(position);
//                Toast.makeText(v.getContext(), "you clicked 删除 " + presetBean.getCameraEquipmentNum(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        PresetBean presetBean = mPresetList.get(position);
//
//
//        holder.tv_serialNumber_itPreset.setText(presetBean.getSerialNumber());
//        holder.tv_cameraEquipmentNum_itpreset.setText(presetBean.getCameraEquipmentNum());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mPresetList.size();
//    }
//
//}
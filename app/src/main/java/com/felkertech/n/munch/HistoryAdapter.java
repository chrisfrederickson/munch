package com.felkertech.n.munch;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felkertech.n.munch.Activities.FoodInfo;
import com.felkertech.n.munch.Objects.HistoryItem;
import com.felkertech.n.munch.Objects.StreamItem;
import com.felkertech.n.munch.Utils.StreamTypes;

import java.util.ArrayList;

/**
 * Created by N on 3/5/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter {
    private ArrayList<StreamItem> mDataset;
    private Context mContext;
    public static String TAG = "munch::HistoryAdapter";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public int type;
        public ViewHolder(View v, int t) {
            super(v);
            Log.d(TAG, "creating viewholder");
            mView = v;
            type = t;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(ArrayList<StreamItem> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
        Log.d(TAG, "Initiated the historyadapter with "+myDataset.size()+" items");
        Log.d(TAG, myDataset.get(0).getTitle());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        Log.d(TAG, "Creating viewholder");
        int layoutId;
        switch(viewType) {
            case StreamTypes.TYPE_ADVICE:
                layoutId = R.layout.history_advice;
                break;
            case StreamTypes.TYPE_ITEM:
                layoutId = R.layout.history_item;
                break;
            default:
                layoutId = R.layout.history_item;
                break;
        }
        Log.d(TAG, "Choosing layout "+layoutId+" from "+viewType);
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, viewType);
        Log.d(TAG, "Adding view and posting");
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, mDataset.get(position).getItemClass()+" type is being drawn at pos "+position);
        switch(mDataset.get(position).getItemClass()) {
            case StreamTypes.TYPE_ADVICE:
                ((ImageView) holder.itemView.findViewById(R.id.hero)).setImageResource(mDataset.get(position).getRelevantImage());
                ((TextView) holder.itemView.findViewById(R.id.title)).setText(mDataset.get(position).getTitle());
                ((TextView) holder.itemView.findViewById(R.id.descriptor)).setText(mDataset.get(position).getSecondaryTitle());
                holder.itemView.findViewById(R.id.button_dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.setVisibility(View.GONE);
                        mDataset.remove(position);
                        notifyDataSetChanged();
                    }
                });
                holder.itemView.findViewById(R.id.button_learn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, FoodInfo.class);
                        i.putExtra("FOOD_TYPE", ((TextView) holder.itemView.findViewById(R.id.title)).getText());
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Smexy activity transition
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                    Pair.create(holder.itemView.findViewById(R.id.food_icon), "food_icon"),
                                    Pair.create(holder.itemView.findViewById(R.id.title), "food_name"));
                            mContext.startActivity(i, options.toBundle());
                        } else {
                            mContext.startActivity(i);
                        }/*

                        AlertDialog d = new AlertDialog.Builder(mContext)
                                .setTitle(((TextView) holder.itemView.findViewById(R.id.title)).getText())
                                .setMessage(mDataset.get(position).getTertiaryTitle())
                                .create();
                        d.show();*/
                    }
                });
                break;
            default:
                ((TextView) holder.itemView.findViewById(R.id.title)).setText(mDataset.get(position).getTitle());
                ((TextView) holder.itemView.findViewById(R.id.secondaryTitle)).setText(mDataset.get(position).getSecondaryTitle());
                ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setText(mDataset.get(position).getTertiaryTitle());
                holder.itemView.findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Open up data activity
                        Intent i = new Intent(mContext, FoodInfo.class);
                        i.putExtra("FOOD_NAME", ((TextView) holder.itemView.findViewById(R.id.title)).getText());
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Smexy activity transition
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                    Pair.create(holder.itemView.findViewById(R.id.food_icon), "food_icon"),
                                    Pair.create(holder.itemView.findViewById(R.id.title), "food_name"));
                            mContext.startActivity(i, options.toBundle());
                        } else {
                            mContext.startActivity(i);
                        }
                    }
                });
                Log.d(TAG, "There are "+((HistoryItem) mDataset.get(position)).getCalories()+" calories");
                if(((HistoryItem) mDataset.get(position)).getCalories() < 300)
                    ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setTextColor(color(R.color.green_500));
                else if(((HistoryItem) mDataset.get(position)).getCalories() < 750)
                    ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setTextColor(color(R.color.orange_500));
                else
                    ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setTextColor(color(R.color.red_500));

                ((ImageView) holder.itemView.findViewById(R.id.food_icon)).setImageResource(mDataset.get(position).getRelevantImage());
                break;
        }
    }
    @Override
    public int getItemViewType(int pos) {
        return mDataset.get(pos).getItemClass();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public int color(int id) {
        return mContext.getResources().getColor(id);
    }
}

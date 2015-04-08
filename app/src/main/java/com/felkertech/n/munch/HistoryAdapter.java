package com.felkertech.n.munch;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.felkertech.n.munch.Activities.FoodInfo;
import com.felkertech.n.munch.Objects.CalendarItem;
import com.felkertech.n.munch.Objects.HistoryItem;
import com.felkertech.n.munch.Objects.StreamItem;
import com.felkertech.n.munch.Objects.StreamPhoto;
import com.felkertech.n.munch.Utils.StreamTypes;
import com.felkertech.n.munch.database.FeedReaderContract;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by N on 3/5/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter {
    private ArrayList<StreamItem> mDataset;
    private ArrayList<View> mViews;
    private Context mContext;
    private int lastPosition = -1;
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
//            Log.d(TAG, "creating viewholder");
            mView = v;
            type = t;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(ArrayList<StreamItem> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
        mViews = new ArrayList<>();
//        Log.d(TAG, "Initiated the historyadapter with "+myDataset.size()+" items");
//        Log.d(TAG, myDataset.get(0).getTitle());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
//        Log.d(TAG, "Creating viewholder");
        int layoutId;
        switch(viewType) {
            case StreamTypes.TYPE_ADVICE:
                layoutId = R.layout.history_advice;
                break;
            case StreamTypes.TYPE_ITEM:
                layoutId = R.layout.history_item;
                break;
            case StreamTypes.TYPE_DAY:
                layoutId = R.layout.history_date;
                break;
            case StreamTypes.TYPE_GALLERY:
                layoutId = R.layout.stream_photo;
                break;
            case StreamTypes.TYPE_CALENDAR:
                layoutId = R.layout.history_calendar;
                break;
            default:
                layoutId = R.layout.history_item;
                break;
        }
//        Log.d(TAG, "Choosing layout "+layoutId+" from "+viewType);
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, viewType);
//        Log.d(TAG, "Adding view and posting");
        return vh;
    }

    Handler h = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            if(inputMessage.getData().getBoolean("anim_in")) {
                setAnimation(mViews.get(inputMessage.getData().getInt("position")), inputMessage.getData().getInt("position"));
            } else {
                hideAnimation(mViews.get(inputMessage.getData().getInt("position")), inputMessage.getData().getInt("position"));
            }
        }
    };

        // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        Log.d(TAG, mDataset.get(position).getItemClass()+" type is being drawn at pos "+position);
        mViews.add(position, holder.itemView);
        Message m = getMessageParams(true, position);
        /*if(position < ) {
            h.sendMessageDelayed(m, 100*position);
        } else {
        */    h.sendMessageDelayed(m, 1);
//        }
        switch(mDataset.get(position).getItemClass()) {
            case StreamTypes.TYPE_ADVICE:
                ((ImageView) holder.itemView.findViewById(R.id.hero)).setImageResource(mDataset.get(position).getRelevantImage());
                ((TextView) holder.itemView.findViewById(R.id.title)).setText(mDataset.get(position).getTitle());
                ((TextView) holder.itemView.findViewById(R.id.descriptor)).setText(mDataset.get(position).getSecondaryTitle());
                holder.itemView.findViewById(R.id.button_dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
                        holder.itemView.startAnimation(animation);
                        Handler hander = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message m) {
                                mDataset.remove(position);
                                notifyDataSetChanged();
                            }
                        };
                        hander.sendEmptyMessageDelayed(0, animation.getDuration());
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
                            options.makeCustomAnimation(mContext, R.anim.enter_left, R.anim.do_nothing);
                            mContext.startActivity(i, options.toBundle());
                        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(mContext, R.anim.enter_left, R.anim.do_nothing);
                            mContext.startActivity(i, options.toBundle());
                        } else
                            mContext.startActivity(i);/*

                        AlertDialog d = new AlertDialog.Builder(mContext)
                                .setTitle(((TextView) holder.itemView.findViewById(R.id.title)).getText())
                                .setMessage(mDataset.get(position).getTertiaryTitle())
                                .create();
                        d.show();*/
                    }
                });
                break;
            case StreamTypes.TYPE_DAY:
                ((TextView) holder.itemView.findViewById(R.id.date)).setText(mDataset.get(position).getTitle());
                break;
            case StreamTypes.TYPE_CALENDAR:
                Log.d(TAG, mDataset.get(position).getTitle());
                Log.d(TAG, holder.itemView.findViewById(R.id.date).toString());
                Log.d(TAG, holder.itemView.findViewById(R.id.food_calories).toString());
                ((TextView) holder.itemView.findViewById(R.id.date)).setText(mDataset.get(position).getTitle());
                ((TextView) holder.itemView.findViewById(R.id.food_calories)).setText(((CalendarItem) (mDataset.get(position))).getCalories()+" Calories");
                holder.itemView.findViewById(R.id.food_calories).setBackgroundColor(mContext.getResources().getColor(
                        getColorMagnitude(1000, ((CalendarItem) (mDataset.get(position))).getCalories(), 4000)));
                break;
            case StreamTypes.TYPE_ITEM: //Food item
                ((TextView) holder.itemView.findViewById(R.id.title)).setText(mDataset.get(position).getTitle());
                ((TextView) holder.itemView.findViewById(R.id.secondaryTitle)).setText(mDataset.get(position).getSecondaryTitle());
                ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setText(mDataset.get(position).getTertiaryTitle());
                holder.itemView.findViewById(R.id.arrow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Open up data activity
                        Intent i = new Intent(mContext, FoodInfo.class);
                        i = pushFood(i, holder, position);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Smexy activity transition
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                    Pair.create(holder.itemView.findViewById(R.id.food_icon), "food_icon"),
                                    Pair.create(holder.itemView.findViewById(R.id.title), "food_name"));
                            options.makeCustomAnimation(mContext, R.anim.enter_left, R.anim.do_nothing);
                            mContext.startActivity(i, options.toBundle());
                        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(mContext, R.anim.enter_left, R.anim.do_nothing);
                            mContext.startActivity(i, options.toBundle());
                        } else
                            mContext.startActivity(i);
                    }
                });
//                Log.d(TAG, "There are "+((HistoryItem) mDataset.get(position)).getCalories()+" calories");
                if(((HistoryItem) mDataset.get(position)).getCalories() < 200)
                    ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setTextColor(color(R.color.nutrition_good));
                else if(((HistoryItem) mDataset.get(position)).getCalories() < 400)
                    ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setTextColor(color(R.color.nutrition_okay));
                else
                    ((TextView) holder.itemView.findViewById(R.id.tertiaryTitle)).setTextColor(color(R.color.nutrition_bad));

                ((ImageView) holder.itemView.findViewById(R.id.food_icon)).setImageResource(mDataset.get(position).getRelevantImage());
                break;
            case StreamTypes.TYPE_GALLERY:
                ((TextView) holder.itemView.findViewById(R.id.title)).setText(mDataset.get(position).getTitle());
                Ion.with(((ImageView) holder.itemView.findViewById(R.id.hero)))
                        .placeholder(R.drawable.ic_launcher)
                        .load(((StreamPhoto) mDataset.get(position)).getEntry().getURI().toString());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Open up data activity -- Same as historyItem
                        Intent i = new Intent(mContext, FoodInfo.class);
                        i = pushFood(i, holder, position);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Smexy activity transition
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                    Pair.create(holder.itemView.findViewById(R.id.food_icon), "food_icon"),
                                    Pair.create(holder.itemView.findViewById(R.id.title), "food_name"));
                            options.makeCustomAnimation(mContext, R.anim.enter_left, R.anim.do_nothing);
                            mContext.startActivity(i, options.toBundle());
                        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(mContext, R.anim.enter_left, R.anim.do_nothing);
                            mContext.startActivity(i, options.toBundle());
                        } else
                            mContext.startActivity(i);
                    }
                });
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
    public void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    public void hideAnimation(View viewToAnimate, int position) {
        /*for(View v: mViews) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            v.startAnimation(animation);
        }*/
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }
    public void hideAll() {
        int i = 0;
        if(mViews == null)
            return;
        for(View v: mViews) {
            h.sendMessageDelayed(getMessageParams(false, i), 20*i);
            i++;
        }
    }
    public Message getMessageParams(boolean anim_in, int position) {
        Message m = new Message();
        Bundle b = new Bundle();
        b.putBoolean("anim_in", anim_in);
        b.putInt("position", position);
        m.setData(b);
        return m;
    }
    public int getColorMagnitude(int low, int value, int high) {
        double p = 100.0*(value-low)/(high-low);
        Log.d(TAG, "Given parameters of " + low + ", " + value + ", and " + high + ", we've returned " + p);
        if(p < 0)
            return R.color.md_red_900;
        if(p < 5)
            return R.color.md_light_blue_500;
        else if(p < 10)
            return R.color.md_light_blue_700;
        else if(p < 15)
            return R.color.md_blue_700;
        else if(p < 20)
            return R.color.md_blue_900;
        else if(p < 25)
            return R.color.md_light_green_500;
        else if(p < 30)
            return R.color.md_light_green_700;
        else if(p < 35)
            return R.color.md_green_300;
        else if(p < 40)
            return R.color.md_green_500;
        else if(p < 45)
            return R.color.md_green_700;
        else if(p < 50)
            return R.color.md_yellow_500;
        else if(p < 55)
            return R.color.md_yellow_700;
        else if(p < 60)
            return R.color.md_amber_500;
        else if(p < 65)
            return R.color.md_amber_700;
        else if(p < 70)
            return R.color.orange_500;
        else if(p < 75)
            return R.color.md_orange_700;
        else if(p < 80)
            return R.color.deep_orange_500;
        else if(p < 85)
            return R.color.md_deep_orange_700;
        else if(p < 90)
            return R.color.red_500;
        else if(p < 95)
            return R.color.md_red_700;
        else
            return R.color.md_red_900;
    }

    /**
     * A single method to add all the relevant food attributes to an intent
     * @param i Intent that you wish to call soon
     * @return Intent containing all of the food attributes as extras
     */
    public Intent pushFood(Intent i, RecyclerView.ViewHolder holder, int position) {
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOOD_ITEM, ((TextView) holder.itemView.findViewById(R.id.title)).getText());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOODURI, ((HistoryItem)mDataset.get(position)).getEntry().getURI().toString());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_TAGLINE, ((HistoryItem)mDataset.get(position)).getEntry().getTagline());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CALORIES, ((HistoryItem)mDataset.get(position)).getEntry().getCalories());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_WATER, ((HistoryItem)mDataset.get(position)).getEntry().getWater());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_PROTEIN, ((HistoryItem)mDataset.get(position)).getEntry().getProtein());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_LIPID, ((HistoryItem)mDataset.get(position)).getEntry().getLipid());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CARB, ((HistoryItem)mDataset.get(position)).getEntry().getCarb());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FIBER, ((HistoryItem)mDataset.get(position)).getEntry().getFiber());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SUGAR, ((HistoryItem)mDataset.get(position)).getEntry().getSugar());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CALCIUM, ((HistoryItem)mDataset.get(position)).getEntry().getCalcium());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_IRON, ((HistoryItem)mDataset.get(position)).getEntry().getIron());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_MAGNESIUM, ((HistoryItem)mDataset.get(position)).getEntry().getMagnesium());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_PHOSPHORUS, ((HistoryItem)mDataset.get(position)).getEntry().getPhosphorus());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_POTASSIUM, ((HistoryItem)mDataset.get(position)).getEntry().getPotassium());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SODIUM, ((HistoryItem)mDataset.get(position)).getEntry().getSodium());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_ZINC, ((HistoryItem)mDataset.get(position)).getEntry().getZinc());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_COPPER, ((HistoryItem)mDataset.get(position)).getEntry().getCopper());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_MANGANESE, ((HistoryItem)mDataset.get(position)).getEntry().getManganese());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SELENIUM, ((HistoryItem)mDataset.get(position)).getEntry().getSelenium());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_C, ((HistoryItem)mDataset.get(position)).getEntry().getVit_c());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_THIAMIN, ((HistoryItem)mDataset.get(position)).getEntry().getThiamin());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_RIBOFLAVIN, ((HistoryItem)mDataset.get(position)).getEntry().getRiboflavin());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_NIACIN, ((HistoryItem)mDataset.get(position)).getEntry().getNiacin());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_PHANTO_ACID, ((HistoryItem)mDataset.get(position)).getEntry().getPhanto_acid());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B6, ((HistoryItem)mDataset.get(position)).getEntry().getVit_b6());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_FOLATE, ((HistoryItem)mDataset.get(position)).getEntry().getFolate());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLINE, ((HistoryItem)mDataset.get(position)).getEntry().getCholine());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_B12, ((HistoryItem)mDataset.get(position)).getEntry().getVit_b12());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_A, ((HistoryItem)mDataset.get(position)).getEntry().getVit_a());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_RETINOL, ((HistoryItem)mDataset.get(position)).getEntry().getRetinol());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_ALPHA_CAROT, ((HistoryItem)mDataset.get(position)).getEntry().getAlpha_carot());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CAROT, ((HistoryItem)mDataset.get(position)).getEntry().getBeta_carot());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_BETA_CRYPT, ((HistoryItem)mDataset.get(position)).getEntry().getBeta_crypt());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_LYCOPHENE, ((HistoryItem)mDataset.get(position)).getEntry().getLycophene());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_LUTEIN_ZEAXANTHIN, ((HistoryItem)mDataset.get(position)).getEntry().getLutein_zeaxanthin());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_E, ((HistoryItem)mDataset.get(position)).getEntry().getVit_e());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_D, ((HistoryItem)mDataset.get(position)).getEntry().getVit_d());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_VIT_K, ((HistoryItem)mDataset.get(position)).getEntry().getVit_k());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_SATURATED_FAT, ((HistoryItem)mDataset.get(position)).getEntry().getSaturated_fat());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_MONOSATURATED_FAT, ((HistoryItem)mDataset.get(position)).getEntry().getMonosaturated_fat());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_POLYSATURATED_FAT, ((HistoryItem)mDataset.get(position)).getEntry().getPolysaturated_fat());
        i.putExtra(FeedReaderContract.FeedEntry.COLUMN_NAME_CHOLESTEROL, ((HistoryItem)mDataset.get(position)).getEntry().getCholesterol());
        i.putExtra("FOOD_DESCRIPTION", ((HistoryItem) mDataset.get(position)).getEntry().getSubtitle());
        return i;
    }
}

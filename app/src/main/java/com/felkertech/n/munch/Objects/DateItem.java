package com.felkertech.n.munch.Objects;

import com.felkertech.n.munch.Utils.StreamTypes;

import java.util.Date;

/**
 * Created by N on 3/21/2015.
 */
public class DateItem extends StreamItem {
    public String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public DateItem(String t) {
        super(StreamTypes.TYPE_DAY, t, "","", 0);
    }
    public DateItem(Date d) {
        super(StreamTypes.TYPE_DAY, "", "", "", 0);
        setTitle(convertDate(d));
    }
    public String convertDate(Date d) {
        Date today = new Date();
        if(today.getDate() == d.getDate()+1 && today.getMonth() == d.getMonth() && today.getYear() == d.getYear())
            return "Yesterday";
        else if(today.getDate() == d.getDate()+2 && today.getMonth() == d.getMonth() && today.getYear() == d.getYear())
            return "Two days ago";
        else if(today.getDate() == d.getDate()+2 && today.getMonth() == d.getMonth() && today.getYear() == d.getYear())
            return "Three days ago";
        else if(today.getDate() == d.getDate()+2 && today.getMonth() == d.getMonth() && today.getYear() == d.getYear())
            return "Four days ago";
        else
            return months[d.getMonth()] + " " + d.getDate();
//        return d.getDate()+"";
    }
}

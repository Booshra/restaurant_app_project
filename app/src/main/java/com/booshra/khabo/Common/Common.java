package com.booshra.khabo.Common;

import com.booshra.khabo.Model.User;

public class Common {
    public static User currentUser;

    public static final String UPDATE ="Update";
    public static final String DELETE ="Delete";

    public static final int PICK_IMAGE_REQUEST =71;

    public static String convertCodeToStatus(String status) {
        if(status!=null) {
            if (status.equals("0"))
                return "Placed";
            else if (status.equals("1"))
                return "On My Way";
            else
                return "Delivered";
        }else
            return "status null";
    }
}

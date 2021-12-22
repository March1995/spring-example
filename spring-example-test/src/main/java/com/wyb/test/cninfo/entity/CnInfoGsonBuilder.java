package com.wyb.test.cninfo.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CnInfoGsonBuilder {

    private static final GsonBuilder INSTANCE = new GsonBuilder();

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(Announcement.class, new AnnouncementAdapter());
//        INSTANCE.registerTypeAdapter(Announcement.AnnouncementsDTO.class, new AnnouncementsDTOAdapter());
    }

    public static Gson create() {
        return INSTANCE.create();
    }
}

package com.wyb.test.cninfo.entity;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter implements JsonDeserializer<Announcement> {
    @Override
    public Announcement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Announcement announcement = new Announcement();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        announcement.setClassifiedAnnouncements(GsonHelper.getString(jsonObject, "classifiedAnnouncements"));
        announcement.setTotalSecurities(GsonHelper.getInteger(jsonObject, "totalSecurities"));
        announcement.setTotalAnnouncement(GsonHelper.getInteger(jsonObject, "totalAnnouncement"));
        announcement.setTotalRecordNum(GsonHelper.getInteger(jsonObject, "totalRecordNum"));


        if (jsonObject.has("announcements")) {
            JsonArray array = jsonObject.getAsJsonArray("announcements");
            List<Announcement.AnnouncementsDTO> announcements = new ArrayList<>();
            for (JsonElement announcementDto : array) {
                Announcement.AnnouncementsDTO dto = CnInfoGsonBuilder.create().fromJson(announcementDto, Announcement.AnnouncementsDTO.class);
//                Announcement.AnnouncementsDTO announcements = CnInfoGsonBuilder.create().fromJson(jsonObject.get("announcements"),
//                        new TypeToken<Announcement.AnnouncementsDTO>() {
//                        }.getType());
                announcements.add(dto);
            }
            announcement.setAnnouncements(announcements);
        }

        if (jsonObject.get("categoryList") != null && !jsonObject.get("categoryList").isJsonNull()) {
            announcement.setCategoryList(GsonHelper.getAsJsonArray(jsonObject.get("categoryList")));

        }
        announcement.setHasMore(GsonHelper.getBoolean(jsonObject, "hasMore"));
        announcement.setTotalpages(GsonHelper.getInteger(jsonObject, "totalpages"));
        return announcement;
    }
}

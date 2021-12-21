package com.wyb.test.cninfo.entity;

import com.google.gson.*;

import java.lang.reflect.Type;

public class AnnouncementsDTOAdapter implements JsonDeserializer<Announcement.AnnouncementsDTO> {
    @Override
    public Announcement.AnnouncementsDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Announcement.AnnouncementsDTO dto = new Announcement.AnnouncementsDTO();
        JsonObject json = jsonElement.getAsJsonObject();
        dto.setId(GsonHelper.getString(json, "id"));
        dto.setSecCode(GsonHelper.getString(json, "secCode"));
        return dto;
    }
}

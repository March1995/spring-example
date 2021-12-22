package com.wyb.test.cninfo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class Announcement {
    @JsonProperty("classifiedAnnouncements")
    private Object classifiedAnnouncements;
    @JsonProperty("totalSecurities")
    private Integer totalSecurities;
    @JsonProperty("totalAnnouncement")
    private Integer totalAnnouncement;
    @JsonProperty("totalRecordNum")
    private Integer totalRecordNum;
    @JsonProperty("announcements")
    private List<AnnouncementsDTO> announcements;
    @JsonProperty("categoryList")
    private Object categoryList;
    @JsonProperty("hasMore")
    private Boolean hasMore;
    @JsonProperty("totalpages")
    private Integer totalpages;

    public static Announcement fromJson(String jsonStr) {
        return CnInfoGsonBuilder.create().fromJson(jsonStr, Announcement.class);
    }

    public List<AnnouncementsDTO> filterAnnouncementsByDate() {
        LocalDate today = LocalDate.now();
        if (!CollectionUtils.isEmpty(announcements)) {
            announcements = announcements.stream().filter(announcementsDTO -> {
                LocalDate localDate = Instant.ofEpochMilli(announcementsDTO.getAnnouncementTime()).atZone(ZoneOffset.ofHours(8)).toLocalDate();
                return today.equals(localDate);
            }).collect(Collectors.toList());
            return announcements;
        }
        return Collections.emptyList();
    }

    @NoArgsConstructor
    @Data
    public static class AnnouncementsDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("secCode")
        private String secCode;
        @JsonProperty("secName")
        private String secName;
        @JsonProperty("orgId")
        private String orgId;
        @JsonProperty("announcementId")
        private String announcementId;
        @JsonProperty("announcementTitle")
        private String announcementTitle;
        @JsonProperty("announcementTime")
        @SerializedName("announcementTime")
        private Long announcementTime;
        @JsonProperty("adjunctUrl")
        private String adjunctUrl;
        @JsonProperty("adjunctSize")
        private Integer adjunctSize;
        @JsonProperty("adjunctType")
        private String adjunctType;
        @JsonProperty("storageTime")
        private Object storageTime;
        @JsonProperty("columnId")
        private String columnId;
        @JsonProperty("pageColumn")
        private String pageColumn;
        @JsonProperty("announcementType")
        private String announcementType;
        @JsonProperty("associateAnnouncement")
        private String associateAnnouncement;
        @JsonProperty("important")
        private String important;
        @JsonProperty("batchNum")
        private String batchNum;
        @JsonProperty("announcementContent")
        private String announcementContent;
        @JsonProperty("orgName")
        private String orgName;
        @JsonProperty("announcementTypeName")
        private String announcementTypeName;

        public static Announcement.AnnouncementsDTO fromJson(String jsonStr) {
            return CnInfoGsonBuilder.create().fromJson(jsonStr, Announcement.AnnouncementsDTO.class);
        }
    }
}

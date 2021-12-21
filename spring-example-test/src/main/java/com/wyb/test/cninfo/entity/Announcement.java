package com.wyb.test.cninfo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

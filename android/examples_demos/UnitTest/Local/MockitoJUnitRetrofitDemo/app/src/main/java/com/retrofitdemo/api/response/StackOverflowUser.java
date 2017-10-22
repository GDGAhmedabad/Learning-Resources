
package com.retrofitdemo.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StackOverflowUser {

    public class Item {

        @SerializedName("badge_counts")
        @Expose
        private BadgeCounts badgeCounts;
        @SerializedName("account_id")
        @Expose
        private Integer accountId;
        @SerializedName("is_employee")
        @Expose
        private Boolean isEmployee;
        @SerializedName("last_modified_date")
        @Expose
        private Integer lastModifiedDate;
        @SerializedName("last_access_date")
        @Expose
        private Integer lastAccessDate;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("reputation_change_year")
        @Expose
        private Integer reputationChangeYear;
        @SerializedName("reputation_change_quarter")
        @Expose
        private Integer reputationChangeQuarter;
        @SerializedName("reputation_change_month")
        @Expose
        private Integer reputationChangeMonth;
        @SerializedName("reputation_change_week")
        @Expose
        private Integer reputationChangeWeek;
        @SerializedName("reputation_change_day")
        @Expose
        private Integer reputationChangeDay;
        @SerializedName("reputation")
        @Expose
        private Integer reputation;
        @SerializedName("creation_date")
        @Expose
        private Integer creationDate;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("accept_rate")
        @Expose
        private Integer acceptRate;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("website_url")
        @Expose
        private String websiteUrl;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("display_name")
        @Expose
        private String displayName;

        /**
         * @return The badgeCounts
         */
        public BadgeCounts getBadgeCounts() {
            return badgeCounts;
        }

        /**
         * @param badgeCounts The badge_counts
         */
        public void setBadgeCounts(BadgeCounts badgeCounts) {
            this.badgeCounts = badgeCounts;
        }

        /**
         * @return The accountId
         */
        public Integer getAccountId() {
            return accountId;
        }

        /**
         * @param accountId The account_id
         */
        public void setAccountId(Integer accountId) {
            this.accountId = accountId;
        }

        /**
         * @return The isEmployee
         */
        public Boolean getIsEmployee() {
            return isEmployee;
        }

        /**
         * @param isEmployee The is_employee
         */
        public void setIsEmployee(Boolean isEmployee) {
            this.isEmployee = isEmployee;
        }

        /**
         * @return The lastModifiedDate
         */
        public Integer getLastModifiedDate() {
            return lastModifiedDate;
        }

        /**
         * @param lastModifiedDate The last_modified_date
         */
        public void setLastModifiedDate(Integer lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        /**
         * @return The lastAccessDate
         */
        public Integer getLastAccessDate() {
            return lastAccessDate;
        }

        /**
         * @param lastAccessDate The last_access_date
         */
        public void setLastAccessDate(Integer lastAccessDate) {
            this.lastAccessDate = lastAccessDate;
        }

        /**
         * @return The age
         */
        public Integer getAge() {
            return age;
        }

        /**
         * @param age The age
         */
        public void setAge(Integer age) {
            this.age = age;
        }

        /**
         * @return The reputationChangeYear
         */
        public Integer getReputationChangeYear() {
            return reputationChangeYear;
        }

        /**
         * @param reputationChangeYear The reputation_change_year
         */
        public void setReputationChangeYear(Integer reputationChangeYear) {
            this.reputationChangeYear = reputationChangeYear;
        }

        /**
         * @return The reputationChangeQuarter
         */
        public Integer getReputationChangeQuarter() {
            return reputationChangeQuarter;
        }

        /**
         * @param reputationChangeQuarter The reputation_change_quarter
         */
        public void setReputationChangeQuarter(Integer reputationChangeQuarter) {
            this.reputationChangeQuarter = reputationChangeQuarter;
        }

        /**
         * @return The reputationChangeMonth
         */
        public Integer getReputationChangeMonth() {
            return reputationChangeMonth;
        }

        /**
         * @param reputationChangeMonth The reputation_change_month
         */
        public void setReputationChangeMonth(Integer reputationChangeMonth) {
            this.reputationChangeMonth = reputationChangeMonth;
        }

        /**
         * @return The reputationChangeWeek
         */
        public Integer getReputationChangeWeek() {
            return reputationChangeWeek;
        }

        /**
         * @param reputationChangeWeek The reputation_change_week
         */
        public void setReputationChangeWeek(Integer reputationChangeWeek) {
            this.reputationChangeWeek = reputationChangeWeek;
        }

        /**
         * @return The reputationChangeDay
         */
        public Integer getReputationChangeDay() {
            return reputationChangeDay;
        }

        /**
         * @param reputationChangeDay The reputation_change_day
         */
        public void setReputationChangeDay(Integer reputationChangeDay) {
            this.reputationChangeDay = reputationChangeDay;
        }

        /**
         * @return The reputation
         */
        public Integer getReputation() {
            return reputation;
        }

        /**
         * @param reputation The reputation
         */
        public void setReputation(Integer reputation) {
            this.reputation = reputation;
        }

        /**
         * @return The creationDate
         */
        public Integer getCreationDate() {
            return creationDate;
        }

        /**
         * @param creationDate The creation_date
         */
        public void setCreationDate(Integer creationDate) {
            this.creationDate = creationDate;
        }

        /**
         * @return The userType
         */
        public String getUserType() {
            return userType;
        }

        /**
         * @param userType The user_type
         */
        public void setUserType(String userType) {
            this.userType = userType;
        }

        /**
         * @return The userId
         */
        public Integer getUserId() {
            return userId;
        }

        /**
         * @param userId The user_id
         */
        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        /**
         * @return The acceptRate
         */
        public Integer getAcceptRate() {
            return acceptRate;
        }

        /**
         * @param acceptRate The accept_rate
         */
        public void setAcceptRate(Integer acceptRate) {
            this.acceptRate = acceptRate;
        }

        /**
         * @return The location
         */
        public String getLocation() {
            return location;
        }

        /**
         * @param location The location
         */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
         * @return The websiteUrl
         */
        public String getWebsiteUrl() {
            return websiteUrl;
        }

        /**
         * @param websiteUrl The website_url
         */
        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        /**
         * @return The link
         */
        public String getLink() {
            return link;
        }

        /**
         * @param link The link
         */
        public void setLink(String link) {
            this.link = link;
        }

        /**
         * @return The profileImage
         */
        public String getProfileImage() {
            return profileImage;
        }

        /**
         * @param profileImage The profile_image
         */
        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        /**
         * @return The displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * @param displayName The display_name
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

    }

    public class BadgeCounts {

        @SerializedName("bronze")
        @Expose
        private Integer bronze;
        @SerializedName("silver")
        @Expose
        private Integer silver;
        @SerializedName("gold")
        @Expose
        private Integer gold;

        /**
         * @return The bronze
         */
        public Integer getBronze() {
            return bronze;
        }

        /**
         * @param bronze The bronze
         */
        public void setBronze(Integer bronze) {
            this.bronze = bronze;
        }

        /**
         * @return The silver
         */
        public Integer getSilver() {
            return silver;
        }

        /**
         * @param silver The silver
         */
        public void setSilver(Integer silver) {
            this.silver = silver;
        }

        /**
         * @return The gold
         */
        public Integer getGold() {
            return gold;
        }

        /**
         * @param gold The gold
         */
        public void setGold(Integer gold) {
            this.gold = gold;
        }

    }

    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("quota_max")
    @Expose
    private Integer quotaMax;
    @SerializedName("quota_remaining")
    @Expose
    private Integer quotaRemaining;

    /**
     * @return The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return The hasMore
     */
    public Boolean getHasMore() {
        return hasMore;
    }

    /**
     * @param hasMore The has_more
     */
    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * @return The quotaMax
     */
    public Integer getQuotaMax() {
        return quotaMax;
    }

    /**
     * @param quotaMax The quota_max
     */
    public void setQuotaMax(Integer quotaMax) {
        this.quotaMax = quotaMax;
    }

    /**
     * @return The quotaRemaining
     */
    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

    /**
     * @param quotaRemaining The quota_remaining
     */
    public void setQuotaRemaining(Integer quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }

}

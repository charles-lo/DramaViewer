package com.charles.dramalist.api.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Datum implements Serializable {
    @Id(assignable = true)
    @SerializedName("drama_id")
    @Expose
    private Long dramaId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("total_views")
    @Expose
    private Integer totalViews;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("rating")
    @Expose
    private Double rating;
    private final static long serialVersionUID = -4523837389539476916L;

    public Long getDramaId() {
        return dramaId;
    }

    public void setDramaId(Long dramaId) {
        this.dramaId = dramaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
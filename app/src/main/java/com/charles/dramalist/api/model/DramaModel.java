package com.charles.dramalist.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class DramaModel {
    @Id(assignable = true)
    @SerializedName("id")
    @Expose
    private Long id = 1L;

    @SerializedName("data")
    @Expose
    List<Datum> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}

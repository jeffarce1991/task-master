package com.example.taskmaster.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class TaskDto(
    /*
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "status")
    var status: Int,

    @ColumnInfo(name = "date_created")
    val dateCreated: Long,

    @ColumnInfo(name = "date_completed")
    var dateCompleted: Long
    */
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("status")
    val status: Int,
    @Expose
    @SerializedName("date_created")
    val dateCreated: Long,
    @Expose
    @SerializedName("date_completed")
    val dateCompleted: Long
) {
    constructor(): this(-1, "", "", 0, 0, 0)
}

data class AddressDto(
    @Expose
    @SerializedName("city")
    val city: String,
    @Expose
    @SerializedName("street")
    val street: String,
    @Expose
    @SerializedName("suite")
    val suite: String,
    @Expose
    @SerializedName("zipcode")
    val zipcode: String,
    @Expose
    @SerializedName("geo")
    val geo: GeoDto
) {
    constructor(): this("", "", "", "",GeoDto())
}

data class CompanyDto(
    @Expose
    @SerializedName("bs")
    val bs: String,
    @Expose
    @SerializedName("catchPhrase")
    val catchPhrase: String,
    @Expose
    @SerializedName("name")
    val name: String
) {
    constructor(): this("", "", "")
}

data class GeoDto(
    @Expose
    @SerializedName("lat")
    val lat: String,
    @Expose
    @SerializedName("lng")
    val lng: String
) {
    constructor(): this("", "")
}


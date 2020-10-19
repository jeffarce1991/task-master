package com.example.taskmaster.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
class Task(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "status")
    var status: Int? = 0,

    @ColumnInfo(name = "date_created")
    val dateCreated: Long,

    @ColumnInfo(name = "date_completed")
    var dateCompleted: Long? = -1


) {
    constructor(): this(null, "", "", null, 0, null)
}

data class Address(
    val city: String,
    val street: String,
    val suite: String,
    val zipcode: String,
    val geo: Geo
) {
    constructor(): this("", "", "", "", Geo())
}

data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
) {
    constructor(): this("", "", "")
}

data class Geo(
    val lat: String,
    val lng: String
) {
    constructor(): this("", "")
}

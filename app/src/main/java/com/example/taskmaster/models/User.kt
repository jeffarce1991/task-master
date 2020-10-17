package com.example.taskmaster.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "address")
    val address: Address,

    @ColumnInfo(name = "company")
    val company: Company,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "website")
    val website: String
) {
    constructor(): this(null, Address(), Company(), "sample@gmail.com", "Squall Leonheart", "","",    "")
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

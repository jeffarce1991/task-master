package com.example.taskmaster.room.converter

import androidx.room.TypeConverter
import com.example.taskmaster.models.Address
import com.example.taskmaster.models.Company
import com.example.taskmaster.models.Geo
import com.example.taskmaster.utils.ConverterUtil.deserialise
import com.example.taskmaster.utils.ConverterUtil.serialise

public class UserConverter {
    private fun UserConverter() {}

    @TypeConverter
    fun fromAddress(address: Address): String {
        return serialise(address)
    }

    @TypeConverter
    fun toAddress(serialised: String?): Address {
        return deserialise(
            serialised!!,
            Address::class.java
        )
    }

    @TypeConverter
    fun fromCompany(company: Company): String {
        return serialise(company)
    }

    @TypeConverter
    fun toCompany(serialised: String?): Company {
        return deserialise(
            serialised!!,
            Company::class.java
        )
    }
    @TypeConverter
    fun fromGeo(geo: Geo): String {
        return serialise(geo)
    }

    @TypeConverter
    fun toGeo(serialised: String?): Geo {
        return deserialise(
            serialised!!,
            Geo::class.java
        )
    }
}
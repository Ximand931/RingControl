package com.happs.ximand.ringcontrol.model.`object`

import java.util.*

class TrustedDevice(var id: Int, val manufacturer: String, val model: String, val date: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as TrustedDevice
        return id == that.id &&
                manufacturer == that.manufacturer &&
                model == that.model && date == that.date
    }

    override fun hashCode(): Int {
        return Objects.hash(id, manufacturer, model, date)
    }

    override fun toString(): String {
        return "TrustedDevice{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", date='" + date + '\'' +
                '}'
    }

}
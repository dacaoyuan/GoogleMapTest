package com.pactera.googlemaptest.utils

import android.text.TextUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.Place

object StringUtil {
    /**
     * 字符串 southWest（西南） : lat,lng
     * 字符串 northEast（东北） : lat,lng
     */
    fun convertToLatLngBounds(southWest: String?, northEast: String?): LatLngBounds? {
        val soundWestLatLng = convertToLatLng(southWest)
        val northEastLatLng = convertToLatLng(northEast)
        return if (soundWestLatLng == null || northEast == null) {
            null
        } else {
            LatLngBounds(soundWestLatLng, northEastLatLng)
        }

    }

    fun convertToLatLng(value: String?): LatLng? {
        if (TextUtils.isEmpty(value)) {
            return null
        }
        val split = value!!.split(",").dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (split.size != 2) {
            null
        } else try {
            LatLng(split[0].toDouble(), split[1].toDouble())
        } catch (e: NullPointerException) {
            null
        } catch (e: NumberFormatException) {
            null
        }
    }
    private const val FIELD_SEPARATOR = "\n\t"
    private const val RESULT_SEPARATOR = "\n---\n\t"

    fun stringifyAutocompleteWidget(place: Place, raw: Boolean): String {
        val builder = StringBuilder()
        builder.append("Autocomplete Widget Result:").append(RESULT_SEPARATOR)
        if (raw) {
            builder.append(place)
        } else {
            builder.append(stringify(place))
        }
        return builder.toString()
    }

    fun stringify(place: Place): String {
        return (place.name
                + " ("
                + place.address
                + ")"
                + " is open now? "
                + place.isOpen(System.currentTimeMillis()))
    }
}
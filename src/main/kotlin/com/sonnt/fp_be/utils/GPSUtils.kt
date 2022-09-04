package com.sonnt.fp_be.utils

import com.sonnt.fp_be.features.shared.model.Cord

class GPSUtils {

    companion object {
        fun distance(cord1: Cord, cord2: Cord): Double {
            return distance(cord1.lat, cord2.lat, cord1.long, cord2.long)
        }

        fun distance(
            lat1: Double, lat2: Double, lon1: Double,
            lon2: Double
        ): Double {
            val R = 6371 // Radius of the earth
            val latDistance = Math.toRadians(lat2 - lat1)
            val lonDistance = Math.toRadians(lon2 - lon1)
            val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)))
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            var distance = R * c * 1000 // convert to meters

            distance = Math.pow(distance, 2.0)
            return Math.sqrt(distance)
        }
    }
}
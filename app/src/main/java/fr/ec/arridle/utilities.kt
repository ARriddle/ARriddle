package fr.ec.arridle

import android.annotation.SuppressLint
import fr.ec.arridle.network.UserProperty
import java.text.SimpleDateFormat
import java.util.*


fun findUserById(list: List<UserProperty>, userId: Int?): UserProperty? {
    return list.find { it.id == userId }
}

fun findRankById(list: List<UserProperty>, user: UserProperty?): Int {
    return list.count { it.points > (user?.points ?: 10) } + 1
}

@SuppressLint("SimpleDateFormat")
fun stringToEpochtime(date: String): Int? {
    return if (validateFormatDate(date)) {
        val formatter = SimpleDateFormat("dd-MM-yyyy:HH:mm:ss")
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val gmt: Date =
            formatter.parse(date)
        val millisecondsSinceEpoch0: Long = gmt.time
        (millisecondsSinceEpoch0 / 1000).toInt()
    } else {
        null
    }
}

fun epochTimeToString(time: Int): String {
    return Date(time*1000.toLong()).toString()
}

fun validateFormatHour(hour: String): Boolean {
    return hour.length == 8 &&
            hour[2] == ':' && hour[5] == ':' &&
            hour[0].isDigit() && hour[1].isDigit() && hour[3].isDigit() &&
            hour[4].isDigit() && hour[6].isDigit() && hour[7].isDigit() &&
            (hour[0].toString() + hour[1].toString()).toInt() < 24 &&
            (hour[3].toString() + hour[4].toString()).toInt() < 60 &&
            (hour[6].toString() + hour[7].toString()).toInt() < 60
}


fun validateFormatDate(date: String): Boolean {
    return date.length == 19 &&
            date[2] == '-' && date[5] == '-' &&
            date[10] == ':' && date[13] == ':' && date[16] == ':' &&
            date[0].isDigit() && date[1].isDigit() && date[3].isDigit() &&
            date[4].isDigit() && date[6].isDigit() && date[7].isDigit() &&
            date[8].isDigit() && date[9].isDigit() && date[11].isDigit() &&
            date[12].isDigit() && date[14].isDigit() && date[15].isDigit() &&
            date[17].isDigit() && date[18].isDigit() &&
            ((date[0].toString() + date[1].toString()).toInt() < 32) && ((date[0].toString() + date[1].toString()).toInt() > 0) &&
            ((date[3].toString() + date[4].toString()).toInt() < 12) && ((date[3].toString() + date[4].toString()).toInt() > 0) &&
            ((date[6].toString() + date[7].toString() + date[8].toString() + date[9].toString()).toInt() > 1900) &&
            ((date[11].toString() + date[12].toString()).toInt() >= 0) && ((date[11].toString() + date[12].toString()).toInt() < 24) &&
            ((date[14].toString() + date[15].toString()).toInt() >= 0) && ((date[14].toString() + date[15].toString()).toInt() < 60) &&
            ((date[17].toString() + date[18].toString()).toInt() >= 0) && ((date[17].toString() + date[18].toString()).toInt() < 60)
    // burn in hell for that
}


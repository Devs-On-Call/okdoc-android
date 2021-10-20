package com.devsoncall.okdoc.utils

import java.text.DateFormatSymbols
import java.util.*

fun formatDateString(date: String?, dayOfWeek: String?): String {

    var dateString = ""

    // Day of week string
    dateString += DateFormatSymbols().weekdays[dayOfWeek?.toInt()!!]


    // date has yyyy-mm-dd format
    // split to access year month and day separately
    val split: List<String>? = date?.split("-")

    // Day of month
    dateString += if (split?.get(2)?.startsWith("0") == true) {
        ", " + split[2].drop(1)
    } else {
        ", " + split?.get(2)
    }

    // Month string
    dateString += " " + DateFormatSymbols().months[split?.get(1)?.toInt()?.minus(1)!!]

    // Year
    dateString += " " + split[0]

    return dateString
}
// finds the day of week for a string of type YYYY-MM-DD
fun getDayOfWeek(date: String): Int {

    val split: List<String> = date.split("-")

    val day = Calendar.getInstance()
    day.set(Calendar.YEAR, split[0].toInt())
    day.set(Calendar.MONTH, split[1].toInt() - 1)
    day.set(Calendar.DAY_OF_MONTH, split[2].toInt())

    return day.get(Calendar.DAY_OF_WEEK)
}
package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*


const val SECOND = 1000L
const val MINUTE = SECOND * 60
const val HOUR = MINUTE * 60
const val DAY = HOUR * 24

fun Date.format(pattern: String="HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits) : Date{
    var time = this.time

    time += when (units){
       TimeUnits.SECOND -> value * SECOND
       TimeUnits.MINUTE -> value * MINUTE
       TimeUnits.HOUR -> value * HOUR
       TimeUnits.DAY -> value * DAY
    }

    this.time = time

    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = (Date().time - date.time) / 1000
    val mins: Int = (diff/60).toInt()
    val hours: Int = ((diff/60)/60).toInt()
    val days: Int = ((((diff/60)/60)/24).toInt())

    return when(diff){
        in 0..1 -> "только что"
        in 1..45 -> "несколько секунд назад"
        in 45..75 -> "минуту назад"
        in 75..2700 -> "${diff/60} ${TimeUnits.MINUTE.plural(mins)} назад"
        in 2700..4500 -> "час назад"
        in 4500..79200 -> "${(diff/60)/60} ${TimeUnits.HOUR.plural(hours)} часов назад"
        in 79200..93600 -> "день назад"
        in 93600..31104000 -> "${((diff/60)/60)/24} ${TimeUnits.DAY.plural(days)} дней назад"
        in 31104000..Long.MAX_VALUE -> "более года назад"
        else -> {
           "hz"
        }
    }

}




enum class TimeUnits{
    SECOND{
        override fun plural(value: Int): String {
           if (value in 5..20){
               return "$value секунд"
           }
            return when(value % 10){
                1 -> "$value секунду"
                2,3,4 -> "$value секунды"
                else -> "$value секунд"
            }
        }
    },
    MINUTE{
        override fun plural(value: Int): String {
            if (value in 5..20){
                return "$value минут"
            }
            return when(value % 10){
                1 -> "$value минуту"
                2,3,4 -> "$value минуты"
                else -> "$value минут"
            }
        }
    },
    HOUR{
        override fun plural(value: Int): String {
            if (value in 5..20){
                return "$value часов"
            }
            return when(value % 10){
                1 -> "$value час"
                2,3,4 -> "$value часа"
                else -> "$value часов"
            }
        }
    },
    DAY{
        override fun plural(value: Int): String {
            if (value in 5..20){
                return "$value дней"
            }
            return when(value % 10){
                1 -> "$value день"
                2,3,4 -> "$value дня"
                else -> "$value дней"
            }
        }
    };


abstract fun plural(value: Int) : String
}


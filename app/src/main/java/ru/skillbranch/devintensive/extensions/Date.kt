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
    val diff = (date.time - this.time) / 1000
    val mins: Int = (diff/60).toInt()
    val hours: Int = ((diff/60)/60).toInt()
    val days: Int = ((((diff/60)/60)/24).toInt())

    return when(diff){
        in 0..1 -> "только что"
        in 1..45 -> "несколько секунд назад"
        in 45..75 -> "минуту назад"
        in 75..2700 -> " ${TimeUnits.MINUTE.plural(mins)} назад"
        in 2700..4500 -> "час назад"
        in 4500..79200 -> "${TimeUnits.HOUR.plural(hours)} назад"
        in 79200..93600 -> "день назад"
        in 93600..31104000 -> "${TimeUnits.DAY.plural(days)} назад"
        in 31104000..Long.MAX_VALUE -> "более года назад"
        in -1..0 -> "только что"
        in -45..-1 -> "через несколько секунд"
        in -75..-45 -> "через минуту"
        in -2700..-45 -> "через ${TimeUnits.MINUTE.plural(mins)}"
        in -4500..-2700 -> "через час"
        in -79200..-4500 -> "через ${TimeUnits.HOUR.plural(hours)}"
        in -93600..-79200 -> "через день"
        in -31104000..-93600 -> "через ${TimeUnits.DAY.plural(days)}"
        in  Long.MIN_VALUE..-31104000 -> "более чем через год"


        else -> {
           "error value"
        }
    }

}




enum class TimeUnits{
    SECOND{
        override fun plural(value: Int): String {
         var resignedValue: Int = value
          if (value < 0){
              resignedValue = value * -1
          }
           if (resignedValue in 5..20){
               return "$resignedValue секунд"
           }
            return when(resignedValue % 10){
                1 -> "$resignedValue секунду"
                2,3,4 -> "$resignedValue секунды"
                else -> "$resignedValue секунд"
            }
        }
    },
    MINUTE{
        override fun plural(value: Int): String {
            var resignedValue: Int = value
            if (value < 0){
                resignedValue = value * -1
            }
            if (resignedValue in 5..20){
                return "$resignedValue минут"
            }
            return when(resignedValue % 10){
                1 -> "$resignedValue минуту"
                2,3,4 -> "$resignedValue минуты"
                else -> "$resignedValue минут"
            }
        }
    },
    HOUR{
        override fun plural(value: Int): String {
            var resignedValue: Int = value
            if (value < 0){
                resignedValue = value * -1
            }
            if (resignedValue in 5..20){
                return "$resignedValue часов"
            }
            return when(resignedValue % 10){
                1 -> "$resignedValue час"
                2,3,4 -> "$resignedValue часа"
                else -> "$resignedValue часов"
            }
        }
    },
    DAY{
        override fun plural(value: Int): String {
            var resignedValue: Int = value
            if (value < 0){
                resignedValue = value * -1
            }
            if (resignedValue in 5..20){
                return "$resignedValue дней"
            }
            return when(resignedValue % 10){
                1 -> "$resignedValue день"
                2,3,4 -> "$resignedValue дня"
                else -> "$resignedValue дней"
            }
        }
    };


abstract fun plural(value: Int) : String
}


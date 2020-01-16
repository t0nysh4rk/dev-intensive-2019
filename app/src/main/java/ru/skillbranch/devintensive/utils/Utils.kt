package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder

object Utils {
   fun parseFullName(fullName: String?) : Pair<String?, String?>{

       val splittedName : List<String>? = fullName?.split(" ")
       val  firstName = splittedName?.getOrNull(0)
       val  lastName = splittedName?.getOrNull(1)
       if (firstName.isNullOrEmpty() && lastName.isNullOrEmpty()){
           return Pair(null, null)
       }
       else if (firstName.isNullOrEmpty()){
           return Pair(null, lastName)
       }
      else if (lastName.isNullOrEmpty()){
           return Pair(firstName, null)
       }
   return Pair(firstName, lastName)
   }

    fun transliteration(payLoad: String, divider: String = " "): String {
        val hashmap = hashMapOf<String, String>("А" to "A",
            "а" to "a", "Б" to "B", "б" to "b", "В" to "V", "в" to "v",
            "Г" to "G", "г" to "g", "Д" to "D", "д" to "d", "Е" to "E",
            "е" to "e", "Ё" to "E", "ё" to "e", "Ж" to "Zh", "ж" to "zh",
            "З" to "Z", "з" to "z", "И" to "I", "и" to "i", "Й" to "I",
            "й" to "i", "К" to "K", "к" to "k", "Л" to "L", "л" to "l",
            "М" to "M", "м" to "m", "Н" to "N", "н" to "n", "О" to "O",
            "о" to "o", "П" to "P", "п" to "p", "Р" to "R", "р" to "r",
            "С" to "S", "с" to "s", "Т" to "T", "т" to "t", "У" to "U",
            "у" to "u", "Ф" to "F", "ф" to "f", "Х" to "H", "х" to "h",
            "Ц" to "C", "ц" to "c", "Ч" to "Ch", "ч" to "ch", "Щ" to "Sh'",
            "щ" to "sh'","Ш" to "Sh","ш" to "sh",  "Ы" to "I", "ы" to "i",
            "Э" to "E", "э" to "e","Ю" to "Yu", "ю" to "yu", "Я" to "Ya",
            "я" to "ya", "Ъ" to "","ъ" to "", "Ь" to "", "ь" to "", " " to divider)

        val result = StringBuilder(payLoad.length)
        for(i in payLoad.toCharArray()){
            if (hashmap.containsKey(i.toString()))
               result.append(hashmap.getValue(i.toString()))
            else result.append(i)
        }

      return result.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
       var result = StringBuilder()

        if ((firstName.isNullOrBlank() || firstName.isEmpty()) && ((lastName.isNullOrBlank() || lastName.isEmpty()))){
            return null
        }

    if (!firstName.isNullOrEmpty()){
       if (!firstName.isBlank()){
           result.append(firstName.take(1).toUpperCase())
       }
    }

        if (!lastName.isNullOrEmpty()){
            if (!lastName.isBlank()){
                result.append(lastName.take(1).toUpperCase())
            }
        }
    var b : Boolean? = null


        return result.toString()
    }
}


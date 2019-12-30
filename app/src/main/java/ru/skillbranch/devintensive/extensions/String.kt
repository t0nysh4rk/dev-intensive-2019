package ru.skillbranch.devintensive.extensions

fun String.truncate(numToTrim: Int = 16) : String {
    if (numToTrim > this.length)
        return this



    val charArr = this.toCharArray()
    var isCharDeleted = false
    var counter = 0
    val sb = StringBuilder()
    for (char in charArr){
        if (counter < numToTrim){
            sb.append(char)
            counter++
        } else {
            if (char != ' '){
                isCharDeleted = true
                break
            }

        }

    }
    return if (!isCharDeleted)
        sb.toString().trimEnd()
    else {
        return sb.toString().trim().plus("...")
    }

}

fun String.stripHtml() : String {

   val regex  = this.replace(Regex("<(A-Z|a-z|\\\\s{2,}|\\\"|\\\\&|\\\\?|\\\\'|\\\\/)[^>]{0,}>"), "")

   val reg2 = regex.replace(Regex("(\\s){2,}"), " ")

    return reg2


}


package ru.skillbranch.devintensive.models

class UserView (
    val id: String,
    val fullName: String,
    val nickName: String,
    val avatar: String? = null,
    val initials: String?,
    val status: String? = "offline"
){

    fun printMe() {
println("""
id: $id
fullName: $fullName
nickName: $nickName
avatar: $avatar
initials: $initials
status: $status
""".trimIndent())
}

}
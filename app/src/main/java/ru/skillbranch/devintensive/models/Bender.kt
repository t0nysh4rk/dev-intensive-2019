package ru.skillbranch.devintensive.models

class Bender (var status: Status = Status.NORMAL, var question: Question = Question.NAME){
    var tryCount = 0

    fun askQuestion(): String = when (question){
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>> {
        val validatedAnswer = validateAnswer(answer)
        if (validatedAnswer.isEmpty()||validatedAnswer.isBlank()) {
            return if (question.answers.contains(answer)) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {

                when (tryCount) {
                    in 0..2 -> {
                        tryCount++
                        status = status.nextStatus()
                        "Это не правильный ответ!\n${question.question}" to status.color
                    }
                    else -> {
                        tryCount = 0
                        status = Status.NORMAL
                        question = Question.NAME
                        "Это не правильный ответ. Давай все по новой\n${question.question}" to status.color


                    }
                }


            }

        } else {
            return "$validatedAnswer \n${question.question}" to status.color
        }
    }

    fun validateAnswer(answer: String) : String{

        return when(question) {
            Question.NAME -> {
                return if (answer.toCharArray()[0].isUpperCase())
                    ""
                else "Имя должно начинаться с заглавной буквы"
            }
            Question.PROFESSION -> {
                return if (!answer.toCharArray()[0].isUpperCase())
                    ""
                else
                    "Профессия должна начинаться со строчной буквы"
            }

            Question.MATERIAL -> {
                return if (!answer.contains("\\d".toRegex()))
                    ""
                else
               "Материал не должен содержать цифр"
        }
            Question.BDAY -> {
                return if (!answer.contains("\\D".toRegex()))
                    ""
                else
                    "Год моего рождения должен содержать только цифры"
            }
            Question.SERIAL -> {
                return if (!answer.contains("\\D".toRegex()) && answer.length == 7)
                    ""
                else
                    "Серийный номер содержит только цифры, и их 7"
            }

            else -> return ""
         }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)) ,
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0)) ;

        fun nextStatus(): Status{
            return if (this.ordinal < values().lastIndex){
                values()[this.ordinal + 1]
            }
            else{
                values()[0]
            }
        }
    }
    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION

        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")){
            override fun nextQuestion(): Question = MATERIAL

        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood"))
        {
            override fun nextQuestion(): Question = BDAY

        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL

        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE

        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE

        };


        abstract fun nextQuestion():Question
    }
}
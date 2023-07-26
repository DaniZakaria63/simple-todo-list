package com.daniza.simple.todolist.data

object DummyData {
    val nameSentence = "Kyle Valles\n" +
            "Ashanti Joiner\n" +
            "Rosalie Harmon\n" +
            "Allen Bautista\n" +
            "Kole Parrish\n" +
            "Andres Hundley\n" +
            "Darryl Vargas\n" +
            "Theron Church\n" +
            "Edna Isbell\n" +
            "Erin Laster\n" +
            "Joseluis Sheridan\n" +
            "Jazmin Valencia\n" +
            "Raymundo Gant\n" +
            "Alfred Kaplan\n" +
            "Alexandrea Maxey\n" +
            "Julisa Combs\n" +
            "Annalee Daley\n" +
            "Loren Keck\n" +
            "Tiffani Maxey\n" +
            "Wendy Ortega\n" +
            "Cooper Ahrens\n" +
            "Sincere Costello\n" +
            "Amya Vo\n" +
            "Adam McFadden\n" +
            "Andreas Law\n" +
            "Sommer Dodds\n" +
            "Denis Dodson\n" +
            "Elyssa Neumann\n" +
            "Jack Greenlee\n" +
            "Keara Lemke\n" +
            "Cory Clark\n" +
            "Coby Willis\n" +
            "Jimmie Bedford\n" +
            "Aden Salmon\n" +
            "Aniya Crawford\n" +
            "Caley Kaur\n" +
            "Armani Enriquez\n" +
            "Dorothy Linares\n" +
            "Lexus Alcantar\n" +
            "Katarina Neely\n" +
            "Babygirl Diaz\n" +
            "Juana Dougherty\n" +
            "Arielle Dominguez\n" +
            "Dayanara Shaw\n" +
            "Julie Way\n" +
            "Lindsay Oglesby\n" +
            "Claire Beckwith\n" +
            "Dajah McCord\n" +
            "Giovanna Lanier\n" +
            "Kristyn Tice"
    val randomNameList: Array<String> = nameSentence.split("\n").toTypedArray()
    fun randomName(): String = randomNameList.random()
    fun randomNameLong(): String = randomName() + randomName() + randomName()
}
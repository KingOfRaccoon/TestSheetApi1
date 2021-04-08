package com.example.testsheetapi

fun main(){
    convertFromDataClass(Message("qwer", "qwert", "dagfb"))
    convertFromJson("{sender=Кто, text=Я, recipient=Кто я}")
}

fun convertFromDataClass(any: Any): Pair<MutableList<String>, MutableList<String>> {
    val mes = any.toString()
//    val message = "Message(sender=Кто, text=Я, recipient=Кто я)"
//    val mes = "Trainer(second_name=Звездаков, date=23-10-2003, sex=Мужской, groupID=CuuQJ30TctLCxENOv0hs, type=trainer, img=https://firebasestorage.googleapis.com/v0/b/balamut-4af92.appspot.com/o/images%2F86?alt=media&token=1e599eba-6d8e-47f8-9073-90fa41e3d3f7, first_name=Александрдрдрдр)"

    var str = ""
    var first = 0
    for (it in mes.indices) {
        if (mes[it] == '(') {
            first = it
            break
        }
    }
    for (m in first+1 until mes.lastIndex) {
        str += mes[m]
    }
    val list = str.split(',')
    val fixedList = mutableListOf<String>()
    val listName = mutableListOf<String>()
    list.forEach {
        val array = it.split('=')
        listName.add(array[0])
        fixedList.add(array[1])
    }
    println(listName)
    println(fixedList)
    return listName to fixedList
}

fun convertFromJson(string: String): Pair<MutableList<String>, MutableList<String>> {
//    val mes ="{second_name=Звездаков, date=23-10-2003, sex=Мужской, groupID=CuuQJ30TctLCxENOv0hs, type=trainer, img=https://firebasestorage.googleapis.com/v0/b/balamut-4af92.appspot.com/o/images%2F86?alt=media&token=1e599eba-6d8e-47f8-9073-90fa41e3d3f7, first_name=Александрдрдрдр}"
    val mes = string
    var str = ""
    var first = 0
    for (it in mes.indices) {
        if (mes[it] == '{') {
            first = it
            break
        }
    }
    for (m in first+1 until mes.lastIndex) {
        str += mes[m]
    }
    val list = str.split(',')
    val listName = mutableListOf<String>()
    val fixedList = mutableListOf<String>()
    list.forEach {
        val array = it.split('=')
        listName.add(array[0])
        fixedList.add(array[1])
    }
    println(listName)
    println(fixedList)
    return listName to fixedList
}
package com.jstudio.jkit

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

/**
 * Created by Jason
 */
val instance: Gson = Gson()

@Throws(JsonSyntaxException::class)
inline fun <reified T> parseToObject(json: String): T = instance.fromJson<T>(json, T::class.java)

@Throws(JsonSyntaxException::class)
inline fun <reified T> parseToObject(element: JsonElement): T = instance.fromJson(element, T::class.java)

@Throws(JsonSyntaxException::class)
inline fun <reified T> parseToJson(obj: Any) = instance.toJson(obj)

@Throws(JsonSyntaxException::class)
inline fun <reified T> parseToList(json: String): ArrayList<T> = parseToList(JsonParser.parseString(json))

@Throws(JsonSyntaxException::class)
inline fun <reified T> parseToList(element: JsonElement): ArrayList<T> {
    val array = element.asJsonArray
    val list: ArrayList<T> = ArrayList()
    array.forEach { list.add(parseToObject(it)) }
    return list
}


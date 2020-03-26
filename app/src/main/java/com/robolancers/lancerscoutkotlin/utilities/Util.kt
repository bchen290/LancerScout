package com.robolancers.lancerscoutkotlin.utilities

import android.util.Log
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.robolancers.lancerscoutkotlin.models.template.*

class Util {
    companion object {
        val gson = GsonBuilder()
            .registerTypeAdapter<MutableList<TemplateModel>> {
                serialize { arg ->
                    val jsonArray = JsonArray()

                    arg.src.forEach {
                        val jsonObject = JsonObject()

                        jsonObject.addProperty("type", it.type)

                        when (it) {
                            is Header -> {
                                jsonObject.addProperty("title", it.text)
                            }
                            is Checkbox -> {
                                jsonObject.addProperty("title", it.title)
                                jsonObject.addProperty("checkedState", it.checkedState)
                            }
                            is Counter -> {
                                jsonObject.addProperty("title", it.title)
                                jsonObject.addProperty("count", it.count)
                                jsonObject.addProperty("unit", it.unit)
                            }
                            is ItemSelector -> {
                                jsonObject.addProperty("title", it.title)
                                jsonObject.addProperty("list", Gson().toJson(it.list))
                            }
                            is Note -> {
                                jsonObject.addProperty("title", it.title)
                                jsonObject.addProperty("text", it.text)
                            }
                            is Stopwatch -> {
                                jsonObject.addProperty("title", it.title)
                                jsonObject.addProperty("time", it.time)
                            }
                        }

                        jsonArray.add(jsonObject)
                    }

                    jsonArray
                }

                deserialize { arg ->
                    val jsonArray = arg.json.asJsonArray
                    val result = mutableListOf<TemplateModel>()

                    jsonArray.forEach {jsonObject ->
                        result.add(when (jsonObject["type"].asString) {
                            "Header" -> {
                                Header(
                                    jsonObject["title"].asString
                                )
                            }
                            "Checkbox" -> {
                                Checkbox(
                                    jsonObject["title"].asString,
                                    jsonObject["checkedState"].asBoolean
                                )
                            }
                            "Counter" -> {
                                Counter(
                                    jsonObject["title"].asString,
                                    jsonObject["count"].asInt,
                                    jsonObject["unit"].asString
                                )
                            }
                            "ItemSelector" -> {
                                val list = jsonObject["list"].asJsonArray.toMutableList().map {
                                    ItemSelectorItem(it["itemName"].asString)
                                }.toMutableList()

                                ItemSelector(
                                    jsonObject["title"].asString,
                                    list
                                )
                            }
                            "Note" -> {
                                Note(
                                    jsonObject["title"].asString,
                                    jsonObject["text"].asString
                                )
                            }
                            "Stopwatch" -> {
                                Stopwatch(
                                    jsonObject["title"].asString,
                                    jsonObject["time"].asString
                                )
                            }

                            else -> TemplateModel("")
                        })
                    }

                    result
                }
            }
            .create()
    }
}
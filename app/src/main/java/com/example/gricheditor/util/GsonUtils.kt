package com.example.gricheditor.util


import android.util.Log
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.math.BigDecimal

/**
 * author：G
 * time：2021/4/28 12:14
 * about：
 **/
object GsonUtils {
    val instance = Gson()
//    val instance = GsonBuilder().registerTypeAdapterFactory(MyTypeAdapterFactory<Any?>()).create()

    fun toJson(o: Any?): String {
        return try {
            instance.toJson(o)
        } catch (e: Exception) {
            Log.e("CacheListUtil->","setList-->$e")
            ""
        }
    }

    fun <T> fromJson(jsonStr: String?, clazz: Class<T>?): T? {
        return try {
            instance.fromJson(jsonStr, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun <T> fromJsonArray(jsonStr: String?, clazz: Class<T>?): List<T>? {
        return try {
            val parser = JsonParser()
            val tradeElement: JsonElement = parser.parse(jsonStr)
            val jsonArray = tradeElement.asJsonArray
            var list = ArrayList<T>();
            for (jsonElement in jsonArray) {
                list.add(instance.fromJson(jsonElement, clazz)); //cls
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


class FloatlNullAdapter : TypeAdapter<Float?>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader): Float {
        if (reader.peek() === JsonToken.STRING) {
            reader.skipValue() //跳过当前
            return 0.00f
        }
        val bigDecimal = BigDecimal(reader.nextString())
        return bigDecimal.toFloat()
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: Float?) {
        writer.value(value)
    }
}

class IntNullAdapter : TypeAdapter<Number?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Number?) {
        out.value(value)
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Number? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        return try {
            reader.nextInt()
        } catch (e: NumberFormatException) {
            reader.nextString()
            0
        }
    }
}

class MyTypeAdapterFactory<T> : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        if (rawType == Float::class.java || rawType == Float::class.javaPrimitiveType) {
            return FloatlNullAdapter() as TypeAdapter<T>
        } else if (rawType == Int::class.java || rawType == Int::class.javaPrimitiveType) {
            return IntNullAdapter() as TypeAdapter<T>
        }
        return null
    }
}
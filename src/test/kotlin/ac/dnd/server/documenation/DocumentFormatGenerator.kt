package ac.dnd.server.documenation

import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Attributes.key

object DocumentFormatGenerator {

    fun required(): Attributes.Attribute {
        return key("required").value("true")
    }

    fun optional(): Attributes.Attribute {
        return key("required").value("false")
    }

    fun customFormat(format: String): Attributes.Attribute {
        return key("format").value(format)
    }

    fun emptyFormat(): Attributes.Attribute {
        return customFormat("")
    }

    fun dateTimeFormat(): Attributes.Attribute {
        return key("format").value("yyyy-MM-dd HH:mm:ss")
    }

    fun dateFormat(): Attributes.Attribute {
        return key("format").value("yyyy-MM-dd")
    }

    fun timeFormat(): Attributes.Attribute {
        return key("format").value("HH:mm:ss")
    }

    fun <E : Enum<E>> generatedEnums(enumClass: Class<E>): String {
        return enumClass.enumConstants
            .joinToString("\n") { "* `${it.name}`" }
    }

    fun <E : Enum<E>> generateEnumAttrs(
        enumClass: Class<E>,
        detailFun: (E) -> String
    ): Attributes.Attribute {
        val value = enumClass.enumConstants
            .joinToString("\n") { "* `${it.name}`(${detailFun(it)})" }
        return key("format").value(value)
    }

    fun <E : Enum<E>> generateEnumListFormatAttribute(
        enumClassList: List<E>,
        detailFun: (E) -> String
    ): Attributes.Attribute {
        val value = enumClassList
            .joinToString("\n") { "* `${it.name}`(${detailFun(it)})" }
        return key("format").value(value)
    }
}
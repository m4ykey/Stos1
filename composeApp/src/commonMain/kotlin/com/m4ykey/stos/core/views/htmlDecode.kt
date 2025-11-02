package com.m4ykey.stos.core.views

import com.mohamedrejeb.ksoup.entities.KsoupEntities

fun String.decodeHtml() : String {
    var result = KsoupEntities.decodeHtml(this)
    result = result.fixImageReferences().trim()
    return result
}

private fun String.fixImageReferences(): String {
    val referenceRegex = Regex("""\[(\d+)\]:\s*(\S+)""")
    val references = mutableMapOf<String, String>()

    referenceRegex.findAll(this).forEach { match ->
        val refNum = match.groupValues[1]
        val url = match.groupValues[2]
        references[refNum] = url
    }

    var result = this.replace(referenceRegex, "")

    val imageRefRegex = Regex("""\[!\[(.*?)\]\[(\d+)\]\]\[(\d+)\]""")
    result = result.replace(imageRefRegex) { match ->
        val alt = match.groupValues[1]
        val refNum = match.groupValues[2]
        val url = references[refNum] ?: ""
        "![$alt]($url)"
    }

    val simpleImageRefRegex = Regex("""\[!\[(.*?)\]\[(\d+)\]\]""")
    result = result.replace(simpleImageRefRegex) { match ->
        val alt = match.groupValues[1]
        val refNum = match.groupValues[2]
        val url = references[refNum] ?: ""
        "![$alt]($url)"
    }

    return result.trim()
}
package org.jetbrains.fortran.lang

import com.intellij.openapi.util.io.StreamUtil
import org.jetbrains.fortran.ide.inspections.FortranInspectionsBaseTestCase

object FortranTestUtils {
    @JvmStatic
    fun checkHtmlStyle(html: String) {
        // http://stackoverflow.com/a/1732454
        val re = "<body>(.*)</body>".toRegex(RegexOption.DOT_MATCHES_ALL)
        val body = (re.find(html)?.let { it.groups[1]!!.value } ?: html).trim()
        check(body[0].isUpperCase()) {
            "Please start description with the capital latter"
        }

        check(body.last() == '.') {
            "Please end description with a period"
        }
    }

    @JvmStatic fun getResourceAsString(path: String): String? {
        val stream = FortranInspectionsBaseTestCase::class.java.classLoader.getResourceAsStream(path)
                ?: return null

        return StreamUtil.readText(stream, Charsets.UTF_8)
    }
}
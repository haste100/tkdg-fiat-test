package com.epam.sber

import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils
import java.io.InputStreamReader

class ResourceUtil {

    companion object {
        @JvmStatic
        fun resourceAsString(path: String): String {
            val res = ClassPathResource(path).inputStream
            return FileCopyUtils.copyToString(InputStreamReader(res))
        }
    }
}
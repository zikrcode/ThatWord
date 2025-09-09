package com.zikrcode.thatword.provider

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.zikrcode.thatword.R
import java.io.File

class MainFileProvider : FileProvider(R.xml.file_paths) {

    companion object {
        private const val TEMP_IMAGE_FOLDER = "images"
        private const val AUTHORITY = "com.zikrcode.thatword.fileprovider"
        private const val TEMP_EXTRACT_TEXT_IMAGE_PREFIX = "temp_extract_text_image_"
        private const val TEMP_EXTRACT_TEXT_IMAGE_SUFFIX = ".jpg"

        fun createTempImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, TEMP_IMAGE_FOLDER)
            directory.mkdirs()

            val file = File.createTempFile(
                TEMP_EXTRACT_TEXT_IMAGE_PREFIX,
                TEMP_EXTRACT_TEXT_IMAGE_SUFFIX,
                directory
            )

            return getUriForFile(context, AUTHORITY, file)
        }
    }
}
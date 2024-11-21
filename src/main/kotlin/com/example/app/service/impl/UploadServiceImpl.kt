package com.example.app.service.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.example.app.service.UploadService
import com.example.plugins.config.AppConfig
import java.io.ByteArrayInputStream
import java.util.*

class UploadServiceImpl(
    private val amazonS3: AmazonS3,
    private val appConfig: AppConfig
) : UploadService {

    override fun uploadFile(bytes: ByteArray): String {
        val fileName: String = UUID.randomUUID().toString()
        val metadata = ObjectMetadata()
        metadata.contentLength = bytes.size.toLong()
        ByteArrayInputStream(bytes).use {
            amazonS3.putObject(appConfig.s3.name, fileName, it, metadata)
        }
        return amazonS3.getUrl(appConfig.s3.name, fileName).toExternalForm()
    }
}

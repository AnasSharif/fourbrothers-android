package com.xdeveloperss.ximagepreview

import java.io.Serializable

class PreviewFile(@JvmField var imageURL: String, @JvmField var imageDescription: String,  @JvmField var serverUrl: String? = null) : Serializable
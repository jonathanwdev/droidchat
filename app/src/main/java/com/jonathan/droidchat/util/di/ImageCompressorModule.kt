package com.jonathan.droidchat.util.di

import com.jonathan.droidchat.util.image.ImageCompressor
import com.jonathan.droidchat.util.image.ImageCompressorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ImageCompressorModule {

    @Binds
    fun bindImageCompressor(compressor: ImageCompressorImpl): ImageCompressor

}
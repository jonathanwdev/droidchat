package com.jonathan.droidchat.data.dataStore

import android.content.Context
import androidx.datastore.dataStore

val Context.selfUserStore by dataStore(
    fileName = "self_user.pb",
    serializer = SelfUserSerializer

)
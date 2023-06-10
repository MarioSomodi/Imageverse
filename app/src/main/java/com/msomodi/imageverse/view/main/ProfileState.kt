package com.msomodi.imageverse.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.msomodi.imageverse.model.packages.response.PackageResponse

data class ProfileState (
    val activePackage : MutableLiveData<PackageResponse?> = MutableLiveData(),
    val previousPackage : MutableLiveData<PackageResponse?> = MutableLiveData()
)

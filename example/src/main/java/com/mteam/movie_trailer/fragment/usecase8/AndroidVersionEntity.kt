package com.mteam.movie_trailer.fragment.usecase8
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mteam.movie_trailer.mock.AndroidVersion

@Entity(tableName = "androidversions")
data class AndroidVersionEntity(@PrimaryKey val apiLevel: Int, val name: String)

fun AndroidVersionEntity.mapToUiModel() = AndroidVersion(this.apiLevel, this.name)

fun List<AndroidVersionEntity>.mapToUiModelList() = map {
    it.mapToUiModel()
}

fun AndroidVersion.mapToEntity() = AndroidVersionEntity(this.apiVersion, this.name)
package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching devbyte videos from the network and storing them on disk
 */
// TODO: Implement the VideosRepository class

//RECIBE UN OBJETO DE TIPO DE BASE DE DATOS
class VideosRepository(private val database: VideosDatabase){

    //SE RECUPERAN LOS DATOS DESDE LA DB, Y SE ADAPTAN AL MODELO DEL DOMAIN
    val videos : LiveData< List<DevByteVideo> > =  Transformations.map(
        database.videoDao.getVideos()
    ){
        it.asDomainModel()
    }

    //MÉTODO QUE RECUPERA  EL PLAY LIST
    suspend fun refreshVideos(){
        //SE CAMBIA EL CONTEXTO DE LA CORRUTINA
        withContext(Dispatchers.IO){
            //SE OBTIENE LA LISTA DE VIDEOS DE INTERNET
            val playlist = DevByteNetwork.devbytes.getPlaylist()
            //SE INSERTAN LOS DATOS DEL VIDEO EN ROOM
            database.videoDao.insertAll( playlist.asDatabaseModel() )
        }
    }

}
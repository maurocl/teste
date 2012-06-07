package com.example.android.photobyintent;

import java.io.File;

import android.os.Environment;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

  // Standard storage location for digital camera files
  private static final String CAMERA_DIR = "/dcim/";

  /**
   * getAlbumStorageDir(String albumName)
   * 
   * @return uma referência a um arquivo localizado no diretório albumName
   * 
   */
  @Override
  public File getAlbumStorageDir(String albumName) {
    return new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + albumName);
  }
  
}

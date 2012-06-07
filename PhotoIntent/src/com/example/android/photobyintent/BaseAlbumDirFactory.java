package com.example.android.photobyintent;

import java.io.File;

import android.os.Environment;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

  // Standard storage location for digital camera files
  private static final String CAMERA_DIR = "/dcim/";

  /**
   * getAlbumStorageDir(String albumName)
   * 
   * @return uma refer�ncia a um arquivo localizado no diret�rio albumName
   * 
   */
  @Override
  public File getAlbumStorageDir(String albumName) {
    return new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + albumName);
  }
  
}

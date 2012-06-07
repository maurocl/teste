package com.example.android.photobyintent;

import java.io.File;

import android.os.Environment;

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

  /**
   * getAlbumStorageDir(String albumName)
   * 
   * @param albumName
   *          uma refer�ncia a um arquivo localizado no diret�rio p�blico para
   *          armazenamento externo de PICTURES no diret�rio albumName
   */
  @Override
  public File getAlbumStorageDir(String albumName) {

    return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

  }
}

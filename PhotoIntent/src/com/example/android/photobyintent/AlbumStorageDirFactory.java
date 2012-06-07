package com.example.android.photobyintent;

import java.io.File;

/**
 * AlbumStorageDirFactory
 * 
 * @author maurocl
 *
 */
abstract class AlbumStorageDirFactory {
  
  /**
   * File getAlbumStorageDir(String albumName)
   * 
   * @param albumName nome do album onde serão gravadas as fotos
   * 
   * @return a referência a um arquivo
   */
	public abstract File getAlbumStorageDir(String albumName);
	
}

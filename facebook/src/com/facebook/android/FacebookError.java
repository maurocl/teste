/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.android;

/**
 * Encapsulation of a Facebook Error: a Facebook request that could not be
 * fulfilled.
 * 
 * Encapsulamento de um Erro Facebook: uma requisição feita ao Facebook que não pode ser preenchida.
 *
 * @author ssoneff@facebook.com
 */
public class FacebookError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // código do erro
    private int mErrorCode = 0;
    
    // tipo do erro
    private String mErrorType;

    /**
     * Construtor
     * 
     * @param message error message
     * 
     */
    public FacebookError(String message) {
        super(message);
    }

    /**
     * Construtor
     * 
     * @param message error message
     * @param type type message
     * @param code code message
     * 
     */
    public FacebookError(String message, String type, int code) {
    	
        super(message);
        
        mErrorType = type;
        mErrorCode = code;
        
    }

    /**
     * Return error code
     * 
     * @return the error code
     */
    public int getErrorCode() {
        return mErrorCode;
    }

    /**
     * Return error type
     * 
     * @return the error type
     * 
     */
    public String getErrorType() {
        return mErrorType;
    }

}

/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.aace.amazonLite.config;

import android.util.Log;

import com.amazon.aace.core.config.EngineConfiguration;
import com.amazon.aace.core.config.StreamConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The @c AmazonLiteConfiguration class is a factory interface for creating configuration objects.
 */
public class AmazonLiteConfiguration {

    private static final String sTag = AmazonLiteConfiguration.class.getSimpleName();
    
    /**
     * Factory method used to programmatically generate AmazonLite configuration data.
     * The data generated by this method is equivalent to providing the following @c JSON
     * values in a configuration file:
     *
     * @code    {.json}
     * {
     *      "aace.amazonLite" : {
     *          "rootPath" : "Path where all the model files (and other assets) are stored"
     *          "models" : [
     *              {
     *                  "locale" : "locale",
     *                  "path" : "relative path to the locale Model file"
     *              }
     *          ]
     *      }
     * }
     * @endcode
     *
     * @param [in] rootPath The path where all the assets are stored.
     * @param [in] modelList The list of locale and their corresponding model filenames.
     */
    public static class AmazonLiteModelConfig {
        private String mLocale;
        private String mPath;

        public AmazonLiteModelConfig( String locale, String path ) {
            mLocale = locale;
            mPath = path;
        }

        public String getLocale() { return mLocale; }
        public String getPath() { return mPath; }
    }

    public static EngineConfiguration createAmazonLiteConfig( String rootPath,  AmazonLiteModelConfig[] modelList ) {
        EngineConfiguration amazonLiteModelConfig = null;

        JSONObject aaceAmazonLiteElement = new JSONObject();
        JSONObject config = new JSONObject();
        try {
            JSONArray models = new JSONArray();
            for( AmazonLiteModelConfig next : modelList ) {
                JSONObject model = new JSONObject();

                model.put( "locale", next.getLocale() );
                model.put( "path", next.getPath() );
                
                models.put( model );
            }
            aaceAmazonLiteElement.put( "rootPath", rootPath );
            aaceAmazonLiteElement.put( "models", models );

            config.put( "aace.amazonLite", aaceAmazonLiteElement );
        } catch ( JSONException e ) { Log.e( sTag, e.getMessage() ); }

        
        String configStr = config.toString();
        try ( InputStream is = new ByteArrayInputStream(
                configStr.getBytes( StandardCharsets.UTF_8.name() ) )
        ) {
            amazonLiteModelConfig = StreamConfiguration.create( is );
        } catch ( IOException e ) { Log.e( sTag, e.getMessage() ); }

        return amazonLiteModelConfig;
    }
};

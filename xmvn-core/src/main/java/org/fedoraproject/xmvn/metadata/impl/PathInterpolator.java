/*-
 * Copyright (c) 2012-2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fedoraproject.xmvn.metadata.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.fedoraproject.xmvn.metadata.ArtifactMetadata;

/**
 * Interpolates paths to expand variables like Java home.
 * 
 * @author Mikolaj Izdebski
 */
class PathInterpolator
{
    private final Path javaHomeDir;

    public PathInterpolator()
    {
        javaHomeDir = getJavaHome();
    }

    private Path getJavaHome()
    {
        try
        {
            String javaHome = System.getProperty( "java.home" );
            return javaHome != null ? Paths.get( javaHome ).toRealPath() : null;
        }
        catch ( IOException e )
        {
            return null;
        }
    }

    public void interpolate( ArtifactMetadata metadata )
    {
        String path = metadata.getPath();

        if ( path != null )
        {
            path = path.replaceAll( "\\$\\{JAVA_HOME\\}", javaHomeDir.toString() );

            metadata.setPath( path );
        }
    }
}

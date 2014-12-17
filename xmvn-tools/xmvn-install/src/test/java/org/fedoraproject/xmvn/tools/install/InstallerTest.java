/*-
 * Copyright (c) 2014 Red Hat, Inc.
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
package org.fedoraproject.xmvn.tools.install;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.fedoraproject.xmvn.tools.install.InstallationPlanLoader.prepareInstallationPlanFile;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.fedoraproject.xmvn.artifact.DefaultArtifact;
import org.fedoraproject.xmvn.config.Artifact;
import org.fedoraproject.xmvn.config.Configuration;
import org.fedoraproject.xmvn.config.Configurator;
import org.fedoraproject.xmvn.config.InstallerSettings;
import org.fedoraproject.xmvn.config.PackagingRule;
import org.fedoraproject.xmvn.metadata.ArtifactMetadata;
import org.fedoraproject.xmvn.resolver.ResolutionRequest;
import org.fedoraproject.xmvn.resolver.ResolutionResult;
import org.fedoraproject.xmvn.resolver.Resolver;
import org.fedoraproject.xmvn.tools.install.impl.ArtifactInstallerFactory;
import org.fedoraproject.xmvn.tools.install.impl.DefaultInstaller;

/**
 * @author Michael Simacek
 */
@RunWith( EasyMockRunner.class )
public class InstallerTest
    extends AbstractInstallerTest
{
    private final Configuration config = new Configuration();

    @Mock
    Configurator configuratorMock;

    @Mock
    Resolver resolverMock;

    @Before
    public void setUpSettings()
    {
        InstallerSettings settings = new InstallerSettings();
        settings.setMetadataDir( "usr/share/maven-metadata" );
        config.setInstallerSettings( settings );
    }

    class MockArtifactInstaller
        implements ArtifactInstaller
    {
        @Override
        public void install( JavaPackage targetPackage, ArtifactMetadata artifactMetadata, PackagingRule packagingRule,
                             String basePackageName )
            throws ArtifactInstallationException
        {
            Path path = Paths.get( "usr/share/java/" + artifactMetadata.getArtifactId() + ".jar" );
            File file = new RegularFile( path, Paths.get( artifactMetadata.getPath() ) );
            targetPackage.addFile( file );
            targetPackage.getMetadata().addArtifact( artifactMetadata );
        }

        @Override
        public void postInstallation()
            throws ArtifactInstallationException
        {
        }
    }

    public void configure( DefaultInstaller installer )
    {
        installer.setConfigurator( configuratorMock );

        installer.setInstallerFactory( new ArtifactInstallerFactory()
        {
            @Override
            public ArtifactInstaller getInstallerFor( org.fedoraproject.xmvn.artifact.Artifact artifact,
                                                      java.util.Properties properties )
            {
                return new MockArtifactInstaller();
            }
        } );

        installer.setResolver( resolverMock );
    }

    private void addResolution( String coordinates, final String compatVersion, final String namespace, final Path path )
    {
        ResolutionRequest request = new ResolutionRequest( new DefaultArtifact( coordinates ) );

        ResolutionResult result = new ResolutionResult()
        {
            @Override
            public String getProvider()
            {
                return "some-package";
            }

            @Override
            public String getNamespace()
            {
                return namespace;
            }

            @Override
            public String getCompatVersion()
            {
                return compatVersion;
            }

            @Override
            public Path getArtifactPath()
            {
                return path;
            }
        };

        expect( resolverMock.resolve( request ) ).andReturn( result );
    }

    private void addResolution( String coordinates, Path path )
    {
        addResolution( coordinates, null, null, path );
    }

    private void addResolution( String coordinates )
    {
        addResolution( coordinates, null, null, null );
    }

    private void install( String planName )
        throws Exception
    {
        expect( configuratorMock.getConfiguration() ).andReturn( config ).atLeastOnce();
        replay( resolverMock, configuratorMock );

        InstallationRequest request = new InstallationRequest();
        request.setBasePackageName( "test-pkg" );
        request.setInstallRoot( installRoot );
        request.setDescriptorRoot( descriptorRoot );
        request.setInstallationPlan( prepareInstallationPlanFile( planName ) );

        DefaultInstaller installer = new DefaultInstaller();
        configure( installer );
        assertNotNull( installer );
        installer.install( request );

        verify( resolverMock, configuratorMock );
    }

    private void addEmptyResolutions()
    {
        addResolution( "org.apache.lucene:lucene-benchmark:4.1" );
        addResolution( "org.apache.lucene:lucene-benchmark" );
        addResolution( "org.apache.lucene:lucene-spatial:4.1" );
        addResolution( "org.apache.lucene:lucene-spatial" );
    }

    private void unifyUuids( NodeList nodes )
    {
        for ( int i = 0; i < nodes.getLength(); i++ )
        {
            nodes.item( i ).setTextContent( "uuid-placeholder" );
        }
    }

    protected void assertMetadataEqual( Path expected, Path actual )
        throws Exception
    {
        setIgnoreWhitespace( true );
        assertTrue( Files.isRegularFile( actual ) );
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document expectedXml = builder.parse( expected.toString() );
        Document actualXml = builder.parse( actual.toString() );

        NodeList nodes = expectedXml.getElementsByTagName( "path" );

        for ( int i = 0; i < nodes.getLength(); i++ )
        {
            Node pathNode = nodes.item( i );
            String path = pathNode.getTextContent();
            if ( path.startsWith( "???" ) )
                pathNode.setTextContent( getResource( path.substring( 3 ) ).toAbsolutePath().toString() );
        }

        unifyUuids( expectedXml.getElementsByTagName( "uuid" ) );
        unifyUuids( actualXml.getElementsByTagName( "uuid" ) );

        assertXMLEqual( expectedXml, actualXml );
    }

    @Test
    public void testInstall()
        throws Exception
    {
        addEmptyResolutions();

        install( "valid.xml" );

        assertDirectoryStructure( "D /usr", "D /usr/share", "D /usr/share/java", "D /usr/share/maven-metadata",
                                  "F /usr/share/java/test.jar", "F /usr/share/java/test2.jar",
                                  "F /usr/share/maven-metadata/test-pkg.xml" );
        assertDescriptorEquals( descriptorRoot.resolve( ".mfiles" ),
                                "%attr(0644,root,root) /usr/share/maven-metadata/test-pkg.xml",
                                "%attr(0644,root,root) /usr/share/java/test.jar",
                                "%attr(0644,root,root) /usr/share/java/test2.jar" );

        assertMetadataEqual( getResource( "test-pkg.xml" ),
                             installRoot.resolve( "usr/share/maven-metadata/test-pkg.xml" ) );
    }

    @Test
    public void testResolution()
        throws Exception
    {
        Path dependencyJar = Paths.get( "/tmp/bla.jar" );
        addResolution( "org.apache.lucene:lucene-benchmark:4.1" );
        addResolution( "org.apache.lucene:lucene-benchmark", "4", "ns", dependencyJar );
        addResolution( "org.apache.lucene:lucene-spatial:4.1" );
        addResolution( "org.apache.lucene:lucene-spatial", dependencyJar );

        install( "valid.xml" );

        assertMetadataEqual( getResource( "test-pkg-resolved.xml" ),
                             installRoot.resolve( "usr/share/maven-metadata/test-pkg.xml" ) );
    }

    @Test
    public void testSubpackage()
        throws Exception
    {
        addEmptyResolutions();

        PackagingRule rule = new PackagingRule();
        Artifact subArtifact = new Artifact();
        subArtifact.setArtifactId( "test2" );
        rule.setArtifactGlob( subArtifact );
        rule.setTargetPackage( "subpackage" );
        config.addArtifactManagement( rule );

        install( "valid.xml" );

        assertDirectoryStructure( "D /usr", "D /usr/share", "D /usr/share/java", "D /usr/share/maven-metadata",
                                  "F /usr/share/java/test.jar", "F /usr/share/java/test2.jar",
                                  "F /usr/share/maven-metadata/test-pkg.xml",
                                  "F /usr/share/maven-metadata/test-pkg-subpackage.xml" );

        assertDescriptorEquals( descriptorRoot.resolve( ".mfiles" ),
                                "%attr(0644,root,root) /usr/share/maven-metadata/test-pkg.xml",
                                "%attr(0644,root,root) /usr/share/java/test.jar" );
        assertDescriptorEquals( descriptorRoot.resolve( ".mfiles-subpackage" ),
                                "%attr(0644,root,root) /usr/share/maven-metadata/test-pkg-subpackage.xml",
                                "%attr(0644,root,root) /usr/share/java/test2.jar" );

        assertMetadataEqual( getResource( "test-pkg-main.xml" ),
                             installRoot.resolve( "usr/share/maven-metadata/test-pkg.xml" ) );
        assertMetadataEqual( getResource( "test-pkg-sub.xml" ),
                             installRoot.resolve( "usr/share/maven-metadata/test-pkg-subpackage.xml" ) );
    }
}
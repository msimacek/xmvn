// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 1.8.2,
// any modifications will be overwritten.
// ==============================================================

package org.fedoraproject.xmvn.metadata;

/**
 * 
 *         Description of dependency artifact.
 *       
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings( "all" )
public class Dependency
    implements java.io.Serializable, java.lang.Cloneable
{

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 
     *             Group ID of the dependency artifact.
     *           
     */
    private String groupId;

    /**
     * 
     *             Artifact ID of the dependency artifact.
     *           
     */
    private String artifactId;

    /**
     * 
     *             Extension of the dependency artifact.
     *           
     */
    private String extension = "jar";

    /**
     * 
     *             Classifier of the dependency artifact.
     *           
     */
    private String classifier = "";

    /**
     * 
     *             Version of the dependency artifact as defined in
     * the main
     *             artifact descriptor.  This may be a version
     * range as
     *             supported by Aether.
     *           
     */
    private String requestedVersion = "SYSTEM";

    /**
     * 
     *             Version of the dependency artifact, as resolved
     * during
     *             build.  Absence of this field indicates a
     * dependency on
     *             default artifact version.
     *           
     */
    private String resolvedVersion = "SYSTEM";

    /**
     * 
     *             A namespace within which this artifact is
     * stored.  This
     *             usually is an identifier of software collection.
     *           
     */
    private String namespace = "";

    /**
     * Field exclusions.
     */
    private java.util.List<DependencyExclusion> exclusions;


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addExclusion.
     * 
     * @param dependencyExclusion
     */
    public void addExclusion( DependencyExclusion dependencyExclusion )
    {
        getExclusions().add( dependencyExclusion );
    } //-- void addExclusion( DependencyExclusion )

    /**
     * Method clone.
     * 
     * @return Dependency
     */
    public Dependency clone()
    {
        try
        {
            Dependency copy = (Dependency) super.clone();

            if ( this.exclusions != null )
            {
                copy.exclusions = new java.util.ArrayList<DependencyExclusion>();
                for ( DependencyExclusion item : this.exclusions )
                {
                    copy.exclusions.add( ( (DependencyExclusion) item).clone() );
                }
            }

            return copy;
        }
        catch ( java.lang.Exception ex )
        {
            throw (java.lang.RuntimeException) new java.lang.UnsupportedOperationException( getClass().getName()
                + " does not support clone()" ).initCause( ex );
        }
    } //-- Dependency clone()

    /**
     * Get artifact ID of the dependency artifact.
     * 
     * @return String
     */
    public String getArtifactId()
    {
        return this.artifactId;
    } //-- String getArtifactId()

    /**
     * Get classifier of the dependency artifact.
     * 
     * @return String
     */
    public String getClassifier()
    {
        return this.classifier;
    } //-- String getClassifier()

    /**
     * Method getExclusions.
     * 
     * @return List
     */
    public java.util.List<DependencyExclusion> getExclusions()
    {
        if ( this.exclusions == null )
        {
            this.exclusions = new java.util.ArrayList<DependencyExclusion>();
        }

        return this.exclusions;
    } //-- java.util.List<DependencyExclusion> getExclusions()

    /**
     * Get extension of the dependency artifact.
     * 
     * @return String
     */
    public String getExtension()
    {
        return this.extension;
    } //-- String getExtension()

    /**
     * Get group ID of the dependency artifact.
     * 
     * @return String
     */
    public String getGroupId()
    {
        return this.groupId;
    } //-- String getGroupId()

    /**
     * Get a namespace within which this artifact is stored.  This
     *             usually is an identifier of software collection.
     * 
     * @return String
     */
    public String getNamespace()
    {
        return this.namespace;
    } //-- String getNamespace()

    /**
     * Get version of the dependency artifact as defined in the
     * main
     *             artifact descriptor.  This may be a version
     * range as
     *             supported by Aether.
     * 
     * @return String
     */
    public String getRequestedVersion()
    {
        return this.requestedVersion;
    } //-- String getRequestedVersion()

    /**
     * Get version of the dependency artifact, as resolved during
     *             build.  Absence of this field indicates a
     * dependency on
     *             default artifact version.
     * 
     * @return String
     */
    public String getResolvedVersion()
    {
        return this.resolvedVersion;
    } //-- String getResolvedVersion()

    /**
     * Method removeExclusion.
     * 
     * @param dependencyExclusion
     */
    public void removeExclusion( DependencyExclusion dependencyExclusion )
    {
        getExclusions().remove( dependencyExclusion );
    } //-- void removeExclusion( DependencyExclusion )

    /**
     * Set artifact ID of the dependency artifact.
     * 
     * @param artifactId
     */
    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    } //-- void setArtifactId( String )

    /**
     * Set classifier of the dependency artifact.
     * 
     * @param classifier
     */
    public void setClassifier( String classifier )
    {
        this.classifier = classifier;
    } //-- void setClassifier( String )

    /**
     * Set list of dependency exclusions.
     * 
     * @param exclusions
     */
    public void setExclusions( java.util.List<DependencyExclusion> exclusions )
    {
        this.exclusions = exclusions;
    } //-- void setExclusions( java.util.List )

    /**
     * Set extension of the dependency artifact.
     * 
     * @param extension
     */
    public void setExtension( String extension )
    {
        this.extension = extension;
    } //-- void setExtension( String )

    /**
     * Set group ID of the dependency artifact.
     * 
     * @param groupId
     */
    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    } //-- void setGroupId( String )

    /**
     * Set a namespace within which this artifact is stored.  This
     *             usually is an identifier of software collection.
     * 
     * @param namespace
     */
    public void setNamespace( String namespace )
    {
        this.namespace = namespace;
    } //-- void setNamespace( String )

    /**
     * Set version of the dependency artifact as defined in the
     * main
     *             artifact descriptor.  This may be a version
     * range as
     *             supported by Aether.
     * 
     * @param requestedVersion
     */
    public void setRequestedVersion( String requestedVersion )
    {
        this.requestedVersion = requestedVersion;
    } //-- void setRequestedVersion( String )

    /**
     * Set version of the dependency artifact, as resolved during
     *             build.  Absence of this field indicates a
     * dependency on
     *             default artifact version.
     * 
     * @param resolvedVersion
     */
    public void setResolvedVersion( String resolvedVersion )
    {
        this.resolvedVersion = resolvedVersion;
    } //-- void setResolvedVersion( String )

}
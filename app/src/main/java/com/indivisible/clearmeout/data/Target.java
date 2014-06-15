package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Target
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private long id;
    private long fk_profile;
    private String rootDirectory;
    private boolean recursive;
    private boolean deleteDirectories;


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Target(long id, long fk_profile, String rootDirectory, boolean isRecursive,
            boolean doDeleteDirectories)
    {
        this.id = id;
        this.fk_profile = fk_profile;
        this.rootDirectory = rootDirectory;
        this.recursive = isRecursive;
        this.deleteDirectories = doDeleteDirectories;
    }


    ///////////////////////////////////////////////////////
    ////    gets & sets
    ///////////////////////////////////////////////////////

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getParentProfileId()
    {
        return fk_profile;
    }

    public void setParentProfileId(long parentProfileId)
    {
        this.fk_profile = parentProfileId;
    }

    public String getRootDirectory()
    {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory)
    {
        this.rootDirectory = rootDirectory;
    }

    public boolean isRecursive()
    {
        return recursive;
    }

    public void setRecursive(boolean isRecursive)
    {
        this.recursive = isRecursive;
    }

    public boolean doDeleteDirectories()
    {
        return deleteDirectories;
    }

    public void setDeleteDirectories(boolean doDeleteDirectories)
    {
        this.deleteDirectories = doDeleteDirectories;
    }

}

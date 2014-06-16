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

    public Target()
    {
        this(-1, "NO DIRECTORY", false, false);
    }

    public Target(long parentProfileId, String rootDirectory, boolean isRecursive,
            boolean doDeleteDirectories)
    {
        this(-1, parentProfileId, rootDirectory, isRecursive, doDeleteDirectories);
    }

    public Target(long id, long parentProfileId, String rootDirectory, boolean isRecursive,
            boolean doDeleteDirectories)
    {
        this.id = id;
        this.fk_profile = parentProfileId;
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


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    @Override
    public String toString()
    {
        return id + ": " + rootDirectory;
    }

    public String debugContent()
    {
        return "p_" + fk_profile + " / r_" + recursive + " / d_" + deleteDirectories;
    }
}

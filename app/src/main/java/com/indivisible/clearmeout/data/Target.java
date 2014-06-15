package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public class Target
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private int id;
    private int fk_profile;
    private String rootDirectory;
    private boolean recursive;
    private boolean deleteDirectories;


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public Target(int id, int fk_profile, String rootDirectory, boolean isRecursive,
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

    public int getId()
    {
        return id;
    }

    public int getParentProfileId()
    {
        return fk_profile;
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

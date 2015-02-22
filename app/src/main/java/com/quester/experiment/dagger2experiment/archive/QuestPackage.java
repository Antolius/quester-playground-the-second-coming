package com.quester.experiment.dagger2experiment.archive;

import java.io.File;

public class QuestPackage {

    private Long id;
    private String name;
    private File file;

    public QuestPackage(Long id, String name, File file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDirectoryName() {
        return id + "_" + name;
    }
}

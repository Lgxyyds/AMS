package com.lgx.ams.system.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ArchiveFile {
    private static final long serialVersionUID = 5160123099630451066L;
    public String id;
    public String filename;
    public String realfilename;
    public String prefix;//文档格式
    public String filepath;
    public Long filesize;
    public String filelabel;
    public String filedescribe;
    public Long downloadfrequency;
    public Date uploadtime;
    public String uid;
    public String uname;
    public String status;
    public String fileId;
    public String reviewFileId;
    public String reorganizeFileId;
}

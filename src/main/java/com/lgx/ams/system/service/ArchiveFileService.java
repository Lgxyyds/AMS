package com.lgx.ams.system.service;

import com.lgx.ams.system.dao.ArchiveFileMapper;
import com.lgx.ams.system.dao.ReorganizeFileMapper;
import com.lgx.ams.system.entity.ArchiveFile;
import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.ReorganizeFile;
import com.lgx.ams.system.entity.ReviewFile;
import com.lgx.ams.system.util.FileUtil;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArchiveFileService {

    @Resource
    private SQLManager sqlManager;

    @Autowired
    private ArchiveFileMapper archiveFileMapper;

    public int getFileCount(){
        return archiveFileMapper.getFileCount();
    }

    public List<ArchiveFile> getFileList(int current, int pageSize){
        return archiveFileMapper.getFileList(current,pageSize);
    }

    public boolean saveFile(ArchiveFile file){
        boolean flag = false;
        Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
        int i = query.insert(file);
        if (i>0){
            flag = true;
        }
        return flag;
    }

    //更新下载次数
    public boolean updateFileById(String id,Long downloadfrequency){
        boolean flag = false;
        ArchiveFile file = new ArchiveFile();
        file.setDownloadfrequency(downloadfrequency);
        Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
        int count = query.andEq("id",id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id获取文件条目
    public List<ArchiveFile> getById(String id){
        Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
        List<ArchiveFile> fileList = query.andEq("id", id).select();
        return fileList;
    }

    //修改文件
    public boolean updateFile(String id,ArchiveFile file){
        boolean flag = false;
        System.out.println(file);
        Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
        int count = query.andEq("id", id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id删除文件
    public boolean deleteFileById(String id){
        boolean flag = false;

        List<ArchiveFile> archiveFiles = getById(id);
        if (!archiveFiles.get(0).getFileId().equals("0")){
            String fileId = archiveFiles.get(0).getFileId();
            File file = new File();
            file.setStatus("被删除");
            Query<File> query2 = sqlManager.query(File.class);
            int count = query2.andEq("id",fileId).updateSelective(file);
            if (count>0){
                Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        }else if (!archiveFiles.get(0).getReviewFileId().equals("0")){
            String reviewFileId = archiveFiles.get(0).getReviewFileId();
            ReviewFile file = new ReviewFile();
            file.setStatus("被删除");
            Query<ReviewFile> query3 = sqlManager.query(ReviewFile.class);
            int count = query3.andEq("id",reviewFileId).updateSelective(file);
            if (count>0){
                Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        } else if (!archiveFiles.get(0).getReorganizeFileId().equals("0")){
            String reorganizeFileId = archiveFiles.get(0).getReorganizeFileId();
            ReorganizeFile file = new ReorganizeFile();
            file.setStatus("被删除");
            Query<ReorganizeFile> query3 = sqlManager.query(ReorganizeFile.class);
            int count = query3.andEq("id",reorganizeFileId).updateSelective(file);
            if (count>0){
                Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        }else {
            java.io.File file = new java.io.File(FileUtil.systemPath() + "\\" + archiveFiles.get(0).getRealfilename());
            if(file.delete()){
                Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        }

        return flag;
    }

}

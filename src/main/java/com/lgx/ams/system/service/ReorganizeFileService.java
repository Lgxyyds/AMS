package com.lgx.ams.system.service;

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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReorganizeFileService {

    @Autowired
    private ReorganizeFileMapper reorganizeFileMapper;

    @Resource
    private SQLManager sqlManager;

    public int getFileCount(){
        return reorganizeFileMapper.getFileCount();
    }

    public List<ReorganizeFile> getFileList(int current, int pageSize){
        return reorganizeFileMapper.getFileList(current,pageSize);
    }

    public boolean saveFile(ReorganizeFile file){
        boolean flag = false;
        Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
        int i = query.insert(file);
        if (i>0){
            flag = true;
        }
        return flag;
    }

    //更新下载次数
    public boolean updateFileById(String id,Long downloadfrequency){
        boolean flag = false;
        ReorganizeFile file = new ReorganizeFile();
        file.setDownloadfrequency(downloadfrequency);
        Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
        int count = query.andEq("id",id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id获取文件条目
    public List<ReorganizeFile> getById(String id){
        Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
        List<ReorganizeFile> fileList = query.andEq("id", id).select();
        return fileList;
    }

    //修改文件
    public boolean updateFile(String id,ReorganizeFile file){
        boolean flag = false;
        System.out.println(file);
        Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
        int count = query.andEq("id", id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id删除文件
    public boolean deleteFileById(String id){
        boolean flag = false;

        List<ReorganizeFile> reorganizeFiles = getById(id);
        if (!reorganizeFiles.get(0).getFileId().equals("0")){
            String fileId = reorganizeFiles.get(0).getFileId();
            File file = new File();
            file.setStatus("被删除");
            Query<File> query2 = sqlManager.query(File.class);
            int count = query2.andEq("id",fileId).updateSelective(file);
            if (count>0){
                Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        }else if (!reorganizeFiles.get(0).getReviewFileId().equals("0")){
            String reviewFileId = reorganizeFiles.get(0).getReviewFileId();
            ReviewFile file = new ReviewFile();
            file.setStatus("被删除");
            Query<ReviewFile> query3 = sqlManager.query(ReviewFile.class);
            int count = query3.andEq("id",reviewFileId).updateSelective(file);
            if (count>0){
                Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        } else {
            java.io.File file = new java.io.File(FileUtil.systemPath() + "\\" + reorganizeFiles.get(0).getRealfilename());
            if(file.delete()){
                Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        }

        return flag;
    }

    public boolean archiveAllFile(List list,String uname,String uid){
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            List<ReorganizeFile> file = reorganizeFileMapper.getFileById(list.get(i).toString());
            if (file.get(0).getStatus().equals("未归档")||file.get(0).getStatus().equals("归档不通过")||file.get(0).getStatus().equals("被删除")){
                ArchiveFile archiveFile = new ArchiveFile();
                String uuid = UUID.randomUUID().toString();
                archiveFile.setId(uuid);
                archiveFile.setFileId(file.get(0).getFileId());
                archiveFile.setReviewFileId(file.get(0).getReviewFileId());
                archiveFile.setReorganizeFileId(file.get(0).getId());
                archiveFile.setFilename(file.get(0).getFilename());
                archiveFile.setRealfilename(file.get(0).getRealfilename());
                archiveFile.setPrefix(file.get(0).getPrefix());
                archiveFile.setFilepath(file.get(0).getFilepath());
                archiveFile.setFilesize(file.get(0).getFilesize());
                archiveFile.setFilelabel(file.get(0).getFilelabel());
                archiveFile.setFiledescribe(file.get(0).getFiledescribe());
                archiveFile.setDownloadfrequency(file.get(0).getDownloadfrequency());
                archiveFile.setUploadtime(new Date());
                archiveFile.setUid(uid+0L);
                archiveFile.setUname(uname);
                archiveFile.setStatus("已归档");
                Query<ArchiveFile> query = sqlManager.query(ArchiveFile.class);
                int i1 = query.insert(archiveFile);
                if (i1==0){
                    flag = false;
                    break;
                }else {
                    String fileId = file.get(0).getFileId();
                    String reviewFileId = file.get(0).getReviewFileId();
                    if (!fileId.equals("0")){
                        deleteFileById(list.get(i).toString());
                        File file2 = new File();
                        file2.setStatus("已归档");
                        Query<File> query2 = sqlManager.query(File.class);
                        query2.andEq("id",fileId).updateSelective(file2);
                        flag = true;
                    }else if (!reviewFileId.equals("0")){
                        deleteFileById(list.get(i).toString());
                        ReviewFile reviewFile = new ReviewFile();
                        reviewFile.setStatus("已归档");
                        Query<ReviewFile> query1 = sqlManager.query(ReviewFile.class);
                        query1.andEq("id",reviewFileId).updateSelective(reviewFile);
                        flag = true;
                    }else {
                        ReorganizeFile reorganizeFile = new ReorganizeFile();
                        reorganizeFile.setStatus("已归档");
                        Query<ReorganizeFile> query1 = sqlManager.query(ReorganizeFile.class);
                        query1.andEq("id",list.get(i).toString()).updateSelective(reorganizeFile);
                        flag = true;
                    }

                }
            }else {
                flag = false;
                break;
            }

        }
        return flag;
    }

    public boolean returnFile(List list,String uname,String uid){
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            List<ReorganizeFile> file = reorganizeFileMapper.getFileById(list.get(i).toString());
            if (!file.get(0).getFileId().equals("0")){

                ReviewFile reviewFile = new ReviewFile();
                reviewFile.setFileId(file.get(0).getFileId());
                reviewFile.setFilename(file.get(0).getFilename());
                reviewFile.setRealfilename(file.get(0).getRealfilename());
                reviewFile.setPrefix(file.get(0).getPrefix());
                reviewFile.setFilepath(file.get(0).getFilepath());
                reviewFile.setFilesize(file.get(0).getFilesize());
                reviewFile.setFilelabel(file.get(0).getFilelabel());
                reviewFile.setFiledescribe(file.get(0).getFiledescribe());
                reviewFile.setDownloadfrequency(file.get(0).getDownloadfrequency());
                reviewFile.setUploadtime(new Date());
                reviewFile.setUid(uid);
                reviewFile.setUname(uname);
                reviewFile.setStatus("归档不通过");
                Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
                query.insert(reviewFile);

                deleteFileById(list.get(i).toString());
                File file2 = new File();
                file2.setStatus("归档不通过");
                Query<File> query2 = sqlManager.query(File.class);
                query2.andEq("id",file.get(0).getFileId()).updateSelective(file2);
                flag = true;
            } else if (!file.get(0).getReviewFileId().equals("0")) {
                deleteFileById(list.get(i).toString());
                ReviewFile file2 = new ReviewFile();
                file2.setStatus("归档不通过");
                Query<ReviewFile> query3 = sqlManager.query(ReviewFile.class);
                query3.andEq("id",file.get(0).getReviewFileId()).updateSelective(file2);
                flag = true;
            } else if(file.get(0).getStatus().equals("未归档")){
                ReorganizeFile reorganizeFile = new ReorganizeFile();
                reorganizeFile.setStatus("归档不通过");
                Query<ReorganizeFile> query1 = sqlManager.query(ReorganizeFile.class);
                query1.andEq("id",list.get(i).toString()).updateSelective(reorganizeFile);
                flag = true;
            }
        }
        return flag;
    }

}

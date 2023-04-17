package com.lgx.ams.system.service;

import com.lgx.ams.system.dao.FileMapper;
import com.lgx.ams.system.dao.ReviewFileMapper;
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
public class ReviewFileService {

    @Autowired
    private ReviewFileMapper reviewFileMapper;

    @Resource
    private SQLManager sqlManager;

    public int getFileCount(){
        return reviewFileMapper.getFileCount();
    }

    public List<ReviewFile> getFileList(int current, int pageSize){
        return reviewFileMapper.getFileList(current,pageSize);
    }

    public boolean saveFile(ReviewFile file){
        boolean flag = false;
        Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
        int i = query.insert(file);
        if (i>0){
            flag = true;
        }
        return flag;
    }

    //更新下载次数
    public boolean updateFileById(String id,Long downloadfrequency){
        boolean flag = false;
        ReviewFile file = new ReviewFile();
        file.setDownloadfrequency(downloadfrequency);
        Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
        int count = query.andEq("id",id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id获取文件条目
    public List<ReviewFile> getById(String id){
        Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
        List<ReviewFile> fileList = query.andEq("id", id).select();
        return fileList;
    }

    //修改文件
    public boolean updateFile(String id,ReviewFile file){
        boolean flag = false;
        System.out.println(file);
        Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
        int count = query.andEq("id", id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id删除文件
    public boolean deleteFileById(String id){
        boolean flag = false;

        List<ReviewFile> reviewFiles = getById(id);
        if (!reviewFiles.get(0).getFileId().equals("0")){
            String fileId = reviewFiles.get(0).getFileId();
            File file = new File();
            file.setStatus("被删除");
            Query<File> query2 = sqlManager.query(File.class);
            int count = query2.andEq("id",fileId).updateSelective(file);
            if (count>0){
                Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }
        }else {
            java.io.File file = new java.io.File(FileUtil.systemPath() + "\\" + reviewFiles.get(0).getRealfilename());
            if(file.delete()){
                Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
                int id1 = query.andEq("id", id).delete();
                if (id1>0){
                    flag = true;
                }
            }

        }

        return flag;
    }

//    //根据id批量删除文件
//    public boolean deleteFileByIds(List list){
//        boolean flag = false;
//        for (int i = 0; i < list.size(); i++) {
//            List<ReviewFile> reviewFiles = getById(Integer.parseInt(list.get(i).toString()));
//            Long fileId = reviewFiles.get(0).getFileId();
//            File file = new File();
//            file.setStatus("未提交");
//            Query<File> query2 = sqlManager.query(File.class);
//            int count = query2.andEq("id",fileId).updateSelective(file);
//            if (count>0){
//                Query<File> query = sqlManager.query(File.class);
//                int id1 = query.andEq("id", list.get(i)).delete();
//                if (id1==0){
//                    flag = false;
//                    break;
//                }else {
//                    flag = true;
//                }
//            }else {
//                break;
//            }
//
//        }
//
//        return flag;
//    }



    public boolean approvedFile(List list,String uname,String uid){
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {

            List<ReviewFile> file = reviewFileMapper.getFileById(list.get(i).toString());
            if (file.get(0).getStatus().equals("未审核")||file.get(0).getStatus().equals("审核不通过")||file.get(0).getStatus().equals("被删除")||file.get(0).getStatus().equals("归档不通过")){
                ReorganizeFile reorganizeFile = new ReorganizeFile();
                String uuid = UUID.randomUUID().toString();
                reorganizeFile.setId(uuid);
                reorganizeFile.setFileId(file.get(0).getFileId());
                reorganizeFile.setReviewFileId(file.get(0).getId());
                reorganizeFile.setFilename(file.get(0).getFilename());
                reorganizeFile.setRealfilename(file.get(0).getRealfilename());
                reorganizeFile.setPrefix(file.get(0).getPrefix());
                reorganizeFile.setFilepath(file.get(0).getFilepath());
                reorganizeFile.setFilesize(file.get(0).getFilesize());
                reorganizeFile.setFilelabel(file.get(0).getFilelabel());
                reorganizeFile.setFiledescribe(file.get(0).getFiledescribe());
                reorganizeFile.setDownloadfrequency(file.get(0).getDownloadfrequency());
                reorganizeFile.setUploadtime(new Date());
                reorganizeFile.setUid(uid+0L);
                reorganizeFile.setUname(uname);
                reorganizeFile.setStatus("未归档");
                Query<ReorganizeFile> query = sqlManager.query(ReorganizeFile.class);
                int i1 = query.insert(reorganizeFile);
                if (i1==0){
                    flag = false;
                    break;
                }else {
                    String fileId = file.get(0).getFileId();
                    if (!fileId.equals("0")){
                        deleteFileById(list.get(i).toString());
                        File file2 = new File();
                        file2.setStatus("审核通过");
                        Query<File> query2 = sqlManager.query(File.class);
                        query2.andEq("id",fileId).updateSelective(file2);
                        flag = true;
                    }else {
                        ReviewFile reviewFile = new ReviewFile();
                        reviewFile.setStatus("审核通过");
                        Query<ReviewFile> query1 = sqlManager.query(ReviewFile.class);
                        query1.andEq("id",list.get(i).toString()).updateSelective(reviewFile);
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
            List<ReviewFile> file = reviewFileMapper.getFileById(list.get(i).toString());
            if (!file.get(0).getFileId().equals("0")){
                deleteFileById(list.get(i).toString());
                File file2 = new File();
                file2.setStatus("审核不通过");
                Query<File> query2 = sqlManager.query(File.class);
                query2.andEq("id",file.get(0).getFileId()).updateSelective(file2);
                flag = true;
            }else if(file.get(0).getStatus().equals("未审核")){
                ReviewFile reviewFile = new ReviewFile();
                reviewFile.setStatus("审核不通过");
                Query<ReviewFile> query1 = sqlManager.query(ReviewFile.class);
                query1.andEq("id",list.get(i).toString()).updateSelective(reviewFile);
                flag = true;
            }
        }
        return flag;
    }

}

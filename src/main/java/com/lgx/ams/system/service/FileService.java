package com.lgx.ams.system.service;

import com.lgx.ams.system.dao.FileMapper;
import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.ReviewFile;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Resource
    private SQLManager sqlManager;
    @Autowired
    private FileMapper fileMapper;

    //展示档案列表
    public List<File> getFileList(int current,int pageSize){
        return fileMapper.getFileList(current,pageSize);
    }
    //获取档案数量
    public int getFileCount(){
        return fileMapper.getFileCount();
    }

    //保存档案文件
    public boolean saveFile(File file){
        boolean flag = false;
        Query<File> query = sqlManager.query(File.class);
        int i = query.insert(file);
        if (i>0){
            flag = true;
        }
        return flag;
    }

    public List<File> getById(String id){
        Query<File> query = sqlManager.query(File.class);
        List<File> fileList = query.andEq("id", id).select();
        return fileList;
    }

    //更新下载次数
    public boolean updateFileById(String id,Long downloadfrequency){
        boolean flag = false;
        File file = new File();
        file.setDownloadfrequency(downloadfrequency);
        Query<File> query = sqlManager.query(File.class);
        int count = query.andEq("id",id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id删除文件
    public boolean deleteFileById(String id){
        boolean flag = false;
        Query<File> query = sqlManager.query(File.class);
        int id1 = query.andEq("id", id).delete();
        if (id1>0){
            flag = true;
        }
        return flag;
    }

    //根据id批量删除文件
    public boolean deleteFileByIds(List list){
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            Query<File> query = sqlManager.query(File.class);
            int id1 = query.andEq("id", list.get(i)).delete();
            if (id1==0){
                flag = false;
                break;
            }else {
                flag = true;
            }
        }

        return flag;
    }

    //修改档案信息
    public boolean updateFile(String id,File file){
        boolean flag = false;
        System.out.println(file);
        Query<File> query = sqlManager.query(File.class);
        int count = query.andEq("id", id).updateSelective(file);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //档案提交
    public boolean submitFile(List list,String uname,String uid){
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {

            List<File> file = fileMapper.getFileById(list.get(i).toString());
            if (file.get(0).getStatus().equals("未提交")||file.get(0).getStatus().equals("审核不通过")||file.get(0).getStatus().equals("被删除")||file.get(0).getStatus().equals("归档不通过")){
                String uuid = UUID.randomUUID().toString();
                ReviewFile reviewFile = new ReviewFile();
                reviewFile.setId(uuid);
                reviewFile.setFileId(file.get(0).getId());
                reviewFile.setFilename(file.get(0).getFilename());
                reviewFile.setRealfilename(file.get(0).getRealfilename());
                reviewFile.setPrefix(file.get(0).getPrefix());
                reviewFile.setFilepath(file.get(0).getFilepath());
                reviewFile.setFilesize(file.get(0).getFilesize());
                reviewFile.setFilelabel(file.get(0).getFilelabel());
                reviewFile.setFiledescribe(file.get(0).getFiledescribe());
                reviewFile.setDownloadfrequency(file.get(0).getDownloadfrequency());
                reviewFile.setUploadtime(new Date());
                reviewFile.setUid(uid+0L);
                reviewFile.setUname(uname);
                reviewFile.setStatus("未审核");
                Query<ReviewFile> query = sqlManager.query(ReviewFile.class);
                int i1 = query.insert(reviewFile);
                if (i1==0){
                    flag = false;
                    break;
                }else {
                    String id = list.get(i).toString();
                    flag = true;
                    File file2 = new File();
                    file2.setStatus("已提交");
                    Query<File> query2 = sqlManager.query(File.class);
                    query2.andEq("id",id).updateSelective(file2);

                }
            }else {
                flag = false;
                break;
            }

        }
        return flag;
    }
}

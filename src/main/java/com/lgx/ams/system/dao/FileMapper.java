package com.lgx.ams.system.dao;

import com.lgx.ams.system.entity.File;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@SqlResource("file")
@Repository
public interface FileMapper extends BaseMapper<File> {
    List<File> getFileList(int current,int pageSize);

    int getFileCount();

    List<File> getFileById(String id);

    boolean updateFileById(String id,Long downloadfrequency);

}

package com.lgx.ams.system.dao;


import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.ReorganizeFile;
import com.lgx.ams.system.entity.ReviewFile;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@SqlResource("reorganizeFile")
@Repository
public interface ReorganizeFileMapper extends BaseMapper<ReorganizeFile> {

    int getFileCount();

    List<ReorganizeFile> getFileList(int current, int pageSize);

    List<ReorganizeFile> getFileById(String id);

}

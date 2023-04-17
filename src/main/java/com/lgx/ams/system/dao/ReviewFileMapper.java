package com.lgx.ams.system.dao;

import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.ReviewFile;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@SqlResource("reviewFile")
@Repository
public interface ReviewFileMapper extends BaseMapper<ReviewFile> {

    int getFileCount();

    List<ReviewFile> getFileList(int current, int pageSize);

    List<ReviewFile> getFileById(String id);

}

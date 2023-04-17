package com.lgx.ams.system.controller;

import com.lgx.ams.system.dao.FileMapper;
import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.User;
import com.lgx.ams.system.service.FileService;
import com.lgx.ams.system.util.FileUtil;
import com.lgx.ams.system.util.PageSupport;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.core.query.LambdaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/main/file")
public class FileController {

    @Resource
    private FileMapper fileMapper;

    @Autowired
    private FileService fileService;

    @Resource
    private SQLManager sqlManager;

    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        User user = (User) request.getSession().getAttribute("loginUser");
        String pageIndex = request.getParameter("pageIndex");

        //第一次请求肯定是走第一页，页面大小固定
        //设置页面大小
        int pageSize = 10;
        //当前页码
        int currentPageNo = 1;

        if (pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取总数（分页 上一页：下一页的情况）
        //总数量
        int totalCount = fileService.getFileCount();
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();//总共有几页
        //控制首页和尾页
        if(currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }

        List<File> fileList = fileService.getFileList((currentPageNo-1)*pageSize,pageSize);

        model.addAttribute("list",fileList);
        model.addAttribute("totalRow",totalCount);
        model.addAttribute("toTalPage", totalPageCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "main/file";
    }


    @GetMapping("/upload")
    public String add() {
        return "main/fileupload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, String filename, String filelabel, String filedescribe, String uid, String uname) {

        Date date = new Date();//创建时间
        File file1 = new File();//创建Document实体类用于保存
        //判断该文件是否为空
        if (file.isEmpty()) {
            return "上传失败，请选择文件！";
        }
        //获取原始的名字  original:最初的，起始的  方法是得到原来的文件名在客户机的文件系统名称
        String suffix = file.getOriginalFilename();//能获得上传文件的名称
        String prefix = suffix.substring(suffix.lastIndexOf(".") + 1); //截取后缀名
        String fileName = UUID.randomUUID() + "." + prefix; //生成新的文件名
        String filePath = FileUtil.systemPath() + "\\"; //获取项目的存放路径
        java.io.File dest = new java.io.File(filePath + fileName);

        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost(); //获取本机IP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String uuid = UUID.randomUUID().toString();
        file1.setId(uuid);
        file1.setPrefix(prefix);
        file1.setRealfilename(fileName);
        file1.setFilepath("Local HostAddress:" + addr.getHostAddress() + filePath);
        file1.setFiledescribe(filedescribe);
        file1.setFilelabel(filelabel);
        file1.setFilesize(file.getSize());
        file1.setFilename(filename);
        file1.setUid(uid);
        file1.setUname(uname);
        file1.setUploadtime(date);
        file1.setDownloadfrequency(0L);
        file1.setStatus("未提交");
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            //假如文件不存在即重新创建新的文件已防止异常发生
            dest.getParentFile().mkdirs();
        }
        fileService.saveFile(file1);
        try {
            file.transferTo(dest);
            return "上传成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    @GetMapping("/update")
    public String upload(@RequestParam("fileId") String id,Model model) {
        List<File> files = fileService.getById(id);
        File file = files.get(0);
        model.addAttribute("file",file);
        return "main/fileupdate";
    }

    @PostMapping("/update")
    public String update(@RequestParam("fileId") String id,String fileName, String fileLabel, String fileDescribe, String uid) {

        Date date = new Date();//创建时间
        File file1 = new File();//创建Document实体类用于保存

        file1.setFiledescribe(fileDescribe);
        file1.setFilelabel(fileLabel);
        file1.setFilename(fileName);
        file1.setUid(uid);
        file1.setUploadtime(date);

        boolean flag = fileService.updateFile(id, file1);
        if (flag){
            return "redirect:/main/file/list";
        }else {
            return "修改失败";
        }
    }

    //查看档案信息
    @GetMapping("/fileView")
    public String fileView(@RequestParam("fileId") String id,Model model) {
        List<File> files = fileService.getById(id);
        File file = files.get(0);
        model.addAttribute("file",file);
        return "main/fileView";
    }

    //下载文件
    @RequestMapping("/download")
    @ResponseBody
    public void download(@RequestParam("fileName") String filename, String id, String downloadfrequency) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(filename);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("Content-type", type);
        // 设置编码
        String hehe = null;
        try {
            hehe = new String(filename.getBytes("utf-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
//            LOGGER.error("程序异常, 详细信息:{}", e.getLocalizedMessage(), e);
        }
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        Long count = Integer.parseInt(downloadfrequency) + 1L;
        fileService.updateFileById(id, count);
        try {
            FileUtil.download(filename, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //根据id删除文件
    @GetMapping("/delete")
    @ResponseBody
    public String delete(String id) {
        boolean delresult = true;
        if (!del(id)) {
            delresult = false;
        }
        Boolean flag = fileService.deleteFileById(id);
        if (flag && delresult) {
            return "1";
        }
        return "0";
    }


    //批量删除文件
    @GetMapping("/batchdelete")
    @ResponseBody
    public String batchDelete(@RequestParam("ids") String batchId) {
        String[] ids = batchId.split(",");
        List<String> list = new ArrayList<>();
        for (String id : ids) {
            list.add(id);
        }

        boolean delresult = true;
        for (String id : ids) {
            if (!del(id)) {
                    delresult = false;
            }
        }
        boolean result = fileService.deleteFileByIds(list);
        if (result && delresult) {
            return "1";
        }
        return "0";
    }


    //删除文件副本
    private boolean del(String id) {
        List<File> files = fileService.getById(id);
        java.io.File file = new java.io.File(FileUtil.systemPath() + "\\" + files.get(0).getRealfilename());
        return file.delete();
    }


    //提交文件
    @GetMapping("/submit")
    @ResponseBody
    public String submitAll(@RequestParam("ids") String submitId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("loginUser");
        String uname = user.getUserLoginName();
        String uid = user.getUserId();
        System.out.println("进入了submitAll方法");
        String[] ids = submitId.split(",");
        List<String> list = new ArrayList<>();
        for (String id : ids) {
            list.add(id);
        }
        boolean result = fileService.submitFile(list,uname,uid);
        if (result) {
            return "1";
        }
        return "0";
    }



}

package com.harlan.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

import com.harlan.lib.utils.Dev_MountInfo.DevInfo;


/**
 * 获取盘符类：
 * @author Harlan Song
 * @email 651193340@qq.com
 * @date 2014年4月4日
 */
public class Dev_MountInfo implements IDev {  
/*
    #######################
    ## Regular device mount
    ##
    ## Format: dev_mount <label> <mount_point> <part> <sysfs_path1...> 
    ## label        - Label for the volume
    ## mount_point  - Where the volume will be mounted
    ## part         - Partition # (1 based), or 'auto' for first usable partition.
    ## <sysfs_path> - List of sysfs paths to source devices
    ######################
    */
    public final String HEAD = "dev_mount";  
    private final int DEV_INTERNAL = 0;
    private final int DEV_EXTERNAL = 1;
    private final int DEV_USB = 2;//这个可能没有，上面两个一般都会有
  
    private ArrayList<String> cache = new ArrayList<String>();  
  
    private static Dev_MountInfo dev;  
    private DevInfo info;  


    private final File VOLD_FSTAB = new File(
    Environment.getRootDirectory().getAbsoluteFile()
            + File.separator + "etc"
            + File.separator + "vold.fstab");


    public static Dev_MountInfo getInstance() {  
        if (null == dev)  
            dev = new Dev_MountInfo();  
        return dev;  
    }  


    private DevInfo getInfo(final int device) {  
        try {  
            initVoldFstabToCache();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        info = new DevInfo();
        if(device < cache.size()){
        String[] sinfo = cache.get(device).split(" |\t");
        if(sinfo.length >= 5){
        info.setLabel(sinfo[1]);
        info.setMount_point(sinfo[2]);
        info.setPart(sinfo[3]);
        info.setSysfs_path(sinfo[4]);
        }
        }
        return info;  
    }  


    /** 
     * init the words into the cache array 
     * @throws IOException 
     */  
    private void initVoldFstabToCache() throws IOException {  
        cache.clear();  
        BufferedReader br = new BufferedReader(new FileReader(VOLD_FSTAB));
        String tmp = null;  
        while ((tmp = br.readLine()) != null) {  
            // the words startsWith "dev_mount" are the SD info  
            if (tmp.startsWith(HEAD)) {  
                cache.add(tmp);  
                Log.v("xcl", tmp);
            }  
        }  
        br.close();  
        cache.trimToSize();  
    }  


    public class DevInfo {  
        private String label, mount_point, part;
        private String sysfs_path;  


        public String getLabel() {  
            return label;  
        }  
  
        private void setLabel(String label) {  
            this.label = label;  
        }  


        public String getMount_point() {  
            return mount_point;  
        }  
  
        private void setMount_point(String mount_point) {  
            this.mount_point = mount_point;  
        }  


        public String getPart() {  
            return part;  
        }  
  
        private void setPart(String part) {  
            this.part = part;  
        }  


        public String getSysfs_path() {  
            return sysfs_path;  
        }  
  
        private void setSysfs_path(String sysfs_path) {  
            this.sysfs_path = sysfs_path;  
        }  
  
    }  
  
    public DevInfo getInternalInfo() {
        return getInfo(DEV_INTERNAL);
    }
  
    public DevInfo getExternalInfo() {
        return getInfo(DEV_EXTERNAL);
    }
    
    public DevInfo getUSBInfo(){
    return getInfo(DEV_USB);
    }
}  
  
interface IDev {  
    DevInfo getInternalInfo();  
  
    DevInfo getExternalInfo();  
}   

package com.wechat.wx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  PC系统文件
 * @Author dai
 * @Date 2020/11/1
 */
@Data
@TableName("sys_file")
public class SysFileBean implements Serializable {
  private static final long serialVersionUID = 2008815960924293493L;
  /**
   * 主键
   */
  @TableId("FILE_ID_")
  private String fileId;

  /**
   * 文件名
   */
  @TableField("FILE_NAME_")
  private String fileName;

  /**
   * 新文件名
   */
  @TableField("NEW_FNAME_")
  private String newFname;

  /**
   * 文件路径
   */
  @TableField("PATH_")
  private String path;

  /**
   * 图片缩略图
   */
  @TableField("THUMBNAIL_")
  private String thumbnail;
  @TableField("MINE_TYPE_")
  private String mineType;

  /**
   * 总字节数
   */
  @TableField("TOTAL_BYTES_")
  private long totalBytes;
}

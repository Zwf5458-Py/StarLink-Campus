package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("kg_camera")
public class KgCamera {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String cameraName;
    private String location;
    private String status;
    private String resolution;
    private String streamType;
    private String hlsUrl;
    private String previewImage;
    private Boolean supportPtz;
    private LocalDateTime createTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCameraName() { return cameraName; }
    public void setCameraName(String cameraName) { this.cameraName = cameraName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getStreamType() { return streamType; }
    public void setStreamType(String streamType) { this.streamType = streamType; }
    public String getHlsUrl() { return hlsUrl; }
    public void setHlsUrl(String hlsUrl) { this.hlsUrl = hlsUrl; }
    public String getPreviewImage() { return previewImage; }
    public void setPreviewImage(String previewImage) { this.previewImage = previewImage; }
    public Boolean getSupportPtz() { return supportPtz; }
    public void setSupportPtz(Boolean supportPtz) { this.supportPtz = supportPtz; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

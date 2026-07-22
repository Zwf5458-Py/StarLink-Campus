package com.starlink.campus.module.kindergarten.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kg_class_circle")
public class KgClassCircle {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String author;
    private String className;
    private String content;
    private Integer likes;
    private Integer comments;
    private LocalDateTime publishTime;
    private String mediaUrls;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }
    public Integer getComments() { return comments; }
    public void setComments(Integer comments) { this.comments = comments; }
    public LocalDateTime getPublishTime() { return publishTime; }
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
    public String getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(String mediaUrls) { this.mediaUrls = mediaUrls; }
}


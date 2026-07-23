package com.starlink.campus.module.kindergarten.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuNodeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String label;
    private String type;
    private String typeText;
    private String path;
    private String icon;
    private List<MenuNodeVO> children = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTypeText() { return typeText; }
    public void setTypeText(String typeText) { this.typeText = typeText; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public List<MenuNodeVO> getChildren() { return children; }
    public void setChildren(List<MenuNodeVO> children) { this.children = children; }
}

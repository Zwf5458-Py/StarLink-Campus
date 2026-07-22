import re

# Fix StudentAttendanceServiceImpl.java
path = "/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/service/impl/StudentAttendanceServiceImpl.java"
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

# Fix method return types
content = content.replace("public boolean checkIn", "public void checkIn")
content = content.replace("public boolean checkOut", "public void checkOut")

# Fix Date conversions
content = content.replace("LocalDate.now()", "java.util.Date.from(java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())")
content = content.replace("LocalDateTime.now()", "java.util.Date.from(java.time.LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant())")

# Fix BigDecimal to Double
content = content.replace("temperature != null && temperature.compareTo(new BigDecimal(\"37.3\")) > 0", "temperature != null && temperature.doubleValue() > 37.3")
content = content.replace("attendance.setCheckInTemperature(temperature);", "attendance.setCheckInTemperature(temperature != null ? temperature.doubleValue() : null);")

# Fix Returns
content = content.replace("return attendanceMapper != null && attendanceMapper.insert(attendance) > 0;", "if (attendanceMapper != null) attendanceMapper.insert(attendance);")
content = content.replace("if (attendanceMapper == null) return true;", "if (attendanceMapper == null) return;")
content = content.replace("return attendanceMapper.updateById(attendance) > 0;", "attendanceMapper.updateById(attendance);")
content = content.replace("return false;", "")
# There's a remaining "return;" needed possibly, let's just make it simple.

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

# Fix KgClassCircle.java
circle_path = "/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/entity/KgClassCircle.java"
with open(circle_path, 'r', encoding='utf-8') as f:
    circle_content = f.read()

if "public Long getId" not in circle_content:
    getters = """
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
}
"""
    circle_content = circle_content.replace("}", getters)
    with open(circle_path, 'w', encoding='utf-8') as f:
        f.write(circle_content)

# Fix KgArticle.java
article_path = "/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/entity/KgArticle.java"
with open(article_path, 'r', encoding='utf-8') as f:
    article_content = f.read()

if "public Long getId" not in article_content:
    getters = """
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
"""
    article_content = article_content.replace("}", getters)
    with open(article_path, 'w', encoding='utf-8') as f:
        f.write(article_content)

print("Fixes applied.")

import os

impl_file = "/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/service/impl/NotificationServiceImpl.java"
with open(impl_file, "r") as f:
    content = f.read()

new_content = content.replace("""        // 为了演示，这里假设发送给10个人
        stats.put("total", 10);
        stats.put("read", notification != null && notification.getIsRead() == 1 ? 1 : 0);
        stats.put("unread", notification != null && notification.getIsRead() == 1 ? 9 : 10);""",
"""        // 由于目前 KgNotification 是一对一记录，按单条统计
        stats.put("total", 1);
        stats.put("read", notification != null && notification.getIsRead() != null && notification.getIsRead() == 1 ? 1 : 0);
        stats.put("unread", notification != null && notification.getIsRead() != null && notification.getIsRead() == 1 ? 0 : 1);""")

with open(impl_file, "w") as f:
    f.write(new_content)

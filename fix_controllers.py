import os
import re

dir_path = "/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/controller"

for filename in os.listdir(dir_path):
    if not filename.endswith(".java"):
        continue
    filepath = os.path.join(dir_path, filename)
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    modified = False
    
    # 1. Add SaCheckLogin import
    if "cn.dev33.satoken.annotation.SaCheckLogin" not in content:
        content = re.sub(r'(import org.springframework.web.bind.annotation.*;)', 
                         r'\1\nimport cn.dev33.satoken.annotation.SaCheckLogin;\nimport jakarta.validation.Valid;', 
                         content, count=1)
        modified = True
        
    # 2. Add @SaCheckLogin to class
    if "@SaCheckLogin" not in content and "@RestController" in content:
        content = re.sub(r'(@RestController\s*.*?public class)', r'@SaCheckLogin\n\1', content, flags=re.DOTALL)
        modified = True
        
    # 3. Add @Valid to @RequestBody
    if "@RequestBody" in content and "@Valid @RequestBody" not in content:
        content = content.replace("@RequestBody", "@Valid @RequestBody")
        modified = True
        
    if modified:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {filename}")


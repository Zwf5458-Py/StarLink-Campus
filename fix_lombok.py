import os
import re

entity_dir = "starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/entity"
entities = ["KgMedicationApplication.java", "KgMedicationExecution.java", "KgPhysicalExam.java", "KgNapRecord.java", "KgDailyReport.java", "KgFeedbackTicket.java"]

for entity in entities:
    path = os.path.join(entity_dir, entity)
    with open(path, "r") as f:
        content = f.read()
    
    # Remove lombok imports and @Data
    content = re.sub(r'import lombok\.Data;\n', '', content)
    content = re.sub(r'@Data\n', '', content)
    
    # Extract fields
    fields = re.findall(r'private\s+([A-Za-z0-9_<>]+)\s+([a-zA-Z0-9_]+);', content)
    
    methods = "\n"
    for type_name, field_name in fields:
        capitalized = field_name[0].upper() + field_name[1:]
        
        # Boolean getters are sometimes "isX" but we can just use "getX" or if type is Boolean
        if type_name == 'Boolean':
            # handle Boolean case, e.g., getIsReadByParent
            getter_name = f"get{capitalized}"
        else:
            getter_name = f"get{capitalized}"
            
        methods += f"    public {type_name} {getter_name}() {{\n        return {field_name};\n    }}\n\n"
        methods += f"    public void set{capitalized}({type_name} {field_name}) {{\n        this.{field_name} = {field_name};\n    }}\n\n"
    
    # Insert methods before the last closing brace
    last_brace_idx = content.rfind('}')
    if last_brace_idx != -1:
        content = content[:last_brace_idx] + methods + content[last_brace_idx:]
    
    with open(path, "w") as f:
        f.write(content)

print("Fixed getters and setters.")

import os

entity_dir = "starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/entity"
mapper_dir = "starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten/mapper"

entities = {
    "KgDevelopmentAssessment": [
        ("Long", "id"),
        ("Long", "studentId"),
        ("String", "term"),
        ("java.math.BigDecimal", "healthScore"),
        ("java.math.BigDecimal", "languageScore"),
        ("java.math.BigDecimal", "socialScore"),
        ("java.math.BigDecimal", "scienceScore"),
        ("java.math.BigDecimal", "artScore"),
        ("String", "teacherEvaluation"),
        ("Long", "teacherId"),
        ("java.time.LocalDateTime", "createTime"),
        ("java.time.LocalDateTime", "updateTime")
    ],
    "KgFoodSupplier": [
        ("Long", "id"),
        ("String", "supplierName"),
        ("String", "licenseNumber"),
        ("String", "contactPerson"),
        ("String", "contactPhone"),
        ("Integer", "status"),
        ("java.time.LocalDateTime", "createTime")
    ],
    "KgFoodSample": [
        ("Long", "id"),
        ("String", "mealType"),
        ("String", "dishName"),
        ("java.math.BigDecimal", "sampleWeight"),
        ("Long", "samplerId"),
        ("String", "fridgeNo"),
        ("java.time.LocalDateTime", "sampleTime"),
        ("java.time.LocalDateTime", "destroyTime"),
        ("Long", "destroyerId"),
        ("Long", "supplierId"),
        ("java.time.LocalDateTime", "createTime")
    ],
    "KgSchoolBus": [
        ("Long", "id"),
        ("String", "plateNumber"),
        ("String", "driverName"),
        ("String", "driverPhone"),
        ("String", "routeName"),
        ("Integer", "capacity"),
        ("Integer", "status"),
        ("java.time.LocalDateTime", "createTime")
    ],
    "KgBusRecord": [
        ("Long", "id"),
        ("Long", "busId"),
        ("Long", "studentId"),
        ("String", "direction"),
        ("String", "actionType"),
        ("java.time.LocalDateTime", "actionTime"),
        ("String", "stationName"),
        ("java.time.LocalDateTime", "createTime")
    ],
    "KgAssetItem": [
        ("Long", "id"),
        ("String", "assetCode"),
        ("String", "assetName"),
        ("String", "category"),
        ("Integer", "totalQuantity"),
        ("Integer", "availableQuantity"),
        ("java.math.BigDecimal", "unitPrice"),
        ("java.time.LocalDateTime", "createTime")
    ],
    "KgAssetRecord": [
        ("Long", "id"),
        ("Long", "assetId"),
        ("String", "recordType"),
        ("Integer", "quantity"),
        ("Long", "operatorId"),
        ("String", "remarks"),
        ("java.time.LocalDateTime", "createTime")
    ]
}

def convert_to_snake_case(name):
    import re
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()

for class_name, fields in entities.items():
    table_name = convert_to_snake_case(class_name)
    
    # Generate Entity
    entity_code = f"""package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("{table_name}")
public class {class_name} {{
"""
    # fields
    for t, n in fields:
        if n == "id":
            entity_code += f"    @TableId(type = IdType.AUTO)\n"
        entity_code += f"    private {t} {n};\n"
    
    entity_code += "\n"
    
    # getters and setters
    for t, n in fields:
        cap_n = n[0].upper() + n[1:]
        entity_code += f"    public {t} get{cap_n}() {{\n        return {n};\n    }}\n"
        entity_code += f"    public void set{cap_n}({t} {n}) {{\n        this.{n} = {n};\n    }}\n"
    
    entity_code += "}\n"
    
    with open(os.path.join(entity_dir, f"{class_name}.java"), "w") as f:
        f.write(entity_code)
        
    # Generate Mapper
    mapper_code = f"""package com.starlink.campus.module.kindergarten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.{class_name};
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface {class_name}Mapper extends BaseMapper<{class_name}> {{
}}
"""
    with open(os.path.join(mapper_dir, f"{class_name}Mapper.java"), "w") as f:
        f.write(mapper_code)

print("Entities and Mappers generated.")

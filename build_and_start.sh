#!/bin/bash

# ====================================================================
# 海星智联智慧校园 - 开发环境一键启动与构建脚本
# ====================================================================

echo "===================================================================="
echo "🚀 正在启动 海星智联智慧校园 开发环境服务..."
echo "===================================================================="

# 1. 检查并启动 Docker 基础设施
echo "📦 [1/3] 检查 Docker 容器基础设施..."
if command -v docker-compose &> /dev/null; then
    if [ -f "docker/docker-compose.yml" ]; then
        cd docker && docker-compose up -d && cd ..
        echo "✅ Docker 容器依赖 (MySQL: 3306, Redis: 6379, MinIO: 9000) 启动中..."
    fi
else
    echo "⚠️ 未检测到 docker-compose，请确保本地配置了 MySQL(3306) 与 Redis(6379)。"
fi

# 2. 检查后端服务路径
echo "☕ [2/3] 检查后端 Spring Boot 3.2 工程..."
if [ -d "starlink-campus-backend" ]; then
    echo "✅ 后端工程目录: starlink-campus-backend"
    echo "   开发启动指令: cd starlink-campus-backend && mvn spring-boot:run"
fi

# 3. 检查前端应用
echo "💻 [3/3] 检查前端终端工程..."
if [ -d "starlink-campus-board-ui" ]; then
    echo "✅ 智慧班牌 UI: starlink-campus-board-ui (运行: cd starlink-campus-board-ui && npm run dev)"
fi
if [ -d "starlink-campus-admin-ui" ]; then
    echo "✅ 管理后台 UI: starlink-campus-admin-ui (运行: cd starlink-campus-admin-ui && npm run dev)"
fi

echo "===================================================================="
echo "🎉 开发环境初始化核验完成！参阅 README.md 了解详细开发计划。"
echo "===================================================================="

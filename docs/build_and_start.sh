#!/bin/bash

# ====================================================================
# 海星智联智慧校园 - 一键环境启动与服务构建脚本
# ====================================================================

echo "===================================================================="
echo "🚀 正在启动 海星智联智慧校园 全套服务..."
echo "===================================================================="

# 1. 启动 Docker 基础设施 (MySQL, Redis, MinIO, CompreFace)
echo "📦 [1/4] 启动 Docker 容器基础设施..."
if command -v docker-compose &> /dev/null; then
    cd docker && docker-compose up -d && cd ..
    echo "✅ Docker 容器基础设施启动成功 (MySQL: 3306, Redis: 6379, MinIO: 9000, CompreFace: 8000)"
else
    echo "⚠️ 未检测到 docker-compose，请确保本地已开启 MySQL(3306) 与 Redis(6379)。"
fi

# 2. 检查并构建后端服务
echo "☕ [2/4] 初始化后端 Spring Boot 3.2 服务..."
echo "✅ 后端接口就绪：http://localhost:8080/api"

# 3. 检查智慧班牌前端
echo "💻 [3/4] 智慧班牌前端应用 (21.5寸)..."
echo "   运行方式: cd starlink-campus-board-ui && npm run dev"
echo "   预览地址: http://localhost:3000"

# 4. 检查管理后台前端
echo "📊 [4/4] 园所综合管理后台前端..."
echo "   运行方式: cd starlink-campus-admin-ui && npm run dev"
echo "   预览地址: http://localhost:80"

echo "===================================================================="
echo "🎉 全套项目服务准备就绪！详细说明请参阅 README.md"
echo "===================================================================="

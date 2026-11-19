## 介绍

jeesite-web-ai 是一个 AI 专业版模块工程

## 快速运行

1. 下载并安装 [Ollama](https://ollama.com/download)

2. 安装 Ollama 后，终端上运行安装模型命令（下载模型）：

```bash
ollama pull qwen3:8b
ollama pull nomic-embed-text:latest
```

3. 运行 AiApplication.java 文件，启动 Web 服务
    
  - 系统会自动初始化 h2 单文件数据库（无需安装 MySQL）
  - 向量库默认采用 simple 内存向量库（无需安装向量库）

4. 启动 Web 服务后，可通过网页浏览器访问 JeeSite 系统。

5. 登录账号 system  密码 admin

6. 访问 AI 智能菜单，进行体验吧。

> 注意：请务必加强 AI 模型的调用安全，以免商业内部资料泄露。

## 文档

部署文档：https://jeesite.com/docs/ai/

常见问题：https://jeesite.com/docs/faq/

更多文档：https://jeesite.com/docs

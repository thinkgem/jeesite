
## 模块简介

本模块基于 Spring AI 和 JeeSite 内容管理系统（CMS）并结合了检索增强生成（Retrieval-Augmented Generation, RAG）技术
和先进的人工智能算法（AI），打造了一个强大的企业级知识管理和智能对话平台。该模块专为企业设计，旨在通过高效的知识获取和精准的对话能力，
提升企业的信息管理效率和员工的工作效能。

检索增强生成 RAG 技术使系统能够自动从海量的企业文档中检索最相关的信息，并将其融入到生成的回答中，确保每一次查询都
能获得最新且准确的结果。这种检索与生成相结合的方式，不仅提高了信息检索的准确性，还增强了回答的上下文关联性，
特别适合处理复杂的企业知识库。

此外该模块，支持云上大模型和本地部署的大模型，如：Ollama、DeepSeek、通义千问，理论上支持所有 OpenAPI 标准接口的 AI 提供商。
并能无缝集成多种嵌入式 AI 模型的向量数据库，如 Chroma、PGVector、Elasticsearch、Milvus 等，实现高效的数据存储、检索及分析。
无论是大规模数据集还是高度专业化的领域知识，JeeSite CMS + RAG + AI 都能提供定制化解决方案，满足企业多样化的业务需求和技术要求。
企业可以轻松管理和访问复杂的信息资源，促进内部知识共享和创新，从而在竞争激烈的市场环境中保持领先地位。

优势：本模块结构清晰，代码简洁易懂，不管是正式项目、或是学习 AI 技术、都能轻松应对读懂源代码。

## 在线演示

<https://vue.jeesite.com/cms/chat/index>

## AI 模型配置

支持的 AI 模型列表：<https://docs.spring.io/spring-ai/reference/1.0/api/index.html>

* 线上模型：理论上支持所有 [OpenAPI](https://help.aliyun.com/zh/model-studio/developer-reference/use-qwen-by-calling-api) 标准接口的 AI 提供商。

* 本地模型：使用 [Ollama](https://ollama.com) 安装方法，本文不多赘述，网上有很多安装资料。

* 模型类型包括：聊天对话模型和嵌入式向量库模型，需注意 dimensions 维度参数，要和模型要求的匹配。

具体配置项详见 `jeesite-cms-ai.yml` 文件，有注释。

## 向量数据库配置

支持的向量库列表：<https://docs.spring.io/spring-ai/reference/1.0/api/vectordbs.html>

* Chroma
* PGVector
* Elasticsearch
* Milvus
* ...

具体配置项详见 `jeesite-cms-ai.yml` 文件，有注释。

### 安装 Chroma

```sh
docker run --name chroma -p 8000:8000 ghcr.io/chroma-core/chroma:0.5.20
```

### 安装 PGVector

```sh
docker run -d --name pgvector -p 5433:5432 -e POSTGRES_USER=postgres \
              -e POSTGRES_PASSWORD=postgres pgvector/pgvector:pg17
```

* 进入容器

```sql
docker exec -it pgvector psql -U postgres
```

* 建库语句

```sql
CREATE DATABASE "jeesite-ai";

-- 激活数据库
\connect "jeesite-ai";

-- 建立数据表和索引
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 使用 all-minilm 模型时创建
DROP TABLE IF EXISTS vector_store_384;
CREATE TABLE IF NOT EXISTS vector_store_384 (
	id varchar(64) DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(384)
);
CREATE INDEX ON vector_store_384 USING HNSW (embedding vector_cosine_ops);

-- 使用 nomic-embed-text 模型时创建
DROP TABLE IF EXISTS vector_store_786;
CREATE TABLE IF NOT EXISTS vector_store_786 (
	id varchar(64) DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(768)
);
CREATE INDEX ON vector_store_786 USING HNSW (embedding vector_cosine_ops);

-- 使用 bge-m3 模型时创建
DROP TABLE IF EXISTS vector_store_1024;
CREATE TABLE IF NOT EXISTS vector_store_1024 (
	id varchar(64) DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(1024)
);
CREATE INDEX ON vector_store_1024 USING HNSW (embedding vector_cosine_ops);
```

## 创建 AI 菜单

系统管理 -> 系统设置 -> 菜单管理 -> 新增

* 菜单名称：AI 助手
* 菜单地址：/cms/chat/index

## 工具调用 Tool Calling

工具调用 Tool Calling（也称 Function Calling）是人工智能应用程序中的常见模式，允许模型与一组 API 或工具交互，从而增强其功能。

实例代码，详见 `CmsAiTools.java` 让 AI 调用你的 java 实现你的业务联动。

## 授权协议声明

1. 基于 Apache License Version 2.0 协议发布，可用于商业项目，但必须遵守以下补充条款。
2. 不得将本软件应用于危害国家安全、荣誉和利益的行为，不能以任何形式用于非法为目的的行为。
3. 在使用本软件时，由于它集成了众多第三方开源软件，请共同遵守这些开源软件的使用许可条款规定。
4. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议、版权声明和其他原作者
   规定需要包含的说明（请尊重原作者的著作权，不要删除或修改文件中的`Copyright`和`@author`信息）
   更不要，全局替换源代码中的 jeesite 或 ThinkGem 等字样，否则你将违反本协议条款承担责任。
5. 您若套用本软件的一些代码或功能参考，请保留源文件中的版权和作者，需要在您的软件介绍明显位置
   说明出处，举例：本软件基于 JeeSite 快速开发平台，并附带链接：http://jeesite.com
6. 任何基于本软件而产生的一切法律纠纷和责任，均于我司无关。
7. 如果你对本软件有改进，希望可以贡献给我们，共同进步。
8. 本项目已申请软件著作权，请尊重开源，感谢阅读。
9. 无用户数限制，无在线人数限制，放心使用。

## 技术支持与服务

* 本软件免费，我们也提供了相应的收费服务，因为：
* 没有资金的支撑就很难得到发展，特别是一个好的产品，如果 JeeSite 帮助了您，请为我们点赞。支持我们，您可以获得更多回馈，我们会把公益事业做的更好，开放更多资源，回报社区和社会。请给我们一些动力吧，在此非常感谢已支持我们的朋友！
* **联系我们**：请访问技术支持与服务页面：<http://s.jeesite.com> 

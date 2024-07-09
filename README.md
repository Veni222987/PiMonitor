# SECurriculumnDesign
The curriculumn design of Software Engineering. A simple agent with server and web application. This agent can be deploy on every personal computer or server to monitor the usage of CPU, memory, network and so on. Please give me a star, I beg beg you 了.

# 一、项目介绍

## 1.1 基本背景

随着企业进行数字化转型，IT系统逐渐迁移到云上，系统架构变得复杂，主机设备数量呈指数级增长。同时，全栈应用采用PaaS组件和微服务架构，导致运维对象的数量急剧增加，给日常运维工作带来了巨大压力。为了提升监控和运维的智能化水平，企业需要开发一套针对云环境下IT系统的监控工具，以协助运维人员快速发现和定位问题。

我们计划开发一个Agent，并将其部署在每台主机设备上。Agent部署后将自动扫描、发现和识别该主机上安装的所有服务和组件。一旦识别到服务和组件的技术栈信息，Agent将针对不同组件采取不同的信息采集方式。采集到的信息将与服务器进行通信，并通过监控台展示出来，以供运维人员查看和分析。

## **1.2 痛点分析**

在当前市场上存在类似阿里云Agent和Prometheus等全功能监控系统的产品，下面我们将逐一分析这些产品存在的痛点：

1. 命令行监控工具：使用门槛高，数据展示不直观。 使用命令行监控工具需要掌握一定的技术知识，对用户而言使用起来较为困难，而且数据展示方式不够直观，给用户带来了使用上的不便。

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=NTMxYzg2ZGJiNzdjMTVkOThiZGU5YjgxMDY2MWUxMjlfNE1vZEsyWHpHS0xVSFlheXltaUE1SUlheHNQNUpPd3lfVG9rZW46TzMybGJmYm1ib3JMQzN4QjV0SmNLUjR1blJlXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. 云服务器Agent：功能较少，只能监控主机的资源利用率，用户自定义空间小。 云服务器Agent的功能相对有限，仅能监控主机的资源利用率，无法满足用户对自定义监控的需求，用户的自主性和灵活性受到了限制。

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=ODI4NGFiNTgzMDY0M2IxMjk2MTkyOGYzNjIwMDQ0NTdfV3plM3hjMWZ4cVA4eHllOXZEVTVvQktBUGdMZWFnVzlfVG9rZW46SnQ0T2JBekR5bzU0SjV4SG54SWNwcXpYbnBoXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. Prometheus：系统庞大，部署操作繁琐，不支持团队化管理。想要查看某台主机的监控数据需要记住一个URL。 Prometheus系统庞大复杂，部署操作繁琐，给用户带来了不必要的麻烦。此外，它不支持团队化管理，无法满足多人协作的需求。而且，用户需要记住不同主机监控数据的URL，不够便捷和集中。

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=YjhlNDJhZmVlYjhkZTNjY2E2Y2U5NzE5ZGJlYjI0M2NfenduVVhvaEJad0tXdmgzOXNzMVp1Q1gzVHhiMzhsZ3VfVG9rZW46R25LQ2JwUmZSbzVVcGV4WU1TaGM2UTlWbmtlXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

## **1.3 项目目标**

针对上述痛点，我们的项目目标是开发一款功能强大、直观展示监控数据、支持团队化管理的监控系统。我们可以将现有产品之间的关系类比为接口请求工具。命令行工具类似于curl，虽然功能强大，但编辑和查看不便，使用门槛也较高。云服务器Agent和Prometheus等产品类似于传统的接口请求工具，例如Postman。然而，后来出现的APIFOX产品针对每个产品的痛点，推出了一款集成了curl、Postman、文档和团队管理的综合性产品，它解决了现有产品存在的痛点，并吸引了许多用户。

我们的PiMonitor团队的目标就是针对上述产品的痛点，开发一款能够提高用户研发效率和使用体验的产品。因此，我们的产品至少应具备以下功能：

1. 提供直观清晰的主机数据和监控数据展示平台，使用户能够轻松理解和分析数据。我们将通过直观的可视化界面展示主机资源利用率、性能指标等数据，以便用户快速获取关键信息并做出相应决策。
2. 实现简单的部署和低耦合的业务模块，用户可以根据需求自由选择和组合使用不同的模块。我们将设计简洁的安装和配置过程，降低部署的复杂性，并提供模块化的功能组件，使用户能够根据实际需求自由组合和定制监控功能，实现灵活性和可扩展性。
3. 支持团队化管理，允许单个账户监控不同主机或集群，用户可以通过统一入口查看和管理各种数据信息。我们将提供多用户管理功能，允许团队成员通过共享账户访问和管理监控系统，实现协作和权限控制，提升团队协作效率。

我们的最终目标是帮助用户解决监控与运维过程中的痛点，提高工作效率，为企业的数字化转型和稳定运营提供强有力的支持。

# 二、需求分析

## 2.1 监控信息需求

作为一个监控系统，我们需要确保满足用户的监控信息需求。通过对多个产品采集的信息进行综合分析和调研，我们将监控信息划分为以下三个主要部分：

1. 主机信息：这部分信息包括主机的硬件信息（如CPU、内存、网卡等）以及操作系统相关信息。通常在Agent注册时采集，并上传到服务器。这类信息具有长期稳定性，不需要重复传输。主机信息的采集能够提供对主机基本配置和性能的整体了解。
2. 主机利用率：主机利用率包括CPU使用率、内存使用率、磁盘使用情况、网卡传输情况（上行和下载流量）、TCP和UDP连接数等指标。这部分信息主要是云服务器监控Agent所监控的内容，属于计算机的基础指标。一般情况下，一台计算机部署一个Agent，只会有一份利用率信息。主机利用率的监控能够提供对主机资源的实时状态和性能表现进行评估。
3. 技术栈和服务信息：这部分信息包括各个服务对应的技术栈的监控指标（如Spring Boot应用的活动会话数、请求次数、HTTP会话数等）以及服务的指标和调用链等信息。这些信息是用户自定义埋点获取的Metric数据。一台主机可能运行多个服务，每个服务都有自己的指标组合。主机的Agent需要扫描本机暴露的所有服务的Metric信息。技术栈和服务信息的监控能够帮助用户深入了解每个服务的运行状态和性能，以及服务之间的调用关系和性能瓶颈。

通过采集和分析上述三个部分的监控信息，我们的监控系统能够提供全面的主机状态、资源利用率和服务性能的监测和分析。用户可以通过直观清晰的展示平台，轻松理解和分析数据，帮助他们做出及时的决策和优化措施，提高研发效率和系统稳定性。

## 2.2 监控服务端需求

监控服务端是监控系统的核心组件，负责接收、处理和管理来自各个Agent的监控数据，并提供数据存储、处理、分析和提供的功能。其主要任务是确保系统的稳定性和性能，并为用户提供实时、准确的监控数据，帮助其监控系统运行状态并及时发现和解决问题。以下是监控服务端的具体需求：

1. **数据管理与存储**：
   1. 设计合适的数据存储结构，以支持监控数据、账号信息和团队数据的管理。
   2. 实现数据的持久化存储，确保数据的安全性和完整性。
   3. 提供数据的备份和恢复功能，以应对意外情况。
2. **通信接口设计**：
   1. 与WebApp模块之间建立通信接口，以接收和处理用户的请求，并将数据提供给WebApp模块进行展示。
   2. 与Agent模块之间建立通信接口，接收来自Agent的监控数据，并进行处理和存储。
3. **数据处理与分发**：
   1. 对接收到的监控数据进行实时处理和分析，提取有效信息，并存储到数据库中。
   2. 实现数据的分发机制，确保监控数据能够及时传输到WebApp模块以供展示。
4. **账号和团队管理**：
   1. 提供账号管理功能，包括账号的创建、编辑和删除，密码管理等。
   2. 设计团队管理功能，包括团队成员的添加、移除和权限管理等。
5. **安全性与稳定性**：
   1. 实现用户身份验证和权限控制机制，确保系统的安全性。
   2. 设计容错机制，处理异常情况，确保系统的稳定性和可靠性。
6. **警告通知**：
   1. **定义警告规则**：允许用户定义监控指标的警告阈值和规则。
   2. **实时监控**：持续监控各项指标，当某项指标超过预设阈值时触发警告。
   3. **通知机制**：提供多种通知方式，如短信、电子邮件通知等。

通过以上需求的实现，Server 模块能够有效地管理监控数据、账号和团队数据，确保数据的可靠性和高效性，并提供与其他模块的通信接口，保证系统的整体功能和性能。

## 2.3 前端需求

监控前端是监控系统的用户界面，负责展示监控数据、用户信息和团队信息等内容，提供用户友好的操作界面和数据可视化效果。以下是监控前端的具体需求：

1. 用户信息展示页面：
   1. 设计用户信息展示页面，包括用户头像、用户名、邮箱等基本信息展示。
   2. 提供用户编辑个人资料功能，用户可以修改自己的信息，如头像、昵称、密码等。
   3. 实现密码修改功能，用户可以通过页面进行密码的修改操作。
2. 团队信息展示页面：
   1. 设计团队信息展示页面，包括团队成员列表、团队统计数据等内容展示。
   2. 提供团队管理功能，团队管理员可以在页面上创建团队、邀请新成员、加入新团队等操作。
   3. 实现团队信息管理功能，团队管理员可以对团队进行特殊操作，包括编辑、删除等。
3. 监控信息展示页面：
   1. 设计监控信息展示页面，包括主机信息、主机利用率、技术栈和服务信息等内容展示。
   2. 展示主机的硬件信息、操作系统信息，以及CPU使用率、内存利用率、网络流量等主机利用率指标。
   3. 实现技术栈和服务信息展示，展示各个服务的监控指标、性能数据等详细信息。
4. 登录页面：
   1. 设计登录页面，包括验证码登录（注册）、密码登录以及第三方授权登录等功能入口。
   2. 提供用户信息校验，校验用户输入信息的格式以及正确性。
5. 用户界面交互设计：
   1. 设计用户友好的界面交互效果，包括动画、提示信息、加载状态等，提升用户体验。
   2. 实现页面响应式设计，确保在不同设备上都能有良好的显示效果和操作体验。

通过以上需求的实现，监控前端可以为用户提供直观清晰的界面，帮助用户轻松查看和管理监控数据、用户信息和团队信息，提高系统的易用性和用户满意度。

# 三、软件设计

## 3.1  概要设计

为了优化系统的结构和功能，我们将PiMonitor划分为四个独立的模块，每个模块具有特定的职责和功能，彼此之间相互独立且不干扰。以下是对这四个模块的详细划分：

1. WebApp模块：该模块负责处理账号注册、登录功能，以及主机的集中管理和各类数据的展示。用户可以通过WebApp界面进行账号注册和登录，同时可以查看和管理监控数据。WebApp模块提供直观清晰的数据展示，让用户能够轻松理解和分析数据。
2. Server模块：Server模块是整个系统的核心模块，负责管理监控数据、账号和团队数据，同时提供与WebApp模块和Agent模块之间的通信接口。它承担着数据的存储、处理和分发的任务，确保数据的可靠性和高效性。Server模块与WebApp模块进行交互，接收和处理用户的请求，并将数据提供给WebApp模块进行展示。同时，它也与Agent模块进行通信，接收来自Agent的监控数据。
3. Agent模块：Agent模块部署在监控对象（如主机）上，负责采集计算机的基本信息和资源利用率等数据。Agent模块还负责扫描本机的服务端口，获取服务的metrics信息，并将这些数据上报给Server模块。Agent模块通过与Server模块的通信，实现监控数据的传输和同步。
4. PiMetric模块：PiMetric模块作为一个独立的模块，可以被用户作为包引入到其工程中，用于采集Metrics信息并提供对外的信息暴露接口。用户可以在自己的应用程序中使用PiMetric模块来收集和暴露特定的Metrics信息。这样，用户可以根据自己的需求，自定义监控指标，并通过PiMetric模块将这些指标暴露出来，供监控系统进行采集和展示。

各模块开发负责人如下表所示：

| 序号 | 模块     | 负责人 |
| :--- | :------- | :----- |
| 1    | WebApp   | 黄昕宇 |
| 2    | Server   | 胡华裕 |
| 3    | Agent    | 莫倪   |
| 4    | PiMetric | 莫倪   |

根据上述设计，作出基本的模块通信图如下：

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=YjYyMzU5M2VlNTViNTdlNThjZmJiN2Y2MzcyMjQzZjZfUE1xSXpRUUd2Zm9RamlzUEdpaHBBdjc1ck9adzVHS2lfVG9rZW46Vm1RUWJDUXFvb044Qkp4WXFlSWNQRHZWbnhmXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

## 3.2 详细设计

> 在DevOps大行其道的今天，运维监控不再是运维工程师的工作，而是程序员和架构师的必备技能。希望大家能够熟练掌握。

### 3.2.1 WebApp模块

在概要设计中，我们定义了WebApp模块的基本功能：登录、监控信息展示、用户信息管理、团队管理。下面将会详细地对WebApp模块的项目架构进行描述。

监控前端是基于nextjs框架实现的一个单页面应用，主要分为六个模块：api、app、components、public、types、utils，下面依次介绍一下这六个模块具体的内容：

1. api 模块：
   1. api 模块主要用于管理与后端服务器进行数据交互的 API 请求。
   2. 包含各种 API 请求方法，如获取主机信息、获取技术栈信息、获取服务信息等。
   3. 通过封装各种 API 请求函数，方便在其他模块中调用，并与后端服务器进行数据传输。
2. app 模块：
   1. app 模块是监控前端的主要应用程序模块，包含了整个单页面应用的页面路由和布局。
   2. 包括各个页面的展示逻辑、数据处理逻辑以及页面之间的跳转和交互逻辑。
   3. 负责主要的用户界面展示和交互功能，与其他模块进行协作，展示监控数据和用户信息。
3. components 模块：
   1. components 模块包含了监控前端的各种可复用组件，如表格组件、图表组件、按钮组件等。
   2. 每个组件负责一个特定的功能或展示样式，可以在不同页面中多次复用。
   3. 组件的设计和封装遵循组件化原则，方便开发者维护和使用。
4. public 模块：
   1. public 模块用于存放公共资源文件，如图片、字体等静态资源。
   2. 这些资源文件可以在页面中直接引用，用于美化页面样式和增强用户交互体验。
5. types 模块：
   1. types 模块定义了监控前端项目中使用的各种类型和接口数据结构。
   2. 包括监控数据的类型定义、接口返回数据的结构定义、用户信息的数据结构等。
   3. 通过定义类型和数据结构，提高代码的可读性和可维护性，减少数据类型错误带来的问题。
6. utils 模块：
   1. utils 模块包含了监控前端项目中使用的各种工具函数和辅助函数。
   2. 包括数据处理函数、日期格式化函数、请求封装函数等。
   3. 这些工具函数可以在整个项目中被共享和复用，提高代码的效率和可维护性。

通过以上六个模块的划分，监控前端项目结构清晰，功能模块化，方便开发者进行代码编写和模块扩展。每个模块负责不同的功能和任务，协同工作，共同构建出一个完整的监控前端单页面应用。

暂时无法在飞书文档外展示此内容

介绍完前端的项目结构，接下来主要介绍一下前端的页面结构，监控系统前端路由主要分为：login、dashboard、user、team几个模块。下面会对这几个页面的设计进行介绍：

1. Login 页面：
   1. Login 页面是用户登录页面，用户需要输入用户名和密码进行身份验证。
   2. 提供登录表单，包括用户名输入框、密码输入框和登录按钮。
   3. 实现用户身份验证功能，用户输入正确的用户名和密码后可以成功登录系统。
2. Dashboard 页面：
   1. Dashboard 页面是监控系统的主页面，展示主要的监控数据和信息。
   2. 包含主机信息展示、技术栈和服务信息展示，以及数据可视化图表展示等内容。
   3. 提供用户友好的界面交互效果，如数据更新、图表刷新等功能。
3. User 页面：
   1. User 页面是用户个人信息管理页面，用户可以查看和编辑个人信息。
   2. 展示用户的基本信息，如头像、用户名、邮箱等，并提供修改功能。
   3. 实现密码修改功能，用户可以通过页面进行密码的修改操作。
4. Team 页面：
   1. Team 页面是团队信息管理页面，展示团队成员列表和权限管理功能。
   2. 包含团队成员的姓名、角色、联系方式等信息展示，团队管理员可编辑成员权限。
   3. 提供添加成员功能，团队管理员可以通过页面添加新成员到团队中。

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTAxYTA4MGNmMzNhYjkxNmY5NGE3Y2VkYTk1ODQ0ZjNfdFVNNnFGNUpRTU96MkJvcUN1c2gwTkUzR3lXTzBGZ2pfVG9rZW46SXZMMmJSOGZrb1dabmp4aE54QmN5R2FOblJjXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. 基本的展示功能：有图表，有颜色

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=ODEwZTgyZDQ1MTdhYTAzN2EwNWQ0OWNhMmVhOTE1YzFfY3NxRklDdExCZHRsY3EwZTdhN01jUzFMY3I1ZHBvTTFfVG9rZW46THpJdmJYU1Rlb1d2bTV4YnpGamNZQlNibjhnXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. 进阶功能：图片聚焦显示细节：鼠标移动过去之后显示详细信息

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=OWU0MTZiNWVhYjdlMmNhOTViNGE4YWU0ZGIwYTk4MjNfTk03elNOdWtGTlVLVllJZEFKOVB4czVhV1VMc1d4dWNfVG9rZW46V3JqM2JDalVFb3dka1J4eU9DN2NFaHRubm1oXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. 可选功能：
2. 1️⃣支持将不同的metric选中之后在同一张图表中展示（像上面的那样，可以有多条线）
3. 2️⃣支持多张图聚焦同一时刻，大概是下面这样，当鼠标移动到其中一张图的时候，其他图也要有同一时刻的线条

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=YWUwMjg4MDMzZWY0NTViYzlkMjA2NjI4ZDE3YTU3MTNfMEVkZVV6a2xtcUNLaHBkUGRZd0lVNWU5SkZsYnRlU0ZfVG9rZW46TU1YMWJOTlVkb0czNmN4T2x0VmNzMXVLbmtWXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

### 3.2.2 Server模块

#### 3.2.2.1 Server模块系统架构

##### 3.2.2.1.1 总体架构概述

监控服务端是一个基于Spring Boot的单体应用，设计为一个统一的服务器，承担接收、处理和管理来自各个Agent的监控数据的任务。它通过REST API与前端进行通信，确保用户可以实时查看和分析监控数据。整个系统架构旨在保证数据的完整性和安全性，同时提供高效的数据处理和分发功能。

##### 3.2.2.1.2 组件描述

- **数据管理与存储模块**：
  - **MySQL**：用于存储关系型数据，如用户信息、团队信息、配置信息等。MySQL提供了数据持久化的可靠解决方案，确保重要业务数据的安全和可恢复性。
  - **InfluxDB**：专门用于存储时间序列数据，如各类监控指标。这种数据库能够高效地处理和查询大量的时间序列数据，适合实时监控的需求。
  - **Redis**：用于存储一些会话数据、配置信息等需要快速访问的数据。
  - **OSS（对象存储服务）**：用于存储大文件和备份数据。OSS提供了高可靠性和高可扩展性的存储解决方案，适合存储大量非结构化数据。
- **通信接口模块**：
  - **与Web前端的通信接口**：通过REST API与Web前端进行通信，接收前端的请求并返回相应的数据。这个接口负责处理用户的操作请求，如数据查询、配置修改等。
  - **与Agent的通信接口**：接收来自各个Agent的监控数据。这个接口需要处理大量的并发请求，确保数据能够及时准确地接收和处理。
- **数据处理与分发模块**：
  - **数据处理**：对接收到的监控数据进行实时处理和分析，提取有效信息并存储到适当的数据库中。
  - **数据分发**：将处理后的数据及时分发到Web前端以供展示。该模块确保监控数据能够实时更新，提供准确的系统状态信息。
- **账号和团队管理模块**：
  - **用户账号管理**：提供用户注册、登录、权限分配等功能，确保用户可以安全地访问系统。
  - **团队管理**：支持团队的创建、成员添加和移除、权限管理等功能，帮助企业有效管理团队和用户权限。
- **第三方登录模块**：
  - **支持的第三方平台**：实现与常见第三方平台（如Google、GitHub、Facebook等）的集成，支持OAuth2.0协议，简化用户登录流程。
  - **用户信息同步**：在用户通过第三方平台登录时，同步获取用户的基本信息，确保系统中的用户信息完整和一致。
- **安全与异常处理模块**：
  - **用户身份验证和权限控制**：确保只有经过授权的用户才能访问系统，并根据用户角色和权限控制其操作范围。
  - **数据加密**：在数据传输和存储过程中使用加密技术，保护敏感信息。
  - **异常处理机制**：设计容错机制，捕获并处理系统运行中的异常情况，确保系统的稳定性和可靠性。
  - **日志记录与监控**：记录系统运行日志，提供故障排查和性能监控的基础。
- **警告通知模块**：
  - **警告规则定义**：允许用户定义监控指标的警告阈值和规则。用户可以根据业务需求设定不同的告警条件。
  - **实时监控和警告触发**：持续监控各项指标，当某项指标超过预设阈值时立即触发警告。
  - **通知机制**：通过多种渠道（如短信、电子邮件、推送通知等）向用户发送警告信息，确保用户能够及时收到重要警报。

通过以上组件的协同工作，监控服务端能够高效、稳定地接收、处理和管理监控数据，为用户提供全面的系统监控解决方案。每个组件都有明确的职责和功能，相互之间通过定义良好的接口进行通信和数据交互。

#### 3.2.2.2 模块设计

##### 3.2.2.2.1 **数据管理与存储模块**

###### 3.2.2.2.1.1 MySQL 表结构设计

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=Y2M0ZTk2MmIwNWQ5NjhkMjc3MDgwMmZhOWYxYWFlYzJfa2h0VzE3S3pzWUM1TjJacVZ5WThZczlvNkVWTU5RQnhfVG9rZW46Q2Q3SmJ6Yzh6bzVCaE14TUF1TGNlOGFvbmpmXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. t_host

用于存储主机的相关信息。

- id: 主键，唯一标识每一台主机。
- mac: 主机的MAC地址，用于唯一标识网络设备。
- cpu: 主机的CPU信息，以JSON格式存储，包括CPU型号、核心数、频率等。
- memory: 主机的内存大小（以字节为单位）。
- disk: 主机的磁盘空间（以字节为单位）。
- network_card: 主机的网络卡信息，以JSON格式存储，包括网络卡型号、MAC地址等。
- os: 主机的操作系统信息，以JSON格式存储，包括操作系统名称、版本号等。
- last_time: 最后一次上线时间，记录主机最后一次与服务器通信的时间。
- status: 主机状态，标识主机当前的运行状态（如在线、离线）。
- hostname: 主机名，主机的网络标识名称。
- team_id: 外键，所属团队ID，关联到t_team表。

1. t_alarm

用于存储告警信息。

- id: 主键，唯一标识每一条告警信息。
- host_id: 外键，关联到t_host表的主机ID，标识触发告警的主机。
- alarm_type: 告警类型，标识告警的种类（如CPU高负载、内存不足等）。
- threshold_value: 阈值，告警触发的阈值。
- severity: 告警级别，标识告警的严重程度（如低、中、高）。
- duration: 持续时间，告警从触发到解除的持续时间。
- notification_account: 通知账户，接收告警通知的账户信息（如邮箱、电话）。
- created_at: 创建时间，记录告警生成的时间。
- updated_at: 更新时间，记录告警信息的最后更新时间。

1. t_permission

用于存储权限信息。

- id: 主键，唯一标识每一个权限。
- permission_name: 权限名称，标识具体的权限操作（如查看、编辑、删除等）。

1. t_role

用于存储角色信息。

- id: 主键，唯一标识每一个角色。
- role_name: 角色名称，标识角色的具体名称（如管理员、用户、运维人员等）。

1. t_auth

用于存储第三方登录认证信息。

- id: 主键，唯一标识每一条认证记录。
- user_id: 外键，关联到t_user表的用户ID，标识与认证信息对应的用户。
- open_id: 第三方平台的Open ID，标识用户在第三方平台上的唯一身份。
- type: 认证类型，标识第三方平台的类型（如Google、GitHub）。
- bind_time: 绑定时间，记录用户与第三方平台绑定的时间。
- name: 用户在第三方平台上的名称。
- avatar: 用户在第三方平台上的头像URL。

1. t_user

用于存储用户信息。

- id: 主键，唯一标识每一个用户。
- username: 用户名，用户的登录名。
- avatar: 头像URL，用户的头像地址。
- phone_number: 电话号码，用户的联系号码。
- email: 邮箱，用户的电子邮箱地址。
- password: 密码，用户的登录密码。

1. t_team

用于存储团队信息。

- id: 主键，唯一标识每一个团队。
- create_time: 创建时间，记录团队创建的时间。
- name: 团队名称，团队的名字。
- owner: 外键，关联到t_user表的用户ID，标识团队的所有者。
- token: 团队令牌，团队访问和操作的标识令牌。

1. t_role_permission

用于存储角色与权限的关联关系。

- id: 主键，唯一标识每一条角色与权限的关联记录。
- role_id: 外键，关联到t_role表的角色ID。
- permission_id: 外键，关联到t_permission表的权限ID。

1. t_team_user

用于存储团队与用户的关联关系。

- id: 主键，唯一标识每一条团队与用户的关联记录。
- team_id: 外键，关联到t_team表的团队ID。
- user_id: 外键，关联到t_user表的用户ID。

1. t_user_role

用于存储用户与角色的关联关系。

- id: 主键，唯一标识每一条用户与角色的关联记录。
- user_id: 外键，关联到t_user表的用户ID。
- role_id: 外键，关联到t_role表的角色ID。

这个数据库设计涵盖了监控系统中的主机、告警、权限、角色、用户、团队及其关联关系的基本信息。每个表都与特定的功能和数据存储需求相关联，确保了系统的功能性和数据的一致性。通过这些表的设计，可以实现对系统各个方面的有效管理和监控，满足业务需求。

###### 3.2.2.2.1.2 InfluxDB 数据存储设计

1. **总体概述**

InfluxDB作为时间序列数据库，用于存储和管理监控系统中的各种性能指标数据。通过针对特定的`agent_{id}`和`performance_{id}`进行数据存储和查询，以支持高效的实时监控和历史数据分析。

1. **数据库设计**

在InfluxDB中，我们主要存储与监控相关的时间序列数据，包括基本信息如CPU使用率、内存使用率、磁盘IO，以及主机特有信息等。这些数据将用于实时监控和告警系统。

1. **数据库结构**

**Bucket**: `myBucket`

**Measurement**:

- `performance_{id}`
- `agent_{id}`

**Tags**: 用于标识数据的属性

**Fields**: 存储实际的监控数据值

- `cpu_percent`
- `disk_percent`
- `mem_percent`
- `network_rate`
- `tcp_connection`
- `send_data_duration`
- `send_message_counter`

**Timestamp**: 每条数据的时间戳，标识数据的采集时间，使用毫秒级时间戳

1. **示例设计**
   1. **agent_1**:
      - **Tags**: 1
      - **Fields**: `cpu_percent`,`disk_percent`,`mem_percent`,`network_rate`,`tcp_connection`
      - **Timestamp**: 数据采集时间
   2. **measurement_i**:
      - **Fields**: `send_data_duration`, `send_message_counter`
      - **Timestamp**: 数据采集时间
2. **数据写入与查询**
   1. **数据写入**: 各个Agent定时调用服务器将监控数据写入InfluxDB，数据通过HTTP API接口发送到InfluxDB。
   2. **数据查询**: Web前端通过服务器查询InfluxDB中的数据，以展示实时和历史的监控信息。

##### 3.2.2.2.2 第三方登录设计

1. 第三方登录流程如下图所示

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=YTg3YWExOWQ0ZmNiY2Y1YzdhZTE0OTk0NzI0ZjNmMjBfRXRsMlZVTGQ1dFQ5VGxTQ2lpVUxDTnMwbGZ5dTExdk9fVG9rZW46UzJaVWI5bVdJb2lKOXR4OWZTa2N0MDZZbjRjXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

1. 第三方登录步骤如下：

（A）浏览器向后端服务器发起请求，获取第三方认证URL地址

（B）后端服务器返回URL地址，带有client_id、scope、redirect url、state（防止csrf攻击）

（C）浏览器根据向第三方服务器请求认证服务

（D）第三方服务器响应给与认证服务（可能是重定向到认证页面 或者 二维码）

（E）浏览器展示给用户认证页面

（F）用户根据提示输入认证凭证

（G）浏览器根据用户给予的凭证，向第三方服务器发送验证请求

（H）第三方服务器验证凭证，成功则返回授权码

​     (H1) 若重定向到前端

​        （I）前端传递Authorization Code 给后端服务器

​        （J）后端服务器根据（I）中的信息 + secret + client_id 向第三方服务器获取Access Token或用户信息

​        （K）第三方服务器返回用户信息

​        （L1）当后端处理完成，给浏览器授予令牌

​    （H2）若重定向到后端

​        （J）后端服务器根据（H2）中的信息 + secret + client_id 向第三方服务器获取Access Token或用户信息

​        （K）第三方服务器返回用户信息

​        （L2.1）当后端处理完成，返回授权令牌

​        （L2.2）第三方服务器，返回授权令牌到浏览器

##### 3.2.2.2.3 团队与成员管理设计

团队与成员管理模块是系统中负责管理用户和团队相关功能的核心部分。其主要功能包括用户账号的创建、管理和删除，团队的创建、管理和成员权限控制等。通过这一模块，系统能够确保用户的身份验证和权限管理，保障数据的安全性和系统的可靠性。

**团队管理**

- 团队创建：用户可以创建团队，并自动成为团队管理员。创建团队时，系统会生成一个唯一的团队令牌（token），用于Agent的身份认证。
- 团队成员管理：团队管理员可以邀请其他用户加入团队，或移除成员。

**团队创建和管理**：

- 已注册用户可以通过团队管理页面创建团队。
- 创建团队时，系统在 `t_team` 表中插入新记录，并将该用户设置为团队管理员，同时生成一个唯一的团队令牌（token），用于Agent的身份认证。
- 团队管理员可以通过邀请功能添加其他用户加入团队，系统在 `t_team_user` 表中记录关联关系。

**成员管理和权限控制**：

- 团队管理员可以在团队管理页面查看和管理成员列表。
- 管理员可以移除成员，系统删除 `t_team_user` 表中的相关记录。

通过上述设计，团队与成员管理模块能够高效管理用户和团队信息，确保系统的安全性和权限控制。团队创建时生成的唯一令牌（token）用于Agent的身份认证，进一步增强了系统的安全性。这一模块的设计不仅提高了系统的安全性和可靠性，还提升了用户管理的灵活性和便捷性。

##### 3.2.2.2.4 警告通知设计

警告通知模块是系统中用于实时监控和告警的关键部分。其主要功能是定义监控指标的警告阈值和规则，持续监控各项指标，当某项指标超过预设阈值时触发警告，并通过多种方式通知相关人员。这一模块的设计能够帮助运维人员及时发现和解决系统问题，确保系统的稳定运行。

**警告规则定义**

- 阈值设置：允许用户定义监控指标的警告阈值。例如，CPU使用率超过80%时触发警告。
- 告警条件：支持复杂的告警条件设置，如多项指标的组合条件。

**实时监控**

- 数据采集：持续采集系统各项监控指标的数据。
- 实时分析：对采集到的数据进行实时分析，判断是否超过预设阈值。

**告警触发**

- 告警触发：当监控指标超过预设阈值时，系统自动触发告警。
- 告警级别：支持不同级别的告警，如信息、警告、严重等。

**通知机制**

- 多种通知方式：支持短信、电子邮件等多种通知方式。

**模块交互流程**

1. **警告规则设置**：
   1. 用户通过警告规则配置页面定义监控指标的警告阈值和规则。
   2. 系统将这些规则存储在 `t_alert_rule` 表中。
2. **实时监控和告警触发**：
   1. 系统持续采集监控数据，并根据定义的警告规则进行实时分析。
   2. 当监控指标超过预设阈值时，系统在 `t_alert_log` 表中记录告警信息，并触发相应的告警。
3. **通知发送**：
   1. 系统根据定义的通知策略，在 `t_notification` 表中查找相应的通知方式和接收人。
   2. 生成告警通知内容，并通过指定的通知方式（短信、邮件、微信、Slack等）发送给相关人员。
4. **告警处理**：
   1. 运维人员接收到告警通知后，可以通过系统查看详细的告警信息。
   2. 处理完成后，更新 `t_alert_log` 表中的告警状态。

通过上述设计，警告通知模块能够高效、准确地监控系统运行状态，并在出现问题时及时通知相关人员。多种通知方式和灵活的通知策略确保了告警信息能够及时传达给运维人员，有助于快速响应和解决系统问题，从而保障系统的稳定运行。

##### 3.2.2.2.5 接口设计

PiMonitor系统的API设计，涵盖了主机基本信息管理、利用率监控、服务查看与推送、用户管理、第三方登录以及一些通用功能。下面将对各个部分进行概括：

1. **基本信息**：
   1. `GET /api/v1/agents/info`：用于获取主机的基本信息。
   2. `POST /api/v1/agents/register`：用于注册新的主机。
   3. `PUT /api/v1/agents/info`：用于修改主机的基本信息。
2. **利用率**：
   1. `GET /api/v1/agents/utilization`：用于查看主机的利用率。
3. **服务**：
   1. `GET /api/v1/services`：用于查看服务信息。
   2. `POST /api/v1/services/push`：用于推送服务信息。
4. **用户**：
   1. `GET /api/v1/users/login`：用户登录接口。
   2. `POST /api/v1/users/reset-password`：重置密码接口。
   3. `POST /api/v1/users/update-info`：修改用户信息接口。
   4. `GET /api/v1/users/info`：查看用户信息接口。
   5. `POST /api/v1/users/unbind`：解除绑定接口。
   6. `GET /api/v1/users/team-list`：查看所属团队列表接口。
5. **第三方登录**：
   1. `GET /api/v1/social-login`：第三方登录接口。
   2. `GET /api/v1/social-login/providers`：获取支持的第三方登录列表接口。
   3. `POST /api/v1/social-login/bind`：绑定第三方接口。
   4. `DELETE /api/v1/social-login/unbind`：解除第三方绑定接口。
   5. `POST /api/v1/social-login/callback`：第三方登录回调接口。
6. **通用功能**：
   1. `GET /api/v1/common/oss-signature`：获取OSS签名接口。
   2. `GET /api/v1/common/image-url`：获取图片URL接口。
   3. `POST /api/v1/common/send-sms-captcha`：发送短信验证码接口。
   4. `POST /api/v1/common/send-email-captcha`：发送邮件验证码接口。
   5. `POST /api/v1/common/validate-captcha`：验证码验证接口。
   6. `POST /api/v1/common/send-email-notification`：发送邮件通知接口。
7. **团队管理**：
   1. `GET /api/v1/teams/alerts`：获取团队的报警信息。
8. **报警**：
   1. `GET /api/v1/alarms`：获取报警信息。

这些接口的设计遵循了RESTful原则，通过HTTP方法（如GET、POST、PUT、DELETE）来对资源进行操作。每个接口都有其特定的请求参数和头信息，用以完成不同的功能。文档中还提供了示例代码和可能的响应状态码，使得开发者能够更容易地理解和使用这些接口，详细文档信息请访问https://7d1lqyqrq5.apifox.cn。

### 3.2.3 Agent模块

在概要设计中，我们定义了Agent模块的基本功能：扫描端口、采集计算机信息并且与server通信。下面将会详细地对Agent模块的项目架构进行描述。

Agent模块的项目架构分为六个包：分别是main, config, entity, logic, repo和utils。下面将一一介绍这六个包。

main包负责程序的主要控制逻辑，包括控制程序的启动和结束。在本次设计中，有两种情况会导致程序启动失败：缺失token或者采集计算机基础信息失败（例如一些gopsutil不兼容的计算机系统或硬件）。在程序启动失败之后，会由context通知其他的协程停止工作，防止出现僵尸协程。

logic负责三个协程的处理逻辑。在main包启动的时候，会读取配置并启动baseinfo, performance和metric三个协程，并且传入一个contextWithCancel用于协程通信。这三个协程将会并行地采集不同的数据,一旦基础信息采集失败，那么就会发出一个取消信号，通知其他两个协程停止工作。否则，其余两个协程将会一直采集信息，知道计算机关闭Agent进程。

config包主要负责读取启动的配置信息，包括读取环境变量，配置日志信息等等。entity包负责定义其他包所需要使用到的基本信息。repo包负责调用外部接口，与server模块进行通信，上传数据等。utils包定义了一些特定的工具，例如带有超时时间的context，用于控制扫描端口的时候的并发逻辑。

暂时无法在飞书文档外展示此内容

下面是Agent模块的时序图：主函数启动之后，分别启动三个协程，获取不同的指标。

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWVlOTA0NjlmZTRiYzE4Mzg1MGI4ZDcxODdjMTc4NjhfWkh0MHRvSkNGMlNtSTlMQk44UXFDdW5wQUJHMGJlSWRfVG9rZW46SkhhY2J1V3M4b2hTcE94RGRsNGN3NGJ2bmloXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

在获取系统资源信息的时候，需要使用一个叫做psutil(process and system util)的第三方工具，用于监测系统资源的利用率。在go语言中，对应的工具叫做gopsutil，在本项目中，获取计算机的CPU信息，内存信息，网络状况等都是基于这个工具的。

### 3.2.4 PiMetric模块

PiMetric模块是用户在开发程序的时候可以导入的模块，用户可以使用这个模块定义和采集不同的metric信息，这里的设计主要包括三种metric信息：counter，gauge和histogram。

从项目管理上看，PiMetric模块其实是一个独立的模块。本项目采用大仓管理，WebApp、Server和Agent处于同一个仓库下，但是由于PiMetric模块是需要被外部导入并且使用的，所以单独作为一个仓库。

PiMetric的项目结构如下：其中的api包定义了一些基本的接口，metrics包定义了三种类型的metric，相比于Promethues的八种metrics，这里提取了最常用的三种，其余的信息都可以使用这三种metric表示出来。store包保存了本机记录的信息，当外部调用获取信息的接口的时候，会从store包读取数据。

暂时无法在飞书文档外展示此内容

为了简化Prometheus的设计并提高易用性，我们对模块进行了改进，以确保在不丢失信息的情况下简化操作。尽管Prometheus在度量指标设计方面是一个标杆，但其复杂的理念导致了不太友好的使用体验。因此，我们在尽量不丢失信息的前提下对直方图（histogram）类型的数据进行了简化。

具体而言，我们将直方图中的"桶"概念修改为"漏斗"，通过使用数组来存储该类型的度量指标数据。在传输数据时，如果成功传输，就会将本地的数据清除，就好像数据从漏斗中流出一样。这种简化的方法可以在减少复杂性的同时保留核心信息。

最后，我们提供了两种对外暴露接口的方式：

1. 启动一个HTTP服务并监听特定端口：这种方式适用于那些不对外提供HTTP接口的服务，例如RPC服务或其他类型的服务。
2. 提供一个处理函数（Handler）并注册到已有的HTTP服务中：这种方式适用于已有HTTP端口的服务。通过将处理函数注册到现有的HTTP服务中，我们可以在不修改现有服务结构的情况下，通过访问特定的HTTP路径来获取监控数据。

以上两种方式可供用户根据实际需求选择，以满足不同场景下的业务监控要求。需要注意的是，本次项目只开发了go语言版本的metric采集包，并且作为一个go module托管到github，因此用户的项目必须是使用go mod管理的。不过由于go mod做包管理在go语言项目中占有绝对优势，因此可以认为这个包适用于go语言的绝大多数场景。

## 3.3 架构总成

监控系统的整体架构图如下：

暂时无法在飞书文档外展示此内容

# 四、编码

## 4.1 WebApp模块

1. **技术栈**

- **编程语言**：Typescript
- **框架**：Nextjs+React+Tailwindcss
- **三方库**：antd、echarts、axios等
- **serverless部署**：vercel+cloudflare+aliyun

1. **代码示例**

**示例 1：处理第三方登录回调**

```JavaScript
"use client";
import {useEffect} from "react";
import {ThirdPartyCallback} from "@/api/thirdParty";
import {useRouter} from "next/navigation";
import {AuthInfo, UserInfo} from "@/types/user";
import {setupToken} from "@/utils/AuthUtils";

export default function CallbackPage() {

    // 初始化router
    const router = useRouter();

    // 将用户信息存到Localstorage中
    const saveUserInfo = (user: UserInfo) => {
        localStorage.setItem("userInfo", JSON.stringify(user))
    }

    // 将授权信息存到Localstorage中
    const saveAuthInfo = (auths: AuthInfo[]) => {
        localStorage.setItem("auths", JSON.stringify(auths))
    }

    useEffect(() => {
        // 使用原生方法从?code=xxx中获取code
        function getUrlParameter(name: string) {
            name = name.replace(/[[]/, "\$$ ").replace(/[ $$]/, "\\]");
            let regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
            let results = regex.exec(window.location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
        }

        // 存储code和state
        let code = getUrlParameter('code');
        let state = getUrlParameter('state');

        // 处理第三方回调
        const handleCallback = async () => {
            try {
                // 向服务端发生校验数据并接受回调信息
                const {jwt, auths, user} = await ThirdPartyCallback({
                    type: window.location.pathname.split('/')[1],
                    code,
                    state
                });
                // 数据本地化存储
                setupToken(jwt);
                saveUserInfo(user);
                saveAuthInfo(auths);
                // 跳转到首页
                router.push('/');
            } catch (e) {
                console.error(e);
            }
        }

        // 非空校验
        if (code && state) {
            handleCallback().then()
        }
    }, []);

    return (
        <div className="w-full h-full flex items-center justify-center">
            <h1 className="text-2xl">正在校验第三方授权...</h1>
        </div>
    );
}
```

**示例 2：渲染主机信息**

```JavaScript
"use client";
import React, {useEffect} from 'react';
import {AgentInfoType} from "@/types/agent";
import { Descriptions } from 'antd';
import type { DescriptionsProps } from 'antd';

type AgentInfoProps = {
    agentInfo: AgentInfoType
}

const AgentInfo: React.FC<AgentInfoProps> = ({agentInfo}) => {

    // 将字节转换为GB
    function bytesToGB(bytes: number) {
        if (bytes === 0) return "0 GB";
        const gb = bytes / (1024 * 1024 * 1024);
        return gb.toFixed(2) + " GB";
    }
    
    // 用于存储Descriptions组件的数据
    const [items, setItems] = React.useState<DescriptionsProps['items']>([]);
    
    // 构造符合Descriptions组件的数据结构
    useEffect(() => {
        console.log('agentInfo:', agentInfo);
        if (agentInfo) {
            const data = [
                {
                    key: '1',
                    label: 'agentID',
                    children: agentInfo.id,
                },
                {
                    key: '2',
                    label: '主机名',
                    children: agentInfo.hostname,
                },
                {
                    key: '3',
                    label: 'cpu型号',
                    children: `${agentInfo.cpu?.cpuName} ${agentInfo.cpu?.core}核 ${agentInfo.cpu?.frequency}`,
                },
                {
                    key: '4',
                    label: '内存空间',
                    children: bytesToGB(agentInfo.memory),
                },
                {
                    key: '5',
                    label: '磁盘空间',
                    children: bytesToGB(agentInfo.disk),
                },
                {
                    key: '6',
                    label: 'mac地址',
                    children: agentInfo.mac,
                },
                {
                    key: '7',
                    label: '操作系统',
                    children: agentInfo.os,
                },
                {
                    key: '8',
                    label: '上次在线时间',
                    children: agentInfo.lastTime,
                },
                {
                    key: '9',
                    label: '状态',
                    children: agentInfo.status,
                },
                {
                    key: '10',
                    label: '网卡',
                    children: agentInfo.networkCard?.map((card) => <span key={card}>{card} </span>),
                }
            ];
            setItems(data)
        }
    }, [agentInfo]);
    return (
        <div className="w-full h-full flex items-center justify-start my-2">
            <Descriptions bordered items={items}/>
        </div>
    );
}

export default AgentInfo
```

**示例 3：团队管理相关接口**

```JavaScript
import {authDel, authGet, authPost, authPut} from "@/utils/AuthUtils";
import {TeamMember} from "@/types/team";

/**
 * 创建团队
 * @param {string} teamName
 */
export const CreateTeam = authPost<{
    teamName: string
}, {
    token: string
}>('/v1/team')

/**
 * 修改团队信息
 * @param {string} teamID
 * @param {string} teamName
 */
export const UpdateTeam = authPut<{
    teamID: string,
    teamName: string
}, {}>('/v1/team?teamID=:teamID&teamName=:teamName')

/**
 * 解散团队
 * @param {string} teamID
 */
export const DismissTeam = authDel<{
    teamID: string
}, {}>(`/v1/team?teamID=:teamID`)

/**
 * 查看团队信息
 * @param {string} teamID
 */
export const GetTeam = authGet<{
    teamID: string
}, {
    id: string,
    createName: string,
    name: string,
    owner: string,
    token: string
}>('/v1/team')

/**
 * 查看团队成员列表
 * @param {string} teamID
 * @param {string} page
 * @param {string} size
 */
export const GetTeamMembers = authGet<{
    teamID: string,
    page: string,
    size: string
}, {
    records: TeamMember[],
    total: number
    size: number,
    current: number,
    pages: number
}>('/v1/team/members')

/**
 * 邀请
 * @param {string} teamID
 * @pathParam {string} type
 */
export const Invite = authGet<{
    teamID: string
    type: string
}, {
    Code: string
}>('/v1/team/invite/:type')

/**
 * 邀请回调
 * @param {string} code
 */
export const InviteCallback = authGet<{
    code: string
}, {}>('/v1/team/invite/callback')
```

1. **编码规范**
   1. **类型校验**：提取公共数据类型，使用ts进行类型校验。
   2. **注释规范**：在重要的接口、方法和逻辑片段添加注释。
   3. **代码格式**：使用统一的缩进和换行。
   4. **模块化**：提取公共组件，降低代码耦合性。

## 4.2 Server模块

1.  **技术栈**

- **编程语言**：Java
- **框架**：Spring Boot、MyBatis Plus
- **数据库**：MySQL、InfluxDB、Redis
- **中间件**：Kafka
- **云存储**：OSS（对象存储服务）
- **依赖管理**：Maven

1. **代码示例**

**示例 1：接收并处理监控数据**

```Java
@Slf4j
@RestController
@RequestMapping("/v1/agents/services/info")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MetricController {
    private final KafkaRepo kafkaRepo;
    private final InformationService informationService;

    /**
     * 提交监控信息
     * @param teamID 团队ID
     * @param agentID 主机ID
     * @param list 监控信息
     * @return ResultCode.SUCCESS 提交成功
     */
    @PostMapping
    public Result<Object> postMetricInfo(@GetAttribute("teamID") @NotNull Integer teamID, @RequestParam String agentID, @NotNull @RequestBody List<InfluxDBPoints> list) {
        // 保存到kafka
        kafkaRepo.produce("metric", JSONObject.toJSONString(list));
        // 更新主机状态
        informationService.updateTime(agentID);
        return ResultUtils.success();
    }
}
```

**示例 2：数据存储到 InfluxDB**

```Java
@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InfluxDBRepo {
    private final InfluxDBClient influxDB;

    /**
     * 写入数据
     * @param data 数据
     * @return 是否成功
     */
    public boolean write(String data) {
        try {
            WriteApiBlocking writeApi = influxDB.getWriteApiBlocking();
            writeApi.writeRecord(WritePrecision.NS, data);
            return true;
        } catch (Exception e) {
            log.error("InfluxDB 写入数据失败", e);
            return false;
        }
    }

    /**
     * 写入数据
     * @param measurement 表名
     * @param tags 标签
     * @param fields 字段
     * @param timestamp 时间戳
     * @param precision 时间精度
     * @return 是否成功
     */
    public boolean write(String measurement, Map<String, String> tags, Map<String, Object> fields, long timestamp, WritePrecision precision) {
        try {
            Point point = Point.measurement(measurement)
                    .addTags(tags)
                    .addFields(fields)
                    .time(timestamp, precision);
            WriteApiBlocking writeApi = influxDB.getWriteApiBlocking();
            try {
                writeApi.writePoint(point);
                return true;
            } catch (Exception e) {
                log.warn("数据异常", e);
                return true;
            }
        } catch (Exception e) {
            log.error("InfluxDB 写入数据失败", e);
            return false;
        }
    }

    /**
     * 查询数据
     * @param flux Flux 查询语句
     * @return 查询结果
     */
    public List<FluxTable> query(String flux) {
        return influxDB.getQueryApi().query(flux);
    }

    /**
     * 解析查询结果
     * @param tables 查询结果
     * @return 解析后的结果
     */
    public Map<String, List<Map<String, Object>>> parse(@NotNull List<FluxTable> tables) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        tables.forEach(table -> {
            table.getRecords().forEach(record -> {
                // 获取一条记录
                String field = record.getField();
                Instant time = record.getTime();
                Object value = record.getValue();
                if (!result.containsKey(field)) {
                    result.put(field, new LinkedList<>());
                } else {
                    assert time != null;
                    assert value != null;
                    result.get(field).add(Map.of("time", time.getLong(ChronoField.INSTANT_SECONDS) * 1000 + time.get(ChronoField.MILLI_OF_SECOND), "value", value));
                }
            });
        });
        return result;
    }
}
```

**示例 3：团队管理**

```Java
@Slf4j
@RestController
@RequestMapping("/v1/team")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TeamController {
    private final TeamService teamService;

    /**
     * 创建团队
     * @param userID 用户ID
     * @param data 团队名称
     * @return ResultCode.SUCCESS 创建成功 ResultCode.OPERATION_ERROR 操作失败
     */
    @PostMapping("")
    public Result<Object> create(@GetAttribute("userID") @NotNull String userID, @RequestBody @NotNull JSONObject data) {
        String teamName = data.getString("teamName");
        Team team = teamService.create(Long.parseLong(userID), teamName);
        if (team == null) {
            return new Result<>(ResultCode.OPERATION_ERROR);
        }
        return ResultUtils.success(team);
    }

    /**
     * 修改团队信息
     * @param userID 用户ID
     * @param teamID 团队ID
     * @param teamName 团队名称
     * @return ResultCode.SUCCESS 修改成功 ResultCode.NO_AUTH_ERROR 无权限
     */
    @PutMapping("")
    public Result<Object> modify(@GetAttribute("userID") @NotNull String userID, @RequestParam String teamID, @RequestParam @NotNull String teamName) {
        boolean modify = teamService.modify(userID, teamID, teamName);
        return modify ? ResultUtils.success() : ResultUtils.error(ResultCode.NO_AUTH_ERROR);
    }

    /**
     * 获取团队成员列表
     * @param userID 用户ID
     * @param teamID 团队ID
     * @return ResultCode.SUCCESS 获取成功 ResultCode.NO_AUTH_ERROR 无权限 ResultCode.NOT_FOUND_ERROR 未找到
     */
    @GetMapping("/members")
    public Result<Object> members(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String teamID,
                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        IPage<Map<String, Object>> members = teamService.members(userID, teamID, page, size);
        if (members == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return members == null ? ResultUtils.error(ResultCode.NO_AUTH_ERROR) : ResultUtils.success(members);
    }

    /**
     * 获取团队信息
     * @param userID 用户ID
     * @param teamID 团队ID
     * @return ResultCode.SUCCESS 获取成功 ResultCode.NO_AUTH_ERROR 无权限 ResultCode.NOT_FOUND_ERROR 未找到
     */
    @GetMapping("")
    public Result<Object> info(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String teamID) {
        Team info = teamService.info(userID, teamID);
        return info == null ? ResultUtils.error(ResultCode.NO_AUTH_ERROR) : ResultUtils.success(info);
    }
}
```

1. **编码规范**
   1. **命名规范**：使用驼峰命名法命名变量、方法和类，使用全大写字母和下划线命名常量。
   2. **注释规范**：在重要的类、方法和逻辑片段添加注释。使用 Javadoc 标准编写类和方法的注释。
   3. **代码格式**：遵循 Google Java Style Guide，使用一致的缩进和空格。
   4. **提交规范**：提交信息需简明扼要，描述所做的变更。推荐使用动词开头，如“feat”、“fix”等。

## 4.3 Agent模块

1. 错误处理：

主函数必须处理所有可能出现的错误，不可忽略错误的返回值。在处理错误的时候也不必直接终止程序运行，而是使用日志记录，必要时加以警告，这样做是为了提高系统的可用性，即便业务系统挂了，监控系统也还可以运行。

```Go
go func() {
        err := metric.MonitorMetric(ctx)
        if err != nil {
            log.Println("get metrics info error")
        }
    }()
```

1. 并发执行：

部分地方并发执行，例如，在Agent扫描本机端口的时候，各个端口之间的数据互不干扰，于是可以将其改为并发执行。此外，获取基本信息，系统资源利用率和metrics信息三个过程也是并发执行的。

```Go
func Scan(pArr []int) []*pimetric.Metricx {
    var mutex sync.Mutex
    rsp := make([]*pimetric.Metricx, 0)
    wg := &sync.WaitGroup{}
    // 设置等待的任务数量
    for _, port := range pArr {
        wg.Add(1)
        go func(p int) {
            defer wg.Done()
            url := "http://127.0.0.1:" + strconv.Itoa(p) + "/metrics"
            method := "GET"
            client := &http.Client{}
            // ... 业务逻辑省略
            // 注意并发安全
            mutex.Lock()
            defer mutex.Unlock()
            rsp = append(rsp, result)
        }(port)
    }
    // 防止网络请求超时，设置1秒timeout
    utils.WaitGroupWithTimeout(wg, time.Second)
    return rsp
}
```

1. 自行封装带有超时时间的WaitGroup：

Go在处理并发操作的时候有天然的优势，使用WaitGroup可以很方便地同步不同的协程之间的状态，但是现实业务中在业务同步的时候，可能会有一个协程接口请求超时，所有协程都在阻塞的情况。所以，为了降低这种情况的影响，我们在项目中封装了一个带有超时时间的WaitGroup，避免了WaitGroup陷入长期等待的情况，这个封装在多处并发操作中都有使用，例如上面的并行代码片段。WaitGroupWithTimeout的具体实现如下：

```Go
// WaitGroupWithTimeout 创建一个带有timeout的wg，返回值：
// true：wg正常完成，false：wg超时
func WaitGroupWithTimeout(wg *sync.WaitGroup, timeout time.Duration) bool {
    c := make(chan struct{})
    go func() {
        defer close(c)
        wg.Wait()
    }()

    select {
    case <-c:
        return true // 正常完成
    case <-time.After(timeout):
        return false // 超时
    }
}
```

## 4.4 PiMetric模块

1. 高健壮性：

PiMetric模块最重要的就是注重健壮性，这里展示了获取某个counter的代码片段，这段代码在操作map的使用了锁，防止并发操作带来的不一致性。另外，这里也直接做了零值处理，在获取Counter的时候，如果Counter不存在，那么直接创建一个计数为0的counter，这样外层使用的时候不需要监测key的存在再从map中获取，极大地提高了系统的健壮性。

```Go
// CounterOf 获取counter
func CounterOf(metricName string) *counter.Counter {
    lock := lockMap[COUNTER_KEY]
    lock.Lock()
    defer lock.Unlock()
    if value, ok := CounterMap[metricName]; ok {
        return value
    }
    rsp := &counter.Counter{
        Name:  metricName,
        Value: 0,
    }
    CounterMap[metricName] = rsp
    return rsp
}
```

1. 轻量化、低依赖：

PiMetric模块追求简单实用，这个模块只使用了Go语言标准包，系统适用性强，并且也不存在依赖冲突。在实战中，我们发现，有一些包对于不同的系统支持程度不同，例如agent使用的gopsutil，在不同的系统上实现不一样，我们尽量避免了这种情况的发生，于是直接自己定义了一套metric记录系统，使用最基本数据结构记录数据，网络接口也是使用了标准的net包，适用于绝大多数系统。

![img](https://mvt4hz3oai.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDliNmNlNTUwNGYyMzc2MmRhM2VjNTJlNGEzZTRkYThfN3JWVWJFbGpEODRURXFoaDNzenN3MDZ4bm1XcFdHRDlfVG9rZW46RHNia2IwYVpib25CQ294MkZadGNVTktnbmNmXzE3MjA1NDI5MzM6MTcyMDU0NjUzM19WNA)

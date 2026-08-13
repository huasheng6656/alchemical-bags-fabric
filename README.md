# 炼金术之袋（Fabric 26.2）

为 Minecraft Java 版 26.2 Fabric 制作的独立炼金术之袋模组。

本模组参考 ProjectE 中炼金术之袋的使用体验，仅提供便携存储功能，不包含 EMC、物质转换、贤者之石或转换桌等 ProjectE 系统。

## 功能

- 提供 16 种颜色的炼金术之袋
- 每个袋子拥有 90 行、共 810 个物品栏槽位
- 使用 Huge Storage 90 的滚动分页界面
- 袋内物品保存在袋子物品自身的数据中
- 不同袋子拥有各自独立的存储内容
- 禁止将炼金术之袋放入另一个炼金术之袋，避免递归存储
- 支持旧版 54 格袋子内容自动迁移
- 支持中文和英文名称

## 运行环境

- Minecraft Java 版 26.2
- Java 25 或更高版本
- Fabric Loader 0.19.3 或更高版本
- Fabric API 0.155.0+26.2 或更高版本
- [Huge Storage 90](https://github.com/huasheng6656/huge-storage-90) 1.1.4 或更高版本

Huge Storage 90 本身可独立运行。需要 Carpet TIS Addition 大型木桶兼容时，再安装 Carpet TIS Addition 及 Fabric Carpet。

## 安装方法

1. 安装 Minecraft 26.2、Fabric Loader 和 Fabric API。
2. 安装 Huge Storage 90 及其依赖。
3. 从 [Releases](https://github.com/huasheng6656/alchemical-bags-fabric/releases) 下载模组 JAR。
4. 将 JAR 放入当前游戏实例的 `mods` 文件夹。
5. 重新启动游戏。

客户端与服务端都需要安装本模组及其依赖。

## 使用方法

手持炼金术之袋并点击右键，即可打开 810 格存储界面。

界面每页显示 6 行，可以使用鼠标滚轮或右侧滚动条切换页面。

## 合成配方

每种颜色均使用对应颜色的羊毛：

```text
钻石    钻石    钻石
羊毛    箱子    羊毛
羊毛    羊毛    羊毛
```

例如，使用白色羊毛会合成白色炼金术之袋，使用红色羊毛会合成红色炼金术之袋。

## 从源码构建

需要安装 Java 25。

Windows：

```powershell
.\gradlew.bat build
```

Linux 或 macOS：

```bash
./gradlew build
```

构建产物位于：

```text
build/libs/alchemical-bags-fabric-26.2-1.1.0.jar
```

构建过程不依赖本地 Minecraft 整合包目录。

## 开源许可

模组源代码使用 [MIT License](LICENSE) 发布。

炼金术之袋纹理改编自 ProjectE 1.19.2。ProjectE 声明使用 MIT License；相关来源与署名见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

## 声明

本项目不是 ProjectE 官方版本，也不隶属于 ProjectE、Mojang Studios 或 Microsoft。



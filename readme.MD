---
title: "DSBB Project 说明文档"
date: 2023/5
geometry: margin=3cm
output: pdf_document
---

## 使用方法
请按照本节方法来启动程序，否则会出现找不到类等错误，运行方式的参数也是必须的，否则程序会直接退出。

- 使用IDE启动应选择`Main`作为主类，并设置好参数，参数可以是大小写任意的：
  - Terminal
  - GUI
- 使用命令行启动请先进入目录`target`，**请不要使用您的系统已有的java环境**，建议的命令如下所示：
```shell
cd .\\target\\
.\\app\\bin\\java edu.dsbbproj.nikoinmaze.Main gui
.\\app\\bin\\java edu.dsbbproj.nikoinmaze.Main terminal
```
由于没有编译环境，暂时无法提供`Linux`或`Mac OS X`的`jlink`运行时。运行时在`Windows 11 x64`下制作，在相近版本的`x64`系统下应当都能运行，且`app.zip`中实际上已经包含了所有的类。而`target`目录下也存放了类文件，若是能配置好`openjfx`，也能使用本地的`JRE`运行，而`terminal`模式是可以直接运行的，无需另外配置。若是无法使用预配好的运行时，也无法使用类文件，也可以直接打开工程用IDE直接运行。大小超过 $50\times50$ 的迷宫不建议使用`GUI`运行，此时的格子已经很小，不适合人眼观察。当然这个要根据屏幕大小变化，经过实测在27英寸`2560*1440`的屏幕上刚好能看清 $100\times100$ 的迷宫。同时大型迷宫也建议使用重定向进行输入，不然真是太折磨了。程序严格使用标准输入输出，可以放心使用重定向。我们也制作了随机生成可解输入，他们有自己的主程序，通过类似的方式使用：
```shell
.\\app\\bin\\java edu.dsbbproj.nikoinmaze.generateMaze 
    > yourpath\\test.txt
.\\app\\bin\\java edu.dsbbproj.nikoinmaze.generateMazeIntensive 
    > yourpath\\test.txt
```
程序并不是并行的，对于 $500\times500$ 以上的迷宫生成十分缓慢，若是想要体验请一定耐心等待。

## 程序结构

### 源码与基本结构
所有源代码存放于`src/java/main`下，所有`.java`文件均属于包`edu.dsbbproj.nikoinmaze`的内容，没有其他文件结构。`Main`类为程序入口，决定程序运行方式，`Maze`中存放迷宫的所有信息，运行时以一个静态实例存在，是为了方便处于不同线程的GUI程序读取，同时文件中也实现了`Point`类，该类在寻路中起到了关键作用。`ARAStarMazeSolver`类中有程序的核心算法。`generateMaze`和`generateMazeIntensive`用于生成随机可解输入，后者生成的迷宫更密集。余下的两个类为GUI相关的类，使用的是`openjfx`，没有使用额外的库。  
整个工程使用 *Maven* 来管理，同时使用其`jlink`插件来制作运行时。

### 数据结构与算法
经过讨论后发现文献中的算法并不完全适用。我们的程序会改变起点，且迷宫在中途会发生变化，所以没有使用上一次搜索产生的$g(s)$ 和连接关系。在文献中 $\epsilon$ 的变化是根据 openset 中的$f(s)$变化的，但我们需要其按时间递减。我们的算法每一次搜索都使用新的 openset 但仍然保留了 closeset 来记录已经访问过的点。在算法我们使用了两种特殊的数据结构来加速代码运行：
```java
PriorityQueue<Point> openSet = new PriorityQueue<>(Comparator.comparingDouble(a -> a.f));
Set<Point> closedSet = new HashSet<>();
```

### GUI 程序
主要使用`javafx`的基础功能，加入了高斯模糊实现半透明的效果，迷宫格子的大小是随着迷宫大小动态变化的，同时整个窗口也是自适应的，可以随意拖动窗口而仍然保持适当的显示效果。且迷宫的内容是和每个格子绑定的，一旦发生变化都会立刻显现出来。

### 随机生成
目前的随机生成可以保证存在唯一解。  
当用户运行`generateMaze.java`的`main`程序时，只需输入行数`n`和列数`m`即可生成一组随机输入，该输入可以直接作为project主程序的输入。

生成的思路如下：

> 1. 随机生成大于迷宫栅格数量的`e`，保证步数足够走出结果
> 2. 随机生成攻击次数`p`和初始地图障碍数目，这部分的总障碍数在最大的情况下小于栅格数的$2/5$，保证在 $min(n,m)>1$ 的情况下可以输出
> 3. 调节2中的随机数上界可以生成障碍更加密集的迷宫，但有可能进入死循环
> 4. 除去一条直角路径全部置0，将上述的随机地图障碍数对应的栅格置1，将`p`对应的格子记录
> 5. 随机生成询问次数`k`和攻击时间，将攻击时间和栅格位置组合输出
> 6. 按照要求顺序输出全部内容

使用此方法可以保证最少一组直角边的解，在输出数组时无法通过程序获取最优解。
作为测试用例，本程序可以较好地满足测试需要。

## 结果示范
囿于篇幅，只展示小型迷宫的结果。

Input:
```shell
.\\app\\bin\\java edu.dsbbproj.nikoinmaze.Main terminal
5 6 20
0 1 0 0 0 0
0 1 0 1 0 0
0 0 0 1 0 0
0 1 1 0 0 0
0 0 0 0 0 0 
3
20 0 0
19 3 3
17 0 3
4
18 17 16 15
```
Output:
```shell
2 1 1 2 0 3 1 4 2 5 3 5 4 5
1 2 2 1 3 0 4 1 4 2 4 3 4 4 4 5
2 1 3 0 4 1 4 2 4 3 4 4 4 5
3 0 4 1 4 2 4 3 4 4 4 5
```

![alt text](.\\pic\\fig1.png "Figure 1")
![alt text](.\\pic\\fig2.png "Figure 2")

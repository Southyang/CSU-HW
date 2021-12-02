import matplotlib.pyplot as plt

# 定义文本框和箭头格式
# 决策节点
decisionNode = dict(boxstyle="sawtooth", fc="0.8")
# 叶节点
leafNode = dict(boxstyle="round4", fc="0.8")
# 箭头格式
arrow_args = dict(arrowstyle="<-")


# 绘制带箭头的注解
def plotNode(nodeTxt, centerPt, parentPt, nodeType):
    createPlot.ax1.annotate(nodeTxt, xy=parentPt, xycoords='axes fraction',
                            xytext=centerPt, textcoords='axes fraction',
                            va="center", ha="center", bbox=nodeType, arrowprops=arrow_args)


# 第一个版本的创建图函数
# def createPlot():
#     fig = plt.figure(1, facecolor='white')
#     fig.clf()
#     # 创建坐标轴
#     createPlot.ax1 = plt.subplot(111, frameon=False)
#     # 创建一个向量[标签名称，纵轴方向上的位移，水平方向上的位移，节点形式]
#     plotNode('a decision node', (0.5, 0.1), (0.1, 0.5), decisionNode)
#     plotNode('a leaf node', (0.8, 0.1), (0.3, 0.8), leafNode)
#     # 显示图
#     plt.show()


# 测试图
# createPlot()


# 获取叶节点的数目
def getNumLeafs(myTree):
    # 初始化叶节点数目为0
    numLeafs = 0
    # 获取头节点标签key值
    firstStr = list(myTree.keys())[0]
    # 获取头结点标签key对应的value子树
    secondDict = myTree[firstStr]
    # 遍历所有子树中的节点
    for key in secondDict.keys():
        # 如果子树中某一节点依旧是字典
        if type(secondDict[
                    key]).__name__ == 'dict':
            # 那么递归对子树的子树进行搜索
            numLeafs += getNumLeafs(secondDict[key])
        # 否则子树的节点是当前分支最深节点，而不是字典
        else:
            # 那么叶数目累加1
            numLeafs += 1
    # 返回累计的叶数目
    return numLeafs


# 获取树的层数深度
def getTreeDepth(myTree):
    # 初始化树深度为0
    maxDepth = 0
    # 获取树的头结点标签
    firstStr = list(myTree.keys())[0]
    # 获取头结点标签key对应的value子树
    secondDict = myTree[firstStr]
    # 遍历当前子树中所有的分支节点
    for key in secondDict.keys():
        # 如果分支是更深的字典
        if type(secondDict[
                    key]).__name__ == 'dict':
            # 那么当前深度累加1，继续深度遍历更深的子树
            thisDepth = 1 + getTreeDepth(secondDict[key])
        else:
            # 没有更深的子树，层数就是1
            thisDepth = 1
        # 如果当前深度大于此前最大深度，更新深度数据
        if thisDepth > maxDepth: maxDepth = thisDepth
    # 返回最大深度层数数据
    return maxDepth


# 预先存储树的信息，这里面有两个预设
def retrieveTree(i):
    listOfTrees = [{'no surfacing': {0: 'no', 1: {'flippers': {0: 'no', 1: 'yes'}}}},
                   {'no surfacing': {0: 'no', 1: {'flippers': {0: {'head': {0: 'no', 1: 'yes'}}, 1: 'no'}}}}
                   ]
    return listOfTrees[i]


# 在父子节点间添加文本信息
def plotMidText(cntrPt, parentPt, txtString):
    xMid = (parentPt[0] - cntrPt[0]) / 2.0 + cntrPt[0]
    yMid = (parentPt[1] - cntrPt[1]) / 2.0 + cntrPt[1]
    createPlot.ax1.text(xMid, yMid, txtString, va="center", ha="center", rotation=30)


# 计算宽与高
def plotTree(myTree, parentPt, nodeTxt):
    # 获取叶子数目
    numLeafs = getNumLeafs(myTree)
    # 获取树深度层数
    depth = getTreeDepth(myTree)
    # 获取树的头节点
    firstStr = list(myTree.keys())[0]
    cntrPt = (plotTree.xOff + (1.0 + float(numLeafs)) / 2.0 / plotTree.totalW, plotTree.yOff)
    plotMidText(cntrPt, parentPt, nodeTxt)
    plotNode(firstStr, cntrPt, parentPt, decisionNode)
    secondDict = myTree[firstStr]
    plotTree.yOff = plotTree.yOff - 1.0 / plotTree.totalD
    for key in secondDict.keys():
        if type(secondDict[
                    key]).__name__ == 'dict':
            plotTree(secondDict[key], cntrPt, str(key))
        else:
            plotTree.xOff = plotTree.xOff + 1.0 / plotTree.totalW
            plotNode(secondDict[key], (plotTree.xOff, plotTree.yOff), cntrPt, leafNode)
            plotMidText((plotTree.xOff, plotTree.yOff), cntrPt, str(key))
    plotTree.yOff = plotTree.yOff + 1.0 / plotTree.totalD


def createPlot(inTree):
    fig = plt.figure(1, facecolor='white')
    fig.clf()
    axprops = dict(xticks=[], yticks=[])
    createPlot.ax1 = plt.subplot(111, frameon=False, **axprops)
    # createPlot.ax1 = plt.subplot(111, frameon=False) #ticks for demo puropses
    plotTree.totalW = float(getNumLeafs(inTree))
    plotTree.totalD = float(getTreeDepth(inTree))
    plotTree.xOff = -0.5 / plotTree.totalW
    plotTree.yOff = 1.0
    plotTree(inTree, (0.5, 1.0), '')
    plt.show()
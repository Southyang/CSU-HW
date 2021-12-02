import numpy as np
import matplotlib.pyplot as plt
import scipy.ndimage

def plotData(X, y):
    # Find Indices of Positive and Negative Examples
    pos = np.where(y == 1)
    neg = np.where(y == 0)
    plt.scatter(X[pos,0], X[pos,1], c='b', label='1')
    plt.scatter(X[neg,0], X[neg,1], c='r', label='0')
    plt.legend()
    return plt

def mapFeature(X1, X2, degree=6):
    """
    特征映射函数，从X1和X2中映射出更多特征
    """
    newX = np.ones((X1.shape[0], sum(range(degree + 2))))    #初始化新的特征矩阵
    end = 1
    for i in range(1, degree + 1):
        for j in range(0, i+1):
            newX[:, end] = np.multiply(np.power(X1, i-j), np.power(X2, j))
            end = end + 1
    return newX

def predict_prob(X, theta):
    
    h = 1 / (1 + np.exp(-np.dot(X,theta)))
    classes = h.round()
    
    return classes

def plotDecisionBoundary(X, y, theta):

    plt.figure(figsize=(10, 6))
    plt.scatter(X[y == 0][:, 0], X[y == 0][:, 1], color='b', label='0')
    plt.scatter(X[y == 1][:, 0], X[y == 1][:, 1], color='r', label='1')
    plt.legend()
    x1_min, x1_max = X[:,0].min(), X[:,0].max(),
    x2_min, x2_max = X[:,1].min(), X[:,1].max(),
    xx1, xx2 = np.meshgrid(np.linspace(x1_min, x1_max,500), np.linspace(x2_min, x2_max,500))
    grid = np.c_[xx1.ravel(), xx2.ravel()]
    newgrid = mapFeature(grid[:, 0], grid[:, 1])
    probs = predict_prob(newgrid, theta).reshape(xx1.shape)
    plt.contour(xx1, xx2, probs, [0.5],linewidths=1, colors='black')
    plt.show()
    return plt
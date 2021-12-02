import os
import struct
import math
import numpy as np

def _change_one_hot_label(X):
    T = np.zeros((X.size, 10))
    for idx, row in enumerate(T):
        row[X[idx]] = 1
        
    return T

def load_mnist(path, normalize=True, flatten=True, one_hot_label=False):
    """Load MNIST data from `path`"""
    dataset = dict()
    for kind in ['train', 'test']:
        labels_path = os.path.join(path,
                                '%s-labels.idx1-ubyte'
                                % kind)
        images_path = os.path.join(path,
                                '%s-images.idx3-ubyte'
                                % kind)

        with open(labels_path, 'rb') as lbpath:
            magic, n = struct.unpack('>II', lbpath.read(8))
            labels = np.fromfile(lbpath, dtype=np.uint8)

        with open(images_path, 'rb') as imgpath:
            magic, num, rows, cols = struct.unpack('>IIII',
                                                imgpath.read(16))
            images = np.fromfile(imgpath,
                                dtype=np.uint8).reshape(len(labels), 784)

        dataset["%s_label" % kind] = labels
        dataset["%s_img" % kind] = images
    
    if normalize:
        for key in ('train_img', 'test_img'):
            dataset[key] = dataset[key].astype(np.float32)
            dataset[key] /= 255.0
            
    if one_hot_label:
        dataset['train_label'] = _change_one_hot_label(dataset['train_label'])
        dataset['test_label'] = _change_one_hot_label(dataset['test_label'])
    
    if not flatten:
         for key in ('train_img', 'test_img'):
            dataset[key] = dataset[key].reshape(-1, 1, 28, 28)
    
    return (dataset['train_img'], dataset['train_label']), (dataset['test_img'], dataset['test_label']) 

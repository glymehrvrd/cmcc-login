from pybrain.tools.shortcuts import buildNetwork
from pybrain.datasets import SupervisedDataSet
from pybrain.supervised.trainers import BackpropTrainer

import requests

import numpy as np
from detector import *

def getCapcha():
    # get capcha
    url_capcha = 'http://218.200.239.185:8888/portalserver/user/randomimage'
    r = requests.get(url_capcha)
    return r.content

def build_target_vector(val, length):
    vec = np.zeros(10, dtype=np.int32)
    vec[val - 1] = 1
    return vec

nn = buildNetwork(220, 220, 10)
ds = SupervisedDataSet(220, 10)

for i in xrange(200):
    img_arr = jpeg2ndarray(getCapcha())
    if isinstance(img_arr, np.ndarray):
        img_arr = rgb2bw(img_arr)
        for spice in splitImageHorizontally(img_arr, 4):
            num = detect(spice)
            ds.addSample(spice.ravel(), build_target_vector(num, 10))

print 'start training'
trainer = BackpropTrainer(nn,ds)
trainer.train()

for i in xrange(1):
    img_arr = jpeg2ndarray(getCapcha())
    if isinstance(img_arr, np.ndarray):
        img_arr = rgb2bw(img_arr)
        for spice in splitImageHorizontally(img_arr, 4):
            num = detect(spice)
            print num
            print nn.activate(spice.ravel())

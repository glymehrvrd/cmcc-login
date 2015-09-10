from pybrain.datasets import SupervisedDataSet
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.structure import FeedForwardNetwork, FullConnection
from pybrain.structure import LinearLayer, SigmoidLayer, SoftmaxLayer, BiasUnit

import os
import cPickle
import requests

import numpy as np
from detector import *


def getCapcha():
    # get capcha
    url_capcha = 'http://218.200.239.185:8888/portalserver/user/randomimage'
    r = requests.get(url_capcha)
    return r.content


def build_network():
    inlayer = LinearLayer(220)
    hiddenlayer = SigmoidLayer(20)
    outlayer = SigmoidLayer(10)
    bias = BiasUnit()

    nn = FeedForwardNetwork()

    nn.addModule(bias)
    nn.addInputModule(inlayer)
    nn.addModule(hiddenlayer)
    nn.addOutputModule(outlayer)

    nn.addConnection(FullConnection(inlayer, hiddenlayer))
    nn.addConnection(FullConnection(hiddenlayer, outlayer))
    nn.addConnection(FullConnection(bias, hiddenlayer))
    nn.addConnection(FullConnection(bias, outlayer))

    nn.sortModules()
    return nn


def inspect_network(nn):
    for mod in nn.modules:
        print("Module:", mod.name)
        if mod.paramdim > 0:
            print("--parameters:", mod.params)
        for conn in nn.connections[mod]:
            print("-connection to", conn.outmod.name)
            if conn.paramdim > 0:
                print("- parameters", conn.params)
                print conn.params.shape
                np.savetxt('a.txt', conn.params, delimiter=' ', newline='\n')
        if hasattr(nn, "recurrentConns"):
            print("Recurrent connections")
            for conn in nn.recurrentConns:
                print("-", conn.inmod.name, " to", conn.outmod.name)
                if conn.paramdim > 0:
                    print("- parameters", conn.params)


def build_target_vector(val, length):
    vec = np.zeros(10, dtype=np.int32)
    vec[val - 1] = 1
    return vec

# write training data to txt file
# f=open('output.txt','w')
# fy=open('output_y.txt','w')
# for i in xrange(250):
#     img_arr = jpeg2ndarray(getCapcha())
#     if isinstance(img_arr, np.ndarray):
#         img_arr = rgb2bw(img_arr)
#         for spice in splitImageHorizontally(img_arr, 4):  
#             f.write(','.join(map(str, spice.ravel('F'))))
#             f.write('\n')
#             fy.write(str(detect(spice))+',')


# load existed network if possible
if not os.path.exists('nn.pickle'):
    nn = build_network()
    print 'building new neural network'
else:
    nn = cPickle.load(open('nn.pickle'))
    print 'using existing neural network'

print 'adding samples'
ds = SupervisedDataSet(220, 10)
for i in xrange(200):
    img_arr = jpeg2ndarray(getCapcha())
    if isinstance(img_arr, np.ndarray):
        img_arr = rgb2bw(img_arr)
        for spice in splitImageHorizontally(img_arr, 4):
            num = nndetect(spice)
            ds.addSample(spice.ravel(), build_target_vector(num, 10))

print 'start training'
trainer = BackpropTrainer(nn, ds)
print trainer.trainUntilConvergence(maxEpochs=500,verbose=True)
print 'train complete'

# write trained network to file
cPickle.dump(nn, open('nn.pickle', 'w'))

# test network result
for i in xrange(1):
    img_arr = jpeg2ndarray(getCapcha())
    if isinstance(img_arr, np.ndarray):
        img_arr = rgb2bw(img_arr)
        for spice in splitImageHorizontally(img_arr, 4):
            num = nndetect(spice)
            print 'num is ', num
            print nn.activate(spice.ravel())
            print 'neural network result: ', np.argmax(nn.activate(spice.ravel()))

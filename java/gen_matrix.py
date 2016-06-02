#!/usr/bin/python

from scipy.io import loadmat


def print_matrix(m,num):
    for i in xrange(m.shape[0]):
        for j in xrange(m.shape[1]):
            print 'Theta%d[%d][%d]=%f;' % (num, i, j, m[i, j])

tmp = loadmat(r'd:\dell\MyPrograms\cmcc-login\theta.mat')
t1 = tmp['Theta1'].T
t2 = tmp['Theta2'].T

print_matrix(t1, 1)
print_matrix(t2, 2)

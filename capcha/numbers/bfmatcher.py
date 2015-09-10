from scipy import ndimage
from scipy import misc

img = []
for i in range(4):
    img.append(misc.imread(str(i+1)+'.jpg'))

print img

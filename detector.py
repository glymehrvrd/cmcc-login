import numpy as np
from PIL import Image
import StringIO


def jpeg2ndarray(jpgdata):
    if jpgdata:
        strio = StringIO.StringIO(jpgdata)
        img = Image.open(strio)
        return np.asarray(img)
    else:
        return False


def readBMP(path):
    '''
    Read bmp image into ndarray
    '''
    im = Image.open(path)
    w, h = im.size
    arr = np.ndarray([h, w], dtype=bool)
    for i in range(h):
        for j in range(w):
            arr[i, j] = im.getpixel((j, i))
    return arr


def splitImageHorizontally(img, num):
    h, w = img.shape
    for i in range(num):
        yield img[:, i * w / 4:(i + 1) * w / 4]


def rgb2bw(img):
    '''
    Convert RGB image to Black White image
    '''
    img = np.dot(img[..., :3], [0.299, 0.587, 0.144])

    img[img > 125] = 255
    img[img <= 125] = 0

    return img


def detect(img):
    '''
    Detect single digit.
    '''
    sim = np.zeros([10, 1], dtype=int)
    for i in range(10):
        sim[i] = (np.logical_or(img, number_template[i]) == 0).ravel().nonzero()[
            0].shape[0]
    return sim.argmax(0)[0]


def readVerifyCode(jpgdata):
    nums = ''
    img_arr = jpeg2ndarray(jpgdata)
    if isinstance(img_arr, np.ndarray):
        img_arr = rgb2bw(img_arr)
        for spice in splitImageHorizontally(img_arr, 4):
            nums = nums + str(detect(spice))
    return nums

# init number template
number_template = []
for i in range(10):
    number_template.append(
        readBMP('number_template/' + str(i) + '.bmp'))

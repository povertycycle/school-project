from PIL import Image
import numpy as np
from matplotlib import pyplot as plt
import sys
import math
import pickle
import glob
import os

# test run = python TestScript.py #image.png* to check if an image is
# pedestrian or non-pedestrian
# to compute individual image, use the first line (1)
# to compute multiple images, use the second commented lines (2)

def hog(image_file):
    bin = []
    image = Image.open(image_file).convert('L')
    (R, H) = image.size
    (r, h) = (64, 128)
    cropped = image.crop(((R-r)/2, (H-h)/2, (R+r)/2, (H+h)/2))
    width, height = cropped.size
    pix = cropped.load()
    for x in range(0, 56, 8):
        for y in range(0, 120, 8):
            bin1 = compute_histogram(x,y, width, height, pix)
            bin2 = compute_histogram(x+8,y, width, height, pix)
            bin3 = compute_histogram(x, y+8, width, height, pix)
            bin4 = compute_histogram(x+8,y+8, width, height, pix)
            bin16x16 = compute_norm16x16(bin1,bin2,bin3,bin4)
            for item in bin16x16:
                bin.append(item)
    return bin

def compute_norm16x16(bin1, bin2, bin3, bin4):
    total = 0
    for x in bin1:
        total = total + x**2
    for x in bin2:
        total = total + x**2
    for x in bin3:
        total = total + x**2
    for x in bin4:
        total = total + x**2
    total = math.sqrt(total)
    if total == 0:
        total = 0.1
    for i in range(9):
        bin1[i] = bin1[i]/total
        bin2[i] = bin2[i]/total
        bin3[i] = bin3[i]/total
        bin4[i] = bin4[i]/total
    concat = []
    for x in bin1:
        concat.append(x)
    for x in bin2:
        concat.append(x)
    for x in bin3:
        concat.append(x)
    for x in bin4:
        concat.append(x)
    return concat

def compute_histogram(left, top, width, height, pix):
    # 8x8 bin of 9 value
    currentbin = [0,0,0,0,0,0,0,0,0]
    for x in range(left, left+7):
        for y in range(top, top+7):
            Gm, Go = magnitude_orientation(x, y, width, height, pix)
            if (0 <= Go <= 20):
                lVal = ((20 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[0] = currentbin[0] + lVal
                currentbin[1] = currentbin[1] + rVal
            elif (20 <= Go <= 40):
                lVal = ((40 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[1] = currentbin[1] + lVal
                currentbin[2] = currentbin[2] + rVal
            elif (40 <= Go <= 60):
                lVal = ((60 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[2] = currentbin[2] + lVal
                currentbin[3] = currentbin[3] + rVal
            elif (60 <= Go <= 80):
                lVal = ((80 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[3] = currentbin[3] + lVal
                currentbin[4] = currentbin[4] + rVal
            elif (80 <= Go <= 100):
                lVal = ((100 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[4] = currentbin[4] + lVal
                currentbin[5] = currentbin[5] + rVal
            elif (100 <= Go <= 120):
                lVal = ((120 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[5] = currentbin[5] + lVal
                currentbin[6] = currentbin[6] + rVal
            elif (120 <= Go <= 140):
                lVal = ((140 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[6] = currentbin[6] + lVal
                currentbin[7] = currentbin[7] + rVal
            elif (140 <= Go <= 160):
                lVal = ((160 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[7] = currentbin[7] + lVal
                currentbin[8] = currentbin[8] + rVal
            elif (160 <= Go):
                lVal = ((180 - Go) / 20) * Gm
                rVal = Gm - lVal
                currentbin[8] = currentbin[8] + lVal
                currentbin[0] = currentbin[0] + rVal

    return currentbin
# Compute magnitude and orientation of a pixel
def magnitude_orientation(i, j, width, height, pix):
    left = 0
    right = 0
    top = 0
    bottom = 0
    if (i-1) >= 0:
        left = pix[i-1,j]
    if (i+1) <= width:
        right = pix[i+1,j]
    if (j-1) >= 0:
        bottom = pix[i,j-1]
    if (j+1) <= height:
        top = pix[i,j+1]
    Gx = right - left
    Gy = bottom - top

    Gm = math.sqrt((Gx**2) + (Gy**2))
    Go = (math.atan2(Gy, Gx) * 180 / np.pi) % 360
    if Go > 180:
        Go = Go - 180

    return Gm, Go

def dot(x, y):
    tot = 0.0
    for (a, b) in zip(x, y):
        tot = tot + (a * b)
    return tot

def compute_pedestrian(w, datapoints):
#    for x in datapoints:
        x = datapoints
        if dot(w, x) < 1.85:
            print("not pedestrian")
        elif dot(w, x) >= 1.85:
            print("pedestrian")
        print(dot(w,x))

#def compute_accuracy(weights, xs, labels):
#    correct = 0
#    total = 0
#    n = 0
#    p = 0
#    for (x, y) in zip(xs, labels):
#        print(dot(weights, x))
#        if dot(weights, x) < 1.85 and y == -1:
#            n += 1
#            correct = correct + 1
#        elif dot(weights, x) >= 1.85 and y == 1:
#            p += 1
#            correct = correct + 1
#        total = total + 1
#    print(n, p)
#    print(correct/total * 100)
#    return correct, total

perc = pickle.load(open("perception.pkl", "rb"))
weights = perc['weights']

# (1) to compute image
image_file_name = sys.argv[1]
hdata = hog(image_file_name)
hdata.append(1)

# (2) to compute multiple image in files
#hw3_dataset = sys.argv[1]
#image_list = hw3_dataset + "/*/*.png"
#paths = glob.glob(image_list)
#datapoints = []
#hdatapoints = []
#labels = []
#for item in paths:
#    path = os.path.split(item)
#    type = os.path.basename(path[0])
#    hdata = hog(item)
#    datapoints.append(hdata)
#    if (type == "pos"):
#        labels.append(1)
#    elif (type == "neg"):
#        labels.append(-1)

#for item in datapoints:
#    item.append(1)
#    hdatapoints.append(item)

compute_pedestrian(weights, hdata)
#compute_accuracy(weights, hdatapoints, labels)

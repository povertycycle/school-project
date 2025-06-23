from PIL import Image
import numpy as np
from matplotlib import pyplot as plt
import sys
import math

image_file_name = sys.argv[1]
sigma_square = sys.argv[2]
k_value = sys.argv[3]
image_file_output_name = sys.argv[4]

image_input = Image.open(image_file_name).convert('RGB')

def gaussian_matrix(w, h, pix, i, j, sigma, k):
    s = k
    r, g, b = pix[i, j]
    sigma_d = sigma
    wout = 0
    rout = 0
    gout = 0
    bout = 0
    for k in range(max(0, i - s), min(w, i + s + 1)):
        for l in range(max(0, j - s), min(h, j + s + 1)):
            d = math.exp(- ((i - k) ** 2 + (j - l) ** 2) / (2 * sigma_d))
            r2, g2, b2 = pix[k, l]
            w = d
            wout = wout + w
            rout = rout + w * r2
            gout = gout + w * g2
            bout = bout + w * b2
    rout = rout / wout
    gout = gout / wout
    bout = bout / wout

    return (int(round(rout)), int(round(gout)), int(round(bout)))

def run_gaussian(image, k, sigma):
    copy = image
    width, height = copy.size
    pixel_image = image.load()
    pixel = copy.load()
    for i in range(width):
        for j in range(height):
            (r, g, b) = gaussian_matrix(width, height, pixel, i, j, sigma, k)
            pixel[i, j] = (r, g, b)

    return copy

def CannyEdgeDetector(image, k, sigma):
    # apply gaussian filter to the image
    copy_image = image
    run_gaussian(copy_image, k, sigma)
    width, height = copy_image.size
    pix = copy_image.load()
    for i in range(width):
        for j in range(height):
            a1,a2 = Sobel(pix, i-1, j+1, width, height)
            b1,b2 = Sobel(pix, i, j+1, width, height)
            c1,c2 = Sobel(pix, i+1, j+1, width, height)
            d1,d2 = Sobel(pix, i-1, j, width, height)
            e1,e2 = Sobel(pix, i, j, width, height)
            f1,f2 = Sobel(pix, i+1, j, width, height)
            h1,h2 = Sobel(pix, i-1, j-1, width, height)
            k1,k2 = Sobel(pix, i, j-1, width, height)
            l1,l2 = Sobel(pix, i+1, j-1, width, height)
            if e2 == 0:
                if (e1 >= b1) and (e1 >= k1):
                    pix[i,j] = (e1,e1,e1)
                else:
                    pix[i,j] = (0,0,0)
            elif e2 == 90:
                if (e1 >= d1) and (e1 >= f1):
                    pix[i,j] = (e1,e1,e1)
                else:
                    pix[i,j] = (0,0,0)
            elif e2 == 135:
                if (e1 >= h1) and (e1 >= c1):
                    pix[i,j] = (e1,e1,e1)
                else:
                    pix[i,j] = (0,0,0)
            elif e2 == 45:
                if (e1 >= a1) and (e1 >= l1):
                    pix[i,j] = (e1,e1,e1)
                else:
                    pix[i,j] = (0,0,0)

    return copy_image

def Sobel(pix, i, j, width, height):
    Gx = Gx_Sobel(pix, i, j, width, height)
    Gy = Gy_Sobel(pix, i, j, width, height)
    g = math.sqrt((Gx**2) + (Gy**2))
    # normalize
    g = g / 4328 * 255
    g = int(g)
    theta = math.atan2(Gy,Gx)
    # change to degrees
    theta = math.degrees(theta)
    theta = rounding(theta)
    return (g, theta)

def Gx_Sobel(pix, i, j, width, height):
    sum = 0
    r1,g1,b1 = boundary(i-1,j+1, width, height, pix)
    r2,g2,b2 = boundary(i+1,j+1, width, height, pix)
    r3,g3,b3 = boundary(i-1,j, width, height, pix)
    r4,g4,b4 = boundary(i+1,j, width, height, pix)
    r5,g5,b5 = boundary(i-1,j-1, width, height, pix)
    r6,g6,b6 = boundary(i+1,j-1, width, height, pix)
    rtot = -r1 + r2 - 2*r3 + 2*r4 - r5 + r6
    gtot = -g1 + g2 - 2*g3 + 2*g4 - g5 + g6
    btot = -b1 + b2 - 2*b3 + 2*b4 - b5 + b6

    return (0.3*rtot) + (0.59*gtot) + (0.11*btot)

def Gy_Sobel(pix, i, j, width, height):
    sum = 0
    r1,g1,b1 = boundary(i-1,j+1, width, height, pix)
    r2,g2,b2 = boundary(i,j+1, width, height, pix)
    r3,g3,b3 = boundary(i+1,j+1, width, height, pix)
    r4,g4,b4 = boundary(i-1,j-1, width, height, pix)
    r5,g5,b5 = boundary(i,j-1, width, height, pix)
    r6,g6,b6 = boundary(i+1,j-1, width, height, pix)
    rtot = -r1 - 2*r2 - r3 + r4 + 2*r5 + r6
    gtot = -g1 - 2*g2 - g3 + g4 + 2*g5 + g6
    btot = -b1 - 2*b2 - b3 + b4 + 2*b5 + b6

    return (0.3*rtot) + (0.59*gtot) + (0.11*btot)

def boundary(a,b, width, height, pix):
    if a < 0 or b < 0 or a > width-1 or b > height-1:
        return (0,0,0)
    else:
        return pix[a,b]

def rounding(angle):
    new_angle = 0
    if (0 <= angle < 22.5) or (157.5 <= angle <= 180):
        new_angle = 0
    elif 22.5 <= angle < 67.5:
        new_angle = 45
    elif 67.5 <= angle < 112.5:
        new_angle = 90
    elif 112.5 <= angle < 157.5:
        new_angle = 135
    return new_angle

output_image = CannyEdgeDetector(image_input, int(k_value), float(sigma_square))
output_image.save(image_file_output_name)
